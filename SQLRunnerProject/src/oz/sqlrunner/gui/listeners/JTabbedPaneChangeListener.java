package oz.sqlrunner.gui.listeners;

import java.util.Observable;
import java.util.logging.Logger;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import oz.sqlrunner.SQLRunnerStaticParameters;

public class JTabbedPaneChangeListener extends Observable implements ChangeListener {
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public void stateChanged(ChangeEvent evt) {
		JTabbedPane jTabbedPane = (JTabbedPane) evt.getSource();
		// Get current tab
		int selectedIndex = jTabbedPane.getSelectedIndex();
		logger.info("State Changed. Tab count: " + String.valueOf(jTabbedPane.getTabCount())
				+ " Selected index: " + String.valueOf(selectedIndex));
		setChanged();
		addObserver(SQLRunnerStaticParameters.getConnectionSqlRunnerParameters().getSqlRunnerToolBar());
		notifyObservers(jTabbedPane);
	}
}
