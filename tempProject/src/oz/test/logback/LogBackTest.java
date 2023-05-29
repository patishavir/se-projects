package oz.test.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogBackTest {

	final static Logger logger = LoggerFactory.getLogger(LogBackTest.class);

	public static void main(String[] args) {
		logger.info("Entering application.");

		logger.info("Exiting application.");
	}

}
