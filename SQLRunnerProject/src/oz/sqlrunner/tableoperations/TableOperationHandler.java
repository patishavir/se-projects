package oz.sqlrunner.tableoperations;

import java.util.Observable;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import oz.infra.constants.OzConstants;
import oz.infra.db.outcome.SqlExecutionOutcome;
import oz.infra.logging.jul.JulUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.gui.listeners.enums.ObjectExplorerOperationEnum;
import oz.sqlrunner.handlers.ConnectionHandler;
import oz.sqlrunner.handlers.SQLStatementHandler;

public class TableOperationHandler extends Observable {
	private final static TableOperationHandler tableOperationHandler = new TableOperationHandler();
	private static Logger logger = JulUtils.getLogger();

	public void handleTableOperation(final String tableOperation) {
		SQLRunnerConnectionParameters sqlRunnerConnectionParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		String tableName = sqlRunnerConnectionParameters.getTableName();
		String tableCreator = sqlRunnerConnectionParameters.getTableCreator();
		String fullTableName = tableCreator + OzConstants.DOT + tableName;
		ObjectExplorerOperationEnum objectExplorerOperationEnum = ObjectExplorerOperationEnum
				.valueOf(tableOperation);
		String operationMessage = tableOperation + " table " + fullTableName;
		String title = "Confirm table " + tableOperation;
		String message = "Are you sure you want to " + tableOperation + " " + fullTableName;
		String sqlStatementSuffix = " IMMEDIATE"; // suffix for truncate
		switch (objectExplorerOperationEnum) {
		case DROP:
			sqlStatementSuffix = ""; // no suffix for drop
		case TRUNCATE:
			int yesNoReturnCode = JOptionPane.showConfirmDialog(sqlRunnerConnectionParameters
					.getSqlRunnerJFrame(), message, title, JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			logger.finest("yesNoReturnCode: " + String.valueOf(yesNoReturnCode));
			if (yesNoReturnCode == 0) {
				ConnectionHandler connectionHandler = ConnectionHandler.getConnectionHandler();
				connectionHandler.close();
				connectionHandler.connect();
				String sqlStatementString = tableOperation + " TABLE " + fullTableName
						+ sqlStatementSuffix;
				SqlExecutionOutcome sqlExecutionOutcome = SQLStatementHandler
						.executeSqlStatement(sqlStatementString);
				if (sqlExecutionOutcome.isExecutionSuccessful()) {
					operationMessage = operationMessage + " has succeeded.";
				} else {
					operationMessage = operationMessage + " has failed. "
							+ sqlExecutionOutcome.getErrorMessage();
				}
			} else {
				operationMessage = operationMessage + " has been aborted";
			}
			setChanged();
			notifyObservers(operationMessage);
			break;
		}
	}

	public static TableOperationHandler getTableoperationhandler() {
		return tableOperationHandler;
	}
}
