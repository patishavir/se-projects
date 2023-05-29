package oz.infra.ssh.jsch.test;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.security.MessageDigestAlgorithm;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.jsch.JschUtils;
import oz.infra.string.StringUtils;
import oz.infra.varargs.VarArgsUtils;

public class JschUtilsTest {
	private static final String DEFAULT_SOURCE_PATH = "C:\\temp\\888.777";
	private static Logger logger = JulUtils.getLogger();

	public static Properties getProperties() {
		String sourceFilePath = DEFAULT_SOURCE_PATH;
		File sourceFile = new File(sourceFilePath);
		if (!sourceFile.isFile()) {
			FileUtils.writeFile(sourceFile, "12345\nacbefg\nHIJKLMNOP\n!@#$%^&*()");
		}
		Properties properties = new Properties();
		// properties.put(SshParams.SERVER, "S5380369");
		// properties.put(SshParams.SERVER, "suswastest1");
		// properties.put(SshParams.SERVER, "snifp-udb01");
		properties.put(SshParams.SERVER, "suswastest");
		properties.put(SshParams.USER_NAME, "root");
		// properties.put(SshParams.PASSWORD, "99maniaroma6777");
		properties.put(SshParams.SOURCE_FILE, sourceFilePath);
		properties.put(SshParams.DESTINATION_FILE, "/tmp/zvl.666");
		properties.put(SshParams.PORT, "22");
		properties.put(SshParams.DIRECTION, "from");
		properties.put(SshParams.DIRECTION, "to");
		properties.put(SshParams.COMMAND_LINE, "hostname;uptime;cal;df -g;/usr/java14/jre/bin/java -version 2>&1;/usr/java14/jre/bin/java -bersion");
		// properties.put(SshParams.PRIVATE_KEY,
		// "C:\\Users\\s177571\\.ssh\\id_rsa");
		// properties.put(SshParams.KNOWN_HOSTS,
		// "C:\\Users\\s177571\\.ssh\\known_hosts");
		return properties;
	}

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// testBadExecfail126();
		// testBadExecfail127();
		// testBadExecfail2();
		// testExec();
		testExecOK();
		// testBadExec2();
		// testScpFrom();
		// testScpTo();
		// logger.info(PrintUtils.getSeparatorLine("="));
		// testBadExec1();
		// logger.info(PrintUtils.getSeparatorLine("="));
		// testSftp1();
		// logger.info(PrintUtils.getSeparatorLine("="));
		// testSftp2();
		// logger.info(PrintUtils.getSeparatorLine("="));
		testScpTo();
		// logger.info(PrintUtils.getSeparatorLine("="));
		// testScpFrom();
		// logger.info(PrintUtils.getSeparatorLine("="));
		 testScpToFrom();
		// logger.info("all done ...");
		// testRunScript();
		// System.exit(0);
	}

	private static void testBadExec1() {
		Properties properties = getProperties();
		properties.put(SshParams.COMMAND_LINE, "cal;df -UUU");
		logger.info(String.valueOf(JschUtils.exec(properties)));
	}

	private static void testBadExec2() {
		Properties properties = getProperties();
		properties.put(SshParams.COMMAND_LINE, "cal;df -UUU;ls balbal;hostname;galgalim");
		SystemCommandExecutorResponse scer = JschUtils.exec(properties);
		logger.info(scer.getExecutorResponse());
	}

	private static void testBadExecfail126() {
		// 126 = not an excutable
		Properties properties = getProperties();
		properties.put(SshParams.COMMAND_LINE, "/tmp/ntpdate.log");
		SystemCommandExecutorResponse scer = JschUtils.exec(properties);
		logger.info(scer.getExecutorResponse());
	}

	private static void testBadExecfail127() {
		// 127 = command not found
		Properties properties = getProperties();
		properties.put(SshParams.COMMAND_LINE, "galgalon -UUU");
		SystemCommandExecutorResponse scer = JschUtils.exec(properties);
		logger.info(scer.getExecutorResponse());
	}

	private static void testBadExecfail2() {
		// 126 = not an excutable
		Properties properties = getProperties();
		properties.put(SshParams.COMMAND_LINE, "ls /hhhhhhh");
		SystemCommandExecutorResponse scer = JschUtils.exec(properties);
		logger.info(scer.getExecutorResponse());
	}

	private static void testExec() {
		Properties properties = getProperties();
		properties.put(SshParams.COMMAND_LINE,
				"hostname;oslevel -s;uptime;cal;df -g;/usr/java14/jre/bin/java -version 2>&1;/usr/java14/jre/bin/java -version");
		SystemCommandExecutorResponse scer = JschUtils.exec(properties);
		logger.finest(scer.getExecutorResponse());
	}

	private static void testExecOK() {
		Properties properties = getProperties();
		properties.put(SshParams.COMMAND_LINE, "hostname;uptime;cal;df -g;/usr/java14/jre/bin/java -version 2>&1;oslevel -s");
		SystemCommandExecutorResponse scer = JschUtils.exec(properties);
		logger.info(scer.getExecutorResponse());
	}

	private static void testRunScript() {
		Properties properties = getProperties();
		String scriptContents = FileUtils.readTextFileFromClassPath("/oz/infra/ssh/jsch/test/scripts/1.script");
		scriptContents = StringUtils.winEol2UnixEol(scriptContents);
		properties.put(SshParams.COMMAND_LINE, scriptContents);
		SystemCommandExecutorResponse scer = JschUtils.exec(properties);
		logger.info(scer.getExecutorResponse());
	}

	private static void testScpFrom(Properties... propertiesParam) {
		Properties properties = VarArgsUtils.getMyArg(getProperties(), propertiesParam);
		String source = properties.getProperty(SshParams.SOURCE_FILE);
		String destination = properties.getProperty(SshParams.DESTINATION_FILE);
		properties.put(SshParams.DESTINATION_FILE, source + DateTimeUtils.getTimeStamp());
		properties.put(SshParams.SOURCE_FILE, destination);
		int returnCode = JschUtils.scpFrom(properties);
		logger.info(String.valueOf(returnCode));
	}

	private static Properties testScpTo() {
		Properties properties = getProperties();
		String destination = properties.getProperty(SshParams.DESTINATION_FILE);
		properties.put(SshParams.DESTINATION_FILE, destination + OzConstants.UNDERSCORE + DateTimeUtils.getTimeStamp());
		int returnCode = JschUtils.scpTo(properties);
		logger.info(StringUtils.concat("returnCode: ", String.valueOf(returnCode)));
		return properties;
	}

	private static Properties testScpTo2() {
		Properties properties = getProperties();
		String destination = properties.getProperty(SshParams.DESTINATION_FILE);
		properties.put(SshParams.DESTINATION_FILE, destination + OzConstants.UNDERSCORE + DateTimeUtils.getTimeStamp());
		int returnCode = JschUtils.scpTo(properties);
		logger.info(StringUtils.concat("returnCode: ", String.valueOf(returnCode)));
		return properties;
	}

	private static void testScpToFrom() {
		Properties properties = testScpTo();
		testScpFrom(properties);
		FileUtils.performMessageDigestsComparison(properties.getProperty(SshParams.DESTINATION_FILE), DEFAULT_SOURCE_PATH,
				MessageDigestAlgorithm.MD5.getAlgorithmName());
		FileUtils.performMessageDigestsComparison(properties.getProperty(SshParams.DESTINATION_FILE), DEFAULT_SOURCE_PATH,
				MessageDigestAlgorithm.SHA1.getAlgorithmName());
		FileUtils.performMessageDigestsComparison(properties.getProperty(SshParams.DESTINATION_FILE), DEFAULT_SOURCE_PATH,
				MessageDigestAlgorithm.SHA256.getAlgorithmName());
		FileUtils.performFullBinaryComparison(properties.getProperty(SshParams.DESTINATION_FILE), DEFAULT_SOURCE_PATH);
	}

	private static void testSftp1() {
		Properties properties = getProperties();
		String destination = properties.getProperty(SshParams.DESTINATION_FILE);
		properties.put(SshParams.DESTINATION_FILE, destination + DateTimeUtils.getTimeStamp());
		logger.info(String.valueOf(JschUtils.sftp(properties)));

	}

	private static void testSftp2() {
		Properties properties = getProperties();
		String destination = properties.getProperty(SshParams.DESTINATION_FILE);
		properties.put(SshParams.DESTINATION_FILE, destination + DateTimeUtils.getTimeStamp());
		logger.info(String.valueOf(JschUtils.sftp(properties)));

	}
}
