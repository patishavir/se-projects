package oz.loganalyzer.reports;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.loganalyzer.common.LogAnalyzerUtils;
import oz.loganalyzer.data.LogRecord;

public class DistinctUsersReport extends AbstractLogAnalyzerReport {
	private String remoteHost = null;
	private Integer usageCount = null;
	private Integer usageCountPlusOne = null;

	private Map<String, Integer> distinctUsersMap = new HashMap<String, Integer>();
	private int grandTotal = 0;
	private static Logger logger = JulUtils.getLogger();

	public final void specificReportLineProcessing(final LogRecord logRecord) {
		remoteHost = logRecord.getRemoteHost();
		usageCount = distinctUsersMap.get(remoteHost);
		grandTotal++;
		if (usageCount == null) {
			distinctUsersMap.put(remoteHost, new Integer(1));
		} else {
			usageCountPlusOne = usageCount + 1;
			distinctUsersMap.put(remoteHost, usageCountPlusOne);
		}
	}

	protected final StringBuilder generateReport() {

		String titleText = "Distinct users report for " + getReportTimeRange() + LINE_SEPARATOR;
		String title = LogAnalyzerUtils.getTitle(titleText, ExcelWorkbook.HEADER01_DIRECTIVE);
		String keyHeader = "remote host";
		boolean firstIteration = true;
		separator = OzConstants.COMMA;
		Set<String> keySet = distinctUsersMap.keySet();
		for (String remoteHost1 : keySet) {
			if (firstIteration) {
				reportSb.append(title);
				String columnHeaderText = keyHeader + separator + "Total user usage count";
				String columnHeaders = LogAnalyzerUtils.getTitle(columnHeaderText,
						ExcelWorkbook.COLUMN_HEADERS_DIRECTIVE);
				reportSb.append(columnHeaders);
				firstIteration = false;
			}
			if (remoteHost1.trim().length() > 0) {
				String totalUserUsageCount = String.valueOf(distinctUsersMap.get(remoteHost1));
				reportSb.append(StringUtils.concat(OzConstants.LINE_FEED, remoteHost1, separator, totalUserUsageCount));
			}
		}
		String distinctUsersStr = String.valueOf(keySet.size());
		reportSb.append(StringUtils.concat(OzConstants.LINE_FEED, OzConstants.LINE_FEED, distinctUsersStr, separator,
				String.valueOf(grandTotal)));
		logger.info(StringUtils.concat("number of distinct users: ", distinctUsersStr, " total usage count: ",
				String.valueOf(grandTotal), "   ", StringUtils.repeatString(OzConstants.ASTERISK, OzConstants.INT_50)));
		return reportSb;
	}
}
