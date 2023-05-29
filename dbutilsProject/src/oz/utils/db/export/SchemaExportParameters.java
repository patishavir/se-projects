package oz.utils.db.export;

import java.util.Properties;
import java.util.logging.Level;

import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.reflection.ReflectionUtils;

public class SchemaExportParameters {
	private static String userName = null;
	private static String password = null;
	private static String passwordDelimiter = null;
	private static String driverClassName = null;
	private static String schemaName = null;
	private static String folderPath = null;
	private static String dbpropertiesFolderPath = null;
	private static String databases = null;
	private static String[] databasesArray = null;
	private static String excludeTables = null;
	private static String[] excludeTablesArray = null;
	private static String rootFolderPath = null;
	private static String compareToolPath = null;
	private static String metaDataOnly = "no";

	public static String getCompareToolPath() {
		return compareToolPath;
	}

	public static Properties getDatabaseProperties(final String database) {
		String databasePropetrtiesFilePath = PathUtils.getFullPath(dbpropertiesFolderPath, database + ".properties");
		Properties databaseProperties = PropertiesUtils.loadPropertiesFile(databasePropetrtiesFilePath);
		PropertiesUtils.printProperties(databaseProperties, Level.INFO);
		if (databaseProperties.getProperty("userName") == null) {
			databaseProperties.setProperty("userName", userName);
		}
		if (databaseProperties.getProperty("password") == null) {
			databaseProperties.setProperty("password", password);
		}
		if (databaseProperties.getProperty("passwordDelimiter") == null) {
			databaseProperties.setProperty("passwordDelimiter", passwordDelimiter);
		}
		if (databaseProperties.getProperty("driverClassName") == null) {
			databaseProperties.setProperty("driverClassName", driverClassName);
		}
		if (databaseProperties.getProperty("schemaName") == null) {
			databaseProperties.setProperty("schemaName", schemaName);
		}
		if (databaseProperties.getProperty("metaDataOnly") == null) {
			databaseProperties.setProperty("metaDataOnly", metaDataOnly);
		}
		if (databaseProperties.getProperty("excludeTables") == null) {
			databaseProperties.setProperty("excludeTables", excludeTables);
		}
		String targetFolderPath = PathUtils.getFullPath(folderPath, database);
		targetFolderPath = PathUtils.getFullPath(targetFolderPath, schemaName);
		if (databaseProperties.getProperty("folderPath") == null) {
			databaseProperties.setProperty("folderPath", targetFolderPath);
		}
		return databaseProperties;
	}

	public static String[] getDatabasesArray() {
		return databasesArray;
	}

	public static String[] getExcludeTablesArray() {
		return excludeTablesArray;
	}

	public static String getFolderPath() {
		return folderPath;
	}

	public static String getPasswordDelimiter() {
		return passwordDelimiter;
	}

	public static void processParameters(final String schemaExportPropertiesFilePath) {
		rootFolderPath = PathUtils.getParentFolderPath(schemaExportPropertiesFilePath);
		FileUtils.terminateIfFileDoesNotExist(schemaExportPropertiesFilePath);
		Properties schemaExportProperties = PropertiesUtils.loadPropertiesFile(schemaExportPropertiesFilePath);
		ReflectionUtils.setFieldsFromProperties(schemaExportProperties, SchemaExportParameters.class);
	}

	public static void setCompareToolPath(final String compareToolPath) {
		SchemaExportParameters.compareToolPath = compareToolPath;
	}

	public static void setDatabases(final String databases) {
		SchemaExportParameters.databases = databases;
		SchemaExportParameters.databasesArray = SchemaExportParameters.databases.split(OzConstants.COMMA);
	}

	public static void setDbpropertiesFolderPath(final String dbpropertiesFolderPath) {
		SchemaExportParameters.dbpropertiesFolderPath = PathUtils.getFullPath(rootFolderPath, dbpropertiesFolderPath);

	}

	public static void setDriverClassName(final String driverClassName) {
		SchemaExportParameters.driverClassName = driverClassName;
	}

	public static void setExcludeTables(String excludeTables) {
		SchemaExportParameters.excludeTables = excludeTables;
		excludeTablesArray = excludeTables.split(OzConstants.COMMA);
	}

	public static void setFolderPath(final String folderPath) {
		SchemaExportParameters.folderPath = folderPath;
	}

	public static void setMetaDataOnly(String metaDataOnly) {
		SchemaExportParameters.metaDataOnly = metaDataOnly;
	}

	public static void setPassword(final String password) {
		SchemaExportParameters.password = password;
	}

	public static void setPasswordDelimiter(String passwordDelimiter) {
		SchemaExportParameters.passwordDelimiter = passwordDelimiter;
	}

	public static void setSchemaName(final String schemaName) {
		SchemaExportParameters.schemaName = schemaName;
	}

	public static void setUserName(final String userName) {
		SchemaExportParameters.userName = userName;
	}
}
