package oz.loganalyzer.reports;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.logging.jul.JulUtils;
import oz.loganalyzer.LogAnalyzerParameters;
import oz.loganalyzer.data.LogRecord;

public class SlowTransactionsReport extends AbstractLogAnalyzerReport {
	private int excessiveReponseTimeThreshold = LogAnalyzerParameters
			.getExcessiveReponseTimeThreshold();
	private int recordCount = 0;
	private static Logger logger = JulUtils.getLogger();

	public void specificReportLineProcessing(final LogRecord logRecord) {
		if (logRecord.getResponseTimeMicroSeconds() > OzConstants.INT_MILLION
				* excessiveReponseTimeThreshold) {
			if (recordCount == 0) {
				String title = ExcelWorkbook.HEADER01_DIRECTIVE + "Slow transactions list"
						+ LINE_SEPARATOR;
				reportSb.append(title);
				reportSb.append(ExcelWorkbook.COLUMN_HEADERS_DIRECTIVE + logRecord.getHeader()
						+ LINE_SEPARATOR);
			}
			reportSb.append(logRecord.toString(OzConstants.COMMA) + LINE_SEPARATOR);
			recordCount++;
		}
	}

	protected StringBuilder generateReport() {
		reportSb.append(ExcelWorkbook.HEADER01_DIRECTIVE + "Response time for "
				+ String.valueOf(recordCount) + " requests has exceeded "
				+ String.valueOf(excessiveReponseTimeThreshold) + " seconds");
		return reportSb;
	}
}
