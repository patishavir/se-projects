package oz.sqlrunner.gui.jlabels;

import java.util.Observable;

import oz.infra.swing.container.ContainerUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.handlers.ObjectExplorerHandler;

public class DatabaseObjectsCountJLabel extends StatusBarJLabel {
	public DatabaseObjectsCountJLabel() {
		ObjectExplorerHandler.getSqlRunnerObjectExplorerHandler().addObserver(this);
		SQLRunnerStaticParameters.getConnectionSqlRunnerParameters().addObserver(this);
	}

	public void update(final Observable observable, final Object object) {
		if (observable instanceof SQLRunnerConnectionParameters) {
			if (object instanceof String
					&& object.toString().equalsIgnoreCase(
							SQLRunnerConnectionParameters.NOT_CONNECTED)) {
				setText("");
			}
		} else {
			setText(object.toString());
		}
		ContainerUtils.refreshDisplay(this);
	}
}
