package oz.sqlrunner.tableoperations;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.db.DBUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;

public class ImportObserver extends Observable implements Observer {
	private static ImportObserver importObserver = new ImportObserver();
	private static Logger logger = JulUtils.getLogger();

	public static ImportObserver getImportObserver() {
		return importObserver;
	}

	@Override
	public final void update(final Observable observable,
			final Object parametersHashTableObj) {
		logger.info("Starting ...");
		Hashtable<String, String> importParametersHashTable = (Hashtable<String, String>) parametersHashTableObj;
		String importFilePath = importParametersHashTable.get("importFilePath");
		logger.info("importFilePath: " + importFilePath);
		String[] importFileLinesArray = FileUtils
				.readTextFile2Array(importFilePath);
		ArrayUtils.printArray(importFileLinesArray, "\n",
				"importFileLinesArray\n", Level.INFO);
		String columnNames = importFileLinesArray[0];
		String columnTypes[] = importFileLinesArray[1].split(OzConstants.COMMA);
		SQLRunnerConnectionParameters sqlRunnerConnectionParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		Connection connection = sqlRunnerConnectionParameters.getConnection();
		String tableName = sqlRunnerConnectionParameters.getTableName();
		String tableCreator = sqlRunnerConnectionParameters.getTableCreator();
		String importMessage = "Import operation failed";
		String[] dataArray = ArrayUtils.selectArrayRowsByStartingRow(
				importFileLinesArray, 2);
		try {
			int[] updateCounts = DBUtils.importIntoTable(connection,
					columnNames, columnTypes, dataArray, tableName,
					tableCreator);
			importMessage = StringUtils.concat(
					tableCreator,
					OzConstants.DOT,
					tableName,
					" Import has succeeded. "
							+ String.valueOf(updateCounts.length),
					" records have been processed.");
		} catch (SQLException ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			SQLException nex = ex.getNextException();
			ExceptionUtils.printMessageAndStackTrace(nex);
			try {
				logger.info("Rolling back import ...");
				connection.rollback();
			} catch (Exception ex2) {
				ExceptionUtils.printMessageAndStackTrace(ex2);
			}
		}
		setChanged();
		notifyObservers(importMessage);
	}
}
