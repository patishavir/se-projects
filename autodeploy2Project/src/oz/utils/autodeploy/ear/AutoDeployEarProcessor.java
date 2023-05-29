package oz.utils.autodeploy.ear;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.logging.jul.levels.SummaryLevel;
import oz.infra.nio.NioUtils;
import oz.infra.operaion.Outcome;
import oz.infra.print.PrintUtils;
import oz.infra.process.FileProcessor;
import oz.infra.regexp.RegexpUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.validation.filesystem.ValidationOutcomeEnum;
import oz.utils.autodeploy.common.AutoDeployGlobalParameters;
import oz.utils.autodeploy.common.AutoDeployMailMessageEnum;
import oz.utils.autodeploy.common.AutoDeployMailUtils;

public class AutoDeployEarProcessor implements FileProcessor {
	private static final String EARS_PROPERTIES_FOLDER_NAME = "earsProperties";
	public static final String AUTODEPLOY_SYSTEM = "autoDeploySystem";
	private static final String AUTODEPLOY_APPLICATION_NAME = "autoDeployApplicationName";
	private static final String MOVE_INPUT_FILE = "moveInputFile";

	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());
	private EarFileParameters earFileParameters = null;
	private String timeStamp = null;
	private String applicationName = null;

	private Handler fileHandler = null;
	private Set<String> systemsSet = null;

	public AutoDeployEarProcessor() {
		File systemsFoder = new File(AutoDeployGlobalParameters.getSystemsRoot());
		String[] systemsList = systemsFoder.list();
		systemsSet = new HashSet<String>(Arrays.asList(systemsList));
	}

	private String getEarFilePropertiesFilePath(final String earFilePath) {
		String system = getSystemAndApplicationName(earFilePath);
		earFileParameters.setSystem(system);
		String earPropertiesFolderPath = PathUtils.getFullPath(AutoDeployGlobalParameters.getSystemsRoot(),
				StringUtils.concat(system, File.separator, EARS_PROPERTIES_FOLDER_NAME));
		String earPropertiesFilePath = PathUtils.getFullPath(earPropertiesFolderPath,
				StringUtils.concat(applicationName, OzConstants.PROPERTIES_SUFFIX));
		logger.info("earPropertiesFilePath: ".concat(earPropertiesFilePath));
		if (!FileUtils.isFileExists(earPropertiesFilePath)) {
			String defaultSystemPrpertiesFileName = StringUtils.concat("ear_default", OzConstants.PROPERTIES_SUFFIX);
			earPropertiesFilePath = PathUtils.getFullPath(earPropertiesFolderPath, defaultSystemPrpertiesFileName);
		}
		FileUtils.exitIfFileDoesNoteExist(earPropertiesFilePath);
		earFileParameters.setApplicationName(applicationName);
		logger.info(StringUtils.concat("ear Properties File Path: ", earPropertiesFilePath));
		return earPropertiesFilePath;
	}

	private String getSystemAndApplicationName(final String earFilePath) {
		String system = null;
		applicationName = PathUtils.getNameWithoutExtension(earFilePath);
		String parent = PathUtils.getParentFolderName(earFilePath);
		if (systemsSet.contains(parent)) {
			system = parent;
		} else {
			String mySystem = PathUtils.getGrandParentFolderName(earFilePath);
			if (systemsSet.contains(mySystem)) {
				system = mySystem;
				applicationName = parent;
			}
		}
		removeTimeStampFromApplicationName(earFilePath);
		String mySystem = SystemUtils.getVarFromSysPropsOrEnv(AUTODEPLOY_SYSTEM);
		if (mySystem != null) {
			system = mySystem;
		}
		String myApplicationName = SystemUtils.getVarFromSysPropsOrEnv(AUTODEPLOY_APPLICATION_NAME);
		if (myApplicationName != null) {
			applicationName = myApplicationName;
		}
		logger.info(StringUtils.concat("earFilePath: ", earFilePath, " application name: ", applicationName,
				" system: ", system));
		return system;
	}

	private String getTargetFilePath(final String folderPath, final String earFileNameAndExt) {
		String[] fileNameAndExtension = PathUtils.getFileNameAndExtensionArray(earFileNameAndExt);
		String targetString = StringUtils.concat(folderPath, File.separator, fileNameAndExtension[0],
				OzConstants.UNDERSCORE, timeStamp, OzConstants.DOT, fileNameAndExtension[1]);
		logger.info(StringUtils.concat("targetString: ", targetString));
		return targetString;
	}

	@Override
	public Outcome processFile(final String earFilePath) {
		earFileParameters = new EarFileParameters();
		Outcome outcome = Outcome.FAILURE;
		logger.info("start processing. Ear File Path: ".concat(earFilePath));
		StopWatch stopWatch = new StopWatch();
		String earPropertiesFilePath = getEarFilePropertiesFilePath(earFilePath);
		if (FileUtils.isFileExists(earPropertiesFilePath)) {
			earFileParameters.processParameters(earPropertiesFilePath);
			applicationName = earFileParameters.getApplicationName();
			File earFile = new File(earFilePath);
			timeStamp = DateTimeUtils.getTimeStamp();
			String originalEarFileNameAndExt = earFile.getName();
			ValidationOutcomeEnum validationOutcome = ValidationOutcomeEnum.OK;
			if (!earFile.isFile()) {
				validationOutcome = ValidationOutcomeEnum.FILE_NOT_NFOUND;
			} else {
				if (!(RegexpUtils.matches(earFilePath, RegexpUtils.REGEXP_EAR_FILE)
						|| RegexpUtils.matches(earFilePath, RegexpUtils.REGEXP_WAR_FILE))) {
					validationOutcome = ValidationOutcomeEnum.NOT_EAR_OR_WAR;
				}
			}
			switch (validationOutcome) {
			case FILE_NOT_NFOUND:
				logger.warning("earFilePath: " + earFile.getAbsolutePath() + " does not exist !");
				break;
			case NOT_EAR_OR_WAR:
				logger.warning(earFile.getAbsolutePath() + " is not an ear or war and will not be processed.");
				String invalidEarFilePath = getTargetFilePath(AutoDeployGlobalParameters.getInvalidFolderPath(),
						originalEarFileNameAndExt);
				if (SystemUtils.getBooleanVarFromSysPropsOrEnv(MOVE_INPUT_FILE)) {
					NioUtils.waitAndMove(earFilePath, invalidEarFilePath);
				} else {
					NioUtils.copy(earFilePath, invalidEarFilePath);
				}
				break;
			case OK:
				startAnewLog(applicationName);
				logger.info(StringUtils.concat("processing earFile: ", earFile.getAbsolutePath(), " applicationName: ",
						applicationName));
				String inProgressEarFilePath = getTargetFilePath(AutoDeployGlobalParameters.getInProgressdFolderPath(),
						originalEarFileNameAndExt);
				if (SystemUtils.getBooleanVarFromSysPropsOrEnv(MOVE_INPUT_FILE)) {
					NioUtils.waitAndMove(earFilePath, inProgressEarFilePath);
				} else {
					NioUtils.copy(earFilePath, inProgressEarFilePath);
				}
				AutoDeployMailUtils.sendMail(earFileParameters.getEmailMessagesProperties(),
						AutoDeployMailMessageEnum.START_MESSAGE, earFileParameters);
				logger.info("call WsadminProcessor ...");
				outcome = WsadminProcessor.runWsadmin(earFileParameters, inProgressEarFilePath);
			}
		}
		String system = earFileParameters.getSystem();
		if (outcome == Outcome.SUCCESS) {
			earFileParameters.setReturnCode(OzConstants.EXIT_STATUS_OK);
			String snifitVersion = AutoDeployEarUtils.getSnifitVersion(earFileParameters);
			earFileParameters.setSnifitVersion(snifitVersion);
			String successMessage = StringUtils.concat(applicationName, " has been installed successfully on ", system,
					" in ", stopWatch.getElapsedTimeString(), ". ear file path: ", earFilePath);
			logger.info(successMessage);
			AutoDeployGlobalParameters.SUMMARY_LOGGER.log(SummaryLevel.SUMMARY, successMessage);
		} else {
			earFileParameters.setReturnCode(OzConstants.EXIT_STATUS_ABNORMAL);
			String failureMessage = StringUtils.concat(applicationName, " has failed to install on ", system,
					". duration: ", stopWatch.getElapsedTimeString(), ". ear file path: ", earFilePath);
			logger.warning(failureMessage);
			AutoDeployGlobalParameters.SUMMARY_LOGGER.log(SummaryLevel.SUMMARY, failureMessage);
		}
		AutoDeployMailUtils.sendMail(earFileParameters.getEmailMessagesProperties(),
				AutoDeployMailMessageEnum.COMPLETION_MESSAGE, earFileParameters);
		logger.info(PrintUtils.getSeparatorLine(StringUtils.concat("processing of ", earFilePath, " has completed."), 2,
				0));
		JulUtils.removeHandler(fileHandler);
		fileHandler = null;
		return outcome;
	}

	private void removeTimeStampFromApplicationName(final String earFilePath) {
		String fileNameWithoutExtension = PathUtils.getNameWithoutExtension(earFilePath);
		String[] applicationNameArray = fileNameWithoutExtension.split(OzConstants.UNDERSCORE);
		if (applicationNameArray.length == OzConstants.INT_2 && applicationNameArray[1].length() == OzConstants.INT_8
				&& StringUtils.isJustDigits(applicationNameArray[1])) {
			applicationName = applicationNameArray[0];
		}
	}

	private void startAnewLog(final String applicationName) {
		String logFileName = StringUtils.concat(applicationName, OzConstants.UNDERSCORE, timeStamp,
				OzConstants.ERR_SUFFIX);
		String logFilePath = PathUtils.getFullPath(AutoDeployGlobalParameters.getLogsFolder(), logFileName);
		File logfile = new File(logFilePath);
		fileHandler = JulUtils.addFileHandler(logfile.getAbsolutePath());
	}
}