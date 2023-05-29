package oz.temp.httpclient;

import java.nio.charset.Charset;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.varargs.VarArgsUtils;

public class HttpClientUtils {
	private static final String TEST_URL = "http://s5381355.fibi.corp/snoop";
	private static final String USERNAME = "s177571";
	private static final String PASSWORD = "ujik00";
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info(getProtectedPageContentsWithBasicAuthRawHeaders(USERNAME, PASSWORD, TEST_URL));
		// logger.info(getProtectedPageContentWithPreemptiveBasicAuthentication("s5381355.fibi.corp",
		// 80, "http", TEST_URL,
		// USERNAME, PASSWORD));
		// preemptiveBasicAuthenticationNoParams();
		 Properties properties = getProperties();
		 logger.info(getProtectedPageContentWithPreemptiveBasicAuthentication(properties));
		 // basicAuthentication();
		// xxx();
		// System.out.println(
		// "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		// ClientPreemptiveBasicAuthentication2();
		// logger.info(getProtectedPageContentsWithBasicAuthRawHeaders(USERNAME,
		// PASSWORD, TEST_URL));
	}

	private static String getProtectedPageContentsWithBasicAuthRawHeaders(final String username, final String password,
			final String url, final String... charsets) {
		String charset = VarArgsUtils.getMyArg(OzConstants.ISO_8859_1, charsets);
		HttpGet request = new HttpGet(url);
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName(charset)));
		String authHeader = "Basic " + new String(encodedAuth);
		request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		HttpClient client = HttpClientBuilder.create().build();
		String pageContents = null;
		try {
			HttpResponse response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			logger.finest(String.valueOf(statusCode));
			if (statusCode == OzConstants.HTTP_STATUS_200) {
				HttpEntity httpEntity = response.getEntity();
				pageContents = EntityUtils.toString(httpEntity);
			} else {
				logger.warning(response.getStatusLine().toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pageContents;
	}

	private static Properties getProperties() {
		Properties properties = new Properties();
		properties.put(ParametersUtils.HOST, "s5381355.fibi.corp");
		properties.put(ParametersUtils.PORT, "80");
		properties.put(ParametersUtils.PROTOCOL, OzConstants.HTTP);
		properties.put(ParametersUtils.URL, "http://s5381355.fibi.corp/snoop");
		properties.put(ParametersUtils.USER_NAME, "s177571");
		properties.put(ParametersUtils.ENCRYPTION_METHOD, "NONE");
		properties.put(ParametersUtils.PASSWORD, "ujik00");
		return properties;

	}

	private static String getProtectedPageContentWithPreemptiveBasicAuthentication(final Properties properties) {
		String encryptedPassword = properties.getProperty(ParametersUtils.PASSWORD);
		String encryptionMethodString = properties.getProperty(ParametersUtils.ENCRYPTION_METHOD);
		EncryptionMethodEnum encryptionMethod = EncryptionMethodEnum.valueOf(encryptionMethodString);
		String password = CryptographyUtils.decryptString(properties.getProperty(ParametersUtils.PASSWORD),
				encryptionMethod);
		int port = Integer.parseInt(properties.getProperty(ParametersUtils.PORT));
		return getProtectedPageContentWithPreemptiveBasicAuthentication(properties.getProperty(ParametersUtils.HOST),
				port, properties.getProperty(ParametersUtils.PROTOCOL), properties.getProperty(ParametersUtils.URL),
				properties.getProperty(ParametersUtils.USER_NAME), password);
	}

	private static String getProtectedPageContentWithPreemptiveBasicAuthentication(final String host, final int port,
			final String protocol, final String url, final String username, final String password) {
		HttpHost targetHost = new HttpHost(host, port, "http");
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
		AuthCache authCache = new BasicAuthCache();
		authCache.put(targetHost, new BasicScheme());
		// Add AuthCache to the execution context
		final HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credsProvider);
		context.setAuthCache(authCache);
		String pageContents = null;
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(new HttpGet(url), context);
			int statusCode = response.getStatusLine().getStatusCode();
			logger.finest(String.valueOf(statusCode));
			if (statusCode == OzConstants.HTTP_STATUS_200) {
				HttpEntity httpEntity = response.getEntity();
				pageContents = EntityUtils.toString(httpEntity);
			} else {
				logger.warning(response.getStatusLine().toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pageContents;
	}

	private static void preemptiveBasicAuthenticationNoParams() {
		HttpHost targetHost = new HttpHost("s5381355.fibi.corp", 80, "http");
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("s177571", "ujik00"));

		AuthCache authCache = new BasicAuthCache();
		authCache.put(targetHost, new BasicScheme());

		// Add AuthCache to the execution context
		final HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credsProvider);
		context.setAuthCache(authCache);
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(new HttpGet("http://s5381355.fibi.corp/snoop"), context);

			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println(String.valueOf(statusCode));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void basicAuthentication() {
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(USERNAME, PASSWORD);
		provider.setCredentials(AuthScope.ANY, credentials);
		HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		try {
			HttpResponse response = client.execute(new HttpGet(TEST_URL));
			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println(String.valueOf(statusCode));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void xxx() {
		try {
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope("s5381355.fibi.corp", 80),
					new UsernamePasswordCredentials(USERNAME, PASSWORD));
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
			try {
				HttpGet httpget = new HttpGet(TEST_URL);
				System.out.println("Executing request " + httpget.getRequestLine());
				CloseableHttpResponse response = httpclient.execute(httpget);
				try {
					System.out.println("----------------------------------------");
					System.out.println(response.getStatusLine());
					System.out.println(EntityUtils.toString(response.getEntity()));
				} finally {
					response.close();
				}
			} finally {
				httpclient.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void ClientPreemptiveBasicAuthentication2() {
		try {
			HttpHost target = new HttpHost("s5381355.fibi.corp", 80, "http");
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(target.getHostName(), target.getPort()),
					new UsernamePasswordCredentials(USERNAME, PASSWORD));
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
			try {
				// Create AuthCache instance
				AuthCache authCache = new BasicAuthCache();
				// Generate BASIC scheme object and add it to the local
				// auth cache
				BasicScheme basicAuth = new BasicScheme();
				authCache.put(target, basicAuth);
				// Add AuthCache to the execution context
				HttpClientContext localContext = HttpClientContext.create();
				localContext.setAuthCache(authCache);
				HttpGet httpget = new HttpGet(TEST_URL);
				System.out.println("Executing request " + httpget.getRequestLine() + " to target " + target);
				for (int i = 0; i < 3; i++) {
					CloseableHttpResponse response = httpclient.execute(target, httpget, localContext);
					try {
						System.out.println("----------------------------------------");
						System.out.println(response.getStatusLine());
						System.out.println(EntityUtils.toString(response.getEntity()));
					} finally {
						response.close();
					}
				}
			} finally {
				httpclient.close();
			}

		} catch (

		Exception ex) {
			ex.printStackTrace();
		}
	}
}
