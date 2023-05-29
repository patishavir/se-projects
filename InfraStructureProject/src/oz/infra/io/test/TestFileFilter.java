package oz.infra.io.test;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.io.filefilter.FileFilterRegExpression;
import oz.infra.io.filefilter.FileNameFilterRegExpression;
import oz.infra.io.filefilter.GeneralFileFilerEnum;
import oz.infra.io.filefilter.GeneralFileFilter;
import oz.infra.logging.jul.JulUtils;

public class TestFileFilter {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testFileFilter(new File("c:\\temp\\p9_server_error.log"),
		// ".*p8_server_.*");
		// testFileFilter(new File("c:\\temp\\p8_server_error.log"),
		// ".*p8_server_.*");
		// testFileFilter(new File("c:\\temp\\p8_ser_ver_error.log"),
		// ".*p8_server_.*");
		// String p8 = "\\\\s5381310\\d$\\Program
		// Files\\IBM\\WebSphere\\AppServer\\profiles\\AppSrv01\\FileNet\\server1\\p8_server_error.log.3";
		// testFileFilter(new File(p8), ".*p8_server_.*");
		// testFileNameFilter(new File("c:\\temp\\"), "p8_ser_ver_error.log",
		// ".*p8_server_.*");
		// testFileNameFilter(new File("c:\\temp\\"), "p8_server_error.log",
		// ".*p8_server_.*");
		testGeneralFilter();
	}

	private static void testFileFilter(final File file, final String regexpression) {
		FileFilterRegExpression fileFilterRegExpression = new FileFilterRegExpression(regexpression, true);
		boolean result = fileFilterRegExpression.accept(file);
		logger.info(
				"file: " + file.getAbsoluteFile() + " regexp: " + regexpression + " result: " + String.valueOf(result));
	}

	private static void testFileNameFilter(final File dir, final String name, final String regexpression) {
		FileNameFilterRegExpression fileNameFilterRegExpression = new FileNameFilterRegExpression(regexpression);
		boolean result = fileNameFilterRegExpression.accept(dir, name);
		logger.info("file: " + dir.getAbsoluteFile() + " name: " + name + " regexp: " + regexpression + " result: "
				+ String.valueOf(result));
	}

	private static void testGeneralFilter() {
		File file = new File("c:\\temp");
		File[] files1 = file.listFiles();
		logger.info(ArrayUtils.getAsDelimitedString(files1));

		FileFilter filreilter1DO = new GeneralFileFilter(GeneralFileFilerEnum.IS_DIRECTORY);
		File[] files2 = file.listFiles(filreilter1DO);
		logger.info(ArrayUtils.getAsDelimitedString(files2));

		FileFilter filreilter1FO = new GeneralFileFilter(GeneralFileFilerEnum.IS_FILE);
		File[] files3 = file.listFiles(filreilter1FO);
		logger.info(ArrayUtils.getAsDelimitedString(files3));
	}
}
