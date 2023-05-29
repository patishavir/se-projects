package oz.infra.io.test;

import java.util.logging.Logger;

import oz.infra.io.filesystem.FileSystemsUtils;
import oz.infra.logging.jul.JulUtils;

public class FileSystemUtilsTest {
	private static Logger logger = JulUtils.getLogger();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info(FileSystemsUtils.getFileSystemsList());
		FileSystemsUtils.getFileSystemsUsageList();
	}
}
