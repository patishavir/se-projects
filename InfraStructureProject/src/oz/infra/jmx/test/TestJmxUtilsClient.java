package oz.infra.jmx.test;

import java.util.logging.Logger;

import oz.infra.jmx.JmxUtils;
import oz.infra.logging.jul.JulUtils;

public class TestJmxUtilsClient {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		JmxUtils.performExit(args[0], Integer.parseInt(args[1]));
	}

}