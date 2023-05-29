package oz.utils.was.autodeploy.portal.parameters;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;

public class PortalDeploymentParameters {

	private static String matafPortalFolder = null;

	private static String applicationServers = null;
	private static String remotePropertiesFilePath = null;
	private static String[] applicationServersArray = null;
	private static String xmlAccessCommand = null;
	private static String xmlAccessUser = null;
	private static String xmlAccessPassword = null;
	private static String portalVersionFolderName = "Version";
	private static String deploymentFolderName = "deployment";
	private static String installWarsXmlFileName = "installWars.xml";
	private static String matafPortalFolders = null;
	private static String[] matafPortalFoldersArray = null;
	private static String system = null;
	private static String rootFolderPath = null;
	private static final Logger logger = JulUtils.getLogger();

	public static String[] getApplicationServersArray() {
		return applicationServersArray;
	}

	public static String getDeploymentFolderName() {
		return deploymentFolderName;
	}

	public static String getInstallWarsXmlFileName() {
		return installWarsXmlFileName;
	}

	public static String getMatafPortalFolder() {
		return matafPortalFolder;
	}

	public static String[] getMatafPortalFoldersArray() {
		return matafPortalFoldersArray;
	}

	public static String getPortalVersionFolderName() {
		return portalVersionFolderName;
	}

	public static String getRemotePortalDeploymentFolderPath() {
		String remotePortalDeploymentFolderPath = PathUtils.addUnixPaths(matafPortalFolder, deploymentFolderName);
		return remotePortalDeploymentFolderPath;
	}

	public static String getRemotePortalVersionFolderPath() {
		String portalVersionFolderPath = PathUtils.addUnixPaths(matafPortalFolder, portalVersionFolderName);
		return portalVersionFolderPath;
	}

	public static String getRemotePropertiesFilePath() {
		return remotePropertiesFilePath;
	}

	public static String getSystem() {
		return system;
	}

	public static String getXmlAccessCommand() {
		return xmlAccessCommand;
	}

	public static String getXmlAccessPassword() {
		return xmlAccessPassword;
	}

	public static String getXmlAccessUser() {
		return xmlAccessUser;
	}

	public static void processParameters(final String propertiesFilePath) {
		ParametersUtils.processPatameters(propertiesFilePath, PortalDeploymentParameters.class);
		system = PathUtils.getParentFolderName(propertiesFilePath);
	}

	public static void setApplicationServers(final String applicationServers) {
		PortalDeploymentParameters.applicationServers = applicationServers;
		applicationServersArray = applicationServers.split(OzConstants.COMMA);
	}

	public static void setMatafPortalFolder(String matafPortalFolder) {
		PortalDeploymentParameters.matafPortalFolder = matafPortalFolder;
	}

	public static void setMatafPortalFolders(String matafPortalFolders) {
		matafPortalFoldersArray = matafPortalFolders.split(OzConstants.COMMA);
	}

	public static void setRemotePropertiesFilePath(final String remotePropertiesFilePath) {
		PortalDeploymentParameters.remotePropertiesFilePath = PathUtils.getFullPath(rootFolderPath,
				remotePropertiesFilePath);
	}

	public static void setRootFolderPath(final String rootFolderPath) {
		PortalDeploymentParameters.rootFolderPath = rootFolderPath;
	}

	public static void setXmlAccessCommand(String xmlAccessCommand) {
		PortalDeploymentParameters.xmlAccessCommand = xmlAccessCommand;
	}

	public static void setXmlAccessPassword(String xmlAccessPassword) {
		PortalDeploymentParameters.xmlAccessPassword = xmlAccessPassword;
	}

	public static void setXmlAccessUser(String xmlAccessUser) {
		PortalDeploymentParameters.xmlAccessUser = xmlAccessUser;
	}
}
