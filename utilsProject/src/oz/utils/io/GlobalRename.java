package oz.utils.io;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.NioUtils;
import oz.infra.string.StringUtils;

public class GlobalRename {
	private static final String FILE_NAME_PREFIX = "HP";
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		doRename("c:/temp/hit");
	}

	private static void doRename(final String folderPath) {
		int renameCount = 0;
		File folderFile = new File(folderPath);
		File[] folders = folderFile.listFiles();
		for (File folder1 : folders) {
			String folder1AbsolutePath = folder1.getAbsolutePath();
			String folderName = folder1.getName();
			logger.info("folder name: ".concat(folderName));
			if (!folder1.isDirectory()) {
				logger.warning(folder1AbsolutePath.concat(" is not a folder !"));
			} else {
				logger.info("processing ".concat(folder1AbsolutePath));
				File[] files = folder1.listFiles();
				for (File file1 : files) {
					String file1AbsolutePath = file1.getAbsolutePath();
					String fileName = file1.getName();
					logger.info(
							StringUtils.concat("file absolute path: ", file1AbsolutePath, " file name: ", fileName));
					if (fileName.startsWith(FILE_NAME_PREFIX)) {
						String newName = fileName.replace(FILE_NAME_PREFIX, folderName);
						Path newPath = NioUtils.rename(file1AbsolutePath, newName);
						logger.info(StringUtils.concat(file1AbsolutePath, " renamed to ", newPath.toString()));
						renameCount++;
					}
				}

			}

		}
		logger.info(String.valueOf(renameCount).concat(" files have been renamed"));
	}
}
