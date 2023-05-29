package oz.utils.snifit.dbload;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.reflection.ReflectionUtils;

public class DbLoadParameters {
	private static String folder2Watch = null;
	private static String backupFolderPath = null;
	private static String rootFolderPath = null;
	private static String remotePropertiesFilePath = null;
	private static Properties remoteProperties = null;

	private static Logger logger = JulUtils.getLogger();

	public static String getBackupFolderPath() {
		return backupFolderPath;
	}

	public static String getFolder2Watch() {
		return folder2Watch;
	}

	public static Properties getRemoteProperties() {
		return remoteProperties;
	}

	public static void processParameters(final String propertiesFilePath) {
		FileUtils.terminateIfFileDoesNotExist(propertiesFilePath);
		rootFolderPath = PathUtils.getParentFolderPath(propertiesFilePath);
		Properties props = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		PropertiesUtils.printProperties(props, Level.INFO);
		ReflectionUtils.setFieldsFromProperties(props, DbLoadParameters.class);
		logger.info("\nrootFolderPath: " + rootFolderPath + "\nfolderPath:" + folder2Watch);
	}

	public static void setBackupFolderPath(final String backupFolderPath) {
		DbLoadParameters.backupFolderPath = backupFolderPath;
		FolderUtils.createFolderIfNotExists(DbLoadParameters.backupFolderPath);
	}

	public static void setFolder2Watch(final String folder2Watch) {
		DbLoadParameters.folder2Watch = folder2Watch;
	}

	public static void setRemotePropertiesFilePath(final String remotePropertiesFilePath) {
		DbLoadParameters.remotePropertiesFilePath = PathUtils.getFullPath(rootFolderPath, remotePropertiesFilePath);
		remoteProperties = PropertiesUtils.loadPropertiesFile(DbLoadParameters.remotePropertiesFilePath);
	}
}
