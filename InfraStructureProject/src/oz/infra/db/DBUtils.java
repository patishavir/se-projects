package oz.infra.db;

import static oz.infra.constants.OzConstants.COMMA;
import static oz.infra.constants.OzConstants.QUOTE;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.datetime.StopWatch;
import oz.infra.db.db2.DB2Utils;
import oz.infra.db.metadata.DBMetaDataUtils;
import oz.infra.db.outcome.SqlExecutionOutcome;
import oz.infra.db.resultset.ResultSetUtils;
import oz.infra.db.sql.comment.SqlCommentsUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.FolderUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public final class DBUtils {

	public static final String LINE_SEPARATOR = SystemUtils.LINE_SEPARATOR;
	private static final String SQL_STATEMENT_DELIMITER = ";";

	private static final Logger logger = JulUtils.getLogger();

	public static void closeConnection(final Connection connection) {
		try {
			if (connection != null) {
				connection.close();
				logger.info("connection has been closed.");
			}
		} catch (SQLException sqlEx) {
			ExceptionUtils.printMessageAndStackTrace(sqlEx);
		}
	}

	public static void closeStatement(final ResultSet resultSet) {
		try {
			if (resultSet != null) {
				Statement statement = resultSet.getStatement();
				if (statement != null) {
					statement.close();
				}
			}
		} catch (SQLException sqlEx) {
			ExceptionUtils.printMessageAndStackTrace(sqlEx);
		}
	}

	public static void closeStatement(final Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException sqlEx) {
			ExceptionUtils.printMessageAndStackTrace(sqlEx);
		}
	}

	public static int[] executeBatch(final Connection connection, final List<String> commands) {
		int[] resultArray = null;
		boolean autoCommit;
		try {
			autoCommit = connection.getAutoCommit();
			if (autoCommit) {
				connection.setAutoCommit(false);
			}
			Statement statement = connection.createStatement();
			for (String command : commands) {
				statement.addBatch(command);
				logger.info(command + " added to batch");
			}
			resultArray = statement.executeBatch();
			connection.commit();
			if (autoCommit) {
				connection.setAutoCommit(autoCommit);
			}
			statement.close();
		} catch (BatchUpdateException buex) {
			ExceptionUtils.printMessageAndStackTrace(buex);
			SQLException sqlex = buex.getNextException();
			ExceptionUtils.printMessageAndStackTrace(sqlex);
		} catch (SQLException sqlex) {
			ExceptionUtils.printMessageAndStackTrace(sqlex);
		}
		logger.info(SystemUtils.LINE_SEPARATOR + ArrayUtils.getAsDelimitedString(resultArray, SystemUtils.LINE_SEPARATOR));
		return resultArray;
	}

	public static SqlExecutionOutcome executeStatement(final String sqlStatementString, final Connection connection) {
		logger.finest(StringUtils.concat("sqlStatementString: ", sqlStatementString, "end of statement *****"));
		Statement sqlStatement = null;
		ResultSet resultSet = null;
		SqlExecutionOutcome sqlExecutionOutcome = new SqlExecutionOutcome(null, null, null);
		try {
			if (connection.isClosed()) {
				logger.warning("Connection is closed. Please reconnect");
			} else {
				StopWatch stopWatch = new StopWatch();
				sqlStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				boolean executeRc = sqlStatement.execute(sqlStatementString);
				// stopWatch.getElapsedTime("Query " + sqlStatementString
				// + " completed in");
				if (executeRc) {
					resultSet = sqlStatement.getResultSet();
					if (resultSet == null) {
						String errorMessage = "Result set is empty ! !";
						logger.warning(errorMessage);
					} else {
						logger.info("Result set has been successfully created !\n");
						String[] columnNames = DBMetaDataUtils.getResultSetColumnNames(resultSet);
						String[][] resultSetArray = ResultSetUtils.getResultSetAs2DimArray(resultSet);
						sqlExecutionOutcome = new SqlExecutionOutcome(sqlStatementString, columnNames, resultSetArray);
					}
				} else {
					int updateCount = sqlStatement.getUpdateCount();
					logger.finest("executeUpdate performed. " + sqlStatementString + "\n" + "Count=" + String.valueOf(updateCount));
					String[] columnNames = { " ", " " };
					String[][] outcomeArray = { { "Ran: ", sqlStatementString }, { "Count: ", String.valueOf(updateCount) } };
					sqlExecutionOutcome = new SqlExecutionOutcome(sqlStatementString, columnNames, outcomeArray);
				}
			}
		} catch (Exception ex) {
			logger.warning("sqlStatementString: " + sqlStatementString);
			ExceptionUtils.printMessageAndStackTrace(ex);
			sqlExecutionOutcome.setErrorInfo(sqlStatementString, ex.getMessage());
		}
		return sqlExecutionOutcome;
	}

	public static void exportSchema(final Properties exportSchemaProperties) {
		boolean metaDataOnly = false;
		String schemaName = exportSchemaProperties.getProperty("schemaName");
		String folderPath = exportSchemaProperties.getProperty("folderPath");
		String excludedColumns = exportSchemaProperties.getProperty("excludedColumns");
		String metaDataOnlyString = exportSchemaProperties.getProperty("metaDataOnly");
		String excludeTables = exportSchemaProperties.getProperty("excludeTables");
		String[] excludeTablesArray = null;
		if (excludeTables != null && excludeTables.length() > 0) {
			excludeTablesArray = excludeTables.split(OzConstants.COMMA);
		}
		if (metaDataOnlyString != null && metaDataOnlyString.equalsIgnoreCase(OzConstants.YES)) {
			metaDataOnly = true;
		}
		FolderUtils.createFolderIfNotExists(folderPath);
		List<String> tableNamesList = new ArrayList<String>();
		tableNamesList = DBMetaDataUtils.getSchemaTableNames(exportSchemaProperties, schemaName);
		ListUtils.getAsDelimitedString(tableNamesList, Level.FINEST);
		Connection connection = getConnection(exportSchemaProperties);
		long totalLength = 0;
		for (String tableName1 : tableNamesList) {
			if (!ArrayUtils.isObjectInArray(excludeTablesArray, tableName1)) {
				logger.info(StringUtils.concat("processing ", tableName1, " ..."));
				try {
					if (metaDataOnly) {
						totalLength += DBMetaDataUtils.tableMetaData2Csv(connection, folderPath, tableName1, schemaName);
					} else {
						String csvString = ResultSetUtils.table2CsvString(connection, tableName1, schemaName, excludedColumns);

						totalLength += csvString.length();
						ResultSetUtils.writeCsvFile(csvString, folderPath, OzConstants.EMPTY_STRING, schemaName, tableName1, Boolean.FALSE);
					}
				} catch (Exception ex) {
					ExceptionUtils.printMessageAndStackTrace(ex);
				}
			}
		}
		String message = StringUtils.concat("Number of tables processed: ", String.valueOf(tableNamesList.size()), " total length: ",
				String.valueOf(totalLength), " schema:", schemaName);
		logger.info(message);
	}

	public static Connection getConnection(final Properties connetionProperties) {
		String url = connetionProperties.getProperty(DBUtilsParameters.URL);
		String userName = connetionProperties.getProperty(DBUtilsParameters.USER_NAME);
		String password = connetionProperties.getProperty(DBUtilsParameters.PASSWORD);
		logger.finest("password:" + password);
		String encryptionMethodString = connetionProperties.getProperty(DBUtilsParameters.ENCRYPTION_METHOD);
		EncryptionMethodEnum encryptionMethod = null;
		if (encryptionMethodString != null) {
			try {
				encryptionMethod = EncryptionMethodEnum.valueOf(encryptionMethodString);
			} catch (Exception ex) {
				SystemUtils.printMessageAndExit(
						StringUtils.concat(encryptionMethodString, " is an invalid encryption method!\n Operation has been aborted."),
						OzConstants.EXIT_STATUS_ABNORMAL, false);
			}
		}
		if (encryptionMethod == null) {
			encryptionMethod = EncryptionMethodEnum.WITH_HOSTNAME_AND_USERNAME;
		}
		String delimiter = connetionProperties.getProperty(DBUtilsParameters.PASSWORD_DELIMITER);
		if (delimiter == null) {
			password = CryptographyUtils.decryptString(password, encryptionMethod);
		} else {
			password = CryptographyUtils.decryptString(password, encryptionMethod, delimiter);
		}
		logger.finest("password:" + password);
		String driverClassName = connetionProperties.getProperty(DBUtilsParameters.DRIVER_CLASS_NAME);
		return getConnection(url, userName, password, driverClassName, false);
	}

	public static Connection getConnection(final String connetionPropertiesFilePath) {
		Properties connectionProperties = PropertiesUtils.loadPropertiesFile(connetionPropertiesFilePath);
		PropertiesUtils.printProperties(connectionProperties, Level.FINEST);
		return getConnection(connectionProperties);
	}

	public static Connection getConnection(final String url, final String userName, final String password, final String driverClassName,
			final boolean verbose) {
		Connection connection = null;
		try {
			Class.forName(driverClassName);
			StopWatch connectionStopWatch = new StopWatch();
			connection = java.sql.DriverManager.getConnection(url, userName, password);
			connectionStopWatch.logElapsedTimeMessage("connection to " + url + " user: " + userName + " completed in");
			printConnectionWarnings(connection);
			if (verbose && connection != null) {
				logger.info("Connection to " + url + " has been successfully established!");
				logger.info(DBMetaDataUtils.getDataBaseMetaDataDetails(connection));
			}
		} catch (Exception exception) {
			String exceptionMessage = "connect attempt has failed. hostName=" + SystemUtils.getHostname() + " url=" + url + " userName=" + userName
					+ " driverClassName=" + driverClassName;
			logger.warning(exceptionMessage);
			ExceptionUtils.printMessageAndStackTrace(exception);
			logger.warning(exception.getMessage());
		}
		return connection;
	}

	public static Connection getConnection(final String driverClassName, final String url, final String serverName, final String portNumber,
			final String databaseName, final String selectMethod, final String userName, final String password) {
		return getConnection(getConnectionUrl(url, serverName, portNumber, databaseName, selectMethod), userName, password, driverClassName, true);

	}

	public static Connection getConnectionThrowsException(final String url, final String userName, final String password,
			final String driverClassName, final boolean verbose) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		Class.forName(driverClassName);
		StopWatch connectionStopWatch = new StopWatch();
		connection = java.sql.DriverManager.getConnection(url, userName, password);
		connectionStopWatch.logElapsedTimeMessage("connection to " + url + " user: " + userName + " completed in");
		printConnectionWarnings(connection);
		if (verbose && connection != null) {
			logger.info("Connection to " + url + " has been successfully established!");
			logger.info(DBMetaDataUtils.getDataBaseMetaDataDetails(connection));
		}
		return connection;
	}

	private static String getConnectionUrl(final String url, final String serverName, final String portNumber, final String databaseName,
			final String selectMethod) {
		if (serverName != null) {
			return url + serverName + ":" + portNumber + ";databaseName=" + databaseName + ";selectMethod=" + selectMethod + ";";
		} else {
			return url;
		}
	}

	public static int getLoginTimeout() {
		int loginTimeOut = DriverManager.getLoginTimeout();
		logger.info("Login timeout: " + String.valueOf(loginTimeOut));
		return loginTimeOut;
	}

	/*
	 *
	 */
	public static ResultSet getResultSet(final Connection connection, final Statement sqlStatement, final String sqlStatementString) {
		logger.fine(sqlStatementString);
		StopWatch stopWatch = new StopWatch();
		ResultSet resultSet = null;
		try {
			resultSet = sqlStatement.executeQuery(sqlStatementString);
		} catch (SQLException exception) {
			ExceptionUtils.printMessageAndStackTrace(exception);
		}
		logger.info(stopWatch.appendElapsedTimeToMessage("Resultset build completed successully in"));
		return resultSet;
	}

	/*
	 * getResultSet
	 */
	public static ResultSet getResultSet(final Connection connection, final String sqlStatementString) {
		logger.fine(sqlStatementString);
		try {
			Statement sqlStatement = connection.createStatement();
			return getResultSet(connection, sqlStatement, sqlStatementString);
		} catch (SQLException exception) {
			ExceptionUtils.printMessageAndStackTrace(exception);
			return null;
		}
	}

	public static final JTable getResultSetAsJTable(final Connection connection, final Statement statement, final String sqlStatementString) {
		ResultSet resultSet = getResultSet(connection, statement, sqlStatementString);
		String[][] resultSetArray = ResultSetUtils.getResultSetAs2DimArray(resultSet);
		String[] columnNames = DBMetaDataUtils.getResultSetColumnNames(resultSet);
		return new JTable(resultSetArray, columnNames);
	}

	/*
	 * getResultSetColumnClassNames
	 */
	public static String[] getResultSetColumnClassNames(final ResultSetMetaData rsMetaData) {
		String[] columnClassNames = null;
		if (rsMetaData != null) {
			logger.finest("Entering getResultSetColumnNames: " + rsMetaData.toString());
			try {
				int columnCount = rsMetaData.getColumnCount();
				columnClassNames = new String[columnCount];
				for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
					columnClassNames[columnIndex - 1] = rsMetaData.getColumnClassName(columnIndex);
					logger.finest("Column: " + String.valueOf(columnIndex - 1) + " column name:" + columnClassNames[columnIndex - 1]);
				}
				// }
			} catch (Exception exception) {
				exception.printStackTrace();
				String errorMessage = exception.getMessage();
				logger.severe(errorMessage);
				JOptionPane.showMessageDialog(null, errorMessage, errorMessage, JOptionPane.ERROR_MESSAGE);
			}
		}
		return columnClassNames;
	}

	/*
	 * getResultSet
	 */
	public static String[] getResultSetColumnNames(final ResultSetMetaData rsMetaData) {
		String[] columnNames = null;
		if (rsMetaData != null) {
			logger.finest("Entering getResultSetColumnNames: " + rsMetaData.toString());
			try {
				int columnCount = rsMetaData.getColumnCount();
				columnNames = new String[columnCount];
				for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
					columnNames[columnIndex - 1] = rsMetaData.getColumnName(columnIndex);
					logger.finest("Column: " + String.valueOf(columnIndex - 1) + " column name:" + columnNames[columnIndex - 1]);
				}
				// }
			} catch (Exception exception) {
				exception.printStackTrace();
				String errorMessage = exception.getMessage();
				logger.severe(errorMessage);
				JOptionPane.showMessageDialog(null, errorMessage, errorMessage, JOptionPane.ERROR_MESSAGE);
			}
		}
		return columnNames;
	}

	private static List<String> getSqlStatementList(final String sqlStatementString, final String sqlStatementDelimiter) {
		logger.finest("Starting " + SystemUtils.getCurrentMethodName());
		List<String> sqlStatementList = new ArrayList<String>();
		String netSqlStatementString = SqlCommentsUtils.removeComments(sqlStatementString);
		netSqlStatementString = SqlCommentsUtils.removeBlockComments(netSqlStatementString);
		String[] sqlStatementArray = netSqlStatementString.trim().split(sqlStatementDelimiter);
		for (String sqlStatement : sqlStatementArray) {
			logger.finest(StringUtils.concat("About to process: ", sqlStatement));
			sqlStatementList.add(DB2Utils.adjustReorgStatement(sqlStatement));
		}
		return sqlStatementList;
	}

	public static String[] getStringColumnFromResultSet(final ResultSet resultSet, final int columnIndex) {
		ArrayList<String> columnArrayList = new ArrayList<String>();
		try {
			while (resultSet.next()) {
				columnArrayList.add(resultSet.getString(columnIndex));
			}
		} catch (SQLException sqlException) {
			ExceptionUtils.printMessageAndStackTrace(sqlException);
		}
		return columnArrayList.toArray(new String[columnArrayList.size()]);
	}

	public static String[] getStringColumnFromResultSet(final ResultSet resultSet, final String columnName) {
		ArrayList<String> columnArrayList = new ArrayList<String>();
		try {
			while (resultSet.next()) {
				columnArrayList.add(resultSet.getString(columnName));
			}
		} catch (SQLException sqlException) {
			ExceptionUtils.printMessageAndStackTrace(sqlException);
		}
		return columnArrayList.toArray(new String[columnArrayList.size()]);
	}

	public static int[] importIntoTable(final Connection connection, final String columnNames, final String[] columnTypes, final String[] dataArray,
			final String tableName, final String schemaName) throws SQLException {
		boolean autoCommit = connection.getAutoCommit();
		connection.setAutoCommit(false);
		int[] updateCounts = { -1 };
		String myColumnNames = StringUtils.removeTrailingSubString(columnNames.trim(), OzConstants.COMMA);
		if (myColumnNames.length() > 0) {
			String insertStatementPrefix = StringUtils.concat("INSERT INTO ", schemaName, OzConstants.DOT, tableName, OzConstants.BLANK, "(",
					myColumnNames, ") VALUES ( ");
			logger.info("insertStatement: " + insertStatementPrefix);
			Statement statement = connection.createStatement();
			for (int i = 0; i < dataArray.length; i++) {
				StringBuilder dataString = new StringBuilder();
				if (dataArray[i].trim().length() > 0) {
					String[] columnsValues = dataArray[i].split(COMMA);
					for (int j = 0; j < columnsValues.length; j++) {
						if (j > 0) {
							dataString.append(COMMA);
						}
						if (columnTypes[j].endsWith("String")) {
							dataString.append(QUOTE);
							dataString.append(columnsValues[j]);
							dataString.append(OzConstants.QUOTE);
						} else {
							dataString.append(columnsValues[j]);
						}
					}
					String insertStatement = StringUtils.concat(insertStatementPrefix, dataString.toString(), " )");
					logger.info(insertStatement);
					try {
						statement.addBatch(insertStatement);
					} catch (Exception ex) {
						ExceptionUtils.printMessageAndStackTrace(ex);
					}
				}
			}
			updateCounts = statement.executeBatch();
			connection.commit();
			connection.setAutoCommit(autoCommit);
		}
		return updateCounts;
	}

	public static String printColumnNames(final ResultSetMetaData resultSetMetaData) {
		StringBuilder stringBuilder = new StringBuilder(LINE_SEPARATOR);
		try {
			int columnCount = resultSetMetaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				stringBuilder.append(LINE_SEPARATOR + "Column " + String.valueOf(i) + OzConstants.TAB);
				stringBuilder.append(resultSetMetaData.getColumnName(i));
			}
		} catch (SQLException sqlException) {
			logger.warning(sqlException.getMessage());
			sqlException.printStackTrace();
		}
		String columnNamesString = stringBuilder.toString();
		logger.info(columnNamesString);
		return columnNamesString;
	}

	public static void printConnectionWarnings(final Connection connection) throws SQLException {
		// Print all warnings
		StringBuilder sb = new StringBuilder();
		for (SQLWarning warn = connection.getWarnings(); warn != null; warn = warn.getNextWarning()) {
			sb.append("\nSQL Warning:");
			sb.append("\nState  : " + warn.getSQLState());
			sb.append("\nMessage: " + warn.getMessage());
			sb.append("\nError  : " + warn.getErrorCode());
		}
		if (sb.length() > 0) {
			logger.warning(sb.toString());
		}
	}

	public static List<SqlExecutionOutcome> runSqlStatements(final Connection connection, final String sqlStatementString,
			final String... sqlStatementDelimiterParameter) {
		List<SqlExecutionOutcome> sqlExecutionOutcomeList = null;
		int successCount = 0;
		int failureCount = 0;
		if (connection != null) {
			boolean continueOnFailure = true;
			String sqlStatementDelimiter = SQL_STATEMENT_DELIMITER;
			if (sqlStatementDelimiterParameter.length == 1) {
				sqlStatementDelimiter = sqlStatementDelimiterParameter[0];
			}
			List<String> sqlStatementList = getSqlStatementList(sqlStatementString, sqlStatementDelimiter);
			sqlExecutionOutcomeList = new ArrayList<SqlExecutionOutcome>();

			for (String sqlStatement1 : sqlStatementList) {
				if (sqlStatement1.trim().length() > 0) {
					SqlExecutionOutcome sqlExecutionOutcome = executeStatement(sqlStatement1, connection);
					sqlExecutionOutcomeList.add(sqlExecutionOutcome);
					if ((!sqlExecutionOutcome.isExecutionSuccessful()) && (!continueOnFailure)) {
						break;
					}
					if (sqlExecutionOutcome.isExecutionSuccessful()) {
						successCount++;
					} else {
						failureCount++;
					}
				}
			}
		}
		logger.info(String.valueOf(successCount) + " commands have run successfully. " + SystemUtils.LINE_SEPARATOR + String.valueOf(failureCount)
				+ " commands have failed. ");
		return sqlExecutionOutcomeList;
	}

	public static void setLoginTimeout(final int loginTimeOut) {
		DriverManager.setLoginTimeout(loginTimeOut);
		logger.finest("Login timeout has beem set to: " + String.valueOf(loginTimeOut));
	}

	private DBUtils() {
	}
}
