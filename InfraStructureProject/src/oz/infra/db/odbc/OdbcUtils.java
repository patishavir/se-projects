package oz.infra.db.odbc;

import java.sql.Connection;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public final class OdbcUtils {
	public static final String JDBC_ODBC_DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver";
	private static Logger logger = JulUtils.getLogger();

	public static Connection getJDBCODBCConnection(final String url, final String userName,
			final String password) {
		Connection connection = null;
		try {
			Class.forName(OdbcUtils.JDBC_ODBC_DRIVER);
			connection = java.sql.DriverManager.getConnection(url, userName, password);
			if (connection != null) {
				logger.info("Connection to " + url + " has been successfully established!");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			logger.warning(exception.getMessage());
		}
		return connection;
	}
}
