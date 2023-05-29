package oz.zel.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.db.DBUtils;
import oz.infra.db.metadata.DBMetaDataUtils;
import oz.infra.db.odbc.OdbcUtils;
import oz.infra.logging.jul.JulUtils;

public class Xls2XmlConversion {
	private static final String EXCEL_URL = "jdbc:odbc:ZelDS";
	private static final String SELECT_STRUCTURE = "select * from [обрд$]";
	private static final String SELECT_DATA = "select * from [DATA$]";
	private static Connection connection;
	private static HashMap<String, String> nameConversionHashMap = new HashMap<String, String>();
	private static Logger logger = JulUtils.getLogger();

	public static void doJdbcProceesning() {
		connection = OdbcUtils.getJDBCODBCConnection(EXCEL_URL, "", "");
		buildNameConversionHashMap();
		processDataSheet();
	}

	private static void buildNameConversionHashMap() {
		try {
			ResultSet resultSet = DBUtils.getResultSet(connection, SELECT_STRUCTURE);
			while (resultSet.next()) {
				String name1 = resultSet.getString(1);
				String name2 = resultSet.getString(2);
				logger.info(name1 + OzConstants.EQUAL_SIGN + name2);
				nameConversionHashMap.put(name1, name2);
			}
			// resultSet.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
	}

	private static void processDataSheet() {
		try {
			ResultSet resultSet = DBUtils.getResultSet(connection, SELECT_DATA);
			String[] columnNames = DBMetaDataUtils.getResultSetColumnNames(resultSet);
			int columnCount = columnNames.length;
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			while (resultSet.next()) {
				for (int column = 1; column <= columnCount; column++) {
					logger.info("ColumnClassName: " + resultSetMetaData.getColumnClassName(column)
							+ "  ColumnDisplaySize: "
							+ String.valueOf(resultSetMetaData.getColumnDisplaySize(column)));
					String key = columnNames[column - 1];
					String value = resultSet.getString(column);
					logger.info(key + OzConstants.EQUAL_SIGN + value);
				}
				System.exit(0);
			}
			// resultSet.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.warning(ex.getMessage());
			}
		}
	}

}
