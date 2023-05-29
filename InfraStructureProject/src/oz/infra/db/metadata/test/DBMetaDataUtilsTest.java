package oz.infra.db.metadata.test;

import java.sql.Connection;
import java.util.logging.Level;

import oz.infra.array.ArrayUtils;
import oz.infra.db.DBUtils;
import oz.infra.db.db2.DB2Utils;
import oz.infra.db.metadata.DBMetaDataUtils;
import oz.infra.exception.ExceptionUtils;

public class DBMetaDataUtilsTest {

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		Connection connection = getConnection();
		// DBMetaDataUtils.getPrevileges(connection, "MATAF",
		// "CICS_PROTO_SELECT");
		// getPrevileges(connection);
		getPrimaryKeys(connection);
	}

	private static Connection getConnection() {
		Connection connection = null;
		try {
			String url = "jdbc:db2://SNIF-UDB:50000/TOZ";
			String userName = "MATAF";
			String password = "dd8877";
			// String driver DB2Utils.DB2_JDBC_APP_DB2DRIVER
			String driver = DB2Utils.DB2_JCC_DB2DRIVER;
			connection = DBUtils.getConnection(url, userName, password, driver, true);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return connection;
	}

	private static void getPrevileges(final Connection connection) {
		String[][] privilegesArray = DBMetaDataUtils.getPrevilegesAs2DimArray(connection, "MATAF", "CICS_PROTO_SELECT");
		ArrayUtils.print2DimArray(privilegesArray, Level.INFO);
	}

	private static void getPrimaryKeys(final Connection connection) {
		String[][] privilegesArray = DBMetaDataUtils.getPrimaryKeys(connection, "MATAF", "CICS_PROTO_SELECT");
		// ArrayUtils.print2DimArray(privilegesArray, Level.INFO);
	}
}
