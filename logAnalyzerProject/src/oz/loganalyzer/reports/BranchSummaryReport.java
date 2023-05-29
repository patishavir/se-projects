package oz.loganalyzer.reports;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.fibi.FibiUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.MapUtils;
import oz.loganalyzer.LogAnalyzerParameters;
import oz.loganalyzer.data.LogRecord;

public class BranchSummaryReport extends AbstractLogAnalyzerReport {
	private static Set<String> applicationSet = new TreeSet<String>();
	private static HashMap<String, String> invalidIpAddressHashMap = new HashMap<String, String>();

	private static Logger logger = JulUtils.getLogger();

	private final static String[][] fieldNames = { { "application", "app" }, { "recordCount", "Requests" },
			{ "requestRate", "Requests/sec" }, { "numberOfDistinctUsers", "Distinct users" },
			{ "averageResponseTime", "Average rt(ms)" }, { "minResponseTime", "Min rt(ms)" },
			{ "maxResponseTime", "Max rt(ms)" }, { "requestPerUser", "Requests per user" },
			{ "logStartTime", "Start time" }, { "logEndTime", "End time" } };

	public void specificReportLineProcessing(final LogRecord logRecord) {
		String branchReportAppPrefix = LogAnalyzerParameters.getBranchReportAppPrefix();
		String application = logRecord.getApplication();
		if (branchReportAppPrefix == null || application.startsWith(branchReportAppPrefix)) {
			String remoteHost = logRecord.getRemoteHost();
			String branch = FibiUtils.calcBranchNumberFromIPaddress(remoteHost);
			if (branch == null) {
				branch = "???";
				invalidIpAddressHashMap.put(remoteHost, remoteHost);
			}
			logger.finest(branch + "   " + logRecord.getRemoteHost());
			updateStatsMap(branch, logRecord);
			applicationSet.add(application);
		}
	}

	protected final StringBuilder generateReport() {
		generateSummaryReport("Branch", fieldNames);
		generateSummaryReportTotals(applicationSet);
		// logger.info(reportSb.toString());
		logger.info(MapUtils.printKeys(invalidIpAddressHashMap, "failed to convert ip address to branch number:",
				Level.FINEST));
		return reportSb;
	}
}
