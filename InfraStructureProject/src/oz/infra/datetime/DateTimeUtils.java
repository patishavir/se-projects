/*
 * Created on 06/12/2004 @author Oded
 */
package oz.infra.datetime;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public final class DateTimeUtils {
	public static final String DATE_FORMAT_HHmmss = "HH:mm:ss";
	public static final String DATE_FORMAT_TOD_STAMP = "HH.mm.ss";
	public static final String DATE_FORMAT_mmss = "mm:ss";
	public static final String DATE_FORMAT_HHmmssCommaSSS = "HH:mm:ss,SSS";
	public static final String DATE_FORMAT_HHmmssDotSSS = "HH:mm:ss.SSS";
	public static final String DATE_FORMAT_dd_MM_yyyy = "dd/MM/yyyy";
	public static final String DATE_FORMAT_yyyy_MM_dd = "yyyy/MM/dd";
	public static final String DATE_FORMAT_yyMMddHHmmssZ = "yyMMddHHmmssZ";
	public static final String DATE_FORMAT_ddMMyyyy_HHmmss = "dd/MM/yyyy HH:mm:ss";
	public static final String DATE_FORMAT_ddMM_HHmmss = "dd/MM HH:mm:ss";
	public static final String DATE_FORMAT_yyyyMMdd_HHmmss = "yyyy.MM.dd_HH.mm.ss";
	public static final String DATE_FORMAT_yyyyMMdd_HHmmssSSS = "yyyy.MM.dd_HH.mm.ss.SSS";
	public static final String DATE_FORMAT_yyyyMMdd = "yyyyMMdd";
	public static final String DATE_FORMAT_ddMMyyyy_HHmmssSSS = "dd/MM/yyyy HH:mm:ss.SSS";
	public static final String DATE_FORMAT_ddMMyyyy_HHmmss_SSS = "dd/MM/yyyy HH:mm:ss_SSS";
	public static final String DATE_FORMAT_yyyy_MMMMMM_dd = "yyyy-MMMMMM-dd";
	public static final String DATE_FORMAT_dd_MMMMMM_yyyy = "dd-MMMMMM-yyyy";
	public static final String DATE_FORMAT_dd_MMM_yy = "dd-MMM-yy";
	private static final SimpleDateFormat ddMMyyyy_HHmmss_formatter = new SimpleDateFormat(DATE_FORMAT_ddMMyyyy_HHmmss);
	private static final String UTC = "UTC";
	private static Hashtable<String, String> gregorianMonthes = new Hashtable<String, String>();
	private static Properties hebrewMonthes = null;
	public static final long MILLIS_IN_A_DAY = 60L * 60L * 1000L * 24L;
	public static final long MILLIS_IN_AN_HOUR = 60L * 60L * 1000L;
	private static Logger logger = JulUtils.getLogger();
	static {
		hebrewMonthes = loadHebrewMonthesProperties();
		//
		gregorianMonthes.put("Jan", "01");
		gregorianMonthes.put("Feb", "02");
		gregorianMonthes.put("Mar", "03");
		gregorianMonthes.put("Apr", "04");
		gregorianMonthes.put("May", "05");
		gregorianMonthes.put("Jun", "06");
		gregorianMonthes.put("Jul", "07");
		gregorianMonthes.put("Aug", "08");
		gregorianMonthes.put("Sep", "09");
		gregorianMonthes.put("Oct", "10");
		gregorianMonthes.put("Nov", "11");
		gregorianMonthes.put("Dec", "12");
	}

	public static long convertMiliSecondsToDays(final long milliSeconds) {
		long hours = milliSeconds / MILLIS_IN_A_DAY;
		return hours;
	}

	public static String formatCalendar(final Calendar calendar, final String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(calendar.getTime());
	}

	public static String formatCurrentTime() {
		long utcTimeInMilliSeconds = System.currentTimeMillis();
		long localTimeInMilliSeconds = getLocalTimeMillis(utcTimeInMilliSeconds);
		return formatTime(localTimeInMilliSeconds % MILLIS_IN_A_DAY);
	}

	public static String formatCurrentTime(final String format) {
		return formatDate(System.currentTimeMillis(), format);
	}

	public static String formatDateTime() {
		String format = DATE_FORMAT_ddMMyyyy_HHmmssSSS;
		return formatDate(format);
	}

	public static String formatDate() {
		String format = DATE_FORMAT_yyyyMMdd;
		return formatDate(format);
	}

	public static String formatDate(final Date date2Format, final String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date2Format);
	}

	public static String formatDate(final long dateParamaeter, final String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String datenewformat = formatter.format(dateParamaeter);
		return datenewformat;
	}

	public static String formatDate(final String format) {
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(today);
	}

	public static String formatDayInWeek(final int dayInWeek, final String dateFormatString) {
		String firstDayInWeekString = formatDate(getDateOfDayInWeek(dayInWeek), dateFormatString);
		logger.finest(firstDayInWeekString);
		return firstDayInWeekString;
	}

	public static String formatTime(final long timeInMilliSeconds) {
		long milliSeconds = timeInMilliSeconds % OzConstants.INT_1000;
		long totalSeconds = timeInMilliSeconds / OzConstants.INT_1000;
		long seconds = totalSeconds % OzConstants.INT_60;
		long totalMinutes = totalSeconds / OzConstants.INT_60;
		long minutes = totalMinutes % OzConstants.INT_60;
		long hours = totalMinutes / OzConstants.INT_60;
		return formatTime(hours, minutes, seconds, milliSeconds);
	}

	public static String formatTime(final long hours, final long minutes, final long seconds, final long milliSeconds) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		nf.setMaximumIntegerDigits(2);
		String formattedTimeString = nf.format(hours) + ":" + nf.format(minutes) + ":" + nf.format(seconds) + "."
				+ milliSeconds;
		return formattedTimeString;
	}

	public static String formatTime(final long milliSeconds, final String formatString) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		sdf.setTimeZone(TimeZone.getTimeZone(UTC));
		return sdf.format(milliSeconds);
	}

	public static String getCurrentDate(final String dateFormat) {
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String currentDate = formatter.format(today);
		return currentDate;
	}

	public static int getCurrentDayInWeek() {
		return GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}

	public static String getCurrentTime() {
		return DateFormat.getTimeInstance().format(new Date());
	}

	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static Date getDateOfDayInWeek(final int dayInWeek) {
		Calendar myCalendar = GregorianCalendar.getInstance();
		int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);
		myCalendar.add(Calendar.DATE, dayInWeek - dayOfWeek);
		Date firstDayInWeek = myCalendar.getTime();
		return firstDayInWeek;
	}

	public static int getDayOfWeek() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static int getDayOfYear(final int year, final int month, final int dayOfMonth) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		// month is zero based
		cal.set(year, month - 1, dayOfMonth);
		int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		logger.info("year:" + String.valueOf(cal.get(Calendar.YEAR)) + " month:"
				+ String.valueOf(cal.get(Calendar.MONTH)) + " day:" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH))
				+ " day of year:" + String.valueOf(dayOfYear));
		return dayOfYear;

	}

	public static long getDifferenceInSeconds(final long previousTimeMillis) {
		long currentTimeMillis = System.currentTimeMillis();
		long diffInSeconds = (currentTimeMillis - previousTimeMillis) / OzConstants.INT_1000;
		if (logger.isLoggable(Level.FINEST)) {
			logger.info("\npreviousTimeMillis: " + String.valueOf(previousTimeMillis) + " currentTimeMillis: "
					+ String.valueOf(currentTimeMillis) + " diffInSeconds: " + String.valueOf(diffInSeconds));
		}
		return diffInSeconds;
	}

	public static String getGregorianMonthNumFromHebrewName(final String hebrewMonthName) {
		logger.finest(hebrewMonthName);
		return (String) hebrewMonthes.get(hebrewMonthName);
	}

	public static String getGregorianMonthNumFromName(final String gregorianMonthName) {
		logger.finest(gregorianMonthName);
		return (String) gregorianMonthes.get(gregorianMonthName);
	}

	public static String getLocalTime(final String utcDateTimeString, final String formatString) {
		SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss.s");
		Date myDateTime = null;
		try {
			myDateTime = sdf.parse(utcDateTimeString);
		} catch (ParseException exception) {
			exception.printStackTrace();
			logger.warning(exception.getMessage());
		}
		int myTimeZoneOffset = sdf.getTimeZone().getOffset(myDateTime.getTime());
		Date localDateTime = new Date(myDateTime.getTime() + myTimeZoneOffset);
		return formatDate(localDateTime.getTime(), formatString);
	}

	public static long getLocalTimeMillis() {
		long utcTimeInMilliSeconds = System.currentTimeMillis();
		TimeZone defaultTimeZone = TimeZone.getDefault();
		int myTimeZoneOffset = defaultTimeZone.getOffset(utcTimeInMilliSeconds);
		return utcTimeInMilliSeconds + myTimeZoneOffset;
		// System.currentTimeMillis();
	}

	public static long getLocalTimeMillis(final long utcTimeInMilliSeconds) {
		TimeZone defaultTimeZone = TimeZone.getDefault();
		int myTimeZoneOffset = defaultTimeZone.getOffset(utcTimeInMilliSeconds);
		return utcTimeInMilliSeconds + myTimeZoneOffset;
		// System.currentTimeMillis();
	}

	public static long getLocalTimeMillis(final String utcDateTimeString, final String formatString) {
		SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss.s");
		Date myDateTime = null;
		try {
			myDateTime = sdf.parse(utcDateTimeString);
		} catch (ParseException exception) {
			exception.printStackTrace();
			logger.warning(exception.getMessage());
		}
		int myTimeZoneOffset = sdf.getTimeZone().getOffset(myDateTime.getTime());
		Date localDateTime = new Date(myDateTime.getTime() + myTimeZoneOffset);
		return localDateTime.getTime();
	}

	public static long getMillisTillNextHHMM(final String hhmm) {
		long millisTillNextHHMM = Long.MIN_VALUE;
		StringBuilder stringBuilder = new StringBuilder();
		if (hhmm != null) {
			try {
				Calendar calendarNow = new GregorianCalendar();
				stringBuilder.append("DAY_IN_MILLIS: " + String.valueOf(MILLIS_IN_A_DAY));
				stringBuilder.append(" calendarNow: " + ddMMyyyy_HHmmss_formatter.format(calendarNow.getTime()));
				Calendar calendarHHMM = new GregorianCalendar(calendarNow.get(Calendar.YEAR),
						calendarNow.get(Calendar.MONTH), calendarNow.get(Calendar.DAY_OF_MONTH),
						Integer.parseInt(hhmm.substring(0, 2)), Integer.parseInt(hhmm.substring(2)));
				stringBuilder.append(" calendarHHMM:" + ddMMyyyy_HHmmss_formatter.format(calendarHHMM.getTime()));
				millisTillNextHHMM = calendarHHMM.getTimeInMillis() - calendarNow.getTimeInMillis();
				stringBuilder.append(" millisTillNextHHMM: " + String.valueOf(millisTillNextHHMM));
				if (millisTillNextHHMM < 0) {
					millisTillNextHHMM += MILLIS_IN_A_DAY;
					stringBuilder.append(" 1 day hase been added: " + String.valueOf(+millisTillNextHHMM));
				}
			} catch (Exception ex) {
				stringBuilder.append(ex.getMessage());
				ex.printStackTrace();
			}
		}
		stringBuilder.append(" millisTillNextHHMM: " + formatTime(millisTillNextHHMM));
		logger.finest(stringBuilder.toString());
		return millisTillNextHHMM;
	}

	public static Calendar getPreviousDate() {
		return getDateByOffset(-1);
	}

	public static Calendar getDateByOffset(final int offset) {
		Calendar currentCal = new GregorianCalendar();
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, offset);
		logger.finest(StringUtils.concat("current time: ", currentCal.getTime().toString(), " offset=",
				String.valueOf(offset), " calculated time: ", cal.getTime().toString()));
		return cal;
	}

	public static String getTimeStamp() {
		return formatDate(DATE_FORMAT_yyyyMMdd_HHmmss);
	}

	public static String getMilliSecsTimeStamp() {
		return formatDate(DATE_FORMAT_yyyyMMdd_HHmmssSSS);
	}

	public static String getTodStamp() {
		return formatDate(DATE_FORMAT_TOD_STAMP);
	}

	public static String getUTCString(final String localDateTimeString, final String formatString) {
		SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
		Date myDateTime = null;
		try {
			myDateTime = sdf.parse(localDateTimeString);
		} catch (ParseException exception) {
			exception.printStackTrace();
			logger.warning(exception.getMessage());
		}
		int myTimeZoneOffset = sdf.getTimeZone().getOffset(myDateTime.getTime());
		Date utcDateTime = new Date(myDateTime.getTime() - myTimeZoneOffset);
		return formatDate(utcDateTime.getTime(), formatString);
	}

	public static List<Calendar> getWeekdayDatesWithinPeriod(final Calendar beginCalendar, final Calendar endCalendar,
			final int weekday) {
		Calendar runningCalendar = (Calendar) beginCalendar.clone();
		StringBuilder sb = new StringBuilder();
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("beginCalendar: ");
		sb.append(beginCalendar.getTime().toString());
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("endCalendar: ");
		sb.append(endCalendar.getTime().toString());
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("runningCalendar: ");
		sb.append(runningCalendar.getTime().toString());
		sb.append(SystemUtils.LINE_SEPARATOR);
		logger.info(sb.toString());

		while (runningCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
			runningCalendar.add(Calendar.DATE, 1);
		}
		// loop from the first friday of the month till the end of month and add
		// 1 week in each iteration.
		List<Calendar> calendarList = new ArrayList<Calendar>();
		while (runningCalendar.compareTo(endCalendar) <= 0) {
			calendarList.add((Calendar) runningCalendar.clone());
			logger.info(formatDate(runningCalendar.getTime(), DATE_FORMAT_dd_MM_yyyy));
			runningCalendar.add(Calendar.WEEK_OF_YEAR, 1);
		}
		return calendarList;
	}

	public static boolean isWithinTimeOfDayRange(final String startTimeHHMM, final String endTimeHHMM) {
		StringBuilder stringBuilder = new StringBuilder();
		boolean isWithinTimeOfDayRange = false;
		long startInMillis = Long.MIN_VALUE;
		long endInMillis = Long.MAX_VALUE;
		try {
			Calendar calendarNow = new GregorianCalendar();
			stringBuilder.append("now: " + ddMMyyyy_HHmmss_formatter.format(calendarNow.getTime()));
			if (startTimeHHMM != null) {
				Calendar calendarStart = new GregorianCalendar(calendarNow.get(Calendar.YEAR),
						calendarNow.get(Calendar.MONTH), calendarNow.get(Calendar.DAY_OF_MONTH),
						Integer.parseInt(startTimeHHMM.substring(0, 2)), Integer.parseInt(startTimeHHMM.substring(2)));
				startInMillis = calendarStart.getTimeInMillis();
				stringBuilder.append(" start time: " + ddMMyyyy_HHmmss_formatter.format(calendarStart.getTime()));
			}
			if (endTimeHHMM != null) {
				Calendar calendarEnd = new GregorianCalendar(calendarNow.get(Calendar.YEAR),
						calendarNow.get(Calendar.MONTH), calendarNow.get(Calendar.DAY_OF_MONTH),
						Integer.parseInt(endTimeHHMM.substring(0, 2)), Integer.parseInt(endTimeHHMM.substring(2)));
				endInMillis = calendarEnd.getTimeInMillis();
				stringBuilder.append(" End time: " + ddMMyyyy_HHmmss_formatter.format(calendarEnd.getTime()));
			}
			isWithinTimeOfDayRange = (calendarNow.getTimeInMillis() > startInMillis)
					&& (calendarNow.getTimeInMillis() < endInMillis);
		} catch (Exception ex) {
			ex.printStackTrace();
			stringBuilder.append(" Message: " + ex.getMessage());
		}
		stringBuilder.append(" isWithinTimeOfDayRange: " + String.valueOf(isWithinTimeOfDayRange));
		logger.finest(stringBuilder.toString());
		return isWithinTimeOfDayRange;
	}

	@Deprecated
	public static boolean isWithinTimeOfDayRange(final String startTimeHHMM, final String endTimeHHMM,
			final boolean unused) {
		boolean isWithinTimeOfDayRange = false;
		int startTimeInt = Integer.MIN_VALUE;
		int endTimeInt = Integer.MAX_VALUE;
		if (startTimeHHMM != null) {
			String startTimeHH = startTimeHHMM.substring(0, 2);
			String startTimeMM = startTimeHHMM.substring(2, OzConstants.INT_4);
			startTimeInt = (Integer.parseInt(startTimeHH) * OzConstants.INT_60) + Integer.parseInt(startTimeMM);
			logger.info("startTimeHH: " + startTimeHH + " startTimeMM: " + startTimeMM + " startTimeInt: "
					+ String.valueOf(startTimeInt));
		}
		if (endTimeHHMM != null) {
			String endTimeHH = endTimeHHMM.substring(0, 2);
			String endTimeMM = endTimeHHMM.substring(2, OzConstants.INT_4);
			endTimeInt = (Integer.parseInt(endTimeHH) * OzConstants.INT_60) + Integer.parseInt(endTimeMM);
			logger.info("endTimeHH: " + endTimeHH + " endTimeMM: " + endTimeMM + " endTimeInt: "
					+ String.valueOf(endTimeInt));
		}
		if (logger.isLoggable(Level.FINEST)) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("startTimeHHMM: " + startTimeHHMM + " startTimeInt: " + String.valueOf(startTimeInt));
			stringBuilder.append(" endTimeHHMM: " + endTimeHHMM + " endTimeInt: " + String.valueOf(endTimeInt));
			logger.info(stringBuilder.toString());
		}
		Calendar rightNow = Calendar.getInstance();
		int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
		int currentMinute = rightNow.get(Calendar.MINUTE);
		int rightNowInt = (currentHour * OzConstants.INT_60) + currentMinute;
		if (rightNowInt >= startTimeInt && rightNowInt <= endTimeInt) {
			isWithinTimeOfDayRange = true;
		}
		return isWithinTimeOfDayRange;
	}

	private static Properties loadHebrewMonthesProperties() {
		hebrewMonthes = PropertiesUtils.loadPropertiesFromClassPathWithInPackage(DateTimeUtils.class,
				"hebrewMonthes.properties");
		logger.finest(PropertiesUtils.getAsDelimitedString(hebrewMonthes));
		return hebrewMonthes;
	}

	public static long subtractDates(final String dateString1, final String dateString0, final String dateFormat) {
		long timeDifference = OzConstants.INT_0;
		if (dateString1 != null && dateString0 != null) {
			try {
				DateFormat sdf = new SimpleDateFormat(dateFormat);
				Date date1 = sdf.parse(dateString1);
				Date date0 = sdf.parse(dateString0);
				Calendar cal1 = Calendar.getInstance();
				cal1.setTime(date1);
				Calendar cal0 = Calendar.getInstance();
				cal0.setTime(date0);
				timeDifference = cal1.getTimeInMillis() - cal0.getTimeInMillis();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logger.finest("dateString1: " + dateString1 + " dateString0: " + dateString0 + " timeDifference: "
					+ timeDifference);
		}
		return timeDifference;

	}

	public static long getMillisFromTimeString(final String timeString, final String... formatString) {
		String myFormatString = DATE_FORMAT_HHmmssDotSSS;
		if (formatString.length > 0) {
			myFormatString = formatString[0];
		}
		SimpleDateFormat sdf = new SimpleDateFormat(myFormatString);
		sdf.setTimeZone(TimeZone.getTimeZone(UTC));
		Calendar cal = Calendar.getInstance();
		long milliSeconds = Long.MIN_VALUE;
		try {
			cal.setTime(sdf.parse(timeString));
			milliSeconds = (cal.getTimeInMillis());
			logger.finest(String.valueOf(milliSeconds));
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return milliSeconds;
	}

	private DateTimeUtils() {
	}
}