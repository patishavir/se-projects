package oz.utils.autodeploy.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.datetime.StopWatch;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.operaion.Outcome;
import oz.infra.regexp.RegexpUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.jsch.JschUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.varargs.VarArgsUtils;
import oz.utils.autodeploy.DeploymentManagerProperties;
import oz.utils.autodeploy.common.AutoDeployUtils;

public class WsadminTask {
	public enum UserPasswordLocation {
		USER_PASSWORD_BEFORE_PARAMS, USER_PASSWORD_AFTER_PARAMS
	};

	private static final String USER_PARAMETER = "-user";
	private static final String PASSWORD_PARAMETER = "-password";

	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());

	public static List<String> getCommandLine(final String commandLine, final Properties deploymentManagerProperties, final List<String> parameters,
			final UserPasswordLocation... userPasswordLocations) {
		UserPasswordLocation userPasswordLocation = VarArgsUtils.getMyArg(UserPasswordLocation.USER_PASSWORD_BEFORE_PARAMS, userPasswordLocations);
		List<String> parametersList = new ArrayList<String>();
		parametersList.addAll(parameters);
		String[] userpasswordArray = getWsadminUserPassword(deploymentManagerProperties);
		if (userpasswordArray != null) {
			if (userPasswordLocation == UserPasswordLocation.USER_PASSWORD_AFTER_PARAMS) {
				parametersList.addAll(Arrays.asList(userpasswordArray));
			} else {
				parametersList.addAll(0, Arrays.asList(userpasswordArray));
			}
		}
		String[] commandLineArray = commandLine.split(RegexpUtils.REGEXP_WHITE_SPACE);
		parametersList.addAll(0, Arrays.asList(commandLineArray));
		List<String> parametersListForLog = new ArrayList<String>();
		parametersListForLog.addAll(parametersList);
		int passwordPtr = parametersListForLog.indexOf(PASSWORD_PARAMETER);
		if (passwordPtr > OzConstants.STRING_NOT_FOUND) {
			parametersListForLog.set(passwordPtr + 1, "??supressed??");
		}
		logger.info(ListUtils.getAsDelimitedString(parametersListForLog, "commandLine: ", Level.FINEST, OzConstants.BLANK));
		return parametersList;
	}

	public static String[] getWsadminUserPassword(final Properties deploymentManagerProperties) {
		String[] userpasswordArray = null;
		String user = deploymentManagerProperties.getProperty(DeploymentManagerProperties.WSADMIN_USER);
		String password = deploymentManagerProperties.getProperty(DeploymentManagerProperties.WSADMIN_PASSWORD);
		if (user != null && password != null) {
			String encryptionMethod = deploymentManagerProperties.getProperty(SshParams.ENCRYPTION_METHOD);
			if (encryptionMethod != null) {
				user = CryptographyUtils.decryptString(user, EncryptionMethodEnum.valueOf(encryptionMethod));
				password = CryptographyUtils.decryptString(password, EncryptionMethodEnum.valueOf(encryptionMethod));
			}
			String[] userpasswordArray1 = { USER_PARAMETER, user, PASSWORD_PARAMETER, password };
			userpasswordArray = userpasswordArray1;
		}
		return userpasswordArray;
	}

	private static Outcome processWsadminResponse(final SystemCommandExecutorResponse scer) {
		Outcome outcome = Outcome.FAILURE;
		String exceptionMessage = "";
		logger.info(scer.getExecutorResponse());
		int returnCode = scer.getReturnCode();
		if (returnCode == 0) {
			outcome = Outcome.SUCCESS;
		}
		String stdOut = scer.getStdout();
		int exceptionIndex = stdOut.toLowerCase().indexOf("exception");
		if (exceptionIndex > OzConstants.STRING_NOT_FOUND) {
			exceptionMessage = stdOut.substring(exceptionIndex);
			logger.warning(exceptionMessage);
		}
		return outcome;
	}

	public static Outcome runWsadminScript(final String system, final String localJythonScriptPathParam, final List<String> parameters) {
		Outcome outcome = Outcome.FAILURE;
		StopWatch stopWatch = new StopWatch();
		Properties deploymentManagerProperties = AutoDeployUtils.getDeploymentManagerProperties(system);
		SystemCommandExecutorResponse scer = null;
		String server = deploymentManagerProperties.getProperty(SshParams.SERVER).trim();
		boolean runLocally = SystemUtils.isCurrentHost(server);
		String jythonScriptPath = localJythonScriptPathParam;
		if (localJythonScriptPathParam == null || localJythonScriptPathParam.length() == 0) {
			jythonScriptPath = deploymentManagerProperties.getProperty(DeploymentManagerProperties.DEFAULT_JYTHON_SCRIPT_FULL_PATH);
		}
		if (!runLocally) {
			jythonScriptPath = RemoteFileSystemOperationsTask.copyFile2RemoteWithTimeStampedName(deploymentManagerProperties, jythonScriptPath);
		}
		List<String> parametersList = new ArrayList<String>(parameters);
		parametersList.add(0, jythonScriptPath);
		parametersList.add(0, "-f");
		parametersList = getCommandLine(deploymentManagerProperties.getProperty(DeploymentManagerProperties.WSADMIN_COMMAND),
				deploymentManagerProperties, parametersList);
		if (runLocally) {
			scer = SystemCommandExecutorRunner.run(parametersList);
		} else {
			String commandLine = ListUtils.getAsDelimitedString(parametersList, OzConstants.BLANK);
			deploymentManagerProperties.put(SshParams.COMMAND_LINE, commandLine);
			scer = JschUtils.exec(deploymentManagerProperties);
		}
		if (scer != null) {
			outcome = processWsadminResponse(scer);
		}
		String completionMessage = StringUtils.concat("wsadmin on server ", server, " has completed in ", stopWatch.getElapsedTimeString(),
				". system: ", system, " return code: ", String.valueOf(scer.getReturnCode()));
		logger.info(completionMessage);
		return outcome;
	}
}
