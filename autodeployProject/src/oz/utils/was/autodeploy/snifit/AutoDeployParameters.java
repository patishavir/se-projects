package oz.utils.was.autodeploy.snifit;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.collection.CollectionUtils;
import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.MapUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.properties.PropertiesUtils;

public final class AutoDeployParameters {
	private static String inputEarFolderPath = null;
	private static String earPropertiesFolderPath = null;

	private static HashMap<String, Properties> earsPropertiesHashMap = null;
	private static String jythonScriptSourceFilePath = null;
	private static String jythonScriptTargetFilePath = null;
	private static String prepare4DeploymentScriptPath = null;
	private static String wsadminRunFilePath = null;
	private static String rootFolderPath = null;
	private static String workFolderPath = null;
	private static String modifyEarPropertiesFilePath = null;

	private static String remotePropertiesFilePath = null;
	private static Properties remoteProperties = null;

	private static String emailMessagesPropertiesFilePath = null;
	private static Properties emailMessagesProperties = null;

	private static int getVersionRetryCount = 100;
	private static int getVersionRetryInterval = 5000;

	private static String invalidFolderPath = null;
	private static String inProgressdFolderPath = null;
	private static String doneFolderPath = null;
	private static String logsFolder = null;

	private static Logger logger = JulUtils.getLogger();

	private static void buildSubFolders() {
		workFolderPath = PathUtils.appendTralingSeparatorIfNotExists(workFolderPath);
		invalidFolderPath = workFolderPath + "invalid";
		inProgressdFolderPath = workFolderPath + "inProgress";
		doneFolderPath = workFolderPath + "done";
		FolderUtils.createFolderIfNotExists(invalidFolderPath);
		FolderUtils.createFolderIfNotExists(inProgressdFolderPath);
		FolderUtils.createFolderIfNotExists(doneFolderPath);
	}

	private static HashMap<String, Properties> generateEarsHashMap(final String earsPropertiesFolderPath) {
		String[] folderPathsArray = earsPropertiesFolderPath.split(OzConstants.COMMA);
		HashMap<String, Properties> hashMap = new HashMap<String, Properties>();
		for (String folderPath1 : folderPathsArray) {
			String fullPath = PathUtils.getFullPath(rootFolderPath, folderPath1);
			hashMap = PropertiesUtils.loadPropertiesFilesFromFolder(fullPath, hashMap);
		}
		Set<String> keySet = hashMap.keySet();
		CollectionUtils.printCollection(keySet, "Found properties files for:", Level.INFO);
		return hashMap;
	}

	public static String getDoneFolderPath() {
		return doneFolderPath;
	}

	public static String getEarPropertiesFolderPath() {
		return earPropertiesFolderPath;
	}

	public static HashMap<String, Properties> getEarsPropertiesHashMap() {
		return earsPropertiesHashMap;
	}

	public static Properties getEmailMessagesProperties() {
		return emailMessagesProperties;
	}

	public static int getGetVersionRetryCount() {
		return getVersionRetryCount;
	}

	public static int getGetVersionRetryInterval() {
		return getVersionRetryInterval;
	}

	public static String getInProgressdFolderPath() {
		return inProgressdFolderPath;
	}

	public static String getInputEarFolderPath() {
		return inputEarFolderPath;
	}

	public static String getInvalidFolderPath() {
		return invalidFolderPath;
	}

	public static String getJythonScriptSourceFilePath() {
		return jythonScriptSourceFilePath;
	}

	public static String getJythonScriptTargetFilePath() {
		return jythonScriptTargetFilePath;
	}

	public static String getLogsFolder() {
		return logsFolder;
	}

	public static String getModifyEarPropertiesFilePath() {
		return modifyEarPropertiesFilePath;
	}

	public static String getPrepare4DeploymentScriptPath() {
		return prepare4DeploymentScriptPath;
	}

	public static Properties getRemoteProperties() {
		return remoteProperties;
	}

	public static String getWsadminRunFilePath() {
		return wsadminRunFilePath;
	}

	public static void processParameters(final String propertiesFilePath) {
		ParametersUtils.processPatameters(propertiesFilePath, AutoDeployParameters.class);
		earsPropertiesHashMap = generateEarsHashMap(earPropertiesFolderPath);
		MapUtils.printMap(earsPropertiesHashMap, Level.FINEST);
		buildSubFolders();
	}

	public static void setEarPropertiesFolderPath(final String earPropertiesFolderPath) {
		AutoDeployParameters.earPropertiesFolderPath = PathUtils.getFullPath(rootFolderPath, earPropertiesFolderPath);
		FolderUtils.dieUnlessFolderExists(AutoDeployParameters.earPropertiesFolderPath);
	}

	public static void setEmailMessagesPropertiesFilePath(final String emailMessagesPropertiesFilePath) {
		AutoDeployParameters.emailMessagesPropertiesFilePath = emailMessagesPropertiesFilePath;
		String emailMessagesPropertiesFileFullPath = PathUtils.getFullPath(rootFolderPath,
				emailMessagesPropertiesFilePath);
		emailMessagesProperties = PropertiesUtils.loadPropertiesFile(emailMessagesPropertiesFileFullPath,
				OzConstants.WIN1255_ENCODING);
	}

	public static void setGetVersionRetryCount(final int getVersionRetryCount) {
		AutoDeployParameters.getVersionRetryCount = getVersionRetryCount;
	}

	public static void setGetVersionRetryInterval(final int getVersionRetryInterval) {
		AutoDeployParameters.getVersionRetryInterval = getVersionRetryInterval;
	}

	public static void setInputEarFolderPath(final String inputEarFolderPath) {
		AutoDeployParameters.inputEarFolderPath = PathUtils.appendTralingSeparatorIfNotExists(inputEarFolderPath);
		FolderUtils.dieUnlessFolderExists(inputEarFolderPath);
	}

	public static void setJythonScriptSourceFilePath(final String jythonScriptSourceFilePath) {
		AutoDeployParameters.jythonScriptSourceFilePath = PathUtils.getFullPath(rootFolderPath,
				jythonScriptSourceFilePath);
		logger.finest("jythonScriptSourceFilePath: " + jythonScriptSourceFilePath);
		FileUtils.terminateIfFileDoesNotExist(AutoDeployParameters.jythonScriptSourceFilePath);
	}

	public static void setJythonScriptTargetFilePath(final String jythonScriptTargetFilePath) {
		AutoDeployParameters.jythonScriptTargetFilePath = PathUtils.getFullPath(rootFolderPath,
				jythonScriptTargetFilePath);
		logger.finest("jythonScriptTargetFilePath:  " + AutoDeployParameters.jythonScriptTargetFilePath);
	}

	public static void setLogsFolder(final String logsFolder) {
		AutoDeployParameters.logsFolder = PathUtils.getFullPath(rootFolderPath, logsFolder);
		logger.info("logsFolder: ".concat(AutoDeployParameters.logsFolder));
	}

	public static void setModifyEarPropertiesFilePath(final String modifyEarPropertiesFilePath) {
		AutoDeployParameters.modifyEarPropertiesFilePath = PathUtils.getFullPath(rootFolderPath,
				modifyEarPropertiesFilePath);
		FileUtils.terminateIfFileDoesNotExist(AutoDeployParameters.modifyEarPropertiesFilePath);
	}

	public static void setPrepare4DeploymentScriptPath(final String prepare4DeploymentScriptPath) {
		AutoDeployParameters.prepare4DeploymentScriptPath = prepare4DeploymentScriptPath;
		FileUtils.terminateIfFileDoesNotExist(prepare4DeploymentScriptPath);
	}

	public static void setRemotePropertiesFilePath(final String remotePropertiesFilePath) {
		String remotePropertiesFileFullPath = PathUtils.getFullPath(rootFolderPath, remotePropertiesFilePath);
		FileUtils.terminateIfFileDoesNotExist(remotePropertiesFileFullPath);
		remoteProperties = PropertiesUtils.loadPropertiesFile(remotePropertiesFileFullPath);
	}

	public static void setRootFolderPath(final String rootFolderPath) {
		AutoDeployParameters.rootFolderPath = rootFolderPath;
	}

	public static void setWorkFolderPath(final String workFolderPath) {
		AutoDeployParameters.workFolderPath = PathUtils.getFullPath(rootFolderPath, workFolderPath);
	}

	public static void setWsadminRunFilePath(final String wsadminRunFilePath) {
		AutoDeployParameters.wsadminRunFilePath = PathUtils.getFullPath(rootFolderPath, wsadminRunFilePath);
		FileUtils.terminateIfFileDoesNotExist(AutoDeployParameters.wsadminRunFilePath);
	}
}
