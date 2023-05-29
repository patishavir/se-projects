package oz.infra.string.test;

import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.bidi.BidiUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.string.StringUtils;
import oz.infra.string.StringUtils.PaddingDirection;
import oz.infra.string.WithinRangeEnum;

public class TestStringUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// testConcatenate();
		// testIsNumeric();
		// testIsFloatingPointNumber();
		// testGetEntryContainedInString();
		// testGetEntryStringStartsWith();
		// testGetEntryStartingWithString();
		// testRemoveCharacterFromString();
		testRange();
		// testRepeat();
		// testPad();
		// System.exit(OzConstants.EXIT_STATUS_OK);
		// testGetSubStringMatchingPattern("01234567", 0);
		// testGetSubStringMatchingPattern("0123X4567", 0);
		// testGetSubStringMatchingPattern("0123X4567", 2);
		// testGetSubStringMatchingPattern("0123X4567", 17);
		// substituteEnvironmentVariables();
		// testRemoveTrailingSubString();
		// testTrimArray();
		// testStringArray2String();
		// testRemoveCharacterFromString1();
		// testContainsHebrewCharacters("yuiyuijkhjkhj9987×”8978");
		// testGetNextLine();
		// testSubstitute();
		// testInsert();
		// testGetSubStringIndexes();
		// testGetUnique();
		// testGetSubStringIndexes();
	}

	private static void testGetUnique() {
		StringUtils.getUniqeFromCsv("hhh,yyy,hhh,yyy,xxx");
		StringUtils.getUniqeFromCsv("hhh,111,222,333,444");
		StringUtils.getUniqeFromCsv("hhh,111,222,333,444,111,hhh,444");

		StringUtils.getUniqeFromCsv(
				"T180394,T184667,T184594,T707065,T171948,T174122,T156159,T174106,s177571,T704609,T181722,T176982,T181676,T103063,T113131,T027839,T145858,T180025,T192473,T195103,T181781,T109932,T180467,T112623,T706677,D188700,T184713,T180408,T184799,T192511,T177520,T180076,T151890,T707188,T706444,T116874,T176397,T201774,T162051,T707736,T707531,T707368,T181420,T181498,T188751,T165840,T703534,T156140,T141216");

	}

	private static void substituteEnvironmentVariables() {
		String[] stringArray = { "xxx", "yyy", "%COMPUTERNAME%" };
		ArrayUtils.printArray(StringUtils.substituteEnvironmentVariables(stringArray));
	}

	private static void testConcatenate() {
		logger.info(StringUtils.concat("qqq", "aaa", "zzz"));
		logger.info(StringUtils.concat("111"));
		logger.info(StringUtils.concat("111", "\n", "222", "", "3"));
		logger.info(StringUtils.concat(null));
	}

	private static void testContainsHebrewCharacters(final String string) {
		testContainsHebrewCharacters1("yuiyuijkhjkhj99878978");
		testContainsHebrewCharacters1("yuiyuijkhjkhj9987×”8978");
		testContainsHebrewCharacters1("yuiyuijkhjkhj9987×ª");
		testContainsHebrewCharacters1("yuiyuijkhjkhj9987×ª");
		testContainsHebrewCharacters1("yuiyuijkhjkhj99878978×");
		testContainsHebrewCharacters1("yuiyuijkhjkhj99878978×");
		testContainsHebrewCharacters1("×–yuiyuijkhjkhj99878978");
		testContainsHebrewCharacters1("1234567890");
		testContainsHebrewCharacters1("1234567890×");
		testContainsHebrewCharacters1("1234567890×");
		testContainsHebrewCharacters1("12345×67890");
	}

	private static void testContainsHebrewCharacters1(final String string) {
		try {
			logger.info(StringUtils.containsHebrewCharacters(string) + "   " + string + "    "
					+ BidiUtils.convertV2L(string));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void testGetEntryContainedInString() {
		String[] stringArray = { "qaz", "wsxedc", "098poi" };
		logger.info(String.valueOf(StringUtils.getEntryContainedInString(stringArray, "nnn.qaz.fibi.")));
		logger.info(String.valueOf(StringUtils.getEntryContainedInString(stringArray, "qa")));
		logger.info(String.valueOf(StringUtils.getEntryContainedInString(stringArray, "098poi")));
		logger.info(String.valueOf(StringUtils.getEntryContainedInString(stringArray, "098poio")));
		logger.info(String.valueOf(StringUtils.getEntryContainedInString(null, "wsxedc")));
		logger.info(String.valueOf(StringUtils.getEntryContainedInString(stringArray, "wsxedc.bibi")));
		logger.info(String.valueOf(StringUtils.getEntryContainedInString(stringArray, "wsxedcq.bibi")));
		logger.info(String.valueOf(StringUtils.getEntryContainedInString(stringArray, "wsxedc")));
		logger.info(String.valueOf(StringUtils.getEntryStringStartsWith(stringArray, "wsxed")));
	}

	private static void testGetEntryStartingWithString() {
		String[] stringArray = { "qaz", "wsxedc", "098poi" };
		logger.info(String.valueOf(StringUtils.getEntryStartingWithString(stringArray, "bbb")));
		logger.info(String.valueOf(StringUtils.getEntryStartingWithString(stringArray, "qa")));
		logger.info(String.valueOf(StringUtils.getEntryStartingWithString(stringArray, "098poi")));
		logger.info(String.valueOf(StringUtils.getEntryStartingWithString(stringArray, "098poio")));
		logger.info(String.valueOf(StringUtils.getEntryStartingWithString(null, "wsxedc")));
		logger.info(String.valueOf(StringUtils.getEntryStartingWithString(stringArray, "wsxedc")));
		logger.info(String.valueOf(StringUtils.getEntryStartingWithString(stringArray, "wsxedc.bibi")));
	}

	private static void testGetEntryStringStartsWith() {
		String[] stringArray = { "qaz", "wsxedc", "098poi" };
		logger.info(String.valueOf(StringUtils.getEntryStringStartsWith(stringArray, "qaz.fibi.")));
		logger.info(String.valueOf(StringUtils.getEntryStringStartsWith(stringArray, "qa")));
		logger.info(String.valueOf(StringUtils.getEntryStringStartsWith(stringArray, "098poi")));
		logger.info(String.valueOf(StringUtils.getEntryStringStartsWith(stringArray, "098poio")));
		logger.info(String.valueOf(StringUtils.getEntryStringStartsWith(null, "wsxedc")));
		logger.info(String.valueOf(StringUtils.getEntryStringStartsWith(stringArray, "wsxedc.bibi")));
		logger.info(String.valueOf(StringUtils.getEntryStringStartsWith(stringArray, "wsxedcq.bibi")));
		logger.info(String.valueOf(StringUtils.getEntryStringStartsWith(stringArray, "wsxedc")));
		logger.info(String.valueOf(StringUtils.getEntryStringStartsWith(stringArray, "wsxed")));
	}

	private static void testGetNextLine() {
		String text = "xxx\nyyy\n\noo";
		int startIndex = 0;
		String delimiter = "\n";
		String line = null;
		while (true) {
			line = StringUtils.getNextString(text, startIndex, delimiter);
			logger.info(line);
			startIndex += line.length();
			if (startIndex >= text.length()) {
				break;
			}
		}
		startIndex = text.length();
		line = StringUtils.getNextString(text, startIndex, delimiter);
		logger.info(line);
		//
		startIndex = text.length() - 1;
		line = StringUtils.getNextString(text, startIndex, delimiter);
		logger.info(line);
		//
		text = null;
		line = StringUtils.getNextString(text, startIndex, delimiter);
	}

	private static void testGetSubStringIndexes() {
		String masterString = "/*1/*111/*2/*0*//*1/\\";
		String string2Index = "/*1";
		int[] indexesArray = StringUtils.getSubStringIndexes(masterString, string2Index);
		logger.info("masterString: " + masterString + "  string2Index: " + string2Index + "  inedxes: "
				+ ArrayUtils.getAsDelimitedString(indexesArray, OzConstants.BLANK));
	}

	private static void testGetSubStringMatchingPattern(final String testString, final int index) {
		logger.info(testString + "    "
				+ StringUtils.getSubStringMatchingPattern(testString, RegexpUtils.REGEXP_DIGIT, index));
	}

	public static void testInsert() {
		String toString = "abcdefghijklmnopqrstuvwxyz";
		logger.info(toString);
		logger.info(StringUtils.overlay("1234567890", toString, 0));
		logger.info(StringUtils.overlay("1234567890", toString, 1));
		logger.info(StringUtils.overlay("1234567890", toString, 2));
	}

	private static void testIsFloatingPointNumber() {
		testIsFloatingPointNumber1("0.0");
		testIsFloatingPointNumber1("999999");
		testIsFloatingPointNumber1("999999.1100");
		testIsFloatingPointNumber1("+999999.1100");
		testIsFloatingPointNumber1("-8999999.1100");
		testIsFloatingPointNumber1("-8999999+.1100");
		testIsFloatingPointNumber1("-8999999.");
	}

	private static void testIsFloatingPointNumber1(final String param) {
		logger.info(param + "  " + StringUtils.isFloatingPointNumber(param));
	}

	private static void testIsNumeric() {
		logger.info("testIsNumeric");
		testIsNumeric1("");
		testIsNumeric1("00");
		testIsNumeric1("0y0");
		testIsNumeric1("123.");
		testIsNumeric1("+7");
		testIsNumeric1("0987");
	}

	private static void testIsNumeric1(final String param) {
		logger.info(param + "  " + StringUtils.isJustDigits(param));
	}

	private static void testPad() {
		String string = "12345";
		char paddingCharacter = '*';
		int totalLength = 10;

		logger.info(StringUtils.pad(string, paddingCharacter, totalLength, PaddingDirection.LEFT));
		logger.info(StringUtils.pad(string, paddingCharacter, totalLength, PaddingDirection.RIGHT));
	}

	private static void testRange() {
		testRange1("04-00", "06-30", "10-00");
		testRange1("06-30", "06-30", "10-00");
		testRange1("07-00", "06-30", "10-00");
		testRange1("10-00", "06-30", "10-00");
		testRange1("10-30", "06-30", "10-00");
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
		testRange1("10-00", "06-30", "10-00", WithinRangeEnum.ExcludeLowExcludeHigh);
		testRange1("10-00", "06-30", "10-00", WithinRangeEnum.ExcludeLowIncludeHigh);
		testRange1("10-00", "06-30", "10-00", WithinRangeEnum.IncludeLowIncludeHigh);
		testRange1("10-00", "06-30", "10-00", WithinRangeEnum.IncludeLowExcludeHigh);
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
		testRange1("06-30", "06-30", "10-00", WithinRangeEnum.ExcludeLowExcludeHigh);
		testRange1("06-30", "06-30", "10-00", WithinRangeEnum.ExcludeLowIncludeHigh);
		testRange1("06-30", "06-30", "10-00", WithinRangeEnum.IncludeLowIncludeHigh);
		testRange1("06-30", "06-30", "10-00", WithinRangeEnum.IncludeLowExcludeHigh);
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
		testRange1("06-5", "06-30", "10-00", WithinRangeEnum.ExcludeLowExcludeHigh);
		testRange1("06-30", "06-30-40", "10-00", WithinRangeEnum.ExcludeLowIncludeHigh);
		testRange1("06-30", "06-30", "10-00-20", WithinRangeEnum.IncludeLowIncludeHigh);
		
	}

	private static void testRange1(final String string2Compare, final String lowRange, final String highRange,
			final WithinRangeEnum... withinRangeEnums) {
		boolean result = StringUtils.isStringWithinRage(string2Compare, lowRange, highRange, withinRangeEnums);
		logger.info("string2Compare: " + string2Compare + " lowRange:" + lowRange + " highRange:" + highRange
				+ " WithinRangeEnum: " + ArrayUtils.getAsDelimitedString(withinRangeEnums) + " result: "
				+ String.valueOf(result));

	}

	private static void testRemoveCharacterFromString() {
		logger.info(StringUtils.removeCharacterFromString("xxxyyyzzz", 'x'));
		logger.info(StringUtils.removeCharacterFromString("xxxyyyzzz", 'y'));
		logger.info(StringUtils.removeCharacterFromString("xxxyyyzzz", 'z'));
	}

	private static void testRemoveCharacterFromString1() {
		logger.info(StringUtils.removeCharacterFromString("1234567", "17"));
		logger.info(StringUtils.removeCharacterFromString("0123456789", "a1b7c"));
		logger.info(StringUtils.removeCharacterFromString("0123456789", "\n\r\t"));
		logger.info(StringUtils.removeCharacterFromString("0123456789", "\n1\r2\t3"));
	}

	private static void testRemoveTrailingSubString() {
		String string = "123456  ";
		String subString = "6";
		String delimiter = OzConstants.NUMBER_SIGN;
		logger.info(StringUtils.concat(string, delimiter, subString, delimiter,
				StringUtils.removeTrailingSubString(string, subString), delimiter));
		string = "123456";
		logger.info(StringUtils.concat(string, delimiter, subString, delimiter,
				StringUtils.removeTrailingSubString(string, subString), delimiter));
		subString = "123456  ";
		logger.info(StringUtils.concat(string, delimiter, subString, delimiter,
				StringUtils.removeTrailingSubString(string, subString), delimiter));
		subString = "123456";
		logger.info(StringUtils.concat(string, delimiter, subString, delimiter,
				StringUtils.removeTrailingSubString(string, subString), delimiter));
	}

	private static void testRepeat() {
		logger.info(StringUtils.repeatChar('7', 3));
		logger.info(StringUtils.repeatString("*", 0));
		logger.info(StringUtils.repeatString("*", 1));
		logger.info(StringUtils.repeatString("*", 2));
		logger.info(StringUtils.repeatString("12", 0));
		logger.info(StringUtils.repeatString("12", 1));
		logger.info(StringUtils.repeatString("12", 2));
	}

	private static void testStringArray2String() {
		String[] inArray = { "aaa                    ", "bbb                     ", "ccccccccc                      " };
		logger.info(StringUtils.stringArray2String(inArray, OzConstants.COMMA) + OzConstants.COMMA);
	}

	private static void testSubstitute() {
		String string2Change = "xxx %reportDate% yyy ^snaif%";
		String sourcePattern = "%reportDate%";
		String destPattern = DateTimeUtils.formatCurrentTime(DateTimeUtils.DATE_FORMAT_dd_MMMMMM_yyyy);
		logger.info(StringUtils.substitute(string2Change, sourcePattern, destPattern));
	}

	private static void testTrimArray() {
		String[] inArray = { "aaa                    ", "bbb                     ", "ccccccccc                      " };
		ArrayUtils.printArray(StringUtils.trimArray(inArray), OzConstants.COMMA);
	}
}
