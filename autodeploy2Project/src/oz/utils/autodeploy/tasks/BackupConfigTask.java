package oz.utils.autodeploy.tasks;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.logging.jul.levels.SummaryLevel;
import oz.infra.operaion.Outcome;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.jsch.JschUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.utils.autodeploy.DeploymentManagerProperties;
import oz.utils.autodeploy.common.AutoDeployGlobalParameters;
import oz.utils.autodeploy.common.AutoDeployUtils;
import oz.utils.autodeploy.tasks.WsadminTask.UserPasswordLocation;

public class BackupConfigTask {
	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());

	public static Outcome runBackupConfig(final String system, final List<String> parameters) {
		Outcome outcome = Outcome.FAILURE;
		StopWatch stopWatch = new StopWatch();
		Properties deploymentManagerProperties = AutoDeployUtils.getDeploymentManagerProperties(system);
		SystemCommandExecutorResponse scer = null;
		String myHostName = SystemUtils.getHostname();
		String deploymentManager = deploymentManagerProperties.getProperty(SshParams.SERVER).trim();
		logger.finest(StringUtils.concat("host name: ", myHostName, " length: ", String.valueOf(myHostName.length()),
				" server: ", deploymentManager, " length: ", String.valueOf(deploymentManager.length()), " equal: ",
				String.valueOf(myHostName.equalsIgnoreCase(deploymentManager))));
		boolean runLocally = myHostName.equalsIgnoreCase(deploymentManager);
		String commandLine = deploymentManagerProperties.getProperty(DeploymentManagerProperties.BACKUPCONFIG_PATH);
		List<String> parametersList = WsadminTask.getCommandLine(commandLine, deploymentManagerProperties, parameters,
				UserPasswordLocation.USER_PASSWORD_AFTER_PARAMS);
		if (runLocally) {
			scer = SystemCommandExecutorRunner.run(parametersList);
		} else {
			commandLine = ListUtils.getAsDelimitedString(parametersList, OzConstants.BLANK);
			deploymentManagerProperties.put(SshParams.COMMAND_LINE, commandLine);
			scer = JschUtils.exec(deploymentManagerProperties);
		}
		if (scer != null) {
			String message = StringUtils.concat("backupConfig on ", deploymentManager, " has completed in ",
					stopWatch.getElapsedTimeString(), ". return code: ", String.valueOf(scer.getReturnCode()));
			if (scer.getReturnCode() == OzConstants.EXIT_STATUS_OK) {
				outcome = Outcome.SUCCESS;
			}
			logger.warning(scer.getExecutorResponse());
			logger.info(message);
			AutoDeployGlobalParameters.SUMMARY_LOGGER.log(SummaryLevel.SUMMARY, message);
		}
		return outcome;
	}
}
