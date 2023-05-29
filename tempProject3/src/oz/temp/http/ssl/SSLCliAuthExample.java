package oz.temp.http.ssl;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

// public class SSLCliAuthExample {
public class SSLCliAuthExample {

	private static final Logger LOG = Logger.getLogger(SSLCliAuthExample.class.getName());

	private static final String CA_KEYSTORE_TYPE = KeyStore.getDefaultType(); // "JKS";
	private static final String CA_KEYSTORE_PATH = "C:\\oj\\projects\\se\\tempProject3\\args\\ssl\\FIBICA_trustStore.jks";
	private static final String CA_KEYSTORE_PASS = "tspass";

	private static final String CLIENT_KEYSTORE_TYPE = "PKCS12";
	private static final String CLIENT_KEYSTORE_PATH = "C:\\oj\\projects\\se\\tempProject3\\args\\ssl\\W284UEA8.p12";
	private static final String CLIENT_KEYSTORE_PASS = "kspass";

	public static void main(String[] args) throws Exception {
		requestTimestamp();
	}

	public final static void requestTimestamp() throws Exception {
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(createSslCustomContext(), new String[] { "TLSv1" }, // Allow TLSv1 protocol only
				null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(csf).build()) {
			HttpPost req = new HttpPost("https://s5381310.fibi.corp/oded.html");
			// req.setConfig(configureRequest());
			HttpEntity ent = new InputStreamEntity(new FileInputStream("C:\\oj\\projects\\se\\tempProject3\\args\\ssl\\jvm.args"));
			req.setEntity(ent);
			try (CloseableHttpResponse response = httpclient.execute(req)) {
				HttpEntity entity = response.getEntity();
				LOG.log(Level.INFO, "*** Reponse status: {0}", response.getStatusLine());
				EntityUtils.consume(entity);
				LOG.log(Level.INFO, "*** Response entity: {0}", entity.toString());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static RequestConfig configureRequest() {
		HttpHost proxy = new HttpHost("changeit.local", 8080, "http");
		RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
		return config;
	}

	public static SSLContext createSslCustomContext()
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, KeyManagementException, UnrecoverableKeyException {
		// Trusted CA keystore
		KeyStore tks = KeyStore.getInstance(CA_KEYSTORE_TYPE);
		tks.load(new FileInputStream(CA_KEYSTORE_PATH), CA_KEYSTORE_PASS.toCharArray());

		// Client keystore
		KeyStore cks = KeyStore.getInstance(CLIENT_KEYSTORE_TYPE);
		cks.load(new FileInputStream(CLIENT_KEYSTORE_PATH), CLIENT_KEYSTORE_PASS.toCharArray());

		SSLContext sslcontext = SSLContexts.custom()
				// .loadTrustMaterial(tks, new TrustSelfSignedStrategy()) // use it to customize
				.loadKeyMaterial(cks, CLIENT_KEYSTORE_PASS.toCharArray()) // load client certificate
				.build();
		return sslcontext;
	}



}