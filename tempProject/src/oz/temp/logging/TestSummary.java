package oz.temp.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.levels.SummaryLevel;

public class TestSummary {

	private static Level SUMMARY = SummaryLevel.SUMMARY;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Logger logger = JulUtils.getSummaryLogger(SUMMARY,
		// "c:\\temp\\summary.log");
		Logger logger = JulUtils.getSummaryLogger(null);
		logger.setLevel(SUMMARY);
		logger.log(SUMMARY, " babay baby on more");
		logger.log(Level.INFO, " babay baby on more");
	}

}
