package oz.infra.xml.keep;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.InputParameters;

public class XMLUtilsMain {
	private static String inXMLFilePath = null;
	private static String outXMLFilePath = null;
	private static String elementPath = null;
	private static String elementDelimiter = null;
	private static String attributeName = null;
	private static String newAttributeValue = null;
	private static String operation = null;
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		final String[][] parametersArray = { { "inXMLFilePath", null }, { "outXMLFilePath", null },
				{ "elementPath", null }, { "elementDelimiter", "#" }, { "attributeName", null },
				{ "newAttributeValue", null }, { "operation", null } };
		new InputParameters().processParameters(args, parametersArray, new XMLUtilsMain());
		logger.finest("Parameters processing done.");
		if (operation.equalsIgnoreCase("setXMLAttributeValue")) {
			XMLUtils.setXMLAttribValueByElementandAttribValue(inXMLFilePath, elementPath, elementDelimiter,
					attributeName, newAttributeValue, outXMLFilePath);
		}
	}

	public static final void setElementDelimiter(final String elementDelimiter) {
		XMLUtilsMain.elementDelimiter = elementDelimiter;
	}

	public static final void setElementPath(final String elementPath) {
		XMLUtilsMain.elementPath = elementPath;
	}

	public static final void setInXMLFilePath(final String inXMLFilePath) {
		XMLUtilsMain.inXMLFilePath = inXMLFilePath;
	}

	public static final void setAttributeName(final String attributeName) {
		XMLUtilsMain.attributeName = attributeName;
	}

	public static final void setNewAttributeValue(final String newAttributeValue) {
		XMLUtilsMain.newAttributeValue = newAttributeValue;
	}

	public static final void setOutXMLFilePath(final String outXMLFilePath) {
		XMLUtilsMain.outXMLFilePath = outXMLFilePath;
	}

	public static final void setOperation(final String operation) {
		XMLUtilsMain.operation = operation;
	}
}