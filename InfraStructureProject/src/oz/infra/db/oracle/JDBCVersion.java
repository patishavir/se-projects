package oz.infra.db.oracle;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

class JDBCVersion {
	public static void main(final String[] args) throws SQLException {
		// Load the Oracle JDBC driver
		String serverName = args[0];
		String port = args[1];
		String sid = args[2];
		String userName = args[3];
		String userPassword = args[4];
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@" + serverName + ":"
				+ port + ":" + sid, userName, userPassword);
		// Create Oracle DatabaseMetaData object
		DatabaseMetaData meta = conn.getMetaData();
		// gets driver info:
		System.out.println("JDBC driver version is " + meta.getDriverVersion());
	}
}
