package oz.infra.string;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.system.env.EnvironmentUtils;
import oz.infra.varargs.VarArgsUtils;

public class StringUtils {
	public enum PaddingDirection {
		LEFT, RIGHT
	};

	private static final Pattern JUST_DIGITS_PATTERN = Pattern.compile(RegexpUtils.REGEXP_DIGITS);
	private static Logger logger = JulUtils.getLogger();

	public static String byteToHex(final byte b) {
		// Returns hex String representation of byte b
		final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		char[] array = { HEX_DIGITS[(b >> 4) & 0x0f], HEX_DIGITS[b & 0x0f] };
		return new String(array);
	}

	public static String capitalize(final String string) {
		if (string.length() == 0) {
			return string;
		}
		return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
	}

	public static String charToHex(final char c) {
		// Returns hex String representation of char c
		byte hi = (byte) (c >>> 8);
		byte lo = (byte) (c & 0xff);
		return byteToHex(hi) + byteToHex(lo);
	}

	public static String concat(final String... strings) {
		if (strings == null) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			for (String string1 : strings) {
				sb.append(string1);
			}
			return sb.toString();
		}
	}

	public static boolean containsHebrewCharacters(final String inputString) {
		boolean result = false;
		for (int i = 0; i < OzConstants.HEBREW_CHARS.length(); i++) {
			char currentChar = OzConstants.HEBREW_CHARS.charAt(i);
			if (inputString.indexOf(currentChar) > OzConstants.STRING_NOT_FOUND) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static String convertByteArrayToHexStr(final byte[] bytesArray) {
		StringBuffer stringBuffer = new StringBuffer(bytesArray.length * 2);
		int i;
		for (i = 0; i < bytesArray.length; i++) {
			if (((int) bytesArray[i] & 0xff) < 0x10) {
				stringBuffer.append("0");
			}
			stringBuffer.append(Long.toString((int) bytesArray[i] & 0xff, 16));
		}
		return stringBuffer.toString();
	}

	public static byte[] convertHexStringToByteArray(final String string) {
		final String hexDigitsString = "0123456789abcdefg";
		byte[] bytesArray = new byte[string.length() / 2];
		for (int i = 0; i < bytesArray.length; i++) {
			int i2 = i * 2;
			int high = hexDigitsString.indexOf(string.substring(i2, i2 + 1));
			int low = hexDigitsString.indexOf(string.substring(i2 + 1, i2 + 2));
			bytesArray[i] = (byte) ((high << OzConstants.INT_4) + low);
		}
		return bytesArray;
	}

	public static String encodeString (final  String inputStr, final Charset  charset) {
	//	StandardCharsets.UTF_8
	//	StandardCharsets.US_ASCII
	//d	StandardCharsets.ISO_8859_1
		byte[] inputBytes = inputStr.getBytes();
		String outputStr = new String(inputBytes, charset);
		return outputStr;
	}

	public static String first2Upper(final String string) {
		if (string == null || string.length() == 0) {
			return string;
		} else {
			return string.substring(0, 1).toUpperCase() + string.substring(1);
		}
	}

	public static String getDelimitedSubString(final String string, final String delimiter) {
		String delimitedSubstring = null;
		int index1 = string.indexOf(delimiter);
		if (index1 > -1) {
			int index2 = string.indexOf(delimiter, (index1 + 1));
			if (index2 > index1) {
				delimitedSubstring = string.substring(index1 + 1, index2);
			}
		}
		logger.finest("input String: " + string + "  delimited Substring: " + delimitedSubstring);
		return delimitedSubstring;
	}

	public static int getEntryContainedInString(final String[] stringArray, final String stringParameter) {
		int index = OzConstants.STRING_NOT_FOUND;
		if (stringArray != null) {
			for (int i = 0; i < stringArray.length; i++) {
				if (stringParameter.indexOf(stringArray[i]) >= 0) {
					index = i;
					break;
				}
			}
		}
		logger.finest("String: " + stringParameter + " index: ".concat(String.valueOf(index)));
		return index;
	}

	public static int getEntryStartingWithString(final String[] stringArray, final String stringParameter) {
		int index = OzConstants.STRING_NOT_FOUND;
		if (stringArray != null) {
			for (int i = 0; i < stringArray.length; i++) {
				if (stringArray[i].startsWith(stringParameter)) {
					index = i;
					break;
				}
			}
		}
		logger.finest("String: " + stringParameter + " index: ".concat(String.valueOf(index)));
		return index;
	}

	public static int getEntryStringStartsWith(final String[] stringArray, final String stringParameter) {
		int index = OzConstants.STRING_NOT_FOUND;
		if (stringArray != null) {
			for (int i = 0; i < stringArray.length; i++) {
				if (stringParameter.startsWith(stringArray[i])) {
					index = i;
					break;
				}
			}
		}
		logger.finest("String: " + stringParameter + " index: ".concat(String.valueOf(index)));
		return index;
	}

	public static String getNextString(final String text, final int startIndex, final String delimiter) {
		String nextLine = null;
		if (text == null) {
			logger.warning("input text is null");
		} else if (startIndex > text.length()) {
			logger.warning("start index is greter than String length");
		} else {
			int delimiterIndex = text.indexOf(text, startIndex);
			if (delimiterIndex > startIndex) {
				nextLine = text.substring(startIndex, delimiterIndex);
			} else {
				nextLine = text.substring(startIndex);
			}
		}
		return nextLine;
	}

	/*
	 * getNumberOfOccurrences
	 */
	public static final int getNumberOfOccurrences(final String myString, final char myChar) {
		int occurrences = 0;
		for (int i = 0; i < myString.length(); i++) {
			if (myString.charAt(i) == myChar) {
				occurrences++;
			}
		}
		return occurrences;
	}

	public static final int getNumberOfOccurrences(final String myString, final String subString2Count) {
		int occurrences = 0;
		for (int i = 0; i < myString.length(); i++) {
			if (myString.startsWith(subString2Count, i)) {
				occurrences++;
			}
		}
		return occurrences;
	}

	public static int[] getSubStringIndexes(final String masterString, final String string2Index) {
		int[] indexesArray = new int[getNumberOfOccurrences(masterString, string2Index)];
		int j = 0;
		for (int i = 0; i < masterString.length(); i++) {
			if (masterString.startsWith(string2Index, i)) {
				indexesArray[j++] = i;
			}
		}
		return indexesArray;
	}

	public static String getSubStringMatchingPattern(final String string, final String pattern, final int index) {
		StringBuilder sb = new StringBuilder();
		for (int i = index; i < string.length(); i++) {
			if (string.substring(i, i + 1).matches(pattern)) {
				sb.append(string.charAt(i));
			} else {
				break;
			}
		}
		return sb.toString();
	}

	public static String getUniqeFromCsv(final String inString) {
		String[] inArray = inString.split(OzConstants.COMMA);
		Collection<String> noDups = new HashSet<String>(inArray.length);
		Collections.addAll(noDups, inArray);
		String outString = "";
		for (String str1 : noDups) {
			outString = outString.concat(str1).concat(OzConstants.COMMA);
		}

		logger.info(StringUtils.concat("\ninString: ", inString, "\noutString: ", outString, "\ninSize: ",
				String.valueOf(inArray.length), "  outStize: ", String.valueOf(noDups.size())));
		return outString;
	}

	public static final boolean isFloatingPointNumber(final String string2Check) {
		return RegexpUtils.matches(string2Check, RegexpUtils.REGEXP_FLOATING_POINT_NUMBER);
	}

	//
	public static final boolean isJustDigits(final String string2Check) {
		return RegexpUtils.matches(string2Check, JUST_DIGITS_PATTERN);
	}

	public static boolean isStringWithinRage(final String string2Compare, final String lowRange, final String highRange,
			final WithinRangeEnum... withinRangeEnums) {
		WithinRangeEnum withinRangeEnum = VarArgsUtils.getMyArg(WithinRangeEnum.IncludeLowIncludeHigh,
				withinRangeEnums);
		boolean equalLength = lowRange.length() == highRange.length();
		boolean result = false;
		if (!equalLength) {
			logger.warning(lowRange + " , " + highRange + " have different lengths ! ");
		} else if (string2Compare.length() < lowRange.length()) {
			logger.warning("string to compare length shorter than range string length. string to compare: "
					+ string2Compare + "   low range: " + lowRange + "   high range: " + highRange);
		} else {
			String string2Compare1 = string2Compare.substring(0, lowRange.length());
			int lowResult = string2Compare1.compareToIgnoreCase(lowRange);
			int highResult = string2Compare1.compareToIgnoreCase(highRange);
			switch (withinRangeEnum) {
			case IncludeLowIncludeHigh:
				result = lowResult >= 0 && highResult <= 0;
				break;
			case IncludeLowExcludeHigh:
				result = lowResult >= 0 && highResult < 0;
				break;
			case ExcludeLowIncludeHigh:
				result = lowResult > 0 && highResult <= 0;
				break;
			case ExcludeLowExcludeHigh:
				result = lowResult > 0 && highResult < 0;
				break;
			}

		}
		return result;
	}

	public static boolean isStringWithinRage(final String string2Compare, final String range,
			final WithinRangeEnum... withinRangeEnums) {
		String[] rangeArray = range.split(OzConstants.HYPHEN);
		return isStringWithinRage(string2Compare, rangeArray[0], rangeArray[1], withinRangeEnums);
	}

	public static final String join(final String[] stringArray, final int fromIndex, final int toIndex,
			final String delimiter) {
		if (stringArray == null) {
			return null;
		}
		if (stringArray.length == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer(stringArray[fromIndex]);
		for (int i = fromIndex + 1; i <= toIndex; i++) {
			sb.append(delimiter);
			sb.append(stringArray[i]);
		}
		return sb.toString();
	}

	public static final String join(final String[] stringArray, final int fromIndex, final String delimiter) {
		if (stringArray == null) {
			return null;
		}
		return join(stringArray, fromIndex, stringArray.length - 1, delimiter);
	}

	public static final String join(final String[] stringArray, final String delimiter) {
		if (stringArray.length == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer(stringArray[0]);
		int stringArrayLength = stringArray.length;
		for (int i = 1; i < stringArrayLength; i++) {
			sb.append(delimiter);
			sb.append(stringArray[i]);
		}
		return sb.toString();
	}

	public static boolean looksLikeXmlString(final String inputString) {
		String trimmedInput = inputString.trim();
		boolean startsWith = trimmedInput.startsWith(OzConstants.LESS_THAN);
		boolean endsWith = trimmedInput.endsWith(OzConstants.GREATER_THAN);
		return (startsWith && endsWith);
	}

	public static String overlay(final String string, final String toString, final int index) {
		char[] stringCharArray = string.toCharArray();
		char[] toStringCharArray = toString.toCharArray();
		for (int i = 0; i < stringCharArray.length; i++) {
			toStringCharArray[index + i] = stringCharArray[i];
		}
		String result = new String(toStringCharArray);
		logger.finest("result: " + result);
		return result;
	}

	public static final String pad(final String string2Pad, final char padChar, final int totaLength,
			final PaddingDirection paddingDirection) {
		if (totaLength > string2Pad.length()) {
			char[] padCharArray = new char[totaLength];
			for (int i = 0; i < totaLength; i++) {
				padCharArray[i] = padChar;
			}
			String padString = new String(padCharArray, 0, totaLength - string2Pad.length());
			if (paddingDirection == PaddingDirection.RIGHT) {
				return string2Pad + padString;
			} else {
				return padString + string2Pad;
			}
		} else {
			logger.warning(
					"String length is greater than or equal to padded length !.\nPadding has not been performed!");
			return string2Pad;
		}
	}

	public static void printStringArray(final String[] stringArray) {
		for (int i = 0; i < stringArray.length; i++) {
			System.out.println(stringArray[i]);
		}
	}

	public static void printStringArray(final String[] stringArray, final String header) {
		System.out.println(header);
		printStringArray(stringArray);
	}

	public static final String removeCharacterFromString(final String string2Process, final char char2Remove) {
		char[] inCharArray = string2Process.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < inCharArray.length; i++) {
			if (inCharArray[i] != char2Remove) {
				sb.append(inCharArray[i]);
			}
		}
		return sb.toString();
	}

	public static final String removeCharacterFromString(final String string2Process, final String chars2RemoveString) {
		char[] inCharArray = string2Process.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < inCharArray.length; i++) {
			if (chars2RemoveString.indexOf(Character.toString(inCharArray[i])) < 0) {
				sb.append(inCharArray[i]);
			}
		}
		return sb.toString();
	}

	public static String removeLinesContainingSubString(final String myString, final String subString) {
		// FileUtils.writeFile("c:\\temp\\i.txt", myString);
		String[] stringArray = myString.split(OzConstants.LINE_FEED);
		logger.finest("Number of lines: " + String.valueOf(String.valueOf(stringArray.length)));
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < stringArray.length; i++) {
			if (stringArray[i].indexOf(subString) < 0) {
				stringBuilder.append(stringArray[i] + OzConstants.LINE_FEED);
			}
		}
		String resultString = stringBuilder.toString();
		// FileUtils.writeFile("c:\\temp\\o.txt", resultString);
		return resultString;
	}

	public static String removeTrailingSubString(final String string, final String subString) {
		if (string.endsWith(subString)) {
			int myLength = string.length() - subString.length();
			return string.substring(0, myLength);
		} else {
			return string;
		}
	}

	//
	public static final String repeatChar(final char char2Repeat, final int repeatCount) {
		char[] charArray = new char[repeatCount];
		for (int i = 0; i < repeatCount; i++) {
			charArray[i] = char2Repeat;
		}
		String outputString = new String(charArray, 0, repeatCount);
		logger.finest(outputString);
		return outputString;
	}

	public static String repeatString(final String string2Repeat, final int repeatCount) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= repeatCount; i++) {
			sb.append(string2Repeat);
		}
		return sb.toString();
	}

	public static String setCharAt (final String inputString, final int index, final char newChar  ) {
		StringBuilder sb = new StringBuilder (inputString);
		sb.setCharAt(index, newChar);
		return sb.toString();
	}

	public static String[] splitOnWhiteSpaces(final String string) {
		String[] splitStringArray = null;
		if (string != null) {
			splitStringArray = string.split(RegexpUtils.REGEXP_WHITE_SPACE);
		}
		return splitStringArray;
	}

	public static String stringArray2String(final String[] stringArray, final String entryDelimiter) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; stringArray != null && i < stringArray.length; i++) {
			sb.append(stringArray[i]);
			if (i < stringArray.length - 1) {
				sb.append(entryDelimiter);
			}
		}
		String resultString = sb.toString();
		logger.finest(resultString);
		return resultString;
	}

	/**
	 * Replacement utility - substitutes <b>all</b> occurrences of
	 * 'sourcePattern' with 'destPattern' in the string 'name'
	 * 
	 * @param name
	 *            the string that the substitution is going to take place on
	 * @param src
	 *            the string that is going to be replaced
	 * @param dest
	 *            the string that is going to be substituted in
	 * @return String with the substituted strings
	 */
	public static final String substitute(final String string2Change, final String sourcePattern,
			final String destPattern) {
		if (string2Change == null || sourcePattern == null || string2Change.length() == 0) {
			return string2Change;
		}
		String myDestPattern = destPattern;
		if (myDestPattern == null) {
			myDestPattern = "";
		}
		int index = string2Change.indexOf(sourcePattern);
		if (index == -1) {
			return string2Change;
		}
		StringBuffer buf = new StringBuffer();
		int lastIndex = 0;
		while (index != -1) {
			buf.append(string2Change.substring(lastIndex, index));
			buf.append(myDestPattern);
			lastIndex = index + sourcePattern.length();
			index = string2Change.indexOf(sourcePattern, lastIndex);
		}
		buf.append(string2Change.substring(lastIndex));
		return buf.toString();
	}

	public static String substituteEnvironmentVariables(final String inputString) {
		Map<String, String> environmentVariables = EnvironmentUtils.getEnvironmentVariablesMap();
		return StringUtils.substituteVariables(inputString, environmentVariables);
	}

	public static String[] substituteEnvironmentVariables(final String[] inputStringsArray) {
		if (inputStringsArray != null) {
			for (int i = 0; i < inputStringsArray.length; i++) {
				inputStringsArray[i] = substituteEnvironmentVariables(inputStringsArray[i]);
			}
		}
		return inputStringsArray;
	}

	public static String substituteVariables(final String inputString, final Map<String, String> map) {
		String outputString = inputString;
		if (outputString != null) {
			String outputString4Compare = null;
			do {
				outputString4Compare = outputString;
				Set<Map.Entry<String, String>> mapSet = map.entrySet();
				Iterator<Map.Entry<String, String>> mapSetIterator = mapSet.iterator();
				while (mapSetIterator.hasNext()) {
					Map.Entry<String, String> keyValuePair = mapSetIterator.next();
					String myKey = OzConstants.PERCENT + keyValuePair.getKey() + OzConstants.PERCENT;
					String myValue = keyValuePair.getValue();
					logger.finest("outputString: " + outputString + " myKey: " + myKey + " myValue: " + myValue);
					// outputString = outputString.replaceAll(myKey,
					// myValue);
					outputString = substitute(outputString, myKey, myValue);
				}
			} while (!outputString.equals(outputString4Compare));
			if (!inputString.equals(outputString)) {
				logger.finest("\ninputString: \n" + inputString);
				logger.finest("\noutputString:\n" + outputString);
			}
		}
		return outputString;
	}

	public static String[] substituteVariables(final String[] inputStringsArray, final Map<String, String> map) {
		if (inputStringsArray != null && inputStringsArray.length > 0) {
			for (int i = 0; i < inputStringsArray.length; i++) {
				inputStringsArray[i] = substituteVariables(inputStringsArray[i], map);
			}
		}
		return inputStringsArray;
	}

	public static String[] trimArray(final String[] inArray) {
		String[] outArray = null;
		if (inArray != null) {
			outArray = new String[inArray.length];
			for (int i = 0; i < inArray.length; i++) {
				outArray[i] = inArray[i].trim();
			}
		}
		return outArray;
	}
	public static String unsplit(final String[] array, final String str) {
		String result = null;
		if (array != null && str != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; (array != null) && i < array.length; i++) {
				sb.append(array[i]);
				if (i < (array.length - 1)) {
					sb.append(str);
				}
			}
			result = sb.toString();
		}
		return result;
	}
	public static String winEol2UnixEol(final String inputString) {
		if (inputString != null) {
			return inputString.replaceAll(OzConstants.WINDOWS_LINE_SEPARATOR, OzConstants.UNIX_LINE_SEPARATOR);
		} else {
			return null;
		}
	}
}
