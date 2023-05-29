package oz.jdir;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.io.FileUtils;

public final class FileComparison {

	private static Logger logger = Logger.getLogger(FileComparison.class.getName());

	/*
	 * compareFiles
	 */
	public static boolean compareFiles(final FileInfo file1, final String endPath1,
			final String fullPath1, final FileInfo file2, final String endPath2,
			final String fullPath2) {
		if (!endPath1.equalsIgnoreCase(endPath2)) {
			return false;
		}
		if (file1.isDirectory() && file2.isDirectory()) {
			return true;
		}
		if (!(file1.isFile() && file2.isFile())) {
			return false;
		}
		if (file1.length() != file2.length()) {
			return false;
		}
		if (file1.lastModified() == file2.lastModified()) {
			return true;
		}
		String myComparisonCriteria = JdirParameters.getFileComparisonCriteria();
		if (myComparisonCriteria.indexOf("stamp") != -1) {
			return (file1.lastModified() == file2.lastModified());
		} else if (myComparisonCriteria.indexOf("digest") != -1) {
			if (file1.length() == 0 && file2.length() == 0) {
				return true;
			} else {
				return FileUtils.performMessageDigestsComparison(fullPath1, fullPath2,
						JdirParameters.getMessageDigestAlgorithm());
			}
		} else if (myComparisonCriteria.indexOf("binary") != -1) {
			if (file1.length() == 0 && file2.length() == 0) {
				return true;
			} else {
				return (FileUtils.performFullBinaryComparison(new File(fullPath1), new File(
						fullPath2)));
			}
		}
		return false;
	}
}