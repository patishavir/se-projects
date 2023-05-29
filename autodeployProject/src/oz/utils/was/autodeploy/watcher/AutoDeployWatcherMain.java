package oz.utils.was.autodeploy.watcher;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;
import oz.utils.modifyear.ModifyEarMain;

public class AutoDeployWatcherMain {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		logger.info(SystemUtils.getRunInfo());
		if (SystemUtils.validateClassPath()) {
			String applicationUpdatePropertiesFilePath = args[0];
			new AutoDeployWatcher(applicationUpdatePropertiesFilePath);
		} else {
			logger.warning("Invalid classpath. Processing has been teminated.");
		}
	}
}
