package oz.clearcase.clearfsimport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.clearcase.infra.ClearCaseUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.FileUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.infra.system.env.EnvironmentUtils;

public class ClearFsImportUtils {
	public static final String RECURSE = "-recurse";
	public static final String NSETEVENT = "-nsetevent";
	private static final String COMMANDS_THAT_FAILED_PATH = "commandsThatFailedPath";

	private static final Logger logger = JulUtils.getLogger();

	public static String getClearfsimportPath() {
		String clearfsimportPath = ClearCaseUtils.getClearCaseHome() + "\\bin\\clearfsimport.exe";
		File clearfsimport = new File(clearfsimportPath);
		if (!clearfsimport.exists()) {
			clearfsimportPath = null;
		}
		return clearfsimportPath;
	}

	public static SystemCommandExecutorResponse runClearfsimport(final String sourcePath,
			final String targetVOBdirectoryPath, final List<String> parameterList, final String... comments) {
		logger.info(SystemUtils.getRunInfo());
		logger.finest("starting clearfsimport. source path: " + sourcePath + " target path: " + targetVOBdirectoryPath
				+ " user: " + SystemPropertiesUtils.getUserName());
		List<String> commandList = new ArrayList<String>();
		Map<String, String> environmentMap = new HashMap<String, String>();
		String comment = null;
		if (comments.length > 0) {
			comment = comments[0];
		}
		commandList.add(getClearfsimportPath());
		for (String parameter : parameterList) {
			commandList.add(parameter);
		}
		if (comment != null) {
			commandList.add("-comment");
			commandList.add(comment);
		}
		commandList.add(sourcePath);
		commandList.add(targetVOBdirectoryPath);
		String commandLine = ListUtils.getAsDelimitedString(commandList, OzConstants.BLANK);
		String logLine = ListUtils.getAsTitledDelimitedString(commandList,
				"User: " + SystemPropertiesUtils.getUserName() + " Command line: ", Level.FINEST, OzConstants.BLANK);
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(commandList,
				environmentMap);
		String scerString = systemCommandExecutorResponse.getExecutorResponse();
		logger.info(logLine + SystemUtils.LINE_SEPARATOR + scerString);
		if (systemCommandExecutorResponse.getReturnCode() != 0) {
			logger.warning("clearfsimport has failed: return code: "
					+ String.valueOf(systemCommandExecutorResponse.getReturnCode()));
			writeFailedCommand(commandLine);
		}
		return systemCommandExecutorResponse;
	}

	public static SystemCommandExecutorResponse runClearfsimport(final String sourcePath,
			final String targetVOBdirectory, final String... comments) {
		List<String> parameters = new ArrayList<String>();
		parameters.add(RECURSE);
		return runClearfsimport(sourcePath, targetVOBdirectory, parameters, comments);

	}

	private static void writeFailedCommand(final String commandLine) {
		String commandsThatFailedPath = EnvironmentUtils.getEnvironmentVariable(COMMANDS_THAT_FAILED_PATH);
		if (commandsThatFailedPath != null) {
			File commandsThatFailedFile = new File(commandsThatFailedPath);
			File parentFile = commandsThatFailedFile.getParentFile();
			if (!parentFile.exists()) {
				logger.warning("folder " + parentFile.getAbsolutePath() + " does not exist!");
			} else {
				String commandString = SystemUtils.LINE_SEPARATOR + "Rem " + DateTimeUtils.formatDateTime() + " user: "
						+ SystemPropertiesUtils.getUserName() + SystemUtils.LINE_SEPARATOR + commandLine;
				FileUtils.appendFile(commandsThatFailedFile, commandString);
				logger.info(commandString);
			}
		} else {
			logger.warning("Enviroment variable " + COMMANDS_THAT_FAILED_PATH + " is not defined!");
		}
	}
}
