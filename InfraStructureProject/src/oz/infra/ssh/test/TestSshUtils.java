package oz.infra.ssh.test;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.SshUtils;

public class TestSshUtils {
	public static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// StopWatch stopWatch = new StopWatch();
		testSsh();
		// stopWatch.logElapsedTimeMessage("scp operation complete in");

	}

	private static void testSsh() {
		Properties sshProperties = new Properties();
		sshProperties.setProperty(ParametersUtils.SERVER, "suswastest2");
		sshProperties.setProperty(ParametersUtils.USER_NAME, "root");
		String password = "root123";
		password = CryptographyUtils.encryptString(password, EncryptionMethodEnum.WITH_HOSTNAME_AND_USERNAME);
		sshProperties.setProperty(ParametersUtils.PASSWORD, password);
		sshProperties.setProperty(ParametersUtils.ENCRYPTION_METHOD, "WITH_HOSTNAME_AND_USERNAME");
		sshProperties.setProperty(SshParams.COMMAND_LINE, "ps -ef | grep  -i java");
		SshUtils.runRemoteCommand(sshProperties);
	}

}
