package oz.infra.zip.test;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.zip.ZipUtils;

public class TestZipUtils {
	private static Logger logger = JulUtils.getLogger();

	private static void addString2ExistingZip() {
		String existingZipFile = "c:\\temp\\1110.zip";
		String[] files2Add = { "qwertyuiop01\nqwertyuiop02" };
		String[] entryPaths = { "1234qwe02r" };
		ZipUtils.addStringsToExistingZip(new File(existingZipFile), files2Add, entryPaths);
	}

	private static void logZipFileContents() {
		ZipUtils.logZipFileContents("c:/temp/13.zip");
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
		ZipUtils.logZipFileContents(new File("c:/temp/13.zip"));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testZipFile();
		// testZipFiles();
		zipFolder2();
		// testExtractFile();
		// updateZip();
		// testZipFolder();
		// logZipFileContents();
		// testAddFilesToExistingZip();
		// addString2ExistingZip();
		// testExtractAllFile();
	}

	private static void testAddFilesToExistingZip() {
		File[] files2Add = { new File("c:\\temp\\17\\1.txt"), new File("c:\\temp\\17\\2.txt") };
		File existingZipFile = new File("c:\\temp\\17\\17.zip");
		ZipUtils.addFilesToExistingZip(existingZipFile, files2Add, "c:\\temp\\17");
		ZipUtils.logZipFileContents(existingZipFile.getAbsolutePath());
	}

	private static void testExtractAllFile() {
		String zipFilePath = "c:\\temp\\b2.zip";
		String outFilePath = "c:\\temp\\outfolder6789";
		ZipUtils.extractAllFiles(zipFilePath, outFilePath);
	}

	private static void testExtractFile() {
		String zipFilePath = "c:\\temp\\b2.zip";
		String file2Exatract = "b2/logs4Scp.tar.gz";
		String targetFilePath = "c:\\temp\\logs4Scp.tar.target.gzip";
		ZipUtils.extractFile(zipFilePath, file2Exatract, targetFilePath);
	}

	private static void testZipFile() {
		String filePath = "c:\\temp\\1.txt";
		FileUtils.writeFile(filePath, "yyyuuuiiioooppp\n123456");
		ZipUtils.zipFile2Root(filePath);
		ZipUtils.zipFile2Root(filePath, "c:\\temp\\out.zoop");
	}

	private static void testZipFiles() {
		String[] filePathes = { "c:\\temp\\1.txt", "c:\\temp\\2.txt" };
		ZipUtils.zipFiles("c:\\temp\\zips.zip", filePathes);
	}

	private static void testZipFolder() {
		String outputZipFilePath = "C:/temp/17.zip";
		String folder2Zip = "C:\\temp\\17";
		String zipEntryBase = "C:/temp/17";
		ZipUtils.zipFolder(outputZipFilePath, folder2Zip, zipEntryBase);
		ZipUtils.logZipFileContents(outputZipFilePath);
	}

	private static void updateZip() {
		String zipFilePath = "c:\\temp\\test\\test.zip";
		String file2Exatract = "sub/txt.txt";
		String targetFilePath = "c:\\temp\\target.txt";
		ZipUtils.extractFile(zipFilePath, file2Exatract, targetFilePath);
		String contents = FileUtils.readTextFile(targetFilePath);
		contents = contents.concat(contents);
		FileUtils.writeFile(targetFilePath, contents);
		ZipUtils.zipFile2RelativePath(targetFilePath, file2Exatract, zipFilePath);
	}

	private static void zipFolder1() {
		ZipUtils.zipFolder("c:\\temp\\zipfolder.zip", "c:\\temp\\test");
	}

	private static void zipFolder2() {
		ZipUtils.zipFolder("c:\\temp\\zipfolder.zip", "c:\\temp\\test", "c:\\temp\\test");
	}
}
