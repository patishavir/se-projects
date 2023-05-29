package oz.utils.cm.ds.scripts;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.parameters.ParametersUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.SshUtils;
import oz.infra.ssh.scp.ScpUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.zip4j.Zip4jUtils;
import oz.utils.cm.ds.common.cc.DsCmCCParameters;
import oz.utils.cm.ds.common.cc.DsCmCCUtils;

public class DsCmScriptsProcessor {
	private static Logger logger = JulUtils.getLogger();

	private static Properties buildSshProperties(final String remoteScriptFilePath, final String remoteZipFilePath) {
		Properties sshProperties = new Properties();
		sshProperties.setProperty(ParametersUtils.SERVER, DsCmScriptsParameters.getServer());
		sshProperties.setProperty(ParametersUtils.USER_NAME, DsCmScriptsParameters.getUserName());
		sshProperties.setProperty(ParametersUtils.PASSWORD, DsCmScriptsParameters.getPassword());
		sshProperties.setProperty(ParametersUtils.ENCRYPTION_METHOD, DsCmScriptsParameters.getEncryptionMethod());
		String remoteZipCommand = StringUtils.concat(remoteScriptFilePath, OzConstants.BLANK, remoteZipFilePath);
		sshProperties.setProperty(SshParams.DESTINATION_FILE, remoteScriptFilePath);
		sshProperties.setProperty(SshParams.COMMAND_LINE, remoteZipCommand);
		PropertiesUtils.printProperties(sshProperties, Level.INFO);
		return sshProperties;
	}

	private static String getLocalFilePath(final String remoteZipFilePath, final Properties scpProperties) {
		logger.info("starting scpDownload ... ");
		File remoteZipFile = new File(remoteZipFilePath);
		String remoteZipFileNameAndExt = remoteZipFile.getName();
		String remoteZipFileName = PathUtils.getNameWithoutExtension(remoteZipFilePath);
		String localFolder = DsCmScriptsParameters.getLocalFoler();
		String localFolderPath = PathUtils.getFullPath(localFolder, remoteZipFileName);
		File localFolderFile = new File(localFolderPath).getAbsoluteFile();
		localFolderFile.mkdir();
		String localFilePath = PathUtils.getFullPath(localFolderPath, remoteZipFileNameAndExt);
		logger.info("localFilePath: ".concat(localFilePath));
		return PathUtils.getAbsolutePath(localFilePath);
	}

	public static void processScripts() {
		String timeStamp = DateTimeUtils.getTimeStamp();
		String remoteScriptFilePath = StringUtils.concat(DsCmScriptsParameters.getTargetFolder(), DsCmScriptsParameters.getZipFilesScript(),
				OzConstants.UNDERSCORE, timeStamp + OzConstants.SH_SUFFIX);
		String remoteZipFilePath = StringUtils.concat(DsCmScriptsParameters.getTargetFolder(), "scripts_", timeStamp, OzConstants.ZIP_SUFFIX);
		Properties sshProperties = buildSshProperties(remoteScriptFilePath, remoteZipFilePath);
		Outcome outcome = runRemoteZip(sshProperties);
		if (outcome == Outcome.SUCCESS) {
			String localFilePath = getLocalFilePath(remoteZipFilePath, sshProperties);
			scpDownload(remoteZipFilePath, localFilePath, sshProperties);
			String targetFolderParentPath = PathUtils.getParentFolderPath(localFilePath);
			String targetFolderPath = PathUtils.getFullPath(targetFolderParentPath,
					StringUtils.concat(DsCmScriptsParameters.getUnzipTargetFolder(), File.separator, DsCmScriptsParameters.getEnvironment()));
			Zip4jUtils.extractAllFiles(localFilePath, targetFolderPath);
			// import to cc
			DsCmCCParameters.processParameters(DsCmScriptsParameters.getClearCasePropertiesFilePath());
			int returnCode = DsCmCCUtils.importToClearCase(DsCmCCParameters.getCcViewRootFolderPath(),
					PathUtils.getParentFolderPath(targetFolderPath), DsCmCCParameters.getViewTag(), DsCmCCParameters.getStreamSelector(),
					DsCmCCParameters.getStgloc());
			logger.info("all done ! return code: ".concat(String.valueOf(returnCode)));
		} else {
			String exitMessage = "Remote zip operation failed. Processing has been aborted !";
			SystemUtils.printMessageAndExit(exitMessage, OzConstants.EXIT_STATUS_ABNORMAL, false);
		}
	}

	private static Outcome runRemoteZip(final Properties sshProperties) {
		Outcome outCome = Outcome.FAILURE;
		String sourceFilePath = PathUtils.getFullPath(DsCmScriptsParameters.getRootFolderPath(), DsCmScriptsParameters.getZipFilesScript());
		FileUtils.terminateIfFileDoesNotExist(sourceFilePath);
		String sourceFilePath1 = sourceFilePath.concat(OzConstants.SH_SUFFIX);
		FileUtils.dos2unix(sourceFilePath, sourceFilePath1);
		sshProperties.setProperty(SshParams.DIRECTION, SshParams.TO);
		ScpUtils.scp(sourceFilePath1, sshProperties);
		Properties sshProperties1 = (Properties) sshProperties.clone();
		String chmodCommand = "/usr/bin/chmod 700 " + sshProperties1.getProperty(SshParams.DESTINATION_FILE);
		sshProperties1.setProperty(SshParams.COMMAND_LINE, chmodCommand);
		SystemCommandExecutorResponse scer1 = SshUtils.runRemoteCommand(sshProperties1);
		logger.info(scer1.getExecutorResponse());
		SystemCommandExecutorResponse scer = SshUtils.runRemoteCommand(sshProperties);
		logger.info(scer.getExecutorResponse());
		if (scer.getReturnCode() == 0 && (scer.getStderr() == null || scer.getStderr().length() == 0)) {
			outCome = Outcome.SUCCESS;
		}
		return outCome;
	}

	private static void scpDownload(final String remoteZipFilePath, final String localFilePath, final Properties scpProperties) {
		scpProperties.setProperty("direction", "from");
		scpProperties.setProperty("scrFile", remoteZipFilePath);
		scpProperties.setProperty("dstFile", localFilePath);
		PropertiesUtils.printProperties(scpProperties, Level.INFO);
		ScpUtils.scp(scpProperties);
	}
}
