package oz.sqlrunner.tableoperations;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import oz.infra.db.resultset.ResultSetUtils;
import oz.infra.io.FolderUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;

public class ExportObserver extends Observable implements Observer {
	private static ExportObserver exportObserver = new ExportObserver();
	private static Logger logger = JulUtils.getLogger();

	public static ExportObserver getExportObserver() {
		return exportObserver;
	}

	@Override
	public final void update(final Observable observable, final Object parametersHashTableObj) {
		Hashtable<String, String> exportParametersHashTable = (Hashtable<String, String>) parametersHashTableObj;
		String exportFolderPath = exportParametersHashTable.get("exportFolderPath");
		FolderUtils.createFolderIfNotExists(exportFolderPath);
		SQLRunnerConnectionParameters sqlRunnerConnectionParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		Connection connection = sqlRunnerConnectionParameters.getConnection();
		String tableName = sqlRunnerConnectionParameters.getTableName();
		String tableCreator = sqlRunnerConnectionParameters.getTableCreator();
		String csvString = ResultSetUtils.table2CsvString(connection, tableName, tableCreator, null);
		logger.info(sqlRunnerConnectionParameters.getDataBaseDisplayName());
		Outcome outcome = ResultSetUtils.writeCsvFile(csvString, exportFolderPath,
				sqlRunnerConnectionParameters.getDataBaseDisplayName(), tableCreator, tableName);
		String exportMessage = "Export operation has completed successfully ";
		if (outcome == Outcome.FAILURE) {
			exportMessage = "Export operation failed";
		}
		setChanged();
		notifyObservers(exportMessage);
	}
}
