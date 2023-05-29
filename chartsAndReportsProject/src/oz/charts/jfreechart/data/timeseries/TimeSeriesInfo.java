package oz.charts.jfreechart.data.timeseries;

import java.util.ArrayList;

import oz.infra.constants.OzConstants;

public class TimeSeriesInfo {
	private String label;
	private ArrayList<TimeSeriesMinuteElement> timeSeriesMinuteElementAR;

	public TimeSeriesInfo(final String label,
			final ArrayList<TimeSeriesMinuteElement> timeSeriesMinuteElementAR) {
		this.label = label;
		this.timeSeriesMinuteElementAR = timeSeriesMinuteElementAR;
	}

	public final String getLabel() {
		return label;
	}

	public final ArrayList<TimeSeriesMinuteElement> getTimeSeriesMinuteElementAR() {
		return timeSeriesMinuteElementAR;
	}

	public String getString() {
		StringBuilder sb = new StringBuilder();
		sb.append(label);
		sb.append(OzConstants.COLON);
		sb.append(OzConstants.LINE_FEED);
		for (TimeSeriesMinuteElement timeSeriesMinuteElement : timeSeriesMinuteElementAR) {
			sb.append(timeSeriesMinuteElement.getString());
			sb.append(OzConstants.LINE_FEED);
		}
		return sb.toString();
	}
}
