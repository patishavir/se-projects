package oz.utils.emergencyreports;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.system.SystemUtils;

public class EmergencyReportsParameters {
	private static String dbPropertiesFilePath = null;
	private static String sqlScriptFilePath = null;
	private static String branchListSqlScriptFilePath = null;
	private static String charsetName = null;
	private static String decriptPassword = null;
	private static String sheetNamePrefix = null;
	private static String reportsFolder = null;
	private static int branches2Process = Integer.MIN_VALUE;
	private static int concurrentThreads = 10;
	private static int loginTimeOut = Integer.MIN_VALUE;
	private static String outputFormat = null;
	private static String rootFolderPath = null;

	private static final Logger logger = JulUtils.getLogger();

	public static int getBranches2Process() {
		return branches2Process;
	}

	public static String getBranchListSqlScriptFilePath() {
		return branchListSqlScriptFilePath;
	}

	public static String getCharsetName() {
		return charsetName;
	}

	public static int getConcurrentThreads() {
		return concurrentThreads;
	}

	public static String getDbPropertiesFilePath() {
		return dbPropertiesFilePath;
	}

	public static int getLoginTimeOut() {
		return loginTimeOut;
	}

	public static String getOutputFormat() {
		return outputFormat;
	}

	public static String getReportsFolder() {
		return reportsFolder;
	}

	public static String getSheetNamePrefix() {
		return sheetNamePrefix;
	}

	public static String getSqlScriptFilePath() {
		return sqlScriptFilePath;
	}

	public static String getDecriptPassword() {
		return decriptPassword;
	}

	public static void processInputParameters(final String[] args) {
		if (args.length != 1) {
			SystemUtils.printMessageAndExit(
					"A property file path should be passed as a parameter. Processing aborted !", -1);
		}
		setRootFolderPath(args[0]);
		Properties emergencyReportsProperties = PropertiesUtils.loadPropertiesFile(args[0]);
		emergencyReportsProperties = PropertiesUtils
				.updatePropertiesUsingEnvironmentVarialbes(emergencyReportsProperties);
		ReflectionUtils.setFieldsFromProperties(emergencyReportsProperties, EmergencyReportsParameters.class);
	}

	public static void setBranches2Process(final int branches2Process) {
		EmergencyReportsParameters.branches2Process = branches2Process;
	}

	public static void setBranchListSqlScriptFilePath(final String branchListSqlScriptFilePath) {
		EmergencyReportsParameters.branchListSqlScriptFilePath = PathUtils.getFullPath(rootFolderPath,
				branchListSqlScriptFilePath);
	}

	public static void setCharsetName(final String charsetName) {
		EmergencyReportsParameters.charsetName = charsetName;
	}

	public static void setConcurrentThreads(final int concurrentThreads) {
		EmergencyReportsParameters.concurrentThreads = concurrentThreads;
	}

	public static void setDbPropertiesFilePath(final String dbPropertiesFilePath) {
		EmergencyReportsParameters.dbPropertiesFilePath = PathUtils.getFullPath(rootFolderPath, dbPropertiesFilePath);
	}

	public static void setDecriptPassword(final String decriptPassword) {
		EmergencyReportsParameters.decriptPassword = decriptPassword;
	}

	public static void setLoginTimeOut(final int loginTimeOut) {
		EmergencyReportsParameters.loginTimeOut = loginTimeOut;
	}

	public static void setOutputFormat(final String outputFormat) {
		EmergencyReportsParameters.outputFormat = outputFormat;
	}

	public static void setReportsFolder(final String reportsFolder) {
		EmergencyReportsParameters.reportsFolder = reportsFolder;
		FolderUtils.createFolderIfNotExists(EmergencyReportsParameters.reportsFolder);
	}

	private static void setRootFolderPath(final String filePath) {
		rootFolderPath = PathUtils.getParentFolderPath(filePath);
		logger.finest(rootFolderPath);
	}

	public static void setSheetNamePrefix(final String sheetNamePrefix) {
		EmergencyReportsParameters.sheetNamePrefix = sheetNamePrefix;
	}

	public static void setSqlScriptFilePath(final String sqlScriptFilePath) {
		EmergencyReportsParameters.sqlScriptFilePath = PathUtils.getFullPath(rootFolderPath, sqlScriptFilePath);
	}

}
