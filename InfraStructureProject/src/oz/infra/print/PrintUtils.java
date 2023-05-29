package oz.infra.print;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.KeyValuePair;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.string.StringUtils;
import oz.infra.string.StringUtils.PaddingDirection;
import oz.infra.system.SystemUtils;
import oz.infra.varargs.VarArgsUtils;

public class PrintUtils {
	public enum PrintOption {
		HEADER_AND_DATA, HEADER_ONLY, DATA_ONLY, MULTIPLE_LINES
	}

	private static final int SEPARATOR_LINE_LENGTH = 140;

	private static final Logger logger = JulUtils.getLogger();

	public static String getSeparatorLine(final String text, final String... str2RepeatArg) {
		return getSeparatorLine(text, 1, 0, str2RepeatArg);
	}

	public static String getSeparatorLine(final String text, final int skipLinesBefore, final int skipLinesAfter, final String... str2RepeatArg) {
		final String str2Repeat = VarArgsUtils.getMyArg(OzConstants.EQUAL_SIGN, str2RepeatArg);
		int repeatCount = ((SEPARATOR_LINE_LENGTH - text.length() - 2) / 2 / str2Repeat.length());
		String sideString = StringUtils.repeatString(str2Repeat, repeatCount);
		String linesBefore = StringUtils.repeatString(SystemUtils.LINE_SEPARATOR, skipLinesBefore);
		String linesAfter = StringUtils.repeatString(SystemUtils.LINE_SEPARATOR, skipLinesAfter);
		String separatorLine = StringUtils.concat(linesBefore, sideString, " ", text, " ", sideString, linesAfter);
		return separatorLine;

	}

	private static String printFields(final Object myObject, final String[][] fieldNames, final String delimiter, final boolean padValues) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fieldNames.length; i++) {
			String value = ReflectionUtils.getFieldValueAsString(myObject, fieldNames[i][0]);
			int headerLength = fieldNames[i][1].length();
			logger.finest("fieldName:" + fieldNames[i][0] + OzConstants.TAB + fieldNames[i][1] + " value: " + value);
			if ((!delimiter.equals(OzConstants.COMMA)) && padValues && (headerLength > value.length())) {
				value = StringUtils.pad(value, ' ', headerLength, PaddingDirection.LEFT);
			}
			sb.append(value);
			if (i < fieldNames.length - 1) {
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}

	private static String printHeader(final String[][] fieldNames, final String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fieldNames.length; i++) {
			sb.append(fieldNames[i][1]);
			if (i < fieldNames.length - 1) {
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}

	private static String printMultipleLines(final Object myObject, final String[][] fieldNames, final String delimiter, final boolean padValues) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fieldNames.length; i++) {
			String key = fieldNames[i][1];
			String value = ReflectionUtils.getFieldValueAsString(myObject, fieldNames[i][0]);
			sb.append(key + OzConstants.COLON + OzConstants.TAB + value + OzConstants.LINE_FEED);
		}
		return sb.toString();
	}

	public static String printObjectFields(final Object myObject, final List<KeyValuePair<String, String>> fieldNamesList, final String delimiter,
			final PrintOption printOption) {
		String[][] fieldNames = new String[fieldNamesList.size()][2];
		int i = 0;
		for (KeyValuePair<String, String> kvp : fieldNamesList) {
			fieldNames[i][0] = kvp.getKey();
			fieldNames[i][1] = kvp.getValue();
			i++;
		}
		return printObjectFields(myObject, fieldNames, delimiter, printOption, true);

	}

	public static String printObjectFields(final Object myObject, final String delimiter, final PrintOption printOption) {
		HashMap<String, Field> fieldsHM = ReflectionUtils.getFieldsHashMap(myObject.getClass());
		Set<String> keySet = fieldsHM.keySet();
		String[] fieldsArray1 = new String[keySet.size()];
		keySet.toArray(fieldsArray1);
		String[][] fieldsArray = new String[fieldsArray1.length][2];
		for (int i = 0; i < fieldsArray1.length; i++) {
			fieldsArray[i][0] = fieldsArray1[i];
			fieldsArray[i][1] = fieldsArray1[i];
		}
		return printObjectFields(myObject, fieldsArray, delimiter, printOption);

	}

	public static String printObjectFields(final Object myObject, final String[] fieldNames, final String separator, final PrintOption printOption) {
		String[][] fieldNames2 = new String[fieldNames.length][2];
		for (int i = 0; i < fieldNames.length; i++) {
			fieldNames2[i][0] = fieldNames[i];
			fieldNames2[i][1] = fieldNames[i];
		}
		return printObjectFields(myObject, fieldNames2, separator, printOption);
	}

	public static String printObjectFields(final Object myObject, final String[][] fieldNames, final String delimiter,
			final PrintOption printOption) {
		return printObjectFields(myObject, fieldNames, delimiter, printOption, true);

	}

	public static String printObjectFields(final Object myObject, final String[][] fieldNames, final String delimiter, final PrintOption printOption,
			final boolean padValues) {
		StringBuilder sb = new StringBuilder();
		switch (printOption) {
		case HEADER_ONLY:
			sb.append(printHeader(fieldNames, delimiter));
			break;
		case HEADER_AND_DATA:
			sb.append(printHeader(fieldNames, delimiter));
			sb.append(SystemUtils.LINE_SEPARATOR);
		case DATA_ONLY:
			sb.append(printFields(myObject, fieldNames, delimiter, padValues));
			break;
		case MULTIPLE_LINES:
			sb.append(printMultipleLines(myObject, fieldNames, delimiter, padValues));
			break;
		}
		logger.finest(sb.toString());
		return sb.toString();
	}
}
