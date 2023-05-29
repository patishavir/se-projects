package oz.infra.system;

import java.awt.Toolkit;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.ZipFileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.net.ip.IpUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.string.StringUtils;
import oz.infra.system.env.EnvironmentUtils;
import oz.infra.varargs.VarArgsUtils;

public final class SystemUtils {
	public static final String SUN_SECURITY_KRB5_DEBUG = "sun.security.krb5.debug";

	public static final String JAVAX_SECURITY_AUTH_USESUBJECTCREDSONLY = "javax.security.auth.useSubjectCredsOnly";
	public static final String JAVA_SECURITY_KRB5_CONF = "java.security.krb5.conf";
	public static final String JAVA_SECURITY_KRB5_REALM = "java.security.krb5.realm";
	public static final String JAVA_SECURITY_KRB5_KDC = "java.security.krb5.kdc";
	public static final String JAVA_SECURITY_AUTH_LOGIN_CONFIG = "java.security.auth.login.config";

	public static final String LINE_SEPARATOR = System.getProperty(SystemPropertiesUtils.SYSTEM_PROPERTY_LINE_SEPARATOR);
	private static final String HOSTNAME = "hostname";
	static Logger logger = JulUtils.getLogger();

	public static void beep() {
		Toolkit.getDefaultToolkit().beep();
	}

	public static long exitAt(final String hhmm) {
		long millisTillHHMM = DateTimeUtils.getMillisTillNextHHMM(hhmm);
		exitIn(millisTillHHMM);
		return millisTillHHMM;
	}

	public static void exitIn(final long milliSeconds) {
		new ExitThread(milliSeconds).start();
	}

	public static boolean getBooleanVarFromSysPropsOrEnv(final String varName) {
		String varValue = getVarFromSysPropsOrEnv(varName);
		boolean result = false;
		result = varValue != null && (varValue.equalsIgnoreCase(OzConstants.YES) || varValue.equalsIgnoreCase(OzConstants.TRUE));
		return result;
	}

	public static String getCallerClassAndMethodName(final Integer... nestingLevel) {
		StringBuilder sb = new StringBuilder();
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		int stackTracePtr = VarArgsUtils.getMyArg(new Integer(OzConstants.INT_3), nestingLevel);
		stackTracePtr = getStackTracePtr(stackTraceElements, stackTracePtr);
		StackTraceElement stackTraceElement = stackTraceElements[stackTracePtr];
		sb.append(stackTraceElement.getClassName());
		sb.append(OzConstants.BLANK);
		sb.append(stackTraceElement.getMethodName());
		return sb.toString();
	}

	public static String getCallerClassAndMethodString(final Integer... nestingLevel) {
		return StringUtils.concat("caller : ", SystemUtils.getCallerClassAndMethodName(nestingLevel), SystemUtils.LINE_SEPARATOR);
	}

	public static String getCallerClassName(final Integer... nestingLevel) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		int stackTracePtr = VarArgsUtils.getMyArg(new Integer(OzConstants.INT_3), nestingLevel);
		stackTracePtr = getStackTracePtr(stackTraceElements, stackTracePtr);
		return stackTraceElements[stackTracePtr].getClassName();
	}

	public static Class getCallerClassObject(final Integer... nestingLevel) {
		int stackTracePtr = VarArgsUtils.getMyArg(new Integer(OzConstants.INT_4), nestingLevel);
		String className = getCallerClassName(stackTracePtr);
		Class clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return clazz;
	}

	public static String getCallerMethodName(final Integer... nestingLevel) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		int stackTracePtr = VarArgsUtils.getMyArg(new Integer(OzConstants.INT_3), nestingLevel);
		stackTracePtr = getStackTracePtr(stackTraceElements, stackTracePtr);
		return stackTraceElements[stackTracePtr].getMethodName();
	}

	public static String getCallerPackageName(final Integer... nestingLevel) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		// printStackTraceElements(stackTraceElements);
		int stackTracePtr = VarArgsUtils.getMyArg(new Integer(OzConstants.INT_3), nestingLevel);
		stackTracePtr = getStackTracePtr(stackTraceElements, stackTracePtr);
		String className = stackTraceElements[stackTracePtr].getClassName();
		String packageName = className.substring(0, className.lastIndexOf(OzConstants.DOT));
		return packageName;
	}

	public static String getCclassPathDelimiter() {
		return File.pathSeparator;
	}

	public static String getClassNameWithoutPackage(final String className) {
		String[] classNameArray = className.split("\\.");
		return classNameArray[classNameArray.length - 1];
	}

	public static String getCurrentClassName() {
		String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		String[] classNameArray = fullClassName.split("\\.");
		return classNameArray[classNameArray.length - 1];
	}

	public static String getCurrentDir() {
		File currentDir = new File(".");
		String currentDirPath = currentDir.getAbsolutePath();
		logger.info(currentDirPath);
		return currentDirPath;
	}

	public static String getCurrentFullClassName() {
		return Thread.currentThread().getStackTrace()[2].getClassName();
	}

	public static String getCurrentMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	public static String getCurrentPackage() {
		String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		return fullClassName.substring(0, fullClassName.lastIndexOf(OzConstants.DOT));
	}

	public static String getHostname() {
		SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(HOSTNAME);
		return scer.getStdout().trim();
	}

	public static String getImplementationVersion() {
		String implementationVersion = SystemUtils.class.getPackage().getImplementationVersion();
		return implementationVersion;
	}

	
	public static String getImplementationVersion(final Class<?> clasz) {
		String implementationVersion = clasz.getPackage().getImplementationVersion();
		return implementationVersion;
	}

	public static String getLocalHostCanonicalName() {
		String canonicalHostName = null;
		try {
			canonicalHostName = InetAddress.getLocalHost().getCanonicalHostName();
			logger.finest("localHostName: " + canonicalHostName);
		} catch (Exception ex) {
			logger.warning("Exception caught =" + ex.getMessage());
		}
		return canonicalHostName;
	}

	public static String getLocalHostName() {
		String localHostName = null;
		try {
			localHostName = InetAddress.getLocalHost().getHostName();
			logger.finest("localHostName: " + localHostName);
		} catch (Exception ex) {
			logger.warning("Exception caught =" + ex.getMessage());
		}
		return localHostName;
	}

	public static Class getMyClass(final Integer... nestingLevel) {
		String myClassName = getCallerClassName(nestingLevel);
		Class myClass = null;
		try {
			myClass = Class.forName(myClassName);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return myClass;
	}

	public static String getProcessId() {
		// Note: may fail in some JVM implementations
		// therefore fallback has to be provided
		// something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
		String pid = null;
		final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		logger.finest("jvmName: " + jvmName);
		final int index = jvmName.indexOf('@');
		if (index > OzConstants.STRING_NOT_FOUND) {
			pid = jvmName.substring(0, index);
		}
		return pid;
	}

	public static String getRunInfo() {
		String classPath = SystemPropertiesUtils.getJavaClassPath().replaceAll(File.pathSeparator, LINE_SEPARATOR);
		String runInfo = LINE_SEPARATOR + "run info: " + getImplementationVersion() + "  host: " + getLocalHostCanonicalName() + "  pid: "
				+ getProcessId() + "  java home: " + SystemPropertiesUtils.getJavaHome() + "  java version: " + SystemPropertiesUtils.getJavaVersion()
				+ LINE_SEPARATOR + "class path:" + LINE_SEPARATOR + classPath;
		return runInfo;
	}

	public static String getScriptSuffix() {
		String scriptSuffix = OzConstants.BAT_SUFFIX;
		if (isUnixOS()) {
			scriptSuffix = OzConstants.SH_SUFFIX;
		}
		return scriptSuffix;
	}

	private static int getStackTracePtr(final StackTraceElement[] stackTraceElements, final int stackTracePt) {
		int stackTracePtr = stackTracePt;
		if (stackTraceElements.length <= stackTracePtr) {
			stackTracePtr = stackTraceElements.length - 1;
		}
		return stackTracePtr;
	}

	public static String getSystemProperty(final String key) {
		String value = System.getProperty(key);
		return value;
	}

	public static String getVarFromSysPropsOrEnv(final String varName) {
		String varValue = SystemUtils.getSystemProperty(varName);
		if (varValue == null) {
			varValue = EnvironmentUtils.getEnvironmentVariable(varName);
		}
		return varValue;
	}

	public static boolean isCurrentHost(final String host) {
		String currentHostName = SystemUtils.getHostname();
		String currentIpAddress = IpUtils.getIpAddress(currentHostName);
		String hostIpAddress = IpUtils.getIpAddress(host);
		boolean currentHost = currentIpAddress.equals(hostIpAddress);
		logger.finest(StringUtils.concat("current host name: ", currentHostName, " ip address: " + currentIpAddress, " host: ", host, " ip address: ",
				hostIpAddress, " isCurrentHost: ", String.valueOf(currentHost)));
		return currentHost;

	}

	public static boolean isUnixOS() {
		return File.separator.equals(OzConstants.UNIX_FILE_SEPARATOR);
	}

	public static boolean isWindowsFlavorOS() {
		return SystemPropertiesUtils.getOsName().toLowerCase().startsWith("win");
	}

	public static void printMessageAndExit(final String exitMessage, final int exitCode, final Boolean... showGuis) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		String callingClass = stackTrace[2].getClassName();
		String callingMethod = stackTrace[2].getMethodName();
		logger.severe(callingClass + " " + callingMethod + LINE_SEPARATOR + exitMessage + LINE_SEPARATOR + "System will exit now ...");
		boolean showGui = VarArgsUtils.getMyArg(true, showGuis);
		if (showGui) {
			JOptionPane.showMessageDialog(null, exitMessage);
		}
		System.exit(exitCode);
	}

	public static void printStackTraceElements(final StackTraceElement[] stackTraceElements) {
		StringBuilder sb = new StringBuilder(SystemUtils.LINE_SEPARATOR);
		for (int i = 0; i < stackTraceElements.length; i++) {
			sb.append(stackTraceElements[i].toString());
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		logger.info(sb.toString());
	}

	public static boolean validateClassPath() {
		boolean returnCode = true;
		StringBuilder sb = new StringBuilder();
		String classPath = EnvironmentUtils.getEnvironmentVariable("CLASSPATH");
		if (classPath != null) {
			String[] classPathArray = classPath.split(getCclassPathDelimiter());
			for (String path1 : classPathArray) {
				File file1 = new File(path1);
				boolean fileExists = file1.exists();
				returnCode = returnCode && fileExists;
				if (fileExists) {
					sb.append(LINE_SEPARATOR + path1 + " exists.");
					if (file1.isFile() && ZipFileUtils.getZipContents(path1) == null) {
						logger.warning("Cannot read " + path1);
					}
				} else {
					logger.warning(path1 + " does not exist !!!");
				}
			}
			logger.finest(sb.toString());
		}
		if (!returnCode) {
			logger.warning("Invalid classpath. Processing has been teminated.");
		} else {
			logger.info("Classpath is valid. Processing continues.");
		}
		return returnCode;
	}

	private SystemUtils() {
		super();
	}
}
