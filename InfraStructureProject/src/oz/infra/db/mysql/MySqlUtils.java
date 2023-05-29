package oz.infra.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class MySqlUtils {
	private static Logger logger = JulUtils.getLogger();
	private static final String mySqlDriverClassName = "com.mysql.jdbc.Driver";

	public static final Connection getMySqlConnection(final String host, final String port,
			final String database, final String user, final String password) {
		String mySqlPort = "3306";
		if (port != null) {
			mySqlPort = port;
		}
		try {
			Class.forName(mySqlDriverClassName).newInstance();
			Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + mySqlPort
					+ "/" + database, user, password);

			return conn;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
			return null;
		}

	}

}
