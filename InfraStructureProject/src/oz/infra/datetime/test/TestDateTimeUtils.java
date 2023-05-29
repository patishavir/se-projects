package oz.infra.datetime.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.thread.ThreadUtils;

public class TestDateTimeUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// testGetDifferenceInSeconds();
		// testDateTimeUtils();
		// testGetDayOfWeek();
		// logger.info(DateTimeUtils.formatCurrentTime(DateTimeUtils.DATE_FORMAT_ddMM_HHmmss));
		// logger.info(DateTimeUtils.formatCurrentTime(DateTimeUtils.DATE_FORMAT_HHmmss));
		// testGetDayOfYear();
		// testGetWeekdayDatesWithinPeriod();
		// testStuff();
		// testTimeString2MilliSeconds();
		// testFormatTime();
		// testDateTimeUtils();
		// testGetTodStamp();
		testGetDateByOffset();
	}

	private static void testDateTimeUtils() {
		logger.info(DateTimeUtils.getCurrentDate(DateTimeUtils.DATE_FORMAT_ddMMyyyy_HHmmss));
		logger.info(DateTimeUtils.getCurrentTime());
		logger.info(String.valueOf(DateTimeUtils.getCurrentDayInWeek()));
		logger.info(String.valueOf(Calendar.SUNDAY));
		logger.info(String.valueOf(Calendar.SATURDAY));
		DateTimeUtils.subtractDates("01:02:03", "01:02:02", DateTimeUtils.DATE_FORMAT_HHmmss);
	}

	private static void testFormatTime() {
		logger.info(DateTimeUtils.formatTime(1002, DateTimeUtils.DATE_FORMAT_HHmmssCommaSSS));
		logger.info(DateTimeUtils.formatTime(3600002, DateTimeUtils.DATE_FORMAT_HHmmssCommaSSS));
	}

	private static void testGetDayOfWeek() {
		logger.info(String.valueOf(DateTimeUtils.getDayOfWeek()));
		logger.info(String.valueOf(Calendar.MONDAY));
	}

	private static void testGetDayOfYear() {
		DateTimeUtils.getDayOfYear(2012, 3, 30);
		DateTimeUtils.getDayOfYear(2012, 9, 23);
	}

	private static void testGetDateByOffset() {
		Calendar cal = DateTimeUtils.getDateByOffset(-1);
		logger.info(cal.getTime().toString());
	}

	private static void testGetDifferenceInSeconds() {
		long m1 = System.currentTimeMillis();
		ThreadUtils.sleep(1000, Level.INFO);
		DateTimeUtils.getDifferenceInSeconds(m1);
		ThreadUtils.sleep(1000, Level.INFO);
		DateTimeUtils.getDifferenceInSeconds(m1);
		ThreadUtils.sleep(1000, Level.INFO);
		DateTimeUtils.getDifferenceInSeconds(m1);
		ThreadUtils.sleep(1000, Level.INFO);
		DateTimeUtils.getDifferenceInSeconds(m1);
		ThreadUtils.sleep(1000, Level.INFO);
		DateTimeUtils.getDifferenceInSeconds(m1);
	}

	private static void testGetTodStamp() {
		logger.info(DateTimeUtils.getTodStamp());
	}

	private static void testGetWeekdayDatesWithinPeriod() {
		Calendar beginCalendar = Calendar.getInstance();
		// initialize the date to the first day the desired year and month
		// Below example initializes to 1st of December 2011
		// beginCalendar.set(2013, Calendar.JANUARY, 4);
		beginCalendar.set(2013, Calendar.JANUARY, 1);
		Calendar endCalendar = Calendar.getInstance();
		// set the end date to the end of the month using the begin date
		// endCalendar.set(beginCalendar.get(Calendar.YEAR), (Calendar.MARCH),
		// 29);
		endCalendar.set(beginCalendar.get(Calendar.YEAR), (Calendar.DECEMBER), 31);
		int weekday = Calendar.FRIDAY;
		List<Calendar> calendarList = DateTimeUtils.getWeekdayDatesWithinPeriod(beginCalendar, endCalendar, weekday);
		for (Calendar calendar : calendarList) {
			logger.info(DateTimeUtils.formatCalendar(calendar, DateTimeUtils.DATE_FORMAT_dd_MM_yyyy));
		}
	}

	private static void testStuff() {
		String hebrewMonth = "מאי";
		logger.info(hebrewMonth + "=" + DateTimeUtils.getGregorianMonthNumFromHebrewName(hebrewMonth));
		hebrewMonth = "דצמבר";
		logger.info(hebrewMonth + "=" + DateTimeUtils.getGregorianMonthNumFromHebrewName(hebrewMonth));
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
		String formattedDate = simpleDateFormat.format(new Date());
		FileUtils.writeFile("c:\\temp\\1.txt", formattedDate);
		DateTimeUtils.getPreviousDate();
		logger.info(DateTimeUtils.formatCurrentTime(DateTimeUtils.DATE_FORMAT_yyyy_MMMMMM_dd));
		logger.info(DateTimeUtils.formatCurrentTime(DateTimeUtils.DATE_FORMAT_dd_MMMMMM_yyyy));
		logger.info(DateTimeUtils.formatCurrentTime(DateTimeUtils.DATE_FORMAT_yyMMddHHmmssZ));
		logger.info(DateTimeUtils.getCurrentTime());
	}

	private static void testTimeString2MilliSeconds() {
		DateTimeUtils.getMillisFromTimeString("00:00:01,002", DateTimeUtils.DATE_FORMAT_HHmmssCommaSSS);
		DateTimeUtils.getMillisFromTimeString("00:00:03.004");
		DateTimeUtils.getMillisFromTimeString("00:01:05.006");
		DateTimeUtils.getMillisFromTimeString("01:02:03.004");
		DateTimeUtils.getMillisFromTimeString("00:00:03,004");
	}
}
