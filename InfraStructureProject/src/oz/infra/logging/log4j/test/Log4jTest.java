package oz.infra.logging.log4j.test;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

public class Log4jTest {

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("naal_y", "yamin");
		System.setProperty("naal_s", "small");
		testPropertyConfigurator();
		// testDOMConfigurator();
	}

	private static void testPropertyConfigurator() {
		System.out.println(new File(".").getAbsolutePath());
		Logger logger = Logger.getLogger("testPropertyConfigurator");
		PropertyConfigurator
				.configure("src/oz/infra/logging/log4j/test/config/log4j.properties");
		logger.info("Test PropertyConfigurator");
		logger.info("123...");
	}

	private static void testDOMConfigurator() {
		Logger logger = Logger.getLogger("testDOMConfigurator");
		DOMConfigurator
				.configure("src/oz/infra/logging/log4j/test/config/log4j.xml");
		logger.info("Test DOMConfigurator");
	}
}
