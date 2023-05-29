package oz.test.log4j;

import org.apache.log4j.xml.DOMConfigurator;

public class Log4jXmlConfiguration {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		DOMConfigurator.configure(args[0]);
		org.apache.log4j.Logger logger = org.apache.log4j.Logger
				.getLogger("filenet_error");
		logger.error("test message");
	}

}
