package oz.loganalyzer.reports.obsolete;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils.PrintOption;
import oz.loganalyzer.data.LogRecord;
import oz.loganalyzer.reports.AbstractLogAnalyzerReport;
import oz.loganalyzer.statistics.LogAnalyzerStatistics;

@Deprecated
public class ApplicationServerReport extends AbstractLogAnalyzerReport {
	HashMap<String, LogAnalyzerStatistics> applicationServerStatshHM = new HashMap<String, LogAnalyzerStatistics>();
	private static Logger logger = JulUtils.getLogger();

	public final void specificReportLineProcessing(final LogRecord logRecord) {
		String applicationServer = logRecord.getApplicationServer();
		LogAnalyzerStatistics logAnalyzerStatistics = applicationServerStatshHM
				.get(applicationServer);
		if (logAnalyzerStatistics == null) {
			logAnalyzerStatistics = new LogAnalyzerStatistics();
			applicationServerStatshHM.put(applicationServer, logAnalyzerStatistics);
		}
		logAnalyzerStatistics.processLogRecord(logRecord);
	}

	protected StringBuilder generateReport() {
		separator = OzConstants.COMMA;
		TreeSet<String> applicationServerTreeSet = new TreeSet<String>(applicationServerStatshHM
				.keySet());
		reportSb.append("*** application server report for " + getReportTimeRange() + " ***"
				+ LINE_SEPARATOR);
		boolean headerPrinted = false;
		for (String applicationServer : applicationServerTreeSet) {
			if (!headerPrinted) {
				reportSb.append(applicationServerStatshHM.get(applicationServer)
						.getStatisticsReport(separator, PrintOption.HEADER_ONLY));
				headerPrinted = true;
			}
			String applicationServerReportString = applicationServerStatshHM.get(applicationServer)
					.getStatisticsReport(separator, PrintOption.DATA_ONLY);
			logger.info(applicationServerReportString + " ****************");
			reportSb.append(LINE_SEPARATOR + applicationServerReportString);
		}
		return reportSb;
	}
}
