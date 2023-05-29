package oz.test.infra;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.regexp.RegexpUtils;

public class TestRegexpUtils {
	private static Logger logger = Logger.getLogger(TestRegexpUtils.class.getName());

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// testLokkingAt("123", RegexpUtils.REGEXP_NON_WHITE_SPACE);
		testLokkingAt("123", RegexpUtils.REGEXP_NON_DIGIT);
		testLokkingAt("123 ", RegexpUtils.REGEXP_NON_DIGIT);
		testLokkingAt("123 \n\t", RegexpUtils.REGEXP_NON_DIGIT);
	}

	private static void testLokkingAt(final String inputString, final String regexpression) {
		logger.info(inputString + OzConstants.TAB + regexpression + OzConstants.TAB
				+ RegexpUtils.lookingAt(inputString, regexpression));
	}

}
