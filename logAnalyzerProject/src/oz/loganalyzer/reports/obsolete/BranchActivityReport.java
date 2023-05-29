package oz.loganalyzer.reports.obsolete;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.fibi.FibiUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils.PrintOption;
import oz.loganalyzer.data.LogRecord;
import oz.loganalyzer.reports.AbstractLogAnalyzerReport;
import oz.loganalyzer.statistics.LogAnalyzerStatistics;

@Deprecated
public class BranchActivityReport extends AbstractLogAnalyzerReport {
	HashMap<String, LogAnalyzerStatistics> branchStatisticsHashMap = new HashMap<String, LogAnalyzerStatistics>();
	private static Logger logger = JulUtils.getLogger();

	public void specificReportLineProcessing(final LogRecord logRecord) {
		String branch = FibiUtils.calcBranchNumberFromIPaddress(logRecord.getRemoteHost());
		if (branch == null) {
			branch = "???";
		}
		logger.finest(branch + "   " + logRecord.getRemoteHost());
		LogAnalyzerStatistics logAnalyzerStatistics = branchStatisticsHashMap.get(branch);
		if (logAnalyzerStatistics == null) {
			logAnalyzerStatistics = new LogAnalyzerStatistics();
			branchStatisticsHashMap.put(branch, logAnalyzerStatistics);
		}
		logAnalyzerStatistics.processLogRecord(logRecord);
	}

	protected StringBuilder generateReport() {
		separator = OzConstants.COMMA;
		LogAnalyzerStatistics logAnalyzerStatistics = null;
		Set<String> branchSet = branchStatisticsHashMap.keySet();
		String[] branchArray = new String[branchSet.size()];
		branchSet.toArray(branchArray);
		Arrays.sort(branchArray);
		logger.finest("Number of branches: " + String.valueOf(branchSet.size() - 1));
		boolean printHeader = true;
		reportSb.append("*** branch activity report for " + getReportTimeRange() + " ***"
				+ LINE_SEPARATOR);
		for (String branch : branchArray) {
			logAnalyzerStatistics = branchStatisticsHashMap.get(branch);
			if (printHeader) {
				reportSb.append("branch "
						+ separator
						+ logAnalyzerStatistics.getStatisticsReport(separator,
								PrintOption.HEADER_ONLY) + LINE_SEPARATOR);
				printHeader = false;
			}
			reportSb.append(branch
					// + " - " + FibiUtils.getBranchName(branch)
					+ separator
					+ logAnalyzerStatistics.getStatisticsReport(separator, PrintOption.DATA_ONLY)
					+ LINE_SEPARATOR);
			// logger.info(logAnalyzerStatistics.getResponseTimeArray());
		}

		return reportSb;
	}
}
