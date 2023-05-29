package oz.utils.was.autodeploy.snifit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.operaion.Outcome;
import oz.infra.properties.PropertiesUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.SshUtils;
import oz.infra.ssh.scp.ScpUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.utils.modifyear.ModifyEar;

public class AutoDeployEarFileProcessor {

	private static final String MATAF_EAR = "MatafEAR";
	private static final String APPLICATION_CODE = "APPLICATION_CODE";
	private Properties currentEarProperties = null;
	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());

	private static String getPreparedEarPath(final String earFilePath, final String applicationNameSuffix) {
		String parentFolderPath = PathUtils.getParentFolderPath(earFilePath);
		String fileName = PathUtils.getNameWithoutExtension(earFilePath);
		String fileType = PathUtils.getFileExtension(earFilePath);
		String preparedEarFilePath = StringUtils.concat(PathUtils.getFullPath(parentFolderPath, fileName),
				OzConstants.UNDERSCORE, applicationNameSuffix, OzConstants.DOT, fileType);
		return preparedEarFilePath;
	}

	private String buildJythonScript() {
		String jythonScriptTargetString = null;
		String jythonScriptSourceString = FileUtils.readTextFile(AutoDeployParameters.getJythonScriptSourceFilePath());
		if (jythonScriptSourceString != null) {
			Map<String, String> currentEarPropertiesMap = PropertiesUtils.getStringsHashMap(currentEarProperties);
			jythonScriptTargetString = StringUtils.substituteVariables(jythonScriptSourceString,
					currentEarPropertiesMap);
			logger.info("jython script:\n" + jythonScriptTargetString);
		}
		return jythonScriptTargetString;
	}

	private Outcome moveEarFile2Done(final String earFilePath, final String preparedEarFilePath) {
		Outcome outcome = Outcome.SUCCESS;
		File preparedEarFile = new File(preparedEarFilePath);
		logger.finest("preparedEarFilePath: " + preparedEarFilePath);
		if (preparedEarFile.exists()) {
			String targetFilePath = PathUtils.getFullPath(AutoDeployParameters.getDoneFolderPath(),
					PathUtils.getFileNameAndExtension(earFilePath));
			boolean rc = new File(earFilePath).renameTo(new File(targetFilePath));
			if (!rc) {
				logger.warning("Failed to move " + earFilePath + " to " + targetFilePath);
				outcome = Outcome.FAILURE;
			}
		}
		return outcome;
	}

	public final void processEarFile(final String earFilePath, final String applicationNameSuffix) {
		logger.info(StringUtils.concat("Start processing ", earFilePath, " ... Suffix: ", applicationNameSuffix));
		currentEarProperties = AutoDeployParameters.getEarsPropertiesHashMap().get(applicationNameSuffix);
		if (currentEarProperties == null) {
			String exitMessage = StringUtils.concat("\nNo properties file found for application ",
					applicationNameSuffix, ".\nFile will not be processed !");
			SystemUtils.printMessageAndExit(exitMessage, OzConstants.EXIT_STATUS_ABNORMAL, false);
		} else {
			currentEarProperties.setProperty("applicationNameSuffix", applicationNameSuffix);
			AutoDeployUtils.sendMail(AutoDeployMailMessageEnum.START_MESSAGE, currentEarProperties);
			String preparedEarFilePath = runPrepare4Deploy(earFilePath, applicationNameSuffix);
			// String preparedEarFilePath = runModifyEar(earFilePath,
			// applicationNameSuffix);
			logger.info(StringUtils.concat("earFilePath: ", earFilePath, " applicationNameSuffix: ",
					applicationNameSuffix, "  preparedEarFilePath: ", preparedEarFilePath));
			currentEarProperties.put("preparedEarFilePath", preparedEarFilePath);
			String applicationName = MATAF_EAR + OzConstants.UNDERSCORE + applicationNameSuffix;
			currentEarProperties.put("applicationName", applicationName);
			// SystemUtils.printMessageAndExit("exit ..................", 0,
			// false);
			String[] nodesArray = currentEarProperties.get("nodes2Sync").toString().split(OzConstants.COMMA);
			for (int i = 0; i < nodesArray.length; i++) {
				currentEarProperties.put("node" + String.valueOf(i + 1), nodesArray[i]);
			}
			if (AutoDeployParameters.getRemoteProperties() == null) {
				runWsadmin(applicationName, preparedEarFilePath);
			} else {
				runRemoteWsadmin(applicationName, preparedEarFilePath);
			}
		}
	}

	private void processWsadminResponse(final SystemCommandExecutorResponse systemCommandExecutorResponse,
			final String applicationName, final String preparedEarFilePath, final StopWatch stopWatch) {
		String exceptionMessage = "";
		String executionMessage = systemCommandExecutorResponse.getExecutorResponse();
		logger.info(executionMessage);
		int returnCode = systemCommandExecutorResponse.getReturnCode();
		String stdOut = systemCommandExecutorResponse.getStdout().toString();
		int exceptionIndex = stdOut.toLowerCase().indexOf("exception");
		if (exceptionIndex > OzConstants.STRING_NOT_FOUND) {
			exceptionMessage = stdOut.substring(exceptionIndex);
			logger.warning(exceptionMessage);
			returnCode = OzConstants.EXIT_STATUS_ABNORMAL;
		}
		String returnCodeString = String.valueOf(returnCode);
		currentEarProperties.setProperty("returnCode", returnCodeString);
		String targetFilePath = AutoDeployParameters.getDoneFolderPath() + File.separator
				+ new File(preparedEarFilePath).getName() + "_RC=" + String.valueOf(returnCode);
		File tagetFile = new File(targetFilePath);
		boolean rc = new File(preparedEarFilePath).renameTo(tagetFile);
		if (rc) {
			logger.finest(preparedEarFilePath + " has been successfully moved to " + targetFilePath);
		} else {
			logger.warning("failed to move " + preparedEarFilePath + " to " + targetFilePath);
		}
		stopWatch.logElapsedTimeMessage(DateTimeUtils.formatDate(DateTimeUtils.DATE_FORMAT_dd_MM_yyyy) + " "
				+ SystemUtils.getCurrentMethodName() + " has completed in ");
		AutoDeployUtils.sendMail(AutoDeployMailMessageEnum.COMPLETION_MESSAGE, currentEarProperties);
	}

	private String runModifyEar(final String earFilePath, final String applicationNameSuffix) {
		String modifyEarPropetiesFilePath = AutoDeployParameters.getModifyEarPropertiesFilePath();
		logger.finest(StringUtils.concat("modifyEarPropetiesFilePath: ", modifyEarPropetiesFilePath));
		String preparedEarFilePath = ModifyEar
				.modifyEar(modifyEarPropetiesFilePath, earFilePath, applicationNameSuffix);
		moveEarFile2Done(earFilePath, preparedEarFilePath);
		return preparedEarFilePath;
	}

	private String runPrepare4Deploy(final String earFilePath, final String applicationNameSuffix) {
		String[] parametersArray = { AutoDeployParameters.getPrepare4DeploymentScriptPath() };
		Map<String, String> environmentMap = new HashMap<String, String>();
		environmentMap.put(APPLICATION_CODE, applicationNameSuffix);
		environmentMap.put("EARFilePath", earFilePath);

		SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(parametersArray, environmentMap);
		logger.info(scer.getExecutorResponse(true, false).toString());
		String preparedEarFilePath = getPreparedEarPath(earFilePath, applicationNameSuffix);
		moveEarFile2Done(earFilePath, preparedEarFilePath);
		logger.info(StringUtils.concat("Prepare for deploy has completed ... earFilePath: ", earFilePath,
				" applicationNameSuffix: ", applicationNameSuffix, " preparedEarFilePath: ", preparedEarFilePath));
		return preparedEarFilePath;
	}

	private void runRemoteWsadmin(final String applicationName, final String preparedEarFilePath) {
		logger.info(StringUtils.concat("applicationName: ", applicationName, " preparedEarFilePath: ",
				preparedEarFilePath));
		StopWatch stopWatch = new StopWatch();
		Properties remoteProperties = AutoDeployParameters.getRemoteProperties();
		String dstFile = remoteProperties.getProperty("dstFile");
		String remotePreparedEarFilePath = dstFile + PathUtils.getNameWithoutExtension(preparedEarFilePath) + OzConstants.DOT
				+ PathUtils.getFileExtension(preparedEarFilePath);
		currentEarProperties.setProperty("preparedEarFilePath", remotePreparedEarFilePath);
		String jythonScriptTargetString = buildJythonScript();
		if (jythonScriptTargetString != null) {
			String jythonScriptTargetFilePath = AutoDeployParameters.getJythonScriptTargetFilePath();
			jythonScriptTargetFilePath = jythonScriptTargetFilePath.replaceFirst("%TIMESTAMP%",
					DateTimeUtils.getTimeStamp());
			jythonScriptTargetFilePath = jythonScriptTargetFilePath.replaceFirst("%APPLICATION_NAME%", applicationName);
			FileUtils.writeFile(jythonScriptTargetFilePath, jythonScriptTargetString);
			ScpUtils.scp(jythonScriptTargetFilePath, remoteProperties);
			ScpUtils.scp(preparedEarFilePath, remoteProperties);
			Properties sshRemoteProperties = (Properties) remoteProperties.clone();
			String commandLine = sshRemoteProperties.getProperty(SshParams.COMMAND_LINE);
			String jythonScriptFileName = jythonScriptTargetFilePath.substring(jythonScriptTargetFilePath
					.lastIndexOf(File.separator));
			String jythonScriptTargetPath = sshRemoteProperties.getProperty(SshParams.DESTINATION_FILE).concat(
					jythonScriptFileName);
			commandLine = commandLine.replaceFirst("%PARAMETER1%", jythonScriptTargetPath);
			sshRemoteProperties.put(SshParams.COMMAND_LINE, commandLine);
			SystemCommandExecutorResponse systemCommandExecutorResponse = SshUtils
					.runRemoteCommand(sshRemoteProperties);
			processWsadminResponse(systemCommandExecutorResponse, applicationName, preparedEarFilePath, stopWatch);
		}
	}

	/*
	 * @deprecated
	 */
	private void runWsadmin(final String applicationName, final String preparedEarFilePath) {
		StopWatch stopWatch = new StopWatch();
		String jythonScriptTargetString = buildJythonScript();
		if (jythonScriptTargetString != null) {
			FileUtils.writeFile(AutoDeployParameters.getJythonScriptTargetFilePath(), jythonScriptTargetString);
			logger.info("Starting " + AutoDeployParameters.getWsadminRunFilePath());
			SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner
					.run(AutoDeployParameters.getWsadminRunFilePath());
			processWsadminResponse(systemCommandExecutorResponse, applicationName, preparedEarFilePath, stopWatch);
		}
	}
}