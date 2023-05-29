package oz.utils.autodeploy.ear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.operaion.Outcome;
import oz.infra.system.SystemUtils;
import oz.utils.autodeploy.DeploymentManagerProperties;
import oz.utils.autodeploy.WsadminActions;
import oz.utils.autodeploy.common.AutoDeployUtils;
import oz.utils.autodeploy.tasks.RemoteFileSystemOperationsTask;
import oz.utils.autodeploy.tasks.WsadminTask;

public class WsadminProcessor {
	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());

	private static String getJythonScriptSourceFilePath(final EarFileParameters earFileParameters,
			final Properties deploymentManagerProperties) {
		String jythonScriptSourceFilePath = earFileParameters.getJythonScriptSourceFilePath();
		if (jythonScriptSourceFilePath == null) {
			jythonScriptSourceFilePath = deploymentManagerProperties
					.getProperty(DeploymentManagerProperties.DEFAULT_JYTHON_SCRIPT_FULL_PATH);
		}
		return jythonScriptSourceFilePath;
	}

	public static Outcome runWsadmin(final EarFileParameters earFileParameters, final String localEarFilePath) {
		String applicationName = earFileParameters.getApplicationName();
		String system = earFileParameters.getSystem();
		Properties deploymentManagerProperties = AutoDeployUtils.getDeploymentManagerProperties(system);
		String localJythonScriptPathParam = getJythonScriptSourceFilePath(earFileParameters,
				deploymentManagerProperties);
		String earFilePath = localEarFilePath;
		String deploymentManagerServer = deploymentManagerProperties.getProperty(DeploymentManagerProperties.SERVER);
		if (!SystemUtils.isCurrentHost(deploymentManagerServer)) {
			earFilePath = RemoteFileSystemOperationsTask.copyFile2RemoteWithTimeStampedName(deploymentManagerProperties,
					localEarFilePath);
		}
		String[] parametersArray = { WsadminActions.UPDATEAPPLICATION.toString(), earFilePath, applicationName };
		ArrayList<String> parameters = new ArrayList<String>(Arrays.asList(parametersArray));
		Outcome outcome = WsadminTask.runWsadminScript(system, localJythonScriptPathParam, parameters);
		return outcome;
	}
}