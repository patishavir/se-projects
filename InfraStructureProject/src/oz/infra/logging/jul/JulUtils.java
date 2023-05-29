/*
 * Created on 16/08/2005
 *
 */
package oz.infra.logging.jul;

import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.filters.OzLogFilter;
import oz.infra.logging.jul.formatters.OneLineFormatter;
import oz.infra.logging.jul.levels.SummaryLevel;
import oz.infra.parameters.ParametersUtils;
import oz.infra.system.SystemUtils;
import oz.infra.system.env.EnvironmentUtils;
import oz.infra.varargs.VarArgsUtils;

/**
 * @author Oded
 * 
 */
public class JulUtils {
	public static final String FILE_HANDLER_CLASS_NAME = "java.util.logging.FileHandler";
	private static final String CONSOLE_HANDLER_CLASS_NAME = "java.util.logging.ConsoleHandler";
	private static OzLogFilter ozLogFilter = new OzLogFilter();
	private static final Logger ROOT_LOGGER = Logger.getLogger("");
	private static final String SUMMARY_LOG_NAME = "summary.log";
	private static final Logger logger = JulUtils.getLogger();

	static {
		setLoggerFormatter();
	}

	public static Handler addFileHandler(final String logFilePath, final Boolean... appends) {
		FileHandler fileHandler = getFileHandler(logFilePath, appends);
		if (fileHandler != null) {
			addHandler(fileHandler);
		}
		return fileHandler;
	}

	public static void addFileHandlerFromEnv(final Boolean... appends) {
		Boolean append = VarArgsUtils.getMyArg(Boolean.FALSE, appends);
		String logFilePath = EnvironmentUtils.getEnvironmentVariable(ParametersUtils.LOG_FILE_PATH);
		if (logFilePath != null) {
			String parentFolderPath = PathUtils.getParentFolderPath(logFilePath);
			FolderUtils.createFolderIfNotExists(parentFolderPath);
			JulUtils.addFileHandler(logFilePath, append);
		} else {
			logger.warning("logFilePath has not been specified. File handler has not been started !");
		}
	}

	public static final void addHandler(final Handler handler, final Logger... add2Logger) {
		Logger myLogger = VarArgsUtils.getMyArg(ROOT_LOGGER, add2Logger);
		Handler[] handlers = myLogger.getHandlers();
		listRootHandlers();
		for (int i = 0; i < handlers.length; i++) {
			if (handlers[i].equals(handler)) {
				JOptionPane.showMessageDialog(null, "Handler " + handler.toString() + " is already active! \n");
				return;
			}
		}
		OneLineFormatter oneLineFormatter = new OneLineFormatter();
		handler.setFormatter(oneLineFormatter);
		myLogger.addHandler(handler);
		listRootHandlers();
		setOzFilter();
		return;
	}

	public static StringBuilder closeHandlers(final Logger logger, final String... classNameParameter) {
		Handler[] handlers = logger.getHandlers();
		String message = "closing " + String.valueOf(handlers.length) + " handlers for " + logger.getName();
		StringBuilder sb = new StringBuilder(message);
		for (Handler handler : handlers) {
			String handlerClassName = handler.getClass().getName();
			try {
				if ((classNameParameter.length == 0) || (classNameParameter.length == 1 && handlerClassName.equals(classNameParameter[0]))) {
					sb.append("\nclosing root handler: ");
					sb.append(handlerClassName);
					handler.close();
				}
			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
			}
		}
		sb.append(OzConstants.LINE_FEED);
		return sb;
	}

	public static StringBuilder closeHandlers(final String... classNameParameter) {
		return closeHandlers(ROOT_LOGGER, classNameParameter);
	}

	public static String getConsoleHandlerClassName() {
		return CONSOLE_HANDLER_CLASS_NAME;
	}

	public static FileHandler getFileHandler(final String logFilePath, final Boolean... appends) {
		Boolean append = VarArgsUtils.getMyArg(Boolean.FALSE, appends);
		FileHandler fileHandler = null;
		try {
			fileHandler = new FileHandler(logFilePath, append);
			fileHandler.setFormatter(new OneLineFormatter());
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return fileHandler;
	}

	public static String getFileHandlerCanonicalName() {
		return FILE_HANDLER_CLASS_NAME;
	}

	public static String getSummaryLogFilePathFromEnv() {
		String logsFolder = null;
		String logFilePath = EnvironmentUtils.getEnvironmentVariable(ParametersUtils.SUMMARY_LOG_FILE_PATH);
		if (logFilePath == null) {
			logFilePath = EnvironmentUtils.getEnvironmentVariable(ParametersUtils.LOG_FILE_PATH);
			if (logFilePath != null) {
				logsFolder = PathUtils.getParentFolderPath(logFilePath);
			} else {
				logsFolder = EnvironmentUtils.getEnvironmentVariable(ParametersUtils.LOGS_FOLDER);
			}
			if (logsFolder != null) {
				String logFileName = "summary" + OzConstants.UNDERSCORE + SystemUtils.getHostname() + OzConstants.UNDERSCORE
						+ DateTimeUtils.formatDate() + OzConstants.LOG_SUFFIX;
				logFilePath = PathUtils.getFullPath(logsFolder, logFileName);
			}
		}
		return logFilePath;
	}

	public static Logger getLogger(final Formatter... formatters) {
		String callerClassName = SystemUtils.getCallerClassName();
		return getLogger(callerClassName, true, formatters);
	}

	public static Logger getLogger(final String loggerName, final boolean isUseParentHandlers, final Formatter... formatters) {
		Logger myLogger = Logger.getLogger(loggerName);
		Formatter formatter = VarArgsUtils.getMyArg(new OneLineFormatter(), formatters);
		setLoggerFormatter(myLogger, formatter);
		setOzFilter();
		myLogger.setUseParentHandlers(isUseParentHandlers);
		return myLogger;
	}

	public static Logger getLogger(final String loggerName, final Formatter... formatters) {
		return getLogger(loggerName, true, formatters);
	}

	public static Logger getLogger(final String loggerName, final Level level, final Formatter... formatters) {
		Logger myLogger = getLogger(loggerName, formatters);
		myLogger.setLevel(level);
		return myLogger;
	}

	public static Logger getSummaryLogger(final String logFilePath, final Formatter... formatters) {
		SummaryLevel summary = new SummaryLevel();
		Logger myLogger = getLogger(SUMMARY_LOG_NAME, summary, formatters);
		String summarylogFilePath = logFilePath;
		if (summarylogFilePath == null) {
			summarylogFilePath = getSummaryLogFilePathFromEnv();
		}
		if (summarylogFilePath != null) {
			FileHandler fh = getFileHandler(summarylogFilePath, true);
			myLogger.addHandler(fh);
		}
		return myLogger;
	}

	public static StringBuilder listRootHandlers() {
		StringBuilder sb = new StringBuilder("**************");
		Handler[] handlers = ROOT_LOGGER.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			String message = "\nRoot handlers: " + handlers[i].getClass().getName();
			sb.append(message);
		}
		logger.finest(sb.toString());
		return sb;
	}

	public static void logBuildInfo() {
		String packageName = SystemUtils.getCallerPackageName();
		logger.finest(packageName);
		String buildInfoPath = packageName.replaceAll("\\.", OzConstants.SLASH) + OzConstants.SLASH + "buildInfo.txt";
		logger.finest(buildInfoPath);
		logger.info(FileUtils.readTextFileFromClassPath(OzConstants.SLASH + buildInfoPath));
	}

	public static void removeHandler(final Handler handler) {
		Handler[] handlers = ROOT_LOGGER.getHandlers();
		listRootHandlers();
		// remove handler
		for (int i = 0; i < handlers.length; i++) {
			if (handlers[i].equals(handler)) {
				handlers[i].close();
				ROOT_LOGGER.removeHandler(handlers[i]);
			}
		}
		listRootHandlers();
		return;
	}

	public static void removeHandlers(final Logger logger, final String canonicalName) {
		Handler[] handlers = logger.getHandlers();
		listRootHandlers();
		// remove handler
		for (int i = 0; i < handlers.length; i++) {
			if (handlers[i].getClass().getCanonicalName().equals(canonicalName)) {
				handlers[i].close();
				logger.removeHandler(handlers[i]);
			}
		}
		listRootHandlers();
		return;

	}

	public static void removeHandlers(final String canonicalName) {
		removeHandlers(ROOT_LOGGER, canonicalName);
	}

	private static final void setHandlerFormatter(final Handler handler, final Formatter... formatters) {
		handler.setFormatter(VarArgsUtils.getMyArg(new OneLineFormatter(), formatters));
	}

	private static void setLoggerFormatter(final Formatter... formatters) {
		setLoggerFormatter(ROOT_LOGGER, formatters);
	}

	private static void setLoggerFormatter(final Logger logger, final Formatter... formatters) {
		Handler[] handlers = logger.getHandlers();
		Formatter formatter = VarArgsUtils.getMyArg(new OneLineFormatter(), formatters);
		int handlerCount = 0;
		for (int i = 0; i < handlers.length; i++) {
			handlers[i].setFormatter(formatter);
			logger.finest("logger: " + logger.getName() + " formatter " + formatter.toString() + " set in " + handlers[i].toString());
			handlerCount++;
		}
		logger.finest("logger: " + logger.getName() + " " + String.valueOf(handlerCount) + " handlers have been processed.");
	}

	public static final void setLoggingLevel(final String levelString) {
		Handler[] handlers = ROOT_LOGGER.getHandlers();
		Level level = Level.parse(levelString);
		for (int i = 0; i < handlers.length; i++) {
			handlers[i].setLevel(level);
		}
		ROOT_LOGGER.setLevel(level);
		logger.log(level, "Logging level set to " + levelString);
	}

	private static void setOzFilter() {
		Handler[] handlers = ROOT_LOGGER.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			handlers[i].setFilter(ozLogFilter);
		}
	}

	public static FileHandler switchFileHandler(final FileHandler currentFileHandler, final String logFilePath) {
		if (currentFileHandler != null) {
			removeHandler(currentFileHandler);
		}
		logger.finest("logFilePath: ".concat(logFilePath));
		FileHandler fileHandler = null;
		try {
			fileHandler = new FileHandler(logFilePath);
			addHandler(fileHandler);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return fileHandler;
	}
}
