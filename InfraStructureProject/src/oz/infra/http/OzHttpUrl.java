package oz.infra.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;

public class OzHttpUrl {
	private int responseCode = Integer.MIN_VALUE;
	private String exceptionMessage = null;
	private static Logger logger = JulUtils.getLogger();

	public boolean openConnection(final int connectTimeoutinMilliseconds,
			final int readTimeoutinMilliseconds, final URL httpUrl) {
		boolean returnCode = false;
		exceptionMessage = null;
		try {
			HttpURLConnection httpurlConnection = (HttpURLConnection) httpUrl.openConnection();
			httpurlConnection.setReadTimeout(readTimeoutinMilliseconds);
			httpurlConnection.setConnectTimeout(connectTimeoutinMilliseconds);
			StringBuilder sb = new StringBuilder();
			sb.append("connect timeout: " + String.valueOf(httpurlConnection.getConnectTimeout()));
			sb.append("  read timeout: " + String.valueOf(httpurlConnection.getReadTimeout()));
			responseCode = httpurlConnection.getResponseCode();
			sb.append("  responseCode: " + String.valueOf(responseCode));
			logger.info(sb.toString());
			returnCode = responseCode == HttpURLConnection.HTTP_OK;
			httpurlConnection.disconnect();
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			exceptionMessage = ex.getMessage();
		}
		return returnCode;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

}
