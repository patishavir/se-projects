package oz.was.perfservlet;

import java.util.ArrayList;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;

public class PerfServletParameters {
	private static String xmlFilePath = null;
	private static String xmlOutPutFilePath = null;
	private static String xslFilesPath = null;
	private static String[] xslFilesArray = null;
	private static String username = null;
	private static String password = null;
	private static String rootFolderPath = null;
	private static ArrayList<String> xslsList = null;
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

	public static ArrayList<String> getXslsList() {
		return xslsList;
	}

	public static void processParameters(final String propertiesFilePath) {
		logger.info("Starting. properties file path: " + propertiesFilePath);
		ParametersUtils.processPatameters(propertiesFilePath, PerfServletParameters.class);
	}

	public static void setPassword(final String password) {
		PerfServletParameters.password = password;
	}

	public static void setRootFolderPath(final String filePath) {
		rootFolderPath = PathUtils.getParentFolderPath(filePath);
		logger.finest(rootFolderPath);
	}

	public static void setUsername(final String username) {
		PerfServletParameters.username = username;
	}

	public static void setXmlFilePath(final String xmlFilePath) {
		if (xmlFilePath.toLowerCase().startsWith(OzConstants.HTTP.toLowerCase())) {
			PerfServletParameters.xmlFilePath = xmlFilePath;
		} else {
			PerfServletParameters.xmlFilePath = PathUtils.getFullPath(rootFolderPath, xmlFilePath);
		}
	}

	public static void setXmlOutPutFilePath(final String xmlOutPutFilePath) {
		PerfServletParameters.xmlOutPutFilePath = PathUtils.getFullPath(rootFolderPath, xmlOutPutFilePath);
	}

	public static void setXslFilesPath(final String xslFilesPath) {
		xslFilesArray = xslFilesPath.split(OzConstants.COMMA);
		for (int i = 0; i < xslFilesArray.length; i++) {
			xslFilesArray[i] = PathUtils.getFullPath(rootFolderPath, xslFilesArray[i]);
		}
	}

	public static void setXslsList(ArrayList<String> xslsList) {
		PerfServletParameters.xslsList = xslsList;
	}
}
