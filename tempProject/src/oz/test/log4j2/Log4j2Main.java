package oz.test.log4j2;

import org.apache.logging.log4j.LogManager;
//Import log4j classes.
import org.apache.logging.log4j.Logger;

public class Log4j2Main {
	private static boolean condition = true;
	// Define a static logger variable so that it references the
	// Logger instance named "MyApp".
	private static final Logger logger = LogManager.getLogger(Log4j2Main.class);

	public static void main(final String... args) {

		// Set up a simple configuration that logs on the console.

		logger.trace("Entering application.");
		if (condition) {
			logger.error("Didn't do it.");
		}
		logger.trace("Exiting application.");
	}
}