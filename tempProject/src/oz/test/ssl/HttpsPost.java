package oz.test.ssl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

public class HttpsPost {
	public static void main(String[] args) throws Exception {
		SSLContext sslctx = SSLContext.getInstance("SSL");
		sslctx.init(null, new X509TrustManager[] { new MyTrustManager() }, null);

		HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
		URL url = new URL("https://intramgmt:9044/admin");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		PrintStream ps = new PrintStream(con.getOutputStream());
		ps.println("f1=abc&f2=xyz");
		ps.close();
		con.connect();
		if (con.getResponseCode() == HttpsURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			br.close();
		}
		con.disconnect();
	}
}

class MyTrustManager implements X509TrustManager {
	public void checkClientTrusted(X509Certificate[] chain, String authType) {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}
}