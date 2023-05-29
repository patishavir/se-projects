package oz.infra.ssh;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.parameters.ParametersUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.string.StringUtils;
import oz.infra.unix.UnixConstants;

public class SshUtils {
	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());

	public static Properties decryptPassword(final Properties sshProperties) {
		String password = sshProperties.getProperty(SshParams.PASSWORD);
		if (password != null) {
			String delimiter = sshProperties.getProperty(SshParams.DELIMITER);
			String endecryptionMethod = sshProperties.getProperty(SshParams.ENCRYPTION_METHOD);
			String decryptedPassword = CryptographyUtils.decryptString(password,
					EncryptionMethodEnum.valueOf(endecryptionMethod), delimiter);
			sshProperties.setProperty(SshParams.PASSWORD, decryptedPassword);
			sshProperties.setProperty(SshParams.ENCRYPTION_METHOD, EncryptionMethodEnum.NONE.toString());
		}
		return sshProperties;
	}

	public static SystemCommandExecutorResponse runRemoteCommand(final Properties sshProperties) {
		// "usage: RunRemoteCommand2 <server[:port]> <username> <password>
		// <command-line>"
		String server = sshProperties.getProperty(ParametersUtils.SERVER);
		String userName = sshProperties.getProperty(ParametersUtils.USER_NAME);
		String password = CryptographyUtils.getPassword(sshProperties);
		String[] commandLineArray = sshProperties.getProperty(SshParams.COMMAND_LINE).split(OzConstants.BLANK);
		String[] parametersArray1 = { server, userName, password };
		String[] parametersArray = ArrayUtils.concatenate1DimArrays(parametersArray1, commandLineArray);
		logger.info(StringUtils.concat("Start remote command. server:", server, " user: ", userName, " command: ",
				ArrayUtils.getAsDelimitedString(commandLineArray, OzConstants.TAB)));
		return RunRemoteCommand3.runRemoteCommand(parametersArray);
	}

	public static SystemCommandExecutorResponse runRemoteCommand(final String sshPropertiesFilePath) {
		Properties sshProperties = PropertiesUtils.loadPropertiesFile(sshPropertiesFilePath);
		return runRemoteCommand(sshProperties);
	}

	public static Properties getDefaultSshPorperties() {
		Properties sshProperties = new Properties();
		sshProperties.setProperty(SshParams.USER_NAME, UnixConstants.ROOT_USER);
		return sshProperties;
	}
}
