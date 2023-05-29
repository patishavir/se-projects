package oz.utils.was.autodeploy.portal;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.io.filefilter.FileFilterIsDirectory;
import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.NioUtils;
import oz.infra.operaion.Outcome;
import oz.infra.process.FileProcessor;
import oz.infra.properties.PropertiesUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.ssh.SshParams;
import oz.infra.ssh.jsch.JschUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.infra.zip.ZipUtils;
import oz.utils.was.autodeploy.AutoDeployEarProcessor;
import oz.utils.was.autodeploy.AutoDeployEarUtils;
import oz.utils.was.autodeploy.parameters.AutoDeployGlobalParameters;
import oz.utils.was.autodeploy.portal.parameters.PortalDeploymentParameters;

public class PortalDeploymentProcessor implements FileProcessor {
	private enum PortalDeploymentFoldersEnum {
		EARs, Portlets, Properties, Resources, shared, xmlAccess, Test;
	}

	private static String versionDate = null;
	private static String portalLocalDeploymentZipFilePath = null;
	private static final String BACKUP_FOLDER_NAME = "bak";
	private static final String LOCAL_SCRIPTS_FOLDER_NAME = "scripts";
	private static final String LOCAL_ZIP_FOLDER_NAME = "zip";
	private static final FileFilterIsDirectory fileFilterIsDirectory = new FileFilterIsDirectory();
	private static final String INSTALL_WARS_XML_PATH_PLACE_HOLDER = "%installWarsXml%";
	private static final String TIME_STAMP_PLACE_HOLDER = "%timeStamp%";
	private static final String USER_PLACE_HOLDER = "%user%";
	private static final String PASSWORD_PLACE_HOLDER = "%password%";
	private static final String DIR_PLACE_HOLDER = "%dir%";
	private static final String COPY_COMMAND = "/usr/bin/cp";
	private static Logger logger = JulUtils.getLogger();

	private static void copyFile2RemoteServers(final String localFilePath, final String remoteFilePath) {
		StringBuilder sb = new StringBuilder();
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
				String[] command = { COPY_COMMAND, localFilePath, remoteFilePath };
				SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(command);
				logger.info(scer.getExecutorResponse());
			}
			if (sb.length() > 0) {
				sb.append(OzConstants.COMMA);
			}
			sb.append(applicationServer);
		}
		logger.info(StringUtils.concat(localFilePath, " has been copied to ", remoteFilePath, " on servers: ",
				sb.toString()));
	}

	private static String generateBackupAndCopyScript(final String installZipFilePath) {
		String scriptLocalPath = null;
		String lineSeparator = OzConstants.UNIX_LINE_SEPARATOR;
		List<ZipEntry> zipContentsList = ZipUtils.getZipFileContents(installZipFilePath);
		logger.info("number of zip entries: " + String.valueOf(zipContentsList.size()));
		if (zipContentsList.size() > 0) {
			logger.info(StringUtils.concat("folder1 Path: ", installZipFilePath));
			String remotePortalVersionFolderPath = PortalDeploymentParameters.getRemotePortalVersionFolderPath();
			StringBuilder backupScriptContents = new StringBuilder(
					StringUtils.concat("cd ", remotePortalVersionFolderPath, OzConstants.UNIX_LINE_SEPARATOR));
			StringBuilder copyScriptContents = new StringBuilder(StringUtils.concat(OzConstants.UNIX_LINE_SEPARATOR,
					"cd ", PortalDeploymentParameters.getMatafPortalFolder(), OzConstants.UNIX_LINE_SEPARATOR));
			String[] matafPortalFoldersArray = PortalDeploymentParameters.getMatafPortalFoldersArray();
			String zipBackupFileName = StringUtils.concat(versionDate, "_backup_", DateTimeUtils.getTimeStamp(),
					OzConstants.ZIP_SUFFIX);
			String portalRemoteBackupFolder = PathUtils.addUnixPaths(PortalDeploymentParameters.getMatafPortalFolder(),
					BACKUP_FOLDER_NAME);
			String targertZipFilePath = StringUtils.concat(portalRemoteBackupFolder, OzConstants.UNIX_FILE_SEPARATOR,
					zipBackupFileName);
			String matafPortalFolder = PortalDeploymentParameters.getMatafPortalFolder();
			for (ZipEntry zipEntry1 : zipContentsList) {
				String[] zipEntryNameBreakdown = zipEntry1.getName().split(RegexpUtils.REGEXP_ANY_FILE_SEPARATOR);
				String currentFolder = null;
				logger.finest(StringUtils.concat("zip entry name: ", zipEntry1.getName(),
						" zip entry name breakdown length: ", String.valueOf(zipEntryNameBreakdown.length)));
				if (zipEntryNameBreakdown.length > 1) {
					currentFolder = zipEntryNameBreakdown[1];
				}
				if (!zipEntry1.isDirectory() && currentFolder != null
						&& ArrayUtils.isObjectInArray(matafPortalFoldersArray, currentFolder)) {
					String zipentryName = zipEntry1.getName().substring(versionDate.length() + 1);
					String sourceFilePath = StringUtils.concat(matafPortalFolder, zipentryName);
					String backupLine = StringUtils.concat("zip ", targertZipFilePath, OzConstants.BLANK,
							sourceFilePath, lineSeparator);
					String copyLine = StringUtils.concat("cp  ",
							PathUtils.addUnixPaths(remotePortalVersionFolderPath, zipEntry1.getName()),
							OzConstants.BLANK, sourceFilePath, lineSeparator);
					backupScriptContents.append(backupLine.replaceAll("\\\\", "/"));
					copyScriptContents.append(copyLine.replaceAll("\\\\", "/"));
				}
			}
			backupScriptContents.append("echo backup done !");
			copyScriptContents.append(StringUtils.concat("echo copy of ", String.valueOf(zipContentsList.size()),
					" files has completed !"));
			String scriptContents = backupScriptContents.append(copyScriptContents).toString();
			logger.info(StringUtils.concat("script:\n", scriptContents));
			String backupScriptFileFullName = StringUtils.concat("backupAndCopy_",
					PathUtils.getTemporaryFileName().concat(OzConstants.SH_SUFFIX));
			scriptLocalPath = getLocalScriptsFolderPath();
			scriptLocalPath = PathUtils.getFullPath(scriptLocalPath, backupScriptFileFullName);
			NioUtils.writeString2File(scriptLocalPath, scriptContents);
		}
		return scriptLocalPath;
	}

	private static String getInstallWarsPath() {
		String installWarsPath = PathUtils.addUnixPaths(PortalDeploymentParameters.getRemotePortalVersionFolderPath(),
				versionDate);
		installWarsPath = PathUtils.addUnixPaths(installWarsPath, PortalDeploymentFoldersEnum.Portlets.toString());
		installWarsPath = PathUtils.addUnixPaths(installWarsPath,
				PortalDeploymentParameters.getInstallWarsXmlFileName());
		logger.info(installWarsPath);
		return installWarsPath;
	}

	private static String getLocalScriptsFolderPath() {
		String localScriptsFolderPath = PathUtils.getFullPath(AutoDeployGlobalParameters.getPortalFolderPath(),
				LOCAL_SCRIPTS_FOLDER_NAME);
		FolderUtils.createFolderIfNotExists(localScriptsFolderPath);
		return localScriptsFolderPath;
	}

	private static String getLocalZipFolderPath() {
		String localZipFolderPath = PathUtils.getFullPath(AutoDeployGlobalParameters.getPortalFolderPath(),
				LOCAL_ZIP_FOLDER_NAME);
		FolderUtils.createFolderIfNotExists(localZipFolderPath);
		return localZipFolderPath;
	}

	private static String getMkdirCommand(final String dirPath) {
		String mkdirCommand = StringUtils.concat("if [[ ! -d ", DIR_PLACE_HOLDER, " ]] ; then mkdir ", DIR_PLACE_HOLDER,
				";echo ", DIR_PLACE_HOLDER, " created;else echo ", DIR_PLACE_HOLDER, " already exists; fi");
		mkdirCommand = mkdirCommand.replaceAll(DIR_PLACE_HOLDER, dirPath);
		logger.info("mkdir command: ".concat(dirPath));
		return mkdirCommand;
	}

	private static File getPortalDeploymentFiles(final String portalDeploymentFilesPath) {
		File portalDeploymentFilesFile = new File(portalDeploymentFilesPath);
		String folder2ProcessPath = null;
		if (portalDeploymentFilesFile.isFile()) {
			String fileType = PathUtils.getFileExtension(portalDeploymentFilesFile);
			logger.info("fileType: ".concat(fileType));
			if (fileType.equals(OzConstants.ZIP_SUFFIX.substring(1))) {
				String portalFolderPath = AutoDeployGlobalParameters.getPortalFolderPath();
				String timeStamp = DateTimeUtils.getTimeStamp();
				folder2ProcessPath = PathUtils.getFullPath(portalFolderPath, timeStamp);
				logger.info("folder2ProcessPath: ".concat(folder2ProcessPath));
				ZipUtils.extractAllFiles(portalDeploymentFilesPath, folder2ProcessPath);
				portalLocalDeploymentZipFilePath = portalDeploymentFilesFile.getAbsolutePath();
			} else {
				SystemUtils.printMessageAndExit("zip file expected. Processing has been aborted.",
						OzConstants.EXIT_STATUS_ABNORMAL);
			}
		} else {
			if (portalDeploymentFilesFile.isDirectory()) {
				folder2ProcessPath = portalDeploymentFilesFile.getAbsolutePath();
				String zipFolderPath = getLocalZipFolderPath();
				String zipFileName = StringUtils.concat("installation_Files_", DateTimeUtils.getTimeStamp(),
						OzConstants.ZIP_SUFFIX);
				portalLocalDeploymentZipFilePath = PathUtils.getFullPath(zipFolderPath, zipFileName);
				ZipUtils.zipFolder(portalLocalDeploymentZipFilePath, portalDeploymentFilesPath,
						portalDeploymentFilesPath);
			} else {
				SystemUtils.printMessageAndExit(
						portalDeploymentFilesPath + " does not exist !. Proccessing has been aborted",
						OzConstants.EXIT_STATUS_ABNORMAL, false);

			}
		}
		File folder2ProcessFile = PathUtils.goDownTheFolderTree(folder2ProcessPath, 1);
		logger.info(StringUtils.concat("processing deployment folder: ", folder2ProcessFile.getAbsolutePath(),
				" zip file path: ", portalLocalDeploymentZipFilePath));
		return folder2ProcessFile;
	}

	private static Properties getRemoteProperties() {
		String remotePropertiesFilePath = PortalDeploymentParameters.getRemotePropertiesFilePath();
		Properties remoteProperties = PropertiesUtils.loadPropertiesFile(remotePropertiesFilePath);
		return remoteProperties;
	}

	@Override
	public Outcome processFile(final String portalLocalDeploymentFilePath) {
		File folder2ProcessFile = getPortalDeploymentFiles(portalLocalDeploymentFilePath);
		versionDate = folder2ProcessFile.getName();
		processMatafPortal(portalLocalDeploymentFilePath);
		File[] folderList = folder2ProcessFile.listFiles(fileFilterIsDirectory);
		for (File folder1 : folderList) {
			String folderName = folder1.getName();
			String folder1Path = folder1.getAbsolutePath();
			PortalDeploymentFoldersEnum portalDeploymentFoldersEnum = PortalDeploymentFoldersEnum.valueOf(folderName);
			switch (portalDeploymentFoldersEnum) {
			case EARs:
				processEarFiles(folder1Path);
				break;
			case Resources:
			case Properties:
			case shared:
			case Test:
				break;
			case Portlets:
				processPortLets(folder1Path);
				break;
			default:
				logger.warning(StringUtils.concat(folder1Path, " has not been processed !"));
			}
		}
		logger.info("portal deployment has completed.");
		return null;
	}

	private static void processMatafPortal(final String portalLocalDeploymentFilePath) {
		ZipUtils.logZipFileContents(portalLocalDeploymentZipFilePath);
		String localBackupCopyScriptFilePath = generateBackupAndCopyScript(portalLocalDeploymentZipFilePath);
		if (localBackupCopyScriptFilePath != null) {
			String portalDeploymentZipFileAbsolutePath = PathUtils.getAbsolutePath(portalLocalDeploymentZipFilePath);
			String remoteDeploymentZipFilePath = PathUtils.addUnixPaths(
					PortalDeploymentParameters.getRemotePortalDeploymentFolderPath(),
					"install_".concat(DateTimeUtils.getTimeStamp()).concat(OzConstants.ZIP_SUFFIX));
			runRemoteCommand(getMkdirCommand(PortalDeploymentParameters.getRemotePortalDeploymentFolderPath()));
			copyFile2RemoteServers(portalDeploymentZipFileAbsolutePath, remoteDeploymentZipFilePath);
			String remoteBackupCopyScriptFilePath = PathUtils.addUnixPaths(
					PortalDeploymentParameters.getRemotePortalDeploymentFolderPath(),
					PathUtils.getFileNameAndExtension(localBackupCopyScriptFilePath));
			copyFile2RemoteServers(localBackupCopyScriptFilePath, remoteBackupCopyScriptFilePath);
			String unzipCommand = StringUtils.concat("cd ",
					PortalDeploymentParameters.getRemotePortalVersionFolderPath(), "; unzip -o ",
					remoteDeploymentZipFilePath, ";chmod 700 ", remoteBackupCopyScriptFilePath);
			runRemoteCommand(unzipCommand);
			runRemoteCommand(remoteBackupCopyScriptFilePath);
		}
		return;
	}

	private static void processEarFiles(final String folder1Path) {
		SystemPropertiesUtils.setSystemProperty(AutoDeployEarProcessor.AUTODEPLOY_SYSTEM,
				PortalDeploymentParameters.getSystem());
		String[] earDeplymentPatameter = { null, folder1Path };
		AutoDeployEarUtils.processEarsPatameter(earDeplymentPatameter);
	}

	private static void processPortLets(final String portletsPath) {
		String installWarsPath = getInstallWarsPath();
		String encryptedUser = PortalDeploymentParameters.getXmlAccessUser();
		String encryptedPassword = PortalDeploymentParameters.getXmlAccessPassword();
		String user = CryptographyUtils.decryptString(encryptedUser, EncryptionMethodEnum.PLAIN);
		String password = CryptographyUtils.decryptString(encryptedPassword, EncryptionMethodEnum.PLAIN);
		String xmlAccessCommand = PortalDeploymentParameters.getXmlAccessCommand();
		xmlAccessCommand = xmlAccessCommand.replace(INSTALL_WARS_XML_PATH_PLACE_HOLDER, installWarsPath);
		xmlAccessCommand = xmlAccessCommand.replace(TIME_STAMP_PLACE_HOLDER, DateTimeUtils.getTimeStamp());
		xmlAccessCommand = xmlAccessCommand.replace(USER_PLACE_HOLDER, user);
		xmlAccessCommand = xmlAccessCommand.replace(PASSWORD_PLACE_HOLDER, password);
		String[] applicationServersArray = PortalDeploymentParameters.getApplicationServersArray();
		Properties remoteProperties = getRemoteProperties();
		logger.info("xmlAccessCommand" + xmlAccessCommand);
		remoteProperties.put(SshParams.COMMAND_LINE, xmlAccessCommand);
		remoteProperties.put(SshParams.SERVER, applicationServersArray[0]);
		JschUtils.exec(remoteProperties);
	}

	private static void runRemoteCommand(final String commandLine) {
		StringBuilder sb = new StringBuilder();
		Properties remoteProperties = getRemoteProperties();
		String[] applicationServersArray = PortalDeploymentParameters.getApplicationServersArray();
		remoteProperties.put(SshParams.COMMAND_LINE, commandLine);
		for (String applicationServer : applicationServersArray) {
			remoteProperties.put(SshParams.SERVER, applicationServer);
			JschUtils.exec(remoteProperties);
			if (sb.length() > 0) {
				sb.append(OzConstants.COMMA);
			}
			sb.append(applicationServer);
		}
		logger.info(StringUtils.concat(commandLine, " has run on ", sb.toString()));
	}
}
