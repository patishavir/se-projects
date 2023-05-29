package oz.infra.system.env;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.MapUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.string.StringUtils;

public class EnvironmentUtils {
	public static enum Scope_Enum {
		LOCAL_MACHINE_SCOPE, CURRENT_USER_SCOPE
	}

	private static final String SETX_PATH = "c:\\windows\\system32\\setx.exe";
	private static Logger logger = JulUtils.getLogger();

	public static String getActualEnvVarValue(final String defaultValue, final String varName) {
		String environmentEnvVar = EnvironmentUtils.getEnvironmentVariable(varName);
		String actualValue = defaultValue;
		if (environmentEnvVar != null && environmentEnvVar.length() > 0) {
			actualValue = environmentEnvVar;
		}
		return actualValue;
	}

	public static String getEnvironmentVariable(final String variable) {
		Map<String, String> envVariablesHashTable = System.getenv();
		String value = envVariablesHashTable.get(variable);
		logger.finest(variable + "=" + value);
		return value;
	}

	public static Map<String, String> getEnvironmentVariablesMap() {
		Map<String, String> envVariablesHashTable = System.getenv();
		return envVariablesHashTable;
	}

	public static Map<String, String> printEnvironmentVariablesMap() {
		Map<String, String> envVariablesHashTable = System.getenv();
		MapUtils.printMap(envVariablesHashTable, Level.INFO);
		return envVariablesHashTable;
	}

	public static void setJvmEnvironmentVariable(final Map<String, String> newenv) {
		try {
			Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
			Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
			theEnvironmentField.setAccessible(true);
			Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
			env.putAll(newenv);
			Field theCaseInsensitiveEnvironmentField = processEnvironmentClass
					.getDeclaredField("theCaseInsensitiveEnvironment");
			theCaseInsensitiveEnvironmentField.setAccessible(true);
			Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
			cienv.putAll(newenv);
		} catch (NoSuchFieldException e) {
			try {
				Class[] classes = Collections.class.getDeclaredClasses();
				Map<String, String> env = System.getenv();
				for (Class cl : classes) {
					if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
						Field field = cl.getDeclaredField("m");
						field.setAccessible(true);
						Object obj = field.get(env);
						Map<String, String> map = (Map<String, String>) obj;
						map.clear();
						map.putAll(newenv);
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void setJvmEnvironmentVariable(final String key, final String value) {
		Map<String, String> envVarMap = new HashMap<String, String>();
		envVarMap.put(key, value);
		setJvmEnvironmentVariable(envVarMap);
	}

	public static void setWindowsEnvironmentVariable(final String variable, final String value,
			final Scope_Enum... scope) {
		File setxFile = new File(SETX_PATH);
		if (setxFile.isFile()) {
			String[] parameters = { SETX_PATH, variable, value };
			List<String> paramterList = ArrayUtils.getAsList(parameters);
			if (scope != null && scope.length > 0 && scope[0].equals(Scope_Enum.LOCAL_MACHINE_SCOPE)) {
				paramterList.add("/M");
			}
			ListUtils.getAsDelimitedString(paramterList, Level.FINEST);
			SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(paramterList);
			logger.finest(systemCommandExecutorResponse.getExecutorResponse());
		} else {
			logger.warning(StringUtils.concat(SETX_PATH, " not found. variable ", variable, " has not been set."));
		}
	}
}
