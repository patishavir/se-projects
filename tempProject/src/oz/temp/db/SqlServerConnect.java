package oz.temp.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqlServerConnect {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		doConnect();
	}

	private static Connection doConnect() {
		Connection conn;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			String url = "jdbc:sqlserver://10.18.197.106:1433;databaseName=P8Manage";
			String user = "P8CM";
			String password = "z13579";
			conn = DriverManager.getConnection(url, user, password);
			System.err.println(conn.toString());
		} catch (Exception e) {
			System.err.println("MSSQL Driver loading error ->" + e.toString());
			return null;
		}

		return conn;
	}
}
