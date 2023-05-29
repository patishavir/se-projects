package oz.utils.autodeploy.tasks;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.remote.RemoteOrLocalUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.ssh.SshParams;
import oz.infra.string.StringUtils;

public class RemoteFileSystemOperationsTask {
	private static Logger logger = JulUtils.getLogger();

	public static void mkRemoteDir(final Properties remotePropertiez) {
		String targetFolderPath = remotePropertiez.get(SshParams.DESTINATION_FILE).toString();
		String mkdirCommaand = StringUtils.concat("if [[ ! -d ", targetFolderPath, " ]] ; then \t mkdir -p ", targetFolderPath, "; fi");
		Properties mkdirProperties = (Properties) remotePropertiez.clone();
		mkdirProperties.setProperty(SshParams.COMMAND_LINE, mkdirCommaand);
		SystemCommandExecutorResponse scer = RemoteOrLocalUtils.exec(mkdirProperties);
		logger.info(scer.getExecutorResponse());
	}

	public static String copyFile2RemoteWithTimeStampedName(final Properties remoteProperties, final String localFilePath) {
		Properties myRemoteProperties = (Properties) remoteProperties.clone();
		String remoteFilePath = PathUtils.getRemoteTemporaryFilePath(myRemoteProperties, localFilePath);
		logger.info(StringUtils.concat("remoteFilePath:", remoteFilePath));
		myRemoteProperties.setProperty(SshParams.SOURCE_FILE, localFilePath);
		myRemoteProperties.setProperty(SshParams.DESTINATION_FILE, remoteFilePath);
		int retcode = RemoteOrLocalUtils.copy(myRemoteProperties);
		return remoteFilePath;
	}

	public static int copyFile2Remote(final Properties remoteProperties, final String localFilePath) {
		Properties myRemoteProperties = (Properties) remoteProperties.clone();
		myRemoteProperties.setProperty(SshParams.SOURCE_FILE, localFilePath);
		int retcode = RemoteOrLocalUtils.copy(myRemoteProperties);
		logger.info(StringUtils.concat(localFilePath, " has been copied to ", remoteProperties.getProperty(SshParams.DESTINATION_FILE),
				" return code:", String.valueOf(retcode)));
		return retcode;
	}
}
