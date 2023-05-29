package oz.infra.html;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.http.HTTPUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;

public class HtmlUtils {
	private static Logger logger = JulUtils.getLogger();

	public static ArrayList<String> getFileUrlsFromPage(final String pageUrl,
			final String fileNamePrefix) {
		final String fileNameStartTag = "<a href=\"";

		final String fileNameEnd = "\">";
		logger.finest("starting  ...");
		String pageContents = HTTPUtils.getPageContents(pageUrl);
		logger.finest(pageContents);
		String[] htmlLines = pageContents.split(OzConstants.LINE_FEED);
		logger.finest(String.valueOf(htmlLines.length));
		ArrayList<String> fileUrlsArrayList = new ArrayList<String>();
		for (int i = 0; i < htmlLines.length; i++) {
			int startIndex = htmlLines[i].indexOf(fileNameStartTag + fileNamePrefix);
			if (startIndex > -1) {
				int endIndex = htmlLines[i].indexOf(fileNameEnd, startIndex);
				String fileNameString = htmlLines[i].substring(startIndex, endIndex);

				fileUrlsArrayList
						.add(pageUrl + fileNameString.substring(fileNameStartTag.length()));
				logger.finest(fileUrlsArrayList.get(fileUrlsArrayList.size() - 1)
						+ " will be processed");
			}
		}
		ListUtils.getAsDelimitedString(fileUrlsArrayList, Level.INFO);
		return fileUrlsArrayList;
	}

}
