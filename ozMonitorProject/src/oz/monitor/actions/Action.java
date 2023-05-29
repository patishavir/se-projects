package oz.monitor.actions;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.io.filefilter.FileFilterRegExpression;
import oz.infra.logging.jul.JulUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.thread.ThreadUtils;
import oz.monitor.OzMonitorParameters;
import oz.monitor.common.OzMonitorResponse;

public class Action {
	private static String serverPidFilepath = null;
	private static String serviceName = null;
	private static String killCommandPath = null;
	private static String scPath = null;
	private static long serviceQueryLoopInterval = 30000L;
	private static long killLoopInterval = 2000L;
	private static String serviceIsRunningMessage = null;
	private static String serviceIsStoppedMessage = null;
	private static final String RUNNING = "STATE              : 4  RUNNING";
	private static final String STOPPED = "STATE              : 1  STOPPED";

	private static String saveLogsFolder = null;
	private static String wasLogsFolderPath = null;
	private static String p8LogsFolderPath = null;
	private static String p8LogsRegExpression = null;
	private static String operations = null;
	private static OzMonitorActionsEnum[] operationsArray = null;
	private static Logger logger = JulUtils.getLogger();

	private static String getWasPid() {
		String pid = null;
		File serverPidFile = new File(serverPidFilepath);
		if (serverPidFile.isFile()) {
			pid = FileUtils.readTextFile(serverPidFile).trim();
		}
		return pid;
	}

	private static void initializeFields(final String propertiesFileRelativePath) {
		String rootFolderPath = OzMonitorParameters.getRootFolderPath();
		String actionPropertiesFilePath = PathUtils.getFullPath(rootFolderPath,
				propertiesFileRelativePath);
		logger.info(actionPropertiesFilePath);
		FileUtils.terminateIfFileDoesNotExist(actionPropertiesFilePath);
		ReflectionUtils.setStaticFieldsFromPropertiesFilePath(actionPropertiesFilePath, Action.class);
		logger.info(serviceName);
		serviceIsRunningMessage = serviceName + " is running now!";
		serviceIsStoppedMessage = serviceName + " is stopped now!";
	}

	private static void moveLogs() {
		logger.info("starting ...");
		String targetFolder = PathUtils.getFullPath(saveLogsFolder, DateTimeUtils.getTimeStamp());
		FolderUtils.moveFolderContents(wasLogsFolderPath, targetFolder, null, true);
		FileFilterRegExpression fileFilterRegExpression = new FileFilterRegExpression(
				p8LogsRegExpression, true);
		FolderUtils.moveFolderContents(p8LogsFolderPath, targetFolder, fileFilterRegExpression,
				true);
		logger.info("done ...");
	}

	public static OzMonitorResponse processOperations(final String propertiesFileRelativePath) {
		initializeFields(propertiesFileRelativePath);
		OzMonitorResponse ozMonitorResponse = null;
		for (int i = 0; i < operationsArray.length; i++) {
			switch (operationsArray[i]) {
			case KILL_PROCESS:
				runKill(getWasPid());
				break;
			case WAIT_FOE_SERVICE_TO_STOP:
				logger.info("wait for service " + serviceName + " to reach stopped state ...");
				waitForServiceToReachState(STOPPED, serviceIsStoppedMessage);
				break;
			case MOVE_LOGS:
				moveLogs();
				break;
			case START_WAS_SERVER:
				logger.info("starting service " + serviceName + " ...");
				String[] scStartParameters = { scPath, "start", serviceName };
				runCommand(scStartParameters);
				break;
			case WAIT_FOR_SERVICE_TO_COME_UP:
				logger.info("waiting for service " + serviceName + " to come up ...");
				SystemCommandExecutorResponse systemCommandExecutorResponse = waitForServiceToReachState(
						RUNNING, serviceIsRunningMessage);
				boolean status = systemCommandExecutorResponse.getReturnCode() == 0;
				ozMonitorResponse = new OzMonitorResponse(status, serviceIsRunningMessage,
						systemCommandExecutorResponse.getStdout().toString());
			}
		}
		logger.info(ozMonitorResponse.getOzMonitorMessage());
		logger.info(ozMonitorResponse.getOzMonitorLongMessage());
		return ozMonitorResponse;
	}

	private static SystemCommandExecutorResponse runCommand(final String[] commandParameters) {
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner
				.run(commandParameters);
		logger.info(systemCommandExecutorResponse.getExecutorResponse());
		return systemCommandExecutorResponse;
	}

	private static void runKill(final String pid) {
		if (pid != null) {
			String[] killParameters = { killCommandPath, "/accepteula", "-t", pid };
			ArrayUtils.printArray(killParameters);
			final String processDoesNotExist = "Process does not exist";
			SystemCommandExecutorResponse systemCommandExecutorResponse = null;
			String stdOut = null;
			do {
				systemCommandExecutorResponse = runCommand(killParameters);
				logger.info(systemCommandExecutorResponse.getExecutorResponse()
						.toString());
				stdOut = systemCommandExecutorResponse.getStdout().toString();
				ThreadUtils.sleep(killLoopInterval, Level.INFO);
			} while (stdOut.indexOf(processDoesNotExist) == OzConstants.STRING_NOT_FOUND);
			logger.info("process " + pid + " has been successfully killed.");
		}
	}

	public static void setKillCommandPath(final String killCommandPath) {
		Action.killCommandPath = killCommandPath;
	}

	public static void setOperations(String operations) {
		Action.operations = operations;
		String[] operationsStringArray = Action.operations.split(OzConstants.COMMA);
		operationsArray = new OzMonitorActionsEnum[operationsStringArray.length];
		for (int i = 0; i < operationsStringArray.length; i++) {
			operationsArray[i] = OzMonitorActionsEnum.valueOf(operationsStringArray[i]);
		}

	}

	public static void setP8LogsFolderPath(final String p8LogsFolderPath) {
		Action.p8LogsFolderPath = p8LogsFolderPath;
	}

	public static void setP8LogsRegExpression(String p8LogsRegExpression) {
		Action.p8LogsRegExpression = p8LogsRegExpression;
	}

	public static void setSaveLogsFolder(final String saveLogsFolder) {
		Action.saveLogsFolder = saveLogsFolder;
	}

	public static void setScPath(final String scPath) {
		Action.scPath = scPath;
	}

	public static void setServerPidFilepath(final String serverPidFilepath) {
		Action.serverPidFilepath = serverPidFilepath;
	}

	public static void setServiceName(final String serviceName) {
		Action.serviceName = serviceName;
	}

	public static void setWasLogsFolderPath(final String wasLogsFolderPath) {
		Action.wasLogsFolderPath = wasLogsFolderPath;
	}

	private static SystemCommandExecutorResponse waitForServiceToReachState(
			final String partialStateString, final String servicsHasReachedStateMessage) {
		String state = null;
		SystemCommandExecutorResponse systemCommandExecutorResponse = null;
		while (state == null) {
			String[] scQueryParameters = { scPath, "query", serviceName };
			systemCommandExecutorResponse = runCommand(scQueryParameters);
			String stdout = systemCommandExecutorResponse.getStdout().toString();
			if (stdout.indexOf(partialStateString) > OzConstants.STRING_NOT_FOUND) {
				state = partialStateString;
				logger.info(servicsHasReachedStateMessage);
			} else {
				ThreadUtils.sleep(serviceQueryLoopInterval, Level.INFO);
			}
		}
		return systemCommandExecutorResponse;
	}
}
