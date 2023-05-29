package oz.infra.map;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.infra.varargs.VarArgsUtils;

public class MapUtils {
	private static Logger logger = JulUtils.getLogger();

	public static String getKeySetAsString(final Map<?, ?> map, final String prefix, final String delimiter) {
		StringBuilder sb = new StringBuilder();
		Set<?> keySet = map.keySet();
		String[] keysArray = keySet.toArray(new String[keySet.size()]);
		for (int i = 0; i < keySet.size(); i++) {
			if (i > 0) {
				sb.append(delimiter);
			}
			if (prefix != null) {
				sb.append(prefix);
			}
			sb.append(keysArray[i].toString());
		}
		logger.finest(sb.toString());
		return sb.toString();
	}

	public static String getKeysHavingSameValues(final Map<?, ?> map1, final Map<?, ?> map2,
			final String... delimiter) {
		StringBuilder keysHavingSameValuesSB = new StringBuilder();
		Set<?> keySet = map1.keySet();
		String actualDelimiter = OzConstants.COMMA;
		if (delimiter.length == 1) {
			actualDelimiter = delimiter[0];
		}
		for (Object key1 : keySet) {
			if (map1.get(key1).equals(map2.get(key1))) {
				if (keysHavingSameValuesSB.length() > 0) {
					keysHavingSameValuesSB.append(actualDelimiter);
				}
				keysHavingSameValuesSB.append(key1.toString());
			}
		}
		return keysHavingSameValuesSB.toString();
	}

	public static String getValuesAsString(final Map<?, ?> map, final String delimiter) {
		StringBuilder sb = new StringBuilder();
		Set<?> keySet = map.keySet();
		String[] keysArray = keySet.toArray(new String[keySet.size()]);
		for (int i = 0; i < keySet.size(); i++) {
			if (i > 0) {
				sb.append(delimiter);
			}
			sb.append(map.get(keysArray[i].toString()));
		}
		logger.finest(sb.toString());
		return sb.toString();
	}

	public static <K, V> void populateMapFromPropertiesFile(final Properties properties, final Map<K, V> map) {
		if (properties == null) {
			logger.warning("properties is null !");
		}
		if (map == null) {
			logger.warning("map is null !");
		}
		for (final Entry<Object, Object> entry : properties.entrySet()) {
			map.put((K) entry.getKey(), (V) entry.getValue());
		}
	}

	public static <K, V> void populateMapFromPropertiesFile(final String propertiesFilePath, final Map<K, V> map) {
		logger.finest("propertiesFilePath: " + propertiesFilePath);
		Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		populateMapFromPropertiesFile(properties, map);
	}

	public static String printKeys(final Map<?, ?> map, final String title, final Level level) {
		StringBuilder sb = new StringBuilder(SystemUtils.LINE_SEPARATOR);
		if (title != null && title.length() > 0) {
			sb.append(title);
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		Set<?> keySet = map.keySet();
		for (Object key : keySet) {
			sb.append(key.toString());
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		logger.log(level, sb.toString());
		return sb.toString();
	}

	public static String printMap(final Map<?, ?> map, final Level... levels) {
		Level level = VarArgsUtils.getMyArg(Level.FINEST, levels);
		return printMap(map, null, level);
	}

	public static String printMap(final Map<?, ?> map, final String title, final Level level) {
		StringBuilder sb = new StringBuilder(SystemUtils.LINE_SEPARATOR);
		if (title != null && title.length() > 0) {
			sb.append(title);
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		Set<?> keySet = map.keySet();
		for (Object key : keySet) {
			sb.append(key.toString() + OzConstants.EQUAL_SIGN + map.get(key) + SystemUtils.LINE_SEPARATOR);
		}
		logger.log(level, sb.toString());
		return sb.toString();
	}
}
