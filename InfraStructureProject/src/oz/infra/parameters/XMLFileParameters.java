package oz.infra.parameters;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.jdom2.Attribute;
import org.jdom2.Element;

import oz.infra.logging.jul.JulUtils;
import oz.infra.xml.XMLUtils;

public final class XMLFileParameters {
	private static String myRootElementName = "InputParametersSet";
	private static String myElementName = "InputParameters";
	private static ArrayList<Hashtable<String, String>> inputParametersArrayList;
	private static Logger logger = JulUtils.getLogger();
	private static final String NOT_PERFORMED_MESSAGE = "Input parameters processing has not been performed.";

	private XMLFileParameters() {
	}

	/*
	 * getInputParameter
	 */
	public static String getInputParameter(final String parameterName) {
		if (inputParametersArrayList.size() == 1) {
			return getInputParameter(parameterName, 0);
		}
		logger.warning("Input parameters ordinal should be specified.\nProcessing aborted.");
		return null;
	}

	/*
	 * getInputParameter
	 */
	public static String getInputParameter(final String parameterName, final int i) {
		Hashtable<String, String> inputParametersHashTable = inputParametersArrayList.get(i);
		return inputParametersHashTable.get(parameterName);
	}

	/*
	 * setInputParameter
	 */
	public static void setInputParameter(final String inputParameterName, final String inputParameterValue,
			final Hashtable<String, String> inputParametersHashTable) {
		inputParametersHashTable.put(inputParameterName, inputParameterValue);
	}

	/*
	 * buildInputParameters
	 */
	public static int buildInputParameters(final String inputParametersFileName) {
		if (!(new File(inputParametersFileName).isFile())) {
			logger.severe(inputParametersFileName + " not found. \n" + NOT_PERFORMED_MESSAGE);
			throw new RuntimeException(inputParametersFileName + " not found. \n" + NOT_PERFORMED_MESSAGE);
		}
		inputParametersArrayList = new ArrayList<Hashtable<String, String>>();
		List<Element> children = XMLUtils.getRootElementChildren(inputParametersFileName, myRootElementName);
		if (children == null) {
			logger.warning(inputParametersFileName + " XML element not found. \n" + NOT_PERFORMED_MESSAGE);
			return -1;
		}
		StringBuilder sb = new StringBuilder();
		int childrenNumber = children.size();
		for (int i = 0; i < childrenNumber; i++) {
			sb.append("\n\nparameter " + String.valueOf(i));
			Element inputParameterElement = children.get(i);
			if (!inputParameterElement.getName().equalsIgnoreCase(myElementName)) {
				logger.warning(inputParametersFileName + " XML element not found. \n" + NOT_PERFORMED_MESSAGE);
				return -1;
			}
			List attributes = inputParameterElement.getAttributes();
			int attributesNumber = attributes.size();
			/*
			 * parameter processing
			 */

			Hashtable<String, String> inputParametersHashTable = new Hashtable<String, String>();
			for (int j = 0; j < attributesNumber; j++) {
				Attribute paramAttribute = (Attribute) attributes.get(j);
				inputParametersHashTable.put(paramAttribute.getName(), paramAttribute.getValue());
				sb.append("\n" + paramAttribute.getName() + "=" + paramAttribute.getValue());
			}
			inputParametersArrayList.add(inputParametersHashTable);

		}
		sb.append("\n");
		logger.info(sb.toString());
		return inputParametersArrayList.size();
	}
}