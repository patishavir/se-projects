package oz.loganalyzer.reports;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils.PrintOption;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.loganalyzer.LogAnalyzerParameters;
import oz.loganalyzer.common.LogAnalyzerUtils;
import oz.loganalyzer.data.LogRecord;
import oz.loganalyzer.statistics.LogAnalyzerStatistics;

public abstract class AbstractLogAnalyzerReport {
	protected static final String LINE_SEPARATOR = SystemUtils.LINE_SEPARATOR;
	private int logRecordsProcessed = 0;
	protected StringBuilder reportSb = new StringBuilder();
	protected String separator = OzConstants.TAB;
	private String reportActualLowHhmmss = "99:99:99";
	private String reportActualHighHhmmss = "00:00:00";
	private String reportDate = null;
	protected Map<String, LogAnalyzerStatistics> statsMap = new HashMap<String, LogAnalyzerStatistics>();
	private static Logger logger = JulUtils.getLogger();

	private void calcReportActualHhmmss(final String logRecordTime, final String logRecordDate) {
		if (logRecordTime.compareTo(reportActualLowHhmmss) <= 0) {
			reportActualLowHhmmss = logRecordTime;
		}
		if (logRecordTime.compareTo(reportActualHighHhmmss) >= 0) {
			reportActualHighHhmmss = logRecordTime;
		}
		reportDate = logRecordDate;
	}

	protected abstract StringBuilder generateReport();

	protected StringBuilder generateSummaryReport(final String reportName, final String[][] fieldNames) {
		Set<String> keySet = new TreeSet<String>(statsMap.keySet());
		String title1 = StringUtils.concat("*** ", reportName, " summary report ", getReportTimeRange(),
				" ***" + LINE_SEPARATOR);
		String title = LogAnalyzerUtils.getTitle(title1, ExcelWorkbook.HEADER01_DIRECTIVE);
		boolean firstIteration = true;
		separator = OzConstants.COMMA;
		for (String key : keySet) {
			if (firstIteration) {
				reportSb.append(title);
				String columnHeaderText = reportName + separator
						+ statsMap.get(key).getStatisticsReport(separator, PrintOption.HEADER_ONLY, fieldNames);
				String columnHeaders = LogAnalyzerUtils.getTitle(columnHeaderText,
						ExcelWorkbook.COLUMN_HEADERS_DIRECTIVE);
				reportSb.append(columnHeaders);
				firstIteration = false;
			}
			if (key.trim().length() > 0) {
				reportSb.append(OzConstants.LINE_FEED + key + separator
						+ statsMap.get(key).getStatisticsReport(separator, PrintOption.DATA_ONLY, fieldNames));
			}
		}
		return reportSb;
	}

	protected StringBuilder generateSummaryReportTotals(final Set<String> totalsItems) {
		int numberOfStatsEntries = statsMap.keySet().size();
		reportSb.append(OzConstants.LINE_FEED);
		reportSb.append(OzConstants.LINE_FEED);
		reportSb.append("***," + String.valueOf(numberOfStatsEntries));
		reportSb.append(OzConstants.LINE_FEED);
		for (String totalsItem : totalsItems) {
			if (totalsItem != null && totalsItem.length() > 0) {
				if (LogAnalyzerParameters.isGenerateExcelWorkBook()) {
					String execlFormula = totalsItem + OzConstants.COMMA + ExcelWorkbook.FORMULA_DIRECTIVE
							+ "COUNTIF(B3:B" + String.valueOf(numberOfStatsEntries + 2) + "%COMMA%\"" + totalsItem
							+ "\")";
					logger.info(execlFormula);
					reportSb.append(execlFormula);
					reportSb.append(OzConstants.LINE_FEED);
				}
			}
		}
		return reportSb;
	}

	public final int getLogRecordsProcessed() {
		return logRecordsProcessed;
	}

	protected String getReportTimeRange() {
		StringBuilder sb = new StringBuilder(reportDate);
		sb.append(OzConstants.BLANK);
		sb.append(reportActualLowHhmmss);
		sb.append(OzConstants.BLANK);
		sb.append(OzConstants.MINUS_SIGN);
		sb.append(OzConstants.BLANK);
		sb.append(reportActualHighHhmmss);
		sb.append(OzConstants.BLANK);
		return sb.toString();
	}

	public final void processLogRecord(final LogRecord logRecord) {
		String logRecordTime = logRecord.getTime();
		calcReportActualHhmmss(logRecordTime, logRecord.getDate());
		logRecordsProcessed++;
		specificReportLineProcessing(logRecord);
		logger.finest(logRecord.getLine());
	}

	public abstract void specificReportLineProcessing(final LogRecord logRecord);

	protected void updateStatsMap(final String key, final LogRecord logRecord) {
		if (statsMap.get(key) != null) {
			statsMap.get(key).processLogRecord(logRecord);
		} else {
			LogAnalyzerStatistics logAnalyzerStatistics = new LogAnalyzerStatistics();
			statsMap.put(key, logAnalyzerStatistics);
			logger.finest(key);
			logAnalyzerStatistics.processLogRecord(logRecord);
			logger.finest(key + " added to statsMap ...");
		}
	}
}
