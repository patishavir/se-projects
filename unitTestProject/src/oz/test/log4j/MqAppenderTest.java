package oz.test.log4j;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MqAppenderTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PropertyConfigurator.configure(args[0]);
		Logger logger = Logger.getLogger(MqAppenderTest.class);
		logger.info("starting main");
	}
}
