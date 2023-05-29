package oz.sqlrunner.handlers;

import java.util.Observable;
import java.util.logging.Logger;

import oz.infra.db.outcome.SqlExecutionOutcome;
import oz.infra.logging.jul.JulUtils;
import oz.infra.number.NumberUtils;
import oz.infra.system.SystemUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;

public class RowCountHandler extends Observable {
	private static Logger logger = JulUtils.getLogger();
	private static final RowCountHandler sqlRunneRowCountHandler = new RowCountHandler();

	public final void countRows() {
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		String tableName = sqlRunnerParameters.getTableName();
		String tableCreator = sqlRunnerParameters.getTableCreator();
		logger.info(SystemUtils.getCurrentMethodName() + ": name=" + tableName + " creator=" + tableCreator);
		String sqlStatementString = "SELECT COUNT(*) FROM " + tableCreator + "." + tableName;
		SqlExecutionOutcome sqlExecutionOutcome = SQLStatementHandler.executeSqlStatement(sqlStatementString);
		String[][] resultSetArray = sqlExecutionOutcome.getResultSetArray();
		if (resultSetArray != null && resultSetArray.length == 1 && resultSetArray[0].length == 1) {
			String rowsCount = NumberUtils.formatNumberString(resultSetArray[0][0]);
			String rowsCountString = rowsCount + " rows in " + tableCreator + "." + tableName;
			logger.info("rowsCountString: " + rowsCountString);
			setChanged();
			notifyObservers(rowsCountString);
		} else {
			logger.info("Unexpcted result set size. rows: " + String.valueOf(resultSetArray.length) + " Columns: "
					+ String.valueOf(resultSetArray[0].length));
		}
	}

	public static RowCountHandler getSqlRunneRowCountHandler() {
		return sqlRunneRowCountHandler;
	}
}
