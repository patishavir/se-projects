package oz.utils.was.autodeploy;

import static oz.infra.constants.OzConstants.DOT;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.debug.DebugUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.nio.NioUtils;
import oz.infra.operaion.Outcome;
import oz.infra.process.FileProcessor;
import oz.infra.regexp.RegexpUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.snifit.SnifitUtils;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.jsch.JschUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.validation.filesystem.ValidationOutcomeEnum;
import oz.utils.was.autodeploy.common.mail.AutoDeployMailMessageEnum;
import oz.utils.was.autodeploy.common.mail.AutoDeployMailUtils;
import oz.utils.was.autodeploy.parameters.AutoDeployGlobalParameters;
import oz.utils.was.autodeploy.parameters.EarFileParameters;

public class AutoDeployEarProcessor implements FileProcessor {
	private EarFileParameters earFileParameters = null;
	private String timeStamp = null;
	private String applicationName = null;
	private FileHandler fileHandler = null;

	private Set<String> systemsSet = null;
	private static final String EARS_PROPERTIES_FOLDER_NAME = "earsProperties";
	private static final String QUIT_REQUEST = "quitAutoDeploy";
	public static final String AUTODEPLOY_SYSTEM = "autoDeploySystem";
	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());

	private static String getSnifitVersion(final EarFileParameters earFileParameters) {
		String snifitVersion = null;
		if (SystemUtils.isWindowsFlavorOS()) {
			String[] getSnifitVersionParametersArray = earFileParameters.getGetSnifitVersionParametersArray();
			if (getSnifitVersionParametersArray != null) {
				String getVersionUrl = getSnifitVersionParametersArray[1].replaceAll("%CONTEXTROOT%",
						earFileParameters.getApplicationName());
				getVersionUrl = getVersionUrl.replaceAll("MatafEAR", "MatafServer");
				int retryCount = Integer.parseInt(getSnifitVersionParametersArray[2]);
				int retryInterval = Integer.parseInt(getSnifitVersionParametersArray[3]);
				snifitVersion = SnifitUtils.getVersionProtected(getSnifitVersionParametersArray[0], getVersionUrl,
						retryCount, retryInterval);
				if (snifitVersion != null) {
					logger.info("snifit Version: ".concat(snifitVersion));
				} else {
					logger.warning("failed to get snifit version");
				}
			}
		}
		return snifitVersion;
	}

	public AutoDeployEarProcessor() {
		File systemsFoder = new File(AutoDeployGlobalParameters.getSystemsRoot());
		String[] systemsList = systemsFoder.list();
		systemsSet = new HashSet<String>(Arrays.asList(systemsList));
	}

	private List<String> getCommandLine(final String remoteEarFilePath, final Properties sshRemoteProperties,
			final String jythonScriptTargetFilePath) {
		ArrayList<String> parametersList = new ArrayList<String>();
		String[] commandLineArray = sshRemoteProperties.getProperty(SshParams.COMMAND_LINE)
				.split(RegexpUtils.REGEXP_WHITE_SPACE);
		parametersList.addAll(Arrays.asList(commandLineArray));
		String user = earFileParameters.getUser();
		String password = earFileParameters.getPassword();
		if (user != null && password != null) {
			String encryptionMethod = earFileParameters.getEncryptionMethod();
			if (encryptionMethod != null) {
				user = CryptographyUtils.decryptString(user, EncryptionMethodEnum.valueOf(encryptionMethod));
				password = CryptographyUtils.decryptString(password, EncryptionMethodEnum.valueOf(encryptionMethod));
			}
			String[] userpasswordArray = { "-user", user, "-password", password };
			parametersList.addAll(parametersList.size() - 1, Arrays.asList(userpasswordArray));
		}
		parametersList.add(jythonScriptTargetFilePath);
		parametersList.add(remoteEarFilePath);
		parametersList.add(earFileParameters.getApplicationName());
		logger.info(StringUtils.concat(ListUtils.getAsTitledDelimitedString(parametersList, "commandLine: ",
				Level.FINEST, OzConstants.BLANK)));
		return parametersList;
	}

	private String getEarFilePropertiesFilePath(final String earFilePath) {
		String system = getSystem(earFilePath);
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

	private String getSystem(final String earFilePath) {
		String system = null;
		String parent = PathUtils.getParentFolderName(earFilePath);
		if (systemsSet.contains(parent)) {
			applicationName = PathUtils.getNameWithoutExtension(earFilePath);
			system = parent;
		} else {
			applicationName = parent;
			system = PathUtils.getGrandParentFolderName(earFilePath);
		}
		String autoDeploySystem = SystemUtils.getSystemProperty(AUTODEPLOY_SYSTEM);
		if (autoDeploySystem != null) {
			system = autoDeploySystem;
			applicationName = PathUtils.getNameWithoutExtension(earFilePath);
			int underScoreIndex = applicationName.indexOf(OzConstants.UNDERSCORE);
			if (underScoreIndex > OzConstants.STRING_NOT_FOUND) {
				String suffix = applicationName.substring(underScoreIndex + 1);
				if (StringUtils.isJustDigits(suffix) && suffix.length() == 8) {
					applicationName = applicationName.substring(0, underScoreIndex);
				}
			}
		}
		logger.info("application name: ".concat(applicationName));
		return system;
	}

	private String getTargetFilePath(final String folderPath, final String earFileName) {
		String[] nameArray = earFileName.split(RegexpUtils.REGEXP_DOT);
		String targetString = StringUtils.concat(folderPath, File.separator, nameArray[0], OzConstants.UNDERSCORE,
				timeStamp, DOT, nameArray[1]);
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
			String applicationName = earFileParameters.getApplicationName();
			File earFile = new File(earFilePath);
			timeStamp = DateTimeUtils.getTimeStamp();
			String originalEarFileNameAndExt = earFile.getName();
			ValidationOutcomeEnum validationOutcome = ValidationOutcomeEnum.OK;
			if (!earFile.isFile()) {
				validationOutcome = ValidationOutcomeEnum.FILE_NOT_NFOUND;
			} else {
				if (originalEarFileNameAndExt.startsWith(QUIT_REQUEST)) {
					validationOutcome = ValidationOutcomeEnum.QUIT;
				} else {
					if (!RegexpUtils.matches(earFilePath, RegexpUtils.REGEXP_EAR_FILE)) {
						validationOutcome = ValidationOutcomeEnum.NOT_EAR;
					}
				}
			}
			String invalidEarFilePath = getTargetFilePath(AutoDeployGlobalParameters.getInvalidFolderPath(),
					originalEarFileNameAndExt);

			switch (validationOutcome) {
			case FILE_NOT_NFOUND:
				logger.warning("earFilePath: " + earFile.getAbsolutePath() + " does not exist !");
				break;
			case QUIT:
				logger.info("Quit request has been successully processed");
				FileUtils.deleteAndLogResult(earFile);
				System.exit(OzConstants.EXIT_STATUS_OK);
				break;
			case NOT_EAR:
				logger.warning(earFile.getAbsolutePath() + " is not an ear and will not be processed.");
				if (DebugUtils.isDebugMode()) {
					NioUtils.copy(earFilePath, invalidEarFilePath);
				} else {
					NioUtils.waitAndMove(earFilePath, invalidEarFilePath);
				}
				break;
			case OK:
				startAnewLog(applicationName);
				logger.info(StringUtils.concat("processing earFile: ", earFile.getAbsolutePath(), " applicationName: ",
						applicationName));
				String inProgressEarFilePath = getTargetFilePath(AutoDeployGlobalParameters.getInProgressdFolderPath(),
						originalEarFileNameAndExt);
				if (DebugUtils.isDebugMode()) {
					NioUtils.copy(earFilePath, inProgressEarFilePath);
				} else {
					NioUtils.waitAndMove(earFilePath, inProgressEarFilePath);
				}
				AutoDeployMailUtils.sendMail(earFileParameters.getEmailMessagesProperties(),
						AutoDeployMailMessageEnum.START_MESSAGE, earFileParameters);
				outcome = runWsadmin(applicationName, inProgressEarFilePath);
			}
		}
		if (outcome == Outcome.SUCCESS) {
			String snifitVersion = getSnifitVersion(earFileParameters);
			earFileParameters.setSnifitVersion(snifitVersion);
			logger.info(StringUtils.concat(earFilePath, " has been installed in ", stopWatch.getElapsedTimeString()));
		} else {
			logger.warning("failed to install ".concat(earFilePath));
		}
		AutoDeployMailUtils.sendMail(earFileParameters.getEmailMessagesProperties(),
				AutoDeployMailMessageEnum.COMPLETION_MESSAGE, earFileParameters);
		return outcome;
	}

	private Outcome processWsadminResponse(final SystemCommandExecutorResponse systemCommandExecutorResponse,
			final String earFilePath, final StopWatch stopWatch) {
		Outcome outcome = Outcome.FAILURE;
		String exceptionMessage = "";
		String executionMessage = systemCommandExecutorResponse.getExecutorResponse().toString();
		logger.info(executionMessage);
		int returnCode = systemCommandExecutorResponse.getReturnCode();
		if (returnCode == 0) {
			outcome = Outcome.SUCCESS;
		}
		String stdOut = systemCommandExecutorResponse.getStdout().toString();
		int exceptionIndex = stdOut.toLowerCase().indexOf("exception");
		if (exceptionIndex > OzConstants.STRING_NOT_FOUND) {
			exceptionMessage = stdOut.substring(exceptionIndex);
			logger.warning(exceptionMessage);
			returnCode = OzConstants.EXIT_STATUS_ABNORMAL;
			outcome = Outcome.FAILURE;
		}
		earFileParameters.setReturnCode(returnCode);
		String targetFilePath = AutoDeployGlobalParameters.getDoneFolderPath() + File.separator
				+ new File(earFilePath).getName() + "_RC=" + String.valueOf(returnCode);
		NioUtils.move(earFilePath, targetFilePath);
		logger.info(DateTimeUtils.formatDate(DateTimeUtils.DATE_FORMAT_dd_MM_yyyy) + " "
				+ SystemUtils.getCurrentMethodName() + " has completed in " + stopWatch.getElapsedTimeString());
		return outcome;
	}

	private Outcome runWsadmin(final String applicationName, final String localEarFilePath) {
		logger.info(StringUtils.concat("applicationName: ", applicationName, " earFilePath: ", localEarFilePath));
		Outcome outcome = Outcome.FAILURE;
		StopWatch stopWatch = new StopWatch();
		Properties remoteProperties = earFileParameters.getRemoteProperties();
		SystemCommandExecutorResponse scer = null;
		String hostName = SystemUtils.getHostname();
		String server = remoteProperties.getProperty(SshParams.SERVER);
		logger.info(StringUtils.concat("host name: ", hostName, " length: ", String.valueOf(hostName.length()),
				" server: ", server, " length: ", String.valueOf(server.length()), " equal: ",
				String.valueOf(hostName.equalsIgnoreCase(server))));
		if (!hostName.equalsIgnoreCase(server)) {
			String jythonScriptTargetFilePath = scpFile(applicationName,
					earFileParameters.getJythonScriptSourceFilePath(), remoteProperties);
			remoteProperties.put(SshParams.SOURCE_FILE, localEarFilePath);
			JschUtils.scpTo(remoteProperties);
			String remoteEarFilePath = StringUtils.concat(remoteProperties.getProperty(SshParams.DESTINATION_FILE),
					new File(localEarFilePath).getName());
			Properties sshRemoteProperties = (Properties) remoteProperties.clone();
			List<String> parametersList = getCommandLine(remoteEarFilePath, remoteProperties,
					jythonScriptTargetFilePath);
			String commandLine = ListUtils.getAsTitledDelimitedString(parametersList, OzConstants.EMPTY_STRING,
					Level.INFO, OzConstants.BLANK);
			sshRemoteProperties.put(SshParams.COMMAND_LINE, commandLine);
			scer = JschUtils.exec(sshRemoteProperties);
		} else {
			List<String> parametersList = getCommandLine(localEarFilePath, remoteProperties,
					earFileParameters.getJythonScriptSourceFilePath());
			scer = SystemCommandExecutorRunner.run(parametersList);
		}
		outcome = processWsadminResponse(scer, localEarFilePath, stopWatch);
		return outcome;
	}

	private String scpFile(final String applicationName, final String filePath, final Properties remoteProperties) {
		Properties myRemoteProperties = (Properties) remoteProperties.clone();
		String fileName1 = new File(filePath).getName();
		String[] nameArray = fileName1.split(RegexpUtils.REGEXP_DOT);
		String targetFileName = StringUtils.concat(nameArray[0], OzConstants.UNDERSCORE, applicationName,
				OzConstants.UNDERSCORE, timeStamp);
		String targetFilePath = StringUtils.concat(myRemoteProperties.getProperty(SshParams.DESTINATION_FILE),
				targetFileName, OzConstants.DOT, nameArray[1]);
		myRemoteProperties.setProperty(SshParams.DESTINATION_FILE, targetFilePath);
		myRemoteProperties.setProperty(SshParams.SOURCE_FILE, filePath);
		// ScpUtils.scp(filePath, myRemoteProperties);
		JschUtils.scpTo(myRemoteProperties);
		return targetFilePath;
	}

	private void startAnewLog(final String applicationName) {
		String logFileName = StringUtils.concat(applicationName, OzConstants.UNDERSCORE, timeStamp,
				OzConstants.ERR_SUFFIX);
		String logFilePath = PathUtils.getFullPath(AutoDeployGlobalParameters.getLogsFolder(), logFileName);
		File logfile = new File(logFilePath);
		fileHandler = JulUtils.switchFileHandler(fileHandler, logfile.getAbsolutePath());
	}
}