package oz.sqlrunner.handlers;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import oz.infra.db.DBUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.container.ContainerUtils;
import oz.infra.system.SystemUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.gui.ResultSetJTable;
import oz.sqlrunner.gui.SQLRunnerJSplitPane;
import oz.sqlrunner.gui.listeners.ObjectExplorerMouseListener;

public class ObjectExplorerHandler extends Observable {
	private static ObjectExplorerHandler sqlRunnerObjectExplorerHandler = new ObjectExplorerHandler();
	private static ObjectExplorerMouseListener objectExplorerMouseListener = new ObjectExplorerMouseListener();
	private static SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
			.getConnectionSqlRunnerParameters();
	private static int[] selectedColumns = { 1, 2, 3 };
	private static final int OBJECT_EXPLORER_TAB_INDEX = 0;

	private static Logger logger = JulUtils.getLogger();

	private static ResultSet generateObjectExplorerResultSetRunningSqlStatement() {
		logger.info("Entering " + SystemUtils.getCurrentMethodName());
		String objectBrowsersqlStatementString = sqlRunnerParameters.getObjectBrowsersqlStatementString();
		String sqlStatementString = objectBrowsersqlStatementString.substring(0,
				objectBrowsersqlStatementString.indexOf("WHERE")) + generateWhereClause();
		ResultSet resultSet = SQLStatementHandler.getResultSet(sqlStatementString);
		try {
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			// DBUtils.printColumnNames(resultSetMetaData);
			sqlRunnerParameters.setCreatorColumName(resultSetMetaData.getColumnName(1));
			sqlRunnerParameters.setTableNameColumName(resultSetMetaData.getColumnName(2));
		} catch (SQLException sqlException) {
			ExceptionUtils.printMessageAndStackTrace(sqlException);
		}
		selectedColumns = null;
		return resultSet;
	}

	private static ResultSet generateObjectExplorerResultSetUsingMetaData() {
		logger.info("Entering " + SystemUtils.getCurrentMethodName());
		ResultSet resultSet = null;
		Connection connection = sqlRunnerParameters.getConnection();
		if (connection != null) {
			try {
				DatabaseMetaData databaseMetaData = connection.getMetaData();
				String creatorFilter = sqlRunnerParameters.getCreatorFilter();
				String nameFilter = sqlRunnerParameters.getNameFilter();
				logger.info("creatorFilter: " + creatorFilter + " nameFilter: " + nameFilter);
				if (creatorFilter != null && creatorFilter.length() == 0) {
					creatorFilter = null;
				}
				if (nameFilter != null && nameFilter.length() == 0) {
					nameFilter = null;
				}
				resultSet = databaseMetaData.getTables(null, creatorFilter, nameFilter, null);
				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
				// DBUtils.printColumnNames(resultSetMetaData);
				sqlRunnerParameters.setCreatorColumName(resultSetMetaData.getColumnName(2));
				sqlRunnerParameters.setTableNameColumName(resultSetMetaData.getColumnName(3));
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.warning(ex.getMessage());
			}
		}
		return resultSet;
	}

	private static String generateWhereClause() {
		String whereClause = "";
		Hashtable<String, String> filterParametersHashTable = new Hashtable<String, String>();
		if (sqlRunnerParameters.getNameFilter() != null) {
			filterParametersHashTable.put(FilterHandler.NAME, sqlRunnerParameters.getNameFilter());
		}
		if (sqlRunnerParameters.getCreatorFilter() != null) {
			filterParametersHashTable.put(FilterHandler.CREATOR, sqlRunnerParameters.getCreatorFilter());
		}
		Set<String> keySet = filterParametersHashTable.keySet();
		for (String parameterName : keySet) {
			String clause = processWhereClauseParameter(parameterName, filterParametersHashTable.get(parameterName));
			if (clause.length() > 0) {
				if (whereClause.length() == 0) {
					whereClause = "WHERE " + clause;
				} else {
					whereClause = whereClause + " AND " + clause;
				}
			}
		}
		logger.finest("Where clause: " + whereClause);
		return whereClause;
	}

	public static ObjectExplorerHandler getSqlRunnerObjectExplorerHandler() {
		return sqlRunnerObjectExplorerHandler;
	}

	private static String processWhereClauseParameter(final String parameterName, final String parameterValue) {
		String parameterClause = "";
		if (parameterValue != null && parameterValue.length() > 0) {
			if (parameterValue.indexOf('%') > -1) {
				parameterClause = parameterName + " LIKE '" + parameterValue + "'";
			} else if (parameterValue.indexOf(',') > 0) {
				parameterClause = parameterName + " IN ('" + parameterValue + "')";
			} else {
				parameterClause = parameterName + " = '" + parameterValue + "'";
			}
		}
		logger.info("parameterName: " + parameterName + " parameterValue: " + parameterValue + " parameterClause: "
				+ parameterClause);
		return parameterClause;
	}

	public static void showObjectExplorer() {
		logger.info("Starting " + SystemUtils.getCurrentMethodName());
		ResultSet objectExplorerResultSet = null;
		if (sqlRunnerParameters.isUseDatabaseMetaData()) {
			objectExplorerResultSet = generateObjectExplorerResultSetUsingMetaData();
		} else {
			objectExplorerResultSet = generateObjectExplorerResultSetRunningSqlStatement();
		}
		ResultSetJTable sqlRunnerResultSetJTable = new ResultSetJTable(objectExplorerResultSet, selectedColumns);
		sqlRunnerResultSetJTable.addMouseListener(objectExplorerMouseListener);
		DBUtils.closeStatement(objectExplorerResultSet);
		JTabbedPane jTabbedPane = sqlRunnerParameters.getSqlRunnerJTabbedPane();
		JScrollPane objectExplorerResultSetJScrollPane = SQLRunnerJSplitPane
				.getResultSetJScrollPane(sqlRunnerResultSetJTable);
		if (sqlRunnerParameters.getObjectExplorerResultSetJScrollPane() != null && jTabbedPane.getTabCount() > 0) {
			logger.finest(SystemUtils.getCurrentMethodName());
			sqlRunnerParameters.getObjectExplorerResultSetJScrollPane().setViewportView(sqlRunnerResultSetJTable);
		} else {
			objectExplorerResultSetJScrollPane.setName("Object explorer");
			jTabbedPane.add(objectExplorerResultSetJScrollPane, OBJECT_EXPLORER_TAB_INDEX);
			sqlRunnerParameters.setObjectExplorerResultSetJScrollPane(objectExplorerResultSetJScrollPane);
		}
		sqlRunnerObjectExplorerHandler.setChanged();
		String databaseObjectsMessage = String.valueOf(sqlRunnerResultSetJTable.getRowCount()) + " database objects";
		sqlRunnerObjectExplorerHandler.notifyObservers(databaseObjectsMessage);
		jTabbedPane.setSelectedIndex(OBJECT_EXPLORER_TAB_INDEX);
		ContainerUtils.refreshDisplay(sqlRunnerParameters.getSqlRunnerJFrame());
	}
}
