package oz.infra.parameters.test;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.string.StringUtils;

public class TestParametersUtils {
	private static String value1 = null;
	private static String value2 = null;
	private static String value3 = null;
	private static String rootFolderPath = null;
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testProcessPatameters4StaticClass();
	}

	public static void setRootFolderPath(String rootFolderPath) {
		TestParametersUtils.rootFolderPath = rootFolderPath;
	}

	public static void setValue1(final String value1) {
		TestParametersUtils.value1 = value1;
	}

	public static void setValue2(final String value2) {
		TestParametersUtils.value2 = value2;
	}

	public static void setValue3(final String value3) {
		TestParametersUtils.value3 = value3;
	}

	private static void testProcessPatameters4StaticClass() {
		ParametersUtils.processPatameters(
				"./src/oz/infra/parameters/test/data/prametersUtilsTest.properties",
				TestParametersUtils.class);
		logger.info(StringUtils.concat(value1, "\t", value2, "\t", value3, "\t", rootFolderPath));
	}
}
