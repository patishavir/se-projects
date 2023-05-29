package oz.infra.parameters;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.string.StringUtils;

public class ParametersUtils {
	public static final String SERVER = "server";
	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String PROTOCOL = "protocol";
	public static final String URL = "url";
	public static final String USER_NAME = "userName";
	public static final String PASSWORD = "password";
	public static final String ENCRYPTION_METHOD = "encryptionMethod";
	public static final String DELIMITER = "delimiter";
	public static final String TIMESTAMP = "timestamp";
	public static final String LOG_FILE_PATH = "logFilePath";
	public static final String SUMMARY_LOG_FILE_PATH = "summaryLogFilePath";
	public static final String LOGS_FOLDER = "logsFolder";
	private static final String SET_ROOT_FOLDER_PATH_METHOD_NAME = "setRootFolderPath";
	public static final String SQL_FILE_NAME_RANGE = "sqlFileNameRange";
	private static Logger logger = JulUtils.getLogger();

	public static String processPatameters(final String propertiesFilePath, final Object myObject) {
		FileUtils.terminateIfFileDoesNotExist(propertiesFilePath);
		String rootFolderPath = PathUtils.getParentFolderPath(PathUtils.getAbsolutePath(propertiesFilePath));
		logger.finest("rootFolderPath: " + rootFolderPath);
		logger.info(StringUtils.concat("\nStarting patameters processing. properties file path: ", propertiesFilePath,
				" myObject: ", myObject.toString()));
		String[] parameters = { rootFolderPath };
		ReflectionUtils.invokeMethod(myObject, SET_ROOT_FOLDER_PATH_METHOD_NAME, parameters);
		Properties parametersProperties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		processPatameters(parametersProperties, myObject);
		return rootFolderPath;
	}

	public static void processPatameters(final Properties parametersProperties, final Object myObject) {
		Class<?> myClass = null;
		if (myObject instanceof Class<?>) {
			myClass = (Class<?>) myObject;
		} else {
			myClass = myObject.getClass();
		}
		PropertiesUtils.printProperties(parametersProperties, Level.INFO);
		PropertiesUtils.updatePropertiesUsingEnvironmentVarialbes(parametersProperties);
		ReflectionUtils.setFieldsFromProperties(parametersProperties, myObject, myClass);
	}

}