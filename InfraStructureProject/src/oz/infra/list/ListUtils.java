package oz.infra.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.KeyValuePair;
import oz.infra.system.SystemUtils;
import oz.infra.varargs.VarArgsUtils;

public class ListUtils {
	private static Logger logger = JulUtils.getLogger();

	public static <T> List<T> getItemsAsList(final T... items) {
		return Arrays.asList(items);
	}

	public static String getAsDelimitedString(final List<?> list, final Level level) {
		return getAsDelimitedString(list, OzConstants.EMPTY_STRING, level);
	}

	public static String getAsDelimitedString(final List<?> list, final String delimiter, final Level... level) {
		Level myLevel = VarArgsUtils.getMyArg(Level.INFO, level);
		return getAsTitledDelimitedString(list, OzConstants.EMPTY_STRING, myLevel, delimiter);
	}

	public static String getAsTitledDelimitedString(final List<?> list, final String title, final Level level,
			final String... delimiters) {
		logger.finest("caller: " + SystemUtils.getCallerMethodName() + SystemUtils.LINE_SEPARATOR);
		String listString = title.concat(OzConstants.LINE_FEED.concat(getAsDelimitedString(list, delimiters)));
		if (logger.isLoggable(level)) {
			logger.log(level, listString);
		}
		return listString;
	}

	public static String getAsDelimitedString(final List<?> list, final String... delimiters) {
		StringBuilder sb = new StringBuilder();
		String delimiter = VarArgsUtils.getMyArg(SystemUtils.LINE_SEPARATOR, delimiters);
		for (int i = 0; list != null && i < list.size(); i++) {
			sb.append(list.get(i).toString());
			if (i < list.size() - 1) {
				sb.append(delimiter);
			}
		}
		String listString = sb.toString();
		return listString;
	}

	public static <T> String printListOfLists(final List<List<T>> list, final String title, final Level level,
			final String... delimiters) {
		String listString = null;
		String delimiter = VarArgsUtils.getMyArg(SystemUtils.LINE_SEPARATOR, delimiters);
		logger.finest("caller: " + SystemUtils.getCallerMethodName());
		StringBuilder stringBuilder = new StringBuilder(title);
		stringBuilder.append(SystemUtils.LINE_SEPARATOR);
		for (int i = 0; i < list.size(); i++) {
			List<T> innerList = list.get(i);
			for (int j = 0; j < innerList.size(); j++) {
				stringBuilder.append(innerList.get(j).toString());
				if (j < innerList.size() - 1) {
					stringBuilder.append(delimiter);
				}
			}
			stringBuilder.append(SystemUtils.LINE_SEPARATOR);
		}
		listString = stringBuilder.toString();
		logger.log(level, listString);
		return listString;
	}

	public static List<KeyValuePair<String, String>> readFileAsKeyValuePairList(final String filePath) {
		return readFileAsKeyValuePairList(filePath, Level.FINEST);
	}

	public static List<KeyValuePair<String, String>> readFileAsKeyValuePairList(final String filePath,
			final Level level) {
		String[] linesArray1 = FileUtils.readTextFile2Array(filePath);
		List<KeyValuePair<String, String>> keyValuePairsList = new ArrayList<KeyValuePair<String, String>>();
		for (int i = 0; i < linesArray1.length; i++) {
			String[] linesArray2 = linesArray1[i].split(OzConstants.EQUAL_SIGN);
			if (linesArray1.length > 0 && (!linesArray2[0].startsWith("#")) && linesArray2.length == 2) {
				keyValuePairsList.add(new KeyValuePair<String, String>(linesArray2[0], linesArray2[1]));
			}
		}
		ListUtils.getAsDelimitedString(keyValuePairsList, level);
		return keyValuePairsList;
	}

	public static String[] stringListToStringArray(final List<String> objList) {
		return objList.toArray(new String[objList.size()]);
	}
}
