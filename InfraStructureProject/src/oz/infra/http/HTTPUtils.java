/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package oz.infra.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.datetime.StopWatch;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.LineProcessor;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.string.StringUtils;
import oz.infra.varargs.VarArgsUtils;

/**
 * How to send a request directly using {@link HttpClient}.
 * 
 * @since 4.0
 */
public class HTTPUtils {
	private static Logger logger = JulUtils.getLogger();

	public static int downloadGzipFromUrl(final String url, final String targetFolder) {
		int returnCode = Integer.MIN_VALUE;
		StopWatch stopWatch = new StopWatch();
		logger.info("url: " + url + " targetFolder: " + targetFolder);
		String[] urlArray = url.split(OzConstants.SLASH);
		String fileName = urlArray[urlArray.length - 1];
		String targetPath = targetFolder + File.separator + fileName;
		logger.info("targetPath: " + targetPath);
		try {
			URL site = new URL(url);
			URLConnection urlConnectionc = site.openConnection();

			java.io.BufferedInputStream bufferedInputStream = new java.io.BufferedInputStream(
					urlConnectionc.getInputStream());
			GZIPInputStream gzipInputStream = new GZIPInputStream(bufferedInputStream);

			java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(targetPath);
			GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);

			byte[] data = new byte[OzConstants.INT_10000];

			int gsize = 0;
			while ((gsize = gzipInputStream.read(data)) != -1) {
				gzipOutputStream.write(data, 0, gsize);
			}

			gzipOutputStream.flush();
			gzipOutputStream.close();
			bufferedInputStream.close();
			stopWatch.logElapsedTimeMessage(targetPath + " download has completed in");
			returnCode = 0;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
		return returnCode;
	}

	private static void formatResponse(final HttpResponse response) {
		StringBuilder sb = new StringBuilder();
		Header[] headers = response.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			sb.append("\nheader " + String.valueOf(i) + ": ");
			sb.append(headers[i].toString());
		}
		sb.append("\nstatus line: " + response.getStatusLine().toString());
		sb.append("\nlocale: line: " + response.getLocale().toString());
		sb.append("\n");
		logger.info(sb.toString());
	}

	public static String getHostName(final String fullUrl) {
		HttpHost httpHost = getHttpHost(fullUrl);
		String hostName = null;
		if (httpHost != null) {
			hostName = httpHost.getHostName();
		}
		logger.finest("fullUrl: " + fullUrl + " hostName: " + hostName);
		return hostName;
	}

	private static DefaultHttpClient getHttpClient(final HttpHost target) {
		final int socketTimeOut = OzConstants.INT_15000;
		// general setup
		SchemeRegistry supportedSchemes = new SchemeRegistry();
		// Register the "http" protocol scheme, it is required
		// by the default operator to look up socket factories.
		supportedSchemes.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), OzConstants.INT_80));
		// prepare parameters
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, socketTimeOut); // miliseconds
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		HttpProtocolParams.setUseExpectContinue(params, true);
		ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params, supportedSchemes);
		DefaultHttpClient httpclient = new DefaultHttpClient(connMgr, params);
		return httpclient;
	}

	private static HttpHost getHttpHost(final String fullUrl) {
		URLBreakDown urLBreakDown = new URLBreakDown(fullUrl);
		HttpHost httpHost = new HttpHost(urLBreakDown.getHost(), urLBreakDown.getPort(), urLBreakDown.getProtocol());
		logger.finest(httpHost.toString());
		return httpHost;
	}

	private static HttpResponse getHttpResponse(final DefaultHttpClient httpclient, final HttpHost target,
			final String uri) {
		HttpGet request = new HttpGet(uri);
		logger.finest("executing request to " + target.toString());
		HttpResponse response = null;
		try {
			response = httpclient.execute(target, request);
		} catch (Exception ex) {
			logger.info("target: " + target.toString() + " uri: " + uri);
			logger.warning(ExceptionUtils.getStackTrace(ex));
			logger.warning(ex.getMessage());
		}
		return response;
	}

	private static String getPageContents(final HttpHost target, final String uri,
			final UsernamePasswordCredentials usernamePasswordCredentials) {
		DefaultHttpClient httpclient = getHttpClient(target);
		if (usernamePasswordCredentials != null) {
			AuthScope authScope = new AuthScope(target.getHostName(), target.getPort());
			httpclient.getCredentialsProvider().setCredentials(authScope, usernamePasswordCredentials);
		}
		HttpResponse response = getHttpResponse(httpclient, target, uri);
		String entityString = null;
		if (response != null) {
			// formatResponse(response);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					StringBuilder sb = new StringBuilder();
					StatusLine statusLine = response.getStatusLine();
					sb.append("\ntarget: " + target.toURI() + uri + "\nstatus line: " + statusLine.toString());
					Header contentType = entity.getContentType();
					if (contentType != null) {
						sb.append(" ContentType: " + contentType.toString());
					}
					sb.append(" ContentLength: " + String.valueOf(entity.getContentLength()));
					entityString = EntityUtils.toString(entity);
					if (statusLine.getStatusCode() != OzConstants.HTTP_STATUS_200) {
						logger.info(sb.toString());
						logger.info(entityString);
					}
				}
			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
			}
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
		return entityString;
	}

	public static final String getPageContents(final Properties connectionProperties) {
		HttpHost target = new HttpHost(connectionProperties.getProperty("hostname"),
				Integer.parseInt(connectionProperties.getProperty("port")),
				connectionProperties.getProperty("protocol"));
		logger.finest("target: " + target.toHostString());
		UsernamePasswordCredentials usernamePasswordCredentials = getUsernamePasswordCredentials(
				connectionProperties.getProperty("username"), connectionProperties.getProperty("password"));
		return getPageContents(target, connectionProperties.getProperty("uri"), usernamePasswordCredentials);
	}

	public static final String getPageContents(final String fullUrl) {
		URLBreakDown urLBreakDown = new URLBreakDown(fullUrl);
		return getPageContents(getHttpHost(fullUrl), urLBreakDown.getFileName(), null);
	}

	public static final String getPageContents(final String hostname, final int port, final String protocol,
			final String uri) {
		HttpHost target = new HttpHost(hostname, port, protocol);
		return getPageContents(target, uri, null);
	}

	public static final String getPageContents(final String fullUrl, final String uri) {
		return getPageContents(getHttpHost(fullUrl), uri, null);
	}

	public static final String getPageContents(final String fullUrl, final String username, final String password) {
		UsernamePasswordCredentials usernamePasswordCredentials = getUsernamePasswordCredentials(username, password);
		URLBreakDown urLBreakDown = new URLBreakDown(fullUrl);
		return getPageContents(getHttpHost(fullUrl), urLBreakDown.getFileName(), usernamePasswordCredentials);
	}

	public static final String getPageContents(final String fullUrl, final String uri, final String username,
			final String password) {
		UsernamePasswordCredentials usernamePasswordCredentials = getUsernamePasswordCredentials(username, password);
		return getPageContents(getHttpHost(fullUrl), uri, usernamePasswordCredentials);
	}

	public static String getPageContents1(final String fullUrl) {
		URL u;
		InputStream is = null;
		DataInputStream dis;
		String pageContents;
		//
		int records = 0;
		StopWatch stopWatch = new StopWatch();
		StringBuilder sb = new StringBuilder();
		try {

			// ------------------------------------------------------------//
			// Step 2: Create the URL. //
			// ------------------------------------------------------------//
			// Note: Put your real URL here, or better yet, read it as a //
			// command-line arg, or read it from a file. //
			// ------------------------------------------------------------//

			// u = new URL("http://200.210.220.1:8080/index.shtml");
			// u = new
			// URL("http://fibiux02.fibi.corp:81/access_2011-03-31-10-00");
			// u = new
			// URL("http://snifp-http01.fibi.corp/access/access_2011-03-31-10-00");
			// u = new URL("http://snifp-http01.fibi.corp/access/");
			u = new URL(fullUrl);
			// ----------------------------------------------//
			// Step 3: Open an input stream from the url. //
			// ----------------------------------------------//

			is = u.openStream(); // throws an IOException

			// -------------------------------------------------------------//
			// Step 4: //
			// -------------------------------------------------------------//
			// Convert the InputStream to a buffered DataInputStream. //
			// Buffering the stream makes the reading faster; the //
			// readLine() method of the DataInputStream makes the reading //
			// easier. //
			// -------------------------------------------------------------//

			dis = new DataInputStream(new BufferedInputStream(is));

			// ------------------------------------------------------------//
			// Step 5: //
			// ------------------------------------------------------------//
			// Now just read each record of the input stream, and print //
			// it out. Note that it's assumed that this problem is run //
			// from a command-line, not from an application or applet. //
			// ------------------------------------------------------------//

			while ((pageContents = dis.readLine()) != null) {
				// System.out.println(s);
				records++;
				sb.append(pageContents);
			}

		} catch (MalformedURLException mue) {
			logger.warning("Ouch - a MalformedURLException happened.");
			logger.warning(ExceptionUtils.getStackTrace(mue));
		} catch (IOException ioe) {
			logger.warning("Oops- an IOException happened.");
			logger.warning(ExceptionUtils.getStackTrace(ioe));
		} finally {

			// ---------------------------------//
			// Step 6: Close the InputStream //
			// ---------------------------------//

			try {
				is.close();
				logger.fine("input stream has been closed");
			} catch (IOException ioe) {
				logger.warning("Oops- an IOException happened.");
				logger.warning(ExceptionUtils.getStackTrace(ioe));
			}

		} // end of 'finally' clause
		String summaryMessage = StringUtils.concat(String.valueOf(sb.length()), " bytes, ", String.valueOf(records),
				" records have been read in ", String.valueOf(stopWatch.getElapsedTimeLong()), " milliseconds.");
		logger.fine(summaryMessage);
		return sb.toString();
	}

	public static String getProtectedPageContentsWithBasicAuthRawHeaders(final Properties properties) {
		String encryptionMethodString = properties.getProperty(ParametersUtils.ENCRYPTION_METHOD);
		EncryptionMethodEnum encryptionMethod = EncryptionMethodEnum.valueOf(encryptionMethodString);
		String password = CryptographyUtils.decryptString(properties.getProperty(ParametersUtils.PASSWORD),
				encryptionMethod);
		String pageContents = getProtectedPageContentsWithBasicAuthRawHeaders(
				properties.getProperty(ParametersUtils.URL), properties.getProperty(ParametersUtils.USER_NAME),
				password);
		return pageContents;
	}

	public static String getProtectedPageContentsWithBasicAuthRawHeaders(final String url, final String username,
			final String password, final String... charsets) {
		HttpResponse response = getProtectedPageResponseWithBasicAuthRawHeaders(url, username, password, charsets);
		int statusCode = response.getStatusLine().getStatusCode();
		logger.finest(String.valueOf(statusCode));
		String pageContents = null;
		try {
			if (statusCode == OzConstants.HTTP_STATUS_200) {
				HttpEntity httpEntity = response.getEntity();
				pageContents = EntityUtils.toString(httpEntity);
			} else {
				logger.warning(response.getStatusLine().toString());
			}
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return pageContents;
	}

	public static String getProtectedPageContentsWithPreemptiveBasicAuthentication(final Properties properties) {
		String encryptionMethodString = properties.getProperty(ParametersUtils.ENCRYPTION_METHOD);
		EncryptionMethodEnum encryptionMethod = EncryptionMethodEnum.valueOf(encryptionMethodString);
		String password = CryptographyUtils.decryptString(properties.getProperty(ParametersUtils.PASSWORD),
				encryptionMethod);
		int port = Integer.parseInt(properties.getProperty(ParametersUtils.PORT));
		String pageContents = getProtectedPageContentsWithPreemptiveBasicAuthentication(
				properties.getProperty(ParametersUtils.HOST), port, properties.getProperty(ParametersUtils.PROTOCOL),
				properties.getProperty(ParametersUtils.URL), properties.getProperty(ParametersUtils.USER_NAME),
				password);
		return pageContents;
	}

	public static String getProtectedPageContentsWithPreemptiveBasicAuthentication(final String host, final int port,
			final String protocol, final String url, final String username, final String password) {
		HttpHost targetHost = new HttpHost(host, port, protocol);
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

	public static HttpResponse getProtectedPageResponseWithBasicAuthRawHeaders(final String url, final String username,
			final String password, final String... charsets) {
		String charset = VarArgsUtils.getMyArg(OzConstants.ISO_8859_1, charsets);
		HttpGet request = new HttpGet(url);
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName(charset)));
		String authHeader = "Basic " + new String(encodedAuth);
		request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		HttpClient client = HttpClientBuilder.create().build();

		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return response;
	}

	public static StatusLine getProtectedPageStausLineWithBasicAuthRawHeaders(final String url, final String username,
			final String password, final String... charsets) {
		HttpResponse response = getProtectedPageResponseWithBasicAuthRawHeaders(url, username, password, charsets);
		StatusLine statusLine = null;
		if (response != null) {
			statusLine = response.getStatusLine();
		}
		return statusLine;
	}

	public static final StatusLine getStatusLine(final String fullUrl) {
		URLBreakDown urLBreakDown = new URLBreakDown(fullUrl);
		return getStatusLine(fullUrl, urLBreakDown.getFileName());
	}

	public static final StatusLine getStatusLine(final String fullUrl, final String uri) {
		StatusLine statusLine = null;
		logger.finest("Starting ...");
		HttpHost target = getHttpHost(fullUrl);
		DefaultHttpClient httpclient = getHttpClient(getHttpHost(fullUrl));
		HttpResponse response = getHttpResponse(httpclient, target, uri);
		if (response != null) {
			statusLine = response.getStatusLine();
			logger.finest(fullUrl + " Staus line: " + statusLine.toString());
		}
		return statusLine;
	}

	public static final StatusLine getStatusLine(final String[] urlArray) {
		if (urlArray.length == 1) {
			return getStatusLine(urlArray[0]);
		} else if (urlArray.length == 2) {
			return getStatusLine(urlArray[0], urlArray[1]);
		} else {
			logger.warning("Array should have 1 or 2 entries. has " + String.valueOf(urlArray.length));
			return null;
		}
	}

	public static UsernamePasswordCredentials getUsernamePasswordCredentials(final String username,
			final String password) {
		if (username != null) {
			return new UsernamePasswordCredentials(username, password);
		} else {
			return null;
		}
	}

	public static int processResponseRecords(final String fullUrl,
			final UsernamePasswordCredentials usernamePasswordCredentials, final LineProcessor[] lineProcessorsArray) {
		int recordCount = 0;
		URLBreakDown urLBreakDown = new URLBreakDown(fullUrl);
		HttpHost httpHost = new HttpHost(urLBreakDown.getHost(), urLBreakDown.getPort(), urLBreakDown.getProtocol());
		DefaultHttpClient httpclient = getHttpClient(httpHost);
		if (usernamePasswordCredentials != null) {
			AuthScope authScope = new AuthScope(httpHost.getHostName(), httpHost.getPort());
			httpclient.getCredentialsProvider().setCredentials(authScope, usernamePasswordCredentials);
		}
		HttpResponse response = getHttpResponse(httpclient, httpHost, urLBreakDown.getFileName());
		if (response != null) {
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					StringBuilder sb = new StringBuilder();
					sb.append("target: " + httpHost.toURI() + urLBreakDown.getFileName() + " status line: "
							+ response.getStatusLine().toString());
					Header header = entity.getContentType();
					if (header != null) {
						sb.append(" ContentType: " + header.toString());
					}
					sb.append(" ContentLength: " + String.valueOf(entity.getContentLength()));
					logger.info(sb.toString());
					// entityString = EntityUtils.toString(entity);
					// logger.finest(entityString);
					BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
					InputStream inputStream = bufferedHttpEntity.getContent();
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
					String line = null;
					while ((line = bufferedReader.readLine()) != null) {
						recordCount++;
						if (logger.isLoggable(Level.FINE)) {
							logger.fine("line: " + line);
						}
						for (LineProcessor lineProcessor1 : lineProcessorsArray) {
							lineProcessor1.processLine(line);
						}
					}
					bufferedReader.close();
					logger.info(String.valueOf(recordCount) + " records records have been processed from "
							+ urLBreakDown.getFileName());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.warning(ex.getMessage());
			}
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
			for (LineProcessor lineProcessor1 : lineProcessorsArray) {
				lineProcessor1.processEOF(new Integer(recordCount));
			}
		}
		return recordCount;
	}
}
