package oz.test.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.string.StringUtils;

public class MethodsReflectionTest {
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
		MethodsReflectionTest methodReflectionTest = new MethodsReflectionTest();
		// methodReflectionTest.setFieldsFromPropertiesFile(propertiesFilePath,
		// methodReflectionTest);
		ReflectionUtils.setStaticFieldsFromPropertiesFilePath(propertiesFilePath, methodReflectionTest);
		logger.info("s1: " + methodReflectionTest.s1);
		logger.info("s2: " + methodReflectionTest.s2);
		logger.info("s3: " + methodReflectionTest.s3);
		logger.info("s4: " + methodReflectionTest.s4);
		// ReflectionUtils.setFieldsFromPropertiesFile(propertiesFilePath,
		// ReflectionTest.class);
	}

	private void setFieldsFromPropertiesFile(final String propertiesFilePath, final Object classObject) {
		// ReflectionUtils.setFieldsFromPropertiesFile(propertiesFilePath,
		// this);
		Class<?> myClass = classObject.getClass();
		Properties myProperties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		HashMap<String, Field> fieldHashMap = new HashMap<String, Field>();
		Field[] myFields = myClass.getDeclaredFields();
		for (Field field1 : myFields) {
			logger.finest("field1: " + field1.getName());
			fieldHashMap.put(field1.getName(), field1);
		}
		Set<?> keySet = myProperties.keySet();
		try {
			for (Object keyObject : keySet) {
				String key = (String) keyObject;
				logger.info("key: " + key);
				Field myField = fieldHashMap.get(key);
				if (myField != null) {
					logger.info("key: " + key + "  field name: " + myField.getName());
					Class<?>[] parametersTypes = new Class[1];
					parametersTypes[0] = java.lang.String.class;
					String methodName = "set" + StringUtils.first2Upper(key);
					Method method = myClass.getMethod(methodName, parametersTypes);
					Object[] arglist = new Object[1];
					arglist[0] = myProperties.getProperty(key);
					method.invoke(classObject, arglist);
					logger.info("class: " + myClass.getName() + "  key: " + key + "  field name: " + myField.getName()
							+ "  method.getName: " + method.getName() + " set to:" + arglist[0].toString());
				} else {
					logger.warning("no field named " + key + " **************");
				}
			}
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public static final String getS1() {
		return s1;
	}

	public static final void setS1(String s1) {
		MethodsReflectionTest.s1 = s1;
	}

	public final String getS2() {
		return s2;
	}

	public final void setS2(String s2) {
		this.s2 = s2;
	}

	public final String getS3() {
		return s3;
	}

	public final void setS3(String s3) {
		this.s3 = s3;
	}

	public final String getS4() {
		return s4;
	}

	public final void setS4(String s4) {
		this.s4 = s4;
	}
}
