package oz.infra.logging.log4j;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import oz.infra.properties.PropertiesUtils;

public class Log4jUtils {
	private static final Logger logger = org.apache.log4j.Logger.getLogger(Log4jUtils.class);

	public static void configure(final String propertiesFilePath) {
		Properties log4jProperties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		PropertyConfigurator.configure(log4jProperties);
		logger.info("done");

	}

}
