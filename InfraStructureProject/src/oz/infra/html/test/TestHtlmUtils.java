package oz.infra.html.test;

import oz.infra.html.HtmlUtils;

public class TestHtlmUtils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testGetFileUrlsFromPage("http://snifp-http01/waslogs/beta01/B/", "log4j");
	}

	private static void testGetFileUrlsFromPage(final String pageUrl, final String fileNamePrefix) {

		HtmlUtils.getFileUrlsFromPage(pageUrl, fileNamePrefix);
	}
}
