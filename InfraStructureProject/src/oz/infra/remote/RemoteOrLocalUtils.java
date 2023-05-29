package oz.infra.remote;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.NioUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.jsch.JschUtils;
import oz.infra.system.SystemUtils;

public class RemoteOrLocalUtils {
	private static Logger logger = JulUtils.getLogger();

	public static int copy(final Properties properties) {
		int returnCode = OzConstants.EXIT_STATUS_ABNORMAL;
		String targetServer = properties.getProperty(SshParams.SERVER);
		if (!SystemUtils.isCurrentHost(targetServer)) {
			String direction = properties.getProperty(SshParams.DIRECTION);
			if (direction.equalsIgnoreCase(SshParams.TO)) {
				returnCode = JschUtils.scpTo(properties);
			} else if (direction.equalsIgnoreCase(SshParams.FROM)) {
				returnCode = JschUtils.scpFrom(properties);
			} else {
				logger.warning("Invalid direction parameter.");
			}
		} else {
			logger.info("target host " + targetServer + " is same as current host " + SystemUtils.getLocalHostName() + ". processing copy locally.");
			String sourceFilePath = properties.getProperty(SshParams.SOURCE_FILE);
			String destinationFilePath = properties.getProperty(SshParams.DESTINATION_FILE);
			String canonicalSourcePath = PathUtils.getCanonicalPath(sourceFilePath);
			String canonicalDestinationPath = PathUtils.getCanonicalPath(destinationFilePath);
			if (canonicalSourcePath.equals(canonicalDestinationPath)) {
				logger.info("source file path: " + canonicalSourcePath + " and destination file path: " + canonicalDestinationPath
						+ " are the same.\nCopy operation will not take place.");
			} else {
				boolean rc = FileUtils.copyFile(canonicalSourcePath, canonicalDestinationPath);
				if (rc) {
					returnCode = OzConstants.EXIT_STATUS_OK;
				}
			}
		}
		return returnCode;
	}

	public static SystemCommandExecutorResponse exec(final Properties properties) {
		SystemCommandExecutorResponse scer = null;
		String targetServer = properties.getProperty(SshParams.SERVER);
		if (!SystemUtils.isCurrentHost(targetServer)) {
			scer = JschUtils.exec(properties, Level.INFO);
		} else {
			logger.info("target host " + targetServer + " is same as current host " + SystemUtils.getLocalHostName() + ". processing exec locally.");
			String commandLine = properties.getProperty(SshParams.COMMAND_LINE);
			String[] commandLineArray = commandLine.split(OzConstants.BLANK);
			File commandFile = new File(commandLineArray[0]);
			if (commandFile.isFile()) {
				scer = SystemCommandExecutorRunner.run(commandLineArray);
			} else {
				String temporaryFilePath = PathUtils.getTemporaryFilePath();
				if (SystemUtils.isWindowsFlavorOS()) {
					temporaryFilePath = temporaryFilePath + OzConstants.BAT_SUFFIX;
				}
				FileUtils.writeFile(temporaryFilePath, commandLine);
				if (SystemUtils.isUnixOS()) {
					NioUtils.setPosixFilePermissions(temporaryFilePath, NioUtils.MODE_755);
				}
				scer = SystemCommandExecutorRunner.run(temporaryFilePath);
			}
		}
		if (scer != null) {
			logger.info(scer.getExecutorResponse());
		}
		return scer;
	}
}
