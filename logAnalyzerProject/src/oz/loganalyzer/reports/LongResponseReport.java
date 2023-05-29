package oz.loganalyzer.reports;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.loganalyzer.LogAnalyzerParameters;
import oz.loganalyzer.data.LogRecord;

public class LongResponseReport extends AbstractLogAnalyzerReport {
	private int excessiveBytesSentThreshold = LogAnalyzerParameters.getExcessiveResponseLengthThreshold();
	private int recordCount = 0;
	private static Logger logger = JulUtils.getLogger();

	public void specificReportLineProcessing(final LogRecord logRecord) {
		if (logRecord.getBytesSent() > excessiveBytesSentThreshold) {
			if (recordCount == 0) {
				String title = StringUtils.concat(ExcelWorkbook.HEADER01_DIRECTIVE, "Long responses list",
						LINE_SEPARATOR);
				reportSb.append(title);
				reportSb.append(StringUtils.concat(ExcelWorkbook.COLUMN_HEADERS_DIRECTIVE + logRecord.getHeader()
						+ LINE_SEPARATOR));
			}
			reportSb.append(logRecord.toString(OzConstants.COMMA) + LINE_SEPARATOR);
			recordCount++;
		}
	}

	protected StringBuilder generateReport() {
		reportSb.append(ExcelWorkbook.HEADER01_DIRECTIVE + "Response length for " + String.valueOf(recordCount)
				+ " requests has exceeded " + String.valueOf(excessiveBytesSentThreshold) + " bytes");
		return reportSb;
	}
}
