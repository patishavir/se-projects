package oz.infra.ssh.scp.test;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.datetime.StopWatch;
import oz.infra.logging.jul.JulUtils;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.scp.ScpUtils;

public class TestScpUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StopWatch stopWatch = new StopWatch();
		testScp();
		stopWatch.logElapsedTimeMessage("scp operation complete in");
	}

	private static void testScp() {
		Properties scpProperties = new Properties();
		scpProperties.setProperty(SshParams.SERVER, "suswastest2");
		scpProperties.setProperty(SshParams.USER_NAME, "root");
		String password = "root123";
		String destinationFilePath = "c:\\temp\\lvmt.log";
		password = CryptographyUtils.encryptString(password, EncryptionMethodEnum.WITH_HOSTNAME_AND_USERNAME);
		scpProperties.setProperty(SshParams.PASSWORD, password);
		scpProperties.setProperty(SshParams.DIRECTION, SshParams.FROM);
		scpProperties.setProperty(SshParams.SOURCE_FILE, "/tmp/lvmt.log");
		scpProperties.setProperty(SshParams.DESTINATION_FILE, destinationFilePath);
		File destinationFile = new File(destinationFilePath);
		destinationFile.delete();
		ScpUtils.scp(scpProperties);
		logger.info(String.valueOf(destinationFile.length()));
	}
}
