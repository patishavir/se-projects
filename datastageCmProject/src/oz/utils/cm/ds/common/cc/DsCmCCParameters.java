package oz.utils.cm.ds.common.cc;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.reflection.ReflectionUtils;

public class DsCmCCParameters {
	private static String rootFolderPath = null;
	private static String viewTag = null;
	private static String ccViewRootFolderPath = null;
	private static String mailRecepients = null;
	private static boolean importToClearCase = true;
	private static boolean anything2Import = false;
	private static String streamSelector = null;
	private static String stgloc = null;
	private static Logger logger = JulUtils.getLogger();

	public static String getCcViewRootFolderPath() {
		return ccViewRootFolderPath;
	}

	public static String getMailRecepients() {
		return mailRecepients;
	}

	public static String getStgloc() {
		return stgloc;
	}

	public static String getStreamSelector() {
		return streamSelector;
	}

	public static String getViewTag() {
		return viewTag;
	}

	public static boolean isAnything2Import() {
		return anything2Import;
	}

	public static boolean isImportToClearCase() {
		return importToClearCase;
	}

	public static void processParameters(final String propertiesFilePath) {
		logger.finest("Current dir: " + FileUtils.getCurrentDir());
		setRootFolderPath(propertiesFilePath);
		logger.info("processing " + propertiesFilePath + " ...");
		Properties dsscmCcProperties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		dsscmCcProperties = PropertiesUtils.updatePropertiesUsingEnvironmentVarialbes(dsscmCcProperties);
		ReflectionUtils.setFieldsFromProperties(dsscmCcProperties, DsCmCCParameters.class);
	}

	public static void setAnything2Import(final boolean anything2Import) {
		DsCmCCParameters.anything2Import = anything2Import;
	}

	public static void setCcViewRootFolderPath(final String ccViewRootFolderPath) {
		DsCmCCParameters.ccViewRootFolderPath = ccViewRootFolderPath;
	}

	public static void setImportToClearCase(final boolean importToClearCase) {
		DsCmCCParameters.importToClearCase = importToClearCase;
	}

	public static void setMailRecepients(final String mailRecepients) {
		DsCmCCParameters.mailRecepients = mailRecepients;
	}

	private static void setRootFolderPath(final String filePath) {
		rootFolderPath = PathUtils.getParentFolderPath(filePath);
		logger.finest(rootFolderPath);
	}

	public static void setStgloc(String stgloc) {
		DsCmCCParameters.stgloc = stgloc;
	}

	public static void setStreamSelector(String streamSelector) {
		DsCmCCParameters.streamSelector = streamSelector;
	}

	public static void setViewTag(final String viewTag) {
		DsCmCCParameters.viewTag = viewTag;
	}
}
