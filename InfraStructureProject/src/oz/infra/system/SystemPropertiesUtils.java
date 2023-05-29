package oz.infra.system;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;
import oz.infra.properties.PropertiesUtils;

public class SystemPropertiesUtils {

	public static final String SYSTEM_PROPERTY_JAVA_CLASS_PASS = "java.class.path";
	public static final String SYSTEM_PROPERTY_JAVA_HOME = "java.home";
	public static final String SYSTEM_PROPERTY_JAVA_IO_TMPDIR = "java.io.tmpdir";
	public static final String SYSTEM_PROPERTY_JAVA_VERSION = "java.version";
	public static final String SYSTEM_PROPERTY_LINE_SEPARATOR = "line.separator";
	public static final String SYSTEM_PROPERTY_OS_NAME = "os.name";
	public static final String SYSTEM_PROPERTY_USER_DIR = "user.dir";
	public static final String SYSTEM_PROPERTY_USER_HOME = "user.home";
	public static final String SYSTEM_PROPERTY_USER_NAME = "user.name";

	public static final String SYSTEM_PROPERTY_PATH_SEPARATOR = "path.separator";
	public static final String SYSTEM_PROPERTY_TRUST_STORE = "javax.net.ssl.trustStore";
	public static final String SYSTEM_PROPERTY_TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";

	private static Logger logger = JulUtils.getLogger();

	public static String getJavaClassPath() {
		return System.getProperty(SYSTEM_PROPERTY_JAVA_CLASS_PASS);
	}

	public static String getJavaHome() {
		return System.getProperty(SYSTEM_PROPERTY_JAVA_HOME);
	}

	public static String getJavaVersion() {
		return System.getProperty(SYSTEM_PROPERTY_JAVA_VERSION);
	}

	public static String getOsName() {
		// Windows XP
		// AIX
		return System.getProperty(SYSTEM_PROPERTY_OS_NAME);
	}

	public static Properties getSystemProperties() {
		return System.getProperties();
	}

	public static String getTempDir() {
		return System.getProperty(SYSTEM_PROPERTY_JAVA_IO_TMPDIR);
	}

	public static String getUserDir() {
		return System.getProperty(SYSTEM_PROPERTY_USER_DIR);
	}

	public static String getUserName() {
		Properties systemProperties = getSystemProperties();
		String userName = systemProperties.getProperty(SYSTEM_PROPERTY_USER_NAME);
		logger.finest("userName: " + userName);
		return userName;
	}

	public static String printSystemProperties(final String... prefix) {
		String startMessage1 = "start of system properites listing";
		if (prefix.length > 0) {
			startMessage1 = startMessage1 + " with prefix " + prefix[0];
		}
		String startMessage = PrintUtils.getSeparatorLine(startMessage1, 2, 2, OzConstants.EQUAL_SIGN);
		Properties properties2Print = System.getProperties();
		if (prefix.length > 0) {
			properties2Print = new Properties();
			Properties systemProperties = System.getProperties();

			Set<Entry<Object, Object>> entrySet = systemProperties.entrySet();
			for (Entry<Object, Object> entry : entrySet) {
				String key = (String) entry.getKey();
				if (key.startsWith(prefix[0])) {
					properties2Print.put(key, (String) entry.getValue());
				}
			}
		}
		String systemPropertiesString = PropertiesUtils.printProperties(properties2Print, Level.FINEST);
		String endMessage = PrintUtils.getSeparatorLine("end of system properites listing", 1, 1, OzConstants.EQUAL_SIGN);
		logger.info(startMessage + systemPropertiesString + endMessage);
		return systemPropertiesString;
	}

	public static String setSystemProperty(final String key, final String value) {
		return System.setProperty(key, value);
	}

}
