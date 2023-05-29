package oz.utils.autodeploy.product;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.levels.SummaryLevel;
import oz.infra.nio.NioUtils;
import oz.infra.nio.visitor.FileVisitorUtils;
import oz.infra.operaion.Outcome;
import oz.infra.print.PrintUtils;
import oz.infra.process.FileProcessor;
import oz.infra.properties.PropertiesUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.remote.RemoteUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.jsch.JschUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.infra.zip.ZipUtils;
import oz.utils.autodeploy.common.AutoDeployGlobalParameters;
import oz.utils.autodeploy.common.AutoDeployUtils;
import oz.utils.autodeploy.ear.AutoDeployEarProcessor;
import oz.utils.autodeploy.ear.AutoDeployEarUtils;

public class ProductDeploymentProcessor implements FileProcessor {
	private enum PortalDeploymentFoldersEnum {
		EARs, Portlets, Properties, Resources, shared, xmlAccess, Test;
	}

	private static String versionDate = null;
	private static String localFolder2ProcessPath = null;
	private static String localZipFile2ProcessPath = null;
	private static String productFolder = ProductDeploymentParameters.getProductFolder();
	private static final String PRODUCT_VERSION_FOLDER = ProductDeploymentParameters.getProductVersionFolder();
	private static final String PRODUCT_BACKUP_FOLDER = ProductDeploymentParameters.getProductBackupFolder();
	private static String remoteVersionFolder2ProcessPath = null;
	private static String timeStamp = DateTimeUtils.getMilliSecsTimeStamp();
	private static String remoteZipFilePath = null;
	private static final String SCRIPT_PREFIX_PATH = "/scripts/processFilesOnTargetServerPrefix.script";
	private static final String SCRIPT_SUFFIX_PATH = "/scripts/processFilesOnTargetServerSuffix.script";
	private static final String SCRIPT_PATH = "/scripts/processFilesOnTargetServer.script";
	private static final int RETENTION_DAYS = 90;
	private static final String INSTALL_WARS_XML_PATH_PLACE_HOLDER = "%installWarsXml%";
	private static final String TIME_STAMP_PLACE_HOLDER = "%timeStamp%";
	private static final String USER_PLACE_HOLDER = "%user%";
	private static final String PASSWORD_PLACE_HOLDER = "%password%";

	private static Logger logger = JulUtils.getLogger();

	private static String buildScriptPrefix() {
		String scriptPrefixString = FileUtils.readTextFileFromClassPath(SCRIPT_PREFIX_PATH);
		Map<String, String> variablesMap = new HashMap<String, String>();
		variablesMap.put("productVersionFolder", PRODUCT_VERSION_FOLDER);
		variablesMap.put("remoteVersionFolder2ProcessPath", remoteVersionFolder2ProcessPath);
		String backupFolderPath = StringUtils.concat(remoteVersionFolder2ProcessPath, OzConstants.UNDERSCORE,
				timeStamp);
		variablesMap.put("backupFolderPath", backupFolderPath);
		variablesMap.put("remoteZipFilePath", remoteZipFilePath);
		String scriptPrefixContents = StringUtils.substituteVariables(scriptPrefixString, variablesMap);
		return scriptPrefixContents;
	}

	private static String buildScriptSuffix() {
		String scriptPrefixString = FileUtils.readTextFileFromClassPath(SCRIPT_SUFFIX_PATH);
		Map<String, String> variablesMap = new HashMap<String, String>();
		variablesMap.put("productVersionFolder", PRODUCT_VERSION_FOLDER);
		variablesMap.put("productBackupFolder", PRODUCT_BACKUP_FOLDER);
		variablesMap.put("retentionDays", String.valueOf(RETENTION_DAYS));
		String scriptPrefixContents = StringUtils.substituteVariables(scriptPrefixString, variablesMap);
		return scriptPrefixContents;
	}

	static String get1TargetServerFoldersProcessorScript(final String folder) {
		String scriptContents = OzConstants.EMPTY_STRING;
		String localFolderPath = PathUtils.getFullPath(localFolder2ProcessPath, folder);
		List<File> fileList = new FileVisitorUtils().recursivelyGetFilesOnly(localFolderPath);
		if (fileList.size() > 0) {
			logger.info("generate script for ".concat(localFolderPath));
			timeStamp = DateTimeUtils.getMilliSecsTimeStamp();
			String sourceFolderPath = PathUtils.addUnixPaths(remoteVersionFolder2ProcessPath, folder);
			String targetFolderPath = PathUtils.addUnixPaths(productFolder, folder);
			String remoteBackupfileName = StringUtils.concat(folder, "_backup_", timeStamp, OzConstants.ZIP_SUFFIX);
			String remoteBackupFilePath = PathUtils.addUnixPaths(PRODUCT_BACKUP_FOLDER, remoteBackupfileName);
			String findFile = OzConstants.UNIX_TMP_FOLDER + OzConstants.UNIX_FILE_SEPARATOR + folder + OzConstants.DOT
					+ timeStamp;
			String sourceDiffFile = OzConstants.UNIX_TMP_FOLDER + OzConstants.UNIX_FILE_SEPARATOR + "diff_" + folder
					+ OzConstants.DOT + "source." + timeStamp;
			String targetDiffFile = OzConstants.UNIX_TMP_FOLDER + OzConstants.UNIX_FILE_SEPARATOR + "diff_" + folder
					+ OzConstants.DOT + "target." + timeStamp;
			String scriptString = FileUtils.readTextFileFromClassPath(SCRIPT_PATH);

			Map<String, String> variablesMap = new HashMap<String, String>();
			variablesMap.put("sourceFolderPath", sourceFolderPath);
			variablesMap.put("targetFolderPath", targetFolderPath);
			variablesMap.put("remoteBackupFilePath", remoteBackupFilePath);
			variablesMap.put("findFile", findFile);
			variablesMap.put("sourceDiffFile", sourceDiffFile);
			variablesMap.put("targetDiffFile", targetDiffFile);
			variablesMap.put("folder", folder);
			scriptContents = StringUtils.substituteVariables(scriptString, variablesMap);
			logger.finest("\n+++++++ script contents +++++++++++++++++++++++++\n" + scriptContents);
		} else {
			logger.info(localFolderPath + " is empty. No script generated.");
		}
		logger.finest("\n\n\n\n++++++++++++++++++++++++++++++++\n\n\n " + scriptContents);
		// FileUtils.writeFile("c:/temp/" + DateTimeUtils.getTimeStamp(),
		// scriptContents);
		return scriptContents;
	}

	private static Properties getRemoteProperties() {
		String remotePropertiesFilePath = ProductDeploymentParameters.getRemotePropertiesFilePath();
		Properties remoteProperties = PropertiesUtils.loadPropertiesFile(remotePropertiesFilePath);
		return remoteProperties;
	}

	private static void process1ProductRemoteFolder() {
		String lineSeparator = OzConstants.UNIX_LINE_SEPARATOR;
		StringBuilder sb = new StringBuilder(buildScriptPrefix());
		for (String matafPortalFolder1 : ProductDeploymentParameters.getProductFoldersArray()) {
			String script1 = get1TargetServerFoldersProcessorScript(matafPortalFolder1);
			sb.append(lineSeparator + script1);
		}
		sb.append(lineSeparator);
		sb.append(buildScriptSuffix());
		String scriptContents = sb.toString() + lineSeparator;
		scriptContents = StringUtils.winEol2UnixEol(scriptContents);
		logger.finest(SystemUtils.LINE_SEPARATOR + "script contents:" + SystemUtils.LINE_SEPARATOR + scriptContents);
		String scriptFilePath4Debug = SystemPropertiesUtils.getTempDir() + "debug_" + timeStamp + OzConstants.SH_SUFFIX;
		FileUtils.writeFile(scriptFilePath4Debug, scriptContents);
		runRemoteCommandOnTargetServers(scriptContents);
	}

	private static void processEarFiles() {
		String localEarFilesPath = PathUtils.getFullPath(localFolder2ProcessPath,
				PortalDeploymentFoldersEnum.EARs.toString());
		logger.info(PrintUtils.getSeparatorLine("processing ear files from ".concat(localEarFilesPath), 2, 0));
		if (!FolderUtils.isEmpty(localEarFilesPath, RegexpUtils.REGEXP_EAR_FILE)) {
			SystemPropertiesUtils.setSystemProperty(AutoDeployEarProcessor.AUTODEPLOY_SYSTEM,
					ProductDeploymentParameters.getSystem());
			String[] earDeplymentPatameter = { null, localEarFilesPath };
			AutoDeployEarUtils.processEarsPatameter(earDeplymentPatameter);
		}
	}

	private static void processPortLets() {
		String localPortlertSFolderPath = PathUtils.getFullPath(localFolder2ProcessPath,
				PortalDeploymentFoldersEnum.Portlets.toString());
		logger.info(PrintUtils.getSeparatorLine("starting portlets processing from ".concat(localPortlertSFolderPath),
				2, 0));
		if (!FolderUtils.isEmpty(localPortlertSFolderPath, RegexpUtils.REGEXP_WAR_FILE)) {
			String partialPath = PathUtils.addUnixPaths(PortalDeploymentFoldersEnum.Portlets.toString(),
					ProductDeploymentParameters.getInstallWarsXmlFileName());
			String installWarsPath = PathUtils.addUnixPaths(remoteVersionFolder2ProcessPath, partialPath);
			String encryptedUser = ProductDeploymentParameters.getXmlAccessUser();
			String encryptedPassword = ProductDeploymentParameters.getXmlAccessPassword();
			String user = CryptographyUtils.decryptString(encryptedUser, EncryptionMethodEnum.PLAIN);
			String password = CryptographyUtils.decryptString(encryptedPassword, EncryptionMethodEnum.PLAIN);
			String xmlAccessCommand = ProductDeploymentParameters.getXmlAccessCommand();
			xmlAccessCommand = xmlAccessCommand.replace(INSTALL_WARS_XML_PATH_PLACE_HOLDER, installWarsPath);
			xmlAccessCommand = xmlAccessCommand.replace(TIME_STAMP_PLACE_HOLDER, timeStamp);
			xmlAccessCommand = xmlAccessCommand.replace(USER_PLACE_HOLDER, user);
			xmlAccessCommand = xmlAccessCommand.replace(PASSWORD_PLACE_HOLDER, password);
			String[] applicationServersArray = ProductDeploymentParameters.getApplicationServersArray();
			Properties remoteProperties = getRemoteProperties();
			logger.info("xmlAccessCommand" + xmlAccessCommand);
			remoteProperties.put(SshParams.COMMAND_LINE, xmlAccessCommand);
			remoteProperties.put(SshParams.SERVER, applicationServersArray[0]);
			SystemCommandExecutorResponse scer = JschUtils.exec(remoteProperties, Level.INFO);
			String completionMessage = "portlets processing has completed. Return code: "
					+ String.valueOf(scer.getReturnCode()) + OzConstants.DOT;
			logger.info(PrintUtils.getSeparatorLine(completionMessage, 2, 0));
			AutoDeployGlobalParameters.SUMMARY_LOGGER.log(SummaryLevel.SUMMARY, completionMessage);
		}
	}

	private static void runRemoteCommandOnTargetServers(final String commandLine) {
		Properties remoteProperties = getRemoteProperties();
		String[] targetServersArray = ProductDeploymentParameters.getApplicationServersArray();
		String summaryMessage = RemoteUtils.runRemoteCommandOnTargetServers(commandLine, remoteProperties,
				targetServersArray);
		AutoDeployGlobalParameters.SUMMARY_LOGGER.log(SummaryLevel.SUMMARY, summaryMessage);
	}

	static void setupDeploymentZipAndFolder(final String productDeploymentFilesPathParameter) {
		String productDeploymentFilesPath = PathUtils.getCanonicalPath(productDeploymentFilesPathParameter);
		String startMessage = PrintUtils.getSeparatorLine(
				StringUtils.concat("starting setupProductDeploymentFiles with parameter ", productDeploymentFilesPath));
		logger.info(OzConstants.LINE_FEED + (startMessage.concat(startMessage)).concat(startMessage)
				+ OzConstants.LINE_FEED);
		File productDeploymentFilesFile = new File(productDeploymentFilesPath);
		if (productDeploymentFilesFile.isDirectory()) {
			String productDeploymentFolderPath = productDeploymentFilesFile.getAbsolutePath();
			File productDeploymentFolderFile = new File(productDeploymentFolderPath);
			versionDate = productDeploymentFolderFile.getName();
			productDeploymentFilesPath = PathUtils.getFullPath(productDeploymentFolderFile.getParent(),
					versionDate.concat(OzConstants.ZIP_SUFFIX));
			Outcome outcome = ZipUtils.zipFolder(productDeploymentFilesPath, productDeploymentFilesFile,
					productDeploymentFolderFile.getParent());
			productDeploymentFilesFile = new File(productDeploymentFilesPath);
			if (!outcome.equals(Outcome.SUCCESS)) {
				logger.warning(StringUtils.concat("failed to zip  inputfolder", productDeploymentFolderPath,
						"\nProcessing has been terminated."));
				System.exit(OzConstants.EXIT_STATUS_ABNORMAL);
			}
		}
		if (productDeploymentFilesFile.isFile()) {
			String fileExtension = PathUtils.getFileExtension(productDeploymentFilesFile);
			if (fileExtension.equals(OzConstants.ZIP_SUFFIX.substring(1))) {
				String zipFileNameAndExtnsion = PathUtils.getFileNameAndExtension(productDeploymentFilesPath);
				String productVersionFolderZipFilePath = PathUtils.getFullPath(PRODUCT_VERSION_FOLDER,
						zipFileNameAndExtnsion);
				if (!PathUtils.arePathsEqual(productDeploymentFilesPath, productVersionFolderZipFilePath)) {
					NioUtils.copyAndSaveExisting(productDeploymentFilesPath, productVersionFolderZipFilePath);
				}
				versionDate = ZipUtils.getZipRootFolder(productDeploymentFilesFile);
				String productVersionCurrentFolder = PathUtils.getFullPath(PRODUCT_VERSION_FOLDER, versionDate);
				File productVersionCurrentFolderFile = new File(productVersionCurrentFolder);
				if (productVersionCurrentFolderFile.exists()) {
					String backupFolder = StringUtils.concat(productVersionCurrentFolder, OzConstants.UNDERSCORE,
							timeStamp);
					NioUtils.move(productVersionCurrentFolder, backupFolder, true);
				}
				ZipUtils.extractAllFiles(productVersionFolderZipFilePath, PRODUCT_VERSION_FOLDER);
				localZipFile2ProcessPath = productVersionFolderZipFilePath;
				localFolder2ProcessPath = productVersionCurrentFolder;
			} else {
				SystemUtils.printMessageAndExit("zip file expected. Processing has been aborted.",
						OzConstants.EXIT_STATUS_ABNORMAL);
			}
		} else {
			SystemUtils.printMessageAndExit(
					productDeploymentFilesPath + " does not exist !. Proccessing has been aborted",
					OzConstants.EXIT_STATUS_ABNORMAL, false);
		}

		logger.info(PrintUtils.getSeparatorLine(StringUtils.concat("productDeploymentFilesPath: ",
				productDeploymentFilesPathParameter, " processing deployment folder: ", localFolder2ProcessPath,
				"    zip file path: ", localZipFile2ProcessPath), 2, 0));
	}

	@Override
	public Outcome processFile(final String productDeploymentFilePath) {
		setupDeploymentZipAndFolder(productDeploymentFilePath);
		List<File> fileList = new FileVisitorUtils().recursivelyGetFilesOnly(localFolder2ProcessPath);
		String completionMessage = null;
		if (fileList.size() > 0) {
			String zipFileNameAndExtension = PathUtils.getFileNameAndExtension(localZipFile2ProcessPath);
			remoteZipFilePath = PathUtils.addUnixPaths(PRODUCT_VERSION_FOLDER, zipFileNameAndExtension);
			remoteVersionFolder2ProcessPath = PathUtils.addUnixPaths(PRODUCT_VERSION_FOLDER, versionDate);
			AutoDeployUtils.copyFile2RemoteServers(localZipFile2ProcessPath, remoteZipFilePath,
					ProductDeploymentParameters.getApplicationServersArray());
			process1ProductRemoteFolder();
			processEarFiles();
			processPortLets();
			completionMessage = StringUtils.concat("proccessing of ", productDeploymentFilePath, " has completed.");
			logger.info(completionMessage);
		} else {
			completionMessage = "No files to process. Program exits.";
			logger.info(completionMessage);
		}
		AutoDeployGlobalParameters.SUMMARY_LOGGER.log(SummaryLevel.SUMMARY, completionMessage);
		return null;
	}
}
