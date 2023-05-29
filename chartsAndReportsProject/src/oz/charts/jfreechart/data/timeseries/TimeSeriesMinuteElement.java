package oz.charts.jfreechart.data.timeseries;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Logger;

import org.jfree.data.time.Minute;

import oz.charts.ChartParameters;
import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.number.NumberUtils;
import oz.infra.regexp.RegexpUtils;

public class TimeSeriesMinuteElement {
	private int minute;
	private int hour;
	private int day;
	private int month;
	private int year;
	private String hhmm;
	private double value;
	private boolean validElement = true;
	private static String chartDate = null;
	private static Logger logger = JulUtils.getLogger();

	public static String getChartDate() {
		return chartDate;
	}

	public TimeSeriesMinuteElement(final String inputRecord) {
		String[] inputRecordArray = inputRecord.split(RegexpUtils.REGEXP_WHITE_SPACE);
		try {
			year = Integer.parseInt(inputRecordArray[0].substring(0, 4));
			month = Integer.parseInt(inputRecordArray[0].substring(4, 6));
			day = Integer.parseInt(inputRecordArray[0].substring(6, 8));
			hour = Integer.parseInt(inputRecordArray[1].substring(0, 2));
			minute = Integer.parseInt(inputRecordArray[1].substring(3, 5));
			hhmm = inputRecordArray[1].substring(0, 5);
			value = Double.parseDouble(inputRecordArray[ChartParameters.getValueColumnIndex()]);
		} catch (Exception ex) {
			logger.warning(inputRecord);
			logger.warning(ex.toString());
			// ex.printStackTrace();
			validElement = false;
		}
		if (chartDate == null) {
			NumberFormat formatter2 = new DecimalFormat("00");
			chartDate = formatter2.format(day).concat(OzConstants.SLASH);
			chartDate = chartDate.concat(formatter2.format(month).concat(OzConstants.SLASH));
			chartDate = chartDate.concat(String.valueOf(year));
		}
		logger.finest(getString());
	}

	public final String getHhmm() {
		return hhmm;
	}

	public final Minute getMinute() {
		return new Minute(minute, hour, day, month, year);
	}

	public final String getString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.valueOf(day));
		sb.append(OzConstants.SLASH);
		sb.append(NumberUtils.format(month, NumberUtils.FORMATTER_00));
		sb.append(OzConstants.SLASH);
		sb.append(String.valueOf(year));
		sb.append(OzConstants.BLANK);
		sb.append(NumberUtils.format(hour, NumberUtils.FORMATTER_00));
		sb.append(OzConstants.COLON);
		sb.append(NumberUtils.format(minute, NumberUtils.FORMATTER_00));
		sb.append(OzConstants.BLANK);
		sb.append(String.valueOf(value));
		sb.append(OzConstants.BLANK);
		sb.append(String.valueOf(validElement));
		return sb.toString();
	}

	public final double getValue() {
		return value;
	}

	public final boolean isValidElement() {
		return validElement;
	}
}
