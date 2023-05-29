package oz.loganalyzer.reports;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.loganalyzer.data.LogRecord;

public class StatusCodeSummaryReport extends AbstractLogAnalyzerReport {
	private static Logger logger = JulUtils.getLogger();

	private static final String[][] fieldNames = { { "recordCount", "Requests" }, { "requestRate", "Requests/sec" },
			{ "numberOfDistinctUsers", "Distinct users" }, { "averageResponseTime", "Average rt(ms)" },
			{ "minResponseTime", "Min rt(ms)" }, { "maxResponseTime", "Max rt(ms)" },
			{ "requestPerUser", "Requests per user" }, { "logStartTime", "Start time" }, { "logEndTime", "End time" } };

	public final void specificReportLineProcessing(final LogRecord logRecord) {
		updateStatsMap(String.valueOf(logRecord.getStatus()), logRecord);
	}

	protected final StringBuilder generateReport() {
		return generateSummaryReport("Status code", fieldNames);
	}
}
