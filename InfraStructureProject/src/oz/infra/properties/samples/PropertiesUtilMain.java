package oz.infra.properties.samples;

import java.io.File;

/*******************************************************************************
 * This program demonstrates how to use the Properties class. It uses some
 * methods added in JDK 1.2 and will not work with JDK 1.1. You can add a
 * key-value pair to a properties file using java PropertiesExample filename set
 * key value and delete a key using java PropertiesExample filename del key and
 * list the property file using java PropertiesExample filename list If the file
 * does not exist when you add a key-value pair, it is created.
 ******************************************************************************/
public class PropertiesUtilMain {
	public static void printUsage() {
		System.err.println("Usage: PropertiesExample filename list|set|del [key] [value]");
	}

	public static void main(String[] args) {
		File file;
		String filename, key = null, value = null;
		String action;
		if (args.length < 1) {
			printUsage();
			errorExit();
		}
		filename = args[0];
		file = new File(filename);
		action = args[1];
		if (action.equals("set")) {
			if (args.length < 4) {
				printUsage();
				errorExit();
			}
			key = args[2];
			value = args[3];
		} else if (action.equals("del")) {
			if (args.length < 3) {
				printUsage();
				errorExit();
			}
			key = args[2];
		} else if (action.equals("list")) {
			if (args.length != 2) {
				printUsage();
				errorExit();
			}
		} else {
			printUsage();
			errorExit();
		}
		PropertiesUtil propertiesUtil = new PropertiesUtil();
		propertiesUtil.process(file, action, key, value);
	}

	final static void errorExit() {
		System.exit(-1);
	}
}
