package oz.utils.io;

import java.io.File;
import java.util.logging.Logger;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;

import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;

public class OZHTTPSClient {
	// private static String trustStorePath =
	// "\\\\hrcc\\Scripts\\jars\\bll.jks";

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		Logger logger = JulUtils.getLogger();
		String url = args[0];
		String userName = args[1];
		String userPassword = args[2];
		String trustStorePath = args[OzConstants.INT_3];
		String trustStorePassword = args[OzConstants.INT_4];
		String outputFilePath = args[OzConstants.INT_5];

		HttpClient httpclient = new HttpClient();
		httpclient.getParams().setAuthenticationPreemptive(true);
		Credentials defaultcreds = new UsernamePasswordCredentials(userName, userPassword);
		httpclient.getState().setCredentials(AuthScope.ANY, defaultcreds);
		System.setProperty("javax.net.ssl.trustStore", trustStorePath);
		System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
		GetMethod httpget = new GetMethod(url);
		try {
			httpclient.executeMethod(httpget);
			logger.info(httpget.getStatusLine().toString());
			String responseBody = httpget.getResponseBodyAsString();
			logger.finest(responseBody);
			FileUtils.writeFile(new File(outputFilePath), responseBody);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.severe(ex.getMessage());

		} finally {
			httpget.releaseConnection();
		}
	}
}
