package oz.infra.collection;

import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public class CollectionUtils {
	private static Logger logger = JulUtils.getLogger();

	public static <T> void convertToGenericSet(final Set oSet, final Set<T> gSet) {
		for (Object object : oSet) {
			gSet.add((T) object);
		}
	}

	public static String getAsDelimitedString(final Collection<?> collection) {
		return getAsDelimitedString(collection, OzConstants.COMMA);
	}

	public static String getAsDelimitedString(final Collection<?> collection, final String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (Object object : collection) {
			if (sb.length() > 0) {
				sb.append(delimiter);
			}
			sb.append(object.toString());
		}
		return sb.toString();
	}

	private static StringBuilder printCollection(final boolean privat, final Collection<?> collection, final String title, final Level level,
			final String... delimitersArray) {
		StringBuilder sb = null;
		if (logger.isLoggable(level)) {
			sb = new StringBuilder(SystemUtils.LINE_SEPARATOR);
			sb.append(SystemUtils.getCallerClassAndMethodString(OzConstants.INT_5));
			String delimiter = SystemUtils.LINE_SEPARATOR;
			if (delimitersArray.length == 1) {
				delimiter = delimitersArray[0];
			}
			if (title != null) {
				sb.append(title);
				sb.append(SystemUtils.LINE_SEPARATOR);
			}
			sb.append(getAsDelimitedString(collection, delimiter));
			logger.log(level, sb.toString());
		}
		return sb;
	}

	public static StringBuilder printCollection(final Collection<?> collection, final Level level, final String... delimitersArray) {
		return printCollection(true, collection, null, level, delimitersArray);
	}

	public static StringBuilder printCollection(final Collection<?> collection, final String title, final Level level,
			final String... delimitersArray) {
		return printCollection(true, collection, title, level, delimitersArray);
	}
}
