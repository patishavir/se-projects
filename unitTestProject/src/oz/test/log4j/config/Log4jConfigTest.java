package oz.test.log4j.config;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Log4jConfigTest {

	/**
	 * @param args
	 */
	private static Logger logger = Logger.getLogger("MAIN");

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File log4jConfigFile = new File(args[0]);
		if (!log4jConfigFile.exists()) {
			System.out.print(args[0] + " does not exist\n");
		} else {
			System.out.print(args[0] + " exists\n");
		}
		DOMConfigurator.configure(args[0]);
		logger.error("error");
		logger.warn("warn");
		logger.info("info");
		logger.debug("debug");

	}

}
