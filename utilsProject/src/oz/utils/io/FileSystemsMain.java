package oz.utils.io;

import java.util.logging.Logger;

import oz.infra.io.filesystem.FileSystemsUtils;
import oz.infra.logging.jul.JulUtils;

public class FileSystemsMain {
	private static Logger logger = JulUtils.getLogger();

	private FileSystemsMain() {
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		FileSystemsUtils.getFileSystemsList();
		logger.info("**********************");
		FileSystemsUtils.getFileSystemsUsageList();
		logger.info("all done !");

	}
}
