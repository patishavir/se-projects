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
package oz.infra.http.win;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.WinHttpClients;
import org.apache.http.util.EntityUtils;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.exception.ExceptionUtils;
import oz.infra.inputstream.InputStreamUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;
import oz.infra.varargs.VarArgsUtils;

/**
 * This example demonstrates how to create HttpClient pre-configured with
 * support for integrated Windows authentication.
 */
public class HttpWinClientUtils {
	private static boolean verbose = false;
	private static Logger logger = JulUtils.getLogger();

	public static Map<String, String> getMultiplePageContents(final String[] urlsArray) {
		CloseableHttpClient httpClient = HttpWinClientUtils.getWinHttpClient();
		Map<String, String> resultMap = new HashMap<String, String>();
		for (int i = 0; i < urlsArray.length; i++) {
			String myUrl = urlsArray[i];
			String pageContents = null;
			if ((myUrl.trim().length() > 0) && (!myUrl.startsWith(OzConstants.HASH))) {
				pageContents = HttpWinClientUtils.getProtectedPageContents(myUrl, httpClient);
				resultMap.put(myUrl, pageContents);
			} else {
				logger.info("skipping url: " + myUrl);
			}
		}
		return resultMap;
	}

	public static final String getProtectedPageContents(final String url, final CloseableHttpClient... httpClients) {
		StopWatch stopWatch = new StopWatch();
		String pageContents = null;
		boolean doCloseHttpClient = false;
		CloseableHttpClient httpClient = VarArgsUtils.getMyArg(null, httpClients);
		if (httpClient == null) {
			httpClient = getWinHttpClient();
			doCloseHttpClient = true;
		}
		if (httpClient != null) {
			// There is no need to provide user credentials
			// HttpClient will attempt to access current user security context
			// through Windows platform specific methods via JNI.
			CloseableHttpResponse response = null;
			try {
				HttpGet httpget = new HttpGet(url);
				logger.info("Executing request " + httpget.getRequestLine());
				response = httpClient.execute(httpget);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					logger.warning(statusLine.toString());
				}
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				pageContents = InputStreamUtils.convertStreamToStr(is);
				if (verbose) {
					long contentLength = entity.getContentLength();
					logger.info("net content length: " + String.valueOf(contentLength) + " content length = "
							+ String.valueOf(pageContents.length()));
					logAllHeaders(response);
					logger.info("content:\n" + pageContents);
				}

			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
			} finally {
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
					if (doCloseHttpClient) {
						httpClient.close();
					}
				} catch (Exception ex) {
					ExceptionUtils.printMessageAndStackTrace(ex);
				}
			}
		} else {
			logger.warning("Integrated Win auth is not supported !");
		}
		String elapsedTimeMessage = "get contents for " + url + " has completed in " + stopWatch.getElapsedTimeString();
		logger.info(elapsedTimeMessage);
		return pageContents;
	}

	public final static CloseableHttpClient getWinHttpClient() {
		CloseableHttpClient httpclient = null;
		if (WinHttpClients.isWinAuthAvailable()) {
			httpclient = WinHttpClients.createDefault();
		}
		return httpclient;
	}

	public static void logAllHeaders(final CloseableHttpResponse response) {
		StringBuilder sb = new StringBuilder("response headers:\n");
		Header[] headers = response.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			sb.append(headers[i].toString() + SystemUtils.LINE_SEPARATOR);
		}
		sb.append("status line: " + response.getStatusLine().toString() + SystemUtils.LINE_SEPARATOR);
		logger.info(sb.toString());
	}
}
