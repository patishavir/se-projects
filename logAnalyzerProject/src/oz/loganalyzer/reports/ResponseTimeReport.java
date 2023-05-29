package oz.loganalyzer.reports;

import java.text.DecimalFormat;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.logging.jul.JulUtils;
import oz.loganalyzer.common.LogAnalyzerUtils;
import oz.loganalyzer.data.LogRecord;
import oz.loganalyzer.statistics.LogAnalyzerStatistics;

public class ResponseTimeReport extends AbstractLogAnalyzerReport {
	private LogAnalyzerStatistics logAnalyzerStatistics = new LogAnalyzerStatistics();
	private static Logger logger = JulUtils.getLogger();

	public void specificReportLineProcessing(final LogRecord logRecord) {
		logAnalyzerStatistics.processLogRecord(logRecord);
	}

	protected StringBuilder generateReport() {
		separator = OzConstants.COMMA;
		String titleText = "Response time breakdown for " + getReportTimeRange() + LINE_SEPARATOR;
		String title = LogAnalyzerUtils.getTitle(titleText, ExcelWorkbook.HEADER01_DIRECTIVE);
		reportSb.append(title);
		reportSb.append(getResponseTimeReport(logAnalyzerStatistics));
		return reportSb;
	}

	public String getResponseTimeReport(final LogAnalyzerStatistics logAnalyzerStatistics) {
		final String COLUMN_SEPARATOR = OzConstants.COMMA;
		int[] responseTimeArray = logAnalyzerStatistics.getResponseTimeArray();
		int recordCount = logAnalyzerStatistics.getRecordCount();
		int responseTimeBreakdownInMillis = logAnalyzerStatistics
				.getResponseTimeBreakdownInMillis();
		DecimalFormat decimalFormat = new DecimalFormat("0.0");
		DecimalFormat percentsDecimalFormat = new DecimalFormat("00.0 ");
		String headerText = "Time range (sec)" + COLUMN_SEPARATOR + "requested processed"
				+ COLUMN_SEPARATOR + "percent processed ";
		final String header1 = LogAnalyzerUtils.getTitle(headerText,
				ExcelWorkbook.COLUMN_HEADERS_DIRECTIVE);
		StringBuilder sb = new StringBuilder(header1);
		final String timeRangeSeperator = OzConstants.MINUS_SIGN;
		for (int i = 0; i < responseTimeArray.length; i++) {
			float lowRange = (float) (responseTimeBreakdownInMillis * i) / OzConstants.INT_MILLION;
			float highRange = (float) (responseTimeBreakdownInMillis * (i + 1))
					/ OzConstants.INT_MILLION;
			sb.append(LINE_SEPARATOR);
			sb.append(decimalFormat.format(lowRange));
			sb.append(timeRangeSeperator);
			if (i < (responseTimeArray.length - 1)) {
				sb.append(decimalFormat.format(highRange));
			} else {
				sb.append("999");
			}
			sb.append(COLUMN_SEPARATOR);
			String requestCountString = String.valueOf(responseTimeArray[i]);
			sb.append(String.valueOf(requestCountString));
			float percents = (float) (responseTimeArray[i] * OzConstants.INT_HUNDRED) / recordCount;
			sb.append(COLUMN_SEPARATOR);
			sb.append(percentsDecimalFormat.format(percents));
		}
		return sb.toString();
	}
}
