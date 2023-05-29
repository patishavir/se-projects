package oz.sqlrunner;

import java.sql.Connection;
import java.util.Observable;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.JScrollPane;

import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.preferences.PreferencesUtils;
import oz.sqlrunner.enums.DatabaseProductEnum;
import oz.sqlrunner.gui.SQLRunnerJFrame;
import oz.sqlrunner.gui.SQLRunnerJTabbedPane;
import oz.sqlrunner.gui.SQLRunnerToolBar;
import oz.sqlrunner.handlers.ConnectionHandler;
import oz.sqlrunner.handlers.FilterHandler;

public class SQLRunnerConnectionParameters extends Observable {
	public static final String NOT_CONNECTED = "Not connected ";
	private static Logger logger = JulUtils.getLogger();

	/*
	 * Database specific attributes
	 */

	private Connection connection = null;
	private String url = null;
	private String creatorColumName = null;
	private String creatorFilter = null;
	private String creatorFilterRegKey = null;
	private String dataBaseDisplayName = null;
	private String databaseObjectTypeFilter = null;
	private DatabaseProductEnum dataBaseProductEnum = null;
	private String nameFilter = null;
	private String nameFilterRegKey = null;
	private JScrollPane objectExplorerResultSetJScrollPane = null;
	private String password = null;
	private String[] schemasArray = null;
	private SQLRunnerJFrame sqlRunnerJFrame = null;
	private SQLRunnerJTabbedPane sqlRunnerJTabbedPane = null;
	private SQLRunnerToolBar sqlRunnerToolBar = null;
	private String tableCreator = null;
	private String tableName = null;
	private String tableNameColumName = null;
	private String userName = null;
	private boolean useDatabaseMetaData = true;

	public final Connection getConnection() {
		return connection;
	}

	public final String getCreatorColumName() {
		return creatorColumName;
	}

	public final String getCreatorFilter() {
		creatorFilterRegKey = dataBaseDisplayName + FilterHandler.CREATOR;
		if (creatorFilter == null) {
			creatorFilter = PreferencesUtils.get(SQLRunnerConnectionParameters.class, creatorFilterRegKey);
		}
		return creatorFilter;
	}

	public final String getDataBaseDisplayName() {
		if (dataBaseDisplayName == null) {
			dataBaseDisplayName = PreferencesUtils.get(this.getClass(), ConnectionHandler.DATABASE);
		}
		return dataBaseDisplayName;
	}

	public final String getDatabaseObjectTypeFilter() {
		return databaseObjectTypeFilter;
	}

	public final DatabaseProductEnum getDataBaseProductEnum() {
		return dataBaseProductEnum;
	}

	public final String getDBDriverClassName() {
		String dbDriverClassName = dataBaseProductEnum.getJdbcDriverClass();
		logger.info("dbDriverClassName: ".concat(dbDriverClassName));
		return dbDriverClassName;
	}

	public final String getNameFilter() {
		nameFilterRegKey = dataBaseDisplayName + FilterHandler.NAME;
		if (nameFilter == null) {
			nameFilter = PreferencesUtils.get(SQLRunnerConnectionParameters.class, nameFilterRegKey);
		}
		return nameFilter;
	}

	public final String getObjectBrowsersqlStatementString() {
		return dataBaseProductEnum.getObjectBrowsersqlStatementString();
	}

	public final JScrollPane getObjectExplorerResultSetJScrollPane() {
		return objectExplorerResultSetJScrollPane;
	}

	public final String getPassword() {
		if (password == null) {
			String encryptedPasswordString = PreferencesUtils.get(this.getClass(), ConnectionHandler.PASSWORD);
			if (encryptedPasswordString != null) {
				password = CryptographyUtils.decryptString(encryptedPasswordString);
			}
		}
		return password;
	}

	public final String[] getSchemasArray() {
		return schemasArray;
	}

	public final SQLRunnerJFrame getSqlRunnerJFrame() {
		return sqlRunnerJFrame;
	}

	public final SQLRunnerJTabbedPane getSqlRunnerJTabbedPane() {
		return sqlRunnerJTabbedPane;
	}

	public final SQLRunnerToolBar getSqlRunnerToolBar() {
		return sqlRunnerToolBar;
	}

	public final String getTableCreator() {
		return tableCreator;
	}

	public final String getTableName() {
		return tableName;
	}

	public final String getTableNameColumName() {
		return tableNameColumName;
	}

	public String getUrl() {
		return url;
	}

	public final String getUserName() {
		if (userName == null) {
			userName = PreferencesUtils.get(this.getClass(), ConnectionHandler.USERNAME);
		}
		return userName;
	}

	public final boolean isUseDatabaseMetaData() {
		return useDatabaseMetaData;
	}

	public final void setConnection(final Connection connection) {
		try {
			this.connection = connection;
			String connectionMessage = NOT_CONNECTED;
			if (connection != null && !connection.isClosed()) {
				connectionMessage = "Connected to " + getDataBaseDisplayName() + " User: " + userName + " ";
			}
			setChanged();
			notifyObservers(connectionMessage);
			if (connection == null) {
				setSchemasArray(null);
				userName = null;
				password = null;
				dataBaseDisplayName = null;
				dataBaseProductEnum = null;
				nameFilter = null;
				nameFilterRegKey = null;
				creatorFilter = null;
				creatorFilterRegKey = null;
				databaseObjectTypeFilter = null;
			}
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public final void setCreatorColumName(final String creatorColumName) {
		this.creatorColumName = creatorColumName;
	}

	public final void setCreatorFilter(final String creatorFilter) {
		this.creatorFilter = creatorFilter;
		PreferencesUtils.putOrRemove(this.getClass(), creatorFilterRegKey, creatorFilter);
	}

	public final void setDataBaseDisplayName(final String dataBaseDisplayName) {
		this.dataBaseDisplayName = dataBaseDisplayName;
		PreferencesUtils.put(this.getClass(), ConnectionHandler.DATABASE, dataBaseDisplayName);
	}

	public final void setDatabaseObjectTypeFilter(final String databaseObjectTypeFilter) {
		this.databaseObjectTypeFilter = databaseObjectTypeFilter;
	}

	public final void setDataBaseProductProperties(final String dataBasedataBaseDisplayName) {
		Properties databaseProperties = SQLRunnerStaticParameters.getDatabaseProperties();
		String[] databasePropertiesArray = databaseProperties.getProperty(dataBasedataBaseDisplayName).split(OzConstants.COMMA);
		url = databasePropertiesArray[0];
		String dataBaseProductString = databasePropertiesArray[1];
		dataBaseProductEnum = DatabaseProductEnum.valueOf(dataBaseProductString);
		if (databasePropertiesArray.length > 2) {
			useDatabaseMetaData = databasePropertiesArray[2].equalsIgnoreCase("USE_DATABASE_METADATA=YES");
		}
	}

	public final void setNameFilter(final String nameFilter) {
		this.nameFilter = nameFilter;
		PreferencesUtils.putOrRemove(this.getClass(), nameFilterRegKey, nameFilter);
	}

	public final void setObjectExplorerResultSetJScrollPane(final JScrollPane objectExplorerResultSetJScrollPane) {
		this.objectExplorerResultSetJScrollPane = objectExplorerResultSetJScrollPane;
	}

	public final void setPassword(final String password) {
		this.password = password;
		String encryptesPasswordString = null;
		if (password != null) {
			encryptesPasswordString = CryptographyUtils.encryptString(password);
		}
		if (encryptesPasswordString != null) {
			PreferencesUtils.put(this.getClass(), ConnectionHandler.PASSWORD, encryptesPasswordString);
		} else {
			PreferencesUtils.remove(this.getClass(), ConnectionHandler.PASSWORD);
		}
	}

	public final void setSchemasArray(final String[] schemasArray) {
		this.schemasArray = schemasArray;
	}

	public final void setSqlRunnerJFrame(final SQLRunnerJFrame sqlRunnerJFrame) {
		this.sqlRunnerJFrame = sqlRunnerJFrame;
	}

	public final void setSqlRunnerJTabbedPane(final SQLRunnerJTabbedPane jTabbedPane) {
		sqlRunnerJTabbedPane = jTabbedPane;
	}

	public final void setSqlRunnerToolBar(final SQLRunnerToolBar sqlRunnerToolBar) {
		this.sqlRunnerToolBar = sqlRunnerToolBar;
	}

	public final void setTableCreator(final String tableCreator) {
		this.tableCreator = tableCreator;
	}

	public final void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	public final void setTableNameColumName(final String tableNameColumName) {
		this.tableNameColumName = tableNameColumName;
	}

	public final void setUserName(final String userName) {
		this.userName = userName;
		PreferencesUtils.put(this.getClass(), ConnectionHandler.USERNAME, userName);
	}
}
