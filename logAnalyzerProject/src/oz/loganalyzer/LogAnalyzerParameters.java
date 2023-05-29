package oz.loganalyzer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.filter.AcceptReject;
import oz.infra.filter.AcceptRejectRegExpFilter;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;
import oz.infra.string.StringUtils.PaddingDirection;

public class LogAnalyzerParameters {
	private static String logFilePath = null;
	private static String[] logFilePathArray = null;
	private static String reportsFolder = null;
	private static String reportFileName = null;
	private static String logFileNamePrefixes = "access";
	private static String[] logFileNamePrefixesArray = null;
	private static String logfileNameDateFilterFormat = null;
	private static String logfileNameFilter = null;
	private static String fileNameTimeRange = null;
	private static String hhmmssRange = null;
	private static String[] hhmmssRangeArray = { "00:00:00", "24:00:00" };
	private static String[][] fileNameTimeRangeArray = null;
	private static int excessiveReponseTimeThreshold = 0;
	private static int excessiveResponseLengthThreshold = 0;
	private static int topUsersThreshold = 0;
	private static String uriFilter4ApplicationStartCount = null;
	private static int responseTimeBreakdownInMicros = 0;
	private static String reports2Generate = null;
	private static String[] reports2GenerateArray = null;
	private static boolean emailReport = false;
	private static String emailProprtiesFilePath = null;
	private static String serversPropertiesFilePath = null;
	private static Map<String, ServerNameEnvironment> serverNameEnvironmentMap = null;
	private static String ipPrefixFilter = null;
	private static String rootFolderPath = null;
	private static String uriFilter = null;
	private static final String URI_FILTER_DELIMITER = OzConstants.COLON;
	private static String uriFilterOperator = AcceptReject.ACCEPT.toString();
	private static String uriRegExpFilter = null;
	private static AcceptRejectRegExpFilter acceptRejectRegExpFilter = null;
	private static boolean generateExcelWorkBook = true;
	private static boolean openExcelWorkBook = false;
	private static boolean generateSwingOutput = false;
	private static boolean saveCsvFiles = false;
	private static String csvFilesFolder = null;
	private static String system = null;
	private static String branchReportAppPrefix = null;

	private static Logger logger = JulUtils.getLogger();

	public static String getBranchReportAppPrefix() {
		return branchReportAppPrefix;
	}

	public static String getCsvFilesFolder() {
		return csvFilesFolder;
	}

	public static String getEmailProprtiesFilePath() {
		return emailProprtiesFilePath;
	}

	public static final int getExcessiveReponseTimeThreshold() {
		return excessiveReponseTimeThreshold;
	}

	public static int getExcessiveResponseLengthThreshold() {
		return excessiveResponseLengthThreshold;
	}

	public static String[][] getFileNameTimeRangeArray() {
		return fileNameTimeRangeArray;
	}

	public static String[] getHhmmssRangeArray() {
		return hhmmssRangeArray;
	}

	public static String getIpPrefixFilter() {
		return ipPrefixFilter;
	}

	public static final String getLogfileNameFilter() {
		return logfileNameFilter;
	}

	public static String[] getLogFileNamePrefixesArray() {
		return logFileNamePrefixesArray;
	}

	public static String[] getLogFilePathArray() {
		return logFilePathArray;
	}

	public static String getReportFileName() {
		return reportFileName;
	}

	public static String[] getReports2GenerateArray() {
		return reports2GenerateArray;
	}

	public static final String getReportsFolder() {
		return reportsFolder;
	}

	public static final int getResponseTimeBreakdownInMicros() {
		return responseTimeBreakdownInMicros;
	}

	public static Map<String, ServerNameEnvironment> getServerNameEnvironmentMap() {
		return serverNameEnvironmentMap;
	}

	public static String getSystem() {
		return system;
	}

	public static int getTopUsersThreshold() {
		return topUsersThreshold;
	}

	public static String getUriFilter() {
		return uriFilter;
	}

	public static String getUriFilter4ApplicationStartCount() {
		return uriFilter4ApplicationStartCount;
	}

	public static String getUriFilterOperator() {
		return uriFilterOperator;
	}

	public static boolean isEmailReport() {
		return emailReport;
	}

	public static boolean isGenerateExcelWorkBook() {
		return generateExcelWorkBook;
	}

	public static boolean isGenerateSwingOutput() {
		return generateSwingOutput;
	}

	public static boolean isOpenExcelWorkBook() {
		return openExcelWorkBook;
	}

	public static boolean isSaveCsvFiles() {
		return saveCsvFiles;
	}

	public static void processParameters(final String logAnalyzerPropertiesFilePath) {
		logger.info(PathUtils.getAbsolutePath(logAnalyzerPropertiesFilePath));
		rootFolderPath = ParametersUtils.processPatameters(logAnalyzerPropertiesFilePath, LogAnalyzerParameters.class);
		if ((logfileNameDateFilterFormat != null && logfileNameDateFilterFormat.length() > 0)
				&& (logfileNameFilter == null || logfileNameFilter.length() == 0)) {
			LogAnalyzerParameters.logfileNameFilter = DateTimeUtils.formatDate(logfileNameDateFilterFormat);
		}
		logFileNamePrefixesArray = logFileNamePrefixes.split(OzConstants.COMMA);
	}

	public static void setBranchReportAppPrefix(String branchReportAppPrefix) {
		LogAnalyzerParameters.branchReportAppPrefix = branchReportAppPrefix;
	}

	public static void setCsvFilesFolder(final String csvFileFolderParam) {
		csvFilesFolder = PathUtils.getFullPath(rootFolderPath, csvFileFolderParam);
		FolderUtils.createFolderIfNotExists(csvFilesFolder);
		csvFilesFolder = PathUtils.appendTralingSeparatorIfNotExists(csvFilesFolder);
		logger.info("csvFileFolder: " + csvFilesFolder);
	}

	public static void setEmailProprtiesFilePath(final String emailProprtiesFilePath) {
		LogAnalyzerParameters.emailProprtiesFilePath = PathUtils.getFullPath(rootFolderPath, emailProprtiesFilePath);
	}

	public static void setEmailReport(final boolean emailReport) {
		LogAnalyzerParameters.emailReport = emailReport;
	}

	public static final void setExcessiveReponseTimeThreshold(final int excessiveReponseTimeThreshold) {
		LogAnalyzerParameters.excessiveReponseTimeThreshold = excessiveReponseTimeThreshold;
	}

	public static void setExcessiveResponseLengthThreshold(final int excessiveResponseLengthThreshold) {
		LogAnalyzerParameters.excessiveResponseLengthThreshold = excessiveResponseLengthThreshold;
	}

	public static void setFileNameTimeRange(final String fileNameTimeRange) {
		LogAnalyzerParameters.fileNameTimeRange = fileNameTimeRange;
		String[] timeRangeArray0 = fileNameTimeRange.split(OzConstants.COMMA);
		String[] timeRangePairArray = new String[2];
		fileNameTimeRangeArray = new String[timeRangeArray0.length][2];
		for (int i = 0; i < timeRangeArray0.length; i++) {
			timeRangePairArray = timeRangeArray0[i].split(OzConstants.UNDERSCORE);
			fileNameTimeRangeArray[i][0] = timeRangePairArray[0];
			if (timeRangePairArray.length > 1) {
				fileNameTimeRangeArray[i][1] = timeRangePairArray[1];
			} else {
				fileNameTimeRangeArray[i][1] = "23-59";
			}
			int timeRangeEntryLength = OzConstants.INT_5;
			if (fileNameTimeRangeArray[i][0].length() < timeRangeEntryLength) {
				fileNameTimeRangeArray[i][0] = StringUtils.pad(fileNameTimeRangeArray[i][0], '0', timeRangeEntryLength,
						PaddingDirection.LEFT);
			}
			if (fileNameTimeRangeArray[i][1].length() < timeRangeEntryLength) {
				fileNameTimeRangeArray[i][1] = StringUtils.pad(fileNameTimeRangeArray[i][1], '0', timeRangeEntryLength,
						PaddingDirection.LEFT);
			}
		}
	}

	public static void setGenerateExcelWorkBook(final boolean generateExcelWorkBook) {
		LogAnalyzerParameters.generateExcelWorkBook = generateExcelWorkBook;
	}

	public static void setGenerateSwingOutput(final boolean generateSwingOutput) {
		LogAnalyzerParameters.generateSwingOutput = generateSwingOutput;
		if (generateSwingOutput) {
			System.setProperty("java.awt.headless", "false");
		}
	}

	public static void setHhmmssRange(final String hhmmssRange) {
		LogAnalyzerParameters.hhmmssRange = hhmmssRange;
		if (LogAnalyzerParameters.hhmmssRange != null && LogAnalyzerParameters.hhmmssRange.length() > 0) {
			hhmmssRangeArray = LogAnalyzerParameters.hhmmssRange.split(OzConstants.UNDERSCORE);
			logger.finest("hhmmssRange: " + hhmmssRange);
		}
	}

	public static void setIpPrefixFilter(final String ipPrefixFilter) {
		logger.finest("ipPrefixFilter set to: " + ipPrefixFilter);
		LogAnalyzerParameters.ipPrefixFilter = ipPrefixFilter;
	}

	public static final void setLogfileNameDateFilterFormat(final String logfileNameDateFilterFormat) {
		LogAnalyzerParameters.logfileNameDateFilterFormat = logfileNameDateFilterFormat;
		logger.finest("logfileNameDateFilterFormat: " + LogAnalyzerParameters.logfileNameDateFilterFormat);
	}

	public static void setLogfileNameFilter(final String logfileNameFilter) {
		if (logfileNameFilter != null && logfileNameFilter.length() > 0) {
			LogAnalyzerParameters.logfileNameFilter = logfileNameFilter;
		}
	}

	public static void setLogFileNamePrefixes(String logFileNamePrefixes) {
		LogAnalyzerParameters.logFileNamePrefixes = logFileNamePrefixes;
	}

	public static void setLogFilePath(final String logFilePath) {
		LogAnalyzerParameters.logFilePath = logFilePath;
		logFilePathArray = logFilePath.split(OzConstants.COMMA);
	}

	public static void setOpenExcelWorkBook(final boolean openExcelWorkBook) {
		LogAnalyzerParameters.openExcelWorkBook = openExcelWorkBook;
	}

	public static void setReportFileName(final String reportFileName) {
		LogAnalyzerParameters.reportFileName = reportFileName;
	}

	public static void setReports2Generate(final String reports2Generate) {
		reports2GenerateArray = reports2Generate.split(OzConstants.COMMA);
	}

	public static final void setReportsFolder(final String reportsFolderParam) {
		reportsFolder = PathUtils.getFullPath(rootFolderPath, reportsFolderParam);
		FolderUtils.createFolderIfNotExists(reportsFolder);
		reportsFolder = PathUtils.appendTralingSeparatorIfNotExists(reportsFolder);
		logger.finest("reportsFolder: " + reportsFolder);
	}

	public static final void setResponseTimeBreakdownInMicros(final int responseTimeBreakdownInMicros) {
		LogAnalyzerParameters.responseTimeBreakdownInMicros = responseTimeBreakdownInMicros;
	}

	public static void setRootFolderPath(final String rootFolderPath) {
		LogAnalyzerParameters.rootFolderPath = rootFolderPath;
	}

	public static void setSaveCsvFiles(final boolean saveCsvFiles) {
		LogAnalyzerParameters.saveCsvFiles = saveCsvFiles;
	}

	public static void setServersPropertiesFilePath(final String serversPropertiesFilePath) {
		LogAnalyzerParameters.serversPropertiesFilePath = PathUtils.getFullPath(rootFolderPath,
				serversPropertiesFilePath);
		Properties serversProperties = PropertiesUtils
				.loadPropertiesFile(LogAnalyzerParameters.serversPropertiesFilePath);
		PropertiesUtils.printProperties(serversProperties, Level.FINEST);
		serverNameEnvironmentMap = new HashMap<String, ServerNameEnvironment>();
		Set servers = serversProperties.keySet();
		for (Object server1 : servers) {
			String server1String = server1.toString();
			String[] serverNameEnvironmentArray = serversProperties.getProperty(server1String).split(OzConstants.COMMA);
			ServerNameEnvironment serverNameEnvironment = new ServerNameEnvironment(serverNameEnvironmentArray[0],
					serverNameEnvironmentArray[1]);
			serverNameEnvironmentMap.put(server1String, serverNameEnvironment);
		}
	}

	public static void setSystem(final String system) {
		LogAnalyzerParameters.system = system;
	}

	public static void setTopUsersThreshold(int topUsersThreshold) {
		LogAnalyzerParameters.topUsersThreshold = topUsersThreshold;
	}

	public static void setUriFilter(final String uriFilter) {
		String[] uriFilterArray = uriFilter.split(URI_FILTER_DELIMITER);
		uriFilterOperator = uriFilterArray[0];
		LogAnalyzerParameters.uriFilter = uriFilterArray[1];
	}

	public static void setUriFilter4ApplicationStartCount(final String uriFilter4ApplicationStartCount) {
		LogAnalyzerParameters.uriFilter4ApplicationStartCount = uriFilter4ApplicationStartCount;
	}

	public static void setUriRegExpFilter(String uriRegExpFilter) {
		LogAnalyzerParameters.uriRegExpFilter = uriRegExpFilter;
		if (uriRegExpFilter != null && uriRegExpFilter.length() > 0) {
			acceptRejectRegExpFilter = new AcceptRejectRegExpFilter(uriRegExpFilter);

		}
	}

	public static AcceptRejectRegExpFilter getAcceptRejectRegExpFilter() {
		return acceptRejectRegExpFilter;
	}
}
