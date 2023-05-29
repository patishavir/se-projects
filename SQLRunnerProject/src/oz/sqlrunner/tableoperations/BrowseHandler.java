package oz.sqlrunner.tableoperations;

import java.util.logging.Logger;

import oz.infra.db.outcome.SqlExecutionOutcome;
import oz.infra.logging.jul.JulUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.enums.DatabaseProductEnum;
import oz.sqlrunner.gui.ResultSetJTable;
import oz.sqlrunner.gui.SQLRunnerJTabbedPane;
import oz.sqlrunner.handlers.SQLStatementHandler;
import oz.sqlrunner.handlers.enums.LimitNumberOfRowsClauseTypeEnum;

public class BrowseHandler {
	private static Logger logger = JulUtils.getLogger();

	public final void browseData(final String numberOfRecords) {
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		String tableName = sqlRunnerParameters.getTableName();
		String tableCreator = sqlRunnerParameters.getTableCreator();
		logger.info("browseData: name=" + tableName + " creator="
				+ tableCreator + " number of records=" + numberOfRecords);
		DatabaseProductEnum dataBaseProduct = sqlRunnerParameters
				.getDataBaseProductEnum();
		String limitNumberOfRowsClause = dataBaseProduct
				.getLimitNumberOfRowsClause().replaceFirst(
						"MAX_NUMBER_OF_ROWS", String.valueOf(numberOfRecords));
		String sqlStatementString = "SELECT * FROM " + tableCreator + "."
				+ tableName;
		LimitNumberOfRowsClauseTypeEnum limitNumberOfRowsClauseType = dataBaseProduct
				.getLimitNumberOfRowsClauseType();
		switch (limitNumberOfRowsClauseType) {
		case APPEND:
			sqlStatementString = sqlStatementString + " "
					+ limitNumberOfRowsClause;
			break;
		case WHERECLAUSE:
			if (sqlStatementString.toLowerCase().indexOf("where") < 0) {
				sqlStatementString = sqlStatementString + " "
						+ limitNumberOfRowsClause;
			} else {
				sqlStatementString = sqlStatementString
						+ limitNumberOfRowsClause.substring("WHERE".length());
			}
			break;
		case SELECT:
			sqlStatementString = sqlStatementString.replaceFirst("SELECT",
					limitNumberOfRowsClause);
			break;
		default:
			logger.severe("Bad limitNumberOfRowsClause ");
		}
		logger.info("sqlStatementString: " + sqlStatementString);
		displayResultSet(sqlStatementString, tableCreator + "." + tableName);
	}

	public final void displayResultSet(final String sqlStatementString,
			final String name) {
		logger.finest("Browse");
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		SqlExecutionOutcome sqlExecutionOutcome = SQLStatementHandler
				.executeSqlStatement(sqlStatementString);
		ResultSetJTable sqlRunnerResultSetJTable = new ResultSetJTable();
		sqlRunnerResultSetJTable.configResultSetJTable(
				sqlExecutionOutcome.getColumnNames(),
				sqlExecutionOutcome.getResultSetArray(), null);
		SQLRunnerJTabbedPane sqlRunnerJTabbedPane = sqlRunnerParameters
				.getSqlRunnerJTabbedPane();
		sqlRunnerJTabbedPane.addSplitPane(sqlStatementString, name,
				sqlRunnerResultSetJTable);

	}
}
