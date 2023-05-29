package oz.utils.run;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.cryptography.CryptographyUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.string.StringUtils;

public class SetPass {
	private static final String PASSWORD = "password";
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		substituteDecryptRun(args);
	}

	private static void substituteDecryptRun(final String[] args) {
		String propertiesFilePath = args[0];
		Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		String command = properties.getProperty("command");
		// logger.info(command);
		String password = properties.getProperty(PASSWORD);
		Map<String, String> map = new HashMap<String, String>();
		map.put(PASSWORD, CryptographyUtils.decryptString(password));
		command = StringUtils.substituteVariables(command, map);
		String[] params = command.split(RegexpUtils.REGEXP_WHITE_SPACE);
		// ArrayUtils.printArray(params, Level.INFO);
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner
				.run(params);
		logger.info(systemCommandExecutorResponse.getExecutorResponse());
	}

}
