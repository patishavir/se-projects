package oz.test.infra;

import java.util.logging.Logger;

import oz.infra.datetime.DateTimeUtils;
import oz.infra.logging.jul.JulUtils;

public class TestDateTimeUtil {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// logger.info(DateTimeUtils.formatDayInWeek(1, "yyy-MM-dd"));
		// testIsWithinTimeOfDayRange();
		testGetCalendarByHHMM();
	}

	private static void testIsWithinTimeOfDayRange() {
		isWithinTimeOfDayRange("0811", "0939");
		isWithinTimeOfDayRange("0811", null);
		isWithinTimeOfDayRange("1611", null);
		isWithinTimeOfDayRange("1527", "1541");
		isWithinTimeOfDayRange("1527", "1541");
		isWithinTimeOfDayRange(null, "1541");
		isWithinTimeOfDayRange(null, null);
		isWithinTimeOfDayRange("zzz1527", "1541");
	}

	private static void testGetCalendarByHHMM() {
		DateTimeUtils.getMillisTillNextHHMM("1555");
		DateTimeUtils.getMillisTillNextHHMM("1549");
		DateTimeUtils.getMillisTillNextHHMM("1755");
		DateTimeUtils.getMillisTillNextHHMM(null);
		DateTimeUtils.getMillisTillNextHHMM("0000");
		DateTimeUtils.getMillisTillNextHHMM("1600");
		DateTimeUtils.getMillisTillNextHHMM("1556");
		DateTimeUtils.getMillisTillNextHHMM(null);
		DateTimeUtils.getMillisTillNextHHMM("zzz1527");
	}

	private static void isWithinTimeOfDayRange(final String startingHHMM, final String endingHHMM) {
		boolean isWithinTimeRange = DateTimeUtils.isWithinTimeOfDayRange(startingHHMM, endingHHMM);
		logger.info("startingHHMM: " + startingHHMM + " endingHHMM: " + endingHHMM
				+ "  isWithinTimeRange: " + String.valueOf(isWithinTimeRange));
	}
}
