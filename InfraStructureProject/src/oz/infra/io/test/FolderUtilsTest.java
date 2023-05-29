package oz.infra.io.test;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.filefilter.FileFilterIsFileAndRegExpression;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.string.StringUtils;

public class FolderUtilsTest {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// FolderUtils.addPrrfix2FileNames("C:\\JFST\\args\\images\\changing");
		// ListUtils.print(FolderUtils.getRecursivelyAllSubFolders(new File(
		// "c:\\oj\\projects\\JarContainer")), Level.INFO);
		// testMoveFolderContents();
		// testGetFilesInFolder();
		// testGetFoldersInFolder();
		// testIsEmpty("c:\\temp\\");
		// testIsEmpty("c:\\temp\\emp");
		// testGetRecursivelyAllSubFolders();
		// testGetRecursivelyAllFilesAndSubFolders();
		// testGetRecursivelyAllFiles();
		// testDeleteFolderRecursively();
		// testIsEmpty();
		// testGetFileListWithInNameRange();
		// testGetSingleFilePathInFolder();
		testDeleteFolder("C:\\temp\\cleanupTest_222");
		testDeleteFolder("C:\\temp\\cleanupTest_2\\1.txt");
		testDeleteFolder("C:\\temp\\cleanupTest_222");
		testDeleteFolder("C:/temp/cleanupTest_2");
	}

	private static void testDeleteFolder(final String folder2DeletePath) {
		File file2Delete = new File(folder2DeletePath);
		FolderUtils.deleteFolder(file2Delete);
	}

	private static void testDeleteFolderRecursively() {
		logger.info(
				String.valueOf(FolderUtils.deleteFolderRecursively("C:/temp/workDir/BTTRuntimeMonitor.war.TMP/dojo")));
		logger.info(String.valueOf(FolderUtils.deleteFolderRecursively("C:/temp/workDir/BTTRuntimeMonitor.war.TMP/")));
		logger.info(String
				.valueOf(FolderUtils.deleteFolderRecursively("C:\\temp\\workDir\\MatafServer.war.TMP\\dse\\server")));
		logger.info(String.valueOf(FolderUtils.deleteFolderRecursively("C:\\temp")));
	}

	private static void testGetFileListWithInNameRange() {
		FolderUtils.getFileListWithInNameRange("C:/temp/New folder", "dbchanges_52806-dbchanges_52821");
	}

	private static void testGetFilesInFolder() {
		File[] fileArray = null;
		logger.info(PrintUtils.getSeparatorLine("default log level"));
		fileArray = FolderUtils.getFilesInFolder("c:\\temp");
		ArrayUtils.printArray(fileArray);
		logger.info(PrintUtils.getSeparatorLine("log level - info"));
		fileArray = FolderUtils.getFilesInFolder("c:\\temp", Level.INFO);

		//
		logger.info(PrintUtils.getSeparatorLine("filter jpg + log level - info"));
		fileArray = FolderUtils.getFilesInFolder("c:\\temp", new FileFilterIsFileAndRegExpression("\\S+.jpg"),
				Level.INFO);
		logger.info(PrintUtils.getSeparatorLine("filter log + log level - info"));
		fileArray = FolderUtils.getFilesInFolder("c:\\temp", new FileFilterIsFileAndRegExpression("\\S+.log", false),
				Level.INFO);

	}

	private static void testGetFoldersInFolder() {
		File[] folders = FolderUtils.getFoldersInFolder("c:\\temp", Level.INFO);
		// ArrayUtils.printArray(folders, "\n", "folders: ");
	}

	private static void testGetRecursivelyAllFiles() {
		List<File> fileList = FolderUtils.getRecursivelyAllFiles("c:\\temp\\testyyy");
		ListUtils.getAsTitledDelimitedString(fileList, "files in folder\n", Level.INFO, OzConstants.LINE_FEED);
	}

	private static void testGetRecursivelyAllFilesAndSubFolders() {
		List<File> fileList = FolderUtils.getRecursivelyAllFilesAndSubFolders("c:\\temp\\test");
		ListUtils.getAsTitledDelimitedString(fileList, "folder contents\n", Level.INFO, OzConstants.LINE_FEED);
	}

	private static void testGetRecursivelyAllSubFolders() {
		// List<File> fileList = FolderUtils.getRecursivelyAllSubFolders(new
		// File("c:\\temp\\f"));
		List<File> subFolderList = FolderUtils.getRecursivelyAllSubFolders("");
		logger.info(StringUtils.concat(String.valueOf(subFolderList.size()), " sub folders found."));
		ListUtils.getAsDelimitedString(subFolderList, Level.INFO);

	}

	private static void testGetSingleFilePathInFolder() {
		String filePath1 = FolderUtils.getSingleFilePathInFolder("C:/temp/fonfon");
		logger.info("filePath1: " + filePath1);
		String filePath2 = FolderUtils.getSingleFilePathInFolder("C:/temp/fonfon", RegexpUtils.REGEXP_ZIP_FILE);
		logger.info("filePath2: " + filePath2);
	}

	private static void testIsEmpty() {
		testIsEmpty1("C:\\temp\\zvl0");
		testIsEmpty1("C:\\temp\\zvl1");
		testIsEmpty1("C:\\temp\\zvl2");
		testIsEmpty1("C:\\temp\\zvl3");
		testIsEmpty1("C:\\temp\\zvl44444443");
	}

	private static void testIsEmpty1(final String folderPath) {
		logger.info(folderPath + (FolderUtils.isEmpty(new File(folderPath)) ? " is empty." : " is not empty."));
		logger.info(folderPath + (FolderUtils.isEmpty(new File(folderPath), RegexpUtils.REGEXP_EAR_FILE) ? " is empty."
				: " is not empty."));
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
	}

	private static void testMoveFolderContents() {
		String sourceFolder = "c:\\temp\\source";
		String folder2move = "c:\\temp\\source\\folder2move\\subfolder";
		File folder2moveFile = new File(folder2move);
		folder2moveFile.mkdirs();
		FileUtils.writeFile(folder2move + "\\file1.txt", "file1.txt");
		String targetFolder = "c:\\temp\\target";
		FolderUtils.moveFolderContents(sourceFolder, targetFolder, null, true);
	}
}
