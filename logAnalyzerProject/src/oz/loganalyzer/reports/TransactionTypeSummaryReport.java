package oz.loganalyzer.reports;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.loganalyzer.data.LogRecord;

public class TransactionTypeSummaryReport extends AbstractLogAnalyzerReport {

	private static Logger logger = JulUtils.getLogger();
	private static final String[][] fieldNames = { { "recordCount", "Requests" },
			{ "logStartTime", "Start time" }, { "logEndTime", "End time" },
			{ "averageResponseTime", "Average rt(ms)" }, { "minResponseTime", "Min rt(ms)" },
			{ "maxResponseTime", "Max rt(ms)" }, { "requestPerUser", "Requests per user" } };

	public void specificReportLineProcessing(final LogRecord logRecord) {
		updateStatsMap(logRecord.getUri(), logRecord);
	}

	protected final StringBuilder generateReport() {
		return generateSummaryReport("Transaction type", fieldNames);
	}
}
