package oz.utils.db.run;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.db.DBFileUtils;
import oz.infra.db.outcome.SqlExecutionOutcome;
import oz.infra.logging.jul.JulUtils;

public class RunSqlScriptFromFile {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		ArrayUtils.printArray(args, "\n", "args array:\n");
		runSqlScriptFromFile(args);
	}

	private static void runSqlScriptFromFile(final String[] args) {
		Properties runSqlScriptFromFileProperties = new Properties();
		runSqlScriptFromFileProperties.put(DBFileUtils.DB_PROPERTIES_FILE_PATH, args[0]);
		runSqlScriptFromFileProperties.put(DBFileUtils.SQL_SCRIPT_FILE_PATH, args[1]);
		String filePath = args[2];
		runSqlScriptFromFileProperties.put(DBFileUtils.CHARSET_NAME, args[3]);
		List<SqlExecutionOutcome> sqlExecutionOutcomeList = DBFileUtils
				.runSqlScriptFromFile(runSqlScriptFromFileProperties);
		if (sqlExecutionOutcomeList != null) {
			logger.info(SqlExecutionOutcome.print(sqlExecutionOutcomeList).toString());
			SqlExecutionOutcome.writeResultSetToFile(sqlExecutionOutcomeList.get(0), filePath);
		} else {
			logger.warning("\nExcecution has failed. sqlExecutionOutcomeList is null.");
		}
	}
}
