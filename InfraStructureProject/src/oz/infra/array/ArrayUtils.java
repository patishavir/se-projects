package oz.infra.array;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.varargs.VarArgsUtils;

public final class ArrayUtils {
	private static Logger logger = JulUtils.getLogger();

	public static <T> T[] concatenate1DimArrays(final T[] first, final T[]... rest) {
		int totalLength = first.length;
		for (T[] array : rest) {
			totalLength += array.length;
		}
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (T[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

	public static String[][] concatenateStringArrays(final String[][] array1, final String[][] array2) {
		String[][] resultArray = null;
		if (array1 != null && array2 != null && array1[0].length == array2[0].length) {
			resultArray = new String[array1.length + array2.length][array1[0].length];
			for (int row1 = 0; row1 < array1.length; row1++) {
				resultArray[row1] = array1[row1];
			}
			for (int row2 = 0; row2 < array2.length; row2++) {
				resultArray[row2 + array1.length] = array2[row2];
			}
		}
		return resultArray;
	}

	public static int[] getArrayDimenstions(final Object[][] objectArray) {
		if (objectArray != null && objectArray[0] != null) {
			int rows = objectArray.length;
			int columns = objectArray[0].length;
			logger.info("rows: " + rows + " columns: " + columns);
			int[] dimensions = { rows, columns };
			return dimensions;
		} else {
			logger.info("Array is null !!!");
			return null;
		}
	}

	public static String getAsDelimitedString(final int[] intArray, final String... separators) {
		Integer[] integerArray = new Integer[intArray.length];
		for (int i = 0; i < intArray.length; i++) {
			integerArray[i] = intArray[i];
		}
		return getAsDelimitedString(integerArray, separators);
	}

	public static <T> String getAsDelimitedString(final T[] objectArray, final String... separators) {
		String separator = VarArgsUtils.getMyArg(OzConstants.TAB, separators);
		StringBuilder sb = new StringBuilder();
		if (objectArray != null) {
			for (int rowPtr = 0; rowPtr < objectArray.length; rowPtr++) {
				if (objectArray[rowPtr] == null) {
					sb.append("null");
				} else {
					sb.append(objectArray[rowPtr].toString());
				}
				if (rowPtr < objectArray.length - 1) {
					sb.append(separator);
				}
			}
		}
		return sb.toString();
	}

	public static <T> List<T> getAsList(final T[] array) {
		List<T> list = new ArrayList<T>();
		for (T arrayEntry : array) {
			list.add(arrayEntry);
		}
		return list;
	}

	public static List<String> getAsListBackedByArray(final String[] array) {
		return new ArrayList<String>(Arrays.asList(array));
	}

	private static StringBuilder getAsStringBuilder(final String[][] stringArray) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int rowPtr = 0; rowPtr < stringArray.length; rowPtr++) {
			if (rowPtr > 0) {
				stringBuilder.append(OzConstants.LINE_FEED);
			}
			for (int columnPtr = 0; columnPtr < stringArray[rowPtr].length; columnPtr++) {
				logger.finest("rowPtr: " + String.valueOf(rowPtr) + " columnPtr:" + String.valueOf(columnPtr));
				if (columnPtr > 0) {
					stringBuilder.append(OzConstants.COMMA);
				}
				stringBuilder.append(stringArray[rowPtr][columnPtr]);
			}
		}
		return stringBuilder;
	}

	public static <T> StringBuilder getAsStringBuilder(final T[] genericArray, final String... separators) {
		String separator = VarArgsUtils.getMyArg(OzConstants.COMMA, separators);
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < genericArray.length; i++) {
			if (i > 0) {
				stringBuilder.append(separator);
			}
			stringBuilder.append(genericArray[i].toString());
		}
		return stringBuilder;
	}

	public static String[] getColumns(final String[] stringArray, final int[] excludedColumns, final int excludeValue) {
		String[] resultArray = null;
		ArrayList<String> arrayList = new ArrayList<String>();
		if (stringArray.length == excludedColumns.length) {
			for (int i = 0; i < stringArray.length; i++) {
				if (excludedColumns[i] != excludeValue) {
					arrayList.add(stringArray[i]);
				}
			}
			resultArray = new String[arrayList.size()];
			resultArray = arrayList.toArray(resultArray);
		}
		return resultArray;
	}

	public static String[][] getColumns(final String[][] stringArray, final int[] excludedColumns, final int excludeValue) {
		String delemiter = OzConstants.COMMA;
		String[][] resultArray = null;
		String[] tempArray = new String[stringArray.length];
		if (stringArray[0].length == excludedColumns.length) {
			for (int rowPtr = 0; rowPtr < stringArray.length; rowPtr++) {
				for (int columnPtr = 0; columnPtr < stringArray[0].length; columnPtr++) {
					if (excludedColumns[columnPtr] != excludeValue) {
						if (tempArray[rowPtr] == null) {
							tempArray[rowPtr] = stringArray[rowPtr][columnPtr];
						} else {
							tempArray[rowPtr] = tempArray[rowPtr].concat(delemiter);
							if (stringArray[rowPtr][columnPtr] != null) {
								tempArray[rowPtr] = tempArray[rowPtr].concat(stringArray[rowPtr][columnPtr]);
							}
						}

					}
				}
			}
			int numberOfResultColumns = StringUtils.getNumberOfOccurrences(tempArray[0], delemiter) + 1;
			logger.finest("numberOfResultColumns: " + String.valueOf(numberOfResultColumns));
			resultArray = new String[stringArray.length][numberOfResultColumns];
			for (int i = 0; i < stringArray.length; i++) {
				resultArray[i] = tempArray[i].split(delemiter);
			}
		}
		return resultArray;
	}

	public static int getFullStringIndex(final String[] stringArray2Search, final String string2Look4) {
		for (int i = 0; i < stringArray2Search.length; i++) {
			if (stringArray2Search[i].equals(string2Look4)) {
				return i;
			}
		}
		return -1;
	}

	public static int getMaxColumn(final String[] rowsArray, final char columnDelemiterChar) {
		int maxColumn = Integer.MIN_VALUE;
		for (int i = 0; i < rowsArray.length; i++) {
			int numberOfOccurrences = StringUtils.getNumberOfOccurrences(rowsArray[i], columnDelemiterChar);
			if (maxColumn < numberOfOccurrences) {
				maxColumn = numberOfOccurrences;
			}
		}
		logger.finest("maxColumn: " + String.valueOf(maxColumn));
		return maxColumn;
	}

	public static int getPartailStringIndex(final String[] stringArray2Search, final String partialString2Look4) {
		for (int i = 0; i < stringArray2Search.length; i++) {
			if (stringArray2Search[i].indexOf(partialString2Look4) > -1) {
				return i;
			}
		}
		return -1;
	}

	public static String[] getSelectedArrayColumn(final String[][] inputArray, final int selectedColumn) {
		String[] selectedColumnArray = null;
		if (inputArray != null && selectedColumn >= 0) {
			selectedColumnArray = new String[inputArray.length];
			for (int i = 0; i < inputArray.length; i++) {
				selectedColumnArray[i] = inputArray[i][selectedColumn];
			}
		}
		return selectedColumnArray;
	}

	public static String[][] getSelectedArrayColumns(final String[][] inputArray, final int[] selectedColumns) {
		String[][] selectedColumnsArray = null;
		if (inputArray != null && selectedColumns != null) {
			int numberOfArrayColumns = inputArray[0].length;
			if (selectedColumns.length <= numberOfArrayColumns && selectedColumns.length > 0) {
				selectedColumnsArray = new String[inputArray.length][selectedColumns.length];
				for (int row = 0; row < inputArray.length; row++) {
					for (int column = 0; column < selectedColumns.length; column++) {
						selectedColumnsArray[row][column] = inputArray[row][selectedColumns[column]];
					}
				}
			}
		}
		return selectedColumnsArray;
	}

	public static String getString(final String[] stringArray, final int[] excludeColums, final int excludeValue) {
		String[] tempArray = getColumns(stringArray, excludeColums, excludeValue);
		return getAsStringBuilder(tempArray).toString();
	}

	public static String getString(final String[][] stringArray) {
		return getAsStringBuilder(stringArray).toString();
	}

	public static String getString(final String[][] stringArray, final int[] excludeColums, final int excludeValue) {
		String[][] tempArray = getColumns(stringArray, excludeColums, excludeValue);
		return getAsStringBuilder(tempArray).toString();
	}

	public static <T> String getString(final T[] genericArray, final String... separators) {
		return getAsStringBuilder(genericArray, separators).toString();
	}

	public static String[] getStringArray(final String... strings) {
		return strings;
	}

	public static String[] initStringArray(final int arrayLength, final Object initialValue) {
		String[] array = new String[arrayLength];
		for (int i = 0; i < array.length; i++) {
			array[i] = initialValue.toString();
		}
		return array;
	}

	public static boolean isIntInArray(final int[] intAarray, final int int2Look4) {
		boolean returnCode = false;
		if (intAarray != null && intAarray.length > 0) {
			for (int arrayInt1 : intAarray) {
				if (arrayInt1 == int2Look4) {
					returnCode = true;
					break;
				}
			}
		}
		logger.finest("int2Look4: " + int2Look4 + " returnCode: " + returnCode);
		return returnCode;
	}

	public static boolean isObjectInArray(final Object[] array, final Object object) {
		boolean returnCode = false;
		if (array != null && array.length > 0) {
			for (Object arrayEntry : array) {
				logger.finest(arrayEntry.getClass().getSimpleName());
				if (arrayEntry.getClass().isInstance(object) && arrayEntry.equals(object)) {
					returnCode = true;
					logger.finest("arrayEntry: " + arrayEntry.toString() + "object: " + object);
					break;
				}
			}
		}
		logger.finest("object: " + object + " returnCode: " + returnCode);
		return returnCode;
	}

	public static String[] moveEntry(final String[] array, final String string, final int index) {
		for (int i = 0; i < array.length; i++) {
			if (array[index].equals(string)) {
				logger.warning(string + " is already the " + String.valueOf(index) + "th entry!");
				break;
			}
			if (array[i].equals(string)) {
				array[i] = array[index];
				array[index] = string;
				logger.finest(array[i] + " switched with " + array[index]);
				break;
			}
		}
		return array;
	}

	public static String[] populateArray(final String value, final int length) {
		String[] resultArray = new String[length];
		for (int i = 0; i < length; i++) {
			resultArray[i] = value;
		}
		return resultArray;
	}

	public static <T> StringBuilder print2DimArray(final T[][] objectArray, final Level level) {
		return print2DimArray(objectArray, OzConstants.TAB, level);
	}

	public static <T> StringBuilder print2DimArray(final T[][] objectArray, final String columnDelimiter, final Level level) {
		StringBuilder stringBuilder = new StringBuilder();
		if (objectArray == null) {
			stringBuilder.append(SystemUtils.getCallerClassAndMethodName());
			stringBuilder.append(" Array is null !");
			logger.warning(stringBuilder.toString());
		} else {
			for (int rowIndex = 0; rowIndex < objectArray.length; rowIndex++) {
				if (rowIndex > 0) {
					stringBuilder.append(SystemUtils.LINE_SEPARATOR);
				}
				for (int columnIndex = 0; columnIndex < objectArray[0].length; columnIndex++) {
					if (columnIndex > 0) {
						stringBuilder.append(columnDelimiter);
					}
					logger.finest("rowIndex: " + String.valueOf(rowIndex) + " columnIndex: " + String.valueOf(columnIndex));
					Object arrayEntry = objectArray[rowIndex][columnIndex];
					if (arrayEntry != null) {
						stringBuilder.append(objectArray[rowIndex][columnIndex].toString());
					} else {
						stringBuilder.append("null");
					}
				}

				logger.finest("rowIndex: " + String.valueOf(rowIndex));
			}
			String logMessage = StringUtils.concat(SystemUtils.getCallerClassAndMethodName() + SystemUtils.LINE_SEPARATOR + stringBuilder.toString());
			logger.log(level, logMessage);
		}
		return stringBuilder;
	}

	public static String printArray(final byte[] byteArray, final String delimiter, final String header) {
		StringBuilder sb = new StringBuilder(header);
		for (int i = 0; i < byteArray.length; i++) {
			if (i > 0) {
				sb.append(delimiter);
			}
			sb.append(String.valueOf(byteArray[i]));
		}
		String result = sb.toString();
		logger.info(result);
		return result;
	}

	public static <T> StringBuilder printArray(final T[] objectArray) {
		return printArray(objectArray, null, null, Level.INFO);
	}

	public static <T> StringBuilder printArray(final T[] objectArray, final Level level, final Boolean... addCallerDetailsArg) {
		return printArray(objectArray, null, null, level, addCallerDetailsArg);
	}

	public static <T> StringBuilder printArray(final T[] objectArray, final String separator) {
		return printArray(objectArray, separator, null, Level.INFO);
	}

	public static <T> StringBuilder printArray(final T[] objectArray, final String separator, final String message) {
		return printArray(objectArray, separator, message, Level.INFO);
	}

	public static <T> StringBuilder printArray(final T[] objectArray, final String separator, final String message, final Level level,
			final Boolean... addCallerDetailsArg) {
		Boolean addCallerDetails = VarArgsUtils.getMyArg(true, addCallerDetailsArg);
		StringBuilder sb = new StringBuilder();
		String mySeparator = separator;
		if (mySeparator == null) {
			mySeparator = OzConstants.TAB;
		}
		if (addCallerDetails) {
			sb.append(SystemUtils.getCallerClassAndMethodString(OzConstants.INT_5));
		}
		if (message != null) {
			sb.append(message);
		}
		sb.append(getAsDelimitedString(objectArray, mySeparator));
		if (level != null && logger.isLoggable(level)) {
			logger.info("\n" + sb.toString());
		}
		return sb;
	}

	public static <T> T[] reverse(final T[] inArray) {
		int length = inArray.length;
		T[] outArray = (T[]) java.lang.reflect.Array.newInstance(inArray.getClass().getComponentType(), length);
		for (int i = 0; i < length; i++) {
			outArray[i] = inArray[length - i - 1];
		}
		return outArray;
	}

	public static String[] selectArrayRows(final String[] array, final int[] rowsToSelect) {
		String[] selectedEntriesArray = null;
		if (rowsToSelect.length <= array.length && rowsToSelect.length > 0) {
			selectedEntriesArray = new String[rowsToSelect.length];
			for (int row = 0; row < rowsToSelect.length; row++) {
				selectedEntriesArray[row] = array[rowsToSelect[row]];
			}
		}
		return selectedEntriesArray;
	}

	public static String[] selectArrayRowsByMinimalLength(final String[] array, final int minimalLength) {
		ArrayList<String> selectedRowsArrayList = new ArrayList<String>();
		for (int row = 0; row < array.length; row++) {
			if (array[row].length() >= minimalLength) {
				selectedRowsArrayList.add(array[row]);
			}
		}
		String[] selectedRowsArray = new String[selectedRowsArrayList.size()];
		selectedRowsArrayList.toArray(selectedRowsArray);
		logger.finest("selectedRowsArray length:" + String.valueOf(selectedRowsArray.length));
		return selectedRowsArray;
	}

	public static String[] selectArrayRowsByRegExpression(final String[] array, final String regexpression, final boolean truefalse) {
		ArrayList<String> selectedRowsArrayList = new ArrayList<String>();
		for (int row = 0; row < array.length; row++) {
			if (RegexpUtils.find(array[row], regexpression) == truefalse) {
				selectedRowsArrayList.add(array[row]);
			}
		}
		String[] selectedRowsArray = new String[selectedRowsArrayList.size()];
		selectedRowsArrayList.toArray(selectedRowsArray);
		logger.info("selectedRowsArray length:" + String.valueOf(selectedRowsArray.length));
		return selectedRowsArray;
	}

	public static String[] selectArrayRowsByStartingRow(final String[] array, final int zeroBasedfromRow) {
		String[] selectedRowsArray = null;
		if (zeroBasedfromRow > 0 && zeroBasedfromRow < array.length) {
			selectedRowsArray = new String[array.length - zeroBasedfromRow];
			logger.finest("subArray length:" + selectedRowsArray.length);
			for (int row = 0; row < selectedRowsArray.length; row++) {
				selectedRowsArray[row] = array[zeroBasedfromRow + row];
			}
		}
		return selectedRowsArray;
	}

	public static String[][] selectArrayRowsByStartingRow(final String[][] array, final int zeroBasedfromRow) {
		String[][] selectedRowsArray = null;
		if (zeroBasedfromRow > 0 && zeroBasedfromRow < array.length) {
			selectedRowsArray = new String[array.length - zeroBasedfromRow][array[0].length];
			logger.finest("subArray rows:" + String.valueOf(selectedRowsArray.length));
			logger.finest("subArray columns:" + String.valueOf(selectedRowsArray[0].length));
			for (int row = 0; row < selectedRowsArray.length; row++) {
				for (int column = 0; column < array[0].length; column++) {
					selectedRowsArray[row][column] = array[zeroBasedfromRow + row][column];
				}
			}
		}
		return selectedRowsArray;
	}

	public static String[] selectArrayRowsRange(final String[] array, final int startIndex, final int endIndex) {
		String[] selectedEntriesArray = null;
		if (array != null && startIndex <= endIndex && endIndex <= array.length) {
			selectedEntriesArray = new String[(endIndex - startIndex) + 1];
			for (int row = startIndex; row <= endIndex; row++) {
				selectedEntriesArray[row - startIndex] = array[row];
			}
		}
		return selectedEntriesArray;
	}

	public static String[] shift(final String[] inputArray, final int offset) {
		String[] resltArray = new String[inputArray.length - offset];
		for (int i = 0; i < resltArray.length; i++) {
			resltArray[i] = inputArray[i + offset];
		}
		return resltArray;
	}

	public static <T> T[] shift(final T[] inputArray, final int offset) {
		Object[] resultArray = new Object[inputArray.length - offset];
		for (int i = 0; i < resultArray.length; i++) {
			resultArray[i] = inputArray[i + offset];
		}
		return (T[]) resultArray;
	}

	public static String[][] string2Dim2Array(final String string, final String rowDelimiter, final String columnDelimiter) {
		String[] rowsArray = string.split(rowDelimiter);
		int maxColumn = getMaxColumn(rowsArray, columnDelimiter.charAt(0));
		String[][] resultArray = new String[rowsArray.length][maxColumn + 1];
		for (int rowNumber = 0; rowNumber < rowsArray.length; rowNumber++) {
			String[] rowArray = rowsArray[rowNumber].split(columnDelimiter);
			for (int colNumber = 0; colNumber < rowArray.length; colNumber++) {
				logger.finest("rowNumber: " + String.valueOf(rowNumber) + " colNumber: " + String.valueOf(colNumber) + " maxColumn: "
						+ String.valueOf(maxColumn));
				resultArray[rowNumber][colNumber] = rowArray[colNumber];
			}
		}
		return resultArray;
	}

	public static <T> T[] truncate(final T[] inputArray, final int entries2RemoveCount) {
		if (inputArray.length >= entries2RemoveCount) {
			T[] result = Arrays.copyOf(inputArray, inputArray.length - entries2RemoveCount);
			return result;
		} else {
			return null;
		}
	}

	public static void writeStringArray2CsvFile(final String[][] stringArray, final File csvFile) {
		StringBuilder csvFileSB = new StringBuilder();
		for (int rowPtr = 0; rowPtr < stringArray.length; rowPtr++) {
			for (int colPtr = 0; colPtr < stringArray[0].length; colPtr++) {
				csvFileSB.append(stringArray[rowPtr][colPtr]);
				if (colPtr < stringArray[0].length - 1) {
					csvFileSB.append(OzConstants.COMMA);
				}
			}
			if (rowPtr < stringArray.length - 1) {
				csvFileSB.append(System.getProperty("line.separator"));
			}
		}
		logger.info(csvFileSB.toString());
		FileUtils.writeFile(csvFile, csvFileSB.toString());
	}

	private ArrayUtils() {
		super();
	}
}
