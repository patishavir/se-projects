package oz.infra.remote.test;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;
import oz.infra.remote.RemoteOrLocalUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.jsch.test.JschUtilsTest;
import oz.infra.system.SystemUtils;

public class TestRemoteOrLocalUtils {
	public static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testCopy();
		testExec();
	}

	private static void testCopy() {
		Properties properties = JschUtilsTest.getProperties();
		int retcode = RemoteOrLocalUtils.copy(properties);
		logger.info("return code: " + String.valueOf(retcode));
		logger.info(PrintUtils.getSeparatorLine(OzConstants.EQUAL_SIGN));
		String localhost = SystemUtils.getLocalHostName();
		properties.setProperty(SshParams.SERVER, localhost);
		retcode = RemoteOrLocalUtils.copy(properties);
		logger.info("return code: " + String.valueOf(retcode));
		logger.info(PrintUtils.getSeparatorLine(OzConstants.EQUAL_SIGN));
		properties.setProperty(SshParams.DESTINATION_FILE, properties.getProperty(SshParams.SOURCE_FILE));
		retcode = RemoteOrLocalUtils.copy(properties);
		logger.info("return code: " + String.valueOf(retcode));
		logger.info(PrintUtils.getSeparatorLine(OzConstants.EQUAL_SIGN));
	}

	private static void testExec() {
		SystemCommandExecutorResponse scer = null;
		Properties properties = JschUtilsTest.getProperties();
		scer = RemoteOrLocalUtils.exec(properties);
		logger.info("return code: " + String.valueOf(scer.getReturnCode()));
		logger.info(PrintUtils.getSeparatorLine(OzConstants.EQUAL_SIGN));
		String localhost = SystemUtils.getLocalHostName();
		properties.setProperty(SshParams.SERVER, localhost);
		properties.setProperty(SshParams.COMMAND_LINE, "hostname");
		scer = RemoteOrLocalUtils.exec(properties);
		logger.info("return code: " + String.valueOf(scer.getReturnCode()));
		logger.info(PrintUtils.getSeparatorLine(OzConstants.EQUAL_SIGN));
		properties.setProperty(SshParams.COMMAND_LINE, "c:\\windows\\system32\\ping.exe  suswastest");
		scer = RemoteOrLocalUtils.exec(properties);
		logger.info("return code: " + String.valueOf(scer.getReturnCode()));
		logger.info(PrintUtils.getSeparatorLine(OzConstants.EQUAL_SIGN));
	}
}
