package oz.stats.entry.test;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.stats.entry.StatsEntry;

public class StatsEntryTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String str1 = "statsName=Web Applications,server=b1,requestCount=5092211,maxServiceTime=300594,startTime=1504449666360,lastSampleTime=1505637834468";
		String str2 = "statsName=Web Applications,server=b1,requestCount=5092311,maxServiceTime=300594,startTime=1504449666360,lastSampleTime=1505637834468";
		testStatsEntry(str1);
		testStatsEntryDiff(str1, str2);
	}

	private static void testStatsEntry(final String str) {
		StatsEntry statsEntry = new StatsEntry(str);
		logger.info(str);
		logger.info(statsEntry.getAsString());
	}

	private static void testStatsEntryDiff(final String str1, final String str2) {
		StatsEntry statsEntry1 = new StatsEntry(str1);
		StatsEntry statsEntry2 = new StatsEntry(str2);
		StatsEntry statsEntry3 = statsEntry1.getDiffStatsEntry(statsEntry2);
		logger.info(str1);
		logger.info(statsEntry1.getAsString());
		logger.info(str2);
		logger.info(statsEntry2.getAsString());
		logger.info(statsEntry3.getAsString());
	}
}