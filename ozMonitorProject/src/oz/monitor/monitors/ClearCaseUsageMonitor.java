package oz.monitor.monitors;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import oz.clearcase.infra.ClearCaseUtils;
import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.map.MapUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.string.StringUtils;
import oz.monitor.common.OzMonitorResponse;

public class ClearCaseUsageMonitor extends AbstractOzMonitor {
	private int usageReportIntervalinSeconds = 3600;
	private long lastReportTimeStamp = 0;
	private int availableLicensesThreshold = 0;
	private static Map<String, Integer> clearCaseUserUsageStatsMap = new HashMap<String, Integer>();
	private static Map<String, String> clearCaseAlreadyCountedUsageMap = new HashMap<String, String>();

	public ClearCaseUsageMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this, this.getClass().getSuperclass());
	}

	public OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		String[] clearLicenseArray = ClearCaseUtils.getClearLicenseArray();
		int getNumberOfActiveUsers = ClearCaseUtils.getNumberOfActiveUsers(clearLicenseArray);
		ClearCaseUtils.getUserUsageStats(clearLicenseArray, clearCaseUserUsageStatsMap,
				clearCaseAlreadyCountedUsageMap);
		long secondsFromLastReport = DateTimeUtils.getDifferenceInSeconds(lastReportTimeStamp);
		if (lastReportTimeStamp == 0 || (secondsFromLastReport >= usageReportIntervalinSeconds)) {
			MapUtils.printMap(clearCaseUserUsageStatsMap, "ClearCase usage stats", Level.INFO);
			lastReportTimeStamp = System.currentTimeMillis();
		}
		MapUtils.printMap(clearCaseAlreadyCountedUsageMap, "ClearCase already counted usage", Level.FINEST);
		int availableLicneses = ClearCaseUtils.getNumberOfAvailableLicenses(clearLicenseArray);
		String message = StringUtils.concat(String.valueOf(getNumberOfActiveUsers), " active ClearCase users. ",
				String.valueOf(availableLicneses), " available licenses.");
		logger.finest(message);
		boolean resourceStatus = availableLicneses > availableLicensesThreshold;
		if (!resourceStatus) {
			ArrayUtils.printArray(clearLicenseArray, OzConstants.LINE_FEED);
		}
		return new OzMonitorResponse(resourceStatus, message, message);
	}

	public void setAvailableLicensesThreshold(int availableLicensesThreshold) {
		this.availableLicensesThreshold = availableLicensesThreshold;
	}

	public void setUsageReportIntervalinSeconds(final int usageReportIntervalinSeconds) {
		this.usageReportIntervalinSeconds = usageReportIntervalinSeconds;
	}
}
