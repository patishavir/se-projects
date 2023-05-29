package oz.utils.run;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;

public class RunAs {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String psexecPath = null;
		String userName = null;
		String encryptedPassword = null;
		String targetMachine = null;
		String[] parametersArray = null;
		String propertiesFilePath = args[0];
		String fileType = PathUtils.getFileExtension(propertiesFilePath);
		if (fileType.equals("properties")) {
			logger.info("processing properties file " + propertiesFilePath);
			Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
			properties = PropertiesUtils.updatePropertiesUsingEnvironmentVarialbes(properties);
			PropertiesUtils.printProperties(properties, Level.INFO);
			psexecPath = properties.getProperty("psexecPath");
			userName = properties.getProperty("userName");
			targetMachine = properties.getProperty("targetMachine");
			encryptedPassword = properties.getProperty("processid");
			parametersArray = properties.getProperty("parametersArray").split(OzConstants.COMMA);
		} else {
			psexecPath = args[0];
			userName = args[1];
			encryptedPassword = args[2];
			targetMachine = args[3];
			parametersArray = ArrayUtils.selectArrayRowsByStartingRow(args, 4);
		}
		FileUtils.terminateIfFileDoesNotExist(psexecPath);
		// FileUtils.terminateIfFileDoesNotExist(parametersArray[0]);
		String password = CryptographyUtils.decryptString(encryptedPassword,
				EncryptionMethodEnum.WITH_HOSTNAME_AND_USERNAME);
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.runAs(psexecPath,
				userName, password, targetMachine, parametersArray);
		if (systemCommandExecutorResponse != null) {
			logger.info(systemCommandExecutorResponse.getExecutorResponse());
		} else {
			logger.warning("systemCommandExecutorResponse is null !");
		}
	}

}
