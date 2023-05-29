package oz.infra.io.test;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class TestPathUtils {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// logger.info(PathUtils.getGrandParentFolderName("c:/temp/1/2/3"));
		// logger.info(PathUtils.getGrandParentFolderName(new
		// File("c:/temp/1/2/3")));
		// logger.info(PathUtils.getGrandParentFolderName("c:\\temp\\11111\\2222\\3333\\444"));
		// logger.info(PathUtils.getGrandParentFolderName(new
		// File("c:\\temp\\11111\\2222\\3333\\444")));
		// testGoDownTheFolderTree("c:/temp/01", 1);
		// testGoDownTheFolderTree("c:/temp/01", 2);
		// testGoDownTheFolderTree("c:/temp/01", 3);
		// testGoDownTheFolderTree("c:/temp/01", 4);
		// testGoDownTheFolderTree("c:/temp/01", 5);
		// testAddPaths("/aaa/bbb", "yyy/zzz", "/");
		// testAddPaths("/aaa/bbb", "/yyy/zzz", "/");
		// testAddPaths("/aaa/bbb/", "yyy/zzz", "/");
		// testAddPaths("/aaa/bbb/", "/yyy/zzz", "/");
		// //
		// testAddUnixPaths("/aaa/bbb", "yyy/zzz");
		// testAddUnixPaths("/aaa/bbb", "/yyy/zzz");
		// testAddUnixPaths("/aaa/bbb/", "yyy/zzz");
		// testAddUnixPaths("/aaa/bbb/", "/yyy/zzz");

		// testGetFullPath("c:\\test", "baba\\gaga");
		// testGetFullPath("c:\\test\\", "baba\\gaga");
		// testGetFullPath("c:\\test", "\\baba\\gaga");
		// testGetFullPath("c:\\test\\", "\\baba\\gaga");
		// testGetFullPath("c:\\test\\", "c:\\baba\\gaga");
		//
		// testGetFullPath("c:\\test\\", "\\hh\\nn");
		// testGetFullPath("\\\\test\\", "\\\\hh\\nn");
		testGetBackupFilePath("c:\\temp\\abc.zip");
		testGetBackupFilePath("c:\\temp\\abcxyz");
		// testGgetFileNameAndExtensionArray("c:\\temp\\xxx.yyy");
		// testGgetFileNameAndExtensionArray("c:\\temp\\xxx-yyy");
		// testGgetFileNameAndExtensionArray("c:\\temp\\xxx-yyy\\z123456.");
		// testGgetFileNameAndExtensionArray("z123456.xxx.yyy.zzz");

	}

	private static void testAddPaths(final String path1, final String path2, final String separator) {
		logger.info(StringUtils.concat("path1: ", path1, " path2: ", path2, " separator: ", separator, " \t",
				PathUtils.addPaths(path1, path2, separator)));
	}

	private static void testAddUnixPaths(final String path1, final String path2) {
		logger.info(StringUtils.concat("path1: ", path1, " path2: ", path2, " \t", PathUtils.addUnixPaths(path1, path2)));
	}

	public static String testGetBackupFilePath(final String filePath) {
		String backupFilePath = PathUtils.getBackupFilePath(filePath);
		logger.info("filePath: " + filePath + " backupFilePath: " + backupFilePath);
		return backupFilePath;
	}

	private static void testGetFullPath(final String path1, final String path2) {
		logger.info(StringUtils.concat("path1: ", path1, " path2: ", path2, " \t", PathUtils.getFullPath(path1, path2)));
	}

	private static void testGgetFileNameAndExtensionArray(final String filePath) {
		String[] array = PathUtils.getFileNameAndExtensionArray(filePath, Level.INFO);
		logger.info(ArrayUtils.getAsDelimitedString(array, SystemUtils.LINE_SEPARATOR));
	}

	private static void testGoDownTheFolderTree(final String path, final int level) {
		File file = PathUtils.goDownTheFolderTree("c:/temp/01", level);
		if (file != null) {
			logger.info(StringUtils.concat("path: ", path, " level:", String.valueOf(level), " result: ", file.getAbsolutePath()));
		} else {
			logger.info(StringUtils.concat("path: ", path, " level:", String.valueOf(level), " result: null"));
		}
	}
}
