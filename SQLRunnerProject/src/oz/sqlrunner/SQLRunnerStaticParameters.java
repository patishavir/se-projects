package oz.sqlrunner;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;

public class SQLRunnerStaticParameters {
	private static Logger logger = JulUtils.getLogger();

	private static final String CONNECT_GUI_XML_FILE = "/oz/sqlrunner/gui/xml/Connect.xml";
	private static final String FILTER_GUI_XML_FILE = "/oz/sqlrunner/gui/xml/Filter.xml";
	private static final String EXPORT_GUI_XML_FILE = "/oz/sqlrunner/gui/xml/ExportFileChooser.xml";
	private static final String IMPORT_GUI_XML_FILE = "/oz/sqlrunner/gui/xml/ImportFileChooser.xml";
	private static final String OBJECT_EXPLORER_CONTEXT_MENU = "/oz/sqlrunner/gui/xml/ObjectExplorerContextMenu.xml";

	private static Properties databasesProperties = null;
	private static String databasePropertiesFilePath = null;

	public static final int MINIMAL_SQL_STATEMENT_LENGTH = 5;
	private static String sqlStatementDelimiter = OzConstants.SEMICOLON;
	private static SQLRunnerConnectionParameters sqlRunnerConnectionParameters = new SQLRunnerConnectionParameters();
	private static String defaultBrowseMaxNumberOfRecords = "50";
	private static String rootFolderPath = null;

	public static String getConnectGuiXmlFile() {
		return CONNECT_GUI_XML_FILE;
	}

	public static SQLRunnerConnectionParameters getConnectionSqlRunnerParameters() {
		return sqlRunnerConnectionParameters;
	}

	public static Properties getDatabaseProperties() {
		if (databasesProperties == null) {
			try {
				databasesProperties = PropertiesUtils.loadPropertiesFile(databasePropertiesFilePath);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.warning(ex.getMessage());
			}
		}
		return databasesProperties;
	}

	public static String getDefaultBrowseMaxNumberOfRecords() {
		return defaultBrowseMaxNumberOfRecords;
	}

	public static String getExportGuiXmlFile() {
		return EXPORT_GUI_XML_FILE;
	}

	public static String getFilterGuiXmlFile() {
		return FILTER_GUI_XML_FILE;
	}

	public static String getImportGuiXmlFile() {
		return IMPORT_GUI_XML_FILE;
	}

	public static String getobjectExplorerContextMenuGuiXmlFile() {
		return OBJECT_EXPLORER_CONTEXT_MENU;
	}

	public static String getSqlStatementDelimiter() {
		return sqlStatementDelimiter;
	}

	public static void setDatabasePropertiesFilePath(final String databasePropertiesFilePath) {
		SQLRunnerStaticParameters.databasePropertiesFilePath = databasePropertiesFilePath;
	}

	public static void setDefaultBrowseMaxNumberOfRecords(String defaultBrowseMaxNumberOfRecords) {
		SQLRunnerStaticParameters.defaultBrowseMaxNumberOfRecords = defaultBrowseMaxNumberOfRecords;
	}

	public static void setSqlStatementDelimiter(String sqlStatementDelimiter) {
		SQLRunnerStaticParameters.sqlStatementDelimiter = sqlStatementDelimiter;
	}

	public static void setRootFolderPath(String rootFolderPath) {
		SQLRunnerStaticParameters.rootFolderPath = rootFolderPath;
	}
}
