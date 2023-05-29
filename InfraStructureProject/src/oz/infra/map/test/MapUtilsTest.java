package oz.infra.map.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.KeyValuePair;
import oz.infra.map.MapUtils;

public class MapUtilsTest {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testKeyVauePair();
		testPopulateMapFromPropertiesFile();
	}

	private static void testGetKeysHavingSameValues() {
		Map<String, String> map1 = new TreeMap<String, String>();
		map1.put("k1", "v1");
		map1.put("k2", "v2");
		map1.put("k3", "v3");
		map1.put("k4", "v4");
		map1.put("k5", "v5");
		map1.put("k6", "v6");

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("k1", "v1");
		map2.put("k2", "v2_");
		map2.put("k3", "v3");
		map2.put("k4", "v4");
		map1.put("k5", "v5_");

		// logger.info(MapUtils.getKeySetAsString(map1, "prefix",
		// OzConstants.UNDERSCORE));
		// logger.info(MapUtils.getValuesString(map1, OzConstants.UNDERSCORE));
		logger.info(MapUtils.getKeysHavingSameValues(map1, map2));
		logger.info(MapUtils.getKeysHavingSameValues(map1, map2,
				OzConstants.UNDERSCORE));
	}

	private static void testKeyVauePair() {
		KeyValuePair<String, String> kvp = new KeyValuePair<String, String>(
				"key1", "value1");
		String str1 = kvp.getKey();
		String str2 = kvp.getValue();
		String str3 = kvp.setValue("newValue");
		String str4 = kvp.getValue();
		logger.info(str1 + " " + str2 + " " + str3 + " " + str4);
	}

	private static void testPopulateMapFromPropertiesFile() {
		Properties properties = new Properties();
		properties.put("key1", "value1");
		properties.put("key2", "value2");
		properties.put("key3", "value3");
		Map<String, String> map = new HashMap<String, String>();
		MapUtils.populateMapFromPropertiesFile(properties, map);
		MapUtils.printMap(map, Level.INFO);
	}
}
