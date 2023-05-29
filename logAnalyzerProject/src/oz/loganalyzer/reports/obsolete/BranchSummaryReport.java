package oz.loganalyzer.reports.obsolete;

import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.excel.workbook.ExcelWorkbook;
import oz.infra.fibi.FibiUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils.PrintOption;
import oz.loganalyzer.data.LogRecord;
import oz.loganalyzer.reports.AbstractLogAnalyzerReport;

@Deprecated
public class BranchSummaryReport extends AbstractLogAnalyzerReport {
	private static Set<String> environmentsSet = new TreeSet<String>();
	private static Logger logger = JulUtils.getLogger();
	final static String[][] fieldNames = { { "environment", "env" }, { "recordCount", "Requests" },
			{ "requestRate", "Requests/sec" }, { "numberOfDistinctUsers", "Distinct users" },
			{ "averageResponseTime", "Average rt(ms)" }, { "minResponseTime", "Min rt(ms)" },
			{ "maxResponseTime", "Max rt(ms)" }, { "logStartTime", "Start time" },
			{ "logEndTime", "End time" } };

	public void specificReportLineProcessing(final LogRecord logRecord) {
		String branch = FibiUtils.calcBranchNumberFromIPaddress(logRecord.getRemoteHost());
		if (branch == null) {
			branch = "???";
		}
		logger.finest(branch + "   " + logRecord.getRemoteHost());
		updateStatsMap(branch, logRecord);
		environmentsSet.add(logRecord.getEnvironment());
	}

	protected final StringBuilder generateReport() {
		TreeSet<String> branchServerTreeSet = new TreeSet<String>(statsMap.keySet());
		boolean firstIteration = true;
		separator = OzConstants.COMMA;
		String title = ExcelWorkbook.HEADER01_DIRECTIVE + "*** Branch summary report for "
				+ getReportTimeRange() + " ***" + LINE_SEPARATOR;
		for (String branch : branchServerTreeSet) {
			if (firstIteration) {
				reportSb.append(title);
				String columnHeaders = ExcelWorkbook.COLUMN_HEADERS_DIRECTIVE
						+ "Branch"
						+ separator
						+ statsMap.get(branch).getStatisticsReport(separator,
								PrintOption.HEADER_ONLY, fieldNames);
				reportSb.append(columnHeaders);
				firstIteration = false;
			}
			if (branch.trim().length() > 0) {
				reportSb.append(OzConstants.LINE_FEED
						+ ExcelWorkbook.FORMAT_AS_RICH_TEXT_DIRECTIVE
						+ branch
						+ separator
						+ statsMap.get(branch).getStatisticsReport(separator,
								PrintOption.DATA_ONLY, fieldNames));
			}
		}
		int numberOfBranches = branchServerTreeSet.size();
		reportSb.append(OzConstants.LINE_FEED);
		reportSb.append(OzConstants.LINE_FEED);
		reportSb.append("***," + String.valueOf(numberOfBranches));
		reportSb.append(OzConstants.LINE_FEED);
		for (String environment : environmentsSet) {
			if (environment != null && environment.length() > 0) {
				reportSb.append(environment + OzConstants.COMMA + ExcelWorkbook.FORMULA_DIRECTIVE
						+ "COUNTIF(B3:B" + String.valueOf(numberOfBranches + 2) + "%COMMA%\""
						+ environment + "\")");
				reportSb.append(OzConstants.LINE_FEED);
			}
		}
		// logger.info(reportSb.toString());
		return reportSb;
	}
}
