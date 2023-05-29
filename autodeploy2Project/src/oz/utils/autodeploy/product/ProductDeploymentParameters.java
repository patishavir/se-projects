package oz.utils.autodeploy.product;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;

public class ProductDeploymentParameters {

	private static String productFolder = null;
	private static String productVersionFolder = null;
	private static String productBackupFolder = null;
	private static String productFolders = null;
	private static String[] productFoldersArray = null;
	private static String applicationServers = null;
	private static String[] applicationServersArray = null;
	private static String remotePropertiesFilePath = null;
	private static String xmlAccessCommand = null;
	private static String xmlAccessUser = null;
	private static String xmlAccessPassword = null;
	private static String installWarsXmlFileName = "installWars.xml";

	private static String system = null;
	private static String rootFolderPath = null;
	private static final Logger logger = JulUtils.getLogger();

	public static String[] getApplicationServersArray() {
		return applicationServersArray;
	}

	public static String getInstallWarsXmlFileName() {
		return installWarsXmlFileName;
	}

	public static String getProductBackupFolder() {
		return productBackupFolder;
	}

	public static String getProductFolder() {
		return productFolder;
	}

	public static String[] getProductFoldersArray() {
		return productFoldersArray;
	}

	public static String getProductVersionFolder() {
		return productVersionFolder;
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
		ParametersUtils.processPatameters(propertiesFilePath, ProductDeploymentParameters.class);
		system = PathUtils.getParentFolderName(propertiesFilePath);
	}

	public static void setApplicationServers(final String applicationServers) {
		ProductDeploymentParameters.applicationServers = applicationServers;
		applicationServersArray = applicationServers.split(OzConstants.COMMA);
	}

	public static void setProductBackupFolder(String productBackupFolder) {
		ProductDeploymentParameters.productBackupFolder = productBackupFolder;
	}

	public static void setProductFolder(final String productFolder) {
		ProductDeploymentParameters.productFolder = productFolder;
	}

	public static void setProductFolders(String productFolders) {
		productFoldersArray = productFolders.split(OzConstants.COMMA);
	}

	public static void setProductVersionFolder(String productVersionFolder) {
		ProductDeploymentParameters.productVersionFolder = productVersionFolder;
	}

	public static void setRemotePropertiesFilePath(final String remotePropertiesFilePath) {
		ProductDeploymentParameters.remotePropertiesFilePath = PathUtils.getFullPath(rootFolderPath,
				remotePropertiesFilePath);
	}

	public static void setRootFolderPath(final String rootFolderPath) {
		ProductDeploymentParameters.rootFolderPath = rootFolderPath;
	}

	public static void setXmlAccessCommand(String xmlAccessCommand) {
		ProductDeploymentParameters.xmlAccessCommand = xmlAccessCommand;
	}

	public static void setXmlAccessPassword(String xmlAccessPassword) {
		ProductDeploymentParameters.xmlAccessPassword = xmlAccessPassword;
	}

	public static void setXmlAccessUser(String xmlAccessUser) {
		ProductDeploymentParameters.xmlAccessUser = xmlAccessUser;
	}
}
