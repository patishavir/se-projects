package oz.infra.io.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public class FileSystemsUtils {
	private static Logger logger = JulUtils.getLogger();

	public static String getFileSystemsList() {
		StringBuilder stringBuilder = new StringBuilder();
		File[] roots = File.listRoots();
		for (File root : roots) {
			logger.info("processing root: " + root.getAbsolutePath());
			File[] fileSystems = root.listFiles();
			if (fileSystems != null && fileSystems.length > 0) {
				for (File fileSystem : fileSystems) {
					if (fileSystem.isDirectory()) {
						stringBuilder.append(SystemUtils.LINE_SEPARATOR
								+ fileSystem.getAbsolutePath() + " "
								+ String.valueOf(fileSystem.getFreeSpace()) + " "
								+ String.valueOf(fileSystem.getTotalSpace()));
					}
				}
			}
		}
		logger.finest(stringBuilder.toString() + SystemUtils.LINE_SEPARATOR);
		return stringBuilder.toString();
	}

	public static List<FileSystemDiskUsage> getFileSystemsUsageList() {
		StringBuilder stringBuilder = new StringBuilder();
		File[] roots = File.listRoots();
		ArrayList<FileSystemDiskUsage> fileSystemDiskUsageArrayList = new ArrayList<FileSystemDiskUsage>();
		for (File root : roots) {
			stringBuilder.append("processing root: " + root.getAbsolutePath());
			File[] fileSystems = root.listFiles();
			if (fileSystems != null && fileSystems.length > 0) {
				for (File fileSystem : fileSystems) {
					if (fileSystem.isDirectory()) {
						FileSystemDiskUsage fileSystemDiskUsage = new FileSystemDiskUsage();
						fileSystemDiskUsage.setName(fileSystem.getAbsolutePath());
						long totalSpace = fileSystem.getTotalSpace();
						long freeSpace = fileSystem.getFreeSpace();
						long usedSpace = totalSpace - freeSpace;
						fileSystemDiskUsage.setFreeSpaceBytes(freeSpace);
						fileSystemDiskUsage.setTotalSpaceBytes(totalSpace);
						fileSystemDiskUsage
								.setPrecentageUsed((int) ((usedSpace * OzConstants.INT_100) / totalSpace));
						fileSystemDiskUsageArrayList.add(fileSystemDiskUsage);
						try {
							stringBuilder.append(SystemUtils.LINE_SEPARATOR
									+ fileSystemDiskUsage.getName() + " "
									+ fileSystemDiskUsage.getFreeSpaceBytes() + " "
									+ fileSystemDiskUsage.getTotalSpaceBytes()

									+ " " + fileSystem.getAbsolutePath() + " "
									+ fileSystem.getCanonicalPath());
						} catch (Exception ex) {
							ExceptionUtils.printMessageAndStackTrace(ex);
						}
					}
				}
			}
		}
		logger.finest(SystemUtils.LINE_SEPARATOR + stringBuilder.toString());
		return fileSystemDiskUsageArrayList;
	}
}
