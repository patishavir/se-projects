package oz.infra.http.win.test;

import java.util.logging.Logger;

import org.apache.http.impl.client.CloseableHttpClient;

import oz.infra.http.win.HttpWinClientUtils;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public class HttpWinClientUtilsTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String contentsString = testGetProtectedPageContent2(args[0], Integer.parseInt(args[1]));
		logger.info(contentsString);
		FileUtils.writeFile("c:/temp/HttpWinClientUtilsTest.txt", contentsString);

	}

	private static String testGetProtectedPageContent1(final String url, final int loopCount) {
		StringBuilder sb = new StringBuilder(SystemUtils.LINE_SEPARATOR);
		for (int i = 0; i < loopCount; i++) {
			sb.append(HttpWinClientUtils.getProtectedPageContents(url));
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		return sb.toString();
	}

	private static String testGetProtectedPageContent2(final String url, final int loopCount) {
		CloseableHttpClient httpClient = HttpWinClientUtils.getWinHttpClient();
		StringBuilder sb = new StringBuilder(SystemUtils.LINE_SEPARATOR);
		for (int i = 0; i < loopCount; i++) {
			sb.append(HttpWinClientUtils.getProtectedPageContents(url, httpClient));
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		return sb.toString();
	}
}