package oz.temp.http.ssl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

public class HcSslTest {

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		getPage(args[0]);
		// xxxxx(args[0]);
	}

	private static void getPage(final String myUrl) {
		String url = myUrl;
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		// add request header
		request.addHeader("User-Agent", "my java program");
		try {
			HttpResponse response = client.execute(request);
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode() + "\nResponse: " + response.toString());
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
				System.out.println("line: " + line);
			}
			System.out.println("Result: " + result.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private static void xxxxx(final String myUrl) {
		try {
			String keyPassphrase = "kspass";
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			System.out.println("keyStore: " + keyStore.toString());
			keyStore.load(new FileInputStream("C:\\oj\\projects\\se\\tempProject3\\args\\ssl\\W284UEA8.p12"), keyPassphrase.toCharArray());
			System.out.println("keyStore: " + keyStore.toString());
			SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, null).build();
			HttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();
			HttpResponse response = httpClient.execute(new HttpGet(myUrl));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}