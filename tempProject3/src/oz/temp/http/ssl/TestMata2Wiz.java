package oz.temp.http.ssl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class TestMata2Wiz {
	private static int deliveryCount = 10;
	private static String logMsgId = "1234567";
	private static Logger logger = Logger.getLogger(TestMata2Wiz.class.toString());

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String serviceEncoding = "";
		int timeoutInterval = 10000;
		String messageTxt = "qazwsxedcrfv";
		String urlString = "http://suqsnifit-http.fibi.corp:82/MatafServer_K_Z5/generalClientDefines.properties";
		urlString = "http://divur.fibi.corp/extra/AgentScreens/MayaService.aspx";
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (Exception e) {
			logger.error(e);

		}
		xxx(timeoutInterval, serviceEncoding, messageTxt, url);
	}

	private static void xxx(final int timeoutInterval, final String serviceEncoding, final String messageTxt, final URL url) {
		try {

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true); // true indicates POST request
			conn.setDoInput(true); // true indicates the server returns response
			conn.setConnectTimeout(timeoutInterval);
			conn.setReadTimeout(timeoutInterval);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text; charset=utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(messageTxt.length()));

			OutputStream os = conn.getOutputStream();
			// os.write(messageTxt.getBytes("utf-8"));
			os.write(messageTxt.getBytes("utf-8"));// windows-1255

			os.flush();
			os.close();

			int rc = conn.getResponseCode();
			String rcMessage = conn.getResponseMessage();
			logger.info("rcMessage: " + rcMessage);

			String contentCharset = getContentCharset(conn);
			if (contentCharset == null) {
				contentCharset = "";
			}
			if (contentCharset.equals("")) {
				contentCharset = "UTF_8";
			}

			if (rc == 422) {
				// Unprocessable Entity
				// permanent data error - do not retry
				reportError(logMsgId
						+ "permanent data error received from called http service, so not retrying to post the same message to this service.");
			} else {
				if (rc < 200 || rc >= 300) {
					// processSystemError(deliveryCount, logMsgId);
				} else {
					InputStream is = conn.getInputStream();
					InputStreamReader isr = new InputStreamReader(is, contentCharset);
					BufferedReader br = new BufferedReader(isr);
					StringBuilder sb = new StringBuilder();
					String output;
					while ((output = br.readLine()) != null) {
						sb.append(output);
					}
					conn.disconnect();
					logger.info(logMsgId + "ResponseCode=" + rc + "-" + rcMessage + "\tResponseBody=" + sb.toString());
				}
			}

		} catch (IOException e) {
			logger.warn(e.toString());
			logger.warn(e.getMessage());
			reportError(logMsgId, e);
			// processSystemError(deliveryCount, logMsgId);
		}

	}

	private static String getContentCharset(HttpURLConnection connection) {
		if (connection != null) {
			final String contentType = connection.getContentType();
			if (contentType != null) {
				final String[] values = contentType.split(";");
				String charset = null;
				if (values != null) {
					for (String value : values) {
						value = value.trim();
						if (value != null && value.toLowerCase().startsWith("charset=")) {
							charset = value.substring("charset=".length());
						}
					}
				}
				return charset;
			}
		}
		return null;
	}

	private static void reportError(String logMsgId, Exception e) {
		logger.warn(e.toString());
		logger.warn(e.getMessage() + " while processing message " + logMsgId);
		// reportError(e + " while processing message " + logMsgId);
	}

	private static void reportError(String s) {
		logger.error(s);

		String es = "<?xml version=\"1.0\"?><event><mainId>מערכת סניפית</mainId><secondaryId>Pdf2Wiz</secondaryId><thirdId></thirdId><errorCode></errorCode><version></version><errorMsg><![CDATA["
				+ s + "]]>"
				+ "</errorMsg><resourceId></resourceId><resourceType></resourceType><Severity>CRITICAL</Severity><subject>Pdf2Wiz: <![CDATA[" + s
				+ "]]>" + "</subject><Hostname>" + getHostName() + "</Hostname></event>";

		try {
			// putAutoMessage.go(es);
		} catch (Exception e1) {
			logger.error(s);
			logger.error(e1);
		}

	}

	private static String getHostName() {
		String localHostStr = "";
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			localHostStr = localHost.getHostName() + " (" + localHost.getHostAddress() + ")";
		} catch (UnknownHostException e1) {
			logger.warn(e1);
		}
		return localHostStr;
	}
}
