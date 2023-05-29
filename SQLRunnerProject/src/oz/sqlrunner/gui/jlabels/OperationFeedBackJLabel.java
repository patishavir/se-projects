package oz.sqlrunner.gui.jlabels;

import java.util.Observable;

import oz.infra.swing.container.ContainerUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.handlers.RowCountHandler;
import oz.sqlrunner.tableoperations.ExportObserver;
import oz.sqlrunner.tableoperations.ImportObserver;
import oz.sqlrunner.tableoperations.ShowResultSetHandler;
import oz.sqlrunner.tableoperations.TableOperationHandler;

public class OperationFeedBackJLabel extends StatusBarJLabel {
	public OperationFeedBackJLabel() {
		RowCountHandler.getSqlRunneRowCountHandler().addObserver(this);
		TableOperationHandler.getTableoperationhandler().addObserver(this);
		ExportObserver.getExportObserver().addObserver(this);
		ImportObserver.getImportObserver().addObserver(this);
		ShowResultSetHandler.getShowResultSetHandler().addObserver(this);
		SQLRunnerStaticParameters.getConnectionSqlRunnerParameters().addObserver(this);
	}

	public final void update(final Observable observable, final Object object) {
		if (observable instanceof SQLRunnerConnectionParameters) {
			if (object instanceof String
					&& object.toString().equalsIgnoreCase(SQLRunnerConnectionParameters.NOT_CONNECTED)) {
				setText("");
			}
		} else {
			setText(object.toString());
		}
		ContainerUtils.refreshDisplay(this.getParent());
	}
}
