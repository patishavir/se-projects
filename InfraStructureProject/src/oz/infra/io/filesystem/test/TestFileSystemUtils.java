package oz.infra.io.filesystem.test;

import java.util.List;
import java.util.logging.Logger;

import oz.infra.io.filesystem.FileSystemDiskUsage;
import oz.infra.io.filesystem.FileSystemsUtils;
import oz.infra.logging.jul.JulUtils;

public class TestFileSystemUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testFileSystemUtils();
	}

	private static void testFileSystemUtils() {
		FileSystemsUtils.getFileSystemsList();
		List<FileSystemDiskUsage> fileSystemDiskUsageArrayList = FileSystemsUtils
				.getFileSystemsUsageList();
		for (FileSystemDiskUsage fileSystemDiskUsage : fileSystemDiskUsageArrayList) {
			logger.info(fileSystemDiskUsage.getName() + " "
					+ fileSystemDiskUsage.getFreeSpaceBytes());
		}
	}
}
