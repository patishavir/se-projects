package oz.infra.io.test;

import java.util.logging.Logger;

import oz.infra.io.ZipFileUtils;
import oz.infra.logging.jul.JulUtils;

public class ZipFileUtilsTest {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testListZipContents();
		testCompareJarFiles();
	}

	private static void testListZipContents() {
		logger.info(ZipFileUtils.getZipContents(
				"C:\\oj\\projects\\JarContainer\\log4j\\1.2.16\\log4j-1.2.16.jar").toString());
	}

	private static void testCompareJarFiles() {
		String file1Path = "C:\\oj\\projects\\JarContainer\\log4j\\1.2.16\\log4j-1.2.16.jar";
		String file2Path = "C:\\oj\\projects\\JarContainer\\log4j\\bak\\1.2.15\\log4j-1.2.15.jar";
		logger.info(String.valueOf(ZipFileUtils.compareJarFiles(file1Path, file2Path)));
		file2Path = "C:\\temp\\log4j-1.2.16.jar";
		logger.info(String.valueOf(ZipFileUtils.compareJarFiles(file1Path, file2Path)));
	}
}
