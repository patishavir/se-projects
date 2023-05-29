package oz.utils.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.datetime.StopWatch;
import oz.infra.html.HtmlUtils;
import oz.infra.http.HTTPUtils;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.thread.ThreadUtils;

public class CopySnifitWasLogs {
	private static String pageUrls = null;
	private static String fileNamePrefix = null;
	private static String logsFolder = null;
	private static int sleepSecondsBetweenFiles = -1;
	private static String[] pageUrlsArray = null;
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String yyyymmdd = DateTimeUtils.formatDate(DateTimeUtils.DATE_FORMAT_yyyyMMdd);
		StopWatch operationStopWatch = new StopWatch();
		int fileCounter = 0;
		long totalSize = 0;
		Properties CopySnifitWasLogsProperties = PropertiesUtils.loadPropertiesFile(args[0]);
		ReflectionUtils.setFieldsFromProperties(CopySnifitWasLogsProperties, CopySnifitWasLogs.class);
		for (String pageUrl : pageUrlsArray) {
			ArrayList<String> fileUrlsArrayList = HtmlUtils.getFileUrlsFromPage(pageUrl, fileNamePrefix);
			for (int i = 0; i < fileUrlsArrayList.size(); i++) {
				StopWatch stopWatch1 = new StopWatch();
				String fileUrl = fileUrlsArrayList.get(i);
				String contentsString = HTTPUtils.getPageContents(fileUrl);
				String[] fileNameBreakDown = fileUrl.split(OzConstants.SLASH);
				String fileName = fileNameBreakDown[fileNameBreakDown.length - 1];
				String subFolders = yyyymmdd + File.separator + fileNameBreakDown[fileNameBreakDown.length - 3]
						+ File.separator + fileNameBreakDown[fileNameBreakDown.length - 2];
				File targetFoldersFile = new File(logsFolder + File.separator + subFolders);
				if (!targetFoldersFile.exists()) {
					targetFoldersFile.mkdirs();
				}
				String targetLogFilePath = targetFoldersFile.getAbsolutePath() + File.separator + fileName;
				logger.finest("subFolders: " + subFolders + "  fileName: " + fileName + "  targetLogFilePath: "
						+ targetLogFilePath);
				FileUtils.writeFile(targetLogFilePath, contentsString);
				stopWatch1.logElapsedTimeMessage(targetLogFilePath + " copied ");
				fileCounter++;
				totalSize += new File(targetLogFilePath).length();
				ThreadUtils.sleep(sleepSecondsBetweenFiles * 1000, Level.INFO);
			}
		}
		operationStopWatch.logElapsedTimeMessage("Copy snifit was logs completed!");
		logger.info(String.valueOf(fileCounter) + " files have been copied. Total length: " + String.valueOf(totalSize)
				+ ".");
	}

	public static void setFileNamePrefix(String fileNamePrefix) {
		CopySnifitWasLogs.fileNamePrefix = fileNamePrefix;
	}

	public static void setLogsFolder(String logsFolder) {
		CopySnifitWasLogs.logsFolder = logsFolder;
	}

	public static void setPageUrls(String pageUrls) {
		CopySnifitWasLogs.pageUrls = pageUrls;
		pageUrlsArray = pageUrls.split(OzConstants.COMMA);
	}

	public static void setSleepSecondsBetweenFiles(int sleepSecondsBetweenFiles) {
		CopySnifitWasLogs.sleepSecondsBetweenFiles = sleepSecondsBetweenFiles;
	}
}
