package oz.infra.db.db2;

import java.sql.Connection;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.db.resultset.ResultSetUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public final class DB2Utils {

	public static final String DB2_JDBC_APP_DB2DRIVER = "COM.ibm.db2.jdbc.app.DB2Driver";
	public static final String DB2_JCC_DB2DRIVER = "com.ibm.db2.jcc.DB2Driver";
	public static final String TABLE_NAME = "TABLE_NAME";
	public static final String COMMAND_SEPARATOR = OzConstants.SEMICOLON;

	private static final String REQORG_PREFIX = "Call Sysproc.admin_cmd ('";
	private static final String REQORG_SUFFIX = "')";

	private static Logger logger = JulUtils.getLogger();

	public static String adjustReorgStatement(final String sqlStatement) {
		String out = sqlStatement;
		if (sqlStatement.trim().toUpperCase().startsWith("REORG ")) {
			out = StringUtils.concat(REQORG_PREFIX, sqlStatement, REQORG_SUFFIX);
			logger.info(out + " added reog prefix, suffix ");
		}
		return out;
	}

	public static String[] getDB2TableNames(final Connection connection) {
		final String sqlStatementString = "SELECT DISTINCT NAME FROM SYSIBM.SYSTABLES";
		return ResultSetUtils.getResultSetAs1DimArray(connection, sqlStatementString);
	}

	public static String getDBNameFromUrl(final String url) {
		String dbName = url.substring(1 + url.lastIndexOf(OzConstants.SLASH));
		return dbName;
	}

	private DB2Utils() {
		super();
	}
}
