package oz.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackTest5 {

	private static Logger logger = LoggerFactory.getLogger(LogbackFileUtils.MY_LOGGER);

	public static void main(String[] args) {
		LogbackFileUtils.start("c:/temp/first.log");
		logger.info("1st file - This is an info message");
		logger.debug("1st file - This is a debug message");
		logger.error("1st file - This is an error message");
		LogbackFileUtils.stop();

		LogbackFileUtils.start("c:/temp/second.log");
		logger.info("2nd file - This is an info message");
		logger.debug("2nd file - This is a debug message");
		logger.error("2nd file - This is an error message");
		LogbackFileUtils.stop();

		logger.info("console only - This is an info message");
		logger.debug("console only - This is a debug message");
		logger.error("console only - This is an error message");
	}
}