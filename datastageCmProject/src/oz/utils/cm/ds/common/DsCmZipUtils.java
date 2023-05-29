package oz.utils.cm.ds.common;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.infra.zip.ZipUtils;
import oz.utils.cm.ds.DsCmRunParameters;

public class DsCmZipUtils {
	private static String zipFolderPath = null;
	private static String currentYear = String.valueOf(DateTimeUtils.getCurrentYear());
	private static final Logger logger = JulUtils.getLogger();

	private static String getZipFilePath(final String sourceFolderPath) {
		final String environment = DsCmRunParameters.getEnvironment();
		logger.info(StringUtils.concat("sourceFolder: ", sourceFolderPath, " environment: ", environment));
		zipFolderPath = PathUtils.getFullPath(PathUtils.getParentFolderPath(sourceFolderPath), currentYear);
		zipFolderPath = PathUtils.getFullPath(zipFolderPath, environment);
		File zipFolderFile = new File(zipFolderPath);
		if (!zipFolderFile.exists()) {
			zipFolderFile.mkdirs();
		}
		String folder2ProcessName = PathUtils.getNameWithoutExtension(sourceFolderPath);
		String zipFilePath = PathUtils.getFullPath(zipFolderPath,
				StringUtils.concat(folder2ProcessName, OzConstants.UNDERSCORE, SystemPropertiesUtils.getUserName(), OzConstants.UNDERSCORE,
						SystemUtils.getHostname(), OzConstants.UNDERSCORE, DateTimeUtils.getTimeStamp(), OzConstants.ZIP_SUFFIX));
		return zipFilePath;

	}

	public static String getZipFolderPath() {
		return zipFolderPath;
	}

	public static void zipContents(final File sourceFolder) {
		String sourceFolderPath = sourceFolder.getAbsolutePath();
		List<File> files2ZipList = FolderUtils.getRecursivelyAllFiles(sourceFolder);
		List<String> paths2ZipList = FolderUtils.fileList2PathList(files2ZipList);
		ZipUtils.zipFiles(getZipFilePath(sourceFolderPath), paths2ZipList, sourceFolderPath);
	}
}
