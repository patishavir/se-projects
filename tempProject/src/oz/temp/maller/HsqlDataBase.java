package oz.temp.maller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class HsqlDataBase {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		connect();
	}

	private static void connect() {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "SA", "");
			logger.info(conn.toString());

			// STEP 4: Execute a query
			System.out.println("Creating table in given database...");
			stmt = conn.createStatement();

			String sql = "CREATE TABLE REGISTRATION " + "(id INTEGER not NULL, "
					+ " first VARCHAR(255), " + " last VARCHAR(255), " + " age INTEGER, "
					+ " PRIMARY KEY ( id ))";

			stmt.executeUpdate(sql);
			System.out.println("Created table in given database...");
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}// do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		System.out.println("Goodbye!");
	}
}