package oz.infra.system.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.infra.system.gc.GcUtils;
import oz.infra.thread.ThreadUtils;

public class TestSystemUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// testGetCurrentDir();
		// testGetUserDir();
		// testGetOSName();
		// testGetUserName();
		// testExitAt("0027");
		// testExitIn(5678);
		// testValidateClassPath();
		// testPrintSystemProperties();
		// testSetSystemProperty();
		// testGetCallerClassAndMethodName();
		// testPrintMessageAndExit();
		// testGetJavaIoTmpDir();
		// testGetHostName();
		// testGetCurrentClassName();
		// testprintGCStats();
		// SystemUtils.getImplementationVersion(TestSystemUtils.class);
		// testGetProcessId();
		// testGetRunInfo();
		// testIsCurrentHost();
		// logger.info(SystemUtils.getLocalHostCanonicalName());
		testGetImplementationVersion();
		testGetImplementationVersion1();
	}

	private static void testGetImplementationVersion() {
		logger.info(SystemUtils.getImplementationVersion());
	}

	private static void testGetImplementationVersion1() {
		logger.info(SystemUtils.getImplementationVersion(TestSystemUtils.class));
	}

	private static void testIsCurrentHost() {
		testIsCurrentHost1("w284uea8");
		testIsCurrentHost1("w284UEa8");
		testIsCurrentHost1("w284UEa8.fibi.corp");
		testIsCurrentHost1("10.12.101.197");
		testIsCurrentHost1("10.12.101.1197");
		testIsCurrentHost1("s5380440");
		testIsCurrentHost1("s5380440440");
	}

	private static void testIsCurrentHost1(final String hostName) {
		logger.info("hostname: " + hostName + " isCurrentHost: " + String.valueOf(SystemUtils.isCurrentHost(hostName)));
	}

	private static void testExitAt(final String hhmm) {
		SystemUtils.exitAt(hhmm);
		while (true) {
			logger.info("Still in main ...");
			ThreadUtils.sleep(1000, Level.INFO);
		}
	}

	private static void testExitIn(final long milliSeconds) {
		SystemUtils.exitIn(milliSeconds);
		while (true) {
			logger.info("Still in main ...");
			ThreadUtils.sleep(1000, Level.INFO);
		}
	}

	private static void testGetCallerClassAndMethodName() {
		// logger.info(SystemUtils.getCallerClassName());
		// logger.info(SystemUtils.getCallerMethodName());
		logger.info(SystemUtils.getCallerPackageName());
		// logger.info(SystemUtils.getCallerClassAndMethodName());
		// logger.info(SystemUtils.getCallerClassAndMethodName(1));
		//
		// logger.info(SystemUtils.getCallerClassAndMethodString(1));
		// logger.info(SystemUtils.getCallerClassAndMethodString(2));
		// logger.info(SystemUtils.getCallerClassAndMethodString(3));
		// logger.info(SystemUtils.getCallerClassAndMethodString());
	}

	private static void testGetCurrentClassName() {
		logger.info(SystemUtils.getCurrentClassName());
	}

	private static void testGetCurrentDir() {
		logger.info(SystemUtils.getCurrentDir());
	}

	private static void testGetHostName() {
		logger.info(StringUtils.concat("hostname: ", SystemUtils.getHostname()));
	}

	private static void testGetJavaIoTmpDir() {
		logger.info(System.getProperty(SystemPropertiesUtils.SYSTEM_PROPERTY_JAVA_IO_TMPDIR));
		logger.info(SystemUtils.getSystemProperty(SystemPropertiesUtils.SYSTEM_PROPERTY_JAVA_IO_TMPDIR));
	}

	private static void testGetOSName() {
		logger.info(SystemPropertiesUtils.getOsName());
		logger.info("line separator: " + SystemUtils.LINE_SEPARATOR + "***");
	}

	private static void testGetProcessId() {
		logger.info(SystemUtils.getProcessId());
	}

	private static void testGetRunInfo() {
		logger.info(SystemUtils.getRunInfo());
	}

	private static void testGetUserDir() {
		logger.info("user dir: " + SystemPropertiesUtils.getUserDir());
		logger.info("line separator: " + SystemUtils.LINE_SEPARATOR + "***");
	}

	private static void testGetUserName() {
		logger.info(SystemPropertiesUtils.getUserName());
		logger.info("line separator: " + SystemUtils.LINE_SEPARATOR + "***");
	}

	private static void testprintGCStats() {
		GcUtils.printGCStats();
	}

	private static void testPrintMessageAndExit() {
		SystemUtils.printMessageAndExit("gui - no param", 17);
		// SystemUtils.printMessageAndExit("gui - true", 17, true);
		// SystemUtils.printMessageAndExit("gui - false", 17, false);
	}

	private static void testPrintSystemProperties() {
		SystemPropertiesUtils.printSystemProperties();
		logger.info("\n" + OzConstants.ASTERISKS_20 + "\n");
		SystemPropertiesUtils.printSystemProperties("sun");
		logger.info("\n" + OzConstants.ASTERISKS_20 + "\n");
		SystemPropertiesUtils.printSystemProperties("sun.j");
		logger.info("\n" + OzConstants.ASTERISKS_20 + "\n");
		SystemPropertiesUtils.printSystemProperties("os.");
		logger.info("\n" + OzConstants.ASTERISKS_20 + "\n");
		SystemPropertiesUtils.printSystemProperties("java.");
	}

	private static void testSetSystemProperty() {
		PropertiesUtils.printSystemProperties();
		logger.info(SystemPropertiesUtils.setSystemProperty("kkkkkk", "vvvvvv"));
		PropertiesUtils.printSystemProperties();

	}

	private static void testValidateClassPath() {
		SystemUtils.validateClassPath();
	}
}
