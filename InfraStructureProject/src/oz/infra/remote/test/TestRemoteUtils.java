package oz.infra.remote.test;

import java.util.Properties;

import oz.infra.array.ArrayUtils;
import oz.infra.remote.RemoteUtils;
import oz.infra.ssh.SshParams;

public class TestRemoteUtils {

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		testRunRemoteCommandOnTargetServers(args);
	}

	private static void testRunRemoteCommandOnTargetServers(final String[] args) {
		String commandLine = args[0];
		String[] targetServersArray = ArrayUtils.shift(args, 1);
		Properties remoteProperties = getRemoteProperties();
		RemoteUtils.runRemoteCommandOnTargetServers(commandLine, remoteProperties, targetServersArray);
	}

	private static Properties getRemoteProperties() {
		Properties properties = new Properties();
		properties.setProperty(SshParams.PORT, "22");
		properties.setProperty(SshParams.USER_NAME, "root");
		properties.setProperty(SshParams.DIRECTION, SshParams.TO);
		return properties;

	}
}
