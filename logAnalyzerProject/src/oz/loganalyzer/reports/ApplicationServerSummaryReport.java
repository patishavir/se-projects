package oz.loganalyzer.reports;

import java.util.TreeSet;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils.PrintOption;
import oz.loganalyzer.LogAnalyzerParameters;
import oz.loganalyzer.ServerNameEnvironment;
import oz.loganalyzer.common.LogAnalyzerUtils;
import oz.loganalyzer.data.LogRecord;
import oz.loganalyzer.statistics.LogAnalyzerStatistics;

public class ApplicationServerSummaryReport extends AbstractLogAnalyzerReport {
	private LogAnalyzerStatistics logAnalyzerStatisticsGrandTotal = new LogAnalyzerStatistics();
	private static Logger logger = JulUtils.getLogger();
	private static final String[][] FIELD_NAMES = { { "recordCount", "Requests" }, { "requestRate", "Requests/sec" },
			{ "numberOfDistinctUsers", "Distinct users" }, { "averageResponseTime", "Average rt(ms)" },
			{ "minResponseTime", "Min rt(ms)" }, { "maxResponseTime", "Max rt(ms)" }, { "logStartTime", "Start time" },
			{ "logEndTime", "End time" } };

	public final void specificReportLineProcessing(final LogRecord logRecord) {
		updateStatsMap(logRecord.getApplicationServer(), logRecord);
		logAnalyzerStatisticsGrandTotal.processLogRecord(logRecord);
	}

	protected final StringBuilder generateReport() {
		TreeSet<String> applicationServerTreeSet = new TreeSet<String>(statsMap.keySet());
		String titleText = "Application server summary report for " + getReportTimeRange() + LINE_SEPARATOR;
		String title = LogAnalyzerUtils.getTitle(titleText, ExcelWorkbook.HEADER01_DIRECTIVE);
		String keyHeader = "Application Server";
		boolean firstIteration = true;
		separator = OzConstants.COMMA;
		for (String applicationServer : applicationServerTreeSet) {
			if (firstIteration) {
				reportSb.append(title);
				String columnHeaderText = keyHeader + separator + statsMap.get(applicationServer)
						.getStatisticsReport(separator, PrintOption.HEADER_ONLY, FIELD_NAMES);
				String columnHeaders = LogAnalyzerUtils.getTitle(columnHeaderText,
						ExcelWorkbook.COLUMN_HEADERS_DIRECTIVE);
				reportSb.append(columnHeaders);
				firstIteration = false;
			}
			if (applicationServer.trim().length() > 0) {
				ServerNameEnvironment serverNameEnvironment = LogAnalyzerParameters.getServerNameEnvironmentMap()
						.get(applicationServer);
				String wasServer = OzConstants.EMPTY_STRING;
				if (serverNameEnvironment != null && serverNameEnvironment.getServerName() != null) {
					wasServer = serverNameEnvironment.getServerName();
				}
				reportSb.append(OzConstants.LINE_FEED + applicationServer + OzConstants.BLANK + wasServer + separator
						+ statsMap.get(applicationServer).getStatisticsReport(separator, PrintOption.DATA_ONLY,
								FIELD_NAMES));
			}
		}
		reportSb.append(OzConstants.LINE_FEED + OzConstants.LINE_FEED + "********" + separator
				+ logAnalyzerStatisticsGrandTotal.getStatisticsReport(separator, PrintOption.DATA_ONLY, FIELD_NAMES));
		return reportSb;
	}
}
