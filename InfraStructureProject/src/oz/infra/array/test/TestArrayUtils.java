package oz.infra.array.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;

public class TestArrayUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testConcatenateStringArrays();
		// testConcatenate1DimArrays();
		// testShift();
		// testGetStringArray();
		// testPrintArray();
		// testShift();
		// testSelectArrayRowsRange();
		// testReverse();
		// testConcatenate2DimArrays();
		testTruncate();
	}

	private static void testConcatenate1DimArrays() {
		String[] a1 = { "1", "2", "3", "AAA" };
		String[] a2 = { "11", "22", "33", "XXX" };
		String[] a3 = ArrayUtils.concatenate1DimArrays(a1, a2);
		ArrayUtils.printArray(a3, Level.INFO);
		String[] a4 = ArrayUtils.concatenate1DimArrays(a1, a2, a3);
		ArrayUtils.printArray(a4, Level.INFO);
		String[] a5 = ArrayUtils.concatenate1DimArrays(a1, a2, a3, a4);
		ArrayUtils.printArray(a5, Level.INFO);
	}

	private static void testConcatenate2DimArrays() {
		String[][] array1 = { { "111", "222", "333", "444" }, { "aaa", "bbb", "ccc", "ddd" }, { "!!!", "@@@", "###", "$$$" } };
		String[][] array2 = { { "q111", "q222", "q333", "q444" }, { "waaa", "wbbb", "wccc", "wddd" }, { "u!!!", "u@@@", "u###", "u$$$" },
				{ "u!!!99", "u@@@99", "u###99", "u$$$99" }, { "u!!!9999", "u@@@9999", "u###9999", "u$$$9999" } };
		String[][] resultArray = ArrayUtils.concatenateStringArrays(array1, array2);
		ArrayUtils.print2DimArray(resultArray, Level.INFO);
	}

	private static void testConcatenateStringArrays() {
		String[][] a1 = { { "1", "2", "3", "AAA" }, { "4", "5", "6", "BBB" } };
		String[][] a2 = { { "11", "22", "33", "XXX" }, { "44", "55", "66", "YYY" } };
		String[][] a3 = ArrayUtils.concatenateStringArrays(a1, a2);
		// ArrayUtils.printStringArray(a3, "\t", Level.INFO);
		ArrayUtils.printArray(ArrayUtils.getSelectedArrayColumn(a1, -1));
	}

	private static void testGetStringArray() {
		ArrayUtils.printArray(ArrayUtils.getStringArray("aaa", "bbb", "ccc", "777888"), OzConstants.COMMA);
		ArrayUtils.printArray(ArrayUtils.getStringArray("aaa", "bbb", "ccc", "777888", null), OzConstants.COMMA);
		ArrayUtils.printArray(ArrayUtils.getStringArray(), OzConstants.COMMA);
		ArrayUtils.printArray(ArrayUtils.getStringArray("aaa"), OzConstants.COMMA);
		ArrayUtils.printArray(ArrayUtils.getStringArray(null), OzConstants.COMMA);
	}

	private static void testPrintArray() {
		String[] objectArray = { "str1", "str2", "str3" };
		ArrayUtils.printArray(objectArray);
		ArrayUtils.printArray(objectArray, Level.INFO);
		ArrayUtils.printArray(objectArray, OzConstants.TAB);
		ArrayUtils.printArray(objectArray, OzConstants.TAB, "array contents message:\t");
		ArrayUtils.printArray(objectArray, OzConstants.TAB, "array contents message:\t", Level.INFO);
	}

	private static void testReverse() {
		String[] in = { "1", "2", "3", "4" };
		String[] out = ArrayUtils.reverse(in);
		ArrayUtils.printArray(in, Level.INFO);
		ArrayUtils.printArray(out, Level.INFO);

	}

	private static void testSelectArrayRowsRange() {
		String[] inputArray = { "str1", "str2", "str3", "str4", "str5", "str6" };
		String[] result = ArrayUtils.selectArrayRowsRange(inputArray, 2, 4);
		ArrayUtils.printArray(result);
		// result = ArrayUtils.shift(inputArray, 2);
		// ArrayUtils.printArray(result);
		// result = ArrayUtils.shift(inputArray, 3);
		// ArrayUtils.printArray(result);
	}

	private static void testShift() {
		String[] inputArray = { "str1", "str2", "str3", "str4", "str5" };
		String[] result = ArrayUtils.shift(inputArray, 1);
		ArrayUtils.printArray(result);
		result = ArrayUtils.shift(inputArray, 2);
		ArrayUtils.printArray(result);
		result = ArrayUtils.shift(inputArray, 3);
		ArrayUtils.printArray(result);
	}

	private static void testTruncate() {
		String[] a1 = { "1", "2", "3", "AAA" };
		testTruncate1(a1);
	}

	private static void testTruncate1(final String[] array) {
		ArrayUtils.printArray(array);
		ArrayUtils.printArray(ArrayUtils.truncate(array, 1));
		ArrayUtils.printArray(ArrayUtils.truncate(array, 2));
		ArrayUtils.printArray(ArrayUtils.truncate(array, 3));
		ArrayUtils.printArray(ArrayUtils.truncate(array, 4));
		ArrayUtils.printArray(ArrayUtils.truncate(array, 5));

	}
}
