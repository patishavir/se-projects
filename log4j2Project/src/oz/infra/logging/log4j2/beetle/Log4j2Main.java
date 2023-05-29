package oz.infra.logging.log4j2.beetle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Logger logger = LogManager.getRootLogger();
    	logger.trace("Configuration File Defined To Be :: "+System.getProperty("log4j.configurationFile"));
    	System.out.println("Configuration File Defined To Be :: "+System.getProperty("log4j.configurationFile"));
    	logger.info("Hey, I am logging !!!");
    	logger.trace("Hey, I am tracing !!!");
	}
}
