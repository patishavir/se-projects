package oz.sqlrunner.handlers;

import java.awt.Component;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import oz.guigenerator.GuiGeneratorDefaultsProviderInterface;
import oz.guigenerator.GuiGeneratorMain;
import oz.guigenerator.GuiGeneratorParamsFileProcessor;
import oz.infra.array.ArrayUtils;
import oz.infra.db.DBUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.exception.sql.SqlExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;

public class ConnectionHandler implements Observer, GuiGeneratorDefaultsProviderInterface {
	private static ConnectionHandler connectionHandler = new ConnectionHandler();

	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	private static final String SAVEPASSWORD = "savepassword";
	public static final String DATABASE = "database";
	public static ConnectionHandler getConnectionHandler() {
		return connectionHandler;
	}
	private Logger logger = JulUtils.getLogger();
	private GuiGeneratorParamsFileProcessor connectGuiGeneratorParamsFileProcessor;
	private String userName = null;
	private String password = null;
	private boolean savePassword = false;

	private String database = null;

	public final void close() {
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		Connection connection = sqlRunnerParameters.getConnection();
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
			}
		}
	}

	public void connect() {
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		String url = sqlRunnerParameters.getUrl();
		String dbDriverClassName = sqlRunnerParameters.getDBDriverClassName();
		Connection connection = null;
		try {
			connection = DBUtils.getConnectionThrowsException(url, userName, password, dbDriverClassName, false);
			connection.setReadOnly(true);
			connection.setAutoCommit(true);
			logger.finest(connection.getMetaData().getDatabaseProductName());
			logger.finest("getAutoCommit: " + String.valueOf(connection.getAutoCommit()));
			sqlRunnerParameters.setConnection(connection);
			sqlRunnerParameters.setDataBaseProductProperties(database);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
			String explanation = null;
			if (ex instanceof SQLException) {
				explanation = SqlExceptionUtils.explainMessage((SQLException) ex);
			}
			String message = ex.getMessage();
			if (explanation != null) {
				message = message.concat("\n").concat(explanation);
			}
			JOptionPane.showMessageDialog(null, message, "Connection failure", JOptionPane.ERROR_MESSAGE);
		}
	}

	public final void disConnect() {
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		Connection connection = sqlRunnerParameters.getConnection();
		logger.finest("Entering disconnect ...");
		try {
			if (connection != null) {
				logger.finest("connection.isClosed() ... " + String.valueOf(connection.isClosed()));
				connection.close();
				logger.info("Connection has been succesfully closed");
				sqlRunnerParameters.setConnection(null);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
	}

	public String getDefaultValue(final String key) {
		String value = null;
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		if (key.equalsIgnoreCase(USERNAME)) {
			value = sqlRunnerParameters.getUserName();
		}
		if (key.equalsIgnoreCase(PASSWORD)) {
			value = sqlRunnerParameters.getPassword();
		}
		if (key.equalsIgnoreCase(DATABASE)) {
			value = sqlRunnerParameters.getDataBaseDisplayName();
		}
		logger.finest(key + "=" + value);
		return value;
	}

	public final String[] getValues(final String key) {
		logger.finest("key: " + key);
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		logger.finest("getDefaultValues " + key);
		if (key.equalsIgnoreCase(DATABASE)) {
			String databaseUsedLast = sqlRunnerParameters.getDataBaseDisplayName();
			Set databaseSet = SQLRunnerStaticParameters.getDatabaseProperties().keySet();
			String[] databaseArray = (String[]) databaseSet.toArray(new String[databaseSet.size()]);
			if (databaseUsedLast != null && databaseUsedLast.length() > 0 && databaseArray.length > 0
					&& (!databaseArray[0].equals(databaseUsedLast))) {
				ArrayUtils.moveEntry(databaseArray, databaseUsedLast, 0);
			}
			Arrays.sort(databaseArray, 1, databaseArray.length);
			return databaseArray;
		} else {
			return null;
		}
	}

	public final void showGui() {
		Component rootComponent = SQLRunnerStaticParameters.getConnectionSqlRunnerParameters().getSqlRunnerJFrame();
		connectGuiGeneratorParamsFileProcessor = GuiGeneratorMain.showGui(connectGuiGeneratorParamsFileProcessor,
				SQLRunnerStaticParameters.getConnectGuiXmlFile(), this, this, rootComponent);
	}

	public final void update(final Observable observable, final Object parametersHashTableObj) {
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		Hashtable<String, String> connectParametersHashTable = (Hashtable<String, String>) parametersHashTableObj;
		userName = connectParametersHashTable.get(USERNAME);
		password = connectParametersHashTable.get(PASSWORD);
		savePassword = connectParametersHashTable.get(SAVEPASSWORD).equalsIgnoreCase("yes");
		database = connectParametersHashTable.get(DATABASE);
		logger.finest("userName: " + userName + "   password: " + password);
		sqlRunnerParameters.setDataBaseDisplayName(database);
		sqlRunnerParameters.setDataBaseProductProperties(database);
		sqlRunnerParameters.setUserName(userName);
		if (savePassword) {
			sqlRunnerParameters.setPassword(password);
		} else {
			sqlRunnerParameters.setPassword(null);
		}
		logger.finest("username:" + userName + " password: " + password + " database: " + database);
		connect();
		if (sqlRunnerParameters.getConnection() != null) {
			ObjectExplorerHandler.showObjectExplorer();
		}
	}

}
