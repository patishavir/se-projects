package oz.utils.cm.ds;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.string.StringUtils;

public class DsCmRunParameters {
	private static String environment = null;
	private static boolean runDs = false;
	private static boolean runSql = false;
	private static String rootFolderPath = null;
	private static String fileNameBreakdownDelimiter = OzConstants.TILDE;
	private static String logFilePath = null;

	private static Logger logger = JulUtils.getLogger();

	public static String getEnvironment() {
		return environment;
	}

	public static String getFileNameBreakdownDelimiter() {
		return fileNameBreakdownDelimiter;
	}

	public static boolean isRunDs() {
		return runDs;
	}

	public static boolean isRunSql() {
		return runSql;
	}

	public static void processParameters(final String propertiesFilePath) {
		logger.info(StringUtils.concat("Current dir: ", FileUtils.getCurrentDir(), "  processing " + propertiesFilePath + " ..."));
		ParametersUtils.processPatameters(propertiesFilePath, DsCmRunParameters.class);
	}

	public static void setEnvironment(final String environment) {
		DsCmRunParameters.environment = environment;
	}

	public static void setFileNameBreakdownDelimiter(String fileNameBreakdownDelimiter) {
		DsCmRunParameters.fileNameBreakdownDelimiter = fileNameBreakdownDelimiter;
	}

	public static void setLogFilePath(final String logFilePath) {
		DsCmRunParameters.logFilePath = PathUtils.getFullPath(rootFolderPath, logFilePath);
		JulUtils.addFileHandler(DsCmRunParameters.logFilePath);
		logger.info("logFilePath: " + DsCmRunParameters.logFilePath);
	}

	public static void setRootFolderPath(final String filePath) {
		rootFolderPath = PathUtils.getParentFolderPath(filePath);
		logger.finest(rootFolderPath);
	}

	public static void setRunDs(final boolean runDs) {
		DsCmRunParameters.runDs = runDs;
	}

	public static void setRunSql(final boolean runSql) {
		DsCmRunParameters.runSql = runSql;
	}
}
