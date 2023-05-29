package oz.infra.io.test;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.io.FileDirectoryEnum;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.LineProcessor;
import oz.infra.logging.VerbosityLevel;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class FileUtilsTest implements LineProcessor {
	private static final String TEST_FILE_PATH = "C:\\oj\\projects\\InfraStructureProject\\files\\FileUtils.txt";
	private static int recordCount = 0;
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// testTextFileProcessor();
		// testReadTextFile();
		// testReadTextFileFromClassPath();
		// testReadLastBytes();
		// testDeleteFolderContents();
		// testCopyFile(args);
		// testRenameFile(args);
		// testTerminateIfFileDoesNotExist();
		// testTerminateIfFolderDoesNotExist();
		// testReadTextFileWithEncoding(
		// "C:\\oj\\projects\\utilsProject\\args\\autoDeploy\\distributionlist.txt",
		// OzConstants.WIN1255_ENCODING);
		// testRenameTo();
		// testGetExisting();
		// testMoveFile();
		// testReverseFile();
		// testDos2Unix();
		// testMkdirIfNoExists();
		// testReadFileFromClassPath(FileUtilsTest.class, "files/test1.txt");
		// testReadFileFromClassPath(FileUtilsTest.class,
		// "/oz/infra/io/test/files/test1.txt");
		testIsFileExists("C:\\Windows\\notepad.exe");
		testIsFileExists("C:\\Windows\\notepad.ZZZZ");
		testIsFileExists("C:\\Windows");
	}

	private static void testCopyFile(final String[] args) {
		// String inFilePath = "c:\\temp\\BGInfo.bmp";
		// String outFilePath = "c:\\temp\\BGInfo1.bmp";
		String inFilePath = "/snifit/scripts/java/test/excelUtils/data/1.csv";
		String outFilePath = "/snifit/scripts/java/test/excelUtils/data/2.csv";
		FileUtils.copyFile(inFilePath, outFilePath);
	}

	public static void testDeleteFolderContents() {
		boolean rc = FileUtils.deleteFolderContents(new File("c:\\temp"), ".*\\.jpg$");
		logger.info(String.valueOf(rc));
	}

	private static void testDos2Unix() {
		FileUtils.dos2unix("c:\\temp\\in.txt", "c:\\temp\\out.txt");
	}

	private static void testGetExisting() {
		String[] pathsArray = { "c:\\temp\\kkkk", "c:\\temp\\ttt", "C:\\temp\\XMLOnlineGW\\META-INF",
				"C:\\temp\\XMLOnlineGW\\META-INF\\index.html", "C:\\temp\\XMLOnlineGW\\index.html",
				"C:/temp/XMLOnlineGW/XMLOnlineGW.WEB/index.html" };
		logger.info(FileUtils.getExisting(pathsArray, FileDirectoryEnum.FILE));
		logger.info(FileUtils.getExisting(pathsArray, FileDirectoryEnum.DIRECTORY));
		logger.info(FileUtils.getExisting(pathsArray, FileDirectoryEnum.ANY));
	}

	private static void testIsFileExists(final String filePath) {
		logger.info("\n\n\nno param");
		FileUtils.isFileExists(filePath);
		logger.info("none");
		FileUtils.isFileExists(filePath, VerbosityLevel.None);
		logger.info("error");
		FileUtils.isFileExists(filePath, VerbosityLevel.Error);
		logger.info("full");
		FileUtils.isFileExists(filePath, VerbosityLevel.Full);

	}

	private static void testMkdirIfNoExists() {
		String folderPath = "c:\\temp\\zvl3";
		FolderUtils.mkdirIfNoExists(folderPath);
		FolderUtils.mkdirIfNoExists(folderPath);
	}

	private static void testMoveFile() {
		String sourceFilePath = "c:\\temp\\1.txt";
		File sourceFile = new File(sourceFilePath);
		FileUtils.writeFile(sourceFile, "contentsString");
		String targetFilePath = "c:\\temp\\folder1\\2.txt2";
		File targerFile = new File(targetFilePath);
		logger.info(String.valueOf(FileUtils.moveFile(sourceFilePath, targetFilePath)));
		if (sourceFile.isFile()) {
			logger.warning(StringUtils.concat("source file ", sourceFilePath, " still exists !"));
		}
		if (!targerFile.isFile()) {
			logger.warning(StringUtils.concat("target file ", targetFilePath, " does not exists !"));
		}
	}

	private static void testReadFileFromClassPath(final Class clazz, final String filePath) {
		String contents = FileUtils.readTextFileFromClassPath(clazz, filePath);
		logger.info(contents);
		String contents2 = FileUtils.readTextFileFromClassPath(filePath);
		logger.info(contents2);
	}

	private static void testReadLastBytes() {
		byte[] bytes = null;
		// bytes = FileUtils.readLastBytes(TEST_FILE_PATH, 100);
		// logger.info(new String(bytes));
		bytes = FileUtils.readLastBytes(TEST_FILE_PATH, 4);
		logger.info(new String(bytes));
		bytes = FileUtils.readLastBytes(TEST_FILE_PATH, 20);
		logger.info(new String(bytes));
		bytes = FileUtils.readLastBytes(TEST_FILE_PATH, 120);
		logger.info(new String(bytes));
	}

	private static void testReadTextFile() {
		String filePath = TEST_FILE_PATH;
		String string = FileUtils.readTextFile(filePath, FileUtils.WINDOWS_1255);
		logger.info(string);
	}

	private static void testReadTextFileFromClassPath() {
		String testFilePath = FileUtilsTest.class.getName();
		testFilePath = testFilePath.replaceAll("\\.", "/") + ".java";
		logger.info(testFilePath);
		// FileUtils.readTextFileFromClassPath(testFilePath);
		logger.info(FileUtils.readTextFileFromClassPath("oz/infra/io/test/zvl.txt"));
		// logger.info(FileUtils.readTextFileFromClassPath("oz/infra/versionInfo.txt"));

	}

	private static void testReadTextFileWithEncoding(final String filePath, final String charsetString) {
		String contents = FileUtils.readTextFileWithEncoding(filePath, charsetString);
		logger.info(contents);
		String[] stringArray = FileUtils.readTextFileWithEncoding2Array(filePath, charsetString);
		logger.info(ArrayUtils.printArray(stringArray).toString());
		FileUtils.writeTextFileWithEncoding("c:\\temp\\z.txt", charsetString, contents);
	}

	private static void testRenameFile(final String[] args) {
		FileUtils.renameFile("c:\\temp\\0", "c:\\temp\\1");
		FileUtils.renameFile("c:\\temp\\keep", "c:\\temp\\keep");
		FileUtils.renameFolder("c:\\temp\\keep1", "c:\\temp\\keep");
		FileUtils.renameFolder("c:\\temp\\keep", "c:\\temp\\keep1");
		FileUtils.renameFolder(null, "c:\\temp\\keep1");
	}

	private static void testRenameTo() {
		String source = "c:\\temp\\run\\OZ77";
		String target = "c:\\temp\\history\\";
		File sourceFile = new File(source);
		File targetFile = new File(target);
		boolean rc = sourceFile.renameTo(targetFile);
		logger.info("rc: " + String.valueOf(rc));
		FileUtils.moveUsingWindowsShell(source, target);
	}

	private static void testReverseFile() {
		FileUtils.reverseFile("c:/temp/rmelem_d.bat", "c:/temp/rmelem_dDDD.bat");
	}

	private static void testterminateIfFileDoesNotExist() {

	}

	private static void testTerminateIfFileDoesNotExist() {
		FileUtils.terminateIfFileDoesNotExist(null, "mamamamama  bla:\n");
		// FileUtils.terminateIfFileDoesNotExist("c:\\temp",
		// "mamamamama bla:\n");
		FileUtils.terminateIfFileDoesNotExist("c:\\temp\\pppppppppp");
		testTerminateIfFileDoesNotExist1("c:\\temp\\111.txt");
		testTerminateIfFileDoesNotExist1("c:\\temp\\111.txtqqq");
		testTerminateIfFileDoesNotExist1("c:\\temp\\");
	}

	private static void testTerminateIfFileDoesNotExist1(final String filePath) {
		logger.info(StringUtils.concat("testing ", filePath));
		FileUtils.terminateIfFileDoesNotExist(filePath);
		logger.info(StringUtils.concat(filePath, " test has completed"));

	}

	private static void testTerminateIfFolderDoesNotExist() {
		testTerminateIfFolderDoesNotExist1("c:\\temp\\");
		// testTerminateIfFolderDoesNotExist1("c:\\temp\\111.txt");
		testTerminateIfFolderDoesNotExist1("c:\\temp\\111.txtqqq");
	}

	private static void testTerminateIfFolderDoesNotExist1(final String filePath) {
		logger.info(StringUtils.concat("testing ", filePath));
		FolderUtils.dieUnlessFolderExists(filePath);
		logger.info(StringUtils.concat(filePath, " test has completed"));

	}

	private static void testTextFileProcessor() {
		StopWatch stopWatch = new StopWatch();
		File file = new File("C:\\temp\\access_2012-10-14-06-00");
		FileUtilsTest fileUtilsTest = new FileUtilsTest();
		FileUtils.processTextFileLines(file, fileUtilsTest);
		stopWatch.logElapsedTimeMessage();
	}

	public void processEOF(final Object object) {
		logger.info(String.valueOf(recordCount));
	}

	public void processLine(final String line) {
		recordCount++;
	}
}
