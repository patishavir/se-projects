package oz.infra.db.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.db.DBUtils;
import oz.infra.db.db2.DB2Utils;
import oz.infra.db.resultset.ResultSetUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.font.FontUtils;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.KeyValuePair;
import oz.infra.print.PrintUtils;
import oz.infra.print.PrintUtils.PrintOption;
import oz.infra.string.StringUtils;
import oz.infra.swing.jtable.JTableUtils;

public class DBMetaDataUtils {
	private static final Logger logger = JulUtils.getLogger();

	public static StringBuilder displayDbProperties(final Connection connection) {
		java.sql.DatabaseMetaData databaseMetaData = null;
		java.sql.ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			if (connection != null) {
				databaseMetaData = connection.getMetaData();
				sb.append("Driver Information");
				sb.append("\tDriver Name: " + databaseMetaData.getDriverName());
				sb.append("\tDriver Version: " + databaseMetaData.getDriverVersion());
				sb.append("\nDatabase Information ");
				sb.append("\tDatabase Name: " + databaseMetaData.getDatabaseProductName());
				sb.append("\tDatabase Version: " + databaseMetaData.getDatabaseProductVersion());
				sb.append("\tAvalilable Catalogs ");
				rs = databaseMetaData.getCatalogs();
				while (rs.next()) {
					sb.append("\tcatalog: " + rs.getString(1));
				}
				rs.close();
				rs = null;
			} else {
				sb.append("Error: No active Connection");
			}
		} catch (SQLException sqlEx) {
			ExceptionUtils.printMessageAndStackTrace(sqlEx);
			sb.append(sqlEx.getMessage());
		}
		logger.finest(sb.toString());
		return sb;
	}

	private static JTable fixJtable(final JTable jtable, final String[] columnNames,
			final String[][] columnMetaDataArray) {
		jtable.setAutoCreateRowSorter(true);
		JTableHeader jtableHeader = jtable.getTableHeader();
		jtableHeader.setFont(FontUtils.ARIAL_BOLD_13);
		JTableUtils.setHeaderHorizontalAlignment(jtable, SwingConstants.LEFT);
		JTableUtils.setColumnsWidths(jtable, columnMetaDataArray, columnNames);
		return jtable;
	}

	public static String getDataBaseMetaDataDetails(final Connection connection) {
		String databaseMetaDataDetails = "";
		if (connection != null) {
			try {
				StringBuffer databaseMetaDataDetailsSB = new StringBuffer();
				DatabaseMetaData databaseMetaData = connection.getMetaData();
				int databaseMajorVersion = databaseMetaData.getDatabaseMajorVersion();
				int databaseMinorVersion = databaseMetaData.getDatabaseMinorVersion();
				String databaseProductName = databaseMetaData.getDatabaseProductName();
				String databaseProductVersion = databaseMetaData.getDatabaseProductVersion();
				String driverVersion = databaseMetaData.getDriverVersion();
				int driverMajorVersion = databaseMetaData.getDriverMajorVersion();
				int driverMinorVersion = databaseMetaData.getDriverMinorVersion();
				String driverName = databaseMetaData.getDriverName();
				int jdbcMajorVersion = databaseMetaData.getJDBCMajorVersion();
				int jdbcMinorVersion = databaseMetaData.getJDBCMinorVersion();
				databaseMetaDataDetailsSB.append("Database Product: " + "\t" + databaseProductName + "  "
						+ databaseProductVersion + "  version: " + String.valueOf(databaseMajorVersion) + "."
						+ String.valueOf(databaseMinorVersion) + DBUtils.LINE_SEPARATOR);
				databaseMetaDataDetailsSB.append("JDBC Driver : " + "\t" + driverName + "  version: " + driverVersion
						+ "  (" + String.valueOf(driverMajorVersion) + "." + String.valueOf(driverMinorVersion) + ")"
						+ DBUtils.LINE_SEPARATOR);
				databaseMetaDataDetailsSB.append("JDBC version : " + "\t" + String.valueOf(jdbcMajorVersion) + "."
						+ String.valueOf(jdbcMinorVersion) + DBUtils.LINE_SEPARATOR);
				databaseMetaDataDetails = databaseMetaDataDetailsSB.toString();
			} catch (Exception sqle) {
				ExceptionUtils.printMessageAndStackTrace(sqle);
			}
		}
		return databaseMetaDataDetails;
	}

	public static String[] getDB2SchemaNames(final Connection connection) {
		final String sqlStatementString = "SELECT DISTINCT CREATOR FROM SYSIBM.SYSTABLES";
		return ResultSetUtils.getResultSetAs1DimArray(connection, sqlStatementString);
	}

	public static String[][] getPrevilegesAs2DimArray(final Connection conn, final String schemaPattern,
			final String tableNamePattern) {
		String[][] privilegesArray = null;
		try {
			ResultSet privileges = null;
			DatabaseMetaData meta = conn.getMetaData();
			// The '_' character represents any single character.
			// The '%' character represents any sequence of zero
			// or more characters.
			privileges = meta.getTablePrivileges(conn.getCatalog(), schemaPattern, tableNamePattern);
			privilegesArray = ResultSetUtils.getResultSetAs2DimArray(privileges);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return privilegesArray;
	}

	public static JTable getPrevilegesAsJTable(final Connection conn, final String schemaPattern,
			final String tableNamePattern) {
		JTable jtable = null;
		try {
			ResultSet privileges = null;
			DatabaseMetaData meta = conn.getMetaData();
			// The '_' character represents any single character.
			// The '%' character represents any sequence of zero
			// or more characters.
			privileges = meta.getTablePrivileges(conn.getCatalog(), schemaPattern, tableNamePattern);
			String[] columnNames = DBMetaDataUtils.getResultSetColumnNames(privileges);
			String[][] privilegesArray = ResultSetUtils.getResultSetAs2DimArray(privileges);
			jtable = new JTable(privilegesArray, columnNames);
			fixJtable(jtable, columnNames, privilegesArray);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return jtable;
	}

	public static JTable getPrimaryKeysAsJTable(final Connection connection, final String schema, final String table) {
		JTable jtable = null;
		try {
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet resultSet = databaseMetaData.getPrimaryKeys(connection.getCatalog(), schema, table);
			String[] columnNames = DBMetaDataUtils.getResultSetColumnNames(resultSet);
			String[][] primaryKeysArray = ResultSetUtils.getResultSetAs2DimArray(resultSet);
			if (primaryKeysArray != null) {
				jtable = new JTable(primaryKeysArray, columnNames);
				fixJtable(jtable, columnNames, primaryKeysArray);
			}
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return jtable;
	}

	public static String[][] getPrimaryKeys(final Connection connection, final String schema, final String table) {
		String[][] resultSetArray = null;
		try {
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet resultSet = databaseMetaData.getPrimaryKeys(connection.getCatalog(), schema, table);
			resultSetArray = ResultSetUtils.printResultSet(resultSet);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return resultSetArray;

	}

	/*
	 * getResultSetColumnClassNames
	 */
	public static String[] getResultSetColumnClassNames(final ResultSet resultSet) {
		String[] columnClassNames = null;
		if (resultSet != null) {
			logger.finest("Entering getResultSetColumnNames: " + resultSet.toString());
			try {
				ResultSetMetaData rsMetaData = resultSet.getMetaData();
				columnClassNames = DBUtils.getResultSetColumnClassNames(rsMetaData);
			} catch (Exception exception) {
				exception.printStackTrace();
				String errorMessage = exception.getMessage();
				logger.severe(errorMessage);
				JOptionPane.showMessageDialog(null, errorMessage, errorMessage, JOptionPane.ERROR_MESSAGE);
			}
		}
		return columnClassNames;
	}

	public static final int getResultSetColumnCount(ResultSet resultSet) throws SQLException {
		ResultSetMetaData rsMetaData = resultSet.getMetaData();
		return rsMetaData.getColumnCount();
	}

	/*
	 * getResultSet
	 */
	public static String[] getResultSetColumnNames(final ResultSet resultSet) {
		String[] columnNames = null;
		if (resultSet != null) {
			logger.finest("Entering getResultSetColumnNames: " + resultSet.toString());
			try {
				ResultSetMetaData rsMetaData = resultSet.getMetaData();
				columnNames = DBUtils.getResultSetColumnNames(rsMetaData);
			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
			}
		}
		return columnNames;
	}

	public static ResultSetMetaData getResultSetMetaData(final Connection connection, final String tableCreator,
			final String tableName) {
		String sqlStatementString = StringUtils.concat("SELECT * FROM ", tableCreator, OzConstants.DOT, tableName);
		logger.finest(sqlStatementString);
		ResultSetMetaData resultSetMetaData = null;
		try {
			ResultSet resultSet = DBUtils.getResultSet(connection, sqlStatementString);
			resultSetMetaData = resultSet.getMetaData();
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return resultSetMetaData;
	}

	public static String[][] getResultSetMetaDataAsArray(final Connection connection, final String tableCreator,
			final String tableName, final Level level) {
		String[][] resultSetMetaDataArray = null;
		try {
			logger.finest(StringUtils.concat("tableName:", tableName));
			ResultSetMetaData resultSetMetaData = getResultSetMetaData(connection, tableCreator, tableName);
			if (resultSetMetaData != null) {
				int columnCount = resultSetMetaData.getColumnCount();
				List<KeyValuePair<String, String>> fieldNamesList = new ArrayList<KeyValuePair<String, String>>();
				fieldNamesList.add(new KeyValuePair<String, String>("columnName", "Column mame"));
				fieldNamesList.add(new KeyValuePair<String, String>("columnTypeName", "Column type"));
				fieldNamesList.add(new KeyValuePair<String, String>("columnDisplaySize", "Column display size"));
				fieldNamesList.add(new KeyValuePair<String, String>("nullable", "is nullable"));
				fieldNamesList.add(new KeyValuePair<String, String>("autoIncrement", "is autoIncrement"));
				fieldNamesList.add(new KeyValuePair<String, String>("isSigned", "is Signed"));
				fieldNamesList.add(new KeyValuePair<String, String>("columnClassName", "Java class name"));
				for (int i = 1; i <= columnCount; i++) {
					ColumnMetaData columnMetaData = new ColumnMetaData(resultSetMetaData, i);
					if (i == 1) {
						String header = PrintUtils.printObjectFields(columnMetaData, fieldNamesList, OzConstants.COMMA,
								PrintOption.HEADER_ONLY);
						String[] headerArray = header.split(OzConstants.COMMA);
						resultSetMetaDataArray = new String[columnCount + 1][headerArray.length];
						resultSetMetaDataArray[0] = headerArray;
					}
					String metaDataString = PrintUtils.printObjectFields(columnMetaData, fieldNamesList,
							OzConstants.COMMA, PrintOption.DATA_ONLY);
					resultSetMetaDataArray[i] = metaDataString.split(OzConstants.COMMA);
				}
				ArrayUtils.print2DimArray(resultSetMetaDataArray, level);
			}
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return resultSetMetaDataArray;
	}

	public static JTable getResultSetMetaDataAsJTable(final Connection connection, final String tableCreator,
			final String tableName) {
		String[][] headerAndcolumnMetaDataArray = ResultSetUtils.getResultSetMetaDataAsArray(connection, tableCreator,
				tableName);
		String[] columnNames = headerAndcolumnMetaDataArray[0];
		String[][] columnMetaDataArray = ArrayUtils.selectArrayRowsByStartingRow(headerAndcolumnMetaDataArray, 1);
		ArrayUtils.printArray(columnNames, Level.FINEST);
		ArrayUtils.print2DimArray(columnMetaDataArray, Level.FINEST);
		JTable jtable = new JTable(columnMetaDataArray, columnNames);
		return fixJtable(jtable, columnNames, columnMetaDataArray);
	}

	public static List<String> getSchemaTableNames(final Connection connection, final String schemaName) {
		List<String> tableNameList = new ArrayList<String>();
		try {
			if (connection != null) {
				DatabaseMetaData databaseMetaData = connection.getMetaData();
				ResultSet resultSet = databaseMetaData.getTables(null, schemaName, "%", null);
				while (resultSet.next()) {
					tableNameList.add(resultSet.getString(DB2Utils.TABLE_NAME));
					logger.finest(
							DB2Utils.TABLE_NAME + OzConstants.EQUAL_SIGN + tableNameList.get(tableNameList.size() - 1));
				}
			}

		} catch (SQLException ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return tableNameList;
	}

	public static List<String> getSchemaTableNames(final Properties properties, final String schemaName) {
		Connection connection = DBUtils.getConnection(properties);
		List<String> tableNames = DBMetaDataUtils.getSchemaTableNames(connection, schemaName);
		DBUtils.closeConnection(connection);
		return tableNames;
	}

	public static int tableMetaData2Csv(final Connection connection, final String folderPath, final String tableName1,
			final String schemaName) throws SQLException {
		String[][] metaDataArray = ResultSetUtils.getResultSetMetaDataAsArray(connection, schemaName, tableName1);
		StringBuilder stringBuilder = ArrayUtils.print2DimArray(metaDataArray, OzConstants.COMMA, Level.FINEST);
		logger.finest(stringBuilder.toString());
		logger.finest("length=" + String.valueOf(stringBuilder.length()));
		String filePath = StringUtils.concat(folderPath, OzConstants.DOT, tableName1, OzConstants.CSV_SUFFIX);
		FileUtils.writeFile(filePath, stringBuilder.toString(), Level.FINEST);
		return stringBuilder.length();
	}
}
