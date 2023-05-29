/*
 * Created on 23/07/2005
 */
package oz.infra.io;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.filefilter.FileFilterIsDirectory;
import oz.infra.io.filefilter.FileFilterIsFileAndRegExpression;
import oz.infra.io.filefilter.FilefilterIsFile;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.number.NumberUtils;
import oz.infra.operaion.Outcome;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.infra.varargs.VarArgsUtils;

/**
 * @author Oded
 */
public class FolderUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void addPrrfix2FileNames(final String folderPath) {
		File folderFile = new File(folderPath);
		File[] filesArray = folderFile.listFiles(new FilefilterIsFile());
		int[] filePrefixesArray = new int[filesArray.length];
		int[] usedPrefixesArray = new int[filesArray.length];
		Arrays.fill(usedPrefixesArray, Integer.MIN_VALUE);
		Random random = new Random();
		for (int i = 0; i < filesArray.length; i++) {
			boolean keepLooking4UnusedPrefix = true;
			do {
				int randomInt = random.nextInt(filesArray.length);
				if (usedPrefixesArray[randomInt] == Integer.MIN_VALUE) {
					usedPrefixesArray[randomInt] = i;
					keepLooking4UnusedPrefix = false;
					filePrefixesArray[i] = randomInt;
				}
			} while (keepLooking4UnusedPrefix);
		}
		for (int i = 0; i < filesArray.length; i++) {
			String newFilePath = filesArray[i].getParent() + File.separator + NumberUtils.formatMinMaxDigits(filePrefixesArray[i], 2, 2)
					+ OzConstants.UNDERSCORE + filesArray[i].getName();
			logger.finest("new file path: " + newFilePath);
			File newFile = new File(newFilePath);
			boolean renameReturnCode = filesArray[i].renameTo(newFile);
			if (!renameReturnCode) {
				logger.warning("Rename operation failed for " + filesArray[i].getAbsolutePath());
			}
		}
		logger.info("addPrrfix2FileNames operation has completed for " + String.valueOf(filesArray.length) + " files.");
	}

	public static final boolean compareFolders(final File dir1, final File dir2) {
		return new FolderUtils().compareFolders(dir1, dir2, false);
	}

	/*
	 * compareFoldersByContents method
	 */
	public static final boolean compareFoldersByContents(final File dir1, final File dir2) {
		return new FolderUtils().compareFolders(dir1, dir2, true);
	}

	public static String copySelectedFiles2Folder(final String sourceFolderPath, final String rangeFilter, final String targetFolderPath) {
		Outcome outcome = Outcome.SUCCESS;
		createFolderIfNotExists(targetFolderPath);
		List<String> fileList = FolderUtils.getFileListWithInNameRange(sourceFolderPath, rangeFilter);
		int fileCount = 0;
		for (String fileName1 : fileList) {
			String sourcePath1 = PathUtils.getFullPath(sourceFolderPath, fileName1);
			String targetPath1 = PathUtils.getFullPath(targetFolderPath, fileName1);
			boolean rc = FileUtils.copyFile(sourcePath1, targetPath1);
			if (!rc) {
				outcome = Outcome.FAILURE;
			} else {
				fileCount++;
			}
		}
		if (outcome == Outcome.FAILURE) {
			logger.warning("copy files to " + targetFolderPath + " has failed !");
		} else {
			logger.info(String.valueOf(fileCount) + " files have been successfully copied to " + targetFolderPath);
		}
		return targetFolderPath;
	}

	public static boolean createFolderIfNotExists(final File folder2Create) {
		boolean returnStatus = false;
		if (!folder2Create.exists()) {
			returnStatus = folder2Create.mkdirs();
			logger.info("creating folder " + folder2Create.getAbsolutePath() + " return code: " + String.valueOf(returnStatus));
		} else {
			returnStatus = folder2Create.isDirectory();
		}
		return returnStatus;
	}

	public static boolean createFolderIfNotExists(final String folderPath) {
		return createFolderIfNotExists(new File(folderPath));
	}

	public static String createTempFolder() {
		String tmpdir = SystemUtils.getSystemProperty(SystemPropertiesUtils.SYSTEM_PROPERTY_JAVA_IO_TMPDIR);
		String timeStamp = DateTimeUtils.getTimeStamp();
		String tempfolderPath = PathUtils.getFullPath(tmpdir, timeStamp);
		File tempfolder = new File(tempfolderPath);
		boolean rc = tempfolder.mkdirs();
		if (rc) {
			logger.info("folder " + tempfolder.getAbsolutePath() + " has been successfully created.");
		} else {
			logger.warning(" create folder " + tempfolder.getAbsolutePath() + " has failed!");
		}
		return tempfolderPath;
	}

	public static boolean deleteFolder(final File folder2Delete) {
		boolean folderRc = false;
		if (folder2Delete != null && folder2Delete.isDirectory()) {
			File[] files = folder2Delete.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteFolder(files[i]);
				} else {
					FileUtils.deleteAndLogResult(files[i]);
				}
			}
			folderRc = FileUtils.deleteAndLogResult(folder2Delete);
		} else {
			logger.warning(folder2Delete.getAbsolutePath() + " does not exist or is not a directory!");
		}
		return folderRc;
	}

	public static boolean deleteFolderRecursively(final File rootFolderFile) {
		boolean outcome = false;
		String absolutePath = rootFolderFile.getAbsolutePath();
		if (!rootFolderFile.isDirectory()) {
			String message = StringUtils.concat(absolutePath, " not found. Processing has been aborted");
			logger.warning(message);
		} else {
			boolean folderameAllowed = false;
			String separator = File.separator;
			if (SystemUtils.isWindowsFlavorOS()) {
				separator = "\\\\";
			}
			String[] pathBreakDown = absolutePath.split(separator);
			final String[] okForRecursiveDelete = { "temp", "tmp", "logs", "bak" };
			if (pathBreakDown.length > 2) {
				for (String foldername : okForRecursiveDelete) {
					folderameAllowed = ArrayUtils.isObjectInArray(pathBreakDown, foldername);
					if (folderameAllowed) {
						break;
					}
				}

				if (!folderameAllowed) {
					String message = StringUtils.concat(absolutePath, " not in allowed for delete folder list. Processing has been aborted");
					logger.warning(message);
				}
				outcome = deleteRecursive(rootFolderFile);
				if (outcome) {
					logger.info(StringUtils.concat(absolutePath, " has been deleted successfully."));
				} else {
					logger.warning(StringUtils.concat("failed to delete ", absolutePath, "."));
				}
			} else {
				logger.warning(StringUtils.concat(absolutePath, " folde level too high for recursive delete !"));
			}
		}
		return outcome;
	}

	public static boolean deleteFolderRecursively(final String rootFolderPath) {
		return deleteFolderRecursively(new File(rootFolderPath));
	}

	private static boolean deleteRecursive(final File rootFolderFile) {
		boolean ret = true;
		if (rootFolderFile.isDirectory()) {
			for (File f : rootFolderFile.listFiles()) {
				ret = ret && deleteRecursive(f);
			}
		}
		return ret && rootFolderFile.delete();
	}

	public static void dieUnlessFolderExists(final File folder) {
		if (!folder.isDirectory()) {
			String exitMessage = SystemUtils.LINE_SEPARATOR + folder.getAbsolutePath() + " does not exist or is not a directory."
					+ SystemUtils.LINE_SEPARATOR + "Operation has been aborted.";
			SystemUtils.printMessageAndExit(exitMessage, OzConstants.EXIT_STATUS_ABNORMAL, false);
		}
	}

	public static void dieUnlessFolderExists(final String folderPath) {
		File folder2Check = new File(folderPath);
		dieUnlessFolderExists(folder2Check);
	}

	public static List<String> fileList2PathList(final List<File> fileList) {
		List<String> pathList = new ArrayList<String>();
		for (File file1 : fileList) {
			pathList.add(file1.getAbsolutePath());
		}
		return pathList;
	}

	public static String getCurrentDirPath() {
		File currentDir = new File(".");
		String currentDirPath = currentDir.getAbsolutePath();
		logger.info("currentDirPath: " + currentDirPath);
		return currentDirPath;
	}

	public static List<String> getFileListWithInNameRange(final String folderPath, final String rangeFilter) {
		List<String> files2Run = new ArrayList<String>();
		String[] rangeArray = rangeFilter.split(OzConstants.HYPHEN);
		String lowRange = rangeArray[0];
		String highRange = rangeArray[1];
		File[] files = FolderUtils.getFilesInFolder(folderPath);
		for (File file1 : files) {
			String fileName = file1.getName();
			if (StringUtils.isStringWithinRage(fileName.substring(0, lowRange.length()), lowRange, highRange)) {
				files2Run.add(fileName);
			}
		}
		logger.info("\nrange: " + rangeFilter + "  \nselected files:\n" + ListUtils.getAsDelimitedString(files2Run));
		return files2Run;
	}

	public static File[] getFilesInFolder(final String folderPath, final FileFilter fileFilter, final Level... logLevels) {
		Level logLevel = VarArgsUtils.getMyArg(Level.FINEST, logLevels);
		File folderFile = new File(folderPath);
		File[] folderContents = folderFile.listFiles(fileFilter);
		logger.log(logLevel, String.valueOf(folderContents.length) + " files found in " + folderPath);
		if (folderContents.length > 0) {
			String header = "files in folder " + folderPath + SystemUtils.LINE_SEPARATOR;
			ArrayUtils.printArray(folderContents, SystemUtils.LINE_SEPARATOR, header, logLevel);
		}
		return folderContents;
	}

	public static String[] getFilesPathsInFolder(final String folderPath, final FileFilter fileFilter, final Level... logLevels) {
		File[] filesArray = getFilesInFolder(folderPath, fileFilter, logLevels);
		String[] pathsArray = new String[filesArray.length];
		for (int i = 0; i < filesArray.length; i++) {
			pathsArray[i] = filesArray[i].getAbsolutePath();
		}
		return pathsArray;
	}

	public static File[] getFilesInFolder(final String folderPath, final Level... levels) {
		return getFilesInFolder(folderPath, new FilefilterIsFile(), levels);
	}

	public static File[] getFoldersInFolder(final String folderPath, final Level... levelParameter) {
		return getFilesInFolder(folderPath, new FileFilterIsDirectory(), levelParameter);
	}

	public static final File getLatestFile(final File folderFile) {
		logger.info("folder: " + folderFile.getAbsolutePath());
		File latestFile = null;
		long latestFileTimeStamp = 0;
		if (folderFile.exists() && folderFile.isDirectory()) {
			File[] filesArray = folderFile.listFiles();
			if (filesArray != null) {
				for (File file1 : filesArray) {
					logger.info("loop: " + file1.getAbsolutePath());
					if (file1.isFile() && file1.lastModified() > latestFileTimeStamp) {
						latestFileTimeStamp = file1.lastModified();
						latestFile = file1;
					}
				}
			}
		}
		return latestFile;
	}

	public static List<File> getRecursivelyAllFiles(final File rootFolder) {
		ArrayList<File> fileList = new ArrayList<File>();
		List<File> subfoldersList = getRecursivelyAllSubFolders(rootFolder);
		if (subfoldersList != null) {
			subfoldersList = new ArrayList<File>(subfoldersList);
			FileFilter fileFilter = new FilefilterIsFile();
			File[] files = rootFolder.listFiles(fileFilter);
			fileList = new ArrayList<File>(Arrays.asList(files));
			for (File folder1 : subfoldersList) {
				files = folder1.listFiles(fileFilter);
				fileList.addAll(Arrays.asList(files));
			}
		}
		return fileList;
	}

	public static List<File> getRecursivelyAllFiles(final String rootFolderPath) {
		return getRecursivelyAllFiles(new File(rootFolderPath));
	}

	public static List<File> getRecursivelyAllFilesAndSubFolders(final File rootFolder) {
		List<File> folderContents = Arrays.asList(rootFolder.listFiles());
		folderContents = new ArrayList<File>(folderContents);
		List<File> deepSubFoldersContents = new ArrayList<File>();
		for (File subdir : folderContents) {
			logger.finest(subdir.getAbsolutePath());
			if (subdir.isDirectory()) {
				deepSubFoldersContents.addAll(getRecursivelyAllFilesAndSubFolders(subdir));
			}
		}
		folderContents.addAll(deepSubFoldersContents);
		return folderContents;
	}

	public static List<File> getRecursivelyAllFilesAndSubFolders(final String rootFolderPath) {
		return getRecursivelyAllFilesAndSubFolders(new File(rootFolderPath));
	}

	public static List<File> getRecursivelyAllSubFolders(final File rootFolder) {
		if (rootFolder.isDirectory()) {
			File[] subFoldersArray = rootFolder.listFiles(new FileFilterIsDirectory());
			List<File> subFoldersList = Arrays.asList(subFoldersArray);
			subFoldersList = new ArrayList<File>(subFoldersList);
			List<File> deepSubFoldersList = new ArrayList<File>();
			for (File subdir : subFoldersList) {
				logger.finest(subdir.getAbsolutePath());
				deepSubFoldersList.addAll(getRecursivelyAllSubFolders(subdir));
			}
			subFoldersList.addAll(deepSubFoldersList);
			return subFoldersList;
		} else {
			return null;
		}
	}

	public static List<File> getRecursivelyAllSubFolders(final String rootFolderPath) {
		return getRecursivelyAllSubFolders(new File(rootFolderPath));
	}

	public static List<String> getRecursivelyAllSubFoldersPaths(final String rootFolderPath) {
		List<File> subfoldersList = getRecursivelyAllSubFolders(new File(rootFolderPath));
		ArrayList<String> subfoldersPathsList = new ArrayList<String>();
		if (subfoldersList != null) {
			for (File file1 : subfoldersList) {
				subfoldersPathsList.add(file1.getAbsolutePath());
			}
		}
		return subfoldersPathsList;
	}

	public static String getSingleFilePathInFolder(final String folderPath, final String... regExpressions) {
		File[] files = null;
		File folder = new File(folderPath);
		String filePath = null;
		String regExpression = null;
		if (regExpressions != null && regExpressions.length == 1) {
			regExpression = regExpressions[0];
		}
		if (regExpression != null) {
			FileFilterIsFileAndRegExpression fileFilterIsFileAndRegExpression = new FileFilterIsFileAndRegExpression(regExpression);
			files = folder.listFiles(fileFilterIsFileAndRegExpression);
		} else {
			files = FolderUtils.getFilesInFolder(folderPath);
		}
		if (files.length == OzConstants.INT_1) {
			filePath = PathUtils.getCanonicalPath(files[0]);
		} else {
			logger.warning("regExpression: " + regExpression + "   " + String.valueOf(files.length) + " files in folder. null is returned !");
		}
		return filePath;
	}

	public static final boolean isEmpty(final File folder, final String... regExps) {
		String regExp = VarArgsUtils.getMyArg(null, regExps);
		List<File> fileList = getRecursivelyAllFiles(folder);
		boolean returnCode = fileList.size() == 0;
		if (regExp != null) {
			returnCode = true;
			for (File file1 : fileList) {
				logger.finest("name: " + file1.getName() + " regexp: " + regExp);
				if (file1.getName().matches(regExp)) {
					returnCode = false;
					break;
				}
			}
		}
		return returnCode;
	}

	public static final boolean isEmpty(final String folderPath, final String... regExps) {
		File file = new File(folderPath);
		return isEmpty(file, regExps);
	}

	public static boolean mkdirIfNoExists(final String folderPath, final Boolean... verboseP) {
		boolean verbose = VarArgsUtils.getMyArg(Boolean.FALSE, verboseP);
		boolean rc = false;
		File folderFile = new File(folderPath);
		if (!folderFile.exists()) {
			rc = folderFile.mkdir();
			logger.info(StringUtils.concat("created folder ", folderPath, ". return code: ", String.valueOf(rc)));
		} else {
			if (verbose) {
				logger.info(StringUtils.concat(folderPath, " already exists."));
			}
		}
		return rc;
	}

	public static boolean moveFolderContents(final File sourceFolderFile, final File targetFolderFile, final FileFilter fileFilter,
			final boolean... createTargetFolderParam) {
		boolean returnCode = true;
		boolean createTargetFolder = createTargetFolderParam.length > 0 && createTargetFolderParam[0];
		if (createTargetFolder) {
			targetFolderFile.mkdirs();
		}
		if (!sourceFolderFile.exists()) {
			logger.warning(sourceFolderFile.getAbsolutePath() + " does not exist.");
		} else {
			try {
				File[] folderContens = sourceFolderFile.listFiles(fileFilter);
				ArrayUtils.printArray(folderContens, "\n",
						sourceFolderFile.getAbsolutePath() + " has " + String.valueOf(folderContens.length) + " files/folders:\n");
				for (File file2Move : folderContens) {
					org.apache.commons.io.FileUtils.moveToDirectory(file2Move, targetFolderFile, createTargetFolder);
				}
			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
				returnCode = false;
			}
		}
		return returnCode;
	}

	public static boolean moveFolderContents(final String sourceFolder, final String targetFolder, final FileFilter fileFilter,
			boolean... createTargetFolderParam) {
		File sourceFolderFile = new File(sourceFolder);
		File targetFolderFile = new File(targetFolder);
		return moveFolderContents(sourceFolderFile, targetFolderFile, fileFilter, createTargetFolderParam);
	}

	private String myFolder;

	private int depth;

	private ArrayList<File> filesAndDirectoriesArrayList;

	/*
	 * compareFolders
	 */
	private final boolean compareFolders(final File dir1, final File dir2, final boolean compareContents) {
		FolderUtils folderUtils = new FolderUtils();
		File[] dirArray1 = folderUtils.getAllFilesAndFoldersInFolder(dir1);
		File[] dirArray2 = folderUtils.getAllFilesAndFoldersInFolder(dir2);
		logger.info("Folder " + dir1.getAbsolutePath() + " has " + String.valueOf(dirArray1.length) + " files and folders");
		logger.info("Folder " + dir2.getAbsolutePath() + " has " + String.valueOf(dirArray2.length) + " files and folders");
		int rootLength1 = dir1.getAbsolutePath().length();
		int rootLength2 = dir2.getAbsolutePath().length();
		File file1, file2;
		String absPath1, absPath2;
		boolean returnCode = dirArray1.length == dirArray2.length;
		for (int i = 0; returnCode && i < dirArray1.length; i++) {
			file1 = dirArray1[i];
			file2 = dirArray2[i];
			absPath1 = file1.getAbsolutePath();
			absPath2 = file2.getAbsolutePath();
			returnCode = absPath1.substring(rootLength1).equals(absPath2.substring(rootLength2)) && (file1.isFile() == file2.isFile());
			if (!returnCode) {
				logger.info("Names do not match: " + absPath1 + " , " + absPath2);
			}
			if (returnCode && file1.isFile() && file2.isFile()) {
				if (!compareContents) {
					returnCode = FileUtils.performLengthAndLastModifiedComparison(file1, file2);
				} else {
					String type = PathUtils.getFileExtension(file1);
					if (type.equalsIgnoreCase("jar")) {
						returnCode = ZipFileUtils.compareJarFiles(absPath1, absPath2);
					} else {
						returnCode = FileUtils.performMessageDigestsComparison(absPath1, absPath2, "MD5");
					}
				}
			}
		}
		if (returnCode) {
			logger.info("Folders " + dir1.getAbsolutePath() + ", " + dir2.getAbsolutePath() + " match!");
		} else {
			logger.info("Folders " + dir1.getAbsolutePath() + ", " + dir2.getAbsolutePath() + " do not match!");
		}
		return returnCode;
	}

	/*
	 * getAllFilesAndFolders method
	 */
	private void getAllFilesAndFolders(final File dir) {
		String dirString = dir.getAbsolutePath();
		if (!dirString.startsWith(myFolder)) {
			logger.warning("Folder=" + myFolder + " dirString=" + dirString + "\nBad parameters.\n Processing terminated.");
			System.exit(-1);
		}
		String pathRemainder = dirString.substring(myFolder.length());
		// System.out.println("REM debug: pathRemainder=" + pathRemainder);
		int myDepth = 0;
		char[] depthArray = pathRemainder.toCharArray();
		for (int i = 0; i < depthArray.length; i++) {
			if (depthArray[i] == File.separatorChar) {
				myDepth++;
			}
		}
		int j = 0;
		/*
		 * Make sure input parameter is a directory
		 */
		if (!(dir.isDirectory())) {
			logger.warning(dir.getAbsolutePath() + " is not a directory");
			System.exit(-1);
		}
		File[] children = dir.listFiles();
		for (int i = 0; i < children.length; i++) {
			filesAndDirectoriesArrayList.add(children[i]);
			j++;
			if (children[i].isDirectory() && (depth == 0 || myDepth < depth)) {
				this.getAllFilesAndFolders(children[i]);
			}
		}
		return;
	}

	/*
	 * getAllFilesAndFoldersInFolder
	 */
	public final File[] getAllFilesAndFoldersInFolder(final File dir) {
		return getAllFilesAndFoldersInFolder(dir, 0);
	}

	/*
	 * getAllFilesAndFoldersInFolder method
	 */
	public final File[] getAllFilesAndFoldersInFolder(final File dir, final int depthP) {
		myFolder = dir.getAbsolutePath();
		depth = depthP;
		filesAndDirectoriesArrayList = new ArrayList<File>();
		getAllFilesAndFolders(dir);
		File[] fileArray = new File[0];
		fileArray = filesAndDirectoriesArrayList.toArray(fileArray);
		return fileArray;
	}
}