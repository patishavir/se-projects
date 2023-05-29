package oz.utils.emergencyreports;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.db.DBFileUtils;
import oz.infra.db.DBUtils;
import oz.infra.db.resultset.ResultSetUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.gc.GcUtils;
import oz.utils.emergencyreports.branch.BranchProcessor;

public class EmergencyReports implements Runnable {
	private static String charSetName = null;
	private static String datedSqlStatementString = null;
	private static String[] branchesArray = { "185", "186", "187" };
	private static List<Thread> threadList = new ArrayList<Thread>();
	private static int branchTablePtr = Integer.MIN_VALUE;
	private static int branches2Process = Integer.MIN_VALUE;
	private static int branchesCompleted = 0;
	private static StopWatch stopWatch = null;
	private static boolean debug = true;
	private static Logger logger = JulUtils.getLogger();

	private static void generateEmergencyReports() {
		String sqlScriptFilePath = EmergencyReportsParameters.getSqlScriptFilePath();
		charSetName = EmergencyReportsParameters.getCharsetName();
		String initailSqlStatementString = DBFileUtils.readSqlStatementsFromFile(sqlScriptFilePath, charSetName);
		logger.info("\ninitailSqlStatementString:\n" + initailSqlStatementString);
		String reportDate = DateTimeUtils.formatCurrentTime(DateTimeUtils.DATE_FORMAT_dd_MMM_yy);
		reportDate = "09-jun-13";
		branchesArray = populateBranchTable();
		datedSqlStatementString = StringUtils.substitute(initailSqlStatementString, "%reportDate%", reportDate);
		logger.info("\ndatedSqlStatementString:\n" + datedSqlStatementString);
		branches2Process = EmergencyReportsParameters.getBranches2Process();
		if (branches2Process <= 0 || branches2Process > branchesArray.length) {
			branches2Process = branchesArray.length;
		}
		int concurrentThreads = EmergencyReportsParameters.getConcurrentThreads();
		stopWatch = new StopWatch();
		for (branchTablePtr = 0; branchTablePtr < concurrentThreads && branchTablePtr < branches2Process; branchTablePtr++) {
			String branch = branchesArray[branchTablePtr];
			startBranchProcessing(branch);
		}
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		ArrayUtils.printArray(args, "\n", "args array:\n");
		EmergencyReportsParameters.processInputParameters(args);
		generateEmergencyReports();
	}

	private static String[] populateBranchTable() {
		if (debug) {
			// String[] branchesDebugArray = { "185", "186", "187", "188", "189"
			// };
			String[] branchesDebugArray = { "185", "186" };
			branchesArray = branchesDebugArray;
			return branchesArray;
		}
		String branchListSqlScriptFilePath = EmergencyReportsParameters.getBranchListSqlScriptFilePath();
		EncryptionMethodEnum encryptionMethod = EncryptionMethodEnum.valueOf(EmergencyReportsParameters
				.getDecriptPassword());
		Connection connection = DBUtils.getConnection(EmergencyReportsParameters.getDbPropertiesFilePath());
		String branchListSqlStatementString = DBFileUtils.readSqlStatementsFromFile(branchListSqlScriptFilePath,
				charSetName);
		String[] branchesArray = ResultSetUtils.getResultSetAs1DimArray(connection, branchListSqlStatementString);
		ArrayUtils.printArray(branchesArray);
		logger.info(String.valueOf(branchesArray.length) + " branches found in branch table");
		return branchesArray;
	}

	public static synchronized void processBranchProcessingCompletion(final String branch) {
		branchesCompleted++;
		String completionMessage = StringUtils.concat("\nEmergency report processing has completed for branch ",
				branch, "\nProcessing has completed for ", String.valueOf(branchesCompleted), " branches");
		logger.info(completionMessage);
		if (branchTablePtr < branches2Process) {
			startBranchProcessing(branchesArray[branchTablePtr]);
			branchTablePtr++;
		} else {
			if (branchesCompleted == branches2Process) {
				Thread thread = new Thread(new EmergencyReports());
				thread.start();
			}
		}
	}

	private static void startBranchProcessing(final String branch) {
		String acutalSqlStatementString = StringUtils.substitute(datedSqlStatementString, "%snif%", branch);
		logger.finest("\nacutalSqlStatementString:\n" + acutalSqlStatementString);
		BranchProcessor branchProcessor = new BranchProcessor(branch, acutalSqlStatementString);
		String sheetNamePrefix = EmergencyReportsParameters.getSheetNamePrefix();
		String threadName = sheetNamePrefix.concat(" ").concat(branch);
		Thread thread = new Thread(branchProcessor, threadName);
		thread.start();
		threadList.add(thread);
		logger.info("Thread " + threadName + " has started");
	}

	@Override
	public void run() {
		// process Reports Genetation Completion
		GcUtils.printGCStats();
		File reportsFolder = new File(EmergencyReportsParameters.getReportsFolder());
		int numberOfReports = reportsFolder.listFiles().length;
		String stopwatchMessage = StringUtils.concat("\n", String.valueOf(branchTablePtr),
				" branches started processing. \n", String.valueOf(BranchProcessor.getConnectionFailures()),
				" connection attempts failed. \n", String.valueOf(BranchProcessor.getNullResultSet()),
				" result sets were null. \n", String.valueOf(threadList.size()), " threads started.\n",
				String.valueOf(BranchProcessor.getProcessingCompleted()), " branches had to be processed.\n",
				String.valueOf(numberOfReports), " reports generated.\n",
				String.valueOf(BranchProcessor.getProcessingCompleted()), " branches completed processing in");
		logger.info(stopWatch.appendElapsedTimeToMessage(stopwatchMessage));
	}
}
