package oz.infra.regexp.test;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;

public class TestRegExpUtils {
	private static Logger logger = JulUtils.getLogger();

	private static void ddmmyyyyTest() {
		runTest("23042017", RegexpUtils.REGEXP_DDMMYYY_DATE);
		runTest("28122097", RegexpUtils.REGEXP_DDMMYYY_DATE);
		runTest("01012001", RegexpUtils.REGEXP_DDMMYYY_DATE);
		runTest("01012101", RegexpUtils.REGEXP_DDMMYYY_DATE);
		runTest("23043017", RegexpUtils.REGEXP_DDMMYYY_DATE);
		runTest("23072117", RegexpUtils.REGEXP_DDMMYYY_DATE);
		runTest("23072173", RegexpUtils.REGEXP_DDMMYYY_DATE);
		runTest("31132017", RegexpUtils.REGEXP_DDMMYYY_DATE);
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		runTest("1.ear", RegexpUtils.REGEXP_EAR_OR_WAR_FILE);
		runTest("1.Ear", RegexpUtils.REGEXP_EAR_OR_WAR_FILE);
		runTest("1.war", RegexpUtils.REGEXP_EAR_OR_WAR_FILE);
		runTest("1.WAR", RegexpUtils.REGEXP_EAR_OR_WAR_FILE);
		runTest("1.war.war2", RegexpUtils.REGEXP_EAR_OR_WAR_FILE);
		runTest("war.bar", RegexpUtils.REGEXP_EAR_OR_WAR_FILE);
		runTest("war.ear2", RegexpUtils.REGEXP_EAR_OR_WAR_FILE);
		System.exit(0);
		ddmmyyyyTest();
		runTest("1.ear", RegexpUtils.REGEXP_EAR_FILE);
		runTest("1.EAr", RegexpUtils.REGEXP_EAR_FILE);
		runTest("1.EAR", RegexpUtils.REGEXP_EAR_FILE);
		runTest("1.earq", RegexpUtils.REGEXP_EAR_FILE);
		runTest("1.earr", RegexpUtils.REGEXP_EAR_FILE);
		runTest("1ear.r", RegexpUtils.REGEXP_EAR_FILE);
		runTest("1.sqL", RegexpUtils.REGEXP_SQL_FILE);
		runTest("1.SQL", RegexpUtils.REGEXP_SQL_FILE);
		runTest("1.sql", RegexpUtils.REGEXP_SQL_FILE);
		runTest("1.sqll", RegexpUtils.REGEXP_SQL_FILE);
		runTest("nan.eaR", RegexpUtils.REGEXP_EAR_FILE);
		runTest("didi.EAr", RegexpUtils.REGEXP_EAR_FILE);
		runTest("q1234_78.EAR", RegexpUtils.REGEXP_EAR_FILE);
		runTest("q1234_78-111.EAr", RegexpUtils.REGEXP_EAR_FILE);
		runTest("q1234_78-111#.Ear", RegexpUtils.REGEXP_EAR_FILE);
		runTest("q1234_78-111#~.EAr", RegexpUtils.REGEXP_EAR_FILE);
		//
		runTest("q1234_78-111#.EarA", RegexpUtils.REGEXP_EAR_FILE);
		runTest("q1234_78-111#.BIra", RegexpUtils.REGEXP_EAR_FILE);
		runTest("q1234_78-111#.ear1", RegexpUtils.REGEXP_EAR_FILE);
		runTest("q1234_78-111#.bear", RegexpUtils.REGEXP_EAR_FILE);
		runTest("bear", RegexpUtils.REGEXP_EAR_FILE);
		System.exit(0);
		runTest("1.2.3.4", RegexpUtils.REGEXP_IPV4_ADDRESS);
		runTest("255.255.255.255", RegexpUtils.REGEXP_IPV4_ADDRESS);
		runTest("0.0.0", RegexpUtils.REGEXP_IPV4_ADDRESS);
		runTest("256.255", RegexpUtils.REGEXP_IPV4_ADDRESS);
		runTest("255.255,255.255", RegexpUtils.REGEXP_IPV4_ADDRESS);
		runTest("1.2.3.455", RegexpUtils.REGEXP_IPV4_ADDRESS);
		runTest("1.2.3.4.", RegexpUtils.REGEXP_IPV4_ADDRESS);
		runTest("256.255.255.255", RegexpUtils.REGEXP_IPV4_ADDRESS);
		runTest("255.256.255.255", RegexpUtils.REGEXP_IPV4_ADDRESS);
		runTest("255.255.255.256", RegexpUtils.REGEXP_IPV4_ADDRESS);
		runTest("255.255.255.2556", RegexpUtils.REGEXP_IPV4_ADDRESS);
		runTest("=.sql", RegexpUtils.REGEXP_SQL_FILE);
		runTest("=.sql=", RegexpUtils.REGEXP_SQL_FILE);
		runTest("sql=.qql=", RegexpUtils.REGEXP_SQL_FILE);
		System.exit(0);
		runTest("=.dsx", "\\S+.dsx");
		runTest("23-4473919.dsx", "\\S+.dsx");
		runTest("23-4473919.dsxx", "\\S+.dsx");
		runTest("23-447_3919.DSX", "\\S+.dsx");
		runTest(".dsx", "\\S+.dsx");

		runTest("123-4473919", RegexpUtils.REGEXP_CELL_PHONE_NUMBER);
		runTest("000-0000000", RegexpUtils.REGEXP_CELL_PHONE_NUMBER);
		runTest("000-9999999", RegexpUtils.REGEXP_CELL_PHONE_NUMBER);
		runTest("000-99999990", RegexpUtils.REGEXP_CELL_PHONE_NUMBER);
		runTest("000+9999999", RegexpUtils.REGEXP_CELL_PHONE_NUMBER);
		runTest("s177571", RegexpUtils.REGEXP_CELL_PHONE_NUMBER);
		runTest("123_4473919", RegexpUtils.REGEXP_CELL_PHONE_NUMBER);

		//
		logger.finest(String.valueOf(RegexpUtils.matches("123", RegexpUtils.REGEXP_DIGITS)));
		logger.finest(String.valueOf(RegexpUtils.matches("X23", RegexpUtils.REGEXP_DIGITS)));
		logger.finest(String.valueOf(RegexpUtils.matches("123", Pattern.compile(RegexpUtils.REGEXP_DIGITS))));
		logger.finest(String.valueOf(RegexpUtils.matches(" 123", Pattern.compile(RegexpUtils.REGEXP_DIGITS))));
		logger.finest(String.valueOf(RegexpUtils.lookingAt("  3  ", "3")));
		logger.finest(String.valueOf(RegexpUtils.find("  3  ", "3")));
		logger.finest(String.valueOf(RegexpUtils.matches("  3  ", "3")));
		logger.finest(String
				.valueOf(RegexpUtils.matches("3", RegexpUtils.REGEXP_ANYSTRING + "3" + RegexpUtils.REGEXP_ANYSTRING)));
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
				+ "<HEADER appId=\"/MatafServer_UE\" date=\"21/10/2010-12:29:37\" serverName=\"S603FA00\">\n"
				+ "<TEST_TYPE id=\"2\" name=\"CicsSmCommand\">" + " <RET_CODE>success</RET_CODE>\n" + "</HEADER>\n";
		logger.finest(xmlString);
		logger.finest(String.valueOf(RegexpUtils.matches(xmlString,
				RegexpUtils.REGEXP_ANYSTRING + "success" + RegexpUtils.REGEXP_ANYSTRING)));
		logger.finest(String.valueOf(RegexpUtils.matches(xmlString, "[.\n\r]" + "success" + "[.\n\r]")));
	}

	private static void runTest(final String string, final String regexp) {
		logger.info("String: " + string + " regexp: " + regexp + "  "
				+ String.valueOf(RegexpUtils.matches(string, regexp)));

	}
}
