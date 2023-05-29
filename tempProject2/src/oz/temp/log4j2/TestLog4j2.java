package oz.temp.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestLog4j2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Logger logger = LogManager.getRootLogger();
		logger.info("Configuration File Defined To Be : " + System.getProperty("log4j.configurationFile"));
		logger.info("have a good one");
		logger.warn("warning: be careful");
	}
}
