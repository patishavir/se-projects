package oz.infra.db.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.db.DBUtils;
import oz.infra.db.db2.DB2Utils;
import oz.infra.db.metadata.DBMetaDataUtils;
import oz.infra.db.outcome.SqlExecutionOutcome;
import oz.infra.db.resultset.ResultSetUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;


public class TestDBUtils {
	private static Connection connection = null;
	private static DatabaseMetaData databaseMetaData = null;
	private static Logger logger = JulUtils.getLogger();

	private static void getConnction() {
		try {
			String url = "jdbc:db2:SAMPLE";
			String userName = "db2admin";
			String password = "db2admin";
			connection = DBUtils.getConnection(url, userName, password, DB2Utils.DB2_JDBC_APP_DB2DRIVER, true);
			databaseMetaData = connection.getMetaData();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
	}

	private static String getSqlStatements1() {
		StringBuilder sb = new StringBuilder("SELECT VERSION FROM MATAF.BTT_VERSION_SUPPORT FETCH FIRST 50 ROWS ONLY");
		sb.append(";");
		sb.append("SELECT COUNT(*) FROM MATAF.BTT_VERSION_SUPPORT ");
		sb.append(";");
		sb.append("SELECT * FROM MATAF.HLST_STATUS");
		sb.append(";");
		sb.append("SELECT  HL_KOD_STATUS,HL_STATUS_TEUR FROM MATAF.HLST_STATUS");
		sb.append(";");
		// sb.append("SELECT  HL_KOD_STATU,HL_STATUS_TEU FROM MATAF.HLST_STATUS");
		// sb.append(";");
		// sb.append("INSERT into MATAF.BTT_VERSION_SUPPORT VALUES ( '5.28.99')");
		sb.append(";");
		sb.append(";");
		sb.append(";");
		sb.append("DELETE FROM MATAF.BTT_VERSION_SUPPORT WHERE VERSION='5.28.99'");
		sb.append(";");
		sb.append("INSERT into MATAF.BTT_VERSION_SUPPORT VALUES ( '5.28.99')");
		sb.append(";");
		sb.append("INSERT into MATAF.BTT_VERSION_SUPPORT VALUES ( '5.28.99')");
		sb.append(";");
		return sb.toString();
	}

	private static String getSqlStatements2() {
		String sqlStatement = "SELECT CREATOR, NAME, TYPE, COLCOUNT, REMARKS  FROM SYSIBM.SYSTABLES WHERE name LIKE '%BTT%' AND creator = 'MATAF'";
		return sqlStatement;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// getConnction();
		// metaData();
		// testGetResultSetMetaDataAsArray();
		// testRunSqlStatements("SELECT VERSION FROM MATAF.BTT_VERSION_SUPPORT FETCH FIRST 50 ROWS ONLY");
		// testRunSqlStatements(getSqlStatements1());
		// testRunSqlStatements(getSqlStatements2());
		// testExceuteStatement("SELECT * from MATAF.GLST_SEX");
		// testExceuteStatement("INSERT INTO MATAF.GLST_SEX values ('x','y')");
		// testExceuteStatement("DELETE FROM  MATAF.GLST_SEX WHERE OP_SEX='x'");

	}

	private static void testExceuteStatement(final String sqlStatementString) {
		try {
			Connection connection = DBUtils.getConnection("jdbc:db2://SNIF-UDB:50020/VZ4", "matafapp", "appm386",
					DB2Utils.DB2_JCC_DB2DRIVER, true);
			SqlExecutionOutcome sqlExecutionOutcome = DBUtils.executeStatement(sqlStatementString, connection);
			sqlExecutionOutcome.print(Level.INFO);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	private static void testGetResultSetMetaDataAsArray() {
		try {
			Connection connection1 = DBUtils.getConnection("jdbc:db2://SNIF-UDB:50000/MATAFDBT", "matafapp", "appm386",
					DB2Utils.DB2_JCC_DB2DRIVER, true);
			String[][] metaDataArray = ResultSetUtils.getResultSetMetaDataAsArray(connection1, "MATAF", "GLST_SEX");
			ArrayUtils.print2DimArray(metaDataArray, OzConstants.COMMA, Level.INFO);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	private static void testMetaDataUtils() {
		try {
			logger.info("**** connection: " + connection.toString());
			logger.info("**** displayDbProperties: " + DBMetaDataUtils.displayDbProperties(connection).toString());
			String catalog = connection.getCatalog();
			logger.info("**** catalog: " + catalog);
			// Properties properties = connection.getClientInfo();
			ResultSet catalogs = databaseMetaData.getCatalogs();
			logger.info("**** catalogs:");
			ResultSetUtils.printResultSet(catalogs);
			logger.info(catalogs.toString());
			logger.info("catalogTerm: " + databaseMetaData.getCatalogTerm());
			ResultSet schemas = databaseMetaData.getSchemas();
			logger.info("**** schemas:");
			ResultSetUtils.printResultSet(schemas);
			ResultSet tableTypes = databaseMetaData.getTableTypes();
			String[] tableTypesArray = ResultSetUtils.getResultSetAs1DimArray(tableTypes);
			ArrayUtils.printArray(tableTypesArray, Level.INFO);
			logger.info("schemaTerm: " + databaseMetaData.getSchemaTerm());
			ResultSet tables = databaseMetaData.getTables(null, null, "%", null);
			logger.info("**** tables:");
			ResultSetUtils.printResultSet(tables);
			// String[][] tablesArray = DBUtils.getResultSetAs2DimArray(tables);
			// ArrayUtils.printArray(tablesArray);
		} catch (Exception ex) {
			logger.info(ex.getMessage());
			ex.printStackTrace();
		}
	}

	private static void testRunSqlStatements(final String sqlStatementString) {
		try {
			Connection connection = DBUtils.getConnection("jdbc:db2://SNIF-UDB:50000/VZ4", "matafapp", "appm386",
					DB2Utils.DB2_JCC_DB2DRIVER, true);
			List<SqlExecutionOutcome> sqlExecutionOutcomeList = DBUtils
					.runSqlStatements(connection, sqlStatementString);
			for (SqlExecutionOutcome sqlExecutionOutcome : sqlExecutionOutcomeList) {
				if (sqlExecutionOutcome != null) {
					sqlExecutionOutcome.print();
				} else {
					logger.info("sqlExecutionOutcome is null for ".concat(sqlStatementString));
				}
			}
			logger.info("all done. Number of results: " + String.valueOf(sqlExecutionOutcomeList.size()));
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}
}
