package oz.test.infra;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import oz.infra.datetime.DateTimeUtils;
import oz.infra.logging.jul.JulUtils;

public class TestDateTimeUtil1 {
	private static Logger logger = JulUtils.getLogger();
	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			DateTimeUtils.DATE_FORMAT_ddMMyyyy_HHmmss);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String startTimeHHMM = "0730";
		String endTimeHHMM = "1719";
		DateTimeUtils.isWithinTimeOfDayRange(startTimeHHMM, endTimeHHMM);
		// Calendar calendarNow = new GregorianCalendar();
		// logger.info(formatter.format(calendarNow.getTime()));
		// Calendar calendarStart = getCalendarByHHMM(startHHMM);
		// Calendar calendarEnd = getCalendarByHHMM(endHHMM);
	}

	private static Calendar getCalendarByHHMM(final String hhmm) {
		Calendar calendar;
		try {
			calendar = new GregorianCalendar();
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hhmm.substring(0, 2)));
			calendar.set(Calendar.MINUTE, Integer.parseInt(hhmm.substring(2)));
			logger.info(formatter.format(calendar.getTime()));

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage() + " ***********************  ");
			calendar = null;
		}
		return calendar;
	}
}
