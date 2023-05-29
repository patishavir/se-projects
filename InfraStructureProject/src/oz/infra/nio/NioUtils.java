package oz.infra.nio;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.spi.FileSystemProvider;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.visitor.FilesInFolderVisitor;
import oz.infra.operaion.Outcome;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.infra.thread.ThreadUtils;
import oz.infra.varargs.VarArgsUtils;

public class NioUtils {
	private static final int WAITLOOP_LIMIT = 10;
	private static final long WAIT_INTERVAL = 1000L;
	public static final String MODE_700 = "-rwx------";
	public static final String MODE_755 = "-rwxr-xr-x";
	private static Logger logger = JulUtils.getLogger();

	public static Path copy(final Path source, final Path destination, final CopyOption... copyOptions) {
		Path target = null;
		try {
			target = Files.copy(source, destination, copyOptions);
			if (Files.isRegularFile(target)) {
				logger.info(StringUtils.concat(source.toAbsolutePath().toString(), " has been copied to ", destination.toAbsolutePath().toString(),
						" result: ", target.toAbsolutePath().toString()));
			} else {
				String copyFailedMessage = StringUtils.concat("Copy failed. source: ", source.toAbsolutePath().toString(), " deatination: ",
						destination.toAbsolutePath().toString(), ".\ntarget file : ", target.toAbsolutePath().toString(), " does not exist !");
				logger.warning(copyFailedMessage);
			}
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return target;
	}

	public static Path copy(final String source, final String target, final CopyOption... copyOptions) {
		return copy(Paths.get(source), Paths.get(target), copyOptions);

	}

	public static Path copyAndSaveExisting(final Path source, final Path destination, final CopyOption... copyOptions) {
		if (Files.exists(destination)) {
			String backupPath = PathUtils.getBackupFilePath(destination.toString());
			move(destination, Paths.get(backupPath));
		}
		return copy(source, destination, copyOptions);
	}

	public static Path copyAndSaveExisting(final String source, final String destination, final CopyOption... copyOptions) {
		return copyAndSaveExisting(Paths.get(source), Paths.get(destination), copyOptions);
	}

	public static Outcome copyFolder(final String sourcePath, final String targetPath, final CopyOption... copyOptions) {
		// StandardCopyOption.COPY_ATTRIBUTES,
		// StandardCopyOption.REPLACE_EXISTING
		Outcome outcome = Outcome.SUCCESS;
		File sourceFile = new File(sourcePath);
		File targetFile = new File(targetPath);
		if (sourceFile.getAbsolutePath().equals(targetFile.getAbsolutePath())) {
			logger.info(StringUtils.concat("Source path ", sourcePath, " and target path ", targetPath, " are identical. Nothing to do."));
		} else {
			CopyFileVisitor copyFileVisitor = new CopyFileVisitor(Paths.get(targetPath), copyOptions);
			try {
				Files.walkFileTree(Paths.get(sourcePath), copyFileVisitor);
				logger.info(StringUtils.concat(String.valueOf(copyFileVisitor.getFileCopyCount()), " files copied from ", sourcePath, " to ",
						targetPath));
			} catch (Exception ex) {
				logger.warning(StringUtils.concat("failed to copy ", sourcePath, " to ", targetPath));
				ExceptionUtils.printMessageAndStackTrace(ex);
				outcome = Outcome.FAILURE;
			}
		}
		return outcome;
	}

	public static Path createDirectories(final String dirPath1, final FileAttribute<?>... attrs) {
		Path dirPath = Paths.get(dirPath1);
		Path newPath = null;
		try {
			newPath = Files.createDirectories(dirPath, attrs);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return newPath;
	}

	public static Path createDirectory(final String dirPath1, final FileAttribute<?>... attrs) {
		Path dirPath = Paths.get(dirPath1);
		Path newPath = null;
		try {
			newPath = Files.createDirectory(dirPath, attrs);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return newPath;
	}

	public static void createDirectoryTrees(final String[] folderPaths) {
		for (String folderPath1 : folderPaths) {
			Path newPath = createDirectories(folderPath1);
			logger.info("folders created: " + newPath.toString());
		}
	}

	public static void delete(final Path source, final Level... levels) {
		Level level = VarArgsUtils.getMyArg(Level.INFO, levels);
		try {
			Files.delete(source);
			logger.log(level, StringUtils.concat(source.toString(), " has been successfully deleted."));
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	public static void delete(final String source, final Level... levels) {
		delete(Paths.get(source), levels);
	}

	private static Map<String, String> getEnv(final boolean create) {
		Map<String, String> env = new HashMap<String, String>();
		env.put("create", String.valueOf(create));
		return env;
	}

	public static String getLastModifiedTime(final String filePath, final String dateFormateString) {
		String lastModifiedTimeString = null;
		try {
			FileTime fileTime = Files.getLastModifiedTime(Paths.get(filePath), LinkOption.NOFOLLOW_LINKS);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormateString);
			lastModifiedTimeString = sdf.format(fileTime.toMillis());
			logger.info("last modified time: " + lastModifiedTimeString);

		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return lastModifiedTimeString;
	}

	public static Set<PosixFilePermission> getPosixFilePermissionSet(final String permissionsString) {
		Set<PosixFilePermission> permissions = new HashSet<>();

		// drwxrwxrwx

		if (permissionsString.charAt(1) == 'r') {
			permissions.add(PosixFilePermission.OWNER_READ);
		}

		if (permissionsString.charAt(2) == 'w') {
			permissions.add(PosixFilePermission.OWNER_WRITE);
		}

		if (permissionsString.charAt(3) == 'x' || permissionsString.charAt(3) == 's') {
			permissions.add(PosixFilePermission.OWNER_EXECUTE);
		}

		if (permissionsString.charAt(4) == 'r') {
			permissions.add(PosixFilePermission.GROUP_READ);
		}

		if (permissionsString.charAt(5) == 'w') {
			permissions.add(PosixFilePermission.GROUP_WRITE);
		}

		if (permissionsString.charAt(6) == 'x' || permissionsString.charAt(3) == 's') {
			permissions.add(PosixFilePermission.GROUP_EXECUTE);
		}

		if (permissionsString.charAt(7) == 'r') {
			permissions.add(PosixFilePermission.OTHERS_READ);
		}

		if (permissionsString.charAt(8) == 'w') {
			permissions.add(PosixFilePermission.OTHERS_WRITE);
		}

		if (permissionsString.charAt(9) == 'x' || permissionsString.charAt(3) == 's') {
			permissions.add(PosixFilePermission.OTHERS_EXECUTE);
		}
		return permissions;
	}

	public static FileSystem getZipFileSystem(final Path zipFilePath, final boolean isCreate) {
		FileSystemProvider fileSystemProvider = getZipFSProvider();
		Map<String, String> env = getEnv(isCreate);
		FileSystem fileSystem = null;
		try {
			fileSystem = fileSystemProvider.newFileSystem(zipFilePath, env);
			logger.info(StringUtils.concat("zip file path: ", zipFilePath.toString(), "  file system: ", fileSystem.toString()));
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return fileSystem;
	}

	public static FileSystem getZipFileSystem(final String zipFilePath, final boolean isCreate) {
		return getZipFileSystem(Paths.get(zipFilePath), isCreate);
	}

	private static FileSystemProvider getZipFSProvider() {
		FileSystemProvider fileSystemProvider = null;
		for (FileSystemProvider provider : FileSystemProvider.installedProviders()) {
			if ("jar".equals(provider.getScheme())) {
				fileSystemProvider = provider;
				logger.info(StringUtils.concat("found ", provider.toString()));
			}
		}
		if (fileSystemProvider == null) {
			logger.warning("no provider found for scheme : jar");
		}
		return fileSystemProvider;
	}

	public static void list(final Path path, final boolean verbose) throws IOException {
		if (!"/".equals(path.toString())) {
			System.out.printf("  %s%n", path.toString());
			if (verbose) {
				logger.info(Files.readAttributes(path, BasicFileAttributes.class).toString());
			}
		}
		if (Files.notExists(path)) {
			return;
		}
		if (Files.isDirectory(path)) {
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
				for (Path child : ds) {
					list(child, verbose);
				}
			}
		}
	}

	public static void listFileSystemProviders() {
		for (FileSystemProvider provider : FileSystemProvider.installedProviders()) {
			logger.info(StringUtils.concat(provider.toString() + "  scheme: " + provider.getScheme()));
		}
	}

	public static String makeRunnableTempFile(final String scriptContents, final String... fileNamePrefix) {
		String namePrefix = VarArgsUtils.getMyArg("runnable", fileNamePrefix);
		String tempDir = SystemPropertiesUtils.getTempDir();
		String timeStamp = DateTimeUtils.getMilliSecsTimeStamp();
		String scriptName = StringUtils.concat(namePrefix, OzConstants.UNDERSCORE, timeStamp, SystemUtils.getScriptSuffix());
		String localScriptPath = StringUtils.concat(tempDir, scriptName);
		logger.info("localSdcriptPath: ".concat(localScriptPath));
		NioUtils.writeString2File(localScriptPath, scriptContents);
		try {
			NioUtils.setPosixFilePermissions(localScriptPath, MODE_700);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return localScriptPath;
	}

	public static Path move(final Path sourcePath, final Path targetPath, final boolean abortOnFailure, final CopyOption... options) {
		Path result = null;
		String message = StringUtils.concat(" move of ", sourcePath.toString(), " to ", targetPath.toString(), " has ");
		try {
			result = Files.move(sourcePath, targetPath, options);
			logger.info(StringUtils.concat(message, "succeeded. result: ", result.toAbsolutePath().toString()));
		} catch (Exception ex) {
			logger.info(message.concat("failed"));
			ExceptionUtils.printMessageAndStackTrace(ex);
			if (abortOnFailure) {
				SystemUtils.printMessageAndExit("\nProcessin has been aborted", OzConstants.EXIT_STATUS_ABNORMAL, false);
			}
		}
		return result;
	}

	public static Path move(final Path sourcePath, final Path targetPath, final CopyOption... options) {
		return move(sourcePath, targetPath, false, options);
	}

	public static Path move(final String source, final String target, final boolean abortOnFailure, final CopyOption... options) {
		return move(Paths.get(source), Paths.get(target), abortOnFailure, options);
	}

	public static Path move(final String source, final String target, final CopyOption... options) {
		return move(Paths.get(source), Paths.get(target), false, options);
	}

	public static void printStandardCopyOptions() {
		StandardCopyOption[] standardCopyOptions = StandardCopyOption.values();
		ArrayUtils.printArray(standardCopyOptions);
	}

	public static String readFile2String(final Path filePath) {
		String contents = null;
		try {
			byte[] allBytes = Files.readAllBytes(filePath);
			contents = new String(allBytes);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return contents;
	}

	public static String readFile2String(final String filePath) {
		return readFile2String(Paths.get(filePath));
	}

	public static List<String> recursivelyListFilesInFolder(final String rootFolderPath, final Level... levels) {
		Level level = VarArgsUtils.getMyArg(Level.FINEST, levels);
		FilesInFolderVisitor fileVisitor = new FilesInFolderVisitor();
		walkFileTree(rootFolderPath, fileVisitor);
		List<String> fileList = fileVisitor.getFileList();
		logger.log(level, StringUtils.concat(String.valueOf(fileList.size()), " files found in ", rootFolderPath, " !"));
		return fileList;
	}

	public static Path rename(final String sourceFilePath, final String newName) {
		Path newPath = null;
		try {
			Path sourcePath = Paths.get(sourceFilePath);
			newPath = Files.move(sourcePath, sourcePath.resolveSibling(newName));
			logger.info("new path: " + newPath.toString());
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return newPath;
	}

	public static Outcome setLastModifiedTime(final String filePathString, final String dateString, final String dateFormatString) {
		Path path = Paths.get(filePathString);
		SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormatString);
		Outcome outcome = Outcome.SUCCESS;
		try {
			Date date = dateFormatter.parse(dateString);
			FileTime fileTime = FileTime.fromMillis(date.getTime());
			Files.setLastModifiedTime(path, fileTime);
		} catch (Exception ex) {
			outcome = Outcome.FAILURE;
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return outcome;
	}

	public static Outcome setPosixFilePermissions(final String pathString, final String permissionString) {
		Outcome outcome = Outcome.FAILURE;
		if (SystemUtils.isUnixOS()) {
			Path path = Paths.get(pathString);
			try {
				Files.setPosixFilePermissions(path, NioUtils.getPosixFilePermissionSet(permissionString));
				outcome = Outcome.SUCCESS;
			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
			}
		} else {
			outcome = Outcome.SUCCESS;
		}
		return outcome;
	}

	public static Path waitAndCopy(final Path sourcePath, final Path targetPath, final CopyOption... copyOptions) {
		Path result = null;

		for (int i = 0; i < WAITLOOP_LIMIT && result == null; i++) {
			try {
				result = Files.copy(sourcePath, targetPath, copyOptions);
				logger.info(StringUtils.concat(sourcePath.toString(), " has been moved to ", targetPath.toString(), " result: ", result.toString()));
			} catch (FileSystemException fsex) {
				logger.info(fsex.getMessage());
				ThreadUtils.sleep(WAIT_INTERVAL, Level.INFO);
			} catch (IOException ioex) {
				ExceptionUtils.printMessageAndStackTrace(ioex);
				ThreadUtils.sleep(WAIT_INTERVAL, Level.INFO);
			}
		}
		return result;
	}

	public static Path waitAndCopy(final String source, final String target, final CopyOption... copyOptions) {
		return waitAndCopy(Paths.get(source), Paths.get(target), copyOptions);

	}

	public static Path waitAndMove(final Path sourcePath, final Path targetPath, final CopyOption... options) {
		Path result = null;

		for (int i = 0; i < WAITLOOP_LIMIT && result == null; i++) {
			try {
				result = Files.move(sourcePath, targetPath, options);
				logger.info(StringUtils.concat(sourcePath.toString(), " has been moved to ", targetPath.toString(), " result: ", result.toString()));
			} catch (FileSystemException fsex) {
				logger.info(fsex.getMessage());
				ThreadUtils.sleep(WAIT_INTERVAL, Level.INFO);
			} catch (IOException ioex) {
				ExceptionUtils.printMessageAndStackTrace(ioex);
				ThreadUtils.sleep(WAIT_INTERVAL, Level.INFO);
			}
		}
		return result;
	}

	public static Path waitAndMove(final String source, final String target, final CopyOption... options) {
		return waitAndMove(Paths.get(source), Paths.get(target), options);
	}

	public static Outcome walkFileTree(final String startingFolder, final FileVisitor<Path> fileVisitor) {
		StopWatch stopWatch = new StopWatch();
		Outcome outCome = Outcome.SUCCESS;
		Path startingFolderPath = Paths.get(startingFolder);
		try {
			Files.walkFileTree(startingFolderPath, fileVisitor);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			outCome = Outcome.FAILURE;
		}
		logger.info(stopWatch.appendElapsedTimeToMessage("Filetree walk has completed in "));
		return outCome;
	}

	public static Path writeString2File(final Path filePath, final String contents, final Level level, final OpenOption... options) {
		Path target = null;
		byte[] allBytes = contents.getBytes();
		try {
			target = Files.write(filePath, allBytes, options);
			logger.log(level, StringUtils.concat(target.toString(), " has been written. length: " + String.valueOf(contents.length())));
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return target;
	}

	public static Path writeString2File(final String filePath, final String contents, final Level level, final OpenOption... options) {
		return writeString2File(Paths.get(filePath), contents, level, options);
	}

	public static Path writeString2File(final String filePath, final String contents, final OpenOption... options) {
		return writeString2File(Paths.get(filePath), contents, Level.INFO, options);
	}
}