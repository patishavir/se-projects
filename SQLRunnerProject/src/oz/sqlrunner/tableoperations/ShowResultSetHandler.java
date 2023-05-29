package oz.sqlrunner.tableoperations;

import java.sql.Connection;
import java.util.Observable;
import java.util.logging.Logger;

import javax.swing.JTable;

import oz.infra.constants.OzConstants;
import oz.infra.db.metadata.DBMetaDataUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.swing.jtable.JTableUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.gui.MetaDataJDialog;
import oz.sqlrunner.gui.listeners.enums.ObjectExplorerOperationEnum;

public class ShowResultSetHandler extends Observable {
	private static ShowResultSetHandler showResultSetHandler = new ShowResultSetHandler();
	private static Logger logger = JulUtils.getLogger();

	public static ShowResultSetHandler getShowResultSetHandler() {
		return showResultSetHandler;
	}

	public final void showResultSet(final ObjectExplorerOperationEnum objectExplorerOperationEnum) {
		logger.info("starting ...");
		SQLRunnerConnectionParameters sqlRunnerConnectionParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		Connection connection = sqlRunnerConnectionParameters.getConnection();
		String tableName = sqlRunnerConnectionParameters.getTableName();
		String tableCreator = sqlRunnerConnectionParameters.getTableCreator();
		JTable jtable = null;
		String title = null;
		String showResultSetMessage = null;
		switch (objectExplorerOperationEnum) {
		case SHOW_META_DATA:
			jtable = DBMetaDataUtils.getResultSetMetaDataAsJTable(connection, tableCreator, tableName);
			title = StringUtils.concat("Metadata for ", tableCreator, OzConstants.DOT, tableName);
			showResultSetMessage = " meta data records found";
			break;
		case SHOW_PRIVILEGES:
			jtable = DBMetaDataUtils.getPrevilegesAsJTable(connection, tableCreator, tableName);
			title = StringUtils.concat("Privileges for ", tableCreator, OzConstants.DOT, tableName);
			showResultSetMessage = " privilege records found";
			break;
		case SHOW_PRIMARY_KEYS:
			jtable = DBMetaDataUtils.getPrimaryKeysAsJTable(connection, tableCreator, tableName);
			title = StringUtils.concat("Primary keys for ", tableCreator, OzConstants.DOT, tableName);
			showResultSetMessage = " primay keys found";
			break;
		}
		int rowCount = 0;
		if (jtable != null) {
			rowCount = jtable.getRowCount();
		} else {
			jtable = JTableUtils.getSingleCellJTable("ResultSet is empty");
		}
		showResultSetMessage = String.valueOf(rowCount).concat(showResultSetMessage);
		new MetaDataJDialog(jtable, title);
		setChanged();
		notifyObservers(showResultSetMessage);
	}
}
