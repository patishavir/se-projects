package oz.utils.autodeploy.common;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.UnixParameters;
import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.SshUtils;
import oz.infra.ssh.jsch.JschUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.utils.autodeploy.DeploymentManagerProperties;

public class AutoDeployUtils {

	private static final Logger logger = JulUtils.getLogger();

	public static void copyFile2RemoteServers(final String localFilePath, final String remoteFilePath,
			final String[] targetServersArray) {
		StringBuilder targetServersSb = new StringBuilder();
		Properties remoteProperties = SshUtils.getDefaultSshPorperties();
		remoteProperties.put(SshParams.SOURCE_FILE, localFilePath);
		remoteProperties.put(SshParams.DESTINATION_FILE, remoteFilePath);
		for (String applicationServer : targetServersArray) {
			remoteProperties.put(SshParams.SERVER, applicationServer);
			if (!SystemUtils.isCurrentHost(applicationServer)) {
				JschUtils.scpTo(remoteProperties);
			} else {
				if (!PathUtils.arePathsEqual(localFilePath, remoteFilePath)) {
					String[] command = { UnixParameters.UNIX_COPY_COMMAND, localFilePath, remoteFilePath };
					logger.info(PrintUtils.getSeparatorLine(StringUtils.concat("running ",
							ArrayUtils.getAsDelimitedString(command, OzConstants.BLANK), " locally"), 2, 0));
					SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(command);
					logger.info(scer.getExecutorResponse());
				}
			}
			if (targetServersSb.length() > 0) {
				targetServersSb.append(OzConstants.COMMA);
			}
			targetServersSb.append(applicationServer);
		}
		logger.info(PrintUtils.getSeparatorLine(StringUtils.concat(localFilePath, " has been copied to ",
				remoteFilePath, " on servers: ", targetServersSb.toString()), 2, 0));
	}

	public static Properties getDeploymentManagerProperties(final String system) {
		String deploymentManagerPropertiesFilePath = PathUtils.getFullPath(AutoDeployGlobalParameters.getSystemsRoot(),
				system);
		deploymentManagerPropertiesFilePath = PathUtils.getFullPath(deploymentManagerPropertiesFilePath,
				DeploymentManagerProperties.DEPLOYMENT_MANAGER_PROPERTIES_FILE_NAME);
		Properties deploymentManagerProperties = PropertiesUtils
				.loadPropertiesFile(deploymentManagerPropertiesFilePath);
		String defaultJythonScriptPath = deploymentManagerProperties
				.getProperty(DeploymentManagerProperties.DEFAULT_JYTHON_SCRIPT_PATH);
		String defaultJythonScriptFullPath = PathUtils.getFullPath(deploymentManagerPropertiesFilePath,
				defaultJythonScriptPath);
		defaultJythonScriptFullPath = PathUtils.getCanonicalPath(defaultJythonScriptFullPath);
		deploymentManagerProperties.setProperty(DeploymentManagerProperties.DEFAULT_JYTHON_SCRIPT_FULL_PATH,
				defaultJythonScriptFullPath);
		return deploymentManagerProperties;
	}

	public static String getSystemRootFilePath(final String system) {
		String systemRootFilePath = PathUtils.getFullPath(AutoDeployGlobalParameters.getSystemsRoot(), system);
		return systemRootFilePath;
	}
}