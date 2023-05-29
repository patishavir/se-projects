package oz.utils.http.load;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.http.HTTPUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class HttpLoadTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int count = 250;
		String url1 = "http://snifp-http01.fibi.corp/loadtestdata.txt";
		runLoadTest(url1, count);
		String url2 = "http://snifp-http02.fibi.corp/loadtestdata2.txt";
		runLoadTest(url2, count);
		String url3 = "http://snif-http.fibi.corp/loadtestdata3.txt";
		runLoadTest(url3, count);
		String url4 = "http://suswastest.fibi.corp/loadtestdata4.txt";
		runLoadTest(url4, count);
	}

	private static void runLoadTest(final String fullUrl, final int count) {
		long totalBytesRead = 0;
		StopWatch methodStopWatch = new StopWatch();
		for (int i = 0; i < count; i++) {
			StopWatch stopWatch = new StopWatch();
			// String string = HTTPUtils.getPageContents(fullUrl);
			String string = HTTPUtils.getPageContents1(fullUrl);
			totalBytesRead += string.length();
			String message = StringUtils.concat("got " + String.valueOf(string.length()) + " bytes from " + fullUrl
					+ " in ", String.valueOf(stopWatch.getElapsedTimeLong()));
			logger.finest(message);
		}
		long elapseTime = methodStopWatch.getElapsedTimeLong();
		logger.info(StringUtils.concat(String.valueOf(count), " tests for ", fullUrl, " completed in ",
				String.valueOf(elapseTime), " milliseconds. total bytes read: ", String.valueOf(totalBytesRead),
				OzConstants.BLANK, StringUtils.repeatString("=", 30)));
	}
}
