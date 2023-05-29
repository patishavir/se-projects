package oz.utils.was.autodeploy.portal;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.io.filefilter.FileFilterIsDirectory;
import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.NioUtils;
import oz.infra.nio.visitor.FileVisitorUtils;
import oz.infra.operaion.Outcome;
import oz.infra.print.PrintUtils;
import oz.infra.process.FileProcessor;
import oz.infra.properties.PropertiesUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.jsch.JschUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.zip.ZipUtils;
import oz.utils.was.autodeploy.AutoDeployEarProcessor;
import oz.utils.was.autodeploy.AutoDeployEarUtils;
import oz.utils.was.autodeploy.portal.parameters.PortalDeploymentParameters;

public class PortalDeploymentProcessor implements FileProcessor {
	private enum PortalDeploymentFoldersEnum {
		EARs, Portlets, Properties, Resources, shared, xmlAccess, Test;
	}

	private static String versionDate = null;
	private static String localFolder2ProcessPath = null;
	private static String localZipFile2ProcessPath = null;
	private static String remoteMatafPortalFolder = PortalDeploymentParameters.getMatafPortalFolder();
	private static String matafPortalVersionFolder = PortalDeploymentParameters.getMatafPortalVersionFolder();
	private static String remoteVersionFolder2ProcessPath = null;
	private static String timeStamp = DateTimeUtils.getTimeStamp();
	private static String remoteZipFilePath = null;
	private static final String INSTALL_WARS_XML_PATH_PLACE_HOLDER = "%installWarsXml%";
	private static final String TIME_STAMP_PLACE_HOLDER = "%timeStamp%";
	private static final String USER_PLACE_HOLDER = "%user%";
	private static final String PASSWORD_PLACE_HOLDER = "%password%";
	private static final String COPY_COMMAND = "/usr/bin/cp";
	private static final FileFilterIsDirectory fileFilterIsDirectory = new FileFilterIsDirectory();
	private static Logger logger = JulUtils.getLogger();

	private static String buildScriptFile(final String scriptContents) {
		String scriptName = StringUtils.concat("portal_deployment_", timeStamp, OzConstants.SH_SUFFIX);
		String localSdcriptPath = StringUtils
				.concat(SystemUtils.getSystemProperty(SystemUtils.SYSTEM_PROPERTY_JAVA_IO_TMPDIR), scriptName);
		logger.info("localSdcriptPath: ".concat(localSdcriptPath));
		NioUtils.writeString2File(localSdcriptPath, scriptContents);
		Path localPath = Paths.get(localSdcriptPath);
		try {
			Files.setPosixFilePermissions(localPath, NioUtils.getPosixFilePermissionSet("-rwx------"));
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return localSdcriptPath;
	}

	private static void copyFile2RemoteServers(final String localFilePath, final String remoteFilePath) {
		StringBuilder targetServersSb = new StringBuilder();
		Properties remoteProperties = getRemoteProperties();
		remoteProperties.put(SshParams.SOURCE_FILE, localFilePath);
		remoteProperties.put(SshParams.DESTINATION_FILE, remoteFilePath);
		String[] applicationServersArray = PortalDeploymentParameters.getApplicationServersArray();
		String hostname = SystemUtils.getHostname();
		for (String applicationServer : applicationServersArray) {
			remoteProperties.put(SshParams.SERVER, applicationServer);
			if (!hostname.equals(applicationServer)) {
				JschUtils.scpTo(remoteProperties);
			} else {
				if (!PathUtils.arePathsEqual(localFilePath, remoteFilePath)) {
					String[] command = { COPY_COMMAND, localFilePath, remoteFilePath };
					logger.info(
							PrintUtils.getSeparatorLine(
									StringUtils.concat("running ",
											ArrayUtils.getAsDelimitedString(command, OzConstants.BLANK), " locally"),
									2, 0));
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

	private static String get1ServerMataPortalFoldersScript(final String folder, final String separator) {
		String scriptContents = OzConstants.EMPTY_STRING;
		String localFolderPath = PathUtils.getFullPath(localFolder2ProcessPath, folder);
		logger.info(PrintUtils.getSeparatorLine("generate script for ".concat(localFolderPath), 2, 0));
		List<File> fileList = new FileVisitorUtils().recursivelyGetFilesOnly(localFolderPath);
		if (fileList.size() > 0) {
			String sourceFolderPath = PathUtils.addUnixPaths(remoteVersionFolder2ProcessPath, folder);
			String targetFolderPath = PathUtils.addUnixPaths(remoteMatafPortalFolder, folder);
			String remoteBackupfileName = StringUtils.concat(folder, "_backup_", timeStamp, OzConstants.ZIP_SUFFIX);
			String remoteBackupFilePath = PathUtils
					.addUnixPaths(PortalDeploymentParameters.getMatafPortalBackupFolder(), remoteBackupfileName);
			String mkdirCommand = StringUtils.concat("if [[ ! -d ", targetFolderPath, " ]] ; then \t mkdir -p ",
					targetFolderPath, separator, "fi", separator);
			String cd2Command = StringUtils.concat("cd ", targetFolderPath, separator);
			String ifCommand = StringUtils.concat("if [[ -d ", sourceFolderPath, " ]] ; then ");
			// String zipCommand = StringUtils.concat(OzConstants.TAB, "zip -rq
			// ",
			// remoteBackupFilePath, " *", separator);
			String zipCommand = StringUtils.concat(OzConstants.TAB, "find ", targetFolderPath,
					" | grep -v Temp | xargs zip -q ", remoteBackupFilePath, separator);
			String zipEchoCommand = StringUtils.concat(OzConstants.TAB, "echo folder ", targetFolderPath,
					" has been backed up to ", remoteBackupFilePath, separator);
			String cpCommand = StringUtils.concat(OzConstants.TAB, "/usr/bin/cp -pr ", sourceFolderPath, "/* ",
					targetFolderPath, separator);
			String echoCommand = StringUtils.concat(OzConstants.TAB, "echo copy of  ", sourceFolderPath, "/* ", " to ",
					targetFolderPath, " has completed", separator);
			String lsCommand = StringUtils.concat(OzConstants.TAB, "/usr/bin/find ", targetFolderPath,
					" -mtime -30 -type f | xargs ls -l", separator);
			String fiCommand = "fi";
			scriptContents = StringUtils.concat(mkdirCommand, cd2Command, ifCommand, zipCommand, zipEchoCommand,
					cpCommand, echoCommand, lsCommand, fiCommand, separator);
		}
		return scriptContents;
	}

	private static Properties getRemoteProperties() {
		String remotePropertiesFilePath = PortalDeploymentParameters.getRemotePropertiesFilePath();
		Properties remoteProperties = PropertiesUtils.loadPropertiesFile(remotePropertiesFilePath);
		return remoteProperties;
	}

	private static void process1MataPortalRemoteFolder() {
		String separator = OzConstants.UNIX_LINE_SEPARATOR;
		String cdCommand = StringUtils.concat("cd ", matafPortalVersionFolder, separator);
		String backupFolderPath = StringUtils.concat(remoteVersionFolder2ProcessPath, OzConstants.UNDERSCORE,
				timeStamp);
		String ifcommand = StringUtils.concat("if [[ -d ", remoteVersionFolder2ProcessPath, " ]] ; then mv ",
				remoteVersionFolder2ProcessPath, OzConstants.BLANK, backupFolderPath, separator, " fi", separator);
		String unzipCommand = StringUtils.concat("unzip ", remoteZipFilePath, separator);
		StringBuilder sb = new StringBuilder(StringUtils.concat(cdCommand, ifcommand, unzipCommand));
		for (String matafPortalFolder1 : PortalDeploymentParameters.getMatafPortalFoldersArray()) {
			String script1 = get1ServerMataPortalFoldersScript(matafPortalFolder1, separator);
			sb.append(script1);
		}
		String scriptContents = sb.toString();
		logger.finest(scriptContents);
		runRemoteCommand(scriptContents);
	}

	private static void processEarFiles() {
		String localEarFilesPath = PathUtils.getFullPath(localFolder2ProcessPath,
				PortalDeploymentFoldersEnum.EARs.toString());
		logger.info(PrintUtils.getSeparatorLine("processing ear files from ".concat(localEarFilesPath), 2, 0));
		if (!FolderUtils.isEmpty(localEarFilesPath, RegexpUtils.REGEXP_EAR_FILE)) {
			SystemUtils.setSystemProperty(AutoDeployEarProcessor.AUTODEPLOY_SYSTEM,
					PortalDeploymentParameters.getSystem());
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
					PortalDeploymentParameters.getInstallWarsXmlFileName());
			String installWarsPath = PathUtils.addUnixPaths(remoteVersionFolder2ProcessPath, partialPath);
			String encryptedUser = PortalDeploymentParameters.getXmlAccessUser();
			String encryptedPassword = PortalDeploymentParameters.getXmlAccessPassword();
			String user = CryptographyUtils.decryptString(encryptedUser, EncryptionMethodEnum.PLAIN);
			String password = CryptographyUtils.decryptString(encryptedPassword, EncryptionMethodEnum.PLAIN);
			String xmlAccessCommand = PortalDeploymentParameters.getXmlAccessCommand();
			xmlAccessCommand = xmlAccessCommand.replace(INSTALL_WARS_XML_PATH_PLACE_HOLDER, installWarsPath);
			xmlAccessCommand = xmlAccessCommand.replace(TIME_STAMP_PLACE_HOLDER, timeStamp);
			xmlAccessCommand = xmlAccessCommand.replace(USER_PLACE_HOLDER, user);
			xmlAccessCommand = xmlAccessCommand.replace(PASSWORD_PLACE_HOLDER, password);
			String[] applicationServersArray = PortalDeploymentParameters.getApplicationServersArray();
			Properties remoteProperties = getRemoteProperties();
			logger.info("xmlAccessCommand" + xmlAccessCommand);
			remoteProperties.put(SshParams.COMMAND_LINE, xmlAccessCommand);
			remoteProperties.put(SshParams.SERVER, applicationServersArray[0]);
			JschUtils.exec(remoteProperties, Level.INFO);
			logger.info(PrintUtils.getSeparatorLine("portlets processing has completed", 2, 0));
		}
	}

	private static void runRemoteCommand(final String commandLine) {
		String hostName = SystemUtils.getHostname();
		Properties remoteProperties = getRemoteProperties();
		String[] applicationServersArray = PortalDeploymentParameters.getApplicationServersArray();
		remoteProperties.put(SshParams.COMMAND_LINE, commandLine);
		StringBuilder targetApplicationServersSb = new StringBuilder();
		logger.info("commandLine: ".concat(commandLine));
		for (String targetApplicationServer : applicationServersArray) {
			logger.info(PrintUtils.getSeparatorLine(StringUtils.concat("running commnad on hostName: ", hostName,
					" targetServer: ", targetApplicationServer), 2, 0));
			if (!hostName.equals(targetApplicationServer)) {
				remoteProperties.put(SshParams.SERVER, targetApplicationServer);
				JschUtils.exec(remoteProperties, Level.INFO);
			} else {
				File scriptFile = new File(commandLine);
				String script2Run = commandLine;
				if (!scriptFile.isFile()) {
					script2Run = buildScriptFile(commandLine);
				}
				SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(script2Run);
				logger.info(scer.getExecutorResponse());
				NioUtils.setPosixFilePermissions(script2Run, "-rw-------");
			}
			if (targetApplicationServersSb.length() > 0) {
				targetApplicationServersSb.append(OzConstants.COMMA);
			}
			targetApplicationServersSb.append(targetApplicationServer);
		}
		logger.finest(PrintUtils.getSeparatorLine(StringUtils.concat(commandLine, " has run on ",
				targetApplicationServersSb.toString(), " hostname: ", hostName), 2, 0));
	}

	private static void setupDeploymentZipAndFolder(final String portalDeploymentFilesPathParameter) {
		String portalDeploymentFilesPath = PathUtils.getCanonicalPath(portalDeploymentFilesPathParameter);
		logger.info(PrintUtils.getSeparatorLine(
				StringUtils.concat("starting setupPortalDeploymentFiles with parameter ", portalDeploymentFilesPath), 2,
				0));
		File portalDeploymentFilesFile = new File(portalDeploymentFilesPath);
		if (portalDeploymentFilesFile.isDirectory()) {
			String portalDeploymentFolderPath = portalDeploymentFilesFile.getAbsolutePath();
			File portalDeploymentFolderFile = new File(portalDeploymentFolderPath);
			versionDate = portalDeploymentFolderFile.getName();
			portalDeploymentFilesPath = PathUtils.getFullPath(portalDeploymentFolderFile.getParent(),
					versionDate.concat(OzConstants.ZIP_SUFFIX));
			Outcome outcome = ZipUtils.zipFolder(portalDeploymentFilesPath, portalDeploymentFilesFile,
					portalDeploymentFolderFile.getParent());
			portalDeploymentFilesFile = new File(portalDeploymentFilesPath);
			if (!outcome.equals(Outcome.SUCCESS)) {
				logger.warning(StringUtils.concat("failed to zip  inputfolder", portalDeploymentFolderPath,
						"\nProcessing has been terminated."));
				System.exit(OzConstants.EXIT_STATUS_ABNORMAL);
			}
		}
		if (portalDeploymentFilesFile.isFile()) {
			String fileExtension = PathUtils.getFileExtension(portalDeploymentFilesFile);
			if (fileExtension.equals(OzConstants.ZIP_SUFFIX.substring(1))) {
				String zipFileNameAndExtnsion = PathUtils.getFileNameAndExtension(portalDeploymentFilesPath);
				String matafPortalVersionFolderZipFilePath = PathUtils.getFullPath(matafPortalVersionFolder,
						zipFileNameAndExtnsion);
				if (!PathUtils.arePathsEqual(portalDeploymentFilesPath, matafPortalVersionFolderZipFilePath)) {
					NioUtils.copyAndSaveExisting(portalDeploymentFilesPath, matafPortalVersionFolderZipFilePath);
				}
				versionDate = ZipUtils.getZipRootFolder(portalDeploymentFilesFile);
				String matafPortalVersionCurrentFolder = PathUtils.getFullPath(matafPortalVersionFolder, versionDate);
				File matafPortalVersionCurrentFolderFile = new File(matafPortalVersionCurrentFolder);
				if (matafPortalVersionCurrentFolderFile.exists()) {
					String backupFolder = StringUtils.concat(matafPortalVersionCurrentFolder, OzConstants.UNDERSCORE,
							timeStamp);
					NioUtils.move(matafPortalVersionCurrentFolder, backupFolder, true);
				}
				ZipUtils.extractAllFiles(matafPortalVersionFolderZipFilePath, matafPortalVersionFolder);
				localZipFile2ProcessPath = matafPortalVersionFolderZipFilePath;
				localFolder2ProcessPath = matafPortalVersionCurrentFolder;
			} else {
				SystemUtils.printMessageAndExit("zip file expected. Processing has been aborted.",
						OzConstants.EXIT_STATUS_ABNORMAL);
			}
		} else {
			SystemUtils.printMessageAndExit(
					portalDeploymentFilesPath + " does not exist !. Proccessing has been aborted",
					OzConstants.EXIT_STATUS_ABNORMAL, false);
		}

		logger.info(PrintUtils.getSeparatorLine(StringUtils.concat("portalDeploymentFilesPath: ",
				portalDeploymentFilesPathParameter, " processing deployment folder: ", localFolder2ProcessPath,
				"    zip file path: ", localZipFile2ProcessPath), 2, 0));
	}

	@Override
	public Outcome processFile(final String portalLocalDeploymentFilePath) {
		setupDeploymentZipAndFolder(portalLocalDeploymentFilePath);
		List<File> fileList = new FileVisitorUtils().recursivelyGetFilesOnly(localFolder2ProcessPath);
		if (fileList.size() > 0) {
			String zipFileNameAndExtension = PathUtils.getFileNameAndExtension(localZipFile2ProcessPath);
			remoteZipFilePath = PathUtils.addUnixPaths(matafPortalVersionFolder, zipFileNameAndExtension);
			remoteVersionFolder2ProcessPath = PathUtils.addUnixPaths(matafPortalVersionFolder, versionDate);
			copyFile2RemoteServers(localZipFile2ProcessPath, remoteZipFilePath);
			process1MataPortalRemoteFolder();
			processEarFiles();
			processPortLets();
			logger.info(StringUtils.concat("proccessing of ", portalLocalDeploymentFilePath, " has completed."));
		} else {
			logger.info("No files to process. Program exits.");
		}
		return null;
	}
}
