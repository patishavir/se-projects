package oz.utils.cm.ds.scripts;

import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.parameters.ParametersUtils;

public class DsCmScriptsParameters {
	private static String environment = null;
	private static String server = null;
	private static String userName = null;
	private static String password = null;
	private static String encryptionMethod = null;
	private static String zipFilesScript = null;
	private static String localFoler = null;
	private static String targetFolder = null;
	private static String unzipTargetFolder = null;
	private static String clearCasePropertiesFilePath = null;
	private static String rootFolderPath = null;

	public static String getClearCasePropertiesFilePath() {
		return clearCasePropertiesFilePath;
	}

	public static String getEncryptionMethod() {
		return encryptionMethod;
	}

	public static String getEnvironment() {
		return environment;
	}

	public static String getLocalFoler() {
		return localFoler;
	}

	public static String getPassword() {
		return password;
	}

	public static String getRootFolderPath() {
		return rootFolderPath;
	}

	public static String getServer() {
		return server;
	}

	public static String getTargetFolder() {
		return targetFolder;
	}

	public static String getUnzipTargetFolder() {
		return unzipTargetFolder;
	}

	public static String getUserName() {
		return userName;
	}

	public static String getZipFilesScript() {
		return zipFilesScript;
	}

	public static void processParameters(final String propertiesFilePath) {
		ParametersUtils.processPatameters(propertiesFilePath, DsCmScriptsParameters.class);
	}

	public static void setClearCasePropertiesFilePath(final String clearCasePropertiesFilePath) {
		DsCmScriptsParameters.clearCasePropertiesFilePath = PathUtils.getFullPath(rootFolderPath,
				clearCasePropertiesFilePath);
		FileUtils.terminateIfFileDoesNotExist(DsCmScriptsParameters.clearCasePropertiesFilePath);
	}

	public static void setEncryptionMethod(final String encryptionMethod) {
		DsCmScriptsParameters.encryptionMethod = encryptionMethod;
	}

	public static void setEnvironment(String environment) {
		DsCmScriptsParameters.environment = environment;
	}

	public static void setLocalFoler(final String localFoler) {
		DsCmScriptsParameters.localFoler = PathUtils.getFullPath(rootFolderPath, localFoler);
		FolderUtils.dieUnlessFolderExists(DsCmScriptsParameters.localFoler);
	}

	public static void setPassword(final String password) {
		DsCmScriptsParameters.password = password;
	}

	public static void setRootFolderPath(final String rootFolderPath) {
		DsCmScriptsParameters.rootFolderPath = rootFolderPath;
	}

	public static void setServer(final String server) {
		DsCmScriptsParameters.server = server;
	}

	public static void setTargetFolder(final String targetFolder) {
		DsCmScriptsParameters.targetFolder = targetFolder;
	}

	public static void setUnzipTargetFolder(String unzipTargetFolder) {
		DsCmScriptsParameters.unzipTargetFolder = unzipTargetFolder;
	}

	public static void setUserName(final String userName) {
		DsCmScriptsParameters.userName = userName;
	}

	public static void setZipFilesScript(String zipFilesScript) {
		DsCmScriptsParameters.zipFilesScript = zipFilesScript;
	}
}
