package oz.infra.datetime.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.datetime.StopWatch;
import oz.infra.logging.jul.JulUtils;
import oz.infra.thread.ThreadUtils;

public class TestStopWatch {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testStopWatch();
	}

	private static void testStopWatch() {
		StopWatch stopWatch = new StopWatch();
		ThreadUtils.sleep(1000, Level.INFO);
		logger.info(stopWatch.getElapsedTimeString());
		//
		StopWatch stopWatch2 = new StopWatch();
		ThreadUtils.sleep(1000, Level.INFO);
		logger.info(stopWatch2.appendElapsedTimeToMessage("baba black sheep "));
		//
		StopWatch stopWatch3 = new StopWatch();
		ThreadUtils.sleep(1000, Level.INFO);
		stopWatch3.logElapsedTimeMessage("wheels on the log");
		stopWatch3.logElapsedTimeMessage();
		//
		// StopWatch stopWatch1 = new StopWatch();
		// ThreadUtils.sleep(1000, Level.INFO);
		// stopWatch1.logElapsedTimeMessage("test 1000");
		// ThreadUtils.sleep(2000, Level.INFO);
		// stopWatch1.logElapsedTimeMessage("test 2000");
	}
}
