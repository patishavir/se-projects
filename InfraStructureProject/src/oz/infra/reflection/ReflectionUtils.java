package oz.infra.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.MapUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;
import oz.infra.swing.color.ColorUtils;
import oz.infra.system.SystemUtils;
import oz.infra.varargs.VarArgsUtils;

public class ReflectionUtils {
	private static Logger logger = JulUtils.getLogger();

	public static HashMap<String, Field> getFieldsHashMap(final Class<?> myClass) {
		HashMap<String, Field> fieldsHashMap = new HashMap<String, Field>();
		Field[] myFields = myClass.getDeclaredFields();
		for (Field field1 : myFields) {
			fieldsHashMap.put(field1.getName(), field1);
			logger.finest("field: " + fieldsHashMap.get(field1.getName()).getName());
		}
		return fieldsHashMap;
	}

	public static String getFieldValueAsString(final Object myObject, final String fieldName) {
		String returnValue = null;
		try {
			Class<?> myClass = null;
			if (myObject instanceof Class<?>) {
				myClass = (Class<?>) myObject;
			} else {
				myClass = myObject.getClass();
			}
			String methodName = "get" + StringUtils.first2Upper(fieldName);
			logger.finest("methodName: " + methodName);
			Class<?>[] parametersTypes = new Class[0];
			Method method = myClass.getMethod(methodName, parametersTypes);
			Class<?> returnType = method.getReturnType();
			TypeEnum typeEnum = TypeEnum.getTypeEnum(returnType.getName());
			switch (typeEnum) {
			case STRING:
				returnValue = (String) method.invoke(myObject);
				break;
			case INT:
				Integer integerValue = (Integer) method.invoke(myObject);
				returnValue = integerValue.toString();
				break;
			case FLOAT:
				Float floatValue = (Float) method.invoke(myObject);
				returnValue = floatValue.toString();
				break;
			case LONG:
				Long longValue = (Long) method.invoke(myObject);
				returnValue = longValue.toString();
				break;
			case BOOLEAN:
				Boolean booleanValue = (Boolean) method.invoke(myObject);
				returnValue = booleanValue.toString();
				break;
			default:
				logger.severe(fieldName + " is an invalid parameter.Processing has been terminated!");
				System.exit(-1);
				break;
			}
			logger.finest(returnValue);
			return returnValue;
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			logger.warning(ExceptionUtils.getStackTrace(ex));
		}
		return null;

	}

	public static Object invokeMethod(final Class<?> cls, final String methodName, final Object[] parameters) {
		Object object = null;
		try {
			object = cls.newInstance();
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}

		return invokeMethod(object, methodName, parameters);
	}

	public static Object invokeMethod(final Object myObject, final String methodName, final Object[] parameters) {
		Object result = null;
		try {
			Class<?>[] parametersClassArray = {};
			if (parameters.length > 0) {
				parametersClassArray = new Class[parameters.length];
				for (int i = 0; i < parameters.length; i++) {
					parametersClassArray[i] = parameters[i].getClass();
				}
			}
			Class<?> myClass = null;
			if (myObject instanceof Class<?>) {
				myClass = (Class<?>) myObject;
			} else {
				myClass = myObject.getClass();
			}
			Method method = myClass.getDeclaredMethod(methodName, parametersClassArray);
			logger.info(StringUtils.concat("invoke method ", methodName, "  parameter: ", parameters.toString()));
			result = method.invoke(myObject, parameters);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return result;
	}

	public static Object invokeMethod(final String classString, final String methodName, final Object[] parameters) {
		Object result = null;
		try {
			Class<?> cls = Class.forName(classString);
			result = invokeMethod(cls, methodName, parameters);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return result;
	}

	public static Object invokeMethod(final String classString, final String methodName, final Object parameter) {
		Object[] parametersArray = { parameter };
		return invokeMethod(classString, methodName, parametersArray);
	}

	public static void setFieldsFromProperties(final Properties myProperties, final Object myObject, final Boolean... verboseParam) {
		Class<?> myClass = null;
		if (myObject instanceof Class<?>) {
			myClass = (Class<?>) myObject;
		} else {
			myClass = myObject.getClass();
		}
		setFieldsFromProperties(myProperties, myObject, myClass, verboseParam);
	}

	public static void setFieldsFromProperties(final Properties myProperties, final Object myObject, final Class<?> myClass,
			final Boolean... verboseParam) {
		boolean verbose = VarArgsUtils.getMyArg(true, verboseParam);
		StringBuilder stringBuilder = new StringBuilder();
		logger.finest(StringUtils.concat("Starting setFieldsFromPropertiesFile .class: ", myClass.getName(), " **********"));
		HashMap<String, Field> fieldsMap = getFieldsHashMap(myClass);
		MapUtils.printMap(fieldsMap, Level.FINEST);
		Set<?> keySet = myProperties.keySet();
		Field myField = null;
		Method method = null;
		try {
			for (Object keyObject : keySet) {
				String key = (String) keyObject;
				logger.finest("key:".concat(key));
				myField = fieldsMap.get(key);
				if (myField != null) {
					Class<?>[] parametersTypes = new Class[1];
					Class<?> type = myField.getType();
					parametersTypes[0] = type;
					logger.finest("Type: " + type.getName());
					String methodName = "set" + StringUtils.first2Upper(key);
					logger.finest(StringUtils.concat("methodName: ", methodName));
					method = myClass.getMethod(methodName, parametersTypes);
					Object[] arglist = new Object[1];
					String value = myProperties.getProperty(key);
					TypeEnum typeEnum = TypeEnum.getTypeEnum(type.getName());
					switch (typeEnum) {
					case STRING:
						arglist[0] = StringUtils.substituteEnvironmentVariables(value);
						break;
					case INT:
						arglist[0] = Integer.parseInt(StringUtils.substituteEnvironmentVariables(value));
						break;
					case LONG:
						arglist[0] = Long.parseLong(StringUtils.substituteEnvironmentVariables(value));
						break;
					case BOOLEAN:
						String booleanStringParameter = StringUtils.substituteEnvironmentVariables(value);
						arglist[0] = booleanStringParameter.equalsIgnoreCase("yes") || booleanStringParameter.equalsIgnoreCase("true");
						break;
					case LOCALE:
						String[] localeStringArray = value.split(OzConstants.MINUS_SIGN);
						Locale locale = new Locale(localeStringArray[0], localeStringArray[1]);
						arglist[0] = locale;
						break;
					case COLOR:
						arglist[0] = ColorUtils.getColor(value);
						break;
					default:
						logger.severe("Invalid parameter.Processing has been terminated!");
						System.exit(-1);
						break;

					}
					logger.finest(StringUtils.concat("methodName: ", method.getName(), " classObject: ", myObject.toString(), " arglist[0] ",
							arglist[0].toString()));
					method.invoke(myObject, arglist);
					stringBuilder.append(SystemUtils.LINE_SEPARATOR + myField.getName() + " set to: " + arglist[0].toString());
				} else {
					logger.finest("no field named " + key + " **************");
				}
			}
		} catch (Exception ex) {
			logger.warning("Field: " + myField);
			if (method != null) {
				logger.warning("Method: " + method.getName());
			}
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		if (verbose) {
			logger.info(stringBuilder.toString());
		}
	}

	public static void setStaticFieldsFromPropertiesFilePath(final String propertiesFilePath, final Object myObject) {
		Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		setFieldsFromProperties(properties, myObject);
	}
}
