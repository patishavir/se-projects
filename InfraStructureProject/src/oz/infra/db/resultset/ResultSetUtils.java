package oz.infra.db.resultset;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.db.DBUtils;
import oz.infra.db.metadata.DBMetaDataUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.runtime.RunTimeUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.varargs.VarArgsUtils;

public class ResultSetUtils {
	private static final int EXCLUDE_COLUMN_CODE = 1;
	private static final Logger logger = JulUtils.getLogger();

	public static int[] getExcludedColumnsArray(final String[] columnNames, final String excludedColumns) {
		int[] excludedColumnsArray = new int[columnNames.length];
		for (int i = 0; i < columnNames.length; i++) {
			String searchForString = OzConstants.NUMBER_SIGN.concat(columnNames[i]).concat(OzConstants.NUMBER_SIGN);
			logger.finest(searchForString);
			if (excludedColumns.indexOf(searchForString) == OzConstants.STRING_NOT_FOUND) {
				excludedColumnsArray[i] = 0;
			} else {
				excludedColumnsArray[i] = EXCLUDE_COLUMN_CODE;
			}
		}
		return excludedColumnsArray;
	}

	/*
	 * getResultSetAs1DimArray
	 */
	public static final String[] getResultSetAs1DimArray(final Connection connection, final String sqlStatementString) {
		ResultSet resultSet = DBUtils.getResultSet(connection, sqlStatementString);
		return getResultSetAs1DimArray(resultSet);
	}

	public static final String[] getResultSetAs1DimArray(final ResultSet resultSet) {
		String[] resultSetArray = null;
		ArrayList<String> arrayList = new ArrayList<String>();
		try {
			while (resultSet.next()) {
				arrayList.add(resultSet.getString(1));
			}
			arrayList.trimToSize();
			resultSetArray = new String[arrayList.size()];
			resultSetArray = arrayList.toArray(resultSetArray);
		} catch (SQLException exception) {
			exception.printStackTrace();
			logger.warning(exception.getMessage());
		}
		return resultSetArray;
	}

	/*
	 * getResultSetAs2DimArray
	 */
	public static final String[][] getResultSetAs2DimArray(final Connection connection,
			final String sqlStatementString) {
		logger.info(sqlStatementString);
		ResultSet resultSet = DBUtils.getResultSet(connection, sqlStatementString);
		return getResultSetAs2DimArray(resultSet);
	}

	public static final String[][] getResultSetAs2DimArray(final ResultSet resultSet) {
		ArrayList<String[]> resultSetArrayList = new ArrayList<String[]>();
		int rowCount = 0;
		int columnCount = -1;
		try {
			columnCount = DBMetaDataUtils.getResultSetColumnCount(resultSet);
			logger.finest("Column count: " + columnCount);
			int columnIndex;
			while (resultSet.next()) {
				String[] rowArray = new String[columnCount];
				for (columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
					rowArray[columnIndex - 1] = resultSet.getString(columnIndex);
				}
				resultSetArrayList.add(rowArray);
				rowCount++;
			}
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			logger.warning(sqlException.getMessage());
		}
		resultSetArrayList.trimToSize();
		String[][] resultSetArray;
		if (rowCount == 0) {
			resultSetArray = null;
		} else {
			resultSetArray = new String[rowCount][columnCount];
			for (int rowPtr = 0; rowPtr < resultSetArrayList.size(); rowPtr++) {
				String[] rowArray = resultSetArrayList.get(rowPtr);
				for (int colPtr = 0; colPtr < rowArray.length; colPtr++) {
					resultSetArray[rowPtr][colPtr] = rowArray[colPtr];
				}
			}
		}
		return resultSetArray;
	}

	public static final HashMap<String, String> getResultSetAsHashMap(final Connection connection,
			final String sqlStatementString) {
		logger.info(sqlStatementString);
		ResultSet resultSet = DBUtils.getResultSet(connection, sqlStatementString);
		HashMap<String, String> hashMap = getResultSetAsHashMap(resultSet);
		try {
			resultSet.close();
		} catch (SQLException ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return hashMap;
	}

	public static final HashMap<String, String> getResultSetAsHashMap(final ResultSet resultSet) {
		HashMap<String, String> resultSetHashMap = new HashMap<String, String>();
		int rowCount = 0;
		int columnCount = -1;
		try {
			columnCount = DBMetaDataUtils.getResultSetColumnCount(resultSet);
			logger.info("Column count: " + columnCount);
			if (columnCount != 2) {
				logger.warning("Result set must have exactly 2 columns!\n Processing has benn aborted. ");
			} else {
				while (resultSet.next()) {
					resultSetHashMap.put(resultSet.getString(1), resultSet.getString(2));
					rowCount++;
				}
			}
		} catch (SQLException sqlException) {
			ExceptionUtils.printMessageAndStackTrace(sqlException);
		}
		logger.info(String.valueOf(rowCount) + " rows have been processed.");
		return resultSetHashMap;
	}

	public static String[][] getResultSetMetaDataAsArray(final Connection connection, final String tableCreator,
			final String tableName) {
		return DBMetaDataUtils.getResultSetMetaDataAsArray(connection, tableCreator, tableName, Level.FINEST);
	}

	public static int getResultSetRowCount(final ResultSet resultSet) {
		int rowCount = 0;
		if (resultSet != null) {
			try {
				resultSet.last();
				rowCount = resultSet.getRow();
				resultSet.beforeFirst();
			} catch (SQLException exception) {
				exception.printStackTrace();
				logger.warning(exception.getMessage());
			}
		}
		return rowCount;
	}

	public static String[][] printResultSet(final Connection connection, final String sqlStatementString) {
		String[][] resultSetArray = getResultSetAs2DimArray(connection, sqlStatementString);
		ArrayUtils.print2DimArray(resultSetArray, Level.INFO);
		return resultSetArray;
	}

	public static String[][] printResultSet(final ResultSet resultSet) {
		String[][] resultSetArray = null;
		try {
			DBUtils.printColumnNames(resultSet.getMetaData());
			resultSetArray = getResultSetAs2DimArray(resultSet);
			ArrayUtils.print2DimArray(resultSetArray, Level.INFO);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return resultSetArray;
	}

	public static String[] getColumnNames(final ResultSet resultSet) {
		String[] columnNames = null;
		try {
			ResultSetMetaData rsMetaData = resultSet.getMetaData();
			columnNames = DBUtils.getResultSetColumnNames(rsMetaData);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return columnNames;
	}

	public static String resultSet2CsvString(final ResultSet resultSet, final String excludedColumns) {
		String[] columnNames = null;
		String[] columnClassNames = null;
		String[][] resultSetArray = null;
		StringBuilder stringBuilder = new StringBuilder();
		StopWatch stopWatch = new StopWatch();
		int records = 0;
		try {
			if (resultSet != null) {
				ResultSetMetaData rsMetaData = resultSet.getMetaData();
				resultSetArray = getResultSetAs2DimArray(resultSet);
				columnNames = DBUtils.getResultSetColumnNames(rsMetaData);
				int[] excludedColumnsArray = new int[columnNames.length];
				if (excludedColumns == null) {
					Arrays.fill(excludedColumnsArray, 0);
				} else {
					excludedColumnsArray = getExcludedColumnsArray(columnNames, excludedColumns);
				}
				stringBuilder.append(ArrayUtils.getString(columnNames, excludedColumnsArray, EXCLUDE_COLUMN_CODE));
				stringBuilder.append(SystemUtils.LINE_SEPARATOR);
				columnClassNames = DBUtils.getResultSetColumnClassNames(rsMetaData);
				stringBuilder.append(ArrayUtils.getString(columnClassNames, excludedColumnsArray, EXCLUDE_COLUMN_CODE));
				stringBuilder.append(SystemUtils.LINE_SEPARATOR);
				if (resultSetArray != null) {
					stringBuilder
							.append(ArrayUtils.getString(resultSetArray, excludedColumnsArray, EXCLUDE_COLUMN_CODE));
					records = resultSetArray.length;
				}
				logger.finest(StringUtils.concat(stringBuilder.toString(),
						"\nlength=" + String.valueOf(stringBuilder.length())));
			} else {
				logger.warning("Resultset is null");
			}
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		String message = "Csv has been Generated. " + String.valueOf(records) + " records. Processing time: ";
		logger.info(stopWatch.appendElapsedTimeToMessage(message));
		RunTimeUtils.displayMemoryInfo(Level.FINEST);
		return stringBuilder.toString();
	}

	public static String table2CsvString(final Connection connection, final String tableName, final String schemaName,
			final String excludedColumns) {
		String selectStatement = StringUtils.concat("SELECT * FROM ", schemaName, OzConstants.DOT);
		final String selectStatmentString = selectStatement + tableName;
		logger.finest("selectStatmentString: " + selectStatmentString);
		ResultSet resultSet = DBUtils.getResultSet(connection, selectStatmentString);
		String csvString = resultSet2CsvString(resultSet, excludedColumns);
		return csvString;
	}

	public static Outcome writeCsvFile(final String csvString, final String folderPath, final String database,
			final String schemaName, final String tableName, final Boolean... addTimeStamps) {
		Boolean addTimeStamp = VarArgsUtils.getMyArg(Boolean.TRUE, addTimeStamps);
		String timeStamp = OzConstants.EMPTY_STRING;
		if (addTimeStamp) {
			timeStamp = OzConstants.UNDERSCORE + DateTimeUtils.getTimeStamp();
		}
		String fileName = StringUtils.concat(database, OzConstants.UNDERSCORE, schemaName, OzConstants.UNDERSCORE,
				tableName, timeStamp, OzConstants.CSV_SUFFIX);
		String csvFilePath = PathUtils.getFullPath(folderPath, fileName);
		Outcome outcome = FileUtils.writeFile(csvFilePath, csvString, Level.INFO);
		String exportMessage = StringUtils.concat("export file ", csvFilePath,
				" has been succeedfully written. File length: ", String.valueOf(csvString.length()));
		if (outcome == Outcome.FAILURE) {
			exportMessage = StringUtils.concat("failed to write export file: ", csvFilePath);
		}
		logger.info(exportMessage);
		return outcome;
	}
}
