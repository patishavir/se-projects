package oz.sqlrunner.handlers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import oz.infra.db.DBUtils;
import oz.infra.db.outcome.SqlExecutionOutcome;
import oz.infra.logging.jul.JulUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;

public final class SQLStatementHandler {
	private static Logger logger = JulUtils.getLogger();

	private SQLStatementHandler() {
	}

	public static SqlExecutionOutcome executeSqlStatement(final String sqlStatementString) {
		logger.info("sqlStatementString: " + sqlStatementString);
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		Connection connection = sqlRunnerParameters.getConnection();
		SqlExecutionOutcome sqlExecutionOutcome = null;
		sqlExecutionOutcome = DBUtils.executeStatement(sqlStatementString, connection);
		if (sqlExecutionOutcome == null) {
			String errorMessage = "Result set is empty ! !";
			JOptionPane.showMessageDialog(null, errorMessage, "Empty result set ...",
					JOptionPane.ERROR_MESSAGE);
		}
		if (sqlExecutionOutcome != null && sqlExecutionOutcome.getErrorMessage() != null) {
			JOptionPane.showMessageDialog(sqlRunnerParameters.getSqlRunnerJTabbedPane(),
					sqlExecutionOutcome.getErrorMessage(), "Sql error", JOptionPane.ERROR_MESSAGE);
		}
		return sqlExecutionOutcome;
	}

	public static ResultSet getResultSet(final String sqlStatementString) {
		logger.info(sqlStatementString);
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		Connection connection = sqlRunnerParameters.getConnection();
		ResultSet resultSet = DBUtils.getResultSet(connection, sqlStatementString);
		return resultSet;
	}
}
