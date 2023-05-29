package oz.utils.files;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.io.FileUtils;
import oz.infra.io.filefilter.FileFilterUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.visitor.FileVisitorUtils;
import oz.infra.number.NumberUtils;
import oz.infra.regexp.RegexpUtils;

public class TotalFilesSize {
	private static int grnadTotalFiles = 0;
	private static long grnadTotalSize = 0l;
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 2) {
			String rootPath = args[0];
			String suffixFiler = args[1];
			getTotalSize(rootPath, suffixFiler);
		} else {
			String paramtersFilePath = args[0];
			processParametersFile(paramtersFilePath);
		}
		logger.info("*** Grand total file count: " + grnadTotalFiles + " \tgrand total length: "
				+ NumberUtils.format(grnadTotalSize, NumberUtils.FORMATTER_COMMAS));
	}

	public static void getTotalSize(final String rootPath, final String suffixFiler) {
		StopWatch stopWatch = new StopWatch();
		List<File> fileList = new FileVisitorUtils().recursivelyGetFilesOnly(rootPath);
		fileList = FileFilterUtils.filerFileListByRegExp(fileList, RegexpUtils.REGEXP_JAVA_FILE);
		int totalFiles = fileList.size();
		grnadTotalFiles += totalFiles;
		long totalSize = getTotalSize(fileList);
		grnadTotalSize += totalSize;
		logger.info("rootPath: " + rootPath + " total file count: " + String.valueOf(totalFiles) + " total length: "
				+ NumberUtils.format(totalSize, NumberUtils.FORMATTER_COMMAS) + " elapsedTime: "
				+ stopWatch.getElapsedTimeString());
	}

	private static long getTotalSize(List<File> fileList) {
		long totalLength = 0;
		for (File file1 : fileList) {
			String lowerName1 = file1.getName().toLowerCase();
			long length = file1.length();
			totalLength += length;
			logger.finest("lowerName1: " + lowerName1 + " length: " + length + " totalLength: "
					+ String.valueOf(totalLength));
		}
		logger.finest(
				"file count: " + String.valueOf(fileList.size()) + " total Length: " + String.valueOf(totalLength));
		return totalLength;
	}

	private static void processParametersFile(final String paramtersFilePath) {
		String[] parametersArray = FileUtils.readTextFile2Array(paramtersFilePath);
		for (String line1 : parametersArray) {
			String[] params1 = line1.split(OzConstants.COMMA);
			getTotalSize(params1[0], params1[1]);
		}
	}
}
