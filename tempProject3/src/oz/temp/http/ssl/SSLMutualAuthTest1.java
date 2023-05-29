package oz.temp.http.ssl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContexts;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SSLMutualAuthTest1 {

	public static void main(String[] args) {
		System.out.println("MagicDude4Eva 2-way / mutual SSL-authentication test");
		org.apache.log4j.BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);
		try {
			String CERT_ALIAS = "s5381310.fibi.corp", CERT_PASSWORD = "kspass";
			KeyStore identityKeyStore = KeyStore.getInstance("pkcs12");
			FileInputStream identityKeyStoreFile = new FileInputStream(new File("C:\\oj\\projects\\se\\tempProject3\\args\\ssl\\W284UEA8.p12"));
			identityKeyStore.load(identityKeyStoreFile, CERT_PASSWORD.toCharArray());

			KeyStore trustKeyStore = KeyStore.getInstance("jks");
			FileInputStream trustKeyStoreFile = new FileInputStream(new File("C:\\oj\\projects\\se\\tempProject3\\args\\ssl\\FIBICA_trustStore.jks"));
			trustKeyStore.load(trustKeyStoreFile, "tspass".toCharArray());
			SSLContext sslContext = SSLContexts.custom()
					// load identity keystore
					.loadKeyMaterial(identityKeyStore, CERT_PASSWORD.toCharArray(), new PrivateKeyStrategy() {
						@Override
						public String chooseAlias(Map<String, PrivateKeyDetails> aliases, Socket socket) {
							return CERT_ALIAS;
						}
					})
					// load trust keystore
					.loadTrustMaterial(trustKeyStore, null).build();
			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1.2", "TLSv1.1" },
					null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
			// Call a SSL-endpoint
			callEndPoint(client, "https://s5381310.fibi.corp/oded.html", "STRING 1");
		} catch (Exception ex) {
			System.out.println("Boom, we failed: " + ex);
			ex.printStackTrace();
		}
	}

	private static void callEndPoint(CloseableHttpClient aHTTPClient, String aEndPointURL, String aPostParams) {

		try {
			System.out.println("Calling URL: " + aEndPointURL);

			HttpGet request = new HttpGet(aEndPointURL);
			// add request header
			request.addHeader("User-Agent", "my user agent");
			HttpClient client = HttpClientBuilder.create().build();

			HttpResponse response = client.execute(request);
			int responseCode = response.getStatusLine().getStatusCode();
			System.out.println("Response Code: " + responseCode);
			System.out.println("**POST** request Url: " + response.toString());
			System.out.println("Parameters : " + aPostParams);

			System.out.println("Content:-\n");
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception ex) {
			System.out.println("Boom, we failed: " + ex);
			ex.printStackTrace();
		}
	}

}