package oz.infra.db.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;

public class OracleUtils {
	private static Logger logger = JulUtils.getLogger();

	public static final Connection getOracleOciJDBCConnection(final String tnsName, final String user,
			final String password) {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			// Prompt the user for connect information
			String connectionString = "jdbc:oracle:oci:@" + tnsName;
			System.out.println("connectionString: " + connectionString);
			Connection conn = DriverManager.getConnection(connectionString, user, password);
			System.out.println("JDBC conncetion to " + tnsName + " has been successfully established");
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return null;
		}
	}

	public static final Connection getOracleThinJDBCConnection(final String host, final String port, final String sid,
			final String user, final String password) {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			// Prompt the user for connect information
			String connectionString = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
			logger.info("connectionString: " + connectionString);
			Connection conn = DriverManager.getConnection(connectionString, user, password);
			logger.info("Conncetion to " + host + ":" + port + ":" + sid + " has been successfully established");
			return conn;
		} catch (SQLException sex) {
			ExceptionUtils.printMessageAndStackTrace(sex);
			return null;
		}
	}

	public static PoolDataSource getPoolDataSource() {
		// Creating a pool-enabled data source
		PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();
		// Setting connection properties of the data source
		try {
			pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
			pds.setURL("jdbc:oracle:thin:@//localhost:1521/XE");
			pds.setUser("hr");
			pds.setPassword("hr");
			// Setting pool properties
			pds.setInitialPoolSize(5);
			pds.setMinPoolSize(5);
			pds.setMaxPoolSize(10);
			// Borrowing a connection from the pool
			// Connection conn = pds.getConnection();
			// System.out.println("\nConnection borrowed from the pool");
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return pds;
	}

	public static SystemCommandExecutorResponse runSqlPlus(final String sqlPlusPath, final String username,
			final String password, final String tnsAlias, final String scriptPath) {
		String[] parameters = { sqlPlusPath, username + OzConstants.SLASH + password + OzConstants.AT_SIGN + tnsAlias,
				OzConstants.AT_SIGN.concat(scriptPath) };
		String message = " running sqlplus with the following patameters:\n";
		ArrayUtils.printArray(parameters, OzConstants.BLANK, message, Level.INFO);
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(parameters);
		return systemCommandExecutorResponse;
	}
}
