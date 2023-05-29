package oz.utils.was.autodeploy;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.utils.was.autodeploy.parameters.AutoDeployGlobalParameters;

public class AutoDeployEarMain {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		SystemPropertiesUtils.printSystemProperties();
		logger.info(SystemUtils.getImplementationVersion(AutoDeployEarMain.class));
		if (SystemUtils.validateClassPath()) {
			String autoDeployGlobalPropertiesFilePath = args[0];
			AutoDeployGlobalParameters.processParameters(autoDeployGlobalPropertiesFilePath);
			AutoDeployEarUtils.processEarsPatameter(args);
		}
	}
}