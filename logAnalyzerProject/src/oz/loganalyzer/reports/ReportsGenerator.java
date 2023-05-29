package oz.loganalyzer.reports;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.excel.utils.ExcelUtils;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.filter.AcceptRejectAnySubStringFilter;
import oz.infra.filter.AcceptRejectRegExpFilter;
import oz.infra.filter.StringFilter;
import oz.infra.filter.StringWithinRangeInclusiveFilter;
import oz.infra.http.HTTPUtils;
import oz.infra.httpserver.HttpServerUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.LineProcessor;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.runtime.RunTimeUtils;
import oz.infra.system.SystemUtils;
import oz.loganalyzer.LogAnalyzerParameters;
import oz.loganalyzer.common.LogAnalyzerUtils;
import oz.loganalyzer.data.LogAnalyzerFileNameFilter;
import oz.loganalyzer.data.LogRecord;

public class ReportsGenerator implements LineProcessor, StringFilter {
	private static int recordsProcessed = 0;
	private static int recordsRejected = 0;
	private static ReportsGenerator reportsGenerator = new ReportsGenerator();
	private static LineProcessor[] lineProcessorsArray = { reportsGenerator };
	private static List<AbstractLogAnalyzerReport> abstractLogAnalyzerReportList = new ArrayList<AbstractLogAnalyzerReport>();
	private static LogAnalyzerFileNameFilter logAnalyzerFileNameFilter = new LogAnalyzerFileNameFilter();

	private static AcceptRejectRegExpFilter acceptRejectRegExpFilter = LogAnalyzerParameters
			.getAcceptRejectRegExpFilter();
	private static AcceptRejectAnySubStringFilter uriFilter = new AcceptRejectAnySubStringFilter(
			LogAnalyzerParameters.getUriFilter(), LogAnalyzerParameters.getUriFilterOperator());
	private static StopWatch stopWatch;
	private static final Logger logger = JulUtils.getLogger();
	private static FileFilter fileFilter = new FileFilter() {
		public boolean accept(final File file) {
			boolean returnCode = false;
			if (file.isFile()) {
				String fileName = PathUtils.getNameWithoutExtension(file.getAbsolutePath());
				returnCode = logAnalyzerFileNameFilter.accept(fileName);
			}
			return returnCode;
		}
	};

	public static void generateReports() {
		String[] reports2GenerateArray = LogAnalyzerParameters.getReports2GenerateArray();
		for (String report2Generate : reports2GenerateArray) {
			abstractLogAnalyzerReportList.add(LogAnalyzerReportsEnum.valueOf(report2Generate).getReportObject());
		}
		logger.info("Starting ...");
		stopWatch = new StopWatch();
		String[] logFilePathArray = LogAnalyzerParameters.getLogFilePathArray();
		int filesProcessed = 0;
		if (logFilePathArray[0].toLowerCase().startsWith("http://")) {
			filesProcessed = processHttpServerFiles(logFilePathArray);
		} else {
			filesProcessed = processFileSystemFiles(logFilePathArray);
		}

		AcceptRejectRegExpFilter acceptRejectRegExpFilter = LogAnalyzerParameters.getAcceptRejectRegExpFilter();
		int regExpFilterRecordsRejected = 0;
		if (acceptRejectRegExpFilter != null) {
			regExpFilterRecordsRejected = acceptRejectRegExpFilter.getRejectedStringsCounter();
		}
		logger.info(String.valueOf(recordsProcessed) + " records from " + String.valueOf(filesProcessed)
				+ " files have been processed. " + String.valueOf(recordsRejected) + " have been filtered out."
				+ " regExp filter has rejected " + String.valueOf(regExpFilterRecordsRejected));
		if (filesProcessed > 0 && recordsProcessed > 0) {
			String[] reportsArray = new String[reports2GenerateArray.length];
			String[] sheetNamesArray = new String[reports2GenerateArray.length];
			System.arraycopy(reports2GenerateArray, 0, sheetNamesArray, 0, sheetNamesArray.length);
			for (int i = 0; i < abstractLogAnalyzerReportList.size(); i++) {
				logger.info("starting ".concat(sheetNamesArray[i]));
				String report = abstractLogAnalyzerReportList.get(i).generateReport().toString();
				reportsArray[i] = report;
				logger.finest(report);
				int indexOfReport = sheetNamesArray[i].lastIndexOf("Report");
				if (indexOfReport > OzConstants.STRING_NOT_FOUND) {
					sheetNamesArray[i] = sheetNamesArray[i].substring(0, indexOfReport);
				}
				int indexOfSummary = sheetNamesArray[i].lastIndexOf("Summary");
				if (indexOfSummary > OzConstants.STRING_NOT_FOUND) {
					sheetNamesArray[i] = sheetNamesArray[i].substring(0, indexOfSummary);
				}
			}
			stopWatch.logElapsedTimeMessage("Reports generation has completed in");
			processGeneratedReports(reportsArray, sheetNamesArray);
		}
	}

	private static String getReportFilePath() {
		String reportFileName = LogAnalyzerParameters.getReportFileName();
		if (reportFileName == null || reportFileName.length() == 0) {
			reportFileName = "SnifitReport_" + DateTimeUtils.formatDate(DateTimeUtils.DATE_FORMAT_yyyyMMdd_HHmmss);
		}
		String reportFilePath = LogAnalyzerParameters.getReportsFolder() + reportFileName + OzConstants.XLS_SUFFIX;
		return reportFilePath;
	}

	private static int processFileSystemFiles(final String[] logFilePathArray) {
		int fileProcessed = 0;
		for (int i = 0; i < logFilePathArray.length; i++) {
			File logFile = new File(logFilePathArray[i]);
			if (!logFile.exists()) {
				String exitMessage = SystemUtils.LINE_SEPARATOR + logFilePathArray[i] + " does not exists!"
						+ SystemUtils.LINE_SEPARATOR + "Processing has been terminated.";
				SystemUtils.printMessageAndExit(exitMessage, OzConstants.EXIT_STATUS_ABNORMAL);
			}
			File[] logFilesArray = null;
			if (logFile.isFile()) {
				File[] logFiles = { logFile };
				logFilesArray = logFiles;
			}
			if (logFile.isDirectory()) {
				File[] logFiles = logFile.listFiles(fileFilter);
				logFilesArray = logFiles;
			}
			for (File logFile1 : logFilesArray) {
				if (reportsGenerator.accept(logFile1.getName())) {
					logger.finest("Starting to process " + logFile1.getAbsolutePath() + " ...");
					String logRecords = FileUtils.readTextFile(logFile1);
					logger.info("Processing file: " + logFile1.getAbsolutePath() + " length: "
							+ String.valueOf(logFile1.length()));
					processlogRecords(logRecords);
					fileProcessed++;
				}
			}
		}
		return fileProcessed;
	}

	private static void processGeneratedReports(final String[] reportsArray, final String[] sheetNamesArray) {
		String excelFilePath = getReportFilePath();
		if (LogAnalyzerParameters.isGenerateExcelWorkBook()) {
			ExcelWorkbook.writeWorkbook(excelFilePath, reportsArray, sheetNamesArray);
			LogAnalyzerUtils.emailReport(excelFilePath);
		}
		if (LogAnalyzerParameters.isSaveCsvFiles()) {
			logger.info("Saving csv files ... ");
			for (int i = 0; i < reportsArray.length; i++) {
				String outFilePath = LogAnalyzerParameters.getCsvFilesFolder() + sheetNamesArray[i]
						+ OzConstants.CSV_SUFFIX;
				FileUtils.writeFile(outFilePath, reportsArray[i]);
			}
		}
		logger.info(stopWatch.appendElapsedTimeToMessage("Processing has completed in"));
		if (LogAnalyzerParameters.isOpenExcelWorkBook()) {
			ExcelUtils.openExcelWorkbook(excelFilePath);
		}
		if (LogAnalyzerParameters.isGenerateSwingOutput()) {
			LogAnalyzerUtils.generateSwingOutput(reportsArray, sheetNamesArray);
		}
		logger.info("generated reports processing has completed ...");
	}

	private static int processHttpServerFiles(final String[] logFilePathArray) {
		int filesProcessed = 0;
		for (int i = 0; i < logFilePathArray.length; i++) {
			ArrayList<String> fileUrlsArrayList = HttpServerUtils.getFileUrlsList(logFilePathArray[i], reportsGenerator,
					logAnalyzerFileNameFilter);
			for (int filePtr = 0; filePtr < fileUrlsArrayList.size(); filePtr++) {
				StopWatch stopWatch = new StopWatch();
				String url = fileUrlsArrayList.get(filePtr);
				logger.info("Processing url: " + url);
				HTTPUtils.processResponseRecords(url, null, lineProcessorsArray);
				filesProcessed++;
				stopWatch.logElapsedTimeMessage(url + " has been processed in");
			}
		}
		return filesProcessed;
	}

	private static void processlogRecords(final String logRecords) {
		StopWatch stopWatch = new StopWatch();
		RunTimeUtils.gc(Level.INFO);
		String[] linesArray = logRecords.split(OzConstants.LINE_FEED);
		logger.info("Processing " + String.valueOf(linesArray.length) + " logRecords. length: "
				+ String.valueOf(logRecords.length()));
		for (int i = 0; i < linesArray.length; i++) {
			reportsGenerator.processLine(linesArray[i]);
		}
		stopWatch.logElapsedTimeMessage("Processing has completed in");
	}

	private StringWithinRangeInclusiveFilter hhmmssRangeFilter = new StringWithinRangeInclusiveFilter(
			LogAnalyzerParameters.getHhmmssRangeArray());

	public boolean accept(final String logFileName) {
		boolean prefixOK = false;
		String[] logFileNamePrefixArray = LogAnalyzerParameters.getLogFileNamePrefixesArray();
		for (String prefix1 : logFileNamePrefixArray) {
			if (logFileName.startsWith(prefix1)) {
				prefixOK = true;
				break;
			}
		}
		return prefixOK;
	}

	public void processEOF(final Object object) {
		logger.info(((Integer) (object)).toString() + " records have been processed");
	}

	public void processLine(final String line) {
		logger.finest(line);
		if (line != null && line.trim().length() > 0) {
			LogRecord logRecord = new LogRecord(line);
			String uri = logRecord.getUri();
			String logRecordTime = logRecord.getTime();
			logger.finest("uri: " + uri);
			logger.finest("logRecord: " + logRecord);
			boolean regExpFilterOK = acceptRejectRegExpFilter == null || acceptRejectRegExpFilter.accept(uri);
			boolean doProcessLine = logRecord.isValidRecord() && hhmmssRangeFilter.accept(logRecordTime)
					&& regExpFilterOK && uriFilter.accept(uri);
			if (doProcessLine) {

				for (AbstractLogAnalyzerReport lineProcessor1 : abstractLogAnalyzerReportList) {
					lineProcessor1.processLogRecord(logRecord);
				}
				recordsProcessed++;
			} else {
				recordsRejected++;
			}
		}
	}
}
