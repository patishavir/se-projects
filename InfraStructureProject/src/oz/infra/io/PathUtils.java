package oz.infra.io;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.filefilter.FileFilterIsDirectory;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.ssh.SshParams;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.infra.system.env.EnvironmentUtils;
import oz.infra.varargs.VarArgsUtils;

public class PathUtils {
	private static final Object[] IMAGE_FILE_EXTENSIONS_ARRAY = { "jpg", "bmp", "png", "tif", "tiff", "gif" };
	private static final String PATH = "Path";
	private static Logger logger = JulUtils.getLogger();

	public static String addPaths(final String path1, final String path2, final String... separators) {
		String separator = VarArgsUtils.getMyArg(File.separator, separators);
		int separatorsCount = 0;
		if (path1.endsWith(separator)) {
			separatorsCount = 1;
		}
		if (path2.startsWith(separator)) {
			separatorsCount++;
		}
		String result = StringUtils.concat(path1, path2);
		if (separatorsCount == 0) {
			result = StringUtils.concat(path1, separator, path2);
		} else {
			if (separatorsCount == 2) {
				result = StringUtils.concat(path1, path2.substring(1));
			}
		}
		return result;
	}

	public static String addPrefix2FilePath(final String filePath, final String... prefix) {
		String fileNamePrefix = OzConstants.UNDERSCORE;
		if (prefix.length > 0) {
			fileNamePrefix = prefix[0];
		}
		String parentFolderPath = getParentFolderPath(filePath);
		String fileName = getNameWithoutExtension(filePath);
		String fileExtension = getFileExtension(filePath);
		String outFilePath = PathUtils.getFullPath(parentFolderPath, fileNamePrefix + fileName + OzConstants.DOT + fileExtension);
		logger.info(outFilePath);
		return outFilePath;
	}

	public static String addUnixPaths(final String path1, final String path2) {
		return addPaths(path1, path2, OzConstants.UNIX_FILE_SEPARATOR);
	}

	public static String adjustPathToCurrentOS(final String path) {
		String adjustedPath = path;
		Map<Character, Character> separatorMap = new HashMap<Character, Character>();
		separatorMap.put('\\', '/');
		separatorMap.put('/', '\\');
		char currentFileSeparator = File.separatorChar;
		// 4 debug !!!!! currentFileSeparator ='/';
		char otherOsSeparator = separatorMap.get(currentFileSeparator);
		if (path.indexOf(otherOsSeparator) >= 0) {
			adjustedPath = path.replace(otherOsSeparator, currentFileSeparator);
		}
		return adjustedPath;
	}

	public static String appendTimeStampToFileName(final File file) {
		String extension = getFileExtension(file);
		String name = getFileNameWithoutExtension(file);
		if (extension.length() > 0) {
			extension = OzConstants.DOT.concat(extension);
		}
		String fileNameAndTimeStamp = StringUtils.concat(name, OzConstants.UNDERSCORE, DateTimeUtils.getTimeStamp(), extension);
		return fileNameAndTimeStamp;
	}

	public static String appendTralingSeparatorIfNotExists(final String path) {
		String path2Return = path;
		if (!path2Return.endsWith(File.separator)) {
			path2Return = path2Return.concat(File.separator);
		}
		return path2Return;
	}

	public static boolean arePathsEqual(final String path1, final String path2) {
		File file1 = new File(path1);
		File file2 = new File(path2);
		String absolutePath1 = file1.getAbsolutePath();
		String absolutePath2 = file2.getAbsolutePath();
		boolean arePathsEquals = absolutePath1.equals(absolutePath2);
		return arePathsEquals;
	}

	public static String changeFileExtension(final String filePath, final String newExtension) {
		String actualNewExtension = newExtension;
		if (actualNewExtension.startsWith(OzConstants.DOT)) {
			actualNewExtension = actualNewExtension.substring(1);
		}
		String newFilePath = filePath;
		int lastIndexOfDot = filePath.lastIndexOf(OzConstants.DOT);
		if (lastIndexOfDot > OzConstants.STRING_NOT_FOUND) {
			newFilePath = filePath.substring(0, lastIndexOfDot + 1) + actualNewExtension;
		} else {
			newFilePath = filePath.concat(OzConstants.DOT).concat(actualNewExtension);
		}
		logger.finest("filePath: " + filePath + " newFilePath: " + newFilePath);
		return newFilePath;
	}

	public static String getAbsolutePath(final String path) {
		String absolutePath = new File(path).getAbsolutePath();
		return absolutePath;
	}

	public static String getBackupFilePath(final File file) {
		String parentPath = file.getParent();
		String backupPath = getFullPath(parentPath, appendTimeStampToFileName(file));
		logger.info("file path: " + file.getAbsolutePath() + " backupPath: " + backupPath);
		return backupPath;
	}

	public static String getBackupFilePath(final String filePath) {
		return getBackupFilePath(new File(filePath));
	}

	public static String getCanonicalPath(final File file) {
		String canonicalPath = null;
		try {
			canonicalPath = file.getCanonicalPath();
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return canonicalPath;
	}

	public static String getCanonicalPath(final String path) {
		return getCanonicalPath(new File(path));
	}

	public static String getCurrentDirPath() {
		return FolderUtils.getCurrentDirPath();
	}

	public static String getFileExtension(final File file) {
		String extension = file.getName();
		int lastDot = extension.lastIndexOf(OzConstants.DOT);
		int lastSeparator = extension.lastIndexOf(File.separator);
		if (lastDot >= 0 && lastDot > lastSeparator) {
			extension = extension.substring(lastDot + 1);
		} else {
			extension = "";
		}
		logger.finest("file: " + file.getAbsolutePath() + " extension: " + extension);
		return extension;
	}

	public static String getFileExtension(final String filePath) {
		return getFileExtension(new File(filePath));
	}

	public static String getFileNameAndExtension(final String inputFilePath, final Level... levels) {
		Level level = VarArgsUtils.getMyArg(Level.FINEST, levels);
		File inputFile = new File(inputFilePath);
		String name = inputFile.getName();
		logger.log(level, StringUtils.concat("path : ", inputFilePath, " file name: ", name));
		return name;
	}

	public static String[] getFileNameAndExtensionArray(final File inputFile, final Level... levels) {
		Level level = VarArgsUtils.getMyArg(Level.FINEST, levels);
		String nameAndExtension = inputFile.getName();
		int lastIndex = nameAndExtension.lastIndexOf(OzConstants.DOT);
		String name = nameAndExtension;
		String extension = OzConstants.EMPTY_STRING;
		if (lastIndex > OzConstants.STRING_NOT_FOUND) {
			name = nameAndExtension.substring(0, lastIndex);
			extension = nameAndExtension.substring(lastIndex + 1);
		}
		String[] fileNameAndExtensionArray = { name, extension };
		logger.finest("nameAndExtension length: " + String.valueOf(nameAndExtension.length() + " lastIndex: " + String.valueOf(lastIndex)));
		logger.log(level, StringUtils.concat("path : ", inputFile.getAbsolutePath(), " file name: #", name, "# extension: #", extension, "#"));
		return fileNameAndExtensionArray;
	}

	public static String[] getFileNameAndExtensionArray(final String inputFilePath, final Level... levels) {
		File inputFile = new File(inputFilePath);
		return getFileNameAndExtensionArray(inputFile, levels);
	}

	public static String getFileNameWithoutExtension(final File file) {
		String fileName = file.getName();
		return getFileNameWithoutExtension(fileName);
	}

	public static String getFileNameWithoutExtension(final String fileNameP) {
		String fileName = fileNameP;
		if (fileName.indexOf(OzConstants.DOT) > OzConstants.STRING_NOT_FOUND) {
			fileName = fileName.substring(0, fileName.lastIndexOf(OzConstants.DOT));
		}
		logger.finest("file Name without extension:" + fileName);
		return fileName;
	}

	public static String getFullPath(final String rootFolderPath, final String relativePath) {
		String resultPath = relativePath;
		if (!isAbsolutePath(resultPath) && (!relativePath.toLowerCase().startsWith(OzConstants.HTTP.toLowerCase() + "://"))) {
			resultPath = addPaths(rootFolderPath, relativePath, File.separator);
		}
		logger.finest("rootFolderPath: " + rootFolderPath + " fileRelativePath:  " + relativePath + " resultPath: " + resultPath);
		return getAbsolutePath(resultPath);
	}

	public static String getGrandParentFolderName(final File file) {
		String name = null;
		if (file != null) {
			File grandParent = file.getParentFile().getParentFile();
			name = grandParent.getName();
		}
		return name;
	}

	public static String getGrandParentFolderName(final String path) {
		File file = new File(path);
		return getGrandParentFolderName(file);
	}

	public static String getGrandParentFolderPath(final String path) {
		File file = new File(path);
		File grandParent = file.getParentFile().getParentFile();
		String grandParentFolderPath = grandParent.getAbsolutePath();
		return grandParentFolderPath;
	}

	public static String getNameWithoutExtension(final String filePath) {
		return getFileNameWithoutExtension(new File(filePath));
	}

	public static String getParentFolderName(final File file) {
		String parentFolderName = null;
		String filePath = file.getPath();
		File parentFile = file.getParentFile();
		if (parentFile != null) {
			parentFolderName = parentFile.getName();
			logger.finest("parent folder name for " + filePath + ": " + parentFolderName);
		} else {
			logger.warning("no parent folder for " + filePath);
		}
		return parentFolderName;
	}

	public static String getParentFolderName(final String filePath) {
		return getParentFolderName(new File(filePath));
	}

	public static String getParentFolderPath(final String filePath) {
		File file = new File(filePath);
		String parentFolderPath = file.getParent();
		logger.finest(parentFolderPath);
		return parentFolderPath;
	}

	public static String getRelativePath(final File fullPathFile, final File basepathFile, final Level... levels) {
		Level level = VarArgsUtils.getMyArg(Level.FINEST, levels);
		String relativePath = basepathFile.toURI().relativize(fullPathFile.toURI()).getPath();
		String message = "fullPath: " + fullPathFile.getAbsolutePath() + " basepath: " + basepathFile.getAbsolutePath() + " relative: "
				+ relativePath;
		logger.log(level, message);
		return relativePath;
	}

	public static String getRelativePath(final String fullPath, final String basepath, final Level... levels) {
		return getRelativePath(new File(fullPath), new File(basepath), levels);
	}

	public static String getRemoteTemporaryFilePath(final Properties remoteProperties, final String localFilePath, final String... fileSeparators) {
		String fileSeparator = VarArgsUtils.getMyArg(OzConstants.UNIX_FILE_SEPARATOR, fileSeparators);
		File file = new File(localFilePath);
		String fileNameWithoutExtension = getFileNameWithoutExtension(file);
		String fileExtension = getFileExtension(file);
		String timeStamp = DateTimeUtils.getTimeStamp();
		String targetFileName = StringUtils.concat(fileNameWithoutExtension, OzConstants.UNDERSCORE, timeStamp);
		String remoteFilePath = PathUtils.addPaths(remoteProperties.getProperty(SshParams.DESTINATION_FILE),
				StringUtils.concat(targetFileName, OzConstants.DOT, fileExtension), fileSeparator);
		return remoteFilePath;
	}

	public static String getTemporaryFileName() {
		String temporaryFileName = SystemUtils.getLocalHostName() + "_"
				+ DateTimeUtils.formatDate(System.currentTimeMillis(), DateTimeUtils.DATE_FORMAT_yyyyMMdd_HHmmss);
		return temporaryFileName;
	}

	public static String getTemporaryFilePath() {
		String tmpdirPath = SystemPropertiesUtils.getTempDir();
		String temporaryFileName = getTemporaryFileName();
		String temporaryFilePath = PathUtils.getFullPath(tmpdirPath, temporaryFileName);
		logger.info("temporaryFilePath: " + temporaryFilePath);
		return temporaryFilePath;
	}

	public static File goDownTheFolderTree(final String folderPath, final int levels) {
		File folderFile = new File(folderPath);
		FileFilterIsDirectory fileFilterIsDirectory = new FileFilterIsDirectory();
		if (folderFile.isDirectory()) {
			for (int i = 0; i < levels && folderFile != null; i++) {
				File[] fileList = folderFile.listFiles(fileFilterIsDirectory);
				if (fileList.length == 1) {
					folderFile = fileList[0];
				} else {
					logger.warning("number of child folders: ".concat(String.valueOf(fileList.length)));
					folderFile = null;
				}
			}
		} else {
			logger.warning(folderPath.concat(" is not a Folder. Processing has been aborted."));
		}
		return folderFile;
	}

	public static boolean isAbsolutePath(final File file) {
		return file.isAbsolute();
	}

	public static boolean isAbsolutePath(final String path) {
		return isAbsolutePath(new File(path));
	}

	public static boolean isImageFile(final File file) {
		String fileExtension = getFileExtension(file);
		return isImageFileType(fileExtension);
	}

	public static boolean isImageFileType(final String fileType) {
		return ArrayUtils.isObjectInArray(IMAGE_FILE_EXTENSIONS_ARRAY, fileType.toLowerCase());
	}

	public static String which(final String partialPath) {
		String pathEnvironmentVariable = EnvironmentUtils.getEnvironmentVariable(PATH);
		String pathSeparator = SystemUtils.getSystemProperty(SystemPropertiesUtils.SYSTEM_PROPERTY_PATH_SEPARATOR);
		logger.info("\npathSeparator: " + pathSeparator + "\npath: " + pathEnvironmentVariable);
		logger.info(PropertiesUtils.getAsDelimitedString(SystemPropertiesUtils.getSystemProperties()));
		String[] pathArray = pathEnvironmentVariable.split(pathSeparator);
		String fullPath = null;
		for (String path1 : pathArray) {
			fullPath = PathUtils.getFullPath(path1, partialPath);
			File fullPathFile = new File(fullPath);
			if (fullPathFile.isFile()) {
				break;
			}
		}
		logger.info("fullPath: " + fullPath);
		return fullPath;
	}
}
