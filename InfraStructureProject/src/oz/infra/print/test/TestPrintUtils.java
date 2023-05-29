/**
 * 
 */
package oz.infra.print.test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.KeyValuePair;
import oz.infra.print.PrintUtils;
import oz.infra.print.PrintUtils.PrintOption;
import oz.infra.print.test.data.TestPrinUtilsData;

/**
 * @author Oded
 * 
 */
public class TestPrintUtils {
	private String attribute1 = "val1attribute1";
	private String attribute2 = "val2attribute2";
	private static String staticAttribute1 = "staticValue1";
	private static String staticAttribute2 = "staticValue2";
	private static int i77 = 77;
	private static String[][] fieldNames = { { "staticAttribute1", "staticAttribute1 header1" },
			{ "staticAttribute2", "staticAttribute2 header2" }, { "i77", "i77 header 77" } };
	private static List<KeyValuePair<String, String>> fieldNamesList = new ArrayList<KeyValuePair<String, String>>();
	private static Logger logger = JulUtils.getLogger();

	// private static String[] fieldNames1 = { "attribute1", "attribute2" };
	// private static String[][] fieldNames2 = { { "attribute1", "attribute 1"
	// },
	// { "attribute2", "attribute 2" } };
	// private static Logger logger = JulUtils.getLogger();

	public static int getI77() {
		return i77;
	}

	public static String getStaticAttribute1() {
		return staticAttribute1;
	}

	public static String getStaticAttribute2() {
		return staticAttribute2;
	}

	private static void initializeList() {
		fieldNamesList.add(new KeyValuePair<String, String>("staticAttribute1", "staticAttribute1 header1"));
		fieldNamesList.add(new KeyValuePair<String, String>("staticAttribute2", "staticAttribute2 header2"));
		fieldNamesList.add(new KeyValuePair<String, String>("i77", "i77 header 77"));

	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// initializeList();
		// testPrintObjectFields();
		// testStaticPrintObjectFields();
		// testStaticPrintListObjectFields();
		testGetSeparatorLine();
	}

	// public static Logger getLogger() {
	// return logger;
	// }
	private static void testGetSeparatorLine() {
		String text = "balbl";
		logger.info(PrintUtils.getSeparatorLine(text));
		logger.info(PrintUtils.getSeparatorLine(text, "-"));
		logger.info(PrintUtils.getSeparatorLine(text, "+_"));
		logger.info(PrintUtils.getSeparatorLine(text, "+-="));
	}

	private static void testPrintObjectFields() {
		TestPrintUtils testPrinUtils = new TestPrintUtils();
		System.out.println(PrintUtils.printObjectFields(testPrinUtils, OzConstants.COMMA, PrintOption.HEADER_AND_DATA));
		System.out.println(PrintUtils.printObjectFields(testPrinUtils, fieldNames, OzConstants.COMMA,
				PrintOption.HEADER_AND_DATA));
	}

	private static void testStaticPrintListObjectFields() {
		logger.info(TestPrintUtils.class.toString());
		// logger.info(TestPrinUtils.class.getClass().toString());
		System.out.println(PrintUtils.printObjectFields(TestPrinUtilsData.class, fieldNamesList, OzConstants.COMMA,
				PrintOption.HEADER_AND_DATA));
	}

	// logger.info(PrintUtils.printObjectFields(testPrinUtils, fieldNames1,
	// OzConstants.COLON,
	// PrintOption.HEADER_AND_DATA));
	// logger.info(PrintUtils.printObjectFields(testPrinUtils, fieldNames1,
	// OzConstants.COLON,
	// PrintOption.HEADER_ONLY));
	// logger.info(PrintUtils.printObjectFields(testPrinUtils, fieldNames1,
	// OzConstants.COLON,
	// PrintOption.DATA_ONLY));
	// logger.info(PrintUtils.printObjectFields(testPrinUtils, fieldNames2,
	// OzConstants.COLON,
	// PrintOption.HEADER_ONLY));
	// logger.info(PrintUtils.printObjectFields(testPrinUtils, fieldNames2,
	// OzConstants.COLON,
	// PrintOption.DATA_ONLY));
	// logger.info(PrintUtils.printObjectFields(testPrinUtils, fieldNames2,
	// OzConstants.COLON,
	// PrintOption.HEADER_AND_DATA));
	// public static String[] getFieldNames1() {
	// return fieldNames1;
	// }

	private static void testStaticPrintObjectFields() {
		logger.info(TestPrintUtils.class.toString());
		// logger.info(TestPrinUtils.class.getClass().toString());
		System.out.println(PrintUtils.printObjectFields(TestPrintUtils.class, fieldNames, OzConstants.COMMA,
				PrintOption.HEADER_AND_DATA));
	}

	public String getAttribute1() {
		return attribute1;
	}

	// public static String[][] getFieldNames2() {
	// return fieldNames2;
	// }

	public String getAttribute2() {
		return attribute2;
	}
}
