package oz.utils.was.autodeploy.watcher7;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.watcher.WatchStarter;
import oz.infra.system.SystemUtils;
import oz.utils.was.autodeploy.AutoDeployEarMain;
import oz.utils.was.autodeploy.parameters.AutoDeployGlobalParameters;
import oz.utils.was.autodeploy.portal.PortalDeploymentProcessor;
import oz.utils.was.autodeploy.portal.parameters.PortalDeploymentParameters;

public class AutoDeployPortalWatcherMain {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// SystemUtils.printSystemProperties();
		SystemUtils.logImplementationVersion(AutoDeployEarMain.class);
		if (SystemUtils.validateClassPath()) {
			String autoDeployGlobalPropertiesFilePath = args[0];
			AutoDeployGlobalParameters.processParameters(autoDeployGlobalPropertiesFilePath);
			String portalDeploymentPropertiesFilePath = args[1];
			PortalDeploymentParameters.processParameters(portalDeploymentPropertiesFilePath);
			String folder2WatchPath = args[2];
			long wait4CopyCompletion = 2000;
			new WatchStarter(folder2WatchPath, new PortalDeploymentProcessor(), wait4CopyCompletion);
		}
	}
}
