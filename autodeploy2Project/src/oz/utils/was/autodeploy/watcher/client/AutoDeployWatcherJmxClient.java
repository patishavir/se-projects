package oz.utils.was.autodeploy.watcher.client;

import java.util.logging.Logger;

import oz.infra.jmx.JmxUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class AutoDeployWatcherJmxClient {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		logger.info(StringUtils.concat("about to perform exit. host: ", host, " port: ", String.valueOf(port), " ..."));
		JmxUtils.performExit(host, port);
		logger.info(StringUtils.concat("exit processing has completed. host: ", host, " port: ", String.valueOf(port),
				" ..."));
	}
}