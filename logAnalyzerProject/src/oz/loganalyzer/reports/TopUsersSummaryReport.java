package oz.loganalyzer.reports;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.loganalyzer.LogAnalyzerParameters;
import oz.loganalyzer.data.LogRecord;
import oz.loganalyzer.statistics.LogAnalyzerStatistics;

public class TopUsersSummaryReport extends AbstractLogAnalyzerReport {
	private static Logger logger = JulUtils.getLogger();

	private static final String[][] fieldNames = { { "recordCount", "Requests" }, { "requestRate", "Requests/sec" },
			{ "numberOfDistinctUsers", "Distinct users" }, { "averageResponseTime", "Average rt(ms)" },
			{ "minResponseTime", "Min rt(ms)" }, { "maxResponseTime", "Max rt(ms)" },
			{ "requestPerUser", "Requests per user" }, { "logStartTime", "Start time" }, { "logEndTime", "End time" } };

	public final void specificReportLineProcessing(final LogRecord logRecord) {
		updateStatsMap(logRecord.getRemoteHost(), logRecord);
	}

	protected final StringBuilder generateReport() {
		Set<Entry<String, LogAnalyzerStatistics>> statsEntrySet = statsMap.entrySet();
		int topUsersThreshold = LogAnalyzerParameters.getTopUsersThreshold();
		Map<String, LogAnalyzerStatistics> topUsersMap = new HashMap<String, LogAnalyzerStatistics>();
		for (Entry<String, LogAnalyzerStatistics> entry : statsEntrySet) {
			if (entry.getValue().getRecordCount() > topUsersThreshold) {
				topUsersMap.put(entry.getKey(), entry.getValue());
			}
		}
		statsMap = topUsersMap;
		return generateSummaryReport("TopUsers", fieldNames);
	}
}
