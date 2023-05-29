package oz.infra.db.oracle.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.logging.Logger;

import oz.infra.db.oracle.OracleUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.string.StringUtils;

public class TestOracleUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testGetOracleThinJDBCConnection();
		testRunSqlPlus();
	}

	private static void testGetOracleThinJDBCConnection() {
		String host = "10.18.189.10";
		String port = "1521";
		String sid = "gmprod";
		String user = "s177571";
		String password = "oded123";
		Connection connection = OracleUtils.getOracleThinJDBCConnection(host,
				port, sid, user, password);
		try {
			DatabaseMetaData dbmd = connection.getMetaData();
			logger.info(StringUtils.concat(dbmd.getUserName(),
					" connected to ", dbmd.getURL()));
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	private static void testRunSqlPlus() {
		String sqlPlusPath = "C:\\app\\s177571\\product\\11.2.0\\client_32bit_1\\sqlplus.exe";
		String username = "haavara_LEyizur";
		String password = "yizur";
		// String netService = "DWPROD_Q.FIBI.CORP";
		String netService = "FATCAT.FIBI.CORP";
		String scriptPath = ".\\oz\\infra\\db\\oracle\\test\\scripts\\test.sql";
		SystemCommandExecutorResponse systemCommandExecutorResponse = OracleUtils
				.runSqlPlus(sqlPlusPath, username, password, netService, scriptPath);
		logger.info(systemCommandExecutorResponse
				.getExecutorResponse());
	}
}
