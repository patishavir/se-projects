package oz.infra.run.processbuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.thread.ThreadUtils;

public class SystemCommandExecutorRunner {
	private static Logger logger = JulUtils.getLogger();

	public static SystemCommandExecutorResponse run(final List<String> commands, final File... files) {
		return run(commands, null, files);
	}

	public static SystemCommandExecutorResponse run(final List<String> commands,
			final Map<String, String> environmentMap, final File... files) {
		// ListUtils.print(commands, "run parameters:", Level.INFO);
		SystemCommandExecutorResponse systemCommandExecutorResponse = null;
		SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands, environmentMap, files);
		try {
			int returnCode = commandExecutor.executeCommand();
			String stdoutString = OzConstants.EMPTY_STRING;
			String stderrString = OzConstants.EMPTY_STRING;
			if (returnCode != 0) {
				logger.finest(ThreadUtils.getStackTrace());
			}
			logger.finest("Return code: " + String.valueOf(returnCode));
			StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
			if (stdout != null) {
				logger.finest(stdout.toString());
				stdoutString = stdout.toString();
			}
			StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();
			if (stderr != null) {
				logger.finest(stderr.toString());
				stderrString = stderr.toString();
			}
			systemCommandExecutorResponse = new SystemCommandExecutorResponse(returnCode, stdoutString, stderrString);
		} catch (Exception ex) {
			ListUtils.getAsTitledDelimitedString(commands, "run parameters:\n", Level.INFO, OzConstants.TAB);
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return systemCommandExecutorResponse;
	}

	public static SystemCommandExecutorResponse run(final String parameter) {
		String[] parametersArray = { parameter };
		return run(parametersArray, null);
	}

	public static SystemCommandExecutorResponse run(final String parameter, final Map<String, String> environmentMap,
			final File... files) {
		String[] parametersArray = { parameter };
		List<String> commands = new ArrayList<String>(Arrays.asList(parametersArray));
		return run(commands, environmentMap, files);
	}

	public static SystemCommandExecutorResponse run(final String[] parametersArray) {
		return run(parametersArray, null);
	}

	public static SystemCommandExecutorResponse run(final String[] parametersArray,
			final Map<String, String> environmentMap) {
		List<String> commands = new ArrayList<String>(Arrays.asList(parametersArray));
		return run(commands, environmentMap);
	}

	public static SystemCommandExecutorResponse runAs(final String psexecPath, final String userName,
			final String password, final String targetMachine, final String parameter) {
		return runAs(psexecPath, userName, password, targetMachine, parameter, null);
	}

	public static SystemCommandExecutorResponse runAs(final String psexecPath, final String userName,
			final String password, final String targetMachine, final String parameter,
			final Map<String, String> environmentMap) {
		String[] parametersArray = { parameter };
		return runAs(psexecPath, userName, password, targetMachine, parametersArray, environmentMap);
	}

	public static SystemCommandExecutorResponse runAs(final String psexecPath, final String userName,
			final String password, final String targetMachine, final String[] parametersArray) {
		return runAs(psexecPath, userName, password, targetMachine, parametersArray, null);
	}

	public static SystemCommandExecutorResponse runAs(final String psexecPath, final String userName,
			final String password, final String targetMachine, final String[] parametersArray,
			final Map<String, String> environmentMap) {
		String[] psExecString = { psexecPath, "\\\\" + targetMachine, "/accepteula", "-u", userName, "-p", password,
				"-e" };
		String[] concatenatedArray = ArrayUtils.concatenate1DimArrays(psExecString, parametersArray);
		List<String> commands = new ArrayList<String>(Arrays.asList(concatenatedArray));
		ListUtils.getAsDelimitedString(commands, Level.FINEST);
		return run(commands, environmentMap);
	}

}
