package oz.test.reflection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;

public class FieldsReflectionTest {
	private static String s1;
	private String s2;
	private String s3;
	private String s4;
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String propertiesFilePath = args[0];

		logger.info("starting " + Class.class.getName());
		FieldsReflectionTest reflectionTest = new FieldsReflectionTest();
		reflectionTest.setFieldsFromPropertiesFile(propertiesFilePath,
				FieldsReflectionTest.class);
		// ReflectionUtils.setFieldsFromPropertiesFile(propertiesFilePath,
		// ReflectionTest.class);
	}

	private void setFieldsFromPropertiesFile(final String propertiesFilePath,
			final Class<?> myClass) {
		// ReflectionUtils.setFieldsFromPropertiesFile(propertiesFilePath,
		// this);
		logger.info("class: " + myClass.getName());
		Properties myProperties = PropertiesUtils
				.loadPropertiesFile(propertiesFilePath);
		HashMap<String, Field> fieldHashMap = new HashMap<String, Field>();
		Field[] myFields = myClass.getDeclaredFields();
		for (Field field1 : myFields) {
			logger.info("field1: " + field1.getName());
			fieldHashMap.put(field1.getName(), field1);
		}
		Set<?> keySet = myProperties.keySet();
		try {
			for (Object keyObject : keySet) {
				String key = (String) keyObject;
				logger.info("key: " + key);
				Field myField = fieldHashMap.get(key);
				if (myField != null) {
					logger.info("field name: " + myField.getName());
					myField.set(this, myProperties.getProperty(key));
				} else {
					logger.info("no field named " + key + " **************");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info("s1: " + s1);
		logger.info("s2: " + s2);
		logger.info("s3: " + s3);
		logger.info("s4: " + s4);
	}
}
