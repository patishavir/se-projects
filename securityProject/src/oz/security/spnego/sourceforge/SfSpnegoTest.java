package oz.security.spnego.sourceforge;

import java.net.URL;

import net.sourceforge.spnego.SpnegoHttpURLConnection;

public class SfSpnegoTest {

	/**
	 * @param args
	 */
	public static void main(final String[] args) throws Exception {
		final String username = ""; // ex. dfelix
		final String password = ""; // ex. myp@s5
		// final String url =
		// "http://snif-http.fibi.corp/MatafServer_V_ZY/generalClientDefines.properties";
		// // ex. http://medusa:8080/hello_jsp.jsp
		final String url = "http://snif-http.fibi.corp/snoop";
		final String module = "com.sun.security.jgss.initiate"; // ex.
																// spnego-client

		// System.setProperty("java.security.krb5.conf", "krb5.conf");
		// System.setProperty("sun.security.krb5.debug", "true");
		// System.setProperty("java.security.auth.login.config", "login.conf");
		String argsPath = ".\\args\\sfspnego\\";
		System.setProperty("java.security.krb5.conf", argsPath + "spnego-krb5.conf");
		System.setProperty("java.security.auth.login.config", argsPath + "spnego-jaas.conf");
		System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
		System.setProperty("java.security.debug", "all");
		System.setProperty("http.auth.preference", "spnego");

		SpnegoHttpURLConnection spnego = null;

		try {
			spnego = new SpnegoHttpURLConnection(module, username, password);
			spnego.connect(new URL(url));
			System.out.println("\nHTTP_STATUS_CODE=" + spnego.getResponseCode());

		} finally {
			if (null != spnego) {
				spnego.disconnect();

			}
		}
	}
}
