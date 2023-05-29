package oz.test.infra;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import org.apache.http.HttpStatus;
import org.junit.Test;

import oz.infra.http.HTTPUtils;
import oz.infra.logging.jul.JulUtils;

public class TestHTTPUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	@Test
	public void testHTTPUtils() {
		String contents = HTTPUtils.getPageContents("http://www.fibi-online.co.il:80", "/");
		System.out.println("**********************************************\n" + contents);
		assertTrue(contents.indexOf("html") > 0);

		http: // snif-http-tyv/wasPerfTool/servlet/perfservlet

		contents = HTTPUtils.getPageContents("http://snif-http", "/wasPerfTool/servlet/perfservlet", "s177571",
				"ujik00");
		System.out.println("**********************************************\n" + contents);
		assertTrue(contents.indexOf("html") > 0);
		contents = HTTPUtils.getPageContents("www.google.co.il", "/");
		assertTrue(contents.indexOf("html") > 0);
		contents = HTTPUtils.getPageContents("ynet.co.il", "/");
		assertTrue(contents.indexOf("html") > 0);
		contents = HTTPUtils.getPageContents("ynet111.co.il", "/");
		System.out.println("**********************************************\n" + contents);
		assertNull(contents);
		assertTrue(HTTPUtils.getStatusLine("http://ynet.co.il").getStatusCode() == HttpStatus.SC_OK);
		assertTrue(HTTPUtils.getStatusLine("http://ynet.co.il", "/").getStatusCode() == HttpStatus.SC_OK);
		assertFalse(HTTPUtils.getStatusLine("http://ynet.co.il", "/ppppp").getStatusCode() == HttpStatus.SC_OK);
		// assertFalse(HTTPUtils.isUrlAvailable("http://ynet.co.il:81"));
		assertTrue(HTTPUtils.getStatusLine("ynet.co.il").getStatusCode() == HttpStatus.SC_OK);
		// assertFalse(HTTPUtils.isUrlAvailable("httpqqq://ynet.co.il"));
		logger.info("starting getPageContents");
		contents = HTTPUtils.getPageContents("http://localhost:9080", "/wasPerfTool/servlet/perfservlet");
		logger.info(contents);
	}
}
