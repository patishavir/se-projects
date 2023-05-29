package oz.test.infra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import oz.infra.array.ArrayUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.string.StringUtils.PaddingDirection;

public class TestStringUtils {
	private static Logger logger = JulUtils.getLogger();

	@Test
	public void testPad() {
		logger.info("Start testPad");
		String paddedString = StringUtils.pad("12345", 'T', 10, PaddingDirection.LEFT);
		logger.info(paddedString);
		assertEquals(paddedString, "TTTTT12345");
		//
		paddedString = StringUtils.pad("12345", '!', 10, PaddingDirection.RIGHT);
		logger.info(paddedString);
		assertEquals(paddedString, "12345!!!!!");
		//
		paddedString = StringUtils.pad("1234567890abc", '!', 10, PaddingDirection.RIGHT);
		logger.info(paddedString);
		assertEquals(paddedString, "1234567890abc");
		logger.info("End testPad");
		//
	}

	@Test
	public void testFirstCharToUpperCase() {
		assertEquals(StringUtils.first2Upper(""), "");
		logger.info(StringUtils.first2Upper("") + "=" + "");
		assertEquals(StringUtils.first2Upper("a"), "A");
		logger.info(StringUtils.first2Upper("a") + "=" + "A");
		assertEquals(StringUtils.first2Upper("A"), "A");
		logger.info(StringUtils.first2Upper("A") + "=" + "A");
		assertEquals(StringUtils.first2Upper("a123A"), "A123A");
		logger.info(StringUtils.first2Upper("a123A") + "=" + "A123A");
		assertEquals(StringUtils.first2Upper("123A"), "123A");
		logger.info(StringUtils.first2Upper("123A") + "=" + "123A");
		logger.info("End testFirstCharToUpperCase");
	}

	@Test
	public void testSubstituteEnvironmentVariables() {
		logger.info("Start testSubstituteEnvironmentVariables");
		//
		logger.info(StringUtils.substituteEnvironmentVariables("123%COMPUTERNAME%zzz"));
		logger.info(StringUtils.substituteEnvironmentVariables("123%COMPUTERNAME%zzz%USERNAME%%"));
		logger.info(StringUtils
				.substituteEnvironmentVariables("%USERDOMAIN%123%COMPUTERNAME%zzz%USERNAME%%"));
		logger.info(StringUtils
				.substituteEnvironmentVariables("%gadon%%USERDOMAIN%123%COMPUTERNAME%zzz%USERNAME%%"));
		logger.info(StringUtils
				.substituteEnvironmentVariables("%gadon%%USERDOMAIN%123%COMPUTERNAME%zzz%USERNAME%%Q"));
		logger.info(StringUtils.substituteEnvironmentVariables("ABC123"));
		String nullString = null;
		assertNull(StringUtils.substituteEnvironmentVariables(nullString));
		String[] nullArray = null;
		assertNull(StringUtils.substituteEnvironmentVariables(nullArray));
		//
		logger.info("End testSubstituteEnvironmentVariables");
	}

	@Test
	public void testPtintObjectArray() {
		String[] stringArray = { "aaa", "CCC", "ZZZ", "jjj", "1234567890" };
		ArrayUtils.printArray(stringArray);
		assert (true);
		String[][] stringArray2 = { { "aaa", "CCC", "0p9o8i7u" }, { "ZZZ", "jjj", "1234567890" },
				{ "poiuyt", "hgfds", "zxcvbnm" } };
		ArrayUtils.print2DimArray(stringArray2, Level.INFO);
		assert (true);
	}

	@Test
	public void testDelimitedSubString() {
		String delimitedSubString = StringUtils.getDelimitedSubString("%123456qaz%wsxedc", "%");
		assertEquals(delimitedSubString, "123456qaz");
		delimitedSubString = StringUtils.getDelimitedSubString("%%123456qaz%wsxedc", "%");
		assertEquals(delimitedSubString, "");
		delimitedSubString = StringUtils.getDelimitedSubString("123456qazwsxedc%%", "%");
		assertEquals(delimitedSubString, "");
		delimitedSubString = StringUtils.getDelimitedSubString("123456qazwsxedc%", "%");
		assertNull(delimitedSubString);
	}
}