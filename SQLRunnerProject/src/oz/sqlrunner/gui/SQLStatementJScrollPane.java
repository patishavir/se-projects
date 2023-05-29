package oz.sqlrunner.gui;

import java.util.logging.Logger;

import javax.swing.JScrollPane;

public class SQLStatementJScrollPane extends JScrollPane {
	private Logger logger = Logger.getLogger(this.getClass().toString());

	SQLStatementJScrollPane(final String sqlStatementString) {
		SQLStatementTextPane sqlStatement = new SQLStatementTextPane(sqlStatementString);
		setViewportView(sqlStatement);
	}
}
