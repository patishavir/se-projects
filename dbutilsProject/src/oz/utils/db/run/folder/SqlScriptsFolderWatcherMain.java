package oz.utils.db.run.folder;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.db.DBFileUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.watcher.WatchDirService;
import oz.infra.nio.watcher.WatchDirServiceParameters;
import oz.infra.operaion.Outcome;
import oz.infra.parameters.ParametersUtils;
import oz.infra.process.FileProcessor;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.thread.ThreadUtils;

public class SqlScriptsFolderWatcherMain implements FileProcessor {
	private static String dbPropertiesFolder = null;
	private static String folder2Watch = null;
	private static String charsetName = null;
	private static String logsFolder = null;
	private static String historyFolder = null;
	private static String smtpEmailProprtiesFilePath = null;
	private static SqlScriptsFolderWatcherMain sqlScriptsFolderWatcherMain = new SqlScriptsFolderWatcherMain();
	private static Properties currentProperties = null;
	private static String rootFolderPath = null;
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		logger.info(SystemUtils.getRunInfo());
		String propertiesFilePath = args[0];
		rootFolderPath = PathUtils.getParentFolderPath(propertiesFilePath);
		currentProperties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		ParametersUtils.processPatameters(propertiesFilePath, SqlScriptsFolderWatcherMain.class);
		startWatching();
	}

	public static void setCharsetName(final String charsetName) {
		SqlScriptsFolderWatcherMain.charsetName = charsetName;
	}

	public static void setDbPropertiesFolder(final String dbPropertiesFolder) {
		SqlScriptsFolderWatcherMain.dbPropertiesFolder = PathUtils.getFullPath(rootFolderPath, dbPropertiesFolder);
		logger.info(StringUtils.concat("dbPropertiesFolder parameter: ", dbPropertiesFolder, "   dbPropertiesFolder: ",
				SqlScriptsFolderWatcherMain.dbPropertiesFolder));
		FolderUtils.dieUnlessFolderExists(SqlScriptsFolderWatcherMain.dbPropertiesFolder);
	}

	public static void setFolder2Watch(final String folder2Watch) {
		SqlScriptsFolderWatcherMain.folder2Watch = folder2Watch;
	}

	public static void setHistoryFolder(final String historyFolder) {
		SqlScriptsFolderWatcherMain.historyFolder = historyFolder;
	}

	public static void setLogsFolder(final String logsFolder) {
		SqlScriptsFolderWatcherMain.logsFolder = logsFolder;
	}

	public static void setRootFolderPath(final String rootFolderPath) {
		SqlScriptsFolderWatcherMain.rootFolderPath = rootFolderPath;
	}

	public static void setSmtpEmailProprtiesFilePath(final String smtpEmailProprtiesFilePath) {
		SqlScriptsFolderWatcherMain.smtpEmailProprtiesFilePath = smtpEmailProprtiesFilePath;
	}

	private static void startWatching() {
		logger.info(StringUtils.concat("\n", OzConstants.ASTERISKS_20, " starting up ... "));
		WatchDirServiceParameters watchDirServiceParameters = new WatchDirServiceParameters(folder2Watch);
		new WatchDirService(watchDirServiceParameters, sqlScriptsFolderWatcherMain);

	}

	public final Outcome processFile(final String folderFullPath) {
		Outcome outcome = Outcome.SUCCESS;
		logger.finest("start processing " + folderFullPath + " ...");
		ThreadUtils.sleep(OzConstants.INT_200, Level.INFO);
		String database = PathUtils.getParentFolderName(folderFullPath);
		File folderFile = new File(folderFullPath);
		if (folderFile.isDirectory()) {
			// Let file copy complete
			ThreadUtils.sleep(2000, Level.INFO);
			String dbpropertiesFilePath = PathUtils.getFullPath(dbPropertiesFolder, database + OzConstants.PROPERTIES_SUFFIX);
			if (!FileUtils.isFileExists(dbpropertiesFilePath)) {
				SystemUtils.printMessageAndExit(dbpropertiesFilePath + " does not exist. Bye bye ...", OzConstants.EXIT_STATUS_ABNORMAL, false);
			}
			currentProperties.setProperty(DBFileUtils.DB_PROPERTIES_FILE_PATH, dbpropertiesFilePath);
			// move scripts folder
			String relativePath = PathUtils.getRelativePath(folderFullPath, folder2Watch, Level.FINEST);
			String tagetFolderPath = PathUtils.getFullPath(historyFolder, relativePath);
			File tagetFolderFile = new File(tagetFolderPath + File.separator + "..");
			if (!tagetFolderFile.isDirectory()) {
				boolean mkdirsRc = tagetFolderFile.mkdirs();
				logger.info("mkdirs " + tagetFolderFile + " . rc =" + String.valueOf(mkdirsRc));
			}
			tagetFolderFile = new File(tagetFolderPath + File.separator + database + OzConstants.UNDERSCORE + DateTimeUtils.getTimeStamp());
			File folderFullPathFile = new File(folderFullPath);
			String targetFolderFullPath = tagetFolderPath + OzConstants.UNDERSCORE + DateTimeUtils.getTimeStamp() + OzConstants.UNDERSCORE + database;
			boolean renameToRc = folderFullPathFile.renameTo(new File(targetFolderFullPath));
			if (!renameToRc) {
				logger.warning("failed to move " + folderFullPath + " to " + tagetFolderPath);
				outcome = Outcome.FAILURE;
			} else {
				currentProperties.setProperty(DBFileUtils.SQL_SCRIPT_FILE_PATH, targetFolderFullPath);
				RunSqlScriptsFromFolder.processFolder(currentProperties);
			}
		} else {
			logger.warning(folderFile.getAbsolutePath() + "  is not a directory and has not been processed.");
			outcome = Outcome.FAILURE;
		}
		return outcome;
	}
}
