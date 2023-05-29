package oz.infra.db.mssql;

import java.sql.Connection;

import oz.infra.db.DBUtils;

public class MsSqlUtils {
	public static final String DRIVER_CLASS = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	public static final String URL = "jdbc:microsoft:sqlserver://";

	public static final Connection getMSSQL2KJDBCConnection(final String serverName, final String portNumber,
			final String databaseName, final String userName, final String password) {

		String selectMethod = "cursor";
		// Informs the driver to use server a side-cursor,
		// which permits more than one active statement
		// on a connection.
		return DBUtils.getConnection(DRIVER_CLASS, URL, serverName, portNumber, databaseName, selectMethod, userName,
				password);
	}

}
