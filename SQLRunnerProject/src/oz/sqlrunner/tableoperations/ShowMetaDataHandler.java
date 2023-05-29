package oz.sqlrunner.tableoperations;

import java.sql.Connection;
import java.util.Observable;
import java.util.logging.Logger;

import javax.swing.JTable;

import oz.infra.constants.OzConstants;
import oz.infra.db.metadata.DBMetaDataUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.gui.MetaDataJDialog;

public class ShowMetaDataHandler extends Observable {
	private static ShowMetaDataHandler showMetaDataHandler = new ShowMetaDataHandler();
	private static Logger logger = JulUtils.getLogger();

	public static ShowMetaDataHandler getShowMetaDataHandler() {
		return showMetaDataHandler;
	}

	public final void showMetaData(final Object parametersHashTableObj) {
		logger.info("starting ...");
		SQLRunnerConnectionParameters sqlRunnerConnectionParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		Connection connection = sqlRunnerConnectionParameters.getConnection();
		String tableName = sqlRunnerConnectionParameters.getTableName();
		String tableCreator = sqlRunnerConnectionParameters.getTableCreator();
		JTable jtable = DBMetaDataUtils.getResultSetMetaDataAsJTable(connection, tableCreator, tableName);
		String title = StringUtils.concat("Metadata for ", tableCreator, OzConstants.DOT,
				tableName);
		new MetaDataJDialog(jtable, title);
		String showMetaDataMessage = "showMetaDataMessage";
		setChanged();
		notifyObservers(showMetaDataMessage);
	}
}
