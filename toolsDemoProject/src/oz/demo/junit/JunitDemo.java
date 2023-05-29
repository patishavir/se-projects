package oz.demo.junit;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.string.StringUtils.PaddingDirection;

public class JunitDemo {
	private static java.util.logging.Logger logger = JulUtils.getLogger();
	private String str = null;

	@Test
	public void testPad() {
		logger.info("Start testPad");
		String paddedString = StringUtils.pad("12345", 'T', 10,
				PaddingDirection.LEFT);
		logger.info(paddedString);
		assertEquals(paddedString, "TTTTT12345");
		//
		paddedString = StringUtils.pad("12345", '!', 10,
				PaddingDirection.RIGHT);
		logger.info(paddedString);
		assertEquals(paddedString, "12345!!!!!");
		//
		paddedString = StringUtils.pad("1234567890abc", '!', 10,
				PaddingDirection.RIGHT);
		logger.info(paddedString);
		assertEquals(paddedString, "1234567890abc");
		//
		paddedString = StringUtils.pad("1234567890abc", '!', 10,
				PaddingDirection.RIGHT);
		logger.info(paddedString);
		assertEquals(paddedString, "1234567890abc");
		logger.info("End testPad");
	}

	//
	@Before
	public void berforeFirst2Upper() {
		str = "qwerty";
		logger.info("str:" + str);
	}
	@Test
	public void testFirst2Upper() {
		logger.info("Start testFirst2Upper");
		logger.info("str:" + str);
		String str1 = StringUtils.first2Upper(str);
		assertEquals(str1, "Qwerty");
		logger.info("str1:" + str1);
	}
	@After
	public void afterFirst2Upper() {
		str = null;
		logger.info("str:" + str);
	}

}