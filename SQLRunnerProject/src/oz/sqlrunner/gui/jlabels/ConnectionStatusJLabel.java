package oz.sqlrunner.gui.jlabels;

import java.util.Observable;

import oz.infra.swing.container.ContainerUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;

public class ConnectionStatusJLabel extends StatusBarJLabel {

	public ConnectionStatusJLabel() {
		super();
		setText(SQLRunnerConnectionParameters.NOT_CONNECTED);
		SQLRunnerStaticParameters.getConnectionSqlRunnerParameters().addObserver(this);
	}
	public void update(final Observable observable, final Object object) {
		setText(object.toString());
		ContainerUtils.refreshDisplay(this);
	}
}
