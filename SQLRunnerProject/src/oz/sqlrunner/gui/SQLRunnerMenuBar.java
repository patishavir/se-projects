package oz.sqlrunner.gui;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.gui.enums.SQLRunnerGuiComponentsEnum;
import oz.sqlrunner.gui.listeners.SQLRunnerActionListener;

public class SQLRunnerMenuBar extends JMenuBar implements Observer {
	private SQLRunnerActionListener sqlRunnerActionListener = new SQLRunnerActionListener();
	private JMenuItem connectJMenuItem = null;
	private JMenuItem disConnectJMenuItem = null;
	private JMenuItem filterJMenuItem = null;
	private JMenuItem newSqlStatementJMenuItem = null;
	private JMenuItem runSqlStatementJMenuItem = null;
	private JMenuItem refreshJMenuItem = null;
	private Logger logger = Logger.getLogger(this.getClass().toString());

	SQLRunnerMenuBar() {
		JMenu jmFile = new JMenu("File");
		JMenu jmView = new JMenu("View");
		JMenu jmHelp = new JMenu("Help");

		connectJMenuItem = addMenuItem(SQLRunnerGuiComponentsEnum.Connect.getDisplayName(),
				SQLRunnerGuiComponentsEnum.Connect.toString(), jmFile, false);
		disConnectJMenuItem = addMenuItem(SQLRunnerGuiComponentsEnum.DisConnect.getDisplayName(),
				SQLRunnerGuiComponentsEnum.DisConnect.toString(), jmFile, false);
		disConnectJMenuItem.setEnabled(false);
		filterJMenuItem = addMenuItem(SQLRunnerGuiComponentsEnum.Filter.getDisplayName(),
				SQLRunnerGuiComponentsEnum.Filter.toString(), jmFile, false);
		filterJMenuItem.setEnabled(false);
		newSqlStatementJMenuItem = addMenuItem(
				SQLRunnerGuiComponentsEnum.NewSqlStatement.getDisplayName(),
				SQLRunnerGuiComponentsEnum.NewSqlStatement.toString(), jmFile, true);
		newSqlStatementJMenuItem.setEnabled(false);
		runSqlStatementJMenuItem = addMenuItem(SQLRunnerGuiComponentsEnum.Run.getDisplayName(),
				SQLRunnerGuiComponentsEnum.Run.toString(), jmFile, false);
		runSqlStatementJMenuItem.setEnabled(false);
		addMenuItem(SQLRunnerGuiComponentsEnum.Exit.getDisplayName(),
				SQLRunnerGuiComponentsEnum.Exit.toString(), jmFile, true);

		refreshJMenuItem = addMenuItem(SQLRunnerGuiComponentsEnum.Refresh.getDisplayName(),
				SQLRunnerGuiComponentsEnum.Refresh.toString(), jmView, false);
		refreshJMenuItem.setEnabled(false);
		addMenuItem(SQLRunnerGuiComponentsEnum.Usage.getDisplayName(),
				SQLRunnerGuiComponentsEnum.Usage.toString(), jmHelp, false);
		addMenuItem(SQLRunnerGuiComponentsEnum.About.getDisplayName(),
				SQLRunnerGuiComponentsEnum.About.toString(), jmHelp, false);

		this.add(jmFile);
		this.add(jmView);
		this.add(jmHelp);
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		sqlRunnerParameters.addObserver(this);
	}

	private JMenuItem addMenuItem(final String displayName, final String actionCommand,
			final JMenu jMenu, final boolean addSeparatorBeforeItem) {
		JMenuItem menuItem = new JMenuItem(displayName);
		menuItem.setActionCommand(actionCommand);
		menuItem.addActionListener(sqlRunnerActionListener);
		if (addSeparatorBeforeItem) {
			jMenu.addSeparator();
		}
		jMenu.add(menuItem);
		return menuItem;
	}

	public void update(final Observable observable, final Object object) {
		logger.fine("Update processing has started ... ");
		if (observable instanceof SQLRunnerConnectionParameters && object instanceof String) {
			if (object.toString().equals(SQLRunnerConnectionParameters.NOT_CONNECTED)) {
				connectJMenuItem.setEnabled(true);
				disConnectJMenuItem.setEnabled(false);
				filterJMenuItem.setEnabled(false);
				newSqlStatementJMenuItem.setEnabled(false);
				runSqlStatementJMenuItem.setEnabled(false);
				refreshJMenuItem.setEnabled(false);
			} else {
				connectJMenuItem.setEnabled(false);
				disConnectJMenuItem.setEnabled(true);
				filterJMenuItem.setEnabled(true);
				newSqlStatementJMenuItem.setEnabled(true);
				runSqlStatementJMenuItem.setEnabled(true);
				refreshJMenuItem.setEnabled(true);
			}
		}
	}
}