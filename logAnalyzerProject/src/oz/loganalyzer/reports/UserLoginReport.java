package oz.loganalyzer.reports;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.filter.RegExFilter;
import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;
import oz.loganalyzer.LogAnalyzerParameters;
import oz.loganalyzer.data.LogRecord;

public class UserLoginReport extends AbstractLogAnalyzerReport {
	private int recordCount = 0;
	private int[] hourlyBreakDown = new int[OzConstants.INT_24];
	private static RegExFilter regExFilter = new RegExFilter(RegexpUtils.REGEXP_ANYSTRING
			+ LogAnalyzerParameters.getUriFilter4ApplicationStartCount()
			+ RegexpUtils.REGEXP_ANYSTRING);
	private static Logger logger = JulUtils.getLogger();

	@Override
	public void specificReportLineProcessing(final LogRecord logRecord) {
		// TODO Auto-generated method stub
		if (regExFilter.accept(logRecord.getUri())) {
			recordCount++;
			int index = Integer.parseInt(logRecord.getHour());
			hourlyBreakDown[index]++;
		}
	}

	@Override
	protected StringBuilder generateReport() {
		// TODO Auto-generated method stub
		String title = ExcelWorkbook.HEADER01_DIRECTIVE + "Snifit user login report";
		reportSb.append(title);
		reportSb.append(LINE_SEPARATOR);
		reportSb.append(ExcelWorkbook.HEADER01_DIRECTIVE + getReportTimeRange());
		reportSb.append(LINE_SEPARATOR);
		// reportSb.append(String.valueOf(recordCount) + " matched String: " +
		// uriFilter
		// + LINE_SEPARATOR);
		reportSb.append(ExcelWorkbook.COLUMN_HEADERS_DIRECTIVE + "From" + OzConstants.COMMA + "to"
				+ OzConstants.COMMA + "count" + LINE_SEPARATOR);
		for (int i = 0; i < hourlyBreakDown.length; i++) {
			reportSb.append(String.valueOf(i) + OzConstants.COLON + "00" + OzConstants.COMMA
					+ String.valueOf(i + 1) + OzConstants.COLON + "00" + OzConstants.COMMA
					+ String.valueOf(hourlyBreakDown[i]) + LINE_SEPARATOR);
		}
		reportSb.append("******************" + OzConstants.COMMA + "******************"
				+ OzConstants.COMMA + String.valueOf(recordCount));
		return reportSb;
	}
}
