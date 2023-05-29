package oz.sqlrunner.gui;

import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.jtable.JTableUtils;

public class SQLRunnerJSplitPane extends JSplitPane {
	private Logger logger = JulUtils.getLogger();
	private JScrollPane resultSetJScrollPane = null;
	private JScrollPane sqlStatementJScrollPane = null;

	public SQLRunnerJSplitPane(final JTable resultSetJTable, final String sqlStatementString,
			final String name) {
		if (resultSetJTable != null) {
			String columnNamesString = JTableUtils.getColumnNamesString(resultSetJTable);
			String detailedSQLStatementString = sqlStatementString.replaceFirst("\\*",
					columnNamesString);
			sqlStatementJScrollPane = new SQLStatementJScrollPane(detailedSQLStatementString);
			resultSetJScrollPane = getResultSetJScrollPane(resultSetJTable);
			setTopComponent(sqlStatementJScrollPane);
			setBottomComponent(resultSetJScrollPane);
		} else {
			setTopComponent(new SQLStatementJScrollPane("Select ..."));
			setBottomComponent(new SQLStatementJScrollPane("Result set ..."));

		}
		setOrientation(JSplitPane.VERTICAL_SPLIT);
		setName(name);
	}

	public static JScrollPane getResultSetJScrollPane(final JTable resultSetJTable) {
		JScrollPane resultSetJScrollPane = new JScrollPane(resultSetJTable);
		resultSetJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		resultSetJScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		return resultSetJScrollPane;
	}
}
