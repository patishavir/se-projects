package oz.infra.httpserver;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.filter.StringFilter;
import oz.infra.http.HTTPUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;

public class HttpServerUtils {
	private static Logger logger = JulUtils.getLogger();

	private static ArrayList<String> createUrls(final String url, final ArrayList<String> fileNamesList) {
		if (fileNamesList != null) {
			for (int i = 0; i < fileNamesList.size(); i++) {
				String fullUrl = url + fileNamesList.get(i);
				fileNamesList.set(i, fullUrl);
			}
		}
		return fileNamesList;
	}

	private static ArrayList<String> dofileNameFilter(final ArrayList<String> fileNamesListIn, final StringFilter fileNamePrefixFilter) {
		ArrayList<String> fileNamesList = new ArrayList<String>();
		if (fileNamesListIn != null) {
			for (String fileName1 : fileNamesListIn) {
				if (fileNamePrefixFilter.accept(fileName1)) {
					fileNamesList.add(fileName1);
				}
			}
		}
		return fileNamesList;
	}

	public static ArrayList<String> getFileNamesFromWebPage(final String pageContents) {
		ArrayList<String> fileNamesList = new ArrayList<String>();
		final String fileNameStartTag = "<a href=\"";
		final String fileNameEnd = "\">";
		logger.finest("starting  ...");
		logger.finest(pageContents);
		if (pageContents != null) {
			String[] htmlLines = pageContents.split(OzConstants.LINE_FEED);
			logger.finest(String.valueOf(htmlLines.length) + " lines in html page");
			for (int i = 0; i < htmlLines.length; i++) {
				logger.finest(htmlLines[i]);
				int startIndex = htmlLines[i].indexOf(fileNameStartTag);

				if (startIndex > 0) {
					int endIndex = htmlLines[i].indexOf(fileNameEnd, startIndex);
					String fileNameString = htmlLines[i].substring(startIndex, endIndex);

					fileNamesList.add(fileNameString.substring(fileNameStartTag.length()));
					logger.finest(fileNamesList.get(fileNamesList.size() - 1) + " will be processed");
				}
			}
		}
		logger.info("logFilePath: " + "\n" + String.valueOf(fileNamesList.size()) + " urls have been returned");
		ListUtils.getAsDelimitedString(fileNamesList, Level.FINEST);
		return fileNamesList;

	}

	public static ArrayList<String> getFileUrlsList(final String url, final StringFilter fileNamePrefixFilter,
			final StringFilter logAnalyzerFileNameFilter) {
		logger.info("url: " + url);
		String pageContents = HTTPUtils.getPageContents(url);
		ArrayList<String> fileNamesList = getFileNamesFromWebPage(pageContents);
		ArrayList<String> fileNamesList1 = dofileNameFilter(fileNamesList, fileNamePrefixFilter);
		ArrayList<String> fileNamesList2 = dofileNameFilter(fileNamesList1, logAnalyzerFileNameFilter);
		ArrayList<String> fileNamesList3 = createUrls(url, fileNamesList2);
		return fileNamesList3;
	}

	public static AccessLogRecord jsonStringToAccessLogRecordObject(final String jsonString) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(jsonString, AccessLogRecord.class);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			return null;
		}
	}
}
