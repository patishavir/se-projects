package oz.utils.db.run.folder;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.db.DBFileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.system.SystemUtils;

public class RunSqlScriptsFromFolderMain {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		logger.info(SystemUtils.getRunInfo());
		ArrayUtils.printArray(args, "\n", "args array:\n");
		Properties runSqlScriptFromFolderProperties = processParameters(args);
		RunSqlScriptsFromFolder.processFolder(runSqlScriptFromFolderProperties);
	}

	private static Properties processParameters(final String[] args) {
		Properties runSqlScriptFromFolderProperties = new Properties();
		runSqlScriptFromFolderProperties.put(DBFileUtils.DB_PROPERTIES_FILE_PATH, args[0]);
		runSqlScriptFromFolderProperties.put(DBFileUtils.SQL_SCRIPT_FILE_PATH, args[1]);
		runSqlScriptFromFolderProperties.put(DBFileUtils.CHARSET_NAME, args[2]);
		runSqlScriptFromFolderProperties.put(RunSqlScriptsFromFolder.SMTP_EMAIL_PROPERTIES_FILE_PATH, args[3]);
		runSqlScriptFromFolderProperties.put(ParametersUtils.LOGS_FOLDER, args[4]);
		return runSqlScriptFromFolderProperties;
	}

}
