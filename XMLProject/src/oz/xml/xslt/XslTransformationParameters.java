package oz.xml.xslt;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;

public class XslTransformationParameters {
	private static String xmlFilePath = null;
	private static String xmlOutPutFilePath = null;
	private static String xslFilesPath = null;
	private static String[] xslFilesArray = null;
	private static String username = null;
	private static String password = null;
	private static String rootFolderPath = null;
	private static Logger logger = JulUtils.getLogger();

	public static String getPassword() {
		return password;
	}

	public static String getUsername() {
		return username;
	}

	public static String getXmlFilePath() {
		return xmlFilePath;
	}

	public static String getXmlOutPutFilePath() {
		return xmlOutPutFilePath;
	}

	public static String[] getXslFilesArray() {
		return xslFilesArray;
	}

	public static void processParameters(final String propertiesFilePath) {
		logger.info("Starting. properties file path: " + propertiesFilePath);
		ParametersUtils.processPatameters(propertiesFilePath, XslTransformationParameters.class);
	}

	public static void setPassword(final String password) {
		XslTransformationParameters.password = password;
	}

	public static void setRootFolderPath(final String filePath) {
		rootFolderPath = PathUtils.getParentFolderPath(filePath);
		logger.finest(rootFolderPath);
	}

	public static void setUsername(final String username) {
		XslTransformationParameters.username = username;
	}

	public static void setXmlFilePath(final String xmlFilePath) {
		if (xmlFilePath.toLowerCase().startsWith(OzConstants.HTTP.toLowerCase())) {
			XslTransformationParameters.xmlFilePath = xmlFilePath;
		} else {
			XslTransformationParameters.xmlFilePath = PathUtils.getFullPath(rootFolderPath, xmlFilePath);
		}
	}

	public static void setXmlOutPutFilePath(final String xmlOutPutFilePath) {
		XslTransformationParameters.xmlOutPutFilePath = PathUtils.getFullPath(rootFolderPath, xmlOutPutFilePath);
	}

	public static void setXslFilesPath(final String xslFilesPath) {
		xslFilesArray = xslFilesPath.split(OzConstants.COMMA);
		for (int i = 0; i < xslFilesArray.length; i++) {
			xslFilesArray[i] = PathUtils.getFullPath(rootFolderPath, xslFilesArray[i]);
		}
	}
}
