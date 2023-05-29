package oz.infra.db;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.db.outcome.SqlExecutionOutcome;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;

public class DBFileUtils {
	public static final String DB_PROPERTIES_FILE_PATH = "dbPropertiesFilePath";
	public static final String SQL_SCRIPT_FILE_PATH = "sqlScriptFilePath";
	public static final String CHARSET_NAME = "charsetName";

	private static final Logger logger = JulUtils.getLogger();

	public static List<SqlExecutionOutcome> runSqlScriptFromFile(final Connection connection,
			final Properties runSqlScriptFromFileProperties) {
		String sqlScriptFilePath = runSqlScriptFromFileProperties.getProperty(DBFileUtils.SQL_SCRIPT_FILE_PATH);
		String charSetName = runSqlScriptFromFileProperties.getProperty(DBFileUtils.CHARSET_NAME);
		String sqlStatementString = DBFileUtils.readSqlStatementsFromFile(sqlScriptFilePath, charSetName);
		return DBUtils.runSqlStatements(connection, sqlStatementString);
	}

	public static List<SqlExecutionOutcome> runSqlScriptFromFile(final Properties runSqlScriptFromFileProperties) {
		Connection connection = DBUtils
				.getConnection(runSqlScriptFromFileProperties.getProperty(DB_PROPERTIES_FILE_PATH));
		return runSqlScriptFromFile(connection, runSqlScriptFromFileProperties);
	}

	public static String readSqlStatementsFromFile(final String sqlScriptFilePath, final String charSetNameParam) {
		String sqlStatementString = null;
		File sqlScriptFile = new File(sqlScriptFilePath);
		if (sqlScriptFile.isFile()) {
			String charSetName = charSetNameParam;
			if (FileUtils.isFileStartWithBom(sqlScriptFilePath)) {
				charSetName = OzConstants.UTF8_CHARSET;
			}
			sqlStatementString = FileUtils.readTextFileWithEncoding(sqlScriptFilePath, charSetName);
			if (charSetName.equals(OzConstants.UTF8_CHARSET)) {
				sqlStatementString = sqlStatementString.substring(1);
			}
		}
		return sqlStatementString;
	}
}
