package oz.utils.autodeploy;

import java.util.Arrays;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.jmx.JmxUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.levels.SummaryLevel;
import oz.infra.nio.watcher.WatchDirService;
import oz.infra.nio.watcher.WatchDirServiceParameters;
import oz.infra.operaion.Outcome;
import oz.infra.print.PrintUtils;
import oz.infra.singleinstance.SingleInstanceUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.utils.autodeploy.common.AutoDeployGlobalParameters;
import oz.utils.autodeploy.ear.AutoDeployEarProcessor;
import oz.utils.autodeploy.ear.AutoDeployEarUtils;
import oz.utils.autodeploy.product.ProductDeploymentParameters;
import oz.utils.autodeploy.product.ProductDeploymentProcessor;
import oz.utils.autodeploy.snifit.AutoDeploySnifitProcessor;
import oz.utils.autodeploy.tasks.BackupConfigTask;
import oz.utils.autodeploy.tasks.SnifitTasks;
import oz.utils.autodeploy.tasks.WsadminTask;

public class AutoDeployMain {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		boolean append = true;
		JulUtils.addFileHandlerFromEnv(append);
		logger.info(SystemUtils.getRunInfo());
		logger.info("args: ".concat(ArrayUtils.getAsDelimitedString(args)));
		String[] autoDeployOperations = args[0].split(OzConstants.COMMA);
		String[] myargs = ArrayUtils.shift(args, 1);
		if (SystemUtils.validateClassPath()) {
			String autoDeployGlobalPropertiesFilePath = myargs[0];
			AutoDeployGlobalParameters.processParameters(autoDeployGlobalPropertiesFilePath);
			for (int i = 0; i < autoDeployOperations.length; i++) {
				AutoDeployOperations autoDeployOperation = AutoDeployOperations.valueOf(autoDeployOperations[i]);
				processDeploymentOeration(autoDeployOperation, myargs);
			}
		}
		logger.info(PrintUtils.getSeparatorLine("autodeploy processing has completed", 1, 1, OzConstants.ASTERISK));
	}

	private static void processDeploymentOeration(final AutoDeployOperations autoDeployOperation, final String[] myargs) {
		Outcome outcome = null;
		String portalDeploymentPropertiesFilePath = null;
		String folder2WatchPath = null;
		String system = null;
		String sepatorMessage = "starting " + autoDeployOperation.toString();
		logger.info(PrintUtils.getSeparatorLine(sepatorMessage, 2, 0, OzConstants.EQUAL_SIGN));
		String startMessage = "start " + autoDeployOperation.toString() + " processing with parameters "
				+ ArrayUtils.getAsDelimitedString(myargs, OzConstants.COMMA + OzConstants.BLANK);
		logger.info(startMessage);
		WatchDirServiceParameters watchDirServiceParameters = null;
		switch (autoDeployOperation) {
		case DEPLOYEAR:
			AutoDeployEarUtils.processEarsPatameter(myargs);
			break;
		case EARWATCHER:
			SingleInstanceUtils.confirmSingleInstanceRun(AutoDeployGlobalParameters.getSingleInstancePort());
			folder2WatchPath = myargs[1];
			watchDirServiceParameters = new WatchDirServiceParameters(folder2WatchPath);
			new WatchDirService(watchDirServiceParameters, new AutoDeployEarProcessor());
			break;
		case SNIFITWATCHER:
			SingleInstanceUtils.confirmSingleInstanceRun(AutoDeployGlobalParameters.getSingleInstancePort());
			folder2WatchPath = myargs[1];
			watchDirServiceParameters = new WatchDirServiceParameters(folder2WatchPath);
			new WatchDirService(watchDirServiceParameters, new AutoDeploySnifitProcessor());
			break;
		case DEPLOYPORTAL:
			int singleInstancePort = AutoDeployGlobalParameters.getSingleInstancePort();
			SingleInstanceUtils.confirmSingleInstanceRun(singleInstancePort);
			portalDeploymentPropertiesFilePath = myargs[1];
			ProductDeploymentParameters.processParameters(portalDeploymentPropertiesFilePath);
			String portalDeploymentFilePath = myargs[2];
			new ProductDeploymentProcessor().processFile(portalDeploymentFilePath);
			break;
		case PORTALWATCHER:
			portalDeploymentPropertiesFilePath = myargs[1];
			ProductDeploymentParameters.processParameters(portalDeploymentPropertiesFilePath);
			folder2WatchPath = myargs[2];
			watchDirServiceParameters = new WatchDirServiceParameters(folder2WatchPath);
			new WatchDirService(watchDirServiceParameters, new ProductDeploymentProcessor());
			break;
		case RIPPLESTARTCLUSTER:
			system = myargs[1];
			outcome = WsadminTask.runWsadminScript(system, null, Arrays.asList(WsadminActions.RIPPLESTARTCLUSTER.toString(), myargs[2]));
			break;
		case BACKUPCONFIG:
			system = myargs[1];
			String[] myargs2 = ArrayUtils.shift(myargs, 2);
			outcome = BackupConfigTask.runBackupConfig(system, Arrays.asList(myargs2));
			break;
		case COPYLOADRULES:
			system = myargs[1];
			String loadRulesFilePath = myargs[2];
			outcome = SnifitTasks.copyLoadRules(system, loadRulesFilePath);
			break;
		case MODIFYLOADRULESCFG:
			system = myargs[1];
			String appid = myargs[2];
			String version = myargs[3];
			SnifitTasks.modifyLoadRulesCfg(system, appid, version);
			break;
		case EXITWATCHER:
			String jmxServerHost = myargs[1];
			int port = Integer.parseInt(myargs[2]);
			logger.info(StringUtils.concat("about to perform exit. host: ", jmxServerHost, " port: ", String.valueOf(port), " ..."));
			JmxUtils.performExit(jmxServerHost, port);
			logger.info(StringUtils.concat("exit processing has completed. host: ", jmxServerHost, " port: ", String.valueOf(port), " ..."));
			break;
		}
		String outcomeString = "completed.";
		if (outcome != null) {
			if (outcome == Outcome.SUCCESS) {
				outcomeString = "completed successfully.";
			} else {
				outcomeString = "failed.";
			}
		}
		String completionMessage = autoDeployOperation.toString() + " processing has " + outcomeString;
		logger.info(PrintUtils.getSeparatorLine(completionMessage, 2, 0, OzConstants.EQUAL_SIGN));
		AutoDeployGlobalParameters.SUMMARY_LOGGER.log(SummaryLevel.SUMMARY, completionMessage);
	}
}