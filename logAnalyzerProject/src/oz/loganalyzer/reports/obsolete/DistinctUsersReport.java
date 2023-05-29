package oz.loganalyzer.reports.obsolete;

import java.util.HashMap;

import oz.loganalyzer.LogAnalyzerParameters;
import oz.loganalyzer.data.LogRecord;
import oz.loganalyzer.reports.AbstractLogAnalyzerReport;

@Deprecated
public class DistinctUsersReport extends AbstractLogAnalyzerReport {
	private HashMap<String, Integer> distinctUserHashMap = new HashMap<String, Integer>();

	public void specificReportLineProcessing(final LogRecord logRecord) {
		String remoteHost = logRecord.getRemoteHost();
		String ipPrefixFilter = LogAnalyzerParameters.getIpPrefixFilter();
		if (ipPrefixFilter == null || ipPrefixFilter.length() == 0
				|| remoteHost.startsWith(ipPrefixFilter)) {
			Integer count = distinctUserHashMap.get(remoteHost);
			if (count == null) {
				distinctUserHashMap.put(remoteHost, 1);
			} else {
				distinctUserHashMap.put(remoteHost, count + 1);
			}
		}
	}

	protected StringBuilder generateReport() {
		reportSb.append("There are " + String.valueOf(distinctUserHashMap.size())
				+ " distinct users.");
		return reportSb;
	}
}
