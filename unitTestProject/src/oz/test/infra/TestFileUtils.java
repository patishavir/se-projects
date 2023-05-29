package oz.test.infra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.logging.Logger;

import org.junit.Test;

import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.io.ZipFileUtils;
import oz.infra.system.SystemUtils;

public class TestFileUtils {

	/**
	 * @param args
	 */
	private static Logger logger = Logger.getLogger(TestFileUtils.class.getName());

	private static final String path1 = "c:\\temp\\file1.txt";
	private static final String path2 = "c:\\temp\\file2.txt";
	private static final String path3 = "c:\\temp\\file3.txt";

	private static File file1 = new File(path1);
	private static File file2 = new File(path2);
	private static File file3 = new File(path3);

	private static final String lines123 = "line1 .. " + SystemUtils.LINE_SEPARATOR
			+ "line2 ... " + SystemUtils.LINE_SEPARATOR + "line3 .... ";

	private static final String lines456 = "line4 .." + SystemUtils.LINE_SEPARATOR + "line5 ... "
			+ SystemUtils.LINE_SEPARATOR + "line6 .... ";

	private static final String lines789 = "line7 .." + SystemUtils.LINE_SEPARATOR + "line8 ... "
			+ SystemUtils.LINE_SEPARATOR + "line9 .... ";

	@Test
	public final void testcompareJarFiles() {

		String folder1Path = "c:\\temp\\test\\infra1";
		String folder2Path = "c:\\temp\\test\\infra2";
		File folder1File = new File(folder1Path);
		File folder2File = new File(folder2Path);
		folder1File.mkdirs();
		folder2File.mkdirs();

		try {
			String commonsPath = "..\\JarContainer\\commons";
			String commonsEmailJar = "commons-email-1.0.jar";
			File commonsEmailFile = new File(commonsPath + File.separator + commonsEmailJar);
			String fi1e1Path = folder1Path + File.separator + commonsEmailJar;
			String fi1e2Path = folder2Path + File.separator + commonsEmailJar;
			FileUtils.copyFile(commonsEmailFile, new File(fi1e1Path));
			FileUtils.copyFile(commonsEmailFile, new File(fi1e2Path));

			logger.info("Comparing " + fi1e1Path + " to " + fi1e2Path);
			assertTrue(ZipFileUtils.compareJarFiles(fi1e1Path, fi1e2Path));

		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	@Test
	public final void testGetFileType() {
		File file = new File("c:\\temp\\jdir\\test\\jarfile.jar");
		assertEquals(PathUtils.getFileExtension(file), "jar");
		file = new File("c:\\temp\\jdir\\test\\jarfile.bar.jar");
		assertEquals(PathUtils.getFileExtension(file), "jar");
		file = new File("c:\\temp\\jdir\\test\\jarfile-bar-jar");
		assertEquals(PathUtils.getFileExtension(file), "");

	}

	@Test
	public final void readWriteTextFile() {
		String contents1 = lines123;
		FileUtils.writeFile(path1, contents1);
		String contents2 = FileUtils.readTextFile(path1);
		FileUtils.writeFile(path2, contents2);
		String contents3 = FileUtils.readTextFile(path2);
		assertEquals(lines123, contents2);
		assertEquals(contents2, contents3);
		File file1 = new File(path1);
		File file2 = new File(path2);
		assertTrue(file1.length() == file2.length());
		assertTrue(FileUtils.performFullBinaryComparison(file1, file2));
	}

	@Test
	public void copyTextFile() {
		String contents1 = lines123 + SystemUtils.LINE_SEPARATOR + lines456
				+ SystemUtils.LINE_SEPARATOR + lines789;
		FileUtils.writeFile(path1, contents1);
		File inFile = new File(path1);
		File outFile = new File(path2);
		FileUtils.copyTextFile(inFile, outFile, 4, 6);
		String file2String = FileUtils.readTextFile(path2);
		logger.info("lines456.length(): " + lines456.length() + "file2String.length(): "
				+ file2String.length());
		logger.finest(lines456);
		logger.finest(file2String);
		assertTrue(lines456.equals(file2String));

	}

	@Test
	public void replaceAStringInTextFile() {
		String contents1 = lines123 + SystemUtils.LINE_SEPARATOR + lines456
				+ SystemUtils.LINE_SEPARATOR + lines789;
		FileUtils.writeFile(path1, contents1);
		String str1 = "line";
		String str2 = "qwer";
		FileUtils.replaceStringsInTextFile(path1, path2, str1, str2);
		assertTrue(!FileUtils.performFullBinaryComparison(file1, file2));
		FileUtils.replaceStringsInTextFile(path2, path3, str2, str1);
		assertTrue(FileUtils.performMessageDigestsComparison(path1, path3, "MD5"));
	}

	@Test
	public void appendFile() {
		String contents1 = lines123 + SystemUtils.LINE_SEPARATOR + lines456
				+ SystemUtils.LINE_SEPARATOR;
		FileUtils.writeFile(path1, contents1);
		FileUtils.appendFile(path1, lines789);
		String contents2 = FileUtils.readTextFile(path1);
		assertEquals(contents1 + lines789, contents2);
	}

	@Test
	public void copyFile() {
		String contents1 = lines123 + SystemUtils.LINE_SEPARATOR + lines456
				+ SystemUtils.LINE_SEPARATOR;
		FileUtils.writeFile(path1, contents1 + lines789);
		FileUtils.copyFile(file1, file2);
		String contents2 = FileUtils.readTextFile(path1);
		assertEquals(contents1 + lines789, contents2);
	}

	@Test
	public void replaceBytesInFile() {
		String contents1 = lines123 + SystemUtils.LINE_SEPARATOR + lines456
				+ SystemUtils.LINE_SEPARATOR + lines789;
		FileUtils.writeFile(path1, contents1);
		byte byte1 = (byte) (32);
		byte byte2 = (byte) (0);
		FileUtils.replaceBytesInFile(path1, path2, byte1, byte2);
		assertTrue(!FileUtils.performFullBinaryComparison(file1, file2));
		FileUtils.replaceBytesInFile(path2, path3, byte2, byte1);
		assertTrue(FileUtils.performMessageDigestsComparison(path1, path3, "MD5"));

	}

	@Test
	public void getNumberOfLines() {
		String contents1 = lines123 + SystemUtils.LINE_SEPARATOR + lines456
				+ SystemUtils.LINE_SEPARATOR + lines789;
		FileUtils.writeFile(path1, contents1);
		int numberOfLines = FileUtils.getNumberOfLines(new File(path1));
		logger.info("Number of line in " + path1 + " is: " + String.valueOf(numberOfLines));
		assertTrue(numberOfLines == 9);
	}

}
