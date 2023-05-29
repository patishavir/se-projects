package oz.infra.db.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Logger;

public class DerbyUtils {
	private static Logger logger = Logger.getLogger(DerbyUtils.class.getName());

	public static void createDB(final String databasePath, final String databaseName,
			final String user, final String password) {

		String protocol = "jdbc:derby:";
		Properties derbyProperties = new Properties();
		derbyProperties.put("user", user);
		derbyProperties.put("password", password);
		String url = protocol + databaseName + ";" + "create=true";
		try {
			Connection connection = getConnection(url, databasePath, derbyProperties);
			logger.info("Created database " + databaseName);
			connection.commit();
			connection.close();
			logger.info("Committed transaction and closed connection");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
	}

	public static Connection getConnection(final String databasePath, final String databaseName,
			final String user, final String password) {
		String protocol = "jdbc:derby:";
		Properties derbyProperties = new Properties();
		derbyProperties.put("user", user);
		derbyProperties.put("password", password);
		String url = protocol + databaseName + ";" + "create=true";
		return getConnection(url, databasePath, derbyProperties);
	}

	private static Connection getConnection(final String url, final String databasePath,
			final Properties derbyProperties) {
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";

		Connection connection = null;
		try {
			Properties systemProperties = System.getProperties();
			systemProperties.put("derby.system.home", databasePath);
			System.setProperties(systemProperties);
			Class.forName(driver).newInstance();
			logger.info(driver + " successfully loaded");
			connection = DriverManager.getConnection(url, derbyProperties);
			logger.info("Connected to database. url:  " + url);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
		return connection;
	}
}
