package oz.infra.temp;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class TestUrlConnection {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		testUrlConnection();
	}

	private static void testUrlConnection() {
		try {
			URL url = new URL("http://p8cetest:9080/P8CE/Health");
			HttpURLConnection httpurlConnection = (HttpURLConnection) url.openConnection();
			httpurlConnection.setReadTimeout(10);
			httpurlConnection.setConnectTimeout(10);
			logger.info("connect timeout: " + String.valueOf(httpurlConnection.getConnectTimeout()));
			logger.info("read timeout: " + String.valueOf(httpurlConnection.getReadTimeout()));
			logger.info(String.valueOf(httpurlConnection.getResponseCode()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
