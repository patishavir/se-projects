package oz.test.infra;

import static org.junit.Assert.assertTrue;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import oz.infra.properties.PropertiesUtils;

public class TestPropertiesUtils {
	private static Logger logger = Logger.getLogger(TestPropertiesUtils.class.getName());
	private static final String propertiesFilePath = "C:\\oj\\projects\\unitTestProject\\files\\test.properties";

	@Test
	public final void TestPropertiesUtils() {
		Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		PropertiesUtils.printProperties(properties, Level.INFO);
		logger.info("Size: " + String.valueOf(properties.size()));
		assertTrue(properties != null);
		assertTrue(properties.size() == 5);
	}

}
