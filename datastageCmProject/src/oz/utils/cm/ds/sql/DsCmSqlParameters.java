package oz.utils.cm.ds.sql;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.FileDirectoryEnum;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.string.StringUtils;

public class DsCmSqlParameters {
	private static String rootFolderPath = null;
	private static String sourceFolderPath = null;
	private static String sourceFolderName = null;
	private static String database2UserMap = null;
	private static String sqlPlusPath = null;
	private static String encryptionMethod = null;
	private static String logsFolder = null;
	private static String sqlPrefixFilePath = null;
	private static String sqlPrefix = null;
	private static String additionalMailRecepients = null;
	private static final String SQL_FOLDER_NAME = "sql";

	private static Logger logger = JulUtils.getLogger();

	public static String getAdditionalMailRecepients() {
		return additionalMailRecepients;
	}

	public static String getDatabase2UserMap() {
		return database2UserMap;
	}

	public static String getEncryptionMethod() {
		return encryptionMethod;
	}

	public static String getLogsFolder() {
		return logsFolder;
	}

	public static String getSourceFolderPath() {
		return sourceFolderPath;
	}

	public static String getSqlPlusPath() {
		return sqlPlusPath;
	}

	public static String getSqlPrefix() {
		return sqlPrefix;
	}

	public static String getSqlPrefixFilePath() {
		return sqlPrefixFilePath;
	}

	public static void processParameters(final String propertiesFilePath) {
		logger.info(StringUtils.concat("Staring " + SQL_FOLDER_NAME + " processing ... "));
		logger.finest("Current dir: " + FileUtils.getCurrentDir());
		setRootFolderPath(propertiesFilePath);
		logger.info("processing " + propertiesFilePath + " ...");
		Properties dscmSqlProperties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		dscmSqlProperties = PropertiesUtils.updatePropertiesUsingEnvironmentVarialbes(dscmSqlProperties);
		ReflectionUtils.setFieldsFromProperties(dscmSqlProperties, DsCmSqlParameters.class);
	}

	public static void setAdditionalMailRecepients(final String additionalMailRecepients) {
		DsCmSqlParameters.additionalMailRecepients = additionalMailRecepients;
	}

	public static void setDatabase2UserMap(final String database2UserMap) {
		DsCmSqlParameters.database2UserMap = PathUtils.getFullPath(rootFolderPath, database2UserMap);
		logger.info("database2UserMap: " + DsCmSqlParameters.database2UserMap);
	}

	public static void setEncryptionMethod(final String encryptionMethod) {
		DsCmSqlParameters.encryptionMethod = encryptionMethod;
	}

	public static void setLogsFolder(final String logsFolder) {
		DsCmSqlParameters.logsFolder = logsFolder;
	}

	private static void setRootFolderPath(final String filePath) {
		rootFolderPath = new File(filePath).getParentFile().getAbsolutePath();
		logger.info("rootFolderPath: ".concat(rootFolderPath));
	}

	public static void setSourceFolderPath(final String sourceFolderPath) {
		sourceFolderName = SQL_FOLDER_NAME;
		DsCmSqlParameters.sourceFolderPath = PathUtils.getFullPath(rootFolderPath, sourceFolderPath);
		DsCmSqlParameters.sourceFolderPath = PathUtils.getFullPath(DsCmSqlParameters.sourceFolderPath, sourceFolderName);
		logger.info("sourceFolderPath: ".concat(DsCmSqlParameters.sourceFolderPath));
	}

	public static void setSqlPlusPath(final String sqlPlusPath) {
		DsCmSqlParameters.sqlPlusPath = FileUtils.getExisting(sqlPlusPath.split(OzConstants.COMMA), FileDirectoryEnum.FILE);
	}

	public static void setSqlPrefixFilePath(final String sqlPrefixFilePath) {
		DsCmSqlParameters.sqlPrefixFilePath = PathUtils.getFullPath(rootFolderPath, sqlPrefixFilePath);
		sqlPrefix = FileUtils.readTextFile(DsCmSqlParameters.sqlPrefixFilePath);
	}
}