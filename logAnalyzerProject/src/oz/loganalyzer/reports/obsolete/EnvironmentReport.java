package oz.loganalyzer.reports.obsolete;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils.PrintOption;
import oz.loganalyzer.data.LogRecord;
import oz.loganalyzer.reports.AbstractLogAnalyzerReport;
import oz.loganalyzer.statistics.LogAnalyzerStatistics;

@Deprecated
public class EnvironmentReport extends AbstractLogAnalyzerReport {
	HashMap<String, LogAnalyzerStatistics> environmentStatsHM = new HashMap<String, LogAnalyzerStatistics>();
	private static Logger logger = JulUtils.getLogger();

	public final void specificReportLineProcessing(final LogRecord logRecord) {
		String environment = logRecord.getEnvironment();
		LogAnalyzerStatistics logAnalyzerStatistics = environmentStatsHM.get(environment);
		if (logAnalyzerStatistics == null) {
			logAnalyzerStatistics = new LogAnalyzerStatistics();
			environmentStatsHM.put(environment, logAnalyzerStatistics);
			logger.info(environment);
		}
		logAnalyzerStatistics.processLogRecord(logRecord);
	}

	protected StringBuilder generateReport() {
		// return generateReport(PrintOption.MULTIPLE_LINES);
		return generateReport(PrintOption.MULTIPLE_LINES);
	}

	protected final StringBuilder generateReport(final PrintOption printOption) {
		Set<String> keySet = environmentStatsHM.keySet();
		logger.info(String.valueOf(keySet.size()));
		TreeSet<String> environmentTreeSet = new TreeSet<String>(keySet);
		for (String environment : environmentTreeSet) {
			reportSb.append(LINE_SEPARATOR + "*** environment report for " + environment + " ***"
					+ LINE_SEPARATOR);
			reportSb.append(environmentStatsHM.get(environment).getStatisticsReport(separator,
					printOption));
		}
		return reportSb;
	}
}
