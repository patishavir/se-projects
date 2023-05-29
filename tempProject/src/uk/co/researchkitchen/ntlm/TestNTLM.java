// HttpClient 3.0-RC2

package uk.co.researchkitchen.ntlm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.params.AuthPolicy;

import oz.infra.logging.jul.JulUtils;

public class TestNTLM {
	private static Logger logger = JulUtils.getLogger();

	private void execute() throws HttpException, IOException {
		HttpClient proxyclient = new HttpClient();

		// proxyclient.getHostConfiguration().setHost("www.yahoo.com");
		proxyclient.getHostConfiguration().setHost("s5380577.fibi.co.il", 9080);

		// proxyclient.getHostConfiguration().setProxy("lproxyhost.researchkitchen.co.uk",
		// 8080);
		// proxyclient.getHostConfiguration().setProxy("s5380577.fibi.co.il",
		// 9080);

		List authPrefs = new ArrayList();
		authPrefs.add(AuthPolicy.NTLM);

		proxyclient.getState().setProxyCredentials(new AuthScope(null, 9080, null),
				new NTCredentials("PeopleSoftMonitor", "123456", "", ""));

		proxyclient.getParams().setParameter(AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);
		logger.info("MonitorPeopleSoftMonitor++++++++++++++++++++");
		GetMethod get = new GetMethod("/psp/ps/?cmd=login&languageCd=HEB");
		int status = proxyclient.executeMethod(get);

		System.out.println(status);

		BufferedReader bis = new BufferedReader(
				new InputStreamReader(get.getResponseBodyAsStream()));

		int count = 0;
		int read = 0;
		System.out.println("Content length: " + get.getResponseContentLength());
		char[] body = new char[2048];
		do {
			count = bis.read(body);
			read += count;
			System.out.println(body);
		} while (count != -1);

		System.out.println("Read " + read + " bytes");

		
	}

	public static void main(String[] args) throws HttpException, IOException {
		new TestNTLM().execute();
	}

}