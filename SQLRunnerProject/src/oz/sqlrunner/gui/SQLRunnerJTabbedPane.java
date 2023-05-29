package oz.sqlrunner.gui;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.JTabbedPane;

import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.container.ContainerUtils;
import oz.infra.swing.jtabbedpane.ButtonTabComponent;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.gui.listeners.JTabbedPaneChangeListener;

public class SQLRunnerJTabbedPane extends JTabbedPane implements Observer {
	private static Logger logger = JulUtils.getLogger();

	public SQLRunnerJTabbedPane() {
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		sqlRunnerParameters.setSqlRunnerJTabbedPane(this);
		sqlRunnerParameters.addObserver(this);
		addChangeListener(new JTabbedPaneChangeListener());
	}

	public void update(final Observable observable, final Object object) {
		logger.fine("Staring update ...");
		int tabcount = getTabCount();
		if (object.toString().equals(SQLRunnerConnectionParameters.NOT_CONNECTED) && tabcount > 0) {
			removeAll();
		}
	}

	public void addSplitPane(final String sqlStatementString, final String name,
			final ResultSetJTable sqlRunnerResultSetJTable) {
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		SQLRunnerJSplitPane sqlRunnerJSplitPane = new SQLRunnerJSplitPane(sqlRunnerResultSetJTable,
				sqlStatementString, name);

		this.add(sqlRunnerJSplitPane);
		this.setSelectedComponent(sqlRunnerJSplitPane);
		ButtonTabComponent buttonTabComponent = new ButtonTabComponent(this);
		this.setTabComponentAt(this.getTabCount() - 1, buttonTabComponent);
		ContainerUtils.refreshDisplay(sqlRunnerParameters.getSqlRunnerJFrame());
	}

}
