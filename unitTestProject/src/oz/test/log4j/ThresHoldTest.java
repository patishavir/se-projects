package oz.test.log4j;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

;
public class ThresHoldTest {
	private static Logger logger = Logger.getLogger(ThresHoldTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DOMConfigurator.configure(args[0]);
		logger.debug("starting debug");
		logger.info("starting info");
		logger.warn("starting warn");
		logger.fatal("starting fatal");

	}

}
