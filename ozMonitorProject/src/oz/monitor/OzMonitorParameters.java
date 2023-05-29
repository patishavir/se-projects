package oz.monitor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.management.MBeanServer;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.VerbosityLevel;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.system.SystemUtils;

public class OzMonitorParameters {
	private static String environment;
	private static HashMap<String, Properties> monitorsHashMap;
	private static HashMap<String, Properties> alertsHashMap;
	private static String log4jconfigFilePath = null;
	private static String monitorsFoldersPaths = null;
	private static String alertsFoldersPaths = null;
	private static String defaultAlerts = null;
	//
	private static String heartBeatDestinations = null;
	private static String sendHeartBeatMessageAt = null;
	//
	private static String defaultStartTimeHHMM = "0700";
	private static String defaultEndTimeHHMM = "2000";
	private static String rootFolderPath = null;
	private static int jmxPort = 9990;
	private static String objectNameString = "ozmonitor:name=ozmonitor";
	private static MBeanServer mbeanServer = null;
	private static String logFilePath = null;

	private static Logger logger = JulUtils.getLogger();

	private static HashMap<String, Properties> generateHashMap(final String folderPaths) {
		String[] folderPathsArray = folderPaths.split(OzConstants.COMMA);
		HashMap<String, Properties> hashMap = new HashMap<String, Properties>();
		for (String folderPath1 : folderPathsArray) {
			String fullPath = PathUtils.getFullPath(rootFolderPath, folderPath1);
			hashMap = PropertiesUtils.loadPropertiesFilesFromFolder(fullPath, hashMap);
		}
		return hashMap;
	}

	public static HashMap<String, Properties> getAlertsHashMap() {
		return alertsHashMap;
	}

	public static final String getDefaultAlerts() {
		return defaultAlerts;
	}

	public static String getDefaultEndTimeHHMM() {
		return defaultEndTimeHHMM;
	}

	public static String getDefaultStartTimeHHMM() {
		return defaultStartTimeHHMM;
	}

	public static final String getEnvironment() {
		return environment;
	}

	public static final String getHeartBeatDestinations() {
		return heartBeatDestinations;
	}

	public static int getJmxPort() {
		return jmxPort;
	}

	public static MBeanServer getMbeanServer() {
		return mbeanServer;
	}

	public static Map<String, Properties> getMonitorsMap() {
		return monitorsHashMap;
	}

	public static String getObjectNameString() {
		return objectNameString;
	}

	public static String getRootFolderPath() {
		return rootFolderPath;
	}

	public static String getSendHeartBeatMessageAt() {
		return sendHeartBeatMessageAt;
	}

	public static void processInputParameters(final String[] args) {
		if (args.length < 1) {
			String errorMessage = "A property file path should be passed as a parameter. Processing aborted !";
			SystemUtils.printMessageAndExit(errorMessage, OzConstants.EXIT_STATUS_ABNORMAL);
		}
		String propertiesFilePath = args[0];
		FileUtils.terminateIfFileDoesNotExist(propertiesFilePath);
		logger.info("processing properties file: " + propertiesFilePath);
		setRootFolderPath(propertiesFilePath);
		Properties ozMonitorProperties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		ozMonitorProperties = PropertiesUtils.updatePropertiesUsingEnvironmentVarialbes(ozMonitorProperties);
		ReflectionUtils.setFieldsFromProperties(ozMonitorProperties, OzMonitorParameters.class);
		logger.info(SystemUtils.getRunInfo());
		if (FileUtils.isFileExists(log4jconfigFilePath,VerbosityLevel.Error)) {
			logger.info("Configuring log4j using " + log4jconfigFilePath + " ...");
			PropertyConfigurator.configure(log4jconfigFilePath);
			logger.info("log4j configuration has completed.");
		} else {
			logger.info("Log4j configuration file has not been specified !");
			BasicConfigurator.configure();
		}
		monitorsHashMap = generateHashMap(monitorsFoldersPaths);
		alertsHashMap = generateHashMap(alertsFoldersPaths);
		logger.info("starting to monitor " + getEnvironment() + " on host: " + SystemUtils.getLocalHostName());
	}

	public static final void setAlertsFoldersPaths(final String alertsFoldersPaths) {
		OzMonitorParameters.alertsFoldersPaths = alertsFoldersPaths;
	}

	public static final void setDefaultAlerts(final String defaultAlerts) {
		OzMonitorParameters.defaultAlerts = defaultAlerts;
	}

	public static void setDefaultEndTimeHHMM(final String defaultEndTimeHHMM) {
		OzMonitorParameters.defaultEndTimeHHMM = defaultEndTimeHHMM;
	}

	public static void setDefaultStartTimeHHMM(final String defaultStartTimeHHMM) {
		OzMonitorParameters.defaultStartTimeHHMM = defaultStartTimeHHMM;
	}

	public static final void setEnvironment(final String environment) {
		OzMonitorParameters.environment = environment;
	}

	public static final void setHeartBeatDestinations(final String heartBeatDestinations) {
		OzMonitorParameters.heartBeatDestinations = heartBeatDestinations;
	}

	public static void setJmxPort(final int jmxPort) {
		OzMonitorParameters.jmxPort = jmxPort;
	}

	public static final void setLog4jconfigFilePath(final String log4jconfigFilePath) {
		OzMonitorParameters.log4jconfigFilePath = PathUtils.getFullPath(rootFolderPath, log4jconfigFilePath);
	}

	public static void setLogFilePath(final String logFilePath) {
		OzMonitorParameters.logFilePath = PathUtils.getFullPath(rootFolderPath, logFilePath);
		JulUtils.addFileHandler(OzMonitorParameters.logFilePath);
	}

	public static void setMbeanServer(final MBeanServer mbeanServer) {
		OzMonitorParameters.mbeanServer = mbeanServer;
	}

	public static final void setMonitorsFoldersPaths(final String monitorsFoldersPaths) {
		OzMonitorParameters.monitorsFoldersPaths = monitorsFoldersPaths;
	}

	public static void setObjectNameString(String objectNameString) {
		OzMonitorParameters.objectNameString = objectNameString;
	}

	private static void setRootFolderPath(final String filePath) {
		rootFolderPath = PathUtils.getParentFolderPath(filePath);
		logger.finest(rootFolderPath);
	}

	public static void setSendHeartBeatMessageAt(final String sendHeartBeatMessageAt) {
		OzMonitorParameters.sendHeartBeatMessageAt = sendHeartBeatMessageAt;
	}

}
