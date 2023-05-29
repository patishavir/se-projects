package oz.utils.cm.ds.deployment;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.reflection.ReflectionUtils;

public class DsCmParameters {
	private static String sourceFolderPath = null;
	private static String sourceFolderName = null;
	private static String rootFolderPath = null;
	private static String project2UserMap = null;

	private static String additionalMailRecepients = null;
	private static final String DSX_FOLDER_NAME = "dsx";

	private static Logger logger = JulUtils.getLogger();

	public static String getAdditionalMailRecepients() {
		return additionalMailRecepients;
	}

	public static String getProject2UserMap() {
		return project2UserMap;
	}

	public static String getSourceFolderName() {
		return sourceFolderName;
	}

	public static String getSourceFolderPath() {
		return sourceFolderPath;
	}

	public static void processParameters(final String propertiesFilePath) {
		logger.finest("Current dir: " + FileUtils.getCurrentDir());
		setRootFolderPath(propertiesFilePath);
		logger.info("processing " + propertiesFilePath + " ...");
		Properties dsscmProperties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		dsscmProperties = PropertiesUtils.updatePropertiesUsingEnvironmentVarialbes(dsscmProperties);
		ReflectionUtils.setFieldsFromProperties(dsscmProperties, DsCmParameters.class);
		ReflectionUtils.setFieldsFromProperties(dsscmProperties, DsCommands.class);

		// rootFolderPath = PathUtils.getParentFolderPath(propertiesFilePath);
		// Properties dsscmProperties =
		// PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		// ParametersUtils.processPatameters4StaticClass(propertiesFilePath,
		// DsCmParameters.class);
		// ParametersUtils.processPatameters4StaticClass(propertiesFilePath,
		// DsCommands.class);
	}

	public static void setAdditionalMailRecepients(final String additionalMailRecepients) {
		DsCmParameters.additionalMailRecepients = additionalMailRecepients;
	}

	public static void setProject2UserMap(final String project2UserMap) {
		DsCmParameters.project2UserMap = PathUtils.getFullPath(rootFolderPath, project2UserMap);
	}

	private static void setRootFolderPath(final String filePath) {
		rootFolderPath = PathUtils.getParentFolderPath(filePath);
		logger.finest(rootFolderPath);
	}

	public static void setSourceFolderPath(final String sourceFolderPath) {
		sourceFolderName = DSX_FOLDER_NAME;
		DsCmParameters.sourceFolderPath = PathUtils.getFullPath(rootFolderPath, sourceFolderPath);
		DsCmParameters.sourceFolderPath = PathUtils.getFullPath(DsCmParameters.sourceFolderPath, sourceFolderName);
		logger.info("sourceFolderName: " + sourceFolderName);
	}
}
