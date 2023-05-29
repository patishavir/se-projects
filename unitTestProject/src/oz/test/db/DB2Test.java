package oz.test.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.db.DBUtils;
import oz.infra.db.db2.DB2Utils;
import oz.infra.db.metadata.DBMetaDataUtils;
import oz.infra.system.env.EnvironmentUtils;

public class DB2Test {
	private static Logger logger = Logger.getLogger(DB2Test.class.getName());

	public static void main(String[] args) {
		Connection connection = null;
		String url = null;
		String userName = null;
		String password = null;
		String computerName = EnvironmentUtils.getEnvironmentVariable("COMPUTERNAME");
		try {
			if (computerName.startsWith("WKSU")) {
				url = "jdbc:db2://DB2-T1:50001/DB2G";
				userName = "UTXET06";
				password = "2W3E4R";
				connection = DBUtils.getConnection(url, userName, password, DB2Utils.DB2_JCC_DB2DRIVER, true);
			} else {
				url = "jdbc:db2:SAMPLE";
				userName = "db2admin";
				password = "db2admin";
				connection = DBUtils.getConnection(url, userName, password, DB2Utils.DB2_JDBC_APP_DB2DRIVER, true);
			}
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			String sqlStatementString = "SELECT DISTINCT CREATOR FROM SYSIBM.SYSTABLES";
			sqlStatementString = "SELECT *  FROM SYSIBM.SYSTABLES";
			ResultSet resultSet = statement.executeQuery(sqlStatementString);
			String[] columnNames = DBMetaDataUtils.getResultSetColumnNames(resultSet);
			ArrayUtils.printArray(columnNames);
			// DBUtils.printResultSet(connection, sqlStatementString);
			// execute the query
			// ResultSet resultSet = statement.executeQuery(query);

			// while (resultSet.next()) {
			// System.out.println(resultSet.getRow());
			//
			// }
			String[] schemaNames = DBMetaDataUtils.getDB2SchemaNames(connection);
			ArrayUtils.printArray(schemaNames, "\n", null);
			logger.info("All done !");
			// resultSet.close();
			// statement.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
	}

	private void con() {
		// load the DB2 Driver
		// Class.forName("com.ibm.db2.jcc.DB2Driver");
		// establish a connection to DB2
		// Connection connection = DriverManager.getConnection(
		// "jdbc:db2://DB2-T1:50001/DB2G", "UTXET06", "1q2w3e");
	}
}
