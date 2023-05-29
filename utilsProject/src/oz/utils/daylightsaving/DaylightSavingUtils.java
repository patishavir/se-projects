package oz.utils.daylightsaving;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;

public class DaylightSavingUtils {
	private static StringBuilder sb = new StringBuilder();
	private static TimeZone tz = null;
	private static String dateString = null;
	private static String formatString = DateTimeUtils.DATE_FORMAT_dd_MM_yyyy;
	private static int loopIndex = 0;
	private static final int DAYS_TO_PROCESS = 500;
	private static Logger logger = JulUtils.getLogger();

	private static int daylightSavingOffsetTest(final int dateOffset) {
		Calendar cal = DateTimeUtils.getDateByOffset(dateOffset);
		dateString = DateTimeUtils.formatDate(cal.getTime(), formatString);
		int offset = daylightSavingOffsetTest(dateString);
		return offset;
	}

	private static int daylightSavingOffsetTest(final String dateString) {
		long millis = DateTimeUtils.getMillisFromTimeString(dateString, formatString);
		int tzOffset = tz.getOffset(millis);
		String message = StringUtils.concat("\n", String.valueOf(loopIndex), "  Date: ", dateString, "\toffset: ", String.valueOf(tzOffset));
		logger.finest(message);
		return tzOffset;
	}

	private static void init() {
		sb.append(OzConstants.LINE_FEED + SystemUtils.getRunInfo());
		sb.append("\njava version: ".concat(System.getProperty(SystemPropertiesUtils.SYSTEM_PROPERTY_JAVA_VERSION)));
		Calendar cal = Calendar.getInstance();
		tz = cal.getTimeZone();
		sb.append("\ntime zone: ".concat(tz.toString()));

	}

	private static void loop(final int numberOfDays) {
		int prevOffset = Integer.MIN_VALUE;
		for (loopIndex = 0; loopIndex < numberOfDays; loopIndex++) {
			int offset = daylightSavingOffsetTest(loopIndex);
			if (prevOffset == Integer.MIN_VALUE) {
				prevOffset = offset;
			} else {
				if (prevOffset != offset) {
					String changeMessage = SystemUtils.LINE_SEPARATOR + "Time zone offset has changed form " + String.valueOf(prevOffset) + " to "
							+ String.valueOf(offset) + " on " + dateString;
					sb.append(changeMessage);
					prevOffset = offset;
				}

			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		init();
		daylightSavingOffsetTest(DateTimeUtils.formatDate(formatString));
		loop(DAYS_TO_PROCESS);
		logger.info(sb.toString());

	}

	private static void processArgs(String[] args) {
		for (String dateString : args) {
			daylightSavingOffsetTest(dateString);
		}

	}
}
