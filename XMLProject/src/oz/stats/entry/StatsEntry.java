package oz.stats.entry;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.number.NumberUtils;

public class StatsEntry {
	private static Logger logger = JulUtils.getLogger();

	private static void addKeyValue(final StringBuilder sb, final String key, final String value) {
		sb.append(key);
		sb.append(OzConstants.EQUAL_SIGN);
		sb.append(value);
		sb.append(OzConstants.COMMA);
	}

	private StatsAttributesEnum statsAttributesEnum;
	private StatsAttributesEnum[] statsAttributesEnumValues = StatsAttributesEnum.values();
	private String statsName = null;
	private String serverName = null;
	private long requestCount = OzConstants.LONG_0;
	private long maxServiceTime = OzConstants.LONG_0;
	private String maxServiceTimeString = null;
	private long startTime = OzConstants.LONG_0;
	private String startTimeString = null;
	private long lastSampleTime = OzConstants.LONG_0;

	private String lastSampleTimeString = null;

	public StatsEntry() {
	}

	public StatsEntry(final String statLine) {
		String[] statsLineArray = statLine.split(OzConstants.COMMA);
		for (String statsline1 : statsLineArray) {
			String[] keyValuePair = statsline1.split(OzConstants.EQUAL_SIGN);
			String key = keyValuePair[0];
			String value = keyValuePair[1];
			StatsAttributesEnum statsAttributesEnum1 = StatsAttributesEnum.valueOf(key);
			switch (statsAttributesEnum1) {
			case statsName:
				statsName = value;
				break;
			case server:
				serverName = value;
				break;
			case requestCount:
				requestCount = Long.parseLong(value);
				break;
			case maxServiceTime:
				maxServiceTime = Long.parseLong(value);
				maxServiceTimeString = DateTimeUtils.formatTime(maxServiceTime, DateTimeUtils.DATE_FORMAT_mmss);
				break;
			case startTime:
				startTime = Long.parseLong(value);
				startTimeString = DateTimeUtils.formatTime(startTime, DateTimeUtils.DATE_FORMAT_ddMMyyyy_HHmmss);
				break;
			case lastSampleTime:
				lastSampleTime = Long.parseLong(value);
				lastSampleTimeString = DateTimeUtils.formatTime(lastSampleTime,
						DateTimeUtils.DATE_FORMAT_ddMMyyyy_HHmmss);
				break;
			}
		}
	}

	public String getAsString() {
		StringBuilder sb = new StringBuilder();
		for (StatsAttributesEnum statsAttributesEnumValue1 : statsAttributesEnumValues) {
			switch (statsAttributesEnumValue1) {
			case statsName:
				addKeyValue(sb, statsAttributesEnumValue1.toString(), statsName);
				break;
			case server:
				addKeyValue(sb, statsAttributesEnumValue1.toString(), serverName);
				break;
			case requestCount:
				String requestCountStr = NumberUtils.format(requestCount, NumberUtils.FORMATTER_COMMAS);
				addKeyValue(sb, statsAttributesEnumValue1.toString(), requestCountStr);
				break;
			case maxServiceTime:
				addKeyValue(sb, statsAttributesEnumValue1.toString(), maxServiceTimeString);
				break;
			case startTime:
				addKeyValue(sb, statsAttributesEnumValue1.toString(), startTimeString);
				break;
			case lastSampleTime:
				addKeyValue(sb, statsAttributesEnumValue1.toString(), lastSampleTimeString);
				break;
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public final StatsEntry getDiffStatsEntry(final StatsEntry statsEntryparameter) {
		StatsEntry statsEntryDiff = new StatsEntry();
		for (StatsAttributesEnum statsAttributesEnumValue1 : statsAttributesEnumValues) {
			switch (statsAttributesEnumValue1) {
			case statsName:
				statsEntryDiff.setStatsName(statsName);
				break;
			case server:
				statsEntryDiff.setServerName(serverName);
				break;
			case requestCount:
				long newRequestCount = statsEntryparameter.getRequestCount();
				long requestDiffCount = newRequestCount - requestCount;
				statsEntryDiff.setRequestCount(requestDiffCount);
				break;
			case maxServiceTime:
				statsEntryDiff.setMaxServiceTime(statsEntryparameter.getMaxServiceTime());
				statsEntryDiff.setMaxServiceTimeString(statsEntryparameter.getMaxServiceTimeString());
				break;
			case startTime:
				statsEntryDiff.setStartTime(statsEntryparameter.getStartTime());
				statsEntryDiff.setStartTimeString(statsEntryparameter.getStartTimeString());
				break;
			case lastSampleTime:
				statsEntryDiff.setLastSampleTime(statsEntryparameter.getLastSampleTime());
				statsEntryDiff.setLastSampleTimeString(statsEntryparameter.getLastSampleTimeString());
				break;
			}
		}

		return statsEntryDiff;
	}

	public final long getLastSampleTime() {
		return lastSampleTime;
	}

	public final String getLastSampleTimeString() {
		return lastSampleTimeString;
	}

	public final long getMaxServiceTime() {
		return maxServiceTime;
	}

	public final String getMaxServiceTimeString() {
		return maxServiceTimeString;
	}

	public final long getRequestCount() {
		return requestCount;
	}

	public final long getStartTime() {
		return startTime;
	}

	public final String getStartTimeString() {
		return startTimeString;
	}

	public final void setLastSampleTime(final long lastSampleTime) {
		this.lastSampleTime = lastSampleTime;
	}

	public final void setLastSampleTimeString(final String lastSampleTimeString) {
		this.lastSampleTimeString = lastSampleTimeString;
	}

	public final void setMaxServiceTime(final long maxServiceTime) {
		this.maxServiceTime = maxServiceTime;
	}

	public final void setMaxServiceTimeString(final String maxServiceTimeString) {
		this.maxServiceTimeString = maxServiceTimeString;
	}

	public final void setRequestCount(final long requestCount) {
		this.requestCount = requestCount;
	}

	public final void setServerName(final String serverName) {
		this.serverName = serverName;
	}

	public final void setStartTime(final long startTime) {
		this.startTime = startTime;
	}

	public final void setStartTimeString(final String startTimeString) {
		this.startTimeString = startTimeString;
	}

	public final void setStatsName(final String statsName) {
		this.statsName = statsName;
	}
}
