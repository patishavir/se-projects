package oz.jdir;

import java.util.logging.Logger;

public class FileInfoFilter {
	private static Logger logger = Logger.getLogger(FileInfoFilter.class.getName());

	static boolean accept(final FileInfo fileInfo) {
		String filePathIncludeFilter = JdirParameters.getFilePathIncludeFilter();
		String filePathExcludeFilter = JdirParameters.getFilePathExcludeFilter();
		// logger.finest("filePathIncludeFilter: " + filePathIncludeFilter
		// + " filePathExcludeFilter: " + filePathExcludeFilter);
		int includeFilterLength = filePathIncludeFilter.length();
		int excludeFilterLength = filePathExcludeFilter.length();
		/*
		 * process filePathFilter
		 */
		if (fileInfo.isFile()) {
			if ((includeFilterLength > 0 && fileInfo.getFilePartialPath().indexOf(
					filePathIncludeFilter) == -1)
					|| (excludeFilterLength > 0 && fileInfo.getFilePartialPath().indexOf(
							filePathExcludeFilter) > -1)
					|| (excludeFilterLength > 0 && filePathExcludeFilter.equals("*"))) {
				logger.finer("File filter rejected: " + fileInfo.getFilePartialPath());
				return false;
			}
		}
		// logger.finest("accepted: " + fileInfo.getFilePartialPath());
		return true;
	}
}
