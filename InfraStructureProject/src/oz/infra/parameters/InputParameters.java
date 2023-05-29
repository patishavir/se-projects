package oz.infra.parameters;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;

public class InputParameters {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private String[] args;
	private String[][] parametersArray;
	private Object obj;

	public final void processParameters(final String[] argsP, final String[][] parametersArrayP,
			Object objP) {
		args = argsP;
		parametersArray = parametersArrayP;
		obj = objP;
		proceessXMLFileParameters();
		processEnvironmentVariables();
		processCommandLineParameters();
		setValues();
	}

	/*
	 * setValues
	 */
	private void setValues() {
		Class myClass = obj.getClass();
		Method[] myMethodsArray = myClass.getDeclaredMethods();
		Method myMethod;
		Object[] setParamArray = { null };
		for (int i = 0; i < parametersArray.length; i++) {
			logger.finest("Set myField=" + parametersArray[i][0]);
			myMethod = null;
			String myLooking4Method = null;
			for (int j = 0; j < myMethodsArray.length; j++) {
				myLooking4Method = "set" + parametersArray[i][0];
				if (myLooking4Method.equalsIgnoreCase(myMethodsArray[j].getName())) {
					myMethod = myMethodsArray[j];
					logger.finer("myMethod: " + myMethod.getName());
					try {
						setParamArray[0] = parametersArray[i][1];
						logger.fine(parametersArray[i][0] + "=" + parametersArray[i][1]);
						myMethod.invoke(obj, setParamArray);

					} catch (Exception ex) {
						ex.printStackTrace();
						logger.warning(ex.getMessage());
					}
				}
			}
			if (myMethod == null) {
				logger.warning("method " + myLooking4Method
						+ " does not exit !. \nProcessing aborted.");
				System.exit(-1);
			}
		}
	}

	/*
	 * proceessXMLFileParameters
	 */
	private void proceessXMLFileParameters() {
		for (int i = 0; i < args.length; i++) {
			if (isXmlPatametersFile(args[i])) {
				// Process Xml file parameeters
				try {
					XMLFileParameters.buildInputParameters(args[i]);
				} catch (RuntimeException rte) {
					logger.severe("Processing terminated!");
					System.exit(-1);
				}
				for (int j = 0; j < parametersArray.length; j++) {
					String myParam = XMLFileParameters.getInputParameter(parametersArray[j][0]);
					if (myParam != null) {
						parametersArray[j][1] = myParam;
					}
					logger.finest("XML processing: " + parametersArray[j][0] + "="
							+ parametersArray[j][1]);
				}
			}
		}
	}

	/*
	 * processCommandLineParameters
	 */
	private void processCommandLineParameters() {
		Arrays.sort(args);
		int index1;
		int index2;
		for (int i = 0; i < args.length - 2; i++) {
			index1 = args[i].indexOf("=");
			index2 = args[i + 1].indexOf("=");
			String string1 = null;
			String string2 = null;
			if (index1 != -1) {
				string1 = args[i].substring(0, index1);
			}
			if (index2 != -1) {
				string2 = args[i + 1].substring(0, index2);
			}
			if (string1 != null && string2 != null && string1.equalsIgnoreCase(string2)) {
				logger.warning(args[i] + " duplicate command line parameter. Processing aborted!");
				System.exit(-1);
			}
		}
		int j = 0;
		for (int i = 0; i < args.length; i++) {
			if (isXmlPatametersFile(args[i])) {
				j++;
			}
		}
		if (j > 1) {
			logger.warning(" more than 1 xml parameter file. Processing aborted!");
			System.exit(-1);
		}
		for (int i = 0; i < args.length; i++) {
			if (isXmlPatametersFile(args[i])) {
				continue;
			}
			// Process command line parameters
			boolean matchFound = false;
			for (j = 0; j < parametersArray.length; j++) {
				String myParam = parametersArray[j][0] + "=";
				logger.finest("myParam=" + myParam);
				if ((myParam.length() < args[i].length())
						&& (myParam.equalsIgnoreCase(args[i].substring(0, myParam.length())))) {
					logger.finest("arg=" + args[i]);
					parametersArray[j][1] = args[i].substring(myParam.length());
					matchFound = true;
					break;
				}
			}
			if (!matchFound) {
				logger.warning("\n" + args[i]
						+ " :Invalid command line parameter.\nProcessing aborted !");
				System.exit(-1);
			}
		}
	}

	/*
	 * processEnvironmentVariables
	 */
	private void processEnvironmentVariables() {
		for (int i = 0; i < parametersArray.length; i++) {
			String value = System.getenv(parametersArray[i][0]);
			if (value != null) {
				parametersArray[i][1] = value.trim();
				logger.finest("var: \"" + parametersArray[i][0] + "\"");
				logger.finest("value: \"" + value + "\"");
			}
		}
	}

	/*
	 * isXmlPatametersFile
	 */
	private boolean isXmlPatametersFile(final String parameter) {
		return (parameter.toLowerCase().indexOf(".xml") != -1) && (parameter.indexOf("=") == -1);
	}
}