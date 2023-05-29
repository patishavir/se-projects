package oz.utils.db.selectinsert;

import oz.infra.io.PathUtils;
import oz.infra.parameters.ParametersUtils;

public class SelectInsertParameters {

	public static String dbpropertiesFolderPath = null;
	public static String sourceDb = null;
	public static String targetDbs = null;
	public static String selectStatement = null;
	public static String deleteStatement = null;
	public static String insertStatement = null;
	private static String rootFolderPath = null;

	public static String getDbpropertiesFolderPath() {
		return dbpropertiesFolderPath;
	}

	public static String getDeleteStatement() {
		return deleteStatement;
	}

	public static String getInsertStatement() {
		return insertStatement;
	}

	public static String getRootFolderPath() {
		return rootFolderPath;
	}

	public static String getSelectStatement() {
		return selectStatement;
	}

	public static String getSourceDb() {
		return sourceDb;
	}

	public static String getTargetDbs() {
		return targetDbs;
	}

	public static void processParameters(final String propertiesFilePath) {
		ParametersUtils.processPatameters(propertiesFilePath, SelectInsertParameters.class);
		dbpropertiesFolderPath = PathUtils.getFullPath(rootFolderPath, dbpropertiesFolderPath);

	}

	public static void setDbpropertiesFolderPath(String dbpropertiesFolderPath) {
		SelectInsertParameters.dbpropertiesFolderPath = dbpropertiesFolderPath;
	}

	public static void setDeleteStatement(String deleteStatement) {
		SelectInsertParameters.deleteStatement = deleteStatement;
	}

	public static void setInsertStatement(String insertStatement) {
		SelectInsertParameters.insertStatement = insertStatement;
	}

	public static void setRootFolderPath(String rootFolderPath) {
		SelectInsertParameters.rootFolderPath = rootFolderPath;
	}

	public static void setSelectStatement(String selectStatement) {
		SelectInsertParameters.selectStatement = selectStatement;
	}

	public static void setSourceDb(String sourceDb) {
		SelectInsertParameters.sourceDb = sourceDb;
	}

	public static void setTargetDbs(String targetDbs) {
		SelectInsertParameters.targetDbs = targetDbs;
	}

}
