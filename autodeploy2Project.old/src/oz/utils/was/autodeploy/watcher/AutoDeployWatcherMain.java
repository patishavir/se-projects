package oz.utils.was.autodeploy.watcher;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;
import oz.utils.was.autodeploy.AutoDeployEarProcessor;
import oz.utils.was.autodeploy.parameters.AutoDeployGlobalParameters;

public class AutoDeployWatcherMain {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		SystemUtils.logImplementationVersion(AutoDeployWatcherMain.class);
		if (SystemUtils.validateClassPath()) {
			String autoDeployGlobalPropertiesFilePath = args[0];
			String folder2WatchPath = args[1];
			AutoDeployGlobalParameters.processParameters(autoDeployGlobalPropertiesFilePath);
			new AutoDeployWatcher(folder2WatchPath, new AutoDeployEarProcessor());
		
		}
	}
}
