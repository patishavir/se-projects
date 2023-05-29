package oz.utils.autodeploy.common;

import java.util.logging.Logger;

import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;

public class AutoDeployGlobalParameters {

	private static String workFolderPath = null;
	private static String invalidFolderPath = null;
	private static String inProgressdFolderPath = null;
	private static String doneFolderPath = null;
	private static String portalFolderPath = null;
	private static String logsFolder = null;
	private static String systemsRoot = null;
	private static String snifitDeploymentPropertiesFileName = null;
	private static String rootFolderPath = null;
	private static boolean sendMail = true;
	private static int singleInstancePort = Integer.MIN_VALUE;
	private static String gmEnvironment = null;
	private static final Logger logger = JulUtils.getLogger();
	public static final Logger SUMMARY_LOGGER = JulUtils.getSummaryLogger(null);

	private static void buildSubFolders() {
		workFolderPath = PathUtils.appendTralingSeparatorIfNotExists(workFolderPath);
		invalidFolderPath = PathUtils.getAbsolutePath(workFolderPath + "ears/invalid");
		inProgressdFolderPath = PathUtils.getAbsolutePath(workFolderPath + "ears/inProgress");
		doneFolderPath = PathUtils.getAbsolutePath(workFolderPath + "ears/done");
		portalFolderPath = PathUtils.getAbsolutePath(workFolderPath + "portal");
		FolderUtils.createFolderIfNotExists(invalidFolderPath);
		FolderUtils.createFolderIfNotExists(inProgressdFolderPath);
		FolderUtils.createFolderIfNotExists(doneFolderPath);
		FolderUtils.createFolderIfNotExists(portalFolderPath);
	}

	public static String getDoneFolderPath() {
		return doneFolderPath;
	}

	public static String getGmEnvironment() {
		return gmEnvironment;
	}

	public static String getInProgressdFolderPath() {
		return inProgressdFolderPath;
	}

	public static String getInvalidFolderPath() {
		return invalidFolderPath;
	}

	public static String getLogsFolder() {
		return logsFolder;
	}

	public static String getPortalFolderPath() {
		return portalFolderPath;
	}

	public static int getSingleInstancePort() {
		return singleInstancePort;
	}

	public static String getSnifitDeploymentPropertiesFileName() {
		return snifitDeploymentPropertiesFileName;
	}

	public static String getSystemsRoot() {
		return systemsRoot;
	}

	public static String getWorkFolderPath() {
		return workFolderPath;
	}

	public static boolean isSendMail() {
		return sendMail;
	}

	public static void processParameters(final String propertiesFilePath) {
		ParametersUtils.processPatameters(propertiesFilePath, AutoDeployGlobalParameters.class);
		buildSubFolders();
	}

	public static void setGmEnvironment(final String gmEnvironment) {
		AutoDeployGlobalParameters.gmEnvironment = gmEnvironment;
	}

	public static void setLogsFolder(final String logsFolder) {
		AutoDeployGlobalParameters.logsFolder = PathUtils.getFullPath(rootFolderPath, logsFolder);
		FolderUtils.mkdirIfNoExists(AutoDeployGlobalParameters.logsFolder);
		logger.info("logsFolder: ".concat(AutoDeployGlobalParameters.logsFolder));
	}

	public static void setRootFolderPath(final String rootFolderPath) {
		AutoDeployGlobalParameters.rootFolderPath = rootFolderPath;
	}

	public static void setSendMail(final boolean sendMail) {
		AutoDeployGlobalParameters.sendMail = sendMail;
		logger.info("send mail set to: " + String.valueOf(AutoDeployGlobalParameters.sendMail));
	}

	public static void setSingleInstancePort(final int singleInstancePort) {
		AutoDeployGlobalParameters.singleInstancePort = singleInstancePort;
	}

	public static void setSnifitDeploymentPropertiesFileName(final String snifitDeploymentPropertiesFileName) {
		AutoDeployGlobalParameters.snifitDeploymentPropertiesFileName = snifitDeploymentPropertiesFileName;
	}

	public static void setSystemsRoot(final String systemsRoot) {
		AutoDeployGlobalParameters.systemsRoot = PathUtils.getFullPath(rootFolderPath, systemsRoot);
		FolderUtils.dieUnlessFolderExists(AutoDeployGlobalParameters.systemsRoot);
		logger.info("systemsRoot: ".concat(AutoDeployGlobalParameters.systemsRoot));

	}

	public static void setWorkFolderPath(final String workFolderPath) {
		AutoDeployGlobalParameters.workFolderPath = PathUtils.getFullPath(rootFolderPath, workFolderPath);
	}
}
