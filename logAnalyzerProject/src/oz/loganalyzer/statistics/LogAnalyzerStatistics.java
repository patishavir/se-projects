package oz.loganalyzer.statistics;

import java.util.HashMap;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;
import oz.infra.print.PrintUtils.PrintOption;
import oz.infra.regexp.RegexpUtils;
import oz.infra.system.SystemUtils;
import oz.loganalyzer.LogAnalyzerParameters;
import oz.loganalyzer.data.LogRecord;

public class LogAnalyzerStatistics {
	private String logStartDate = null;
	private String logStartTime = null;
	private String logEndDate = null;
	private String logEndTime = null;
	private int recordCount = 0;
	private int minResponseTime = Integer.MAX_VALUE;
	private int maxResponseTime = Integer.MIN_VALUE;
	private String maxLine = null;
	private String minLine = null;
	private long totalResponseTime = 0;
	private int subZeroResponseTimeCounter = 0;
	private float averageResponseTime = Float.MIN_VALUE;
	private float requestRate = Float.MIN_VALUE;
	private float requestPerUser = Float.MIN_VALUE;
	private final int responseTimeBreakdownInMillis = LogAnalyzerParameters
			.getResponseTimeBreakdownInMicros();
	private final int numberOfCategories = OzConstants.INT_25;
	private int[] responseTimeArray = new int[numberOfCategories];
	private HashMap<String, Integer> distinctUserHashMap = new HashMap<String, Integer>();
	private int numberOfDistinctUsers = 0;
	private String uriFilter4ApplicationStartCount = LogAnalyzerParameters
			.getUriFilter4ApplicationStartCount();
	private int applicationStartCounter = 0;
	private String application = null;
	private String environment = null;
	private String remoteHost = null;
	private static final String LINE_SEPARATOR = SystemUtils.LINE_SEPARATOR;

	private static Logger logger = JulUtils.getLogger();

	public String getApplication() {
		return application;
	}

	public final int getApplicationStartCounter() {
		return applicationStartCounter;
	}

	public float getAverageResponseTime() {
		return averageResponseTime;
	}

	public String getDebugInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append(LINE_SEPARATOR);
		sb.append("maxLine: " + maxLine);
		sb.append(LINE_SEPARATOR);
		sb.append("minLine: " + minLine);
		sb.append(LINE_SEPARATOR);
		sb.append("subZeroResponseTimeCounter: " + String.valueOf(subZeroResponseTimeCounter));
		return sb.toString();
	}

	public String getEnvironment() {
		return environment;
	}

	public String getLogEndTime() {
		return logEndTime;
	}

	public String getLogStartDate() {
		return logStartDate;
	}

	public String getLogStartTime() {
		return logStartTime;
	}

	public final int getMaxResponseTime() {
		return maxResponseTime;
	}

	public final int getMinResponseTime() {
		return minResponseTime;
	}

	public final int getNumberOfDistinctUsers() {
		return numberOfDistinctUsers;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public float getRequestPerUser() {
		return requestPerUser;
	}

	public float getRequestRate() {
		return requestRate;
	}

	public int[] getResponseTimeArray() {
		return responseTimeArray;
	}

	public int getResponseTimeBreakdownInMillis() {
		return responseTimeBreakdownInMillis;
	}

	public String getStatisticsReport(final String separator, final PrintOption printOption) {
		final String[][] defaultFieldNames = { { "logStartDate", "Start date" },
				{ "logStartTime", "Start time" }, { "logEndTime", "End time" },
				{ "recordCount", "Request count" }, { "requestRate", "Requests per second" },
				{ "minResponseTime", "Min responseTime (milli seconds)" },
				{ "maxResponseTime", "Max responseTime (milli seconds)" },
				{ "averageResponseTime", "Average response time (milli seconds)" },
				{ "numberOfDistinctUsers", "Number of distinct users" },
				{ "applicationStartCounter", "Application start counter" } };

		return getStatisticsReport(separator, printOption, defaultFieldNames);
	}

	public String getStatisticsReport(final String separator, final PrintOption printOption,
			final String[][] fieldNames) {
		numberOfDistinctUsers = distinctUserHashMap.size();
		averageResponseTime = (float) totalResponseTime / recordCount / OzConstants.INT_THOUSAND;
		long timeDifference = DateTimeUtils.subtractDates(logEndTime, logStartTime,
				DateTimeUtils.DATE_FORMAT_HHmmss)
				/ OzConstants.INT_THOUSAND;
		requestRate = ((float) recordCount / timeDifference);
		requestPerUser = ((float) recordCount / numberOfDistinctUsers);
		return PrintUtils.printObjectFields(this, fieldNames, separator, printOption);
	}

	private void performDistinctUsersProcessing(final LogRecord logRecord) {
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

	private void processApplicationStartCounter(final LogRecord logRecord) {
		if (logRecord.getUri().matches(
				RegexpUtils.REGEXP_ANYSTRING + uriFilter4ApplicationStartCount
						+ RegexpUtils.REGEXP_ANYSTRING)) {
			applicationStartCounter++;
		}
	}

	public final void processLogRecord(final LogRecord logRecord) {
		logger.finest(logRecord.getLine());
		recordCount++;
		performDistinctUsersProcessing(logRecord);
		processApplicationStartCounter(logRecord);
		environment = logRecord.getEnvironment();
		application = logRecord.getApplication();
		remoteHost = logRecord.getRemoteHost();
		int responseTime = logRecord.getResponseTimeMicroSeconds();
		totalResponseTime += responseTime;
		int responseTimeInMillis = responseTime / OzConstants.INT_THOUSAND;
		if (responseTimeInMillis > maxResponseTime) {
			maxResponseTime = responseTimeInMillis;
			maxLine = logRecord.getLine();
		}
		if (responseTimeInMillis < minResponseTime) {
			minResponseTime = responseTimeInMillis;
			minLine = logRecord.getLine();
		}
		if (responseTime < 0) {
			subZeroResponseTimeCounter++;
		}

		logEndTime = logRecord.getTime();
		logEndDate = logRecord.getDate();
		if (logStartTime == null) {
			logStartTime = logEndTime;
		}
		if (logStartDate == null) {
			logStartDate = logEndDate;
		}
		int responseTimeArrayIndex = responseTime / responseTimeBreakdownInMillis;
		if (responseTimeArrayIndex > responseTimeArray.length - 1) {
			responseTimeArrayIndex = responseTimeArray.length - 1;
		}
		responseTimeArray[responseTimeArrayIndex]++;
	}
}
