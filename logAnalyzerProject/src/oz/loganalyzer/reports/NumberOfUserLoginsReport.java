package oz.loganalyzer.reports;

import java.util.TreeSet;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.filter.RegExFilter;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils.PrintOption;
import oz.infra.regexp.RegexpUtils;
import oz.loganalyzer.LogAnalyzerParameters;
import oz.loganalyzer.data.LogRecord;
import oz.loganalyzer.statistics.LogAnalyzerStatistics;

public class NumberOfUserLoginsReport extends AbstractLogAnalyzerReport {
	private static final int LOGIN_COUNT_REPORT_THRESHOLD = 1;
	private static RegExFilter regExFilter = new RegExFilter(RegexpUtils.REGEXP_ANYSTRING
			+ LogAnalyzerParameters.getUriFilter4ApplicationStartCount()
			+ RegexpUtils.REGEXP_ANYSTRING);
	private static final String[][] fieldNames = { { "remoteHost", "User IP address" },
			{ "recordCount", "Number of logins" }, { "logStartTime", "Start time" },
			{ "logEndTime", "End time" }, { "averageResponseTime", "Average rt(ms)" },
			{ "minResponseTime", "Min rt(ms)" }, { "maxResponseTime", "Max rt(ms)" } };
	private static Logger logger = JulUtils.getLogger();

	public final void specificReportLineProcessing(final LogRecord logRecord) {
		if (regExFilter.accept(logRecord.getUri())) {
			String remoteHost = logRecord.getRemoteHost();
			updateStatsMap(remoteHost, logRecord);
		}
	}

	protected final StringBuilder generateReport() {
		TreeSet<String> remoteHostTreeSet = new TreeSet<String>(statsMap.keySet());
		boolean firstIteration = true;
		separator = OzConstants.COMMA;
		String title = ExcelWorkbook.HEADER01_DIRECTIVE + "*** Number of logins report for "
				+ getReportTimeRange() + " ***" + LINE_SEPARATOR;
		for (String remoteHost : remoteHostTreeSet) {
			if (firstIteration) {
				reportSb.append(title);
				String columnHeaders = ExcelWorkbook.COLUMN_HEADERS_DIRECTIVE
						+ statsMap.get(remoteHost).getStatisticsReport(separator,
								PrintOption.HEADER_ONLY, fieldNames);
				reportSb.append(columnHeaders);
				firstIteration = false;
			}
			if (remoteHost.trim().length() > 0) {
				LogAnalyzerStatistics remoteUserStats = statsMap.get(remoteHost);
				if (remoteUserStats.getRecordCount() > LOGIN_COUNT_REPORT_THRESHOLD) {
					reportSb.append(OzConstants.LINE_FEED
							+ statsMap.get(remoteHost).getStatisticsReport(separator,
									PrintOption.DATA_ONLY, fieldNames));
				}
			}
		}
		return reportSb;
	}
}
