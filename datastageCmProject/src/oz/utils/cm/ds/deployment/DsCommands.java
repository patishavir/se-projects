package oz.utils.cm.ds.deployment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.collection.CollectionUtils;
import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.io.FileDirectoryEnum;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.MapUtils;
import oz.infra.operaion.Outcome;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.string.StringUtils;
import oz.utils.cm.ds.DsCmRunParameters;
import oz.utils.cm.ds.common.DsCmEmailUtils;
import oz.utils.cm.ds.deployment.stats.DsDeploymentStats;

public class DsCommands {
	enum DsCommandEnum {
		DSCMDIMPORT, DSCC
	}

	private static String dsCmdImportPath = null;
	private static String dsccPath = null;
	private static String dsHost = null;
	private static String dsDomain = null;
	private String dsProjectName = null;
	private String username = null;
	private String password = null;
	private String dsxFolderPath = null;
	private String dsxFilename = null;
	private String dsxFilePath = null;
	private File dsxFile = null;
	private static Map<String, String> project2UserMapping = new HashMap<String, String>();
	private static String logFolderPath = null;
	private static DsDeploymentStats importStats = new DsDeploymentStats("imports");
	private static DsDeploymentStats compileStats = new DsDeploymentStats("compiles");
	private static Map<String, String> emailMap = new HashMap<String, String>();
	private static final String LOGS_FOLDER_NAME = "logs";

	private static Logger logger = JulUtils.getLogger();
	static {
		MapUtils.populateMapFromPropertiesFile(DsCmParameters.getProject2UserMap(), project2UserMapping);
	}

	private static Outcome checkOutome(final SystemCommandExecutorResponse scer, final DsCommandEnum dsCommand) {
		Outcome dsCommandOutcome = Outcome.FAILURE;
		if (scer.getReturnCode() == 0) {
			switch (dsCommand) {
			case DSCMDIMPORT:
				final String importStarted = "DSImport started at:";
				final String importEnded = "DSImport ended at:";
				if (((scer.getStdout().indexOf(importStarted)) > OzConstants.STRING_NOT_FOUND)
						&& (scer.getStdout().indexOf(importEnded) > OzConstants.STRING_NOT_FOUND)) {
					dsCommandOutcome = Outcome.SUCCESS;
				}
				break;
			case DSCC:
				final String ccFailed = "Failed";
				final String ccSucceeded = "Succeeded";
				if (((scer.getStdout().indexOf(ccFailed)) == OzConstants.STRING_NOT_FOUND)
						&& (scer.getStdout().indexOf(ccSucceeded) > OzConstants.STRING_NOT_FOUND)) {
					dsCommandOutcome = Outcome.SUCCESS;
					break;
				}
			}

		}
		return dsCommandOutcome;
	}

	public static DsDeploymentStats getCompileStats() {
		return compileStats;
	}

	public static Map<String, String> getEmailMap() {
		return emailMap;
	}

	public static DsDeploymentStats getImportStats() {
		return importStats;
	}

	private static String getLogFilePath(final String dsxFolderPath, final String dsxFilename) {
		logFolderPath = PathUtils.getFullPath(dsxFolderPath, ".." + File.separator + LOGS_FOLDER_NAME);
		File logFolderFile = new File(logFolderPath);
		if (!logFolderFile.exists()) {
			logFolderFile.mkdir();
		}
		String logFileName = dsxFilename.substring(0, dsxFilename.lastIndexOf(OzConstants.DOT));
		logFileName = StringUtils.concat(logFileName + OzConstants.UNDERSCORE + DateTimeUtils.getTimeStamp() + ".log");
		String logFilePath = PathUtils.getFullPath(logFolderPath, logFileName);
		logger.finest("logFilePath: " + logFilePath);
		return logFilePath;
	}

	public static String getLogFolderPath() {
		return logFolderPath;
	}

	public static void setDsccPath(final String dsccPath) {
		DsCommands.dsccPath = FileUtils.getExisting(dsccPath.split(OzConstants.COMMA), FileDirectoryEnum.FILE);
	}

	public static void setDsCmdImportPath(final String dsCmdImportPath) {
		DsCommands.dsCmdImportPath = FileUtils.getExisting(dsCmdImportPath.split(OzConstants.COMMA), FileDirectoryEnum.FILE);
	}

	public static void setDsDomain(final String dsDomain) {
		DsCommands.dsDomain = dsDomain;
	}

	public static void setDsHost(final String dsHost) {
		DsCommands.dsHost = dsHost;
	}

	DsCommands(final File dsxFile) {
		this.dsxFile = dsxFile;
		dsxFolderPath = dsxFile.getParent();
		dsxFilename = dsxFile.getName();
		dsProjectName = dsxFilename.split(DsCmRunParameters.getFileNameBreakdownDelimiter())[0];
		dsxFilePath = dsxFile.getAbsolutePath();
		String dsUserPassword = project2UserMapping.get(dsProjectName);
		if (dsUserPassword != null) {
			String[] dsUserPasswordArray = dsUserPassword.split(OzConstants.COMMA);
			username = dsUserPasswordArray[0];
			password = CryptographyUtils.decryptString(dsUserPasswordArray[1]);
		} else {
			logger.warning("\nNo user / password found for project " + dsProjectName + "\nImport has not been performed");
		}
	}

	/*
	 * 
	 * ? Show program usage /bo (value) BuildOp to compile, * = All BuildOps, \folder\* = folder /d (value) ASB Domain Name /f Use the Force Compile /h (value) Name
	 * of Host to attach to /j (value) Job to compile, * = All Jobs, \folder\* = folder /jt (value) Job Type To Compile -1 (Default) = All Types 0 = Server 1 =
	 * Mainframe 2 = Sequence 3 = Parallel /mfcgb (value) Mainframe Code Gen base directory location /mful (value) Mainframe Job Upload Profile /ouc Only Compile
	 * Uncompiled Objects /p (value) Password project Project To Attach to /qs (value) QS objects to provision * = All QS objects, \folder\* = folder /qspa
	 * Provision all dependant QS objects /r (value) Routine to compile, * = All Routines, \folder\* = folder /rcf Use this flag to get -1 returned if anything
	 * fails to compile /rd (value) Report Directory and Name /rt (value) Type of report to produce : X = xml, T = txt - default /u (value) User Name
	 */

	private Outcome runCommand(final List<String> dsCommandParameters, final String command, final DsCommandEnum dsCommand) {
		StopWatch stopWatch = new StopWatch();
		logger.info("start " + dsCommand.toString() + " of " + dsxFilePath);
		CollectionUtils.printCollection(dsCommandParameters, Level.FINEST, OzConstants.BLANK);
		SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(dsCommandParameters);
		String dsImportOutcome = scer.getExecutorResponse();
		logger.info(dsImportOutcome);
		String logFilePath = getLogFilePath(dsxFolderPath, command + OzConstants.UNDERSCORE + dsxFilename);
		FileUtils.writeFile(logFilePath, dsImportOutcome);
		stopWatch.logElapsedTimeMessage(command + " of " + dsxFilePath + " has completed in ");
		DsCmEmailUtils.processEmail(dsxFilePath, dsImportOutcome, emailMap);
		return checkOutome(scer, dsCommand);
	}

	public final void runDscc() {
		ArrayList<String> jobNames = DsUtils.getJobNames(dsxFile);
		for (String jobName : jobNames) {
			List<String> dsParameters = new ArrayList<String>();
			dsParameters.add(dsccPath);
			dsParameters.add("/H");
			dsParameters.add(dsHost);
			dsParameters.add("/D");
			dsParameters.add(dsDomain);
			dsParameters.add("/U");
			dsParameters.add(username);
			dsParameters.add("/P");
			dsParameters.add(password);
			dsParameters.add("/J");
			dsParameters.add(jobName);
			dsParameters.add(dsProjectName);
			compileStats.updateRunStats(runCommand(dsParameters, "dscc_" + jobName, DsCommandEnum.DSCC));
		}

	}

	public final void runDsCmdImport() {
		List<String> dsImportParameters = new ArrayList<String>();
		dsImportParameters.add(dsCmdImportPath);
		dsImportParameters.add("/H=" + dsHost);
		dsImportParameters.add("/D=" + dsDomain);
		dsImportParameters.add("/U=" + username);
		dsImportParameters.add("/P=" + password);
		dsImportParameters.add(dsProjectName);
		dsImportParameters.add(dsxFilePath);
		dsImportParameters.add("/NUA");
		importStats.updateRunStats(runCommand(dsImportParameters, "dsCmdImport", DsCommandEnum.DSCMDIMPORT));
	}
}