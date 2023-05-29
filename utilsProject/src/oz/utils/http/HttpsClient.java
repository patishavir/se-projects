package oz.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import oz.infra.datetime.StopWatch;
import oz.infra.logging.jul.JulUtils;
import oz.infra.thread.ThreadUtils;

public class HttpsClient {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new HttpsClient().testIt("https://www.fibi-online.co.il:443/web/Ping");
			ThreadUtils.sleep(500, Level.FINEST);
		}
	}

	private void testIt(final String https_url) {
		StopWatch stopWatch = new StopWatch();
		URL url;
		try {
			url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			// dumpl all cert info
			int responseCode = print_https_cert(con);
			// dump all the content
			String serverName = print_content(con);
			logger.info(serverName + "  response time: " + stopWatch.getElapsedTimeString() + " response code:"
					+ String.valueOf(responseCode));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int print_https_cert(final HttpsURLConnection con) {
		int responseCode = Integer.MIN_VALUE;
		if (con != null) {
			try {
				StringBuilder sb = new StringBuilder();
				responseCode = con.getResponseCode();
				sb.append("Response Code : " + con.getResponseCode() + "\n");
				sb.append("Cipher Suite : " + con.getCipherSuite() + "\n");
				sb.append("\n");

				Certificate[] certs = con.getServerCertificates();
				for (Certificate cert : certs) {
					sb.append("Cert Type : " + cert.getType() + "\n");
					sb.append("Cert Hash Code : " + cert.hashCode() + "\n");
					sb.append("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm() + "\n");
					sb.append("Cert Public Key Format : " + cert.getPublicKey().getFormat() + "\n");
					sb.append("\n");
					logger.finest(sb.toString());

				}
			} catch (SSLPeerUnverifiedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseCode;
	}

	private String print_content(final HttpsURLConnection con) {
		String serverName = null;
		if (con != null) {
			try {
				StringBuilder sb = new StringBuilder();
				sb.append("****** Content of the URL ********\n");
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String input;
				while ((input = br.readLine()) != null) {
					sb.append(input + "\n");
				}
				br.close();
				String contents = sb.toString();
				logger.finest(contents);
				serverName = getServerName(contents);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return serverName;
	}

	private static String getServerName(final String contents) {
		String startTag = "<Host>";
		String endTag = "</Host>";
		int beginIndex = contents.indexOf(startTag) + startTag.length() + 1;
		int endIndex = contents.indexOf(endTag) - 1;
		String serverName = contents.substring(beginIndex, endIndex);
		logger.finest(serverName);
		return serverName;
	}
}