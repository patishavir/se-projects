package oz.infra.http;

import oz.infra.datetime.StopWatch;

public class TestHttpUtils {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testGetPageContents1("https://swvsnif-apbpm/login/");
	}

	private static void testGetPageContents1(final String fullUrl) {
		StopWatch StopWatch = new StopWatch();
		// String string = HTTPUtils.getPageContents(fullUrl);
		String string = HTTPUtils.getPageContents1(fullUrl);
		StopWatch.logElapsedTimeMessage("got " + String.valueOf(string.length()) + " bytes from " + fullUrl + " in ");

	}

}
