package oz.utils.snifit.copydbloadzip;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.fibi.gm.GMUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.NioUtils;
import oz.infra.nio.watcher.WatchDirService;
import oz.infra.nio.watcher.WatchDirServiceParameters;
import oz.infra.operaion.Outcome;
import oz.infra.process.FileProcessor;
import oz.infra.properties.PropertiesUtils;
import oz.infra.singleinstance.SingleInstanceUtils;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.jsch.JschUtils;

public class CopyDbloadZip implements FileProcessor {
	private static String singleInstqancePropertiesFilePath = null;
	private static String watcherPropertiesFilePath = null;
	private static String remotePropertiesFilePath = null;
	private static String gmMailPorpertiesFilePath = null;
	private static String backupFolderPath = null;
	private static final Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		singleInstqancePropertiesFilePath = args[0];
		watcherPropertiesFilePath = args[1];
		remotePropertiesFilePath = args[2];
		gmMailPorpertiesFilePath = args[3];
		backupFolderPath = args[4];
		confirmSingleInstanceRun(singleInstqancePropertiesFilePath);
		CopyDbloadZip copyDbloadZip = new CopyDbloadZip();
		copyDbloadZip.watch(watcherPropertiesFilePath);
	}

	private static void confirmSingleInstanceRun(final String propertiesFilePath) {
		SingleInstanceUtils.confirmSingleInstanceRun(propertiesFilePath);
	}

	private static void saveBackup(final String sourcefilePath, final String backupFolderPath) {
		File sourceFile = new File(sourcefilePath);
		String backupFileName = PathUtils.appendTimeStampToFileName(sourceFile);
		String targetFilePath = PathUtils.getFullPath(backupFolderPath, backupFileName);
		NioUtils.move(sourcefilePath, targetFilePath);
	}

	private static void sendGmMail(final String subject) {
		Properties emailProperties = PropertiesUtils.loadPropertiesFile(gmMailPorpertiesFilePath);
		Properties gmMailProperties = GMUtils.getGmEmailDefaultProperties(emailProperties.getProperty(GMUtils.GM_ENVIRONMENT));
		gmMailProperties.put(GMUtils.TO, emailProperties.getProperty(GMUtils.TO));
		gmMailProperties.put(GMUtils.FROM, emailProperties.getProperty(GMUtils.FROM));
		gmMailProperties.put(GMUtils.SUBJECT, subject);
		logger.finest(PropertiesUtils.getAsDelimitedString(gmMailProperties));
		GMUtils.sendEmail(gmMailProperties);
	}

	@Override
	public final Outcome processFile(final String filePath) {
		Outcome outcome = Outcome.SUCCESS;
		logger.info("processing filePath: " + filePath);
		Properties remoteProperties = PropertiesUtils.loadPropertiesFile(remotePropertiesFilePath);
		remoteProperties.put(SshParams.SOURCE_FILE, filePath);
		int retcode = JschUtils.scpTo(remoteProperties);
		saveBackup(filePath, backupFolderPath);
		String server = remoteProperties.getProperty(SshParams.SERVER);
		String message = filePath + " has been successfully copied to " + server;
		if (retcode != 0) {
			outcome = Outcome.FAILURE;
			message = "copy of " + filePath + " to " + server + " has failed";
		}
		logger.info(message + ". outcome: " + outcome.toString());
		sendGmMail(message);
		return outcome;
	}

	private void watch(final String propertiesFilePath) {
		Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		WatchDirServiceParameters watchDirServiceParameters = new WatchDirServiceParameters(properties);
		new WatchDirService(watchDirServiceParameters, this);
	}
}