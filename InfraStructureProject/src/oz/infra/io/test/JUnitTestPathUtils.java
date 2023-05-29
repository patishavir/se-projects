package oz.infra.io.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.logging.Logger;

import org.junit.Test;

import oz.infra.io.PathUtils;
import oz.infra.junit.JunitUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class JUnitTestPathUtils {

	private static Logger logger = JulUtils.getLogger();

	private static void getParentFolderPath1(final String path, final String expectedResult) {
		String actualResult = PathUtils.getParentFolderPath(path);
		String message = StringUtils.concat("path: ", path, " expectedResult: ", expectedResult, " actualResult: ",
				actualResult);
		boolean outcome = false;
		if (actualResult == null) {
			outcome = expectedResult == null;
		} else {
			outcome = actualResult.equals(expectedResult);
		}
		if (outcome) {
			logger.info(JunitUtils.SUCCESS + message);
		} else {
			logger.info(JunitUtils.FAILURE + message);
		}
		assertTrue(outcome);
	}

	private static void testChangeFileType() {
		logger.finest(PathUtils.changeFileExtension("c:\\temp\\oz\\111.sss", "bbbbbbbbbb"));
		logger.finest(PathUtils.changeFileExtension("c:\\temp\\oz\\111.sss", ".bbbbbbbbbb"));
		logger.finest(PathUtils.changeFileExtension("c:\\temp\\oz\\111_sss", "bbbbbbbbbb"));
	}

	@Test
	public void testAdjustPathToCurrentOS() {
		String path1 = "C:\\oj\\projects\\jistProject\\files\\Images\\changing\\gray_blue_east.png";
		String adjsutedPath1 = PathUtils.adjustPathToCurrentOS(path1);
		logger.info(StringUtils.concat("\n", path1, "\n", adjsutedPath1, "\n",
				String.valueOf(path1.equals(adjsutedPath1))));
		assertTrue(path1.equals(adjsutedPath1));
		String path2 = "C:/oj/projects/jistProject/files/Images/changing/gray_blue_east.png";
		String adjsutedPath2 = PathUtils.adjustPathToCurrentOS(path2);
		logger.info(StringUtils.concat("\n", path1, "\n", adjsutedPath2, "\n",
				String.valueOf(path1.equals(adjsutedPath2))));
		assertTrue(path1.equals(adjsutedPath2));

	}

	@Test
	public void testGetCurrentDir() {
		String currentDir = PathUtils.getCurrentDirPath();
		logger.info("Current dir: " + currentDir);
		logger.info(String.valueOf(currentDir.equals("C:\\oj\\projects\\se\\InfraStructureProject\\.")));
		assertTrue(currentDir.equals("C:\\oj\\projects\\se\\InfraStructureProject\\."));
	}

	@Test
	public void testGetFileName() {
		String filePath = "C:\\oj\\projects\\jistProject\\files\\Images\\changing\\gray_blue_east.png";
		File file = new File(filePath);
		String fileName = PathUtils.getNameWithoutExtension(filePath);
		String expectedName = "gray_blue_east";
		assertEquals(fileName, expectedName);
		logger.info("Path: " + filePath + "fileName: " + fileName);
		filePath = "C:\\oj\\projects\\jistProject\\files\\Images\\changing\\gray_blue_east";
		fileName = PathUtils.getNameWithoutExtension(filePath);
		assertEquals(fileName, expectedName);
		logger.info("Path: " + filePath + "fileName: " + fileName);

	}

	@Test
	public void testGetFileName0() {
		String filePath = "C:\\oj\\projects\\jistProject\\files\\Images\\changing\\gray_blue_east";
		File file = new File(filePath);
		String fileName = PathUtils.getNameWithoutExtension(filePath);
		String fileNameF = PathUtils.getFileNameWithoutExtension(file);
		String expectedName = "gray_blue_east";
		assertEquals(fileName, expectedName);
		assertEquals(fileNameF, expectedName);
		logger.info("Path: " + filePath + "fileName: " + fileName);
		filePath = "C:\\oj\\projects\\jistProject\\files\\Images\\changing\\gray_blue_east";
		fileName = PathUtils.getNameWithoutExtension(filePath);
		assertEquals(fileName, expectedName);
		logger.info("Path: " + filePath + "fileName: " + fileName);

	}

	@Test
	public void testGetFileNameAndType() {
		testGetFileNameAndType("c:\\xxx.yyy", "xxx.yyy");
		testGetFileNameAndType("c:/xxx.yyy", "xxx.yyy");
		testGetFileNameAndType("c:\\yyy.zzz\\qqq.ppp", "qqq.ppp");
		testGetFileNameAndType("c:/yyy.zzz/qqq.ppp", "qqq.ppp");
		testGetFileNameAndType("c:\\", "");
	}

	private void testGetFileNameAndType(final String path, final String expected) {
		logger.info(path + "   " + expected);
		assertEquals(PathUtils.getFileNameAndExtension(path), expected);
	}

	@Test
	public void testGetFileType() {
		testGetFileType1("c:\\temp\\1.zzz", "zzz");
		testGetFileType1("c:\\temp.1\\zzz", "");
		testGetFileType1("c:\\temp\\1_zzz_yyy", "");
		testGetFileType1("c:\\temp\\.1_zzz_yyy", "1_zzz_yyy");
		testGetFileType1("1_zzz_yyy", "");
		testGetFileType1(".1_zzz_yyy", "1_zzz_yyy");
	}

	public void testGetFileType1(final String filePath, final String expectedSuffix) {
		String type = PathUtils.getFileExtension(filePath);
		logger.info(StringUtils.concat("filepath: ", filePath, " type: ", type, " expectedSuffix: ", expectedSuffix));
		assertEquals(type, expectedSuffix);
	}

	@Test
	public void testGetFullPath() {
		JunitUtils.startTest();
		testGetFullPath1("c:\\temp\\", "c:\\temppppoo\\", "c:\\temppppoo\\");
		testGetFullPath1("c:\\temp", "f.txt", "c:\\temp\\f.txt");
		testGetFullPath1("c:\\temp\\", "f.txt", "c:\\temp\\f.txt");
		testGetFullPath1("c:\\temp", "\\f.txt", "c:\\temp\\f.txt");
		testGetFullPath1("c:\\temp\\", "/xxx/yyy", "c:\\temp\\/xxx/yyy");
		testGetFullPath1("c:\\temp\\", "\\\\tempppp\\XX", "\\\\tempppp\\XX");
		testGetFullPath1("c:\\temp\\", "\\temp1111\\", "c:\\temp\\temp1111\\");
		testGetFullPath1("c:\\temp\\", "temp1111\\", "c:\\temp\\temp1111\\");
		testGetFullPath1("c:\\temp0000", "\\temp", "c:\\temp0000\\temp");
		testGetFullPath1("c:\\a\\", "\\bbbbbbbtemp", "c:\\a\\bbbbbbbtemp");
		JunitUtils.endTest();
	}

	private void testGetFullPath1(final String... params) {
		String root = params[0];
		String relative = params[1];
		String expectedResult = params[2];
		String actualResult = PathUtils.getFullPath(root, relative);
		boolean outcome = false;
		if (actualResult == null) {
			outcome = expectedResult == null;
		} else {
			outcome = actualResult.equals(expectedResult);
		}
		String message = "root: " + root + " relative: " + relative + " expectedResult:" + expectedResult
				+ " actualResult: " + actualResult;
		if (outcome) {
			logger.info(JunitUtils.SUCCESS + message);
		} else {
			logger.warning(JunitUtils.FAILURE + message);
		}
		assertTrue(outcome);
	}

	@Test
	public void testGetParentFolder() {
		JunitUtils.startTest();
		getParentFolderPath1("c:\\temp\\ooo\\", "c:\\temp");
		getParentFolderPath1("c:\\temp\\ooo", "c:\\temp");
		getParentFolderPath1("c:\\", null);
		// JunitUtils.endTest();
	}

	@Test
	public void testGetRelativePath() {
		assertEquals("blabla", PathUtils.getRelativePath("c:\\temp\\blabla", "c:\\temp"));
		assertEquals("blabla", PathUtils.getRelativePath("c:\\temp\\blabla", "c:\\temp\\"));
		assertEquals("blabla", PathUtils.getRelativePath("c:\\temp\\blabla", "c:\\temp\\"));
		assertFalse("blabla".equals(PathUtils.getRelativePath("c:\\temp\\blabla", "D:\\temp\\")));
	}

	@Test
	public void testIsImageFileType() {
		assertTrue(PathUtils.isImageFileType("gif"));
		assertTrue(PathUtils.isImageFileType("BMP"));
		assertTrue(PathUtils.isImageFileType("PnG"));
		assertFalse(PathUtils.isImageFileType("Xls"));
		assertTrue(PathUtils.isImageFile(new File("c:\\kika\\qqq.png")));
		assertTrue(PathUtils.isImageFile(new File("c:\\temp\\qqq.Gif")));
		assertTrue(PathUtils.isImageFile(new File("c:\\perm\\qqq.PnG")));
		assertTrue(PathUtils.isImageFile(new File("c:\\kika\\qqq.TIFf")));
		assertFalse(PathUtils.isImageFile(new File("c:\\kika\\qqq.FIFf")));
		assertFalse(PathUtils.isImageFile(new File("c:\\kika\\qqq.GI")));
		assertFalse(PathUtils.isImageFile(new File("c:\\kika\\qqq.pngg")));

	}

	@Test
	public void testWhich() {
		assertEquals("C:\\WINDOWS\\system32\\notepad.exe", PathUtils.which("notepad.exe"));
	}
}
