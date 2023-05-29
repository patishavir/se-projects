package oz.utils.db.selectinsert;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.db.DBUtils;
import oz.infra.db.outcome.SqlExecutionOutcome;
import oz.infra.db.resultset.ResultSetUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;
import oz.infra.string.StringUtils;

public class SelectInsert {

	private static final Logger logger = JulUtils.getLogger();

	private static ArrayList<String> generateInsertCommands(final ResultSet resultSet) {
		ArrayList<String> al = new ArrayList<String>();
		String[] columnNames = ResultSetUtils.getColumnNames(resultSet);
		String columnNamesString = StringUtils.join(columnNames, OzConstants.COMMA);
		List<String[]> resultSetListOfArrays = getResultSetAsListOfArrays(resultSet, columnNames);
		for (String[] row : resultSetListOfArrays) {
			StringBuilder valuesSb = new StringBuilder();
			for (int i = 0; i < row.length; i++) {
				valuesSb.append(OzConstants.QUOTE);
				valuesSb.append(row[i]);
				valuesSb.append(OzConstants.QUOTE);
				if (i < row.length - 1) {
					valuesSb.append(OzConstants.COMMA);
				}
			}
			String values = valuesSb.toString();
			String insertCommand = SelectInsertParameters.getInsertStatement();
			insertCommand = insertCommand.replace("%columnNames%", columnNamesString);
			insertCommand = insertCommand.replace("%values%", values);
			al.add(insertCommand);
			logger.info(insertCommand);
		}
		return al;
	}

	private static Connection getConnection(final String dbPropertiesFolderPath, final String db) {
		String dbPropertiesFolderAbsolutePath = PathUtils.getAbsolutePath(dbPropertiesFolderPath);
		String db2FromPropertiesFielPath = PathUtils.getFullPath(dbPropertiesFolderAbsolutePath,
				db + OzConstants.PROPERTIES_SUFFIX);
		Connection connection = DBUtils.getConnection(db2FromPropertiesFielPath);
		return connection;
	}

	private static ArrayList<String> getInsertCommands() {
		ArrayList<String> insertCommandsArrayList = null;
		try {
			logger.info(PrintUtils.getSeparatorLine(
					"processing source database " + SelectInsertParameters.getSourceDb(), OzConstants.EQUAL_SIGN));
			Connection sourceConnection = getConnection(SelectInsertParameters.getDbpropertiesFolderPath(),
					SelectInsertParameters.getSourceDb());
			sourceConnection.setReadOnly(true);
			ResultSet resultSet = DBUtils.getResultSet(sourceConnection, SelectInsertParameters.getSelectStatement());
			insertCommandsArrayList = generateInsertCommands(resultSet);
			DBUtils.closeConnection(sourceConnection);
		} catch (SQLException ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return insertCommandsArrayList;
	}

	private static final List<String[]> getResultSetAsListOfArrays(final ResultSet resultSet,
			final String[] columnNames) {
		List<String[]> resultSetArrayList = new ArrayList<String[]>();
		int rowCount = 0;
		int columnCount = -1;
		try {
			columnCount = columnNames.length;
			logger.finest("Column count: " + columnCount);
			while (resultSet.next()) {
				String[] rowArray = new String[columnCount];
				for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
					rowArray[columnIndex] = resultSet.getString(columnNames[columnIndex]);
				}
				resultSetArrayList.add(rowArray);
				rowCount++;
			}
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			logger.warning(sqlException.getMessage());
		}
		logger.info(String.valueOf(rowCount) + " rows and " + String.valueOf(columnCount)
				+ " columns have been processed.");
		return resultSetArrayList;

	}

	private static void insertResultSet() {
		ArrayList<String> insertCommandsArrayList = getInsertCommands();
		if (insertCommandsArrayList.size() == 0) {
			logger.warning("No insert commands. Porcessing has been terminated.");
		} else {
			String[] targetDbsArray = SelectInsertParameters.getTargetDbs().split(OzConstants.COMMA);
			for (String targetdb : targetDbsArray) {
				logger.info((PrintUtils.getSeparatorLine("processing target db " + targetdb, OzConstants.EQUAL_SIGN)));
				Connection targetConnection = getConnection(SelectInsertParameters.getDbpropertiesFolderPath(),
						targetdb);
				SqlExecutionOutcome sqleo = DBUtils.executeStatement(SelectInsertParameters.getDeleteStatement(),
						targetConnection);
				sqleo.print(Level.INFO);
				DBUtils.executeBatch(targetConnection, insertCommandsArrayList);
				try {
					DBUtils.closeConnection(targetConnection);
				} catch (Exception ex) {
					ExceptionUtils.printMessageAndStackTrace(ex);
				}
			}
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SelectInsertParameters.processParameters(args[0]);
		insertResultSet();
	}
}
