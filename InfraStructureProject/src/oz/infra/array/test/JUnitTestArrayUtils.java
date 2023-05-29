package oz.infra.array.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class JUnitTestArrayUtils {
	private static final String NEW_LINE = SystemUtils.LINE_SEPARATOR;
	private static Logger logger = JulUtils.getLogger();

	@BeforeClass
	public static void beforeClass() {
		logger.info(StringUtils.concat(OzConstants.LINE_FEED,
				StringUtils.repeatChar('<', OzConstants.INT_100), "\nstarting ..."));
	}

	@Test
	public void testGetColumns() {
		String[] stringArray = { "00", "Q1", "A2", "Z3", "W4", "S5", "X6", "E7", "D8", "C9" };
		int[] excludedColumns = { 0, 1, 2, 3, 4, 5, 1, 7, 8, 9 };
		String[] expectedResultArray = { "00", "A2", "Z3", "W4", "S5", "E7", "D8", "C9" };
		String[] actualResultArray = ArrayUtils.getColumns(stringArray, excludedColumns, 1);
		assertTrue("1 demension", Arrays.deepEquals(expectedResultArray, actualResultArray));
	}

	@Test
	public void testGetColumns2() {
		String[][] stringArray = { { "00", "Q1", "A2", "Z3", "W4", "S5", "X6", "E7", "D8", "C9" },
				{ "00", "Q1", "A2", "Z3", "W4", "S5", "X6", "E7", "D8", "C9" } };
		int[] excludedColumns = { 0, 1, 2, 3, 4, 5, 1, 7, 8, 9 };
		String[][] expectedResultArray = { { "00", "A2", "Z3", "W4", "S5", "E7", "D8", "C9" },
				{ "00", "A2", "Z3", "W4", "S5", "E7", "D8", "C9" } };
		String[][] actualResultArray = ArrayUtils.getColumns(stringArray, excludedColumns, 1);
		assertTrue("2 demensions", Arrays.deepEquals(expectedResultArray, actualResultArray));
	}

	@Test
	public void testIsIntInArray() {
		int[] intArray = { 7, 9, 1, 99, 17 };
		int int2Look4 = 77;
		assertFalse(ArrayUtils.isIntInArray(intArray, int2Look4));
		assertTrue("Test failed", ArrayUtils.isIntInArray(intArray, 17));
	}

	@Test
	public void testIsObjectInArray() {
		String[] strArray = { "s1", "s2", "s3", "s4" };
		String str2Look4 = "s33";
		assertFalse(ArrayUtils.isObjectInArray(strArray, str2Look4));
		str2Look4 = "s3";
		assertTrue("Test failed", ArrayUtils.isObjectInArray(strArray, str2Look4));
	}

	@Test
	public void testSplit() {
		String[] strArray = "123qweasdzxc;".split(";");
		logger.info("Array length: " + String.valueOf(strArray.length));
		assertTrue(strArray.length == 1);
		String[] strArray1 = "123qweasdzxc;v".split(";");
		logger.info("Array length: " + String.valueOf(strArray1.length));
		assertTrue(strArray1.length == 2);
	}

	@Test
	public void testPrintArray() {
		String[][] stringArray = { { "00", "Q1" }, { "A2", "Z3" }, { "W4", "S5" } };
		ArrayUtils.print2DimArray(stringArray, Level.INFO);
		String actualResult = ArrayUtils.print2DimArray(stringArray, OzConstants.COMMA, Level.INFO)
				.toString();
		String expectedResult = StringUtils.concat("00,Q1", NEW_LINE, "A2,Z3", NEW_LINE, "W4,S5");
		assertEquals(actualResult, expectedResult);
		// actualResultArray));
	}

	@Test
	public void testConcatenateStringArrays() {
		String[][] a1 = { { "1", "2", "3", "AAA" }, { "4", "5", "6", "BBB" } };
		String[][] a2 = { { "11", "22", "33", "XXX" }, { "44", "55", "66", "YYY" } };
		String[][] a3 = ArrayUtils.concatenateStringArrays(a1, a2);
		String[][] expected = { { "1", "2", "3", "AAA" }, { "4", "5", "6", "BBB" },
				{ "11", "22", "33", "XXX" }, { "44", "55", "66", "YYY" } };
		// ArrayUtils.printStringArray(a3, "\t", Level.INFO);
		// ArrayUtils.printArray(ArrayUtils.getSelectedArrayColumn(a1, -1));
		logger.info(StringUtils.concat(OzConstants.LINE_FEED,
				ArrayUtils.print2DimArray(a3, Level.FINEST).toString()));
		Assert.assertArrayEquals(expected, a3);
	}

	@AfterClass
	public static void afterClass() {
		logger.info(StringUtils.concat("done  ...", OzConstants.LINE_FEED,
				StringUtils.repeatChar('>', OzConstants.INT_100), OzConstants.LINE_FEED));
	}
}
