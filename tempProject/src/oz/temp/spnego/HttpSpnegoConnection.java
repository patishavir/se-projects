package oz.temp.spnego;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpSpnegoConnection {

	/**
	 * Uses HttpClient 4.3.4 and CommonsIO 2.4
	 */
	public static void main(String[] args) throws ClientProtocolException, IOException {
		SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		String domain = "fibi.corp";
		String kdc = "fibi.corp";
		// careful with aliases, 'somewebsite' has to be in the kerberos
		// database
		String url = "http://snifp-http/snoop";
		// if the port does not match the kerberos database entry, skip it
		// during the lookup
		boolean skipPortAtKerberosDatabaseLookup = true;

		File krb5Config = createKrb5Configuration(domain, kdc);

		System.out.println("Created config '" + krb5Config.getAbsolutePath() + "':");
		System.out.println(IOUtils.toString(new FileReader(krb5Config)));

		System.setProperty("java.security.krb5.conf", krb5Config.toURI().toString());
		System.setProperty("sun.security.krb5.debug", "true");
		System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");

		Lookup<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider> create()
				.register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory(skipPortAtKerberosDatabaseLookup)).build();

		CloseableHttpClient client = HttpClients.custom().setDefaultAuthSchemeRegistry(authSchemeRegistry).build();
		HttpClientContext context = HttpClientContext.create();
		BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();

		// This may seem odd, but specifying 'null' as principal tells java to
		// use the logged in user's credentials
		Credentials useJaasCreds = new Credentials() {

			public String getPassword() {
				return null;
			}

			public Principal getUserPrincipal() {
				return null;
			}

		};
		credentialsProvider.setCredentials(new AuthScope(null, -1, null), useJaasCreds);
		context.setCredentialsProvider(credentialsProvider);

		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = client.execute(httpget, context);
		String responseString = IOUtils.toString(response.getEntity().getContent());

		System.out.println("HTTP Response:");
		System.out.println(responseString);
	}

	/**
	 * Creates a temporary krb5.conf [libdefaults] default_realm = <domain>
	 * 
	 * [realms] snb.ch = { kdc = <kdc> admin_server = <kdc> }
	 */
	private static File createKrb5Configuration(String domain, String kdc) throws IOException {
		File tempFile = File.createTempFile("krb5", "kdc");
		ArrayList<String> lines = new ArrayList<>();
		lines.add("[libdefaults]");
		lines.add("\tdefault_realm = " + domain);
		lines.add("[realms]");
		lines.add("\t" + domain + " = {");
		lines.add("\t\tkdc = " + kdc);
		lines.add("\t\tadmin_server = " + kdc);
		lines.add("\t}");
		FileWriter writer = new FileWriter(tempFile);
		IOUtils.writeLines(lines, System.lineSeparator(), writer);
		IOUtils.closeQuietly(writer);
		return tempFile;
	}

}