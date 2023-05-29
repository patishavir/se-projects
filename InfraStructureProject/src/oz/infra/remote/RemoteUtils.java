package oz.infra.remote;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.NioUtils;
import oz.infra.print.PrintUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.jsch.JschUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class RemoteUtils {
	private static Logger logger = JulUtils.getLogger();

	public static String runRemoteCommandOnTargetServers(final String commandLine, final Properties remoteProperties,
			final String[] targetServersArray) {
		int rc = Integer.MIN_VALUE;
		int maxRc = Integer.MIN_VALUE;
		String hostName = SystemUtils.getHostname();
		remoteProperties.put(SshParams.COMMAND_LINE, commandLine);
		StringBuilder targetApplicationServersSb = new StringBuilder();
		logger.finest(StringUtils.concat("commandLine:", SystemUtils.LINE_SEPARATOR, commandLine));
		for (String targetApplicationServer : targetServersArray) {
			logger.info(PrintUtils.getSeparatorLine(StringUtils.concat("running commnad on hostName: ", hostName,
					" targetServer: ", targetApplicationServer), 2, 0));
			if (!SystemUtils.isCurrentHost(targetApplicationServer)) {
				remoteProperties.put(SshParams.SERVER, targetApplicationServer);
				SystemCommandExecutorResponse scer = JschUtils.exec(remoteProperties);
				rc = scer.getReturnCode();
				if (rc > maxRc) {
					maxRc = rc;
				}
			} else {
				String script2RunLocally = commandLine;
				if (!FileUtils.isFileExists(script2RunLocally)) {
					script2RunLocally = NioUtils.makeRunnableTempFile(commandLine);
				}
				SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(script2RunLocally);
				logger.info(scer.getExecutorResponse());
				rc = scer.getReturnCode();
				if (rc > maxRc) {
					maxRc = rc;
				}
				NioUtils.setPosixFilePermissions(script2RunLocally, "-rw-------");
			}
			if (targetApplicationServersSb.length() > 0) {
				targetApplicationServersSb.append(OzConstants.COMMA);
			}
			targetApplicationServersSb.append(targetApplicationServer);
		}
		String completionMessage = StringUtils.concat(commandLine, " has run on ",
				targetApplicationServersSb.toString(), " hostname: ", hostName);
		logger.finest(PrintUtils.getSeparatorLine(completionMessage, 2, 0));
		String summaryMessage = StringUtils.concat(" commandLine has run on ", targetApplicationServersSb.toString(),
				" hostname: ", hostName, " max RC: " + String.valueOf(maxRc));
		return summaryMessage;

	}
}
