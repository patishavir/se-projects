package oz.utils.p8;

import java.util.logging.Logger;

import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;

public class P8UsageParameters {

	private static String excelFolderPath = null;
	private static String excelFileName = null;
	private static String sqlSriptPath = null;
	private static String dbPropertiesFile = null;
	private static String smtpMailTo = null;
	private static String rootFolderPath = null;
	private static final Logger logger = JulUtils.getLogger();

	public static String getDbPropertiesFile() {
		return dbPropertiesFile;
	}

	public static String getExcelFileName() {
		return excelFileName;
	}

	public static String getExcelFolderPath() {
		return excelFolderPath;
	}

	public static String getRootFolderPath() {
		return rootFolderPath;
	}

	public static String getSmtpMailTo() {
		return smtpMailTo;
	}

	public static String getSqlSriptPath() {
		return sqlSriptPath;
	}

	public static void processParameters(final String propertiesFilePath) {
		ParametersUtils.processPatameters(propertiesFilePath, P8UsageParameters.class);
	}

	public static void setDbPropertiesFile(final String dbPropertiesFile) {
		P8UsageParameters.dbPropertiesFile = PathUtils.getFullPath(rootFolderPath, dbPropertiesFile);
	}

	public static void setExcelFileName(final String excelFileName) {
		P8UsageParameters.excelFileName = excelFileName;
	}

	public static void setExcelFolderPath(final String excelFolderPath) {
		P8UsageParameters.excelFolderPath = excelFolderPath;
	}

	public static void setRootFolderPath(String rootFolderPath) {
		P8UsageParameters.rootFolderPath = rootFolderPath;
	}

	public static void setSmtpMailTo(String smtpMailTo) {
		P8UsageParameters.smtpMailTo = smtpMailTo;
	}

	public static void setSqlSriptPath(final String sqlSriptPath) {
		P8UsageParameters.sqlSriptPath = PathUtils.getFullPath(rootFolderPath, sqlSriptPath);
	}
}