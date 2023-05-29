package oz.sqlrunner.handlers;

import java.awt.Component;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import oz.infra.db.DBUtils;
import oz.infra.db.outcome.SqlExecutionOutcome;
import oz.infra.font.FontUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.gui.ResultSetJTable;

public final class RunSQLStatementHandler {
	private static Logger logger = JulUtils.getLogger();

	private static JSplitPane getSelectedJSplitPane() {
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		JTabbedPane jTabbedPane = sqlRunnerParameters.getSqlRunnerJTabbedPane();
		Component selectedComponent = jTabbedPane.getSelectedComponent();
		logger.finest(selectedComponent.toString());
		JSplitPane selectedJSplitPane = null;
		if (selectedComponent instanceof JSplitPane) {
			selectedJSplitPane = (JSplitPane) selectedComponent;
		}
		return selectedJSplitPane;
	}

	private static String getSqlStatement(final JSplitPane selectedJSplitPane) {
		String sqlStatementString = null;
		Component topComponent = selectedJSplitPane.getTopComponent();
		logger.finest(topComponent.toString());
		if (topComponent instanceof JScrollPane) {
			JScrollPane topJScrollPane = (JScrollPane) topComponent;
			logger.finest(String.valueOf(topJScrollPane.getViewport().getComponentCount()));
			Component textComponent = topJScrollPane.getViewport().getComponent(0);
			logger.finest(textComponent.toString());
			if (textComponent instanceof JTextPane) {
				sqlStatementString = ((JTextPane) textComponent).getText();
			}
		}
		logger.info("SQL statement: " + sqlStatementString);
		return sqlStatementString;
	}

	public static void runSQLStatement() {
		logger.finest("Starting " + SystemUtils.getCurrentMethodName());
		JSplitPane selectedJSplitPane = getSelectedJSplitPane();
		String sqlStatementString = getSqlStatement(selectedJSplitPane);
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		Connection connection = sqlRunnerParameters.getConnection();
		List<SqlExecutionOutcome> sqlExecutionOutcomeList = DBUtils.runSqlStatements(connection,
				sqlStatementString);
		JTabbedPane tabbedPane = new JTabbedPane();
		for (SqlExecutionOutcome sqlExecutionOutcome : sqlExecutionOutcomeList) {
			if (!sqlExecutionOutcome.isExecutionSuccessful()) {
				UIManager.put("OptionPane.font", FontUtils.ARIAL_PLAIN_14);
				JOptionPane.showMessageDialog(sqlRunnerParameters.getSqlRunnerJFrame(),
						sqlExecutionOutcome.getErrorMessage(), StringUtils
								.removeCharacterFromString(sqlExecutionOutcome
										.getSqlStatementString(), "\n\r"),
						JOptionPane.DEFAULT_OPTION);
			} else {
				ResultSetJTable resultSetJTable = new ResultSetJTable();
				resultSetJTable.configResultSetJTable(sqlExecutionOutcome.getColumnNames(),
						sqlExecutionOutcome.getResultSetArray(), null);
				JScrollPane jScrollPane = new JScrollPane(resultSetJTable);
				if (jScrollPane != null) {
					tabbedPane.addTab(sqlExecutionOutcome.getSqlStatementString(), jScrollPane);
				}
			}
		}
		selectedJSplitPane.setBottomComponent(tabbedPane);
		selectedJSplitPane.revalidate();
		selectedJSplitPane.repaint();
	}

	private RunSQLStatementHandler() {
	}

	// SELECT OP_SEX,OP_SEX_DESC FROM MATAF.GLST_SEX ; SELECT OP_SEX_DESC FROM
	// MATAF.GLST_SEX;SELECT * FROM MATAF.PERSONAL_MENU

	// SELECT DEPTNO,DEPTNAME,MGRNO,ADMRDEPT,LOCATION FROM DB2ADMIN.DEPT; SELECT
	// DEPTNO,DEPTNAME FROM DB2ADMIN.DEPT; SELECT COUNT(*) FROM DB2ADMIN.DEPT

}
