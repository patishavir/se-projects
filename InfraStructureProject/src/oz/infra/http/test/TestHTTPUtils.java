package oz.infra.http.test;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.http.HTTPUtils;
import oz.infra.http.URLBreakDown;
import oz.infra.io.LineProcessor;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.string.StringUtils;

public class TestHTTPUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// testUrlBreakDown();
		// testGetPageContents(numberOfIterations, url);
		// testProcessResponse();
		// testDownloadGzipFromUrl();
		// testGetPageContents1("http://snifp-http01.fibi.corp:81/7.0/access/");
		// testGetPageContents1("https://swvsnif-apbpm/login/");
		// testGetProtectedPageContents();
		testGetProtectedPageContentsWithPreemptiveBasicAuthentication();
		testGetProtectedPageContentsWithBasicAuthRawHeaders();
	}

	private static Properties getProperties() {
		Properties properties = new Properties();
		properties.put(ParametersUtils.HOST, "s5381355.fibi.corp");
		properties.put(ParametersUtils.PORT, "80");
		properties.put(ParametersUtils.PROTOCOL, OzConstants.HTTP);
		properties.put(ParametersUtils.URL, "http://s5381355.fibi.corp/snoop");
		properties.put(ParametersUtils.USER_NAME, "s177571");
		properties.put(ParametersUtils.ENCRYPTION_METHOD, "NONE");
		properties.put(ParametersUtils.PASSWORD, "jkfd77");
		return properties;

	}

	private static void testGetProtectedPageContentsWithPreemptiveBasicAuthentication() {
		Properties properties = getProperties();
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, 120));
		String pageContents1 = HTTPUtils.getProtectedPageContentsWithPreemptiveBasicAuthentication(properties);
		logger.info(pageContents1);
	}

	private static void testGetProtectedPageContentsWithBasicAuthRawHeaders() {
		Properties properties = getProperties();
		String pageContents = HTTPUtils.getProtectedPageContentsWithBasicAuthRawHeaders(properties);
		logger.info(pageContents);
	}

	private static void testUrlBreakDown() {
		// "http://10.18.188.55:9082/helloServerWeb/dsedata.xml";
		String fullUrl = "http://snifp-httpp:81/MatafServer/dse/desktop/matafdesktop.xml";
		new URLBreakDown(fullUrl);
		fullUrl = "http://10.18.188.55/helloServerWeb/dsedata.xml";
		new URLBreakDown(fullUrl);
		fullUrl = "http://10.18.188.55/";
		new URLBreakDown(fullUrl);
		fullUrl = "http://10.18.188.55";
		new URLBreakDown(fullUrl);
	}

	private static void testGetPageContents1(final String fullUrl) {
		StopWatch StopWatch = new StopWatch();
		// String string = HTTPUtils.getPageContents(fullUrl);
		String string = HTTPUtils.getPageContents1(fullUrl);
		StopWatch.logElapsedTimeMessage("got " + String.valueOf(string.length()) + " bytes from " + fullUrl + " in ");

	}

	private static void testGetPageContents(final int numberOfIterations, final String fullUrl) {
		// String url =
		// "http://snifp-httpb:81/MatafServer/dse/desktop/matafdesktop.xml";
		// url =
		// "http://snifp-httpb:81/MatafServer/dse/client/definitions/dsedata.xml";
		// int numberOfIterations = 1;
		// String fullUrl =
		// "http://10.18.188.55:9082/helloServerWeb/dsedata.xml";
		// http://fibiux02:81/helloServerWeb/dsedata.xml
		// "http://snifp-httpp:81/MatafServer/dse/client/definitions/dsedata.xml";
		// if (args.length > 1) {
		// url = args[1];
		// }
		// if (args.length > 0) {
		// numberOfIterations = Integer.parseInt(args[0]);
		// }
		String pageContents = null;
		for (int i = 0; i < numberOfIterations; i++) {
			StopWatch stopWatch = new StopWatch();
			pageContents = HTTPUtils.getPageContents(fullUrl);
			logger.finest("pageContents: " + pageContents.substring(pageContents.length() - OzConstants.INT_80));

			// StatusLine statusLine = HTTPUtils.getStatusLine(fullUrl);
			// logger.info("statusLine: " + statusLine.toString());
			stopWatch.logElapsedTimeMessage();
		}
		logger.info("url: " + fullUrl + "   page length: " + String.valueOf(pageContents.length()));
	}

	private static void testProcessResponse() {
		// String fullUrl =
		// "http://snifp-http01/access/access_2010-06-03-06-00";
		String fullUrl = "http://snifp-http02/access/access_2010-06-06-11-30";
		LineProcessor[] lineProcessorArray = { new HttpLineProcessor() };
		HTTPUtils.processResponseRecords(fullUrl, null, lineProcessorArray);
	}

	private static void testDownloadGzipFromUrl() {
		HTTPUtils.downloadGzipFromUrl("http://snifp-http01/waslogs/beta01/logs4Scp.tar.gz", "c:\\temp");
	}
}
