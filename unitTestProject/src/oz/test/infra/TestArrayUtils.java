package oz.test.infra;

import oz.infra.array.ArrayUtils;
import oz.infra.regexp.RegexpUtils;

public class TestArrayUtils {
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		selectArrayRowsRange();
		// isObjectInArray();
		// String[] array = { "1", "2", "3", "4", "5", "6" };
		// testSelectArrayRowsByRegExpression(array);
		// String[] array1 = { "b1", "2 ", "3", "4", "a5", "6" };
		// testSelectArrayRowsByRegExpression(array1);
		// String[] array2 = { ":", ".","b1", "4", "a5", "seven", "", "\n"};
		// testSelectArrayRowsByRegExpression(array2);
	}

	private static void testSelectArrayRowsByRegExpression(final String[] array) {
		String[] subArray = ArrayUtils.selectArrayRowsByRegExpression(array,
				RegexpUtils.REGEXP_DIGIT + "|\\.+|:", true);
		ArrayUtils.printArray(subArray);
	}

	private static void testSelectArrayRowsByStartingRow() {
		String[] array = { "1", "2", "3", "4", "5" };
		String[] subArray = ArrayUtils.selectArrayRowsByStartingRow(array, 1);
		ArrayUtils.printArray(subArray);
	}

	private static void isObjectInArray() {
		String[] array = { "1", "2", "3", "345", "4", "5", "555" };
		String string = "345";
		ArrayUtils.isObjectInArray(array, string);
	}

	private static void selectArrayRowsRange() {
		String[] array = { "1", "2", "3", "345", "4", "5", "555" };
		ArrayUtils.printArray(ArrayUtils.selectArrayRowsRange(array, 1, array.length - 1));
		ArrayUtils.printArray(ArrayUtils.selectArrayRowsRange(array, 1, 1));
		ArrayUtils.printArray(ArrayUtils.selectArrayRowsRange(array, 2, 9));
		ArrayUtils.printArray(ArrayUtils.selectArrayRowsRange(null, 2, 9));
		ArrayUtils.printArray(ArrayUtils.selectArrayRowsRange(array, 1, 5));
	}
}
