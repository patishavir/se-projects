package oz.utils.cm.ds.sql.run;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.db.oracle.OracleUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.io.filefilter.FileFilterIsFileAndRegExpression;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.MapUtils;
import oz.infra.print.PrintUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.utils.cm.ds.DsCmRunParameters;
import oz.utils.cm.ds.common.DsCmEmailUtils;
import oz.utils.cm.ds.common.DsCmUtils;
import oz.utils.cm.ds.common.DsCmZipUtils;
import oz.utils.cm.ds.common.cc.DsCmCCParameters;
import oz.utils.cm.ds.sql.DsCmSqlParameters;

public class DsCmSqlRun {
	private static final Map<String, String> database2UserMapping = new HashMap<String, String>();
	private static final String encryptionMethod = DsCmSqlParameters.getEncryptionMethod();
	private static int scriptsProcessed = 0;
	private static int schemaProcessed = 0;
	private static Map<String, String> emailMap = new HashMap<>();
	private static final String SQL_SCRIPT_SUFFIX = "commit;\r\nexit;";
	private static final Logger logger = JulUtils.getLogger();

	static {
		MapUtils.populateMapFromPropertiesFile(DsCmSqlParameters.getDatabase2UserMap(), database2UserMapping);
	}

	private static String getLogFilePath(final File sqlScriptFile) {
		String sqlScriptName = sqlScriptFile.getName();
		String logFolderPath = PathUtils.getFullPath(sqlScriptFile.getParent(), DsCmSqlParameters.getLogsFolder());
		FolderUtils.createFolderIfNotExists(logFolderPath);
		String logFilePath = PathUtils.getFullPath(logFolderPath,
				StringUtils.concat(sqlScriptName, OzConstants.UNDERSCORE, DateTimeUtils.getTimeStamp(), OzConstants.DOT, "log"));
		return logFilePath;
	}

	private static String modifySqlScript(final File sqlScriptFile, final String spoolLogFilePath) {
		String scriptContents = FileUtils.readTextFile(sqlScriptFile);
		String spoolLine = "spool ".concat(spoolLogFilePath);
		spoolLine = "";
		String modifiedScriptContents = StringUtils.concat(DsCmSqlParameters.getSqlPrefix(), SystemUtils.LINE_SEPARATOR, spoolLine,
				SystemUtils.LINE_SEPARATOR, scriptContents);
		modifiedScriptContents = StringUtils.concat(modifiedScriptContents, SystemUtils.LINE_SEPARATOR, SQL_SCRIPT_SUFFIX);
		String sqlScriptAbsolutePath = sqlScriptFile.getAbsolutePath();
		String modifiedFilePath = sqlScriptAbsolutePath.concat(".run");
		logger.finest("modified File Path: " + modifiedFilePath);
		logger.finest("modified File contents:\n" + modifiedScriptContents);
		FileUtils.writeFile(modifiedFilePath, modifiedScriptContents);
		return modifiedFilePath;
	}

	private static void processCompletion(final StopWatch stopWatch) {
		String sourceFolderPath = DsCmSqlParameters.getSourceFolderPath();
		DsCmZipUtils.zipContents(new File(sourceFolderPath));
		String stopwatchMessage = StringUtils.concat(String.valueOf(scriptsProcessed), " scripts, ", String.valueOf(schemaProcessed),
				" schemas processed. Elapsed time:");
		if (schemaProcessed > 0) {
			DsCmCCParameters.setAnything2Import(true);
		}
		logger.info(StringUtils.concat(stopWatch.appendElapsedTimeToMessage(stopwatchMessage), OzConstants.LINE_FEED,
				StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_150)));
	}

	private static void processDbFolder(final File folder) {
		String folderPath = folder.getAbsolutePath();
		logger.info("folderName: " + folderPath);
		FileFilter fileFiler = new FileFilterIsFileAndRegExpression(RegexpUtils.REGEXP_SQL_FILE, true);
		File[] sqlScriptfiles = FolderUtils.getFilesInFolder(folderPath, fileFiler, Level.INFO);
		if (sqlScriptfiles.length > 0) {
			logger.info(PrintUtils.getSeparatorLine("Starting sql files loop"));
			String doneFolderPath = DsCmUtils.makeDoneFolder(folderPath);
			for (File sqlScriptfile : sqlScriptfiles) {
				String[] sqlScriptfileBreakDown = sqlScriptfile.getName().split(DsCmRunParameters.getFileNameBreakdownDelimiter());
				String tnsAlias = sqlScriptfileBreakDown[0].toUpperCase();
				String userNamePasswordString = database2UserMapping.get(tnsAlias);
				if (userNamePasswordString == null) {
					logger.info(StringUtils.concat());
					SystemUtils.printMessageAndExit(" no username password found for ".concat(tnsAlias), OzConstants.EXIT_STATUS_ABNORMAL, true);
				} else {
					String[] userNamePassword = database2UserMapping.get(tnsAlias).split(OzConstants.COMMA);
					String username = userNamePassword[0];
					String password = userNamePassword[1];
					password = CryptographyUtils.decryptString(password, EncryptionMethodEnum.valueOf(encryptionMethod));
					ArrayUtils.printArray(sqlScriptfiles, Level.INFO);
					File movedScriptFile = DsCmUtils.move2DoneFolder(sqlScriptfile, doneFolderPath);
					processSqlSript(username, password, tnsAlias, movedScriptFile);
					scriptsProcessed++;
					schemaProcessed++;
					logger.info(PrintUtils.getSeparatorLine(sqlScriptfile.getName() + "processing has completed"));
				}
			}
		}
	}

	private static void processSqlSript(final String username, final String password, final String tnsAlias, final File sqlScriptFile) {
		String logFilePath = getLogFilePath(sqlScriptFile);
		String modifiedFilePath = modifySqlScript(sqlScriptFile, logFilePath.concat(".spool"));
		SystemCommandExecutorResponse scer = OracleUtils.runSqlPlus(DsCmSqlParameters.getSqlPlusPath(), username, password, tnsAlias,
				modifiedFilePath);
		String sqlRunOutcome = scer.getExecutorResponse();
		logger.info(sqlRunOutcome);
		FileUtils.writeFile(logFilePath, sqlRunOutcome);
		sendEmail(sqlScriptFile, sqlRunOutcome);
	}

	public static void run() {
		StopWatch stopWatch = new StopWatch();
		String sourceFolderPath = DsCmSqlParameters.getSourceFolderPath();
		processDbFolder(new File(sourceFolderPath));
		// DsCmEmailUtils.sendEmails(emailMap, "sql run results for ",
		// DsCmSqlParameters.getAdditionalMailRecepients());
		processCompletion(stopWatch);
	}

	private static void sendEmail(final File sqlScriptFile, final String sqlRunOutcome) {
		String recepient = DsCmEmailUtils.getMailRecepientFromFilePath(sqlScriptFile.getName());
		String subject = StringUtils.concat(sqlScriptFile.getName(), "  run results for ");
		DsCmEmailUtils.sendEmail1(sqlRunOutcome, subject, recepient, DsCmSqlParameters.getAdditionalMailRecepients());
	}
}