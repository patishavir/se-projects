package oz.infra.ssh.jsch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.ssh.SshParams;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.infra.thread.ThreadUtils;
import oz.infra.varargs.VarArgsUtils;

public class JschUtils {
	private static final String CHANNEL_TYPE_EXEC = "exec";
	private static final String CHANNEL_TYPE_SFTP = "sftp";
	public static Logger logger = JulUtils.getLogger();

	private static int checkAck(final InputStream in) throws IOException {
		int b = in.read();
		/*
		 * b may be: 0 for success, 1 for error, 2 for fatal error, -1
		 */
		if (b == 0) {
			return b;
		}
		if (b == -1) {
			return b;
		}
		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			if (b == 1) { // error
				logger.warning(sb.toString());
			}
			if (b == 2) { // fatal error
				logger.severe(sb.toString());
			}
		}
		return b;
	}

	public static SystemCommandExecutorResponse exec(final Properties properties, final Level... levels) {
		Level level = VarArgsUtils.getMyArg(Level.INFO, levels);
		String command = properties.getProperty(SshParams.COMMAND_LINE);
		String propertiesString = PropertiesUtils.getAsDelimitedString(properties, OzConstants.BLANK);
		logger.log(level, propertiesString);
		int returnCode = OzConstants.EXIT_STATUS_OK;
		String stdout = null;
		String stderr = null;
		StopWatch stopWatch = new StopWatch();
		String completionMessage = null;
		try {
			Session session = getSession(properties);
			ChannelExec channelExec = (ChannelExec) session.openChannel(CHANNEL_TYPE_EXEC);
			InputStream err = channelExec.getErrStream();
			InputStream out = channelExec.getInputStream();
			// channelExec.setErrStream(System.err);
			channelExec.setCommand(command);
			channelExec.connect();
			stdout = getSteamContents(out);
			stderr = getSteamContents(err);
			channelExec.disconnect();
			session.disconnect();
			returnCode = getExitStatus(channelExec);

		} catch (Exception ex) {
			logger.warning(PropertiesUtils.getAsDelimitedString(properties, OzConstants.BLANK));
			ExceptionUtils.printMessageAndStackTrace(ex);
			returnCode = OzConstants.EXIT_STATUS_ABNORMAL;
		}
		SystemCommandExecutorResponse scer = new SystemCommandExecutorResponse(returnCode, stdout, stderr);
		completionMessage = StringUtils.concat("Execution on ", properties.getProperty(SshParams.SERVER), " has completed in ",
				stopWatch.getElapsedTimeString(), " user: ", properties.getProperty(SshParams.USER_NAME), SystemUtils.LINE_SEPARATOR,
				scer.getExecutorResponse());
		logger.log(level, completionMessage);
		return scer;
	}

	public static SystemCommandExecutorResponse exec(final String command, final String remoteServer, final Level... levels) {
		return exec(getRemoteExecProperties(command, remoteServer), levels);
	}

	private static int getExitStatus(final Channel channel) {
		while (!channel.isClosed()) {
			ThreadUtils.sleep(OzConstants.INT_200, Level.INFO);
		}
		return channel.getExitStatus();
	}

	private static String getPrivateKey(final Properties properties) {
		String privateKey = properties.getProperty(SshParams.PRIVATE_KEY);
		if (privateKey == null) {
			privateKey = StringUtils.concat(SystemUtils.getSystemProperty(SystemPropertiesUtils.SYSTEM_PROPERTY_USER_HOME), File.separator,
					SshParams.DEFAULT_SSH_FOLDER, File.separator, SshParams.DEFAULT_PRIVATE_KEY);
		}
		return privateKey;
	}

	public static Properties getRemoteExecProperties(final String command, final String remoteServer) {
		Properties properties = new Properties();
		properties.put(SshParams.COMMAND_LINE, command);
		properties.put(SshParams.SERVER, remoteServer);
		properties.put(SshParams.PORT, SshParams.DEFAULT_SSH_PORT);
		properties.put(SshParams.USER_NAME, SshParams.ROOT);
		return properties;
	}

	private static Session getSession(final Properties properties) throws JSchException {
		logger.finest(PropertiesUtils.getAsDelimitedString(properties));
		StopWatch stopWatch = new StopWatch();
		Session session = null;
		JSch jsch = new JSch();
		String user = properties.getProperty(SshParams.USER_NAME);
		String password = properties.getProperty(SshParams.PASSWORD);

		String server = properties.getProperty(SshParams.SERVER);
		int port = SshParams.DEFAULT_SSH_PORT;
		String portString = properties.getProperty(SshParams.PORT);
		if (portString != null) {
			port = Integer.parseInt(portString);
		}
		String privateKey = getPrivateKey(properties);
		String knownHosts = properties.getProperty(SshParams.KNOWN_HOSTS);
		if (knownHosts == null) {
			knownHosts = StringUtils.concat(SystemUtils.getSystemProperty(SystemPropertiesUtils.SYSTEM_PROPERTY_USER_HOME), File.separator,
					SshParams.DEFAULT_SSH_FOLDER, File.separator, SshParams.DEFAULT_KNOWN_HOSTS);
		}
		jsch.addIdentity(privateKey);
		jsch.setKnownHosts(knownHosts);
		logger.finest("identity added ...");
		logger.finest(StringUtils.concat("privateKey: ", privateKey, " knownHosts: ", knownHosts, " user: ", user, " server: ", server, " port:",
				String.valueOf(port)));
		session = jsch.getSession(user, server, port);
		if (password != null) {
			session.setPassword(password);
		}
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", OzConstants.YES);
		session.setConfig(config);
		logger.finest("session created.");
		session.connect();
		// stopWatch.getElapsedTime("session connected in");
		return session;
	}

	private static String getSteamContents(final InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		String msg = null;
		while ((msg = bufferedReader.readLine()) != null) {
			sb.append(SystemUtils.LINE_SEPARATOR);
			sb.append(msg);
		}
		return sb.toString();
	}

	private static void printLog(final BufferedReader in) throws IOException {
		String msg = null;
		while ((msg = in.readLine()) != null) {
			logger.info(msg);
		}
	}

	public static int scpFrom(final Properties properties) {
		int returnCode = OzConstants.EXIT_STATUS_OK;
		StopWatch stopWatch = new StopWatch();
		FileOutputStream fos = null;
		String server = properties.getProperty(SshParams.SERVER);
		try {
			Session session = getSession(properties);
			String remoteFile = properties.getProperty(SshParams.SOURCE_FILE);
			String localFile = properties.getProperty(SshParams.DESTINATION_FILE);
			String prefix = null;
			if (new File(localFile).isDirectory()) {
				prefix = localFile + File.separator;
			}

			// exec 'scp -f rfile' remotely
			String scpCommand = "scp -f " + remoteFile;
			Channel channel = session.openChannel(CHANNEL_TYPE_EXEC);
			ChannelExec channelExec = (ChannelExec) channel;
			channelExec.setCommand(scpCommand);

			// get I/O streams for remote scp
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();
			channel.connect();
			byte[] buf = new byte[1024];
			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();

			while (true) {
				int ackCode1 = checkAck(in);
				if (ackCode1 != 'C') {
					break;
				}

				// read '0644 '
				in.read(buf, 0, 5);

				long filesize = 0L;
				while (true) {
					if (in.read(buf, 0, 1) < 0) {
						// error
						break;
					}
					if (buf[0] == ' ')
						break;
					filesize = filesize * 10L + (long) (buf[0] - '0');
				}

				String file = null;
				for (int i = 0;; i++) {
					in.read(buf, i, 1);
					if (buf[i] == (byte) 0x0a) {
						file = new String(buf, 0, i);
						break;
					}
				}

				logger.finest(StringUtils.concat(" file=" + file, " filesize=" + String.valueOf(filesize)));

				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();

				// read a content of lfile
				fos = new FileOutputStream(prefix == null ? localFile : prefix + file);
				int foo;
				while (true) {
					if (buf.length < filesize)
						foo = buf.length;
					else
						foo = (int) filesize;
					foo = in.read(buf, 0, foo);
					if (foo < 0) {
						// error
						break;
					}
					fos.write(buf, 0, foo);
					filesize -= foo;
					if (filesize == 0L)
						break;
				}
				fos.close();
				fos = null;
				int ackCode2 = checkAck(in);
				if (ackCode2 != 0) {
					logger.warning(StringUtils.concat("copy failed. ackCode: ", String.valueOf(ackCode2)));
					return ackCode2;
				}
				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();
			}
			channel.disconnect();
			session.disconnect();
			returnCode = getExitStatus(channel);
			stopWatch.logElapsedTimeMessage(
					StringUtils.concat("server: ", server, "  ", scpCommand, " to ", localFile, " operation has completed in"));
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			returnCode = OzConstants.EXIT_STATUS_ABNORMAL;
			try {
				if (fos != null)
					fos.close();
			} catch (Exception ee) {
				ExceptionUtils.printMessageAndStackTrace(ex);
				returnCode = OzConstants.EXIT_STATUS_ABNORMAL;
			}
		}
		return returnCode;
	}

	public static int scpTo(final Properties properties) {
		int returnCode = OzConstants.EXIT_STATUS_OK;
		StopWatch stopWatch = new StopWatch();
		String sourceFilePath = properties.getProperty(SshParams.SOURCE_FILE);
		String server = properties.getProperty(SshParams.SERVER);
		FileInputStream fis = null;
		try {
			File sourceFile = new File(sourceFilePath);
			String lastModifiedString = DateTimeUtils.formatTime(sourceFile.lastModified(), DateTimeUtils.DATE_FORMAT_ddMMyyyy_HHmmssSSS);
			String logMessage = StringUtils.concat(SystemUtils.LINE_SEPARATOR, "copy of ", sourceFilePath, " last modified on ", lastModifiedString,
					" to ", properties.get(SshParams.DESTINATION_FILE).toString(), " on ", server);
			Session session = getSession(properties);
			// exec 'scp -t rfile' remotely
			String scpCommand = "scp -p -t " + properties.getProperty(SshParams.DESTINATION_FILE);
			Channel channel = session.openChannel(CHANNEL_TYPE_EXEC);
			ChannelExec channelExec = (ChannelExec) channel;
			channelExec.setCommand(scpCommand);
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();
			channel.connect();
			int ackCode = checkAck(in);
			if (ackCode == 0) {
				logger.finest(StringUtils.concat("connect to ", server, " has succeeded."));
			} else {
				logger.warning(StringUtils.concat("connect to ", server, " has failed."));
				returnCode = OzConstants.EXIT_STATUS_ABNORMAL;
				return ackCode;
			}
			// send "C0644 filesize filename", where filename should not include
			// '/'
			long fileSize = sourceFile.length();
			String command = "C0644 " + fileSize + " ";
			if (sourceFilePath.lastIndexOf(File.separator) > 0) {
				command += sourceFilePath.substring(sourceFilePath.lastIndexOf(File.separator) + 1);
			} else {
				command += sourceFilePath;
			}
			command += "\n";
			logger.finest(StringUtils.concat("command:", command));
			out.write(command.getBytes());
			out.flush();
			ackCode = checkAck(in);

			if (ackCode == 0) {
				logger.finest(StringUtils.concat(logMessage, " has succeeded. Contents. length: ", String.valueOf(fileSize)));
			} else {
				logger.warning(StringUtils.concat(logMessage, " has failed."));
				returnCode = OzConstants.EXIT_STATUS_ABNORMAL;
				return ackCode;
			}

			fis = new FileInputStream(sourceFilePath);
			byte[] buf = new byte[1024];
			while (true) {
				int len = fis.read(buf, 0, buf.length);
				if (len <= 0)
					break;
				out.write(buf, 0, len); // out.flush();
			}
			fis.close();
			fis = null;
			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
			ackCode = checkAck(in);
			if (ackCode == 0) {
				logger.finest(StringUtils.concat(logMessage, " has succeeded. Contents. length: ", String.valueOf(fileSize)));
			} else {
				logger.warning(StringUtils.concat(logMessage, " has failed."));
				return ackCode;
			}
			out.close();
			channelExec.disconnect();
			session.disconnect();
			// returnCode = getExitStatus(channelExec);
			String message = StringUtils.concat(logMessage, " operation completed successfully in ", stopWatch.getElapsedTimeString(),
					" Contents length: ", String.valueOf(fileSize));
			logger.info(message);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			logger.warning(PropertiesUtils.getAsDelimitedString(properties));
			returnCode = OzConstants.EXIT_STATUS_ABNORMAL;
			try {
				if (fis != null)
					fis.close();
			} catch (Exception ex1) {
				ExceptionUtils.printMessageAndStackTrace(ex);
				returnCode = OzConstants.EXIT_STATUS_ABNORMAL;
			}
		}
		return returnCode;
	}

	public static int scpTo(final String propertiesFilePath, final String localFilePath) {
		Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		properties.setProperty(SshParams.SOURCE_FILE, localFilePath);
		return scpTo(properties);
	}

	public static int sftp(final Properties properties) {
		int returnCode = OzConstants.EXIT_STATUS_OK;
		StopWatch stopWatch = new StopWatch();
		try {
			Session session = getSession(properties);
			Channel channel = session.openChannel(CHANNEL_TYPE_SFTP);
			channel.setInputStream(System.in);
			channel.setOutputStream(System.out);
			channel.connect();
			logger.finest("sftp channel connected....");
			ChannelSftp channelSftp = (ChannelSftp) channel;
			String sourceFile = properties.getProperty(SshParams.SOURCE_FILE);
			String destinationFile = properties.getProperty(SshParams.DESTINATION_FILE);
			channelSftp.put(sourceFile, destinationFile);
			channelSftp.exit();
			session.disconnect();
			returnCode = getExitStatus(channelSftp);
			stopWatch.logElapsedTimeMessage(StringUtils.concat("sftp operation ", sourceFile, " to ", destinationFile, " has completed in"));
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			returnCode = OzConstants.EXIT_STATUS_ABNORMAL;
		}
		return returnCode;
	}
}
