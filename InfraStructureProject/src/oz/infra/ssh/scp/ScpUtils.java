package oz.infra.ssh.scp;

import java.util.Properties;
import java.util.logging.Logger;

import examples.SCPExample;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.ssh.SshParams;
import oz.infra.string.StringUtils;

public class ScpUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void scp(final Properties scpPropertiesArg) {
		// TODO Auto-generated method stub
		// "Usage: SCPExample <server:port> <username> <password> to|from
		// <src_file> <dst_file>"
		StopWatch stopWatch = new StopWatch();
		Properties scpProperties = (Properties) scpPropertiesArg.clone();
		String password = CryptographyUtils.getPassword(scpProperties);
		scpProperties.put(ParametersUtils.PASSWORD, password);
		String[] keys = { ParametersUtils.SERVER, ParametersUtils.USER_NAME, ParametersUtils.PASSWORD,
				SshParams.DIRECTION, SshParams.SOURCE_FILE, SshParams.DESTINATION_FILE };
		String[] argv = PropertiesUtils.getAsStringArray(scpProperties, keys);
		scp(argv);
		long elapsedTime = stopWatch.getElapsedTimeLong();
		logger.info(StringUtils.concat("scp operation has completed in ", String.valueOf(elapsedTime), " milliseconds.",
				"\nsource file: ", scpProperties.getProperty(SshParams.SOURCE_FILE), "  target file: ",
				scpProperties.getProperty(SshParams.DESTINATION_FILE), "  on: ",
				scpProperties.getProperty(SshParams.SERVER)));
	}

	public static void scp(final String scrFile, final Properties scpProperties) {
		scpProperties.setProperty(SshParams.SOURCE_FILE, scrFile);
		scp(scpProperties);
	}

	public static void scp(final String scpPropertiesFilePath) {
		Properties scpProperties = PropertiesUtils.loadPropertiesFile(scpPropertiesFilePath);
		scp(scpProperties);
	}

	public static void scp(final String[] argv) {
		SCPExample.main(argv);
	}
}