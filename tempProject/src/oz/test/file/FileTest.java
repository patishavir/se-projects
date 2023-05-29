package oz.test.file;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class FileTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info(String.valueOf(new File(".").isAbsolute()));
		logger.info(String.valueOf(new File("..").isAbsolute()));
		logger.info(String.valueOf(new File("temp\\bak\\.").isAbsolute()));
		logger.info(String.valueOf(new File("temp/bak/").isAbsolute()));
		logger.info(String.valueOf(new File("\\temp\\").isAbsolute()));
		logger.info("\n\nfalse above true below ***************************\n");
		logger.info(String.valueOf(new File("c:\\temp\\.").isAbsolute()));
		logger.info(String.valueOf(new File("\\\\matafcc\\temp\\..").isAbsolute()));
		logger.info(String.valueOf(new File("c:/oj").isAbsolute()));
		
	}
}
