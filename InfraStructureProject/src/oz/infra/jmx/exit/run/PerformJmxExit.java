package oz.infra.jmx.exit.run;

import java.util.logging.Logger;

import oz.infra.jmx.JmxUtils;
import oz.infra.logging.jul.JulUtils;

public class PerformJmxExit {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String hostName = args[0];
		int port = Integer.parseInt(args[1]);
		JmxUtils.performExit(hostName, port);
	}

}