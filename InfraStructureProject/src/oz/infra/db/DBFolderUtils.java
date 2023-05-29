package oz.infra.db;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.db.outcome.SqlExecutionOutcome;
import oz.infra.db.outcome.SqlScriptExecutionOutcome;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.io.filefilter.FileNameRegExpresionAndNameRange;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.string.WithinRangeEnum;
import oz.infra.system.env.EnvironmentUtils;

public class DBFolderUtils {
	private static final Logger logger = JulUtils.getLogger();

	public static List<SqlScriptExecutionOutcome> runSqlScriptsFromFolder(
			final Properties runSqlScriptFromFolderProperties) {

		List<SqlScriptExecutionOutcome> sqlScriptExecutionOutcomeList = null;
		Connection connection = DBUtils
				.getConnection(runSqlScriptFromFolderProperties.getProperty(DBFileUtils.DB_PROPERTIES_FILE_PATH));
		String sqlScriptsFolderPath = runSqlScriptFromFolderProperties.getProperty(DBFileUtils.SQL_SCRIPT_FILE_PATH);

		File sqlScriptFolder = new File(sqlScriptsFolderPath);
		if (sqlScriptFolder.isDirectory()) {
			String sqlFileNameRange = EnvironmentUtils.getEnvironmentVariable(ParametersUtils.SQL_FILE_NAME_RANGE);
			String logMessage = "\nstart running sql scripts from folder " + sqlScriptsFolderPath;
			if (sqlFileNameRange != null) {
				logMessage = logMessage + ". range filter: " + sqlFileNameRange;
			}
			logger.info(logMessage);
			FileNameRegExpresionAndNameRange fileNameFiltersSuffixAndNameRange = new FileNameRegExpresionAndNameRange(
					RegexpUtils.REGEXP_SQL_FILE, sqlFileNameRange, WithinRangeEnum.ExcludeLowIncludeHigh);
			String[] sqlScriptFilePathArray = sqlScriptFolder.list(fileNameFiltersSuffixAndNameRange);
			Arrays.sort(sqlScriptFilePathArray, String.CASE_INSENSITIVE_ORDER);
			sqlScriptExecutionOutcomeList = new ArrayList<SqlScriptExecutionOutcome>();
			for (String sqlScriptFilePath : sqlScriptFilePathArray) {
				sqlScriptFilePath = PathUtils.getFullPath(sqlScriptsFolderPath, sqlScriptFilePath);
				if (FileUtils.isFileExists(sqlScriptFilePath)) {
					runSqlScriptFromFolderProperties.put(DBFileUtils.SQL_SCRIPT_FILE_PATH, sqlScriptFilePath);
					List<SqlExecutionOutcome> sqlExecutionOutcomeList = DBFileUtils.runSqlScriptFromFile(connection,
							runSqlScriptFromFolderProperties);
					SqlScriptExecutionOutcome sqlScriptExecutionOutcome = new SqlScriptExecutionOutcome(
							sqlExecutionOutcomeList, sqlScriptFilePath);
					sqlScriptExecutionOutcomeList.add(sqlScriptExecutionOutcome);
				} 
			}
		} else {
			String errorMessage = sqlScriptsFolderPath + " is not a folder.\nOperation has been aborted !";
			logger.warning(errorMessage);
			System.exit(OzConstants.EXIT_STATUS_ABNORMAL);
		}
		runSqlScriptFromFolderProperties.put(DBFileUtils.SQL_SCRIPT_FILE_PATH, sqlScriptsFolderPath);
		return sqlScriptExecutionOutcomeList;
	}

}
