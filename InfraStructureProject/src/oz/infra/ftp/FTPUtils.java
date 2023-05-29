package oz.infra.ftp;

/*
 * Copyright 2001-2005 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;

import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class FTPUtils {
	private FTPClient myFtpClient = new FTPClient();

	private boolean downloadFile = false;

	private boolean binaryTransfer = false;

	private String server;

	private String username;

	private String password;

	private String remoteFiles;

	private String localFiles;

	private static Logger logger = JulUtils.getLogger();

	public static final void main(String[] args) {
		FTPUtils ftpUtils = new FTPUtils();
		ftpUtils.processFTP(args);
	}

	private void processFTP(final String[] args) {
		String[] remoteFileArray;
		String[] localFileArray;
		if (args.length == 0) {
			terminate("No parameters specified. Processing aborted!");
		}

		for (int i = 0; i < args.length; i++) {

			System.out.flush();
			processParameters(args[i]);
			if (!myFtpClient.isConnected()) {
				connrect2Server();
			}

			remoteFileArray = remoteFiles.split(",");
			localFileArray = localFiles.split(",");
			if (localFileArray.length != remoteFileArray.length) {
				terminate("Number of remote files should be equal to number of local files.\nProcessing aborted!");
			}

			for (int j = 0; j < remoteFileArray.length; j++) {
				processFile(
						StringUtils
								.substituteEnvironmentVariables(localFileArray[j]),
						StringUtils
								.substituteEnvironmentVariables(remoteFileArray[j]));
			}
			if (myFtpClient.isConnected()) {
				try {
					myFtpClient.logout();
					myFtpClient.disconnect();
				} catch (IOException ex) {
					logger.warning(ex.getMessage());
					// do nothing
				}
			}
		}
	}

	private void processParameters(final String ftpPropertiesPath) {
		logger.info("Processing proprties file: " + ftpPropertiesPath
				+ System.getProperty("line.separator"));
		Properties ftpProperties = new Properties();
		try {
			ftpProperties.load(new FileInputStream(ftpPropertiesPath));
		} catch (IOException ioex) {
			ioex.printStackTrace();
			terminate(ioex.getMessage());
		}

		server = ftpProperties.getProperty("server");
		username = ftpProperties.getProperty("username");
		password = ftpProperties.getProperty("password");
		remoteFiles = ftpProperties.getProperty("remoteFiles");
		localFiles = ftpProperties.getProperty("localFiles");
		downloadFile = ftpProperties.getProperty("downloadFile")
				.equalsIgnoreCase("yes");
		binaryTransfer = ftpProperties.getProperty("binaryTransfer")
				.equalsIgnoreCase("yes");
		Enumeration ftpPropertiesEnumeration = ftpProperties.keys();
		for (; ftpPropertiesEnumeration.hasMoreElements();) {
			String key = (String) ftpPropertiesEnumeration.nextElement();
			logger.info(key + "=" + ftpProperties.getProperty(key));

		}

	}

	private void connrect2Server() {

		myFtpClient.addProtocolCommandListener(new PrintCommandListener(
				new PrintWriter(System.out)));

		try {
			int reply;
			myFtpClient.connect(server);
			logger.info("Connected to " + server + ".");

			// After connection attempt, you should check the reply code to
			// verify
			// success.
			reply = myFtpClient.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply)) {
				myFtpClient.disconnect();
				terminate("FTP server refused connection.");

			}
			if (!myFtpClient.login(username, password)) {
				myFtpClient.logout();
				terminate("Login to " + server + " user: " + username
						+ "  failed !");

			}

			System.out.println("Remote system is "
					+ myFtpClient.getSystemName());
		} catch (IOException e) {
			if (myFtpClient.isConnected()) {
				try {
					myFtpClient.disconnect();
				} catch (IOException f) {
					// do nothing
				}
			}

			e.printStackTrace();
			terminate("Could not connect to server.");
		}

	}

	private void processFile(final String localFile, final String remoteFile) {
		try {

			if (binaryTransfer)
				myFtpClient.setFileType(FTP.BINARY_FILE_TYPE);

			// Use passive mode as default because most of us are
			// behind firewalls these days.
			myFtpClient.enterLocalPassiveMode();

			if (!downloadFile) {
				InputStream input;

				input = new FileInputStream(localFile);

				myFtpClient.storeFile(remoteFile, input);

				input.close();
				logger.info(localFile + " has been uploaded to " + remoteFile);
			} else {
				OutputStream output;

				output = new FileOutputStream(localFile);

				myFtpClient.retrieveFile(remoteFile, output);

				output.close();
				logger.info(remoteFile + " has been downloaded to " + localFile);
			}

		} catch (FTPConnectionClosedException e) {
			e.printStackTrace();
			terminate("Server closed connection.");

		} catch (IOException e) {

			e.printStackTrace();
			terminate(e.getMessage());
		}

	}

	private void terminate(final String message) {
		logger.warning(message);
		logger.warning("Processing aborted!");
		System.exit(-1);
	}

}

//
// Unpaged (whole list) access on a Windows-NT server in a different time zone.
// (Note, since the NT Format uses numeric date formatting, language issues are
// irrelevant here).
//
// FTPClient f=FTPClient();
// FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
// conf.setTimeZoneId("America/Denver");
// f.configure(conf);
// f.connect(server);
// f.login(username, password);
// FTPFile[] files = listFiles(directory);

// Unpaged (whole list) access on a Windows-NT server in a different time zone
// but which has been configured to use a unix-style listing format.
// FTPClient f=FTPClient();
// FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
// conf.setTimeZoneId("America/Denver");
// f.configure(conf);
// f.connect(server);
// f.login(username, password);
// FTPFile[] files = listFiles(directory);

