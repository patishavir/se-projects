package oz.infra.properties.test;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;

public class TestPropertisUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// testloadPropertiesFile();
		// PropertiesUtils.printSystemProperties();
		// testUpdatePropertiesUsingEnvironmentVarialbes();
		// testSubstitutePropertiesValues();
		// testCloneProperties();
		// testLoadPropertiesFromClassPathWithInPackage();
		// testPrintProperties();
		testDuplicateProperties();
	}

	private static Properties getTestProperties() {
		Properties properties = new Properties();
		properties.put("key1", "value1");
		properties.put("key2", "value2");
		properties.put("key3", "value3");
		properties.put("key4", "value4");
		properties.put("key5", "value5");
		properties.put("keykey", "valuevalueKey");
		return properties;
	}

	private static void testCloneProperties() {
		Properties properties = new Properties();
		properties.put("COMPUTERNAME", "zvl");
		properties.put("XXXCOMPUTERNAME", "XXXzvl");
		properties.put("YYYXXXCOMPUTERNAME", "YYYXXXzvl");
		Properties clonedProperties = (Properties) properties.clone();
		PropertiesUtils.printProperties(clonedProperties, Level.INFO);

	}

	private static void testDuplicateProperties() {
		Properties properties = getTestProperties();
		Properties dup = PropertiesUtils.duplicate(properties);
		PropertiesUtils.printProperties(properties, "\t", Level.INFO);
		PropertiesUtils.printProperties(dup, "\t", Level.INFO);
	}

	private static void testloadPropertiesFile() {
		String propertiesFilePath = ".\\files\\settings.properties";
		Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath, OzConstants.WIN1255_ENCODING);
		PropertiesUtils.printProperties(properties, Level.INFO);
	}

	private static void testLoadPropertiesFromClassPathWithInPackage() {
		// int x = DateTimeUtils.getDayOfWeek();
		Properties props = PropertiesUtils.loadPropertiesFromClassPathWithInPackage(DateTimeUtils.class,
				"hebrewMonthes.properties");
		PropertiesUtils.printProperties(props);
	}

	private static void testPrintProperties() {
		Properties properties = new Properties();
		properties.put("key1", "value1");
		properties.put("key2", "value2");
		properties.put("key3", "value3");
		properties.put("key4", "value4");
		PropertiesUtils.printProperties(properties, "\t", Level.INFO);
	}

	public static void testSubstitutePropertiesValues() {
		Properties properties = new Properties();
		properties.put("COMPUTERNAME", "zvl");
		properties.put("XXXCOMPUTERNAME", "XXXzvl");
		PropertiesUtils.setPropertiesValuesUsingEnvironmentVariables(properties, Level.SEVERE);
	}

	private static void testUpdatePropertiesUsingEnvironmentVarialbes() {
		Properties properties = new Properties();
		properties.put("key1", "value1");
		properties.put("key2", "value2");
		properties.put("key3", "value3");
		properties.put("key4", "value4");
		PropertiesUtils.updatePropertiesUsingEnvironmentVarialbes(properties);
		logger.info(PropertiesUtils.printProperties(properties, Level.INFO));
	}
}
