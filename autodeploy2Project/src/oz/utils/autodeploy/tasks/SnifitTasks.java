package oz.utils.autodeploy.tasks;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.map.MapUtils;
import oz.infra.operaion.Outcome;
import oz.infra.properties.PropertiesUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.remote.RemoteOrLocalUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.SshUtils;
import oz.infra.ssh.jsch.JschUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.utils.autodeploy.common.AutoDeployGlobalParameters;
import oz.utils.autodeploy.common.AutoDeployUtils;

public class SnifitTasks {
	private static final String LOAD_RULES_FOLDER_NAME = "loadRulesFolderName";
	private static final String LOAD_RULES_PROPERTIES_FILE_NAME = "loadRulesPropertiesFileName";
	private static final String CFG_FOLDER = "cfgFolder";
	private static final String CFG_BACKUP_FOLDER = "cfgBackupFolder";
	private static final String CFG_FILE_NAME_PREFIX = "cfgFileNamePrefix";
	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());

	public static Outcome copyLoadRules(final String system, final String loadRulesfilePath) {
		Properties loadRulesRemoteProperties = getLoadRulesRemoteProperties(system);
		SshUtils.decryptPassword(loadRulesRemoteProperties);
		File loadRulesfile = new File (loadRulesfilePath);
		String loadRulesfilePath1 = loadRulesfilePath;
		if ( loadRulesfile.isDirectory()) {
			loadRulesfilePath1 =FolderUtils.getSingleFilePathInFolder(loadRulesfilePath, RegexpUtils.REGEXP_ZIP_FILE);
		}
		loadRulesRemoteProperties.setProperty(SshParams.SOURCE_FILE, PathUtils.getCanonicalPath(loadRulesfilePath1));
		int retcode = JschUtils.scpTo(loadRulesRemoteProperties);
		Outcome outcome = Outcome.FAILURE;
		if (retcode == 0) {
			outcome = Outcome.SUCCESS;
		}
		return outcome;
	}

	private static Properties getLoadRulesRemoteProperties(final String system) {
		String systemRootPath = AutoDeployUtils.getSystemRootFilePath(system);
		Properties sniiftDeploymentProperties = getSniiftDeploymentPropertiess(system);
		String loadRulesPropertiesFolderName = sniiftDeploymentProperties.getProperty(LOAD_RULES_FOLDER_NAME);
		String loadRulesFolderPath = PathUtils.getFullPath(systemRootPath, loadRulesPropertiesFolderName);
		String remotePropertiesFilePath = PathUtils.getFullPath(loadRulesFolderPath,
				sniiftDeploymentProperties.getProperty(LOAD_RULES_PROPERTIES_FILE_NAME));
		Properties loadRulesRemoteProperties = PropertiesUtils.loadPropertiesFile(remotePropertiesFilePath);
		return loadRulesRemoteProperties;
	}

	private static Properties getSniiftDeploymentPropertiess(final String system) {
		String systemRootPath = AutoDeployUtils.getSystemRootFilePath(system);
		String snifitDeploymentPropertiesFileName = AutoDeployGlobalParameters.getSnifitDeploymentPropertiesFileName();
		String snifitDeploymentPropertiesFilePath = PathUtils.getFullPath(systemRootPath,
				snifitDeploymentPropertiesFileName);
		Properties sniiftDeploymentProperties = PropertiesUtils.loadPropertiesFile(snifitDeploymentPropertiesFilePath);
		return sniiftDeploymentProperties;
	}

	public static Outcome modifyLoadRulesCfg(final String system, final String appId, final String version) {
		Properties loadRulesRemoteProperties = getLoadRulesRemoteProperties(system);
		String cfgFolder = loadRulesRemoteProperties.getProperty(CFG_FOLDER);
		String cfgBackupFolder = loadRulesRemoteProperties.getProperty(CFG_BACKUP_FOLDER);
		String cfgFileNamePrefix = loadRulesRemoteProperties.getProperty(CFG_FILE_NAME_PREFIX);
		String cfgFileName = cfgFileNamePrefix + appId + OzConstants.XML_SUFFIX;
		String cfgFilePath = cfgFolder + OzConstants.UNIX_FILE_SEPARATOR + cfgFileName;
		String timeStamp = DateTimeUtils.getMilliSecsTimeStamp();
		String cfgBackupFilePath = cfgBackupFolder + OzConstants.UNIX_FILE_SEPARATOR + cfgFileName + OzConstants.DOT
				+ timeStamp;
		String separator = OzConstants.UNIX_LINE_SEPARATOR;
		String[] scriptArray = { "cd %cfgFolder% %separator%", "cp -p  %cfgFilePath% %cfgBackupFilePath% %separator%",
				"sed  's!<version>[6,7,8,9]\\.[0-9][0-9]\\.[0-9][0-9]<\\/version>!<version>%version%</version>!' %cfgFilePath% > %cfgBackupFilePath%.sed %separator%",
				"/usr/bin/cp -p   %cfgBackupFilePath%.sed %cfgFilePath%%separator%",
				"/usr/bin/echo comparing %cfgFilePath%  %cfgBackupFilePath% %separator%",
				"/usr/bin/diff  %cfgFilePath%  %cfgBackupFilePath% %separator%",
				"/usr/bin/stopsrc -s dbload.%appId% %separator%", "/usr/bin/sleep 1 %separator%",
				"/usr/bin/startsrc -s dbload.%appId%" };
		//
		Map<String, String> variablesMap = new HashMap<String, String>();
		variablesMap.put("cfgFolder", cfgFolder);
		variablesMap.put("cfgFilePath", cfgFilePath);
		variablesMap.put("cfgBackupFilePath", cfgBackupFilePath);
		variablesMap.put("appId", appId);
		variablesMap.put("version", version);
		variablesMap.put("separator", separator);
		logger.info(MapUtils.printMap(variablesMap));
		//
		String[] finalArray = StringUtils.substituteVariables(scriptArray, variablesMap);
		String scriptContents = ArrayUtils.getString(finalArray, OzConstants.EMPTY_STRING);
		loadRulesRemoteProperties.setProperty(SshParams.COMMAND_LINE, scriptContents);
		logger.info(PropertiesUtils.getAsDelimitedString(loadRulesRemoteProperties, SystemUtils.LINE_SEPARATOR));
		SystemCommandExecutorResponse scer = RemoteOrLocalUtils.exec(loadRulesRemoteProperties);
		Outcome outcome = Outcome.FAILURE;
		if (scer.getReturnCode() == 0) {
			outcome = Outcome.SUCCESS;
		}
		return outcome;
	}
}
