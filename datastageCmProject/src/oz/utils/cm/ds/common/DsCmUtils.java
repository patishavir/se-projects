package oz.utils.cm.ds.common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import oz.infra.datetime.DateTimeUtils;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class DsCmUtils {
	private static final String DONE_FOLDER_NAME = "done";
	private static final Logger logger = JulUtils.getLogger();

	public static String makeDoneFolder(final String folderPath) {
		String doneFolderPath = PathUtils.getFullPath(folderPath, DONE_FOLDER_NAME);
		doneFolderPath = PathUtils.getFullPath(doneFolderPath, DateTimeUtils.getTodStamp());
		new File(doneFolderPath).mkdirs();
		logger.info(StringUtils.concat("made folder ", doneFolderPath));
		return doneFolderPath;
	}

	public static File move2DoneFolder(final File file2Move, final String doneFolderPath) {
		String messagePrefix = " failed";

		Path sourcePath = Paths.get(file2Move.getAbsolutePath());
		String doneFilePath = PathUtils.getFullPath(doneFolderPath, file2Move.getName());
		Path targetPath = Paths.get(doneFilePath);
		try {
			Files.move(sourcePath, targetPath);
			messagePrefix = " succeeded";
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		logger.info(StringUtils.concat(messagePrefix, " to move ", file2Move.getAbsolutePath(), " to ", doneFolderPath));
		return targetPath.toFile();
	}
}
