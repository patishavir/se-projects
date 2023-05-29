package oz.test.jdir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;

public class JdirTest {
	private static final Logger logger = Logger.getLogger("oz.jdir.test.JdirTest");
	private String myLeftFolder;
	private String myRightFolder;
	private final String txtFile1Contents = "111\\n222\\n333\\qqq\\nwww\\neee";
	private File leftTxtFile;
	private File rightTxtFile;
	private String leftTxtFileFullPath;
	private String rightTxtFileFullPath;
	private String javaFilename;
	private String javaFileFullPath;
	private final String javaFileContents = "package oz.jdirtest;class JdirTest{}";

	@Before
	public void setupJdirTest() {
		myLeftFolder = "c:\\temp\\jdir\\zvl1";
		myRightFolder = "c:\\temp\\jdir\\zvl2";
		File myLeftFolderFile = new File(myLeftFolder);
		File myRightFolderFile = new File(myRightFolder);
		if (!myLeftFolderFile.exists()) {
			myLeftFolderFile.mkdirs();
		}
		if (!myRightFolderFile.exists()) {
			myRightFolderFile.mkdirs();
		}
		String myLeftFileName = "leftFile.txt";
		String myRightFileName = "rightFile.txt";
		String leftFileFullPath = myLeftFolder + File.separator + myLeftFileName;
		String rightFileFullPath = myRightFolder + File.separator + myRightFileName;
		try {
			new File(leftFileFullPath).createNewFile();
			new File(rightFileFullPath).createNewFile();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		leftTxtFileFullPath = myLeftFolder + File.separator + "jdirtestFile1.txt";
		rightTxtFileFullPath = myRightFolder + File.separator + "jdirtestFile1.txt";
		leftTxtFile = new File(leftTxtFileFullPath);
		rightTxtFile = new File(rightTxtFileFullPath);

	}

	@Test
	public void jdirTest() {
		String myParametersFile = "C:\\oj\\Projects\\jdir2Project\\Files\\jdir2.xml";

		// jdir Print selected files directory info
		String[] myArgs1 = { "sourcedir=" + myLeftFolder, myParametersFile, "loggingLevel=INFO",
				"show=All", "gui=nes", "operation=PrintAllDirInfo",
				"destinationDir=" + myRightFolder };
		oz.jdir.JDirMain.main(myArgs1);
		printSeparator();
		// jdir Print selected files directory info
		String[] myArgs2 = { "sourcedir=" + myLeftFolder, "destinationDir=" + myRightFolder,
				myParametersFile, "loggingLevel=INFO", "show=All", "gui=nes",
				"operation=Print2DirInfo" };
		oz.jdir.JDirMain.main(myArgs2);
		printSeparator();
		// delete myLeftFolder
		String[] myArgs3 = { "sourcedir=" + myLeftFolder, "destinationDir=" + myRightFolder,
				myParametersFile, "loggingLevel=INFO", "show=All", "gui=nes", "operation=Delete" };
		oz.jdir.JDirMain.main(myArgs3);
		printSeparator();
		assertTrue(FolderUtils.isEmpty(new File(myLeftFolder)));
		// Sync folders
		String[] myArgs4 = { "sourcedir=" + myLeftFolder, "destinationDir=" + myRightFolder,
				myParametersFile, "loggingLevel=INFO", "show=All", "gui=nes",
				"operation=Synchronize" };
		oz.jdir.JDirMain.main(myArgs4);
		printSeparator();
		boolean returnCode = FolderUtils.compareFolders(new File(myLeftFolder), new File(
				myRightFolder));
		assertTrue(returnCode);
		// delete myLeftFolder
		String[] myArgs5 = { "sourcedir=" + myLeftFolder, "destinationDir=" + myRightFolder,
				myParametersFile, "loggingLevel=INFO", "show=All", "gui=nes", "operation=Delete" };
		oz.jdir.JDirMain.main(myArgs5);
		printSeparator();
		assertTrue(FolderUtils.isEmpty(new File(myLeftFolder)));
		logger.info("Test 5 completed!");
		// Mirror folders
		String[] myArgs6 = { "sourcedir=" + myRightFolder, "destinationDir=" + myLeftFolder,
				myParametersFile, "loggingLevel=INFO", "show=All", "gui=nes", "operation=Mirror" };
		oz.jdir.JDirMain.main(myArgs6);
		printSeparator();
		boolean returnCode6 = FolderUtils.compareFolders(new File(myLeftFolder), new File(
				myRightFolder));
		assertTrue(returnCode6);
		// Write file

		setFileContents(leftTxtFileFullPath, txtFile1Contents);

		returnCode = FolderUtils.compareFolders(new File(myLeftFolder), new File(myRightFolder));
		assertTrue(!returnCode);
		FileUtils.copyFile(new File(leftTxtFileFullPath), new File(rightTxtFileFullPath));
		assertTrue(FileUtils.performMessageDigestsComparison(leftTxtFileFullPath,
				rightTxtFileFullPath, "MD5"));
		// Compare folders
		returnCode = FolderUtils.compareFolders(new File(myLeftFolder), new File(myRightFolder));
		assertTrue(returnCode);
		logger.info("Test 6 completed ! 66666666666666666666666666666666666666666666666666666666666");
		// replace String
		String string2Find = "qqq";
		String replaceWithString = "yyy";
		String[] myArgs7 = { "sourcedir=" + myRightFolder, "destinationDir=" + myLeftFolder,
				myParametersFile, "loggingLevel=INFO", "show=All", "gui=nes",
				"operation=FindAndReplace", "string2Find=" + string2Find,
				"replaceWithString=" + replaceWithString, "suffixFilter=txt" };
		oz.jdir.JDirMain.main(myArgs7);
		logger.info("Returned from JDirMain");
		assertTrue(!FileUtils.performFullBinaryComparison(leftTxtFile, rightTxtFile));
		String fileContents4Assert = getFileContents(rightTxtFile);
		assertEquals(fileContents4Assert.replaceAll(replaceWithString, string2Find),
				txtFile1Contents);
		logger.info("Test 7 completed ! 77777777777777777777777777777777777777777777777777777777777");

		// replace String

		String[] myArgs8 = { "sourcedir=" + myLeftFolder, "destinationDir=" + myRightFolder,
				myParametersFile, "loggingLevel=INFO", "show=All", "gui=nes",
				"operation=FindAndReplace", "string2Find=" + string2Find,
				"replaceWithString=" + replaceWithString, "suffixFilter=txt" };
		oz.jdir.JDirMain.main(myArgs8);
		logger.info("Returned from JDirMain");
		assertTrue(FileUtils.performFullBinaryComparison(leftTxtFile, rightTxtFile));
		fileContents4Assert = getFileContents(leftTxtFile);
		assertEquals(fileContents4Assert.replaceAll(replaceWithString, string2Find),
				txtFile1Contents);
		logger.info("Test 8 completed ! 88888888888888888888888888888888888888888888888888888888888");
		logger.info("All Done! ***************************************************");
	}

	private static void printSeparator() {
		logger.info("**********************************************************************************************");

	}

	private static void setFileContents(final String fileFullPath, final String contents) {
		try {
			FileUtils.writeFile(new File(fileFullPath), contents);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static String getFileContents(final File currentFile) {
		String currentFileContents = null;
		try {
			currentFileContents = org.apache.commons.io.FileUtils.readFileToString(currentFile);
		} catch (IOException ioex) {
			ioex.printStackTrace();
			logger.warning(ioex.getMessage());

		}
		return currentFileContents;
	}
}