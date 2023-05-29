package oz.infra.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.system.env.EnvironmentUtils;
import oz.infra.varargs.VarArgsUtils;

public class PropertiesUtils {

	private static final boolean THROW_ON_LOAD_FAILURE = true;
	private static final boolean LOAD_AS_RESOURCE_BUNDLE = false;
	private static final String SUFFIX = ".properties";
	private static Logger logger = JulUtils.getLogger();

	public static final Properties duplicate(final Properties properties) {
		Properties result = new Properties();
		result.putAll(properties);
		return result;
	}

	public static final String getAsDelimitedString(final Properties properties, final String... delimiters) {
		String propertiesString = null;
		if (properties == null) {
			logger.warning(SystemUtils.getCallerClassAndMethodName() + "  got null parameter. exiting ...");
		} else {
			String delimiter = VarArgsUtils.getMyArg(SystemUtils.LINE_SEPARATOR, delimiters);
			Set<?> propertiesKeySet = properties.keySet();
			StringBuilder stringBuilder = new StringBuilder();
			for (Object key : propertiesKeySet) {
				String keyString = key.toString();
				stringBuilder.append(keyString + " = " + properties.getProperty(keyString) + delimiter);
			}
			propertiesString = stringBuilder.toString();
		}
		return propertiesString;
	}

	public static String[][] getAsStringArray(final Properties properties) {
		String[][] resultsArray = null;
		if (properties != null) {
			int numberOfEntries = properties.size();
			resultsArray = new String[numberOfEntries][2];
			Set<Entry<Object, Object>> entrySet = properties.entrySet();
			int i = 0;
			for (Entry<Object, Object> entry1 : entrySet) {
				resultsArray[i][0] = entry1.getKey().toString();
				resultsArray[i][1] = entry1.getValue().toString();
				i++;
			}
			ArrayUtils.print2DimArray(resultsArray, Level.FINEST);
		}
		return resultsArray;
	}

	public static String[] getAsStringArray(final Properties properties, final String[] keys) {
		String[] resultsArray = new String[keys.length];
		if (properties != null) {
			for (int i = 0; i < keys.length; i++) {
				String value = properties.getProperty(keys[i]);
				if (value == null) {
					logger.warning(StringUtils.concat("key ", keys[i], " not found in properties !"));
				}
				resultsArray[i] = properties.getProperty(keys[i]);
			}
		} else {
			logger.warning(" properites is null !");
		}
		return resultsArray;
	}

	public static final HashMap<String, String> getStringsHashMap(final Properties properties) {
		HashMap<String, String> hashMap = new HashMap<String, String>((Map) properties);
		return hashMap;
	}

	public static Properties loadPropertiesFile(final String propertiesFilePath) {
		// Read properties file.
		Properties properties = null;
		if (propertiesFilePath != null) {
			File propertiesFile = new File(propertiesFilePath);
			if (propertiesFile.exists()) {
				properties = new Properties();
				try {
					FileInputStream in = new FileInputStream(propertiesFilePath);
					properties.load(in);
					in.close();
				} catch (Exception exception) {
					exception.printStackTrace();
					logger.info(exception.getMessage());
				}
			} else {
				logger.warning(propertiesFilePath + " not found !");
			}
		} else {
			logger.warning("propertiesFilePath is null !");
		}
		return properties;
	}

	public static Properties loadPropertiesFile(final String propertiesFilePath, final String charsetString) {
		Properties properties = new Properties();
		try {
			InputStream input = new FileInputStream(propertiesFilePath);
			properties.load(new InputStreamReader(input, charsetString));
		} catch (Exception ex) {
			logger.warning(ExceptionUtils.printMessageAndStackTrace(ex));
			properties = null;
		}
		return properties;
	}

	public static HashMap<String, Properties> loadPropertiesFilesFromFolder(final String propertiesfilesFolderPath) {
		HashMap<String, Properties> propertiesHashMap = new HashMap<String, Properties>();
		return loadPropertiesFilesFromFolder(propertiesfilesFolderPath, propertiesHashMap);
	}

	public static HashMap<String, Properties> loadPropertiesFilesFromFolder(final String propertiesfilesFolderPath,
			final HashMap<String, Properties> propertiesHashMap) {
		logger.info("Processing: " + propertiesfilesFolderPath);
		File propertyFilesFolderFile = new File(propertiesfilesFolderPath);
		if (!propertyFilesFolderFile.exists()) {
			SystemUtils.printMessageAndExit(
					propertiesfilesFolderPath.concat(": folder does not exit. Processing aborted !"), -1);
		}
		if (!propertyFilesFolderFile.isDirectory()) {
			SystemUtils.printMessageAndExit("Parmeter must be a directory. Processing aborted !", -1);
		}
		String[] fileList = propertyFilesFolderFile.list();
		if (fileList.length == 0) {
			SystemUtils.printMessageAndExit("No files to load. Processing aborted !", -1);
		}
		try {
			for (int i = 0; i < fileList.length; i++) {
				String propertyFilePath = propertiesfilesFolderPath + File.separator + fileList[i];
				logger.finest("propertyFilePath: " + propertyFilePath);
				File propertyFile = new File(propertyFilePath);
				if (propertyFile.isFile()) {
					FileInputStream fis = new FileInputStream(propertyFilePath);
					Properties properties = new Properties();
					properties.load(fis);
					String key = fileList[i];
					if (key.lastIndexOf(".") > 1) {
						key = key.substring(0, key.indexOf("."));
					}
					propertiesHashMap.put(key, properties);
					logger.finest("Hashmap key: " + key);
					PropertiesUtils.printProperties(properties, Level.INFO);
					fis.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
		if (propertiesHashMap == null) {
			logger.warning("Hashmap is null !!!");
		} else {
			logger.finest("Hashmap size is " + String.valueOf(propertiesHashMap.size()));
		}
		return propertiesHashMap;
	}

	/**
	 * A convenience overload of
	 * {@link #loadPropertiesFromClassPath(String, ClassLoader)} that uses the
	 * current thread's context classloader.
	 */
	public static Properties loadPropertiesFromClassPath(final String name) {
		return loadPropertiesFromClassPath(name, Thread.currentThread().getContextClassLoader());
	}

	/**
	 * Looks up a resource named 'name' in the classpath. The resource must map
	 * to a file with .properties extention. The name is assumed to be absolute
	 * and can use either "/" or "." for package segment separation with an
	 * optional leading "/" and optional ".properties" suffix. Thus, the
	 * following names refer to the same resource:
	 * 
	 * <pre>
	 * some.pkg.Resource
	 * some.pkg.Resource.properties
	 * some/pkg/Resource
	 * some/pkg/Resource.properties
	 * /some/pkg/Resource
	 * /some/pkg/Resource.properties
	 * </pre>
	 * 
	 * @param name
	 *            classpath resource name [may not be null]
	 * @param loader
	 *            classloader through which to load the resource [null is
	 *            equivalent to the application loader]
	 * 
	 * @return resource converted to java.util.Properties [may be null if the
	 *         resource was not found and THROW_ON_LOAD_FAILURE is false]
	 * @throws IllegalArgumentException
	 *             if the resource was not found and THROW_ON_LOAD_FAILURE is
	 *             true
	 */

	private static Properties loadPropertiesFromClassPath(final String nameParameter,
			final ClassLoader loaderParameter) {
		String name = nameParameter;
		ClassLoader loader = loaderParameter;
		if (name == null) {
			throw new IllegalArgumentException("null input: name");
		}

		if (name.startsWith("/")) {
			name = name.substring(1);
		}

		if (name.endsWith(SUFFIX)) {
			name = name.substring(0, name.length() - SUFFIX.length());
		}

		Properties resultProperties = null;

		InputStream in = null;
		try {
			if (loader == null) {
				loader = ClassLoader.getSystemClassLoader();
			}

			if (LOAD_AS_RESOURCE_BUNDLE) {
				name = name.replace('/', '.');
				// Throws MissingResourceException on lookup failures:
				final ResourceBundle rb = ResourceBundle.getBundle(name, Locale.getDefault(), loader);

				resultProperties = new Properties();
				for (Enumeration keys = rb.getKeys(); keys.hasMoreElements();) {
					final String key = (String) keys.nextElement();
					final String value = rb.getString(key);

					resultProperties.put(key, value);
				}
			} else {
				name = name.replace('.', '/');
				if (!name.endsWith(SUFFIX)) {
					name = name.concat(SUFFIX);
				}
				// Returns null on lookup failures:
				in = loader.getResourceAsStream(name);
				if (in != null) {
					resultProperties = new Properties();
					resultProperties.load(in); // Can throw IOException
				}
			}
		} catch (Exception ex) {
			resultProperties = null;
			ExceptionUtils.printMessageAndStackTrace(ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Throwable ignore) {
				}
			}
		}

		if (THROW_ON_LOAD_FAILURE && (resultProperties == null)) {
			throw new IllegalArgumentException("could not load [" + name + "]" + " as "
					+ (LOAD_AS_RESOURCE_BUNDLE ? "a resource bundle" : "a classloader resource"));
		}

		return resultProperties;
	}

	public static Properties loadPropertiesFromClassPathWithInPackage(final Class clazz,
			final String propertiesFileName) {
		Properties properties = null;
		InputStream inputStream = clazz.getResourceAsStream(propertiesFileName);
		if (inputStream != null) {
			properties = new Properties();
			try {
				properties.load(inputStream); // Can throw IOException
			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
			}
		}
		return properties;
	}

	public static final String printProperties(final Properties properties, final Level... levels) {
		return printProperties(properties, SystemUtils.LINE_SEPARATOR, levels);
	}

	public static final String printProperties(final Properties properties, final String delimiter,
			final Level... levels) {
		Level level = VarArgsUtils.getMyArg(Level.INFO, levels);
		String propertiesString = getAsDelimitedString(properties, delimiter);
		logger.log(level, SystemUtils.getCallerClassName() + " " + SystemUtils.getCallerMethodName()
				+ SystemUtils.LINE_SEPARATOR + propertiesString);
		return propertiesString;
	}

	public static final void printSystemProperties() {
		printProperties(System.getProperties(), Level.INFO);

		// file.encoding The character encoding for the default locale 1.1
		// file.encoding.pkg The package that contains the converters that
		// handle converting between local encodings and Unicode 1.1
		// file.separator The platform-dependent file separator (e.g., "/" on
		// UNIX, "\" for Windows) 1.0
		// java.class.path The value of the CLASSPATH environment variable 1.0
		// java.class.version The version of the Java API 1.0
		// java.compiler The just-in-time compiler to use, if any. The java
		// interpreter provided with the JDK initializes this property from the
		// environment variable JAVA_COMPILER. 1.0
		// java.home The directory in which Java is installed 1.0
		// java.io.tmpdir The directory in which java should create temporary
		// files 1.2
		// java.version The version of the Java interpreter 1.0
		// java.vendor A vendor-specific string 1.0
		// java.vendor.url A vendor URL 1.0
		// line.separator The platform-dependent line separator (e.g., "\n" on
		// UNIX, "\r\n" for Windows) 1.0
		// os.name The name of the operating system 1.0
		// os.arch The system architecture 1.0
		// os.version The operating system version 1.0
		// path.separator The platform-dependent path separator (e.g., ":" on
		// UNIX, "," for Windows) 1.0
		// user.dir The current working directory when the properties were
		// initialized 1.0
		// user.home The home directory of the current user 1.0
		// user.language The two-letter language code of the default locale 1.1
		// user.name The username of the current user 1.0
		// user.region The two-letter country code of the default locale 1.1
		// user.timezone The default time zone

	}

	public static void setPropertiesValuesUsingEnvironmentVariables(final Properties properties, final Level level) {
		Map<String, String> envVariablesHashTable = EnvironmentUtils.getEnvironmentVariablesMap();
		Set keySet = properties.keySet();
		for (Object keyObject : keySet) {
			String key = keyObject.toString();
			String value = envVariablesHashTable.get(key);
			logger.log(level, key + "=" + value);
			if (value != null) {
				properties.put(key, value);
				logger.log(level, "key " + key + ". value changed to " + value);
			}
		}
		printProperties(properties, level);
	}

	public static final Properties updatePropertiesUsingEnvironmentVarialbes(final Properties properties) {
		if (properties != null) {
			Enumeration<Object> keysEnumeration = properties.keys();
			while (keysEnumeration.hasMoreElements()) {
				String key = (String) keysEnumeration.nextElement();
				logger.finest("key: " + key + " value: " + properties.getProperty(key));
				String envVar = EnvironmentUtils.getEnvironmentVariable(key);
				logger.finest("key: " + key + " value: " + envVar);
				if (envVar != null) {
					properties.put(key, envVar);
					logger.info(key + " has been set to " + envVar + " from environment variable.");
				}
			}
		}
		return properties;
	}
}
