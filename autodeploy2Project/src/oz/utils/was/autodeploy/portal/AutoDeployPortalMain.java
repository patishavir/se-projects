package oz.utils.was.autodeploy.portal;

import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;
import oz.utils.was.autodeploy.AutoDeployEarMain;
import oz.utils.was.autodeploy.parameters.AutoDeployGlobalParameters;
import oz.utils.was.autodeploy.portal.parameters.PortalDeploymentParameters;

public class AutoDeployPortalMain {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// SystemUtils.printSystemProperties();
		logger.info("args: ".concat(ArrayUtils.getAsDelimitedString(args)));
		SystemUtils.logImplementationVersion(AutoDeployEarMain.class);
		if (!SystemUtils.validateClassPath()) {
			logger.warning("Invalid classpath. Processing has been teminated.");
		} else {
			String autoDeployGlobalPropertiesFilePath = args[0];
			AutoDeployGlobalParameters.processParameters(autoDeployGlobalPropertiesFilePath);
			String portalDeploymentPropertiesFilePath = args[1];
			PortalDeploymentParameters.processParameters(portalDeploymentPropertiesFilePath);
			String portalDeploymentFilePath = args[2];
			new PortalDeploymentProcessor().processFile(portalDeploymentFilePath);
		}
	}
}
