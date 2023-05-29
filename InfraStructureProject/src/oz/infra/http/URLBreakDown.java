package oz.infra.http;

import java.net.URL;
import java.util.logging.Logger;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class URLBreakDown {
	private static Logger logger = JulUtils.getLogger();

	public static Logger getLogger() {
		return logger;
	}

	private String urlString = null;
	private String protocol = null;
	private String host = null;
	private int port = Integer.MIN_VALUE;
	private String authority = null;
	private String query = null;
	private String fileName = null;

	public URLBreakDown(final String fullUrl) {
		try {
			URL url = new URL(fullUrl);
			urlString = url.toString();
			protocol = url.getProtocol();
			host = url.getHost();
			port = url.getPort();
			authority = url.getAuthority();
			query = url.getQuery();
			fileName = url.getFile();
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	public String getAuthority() {
		return authority;
	}

	public String getFileName() {
		return fileName;
	}

	public final String getHost() {
		return host;
	}

	public final int getPort() {
		return port;
	}

	public final String getProtocol() {
		return protocol;
	}

	public String getQuery() {
		return query;
	}

	public String toString() {
		String urlBreakDownString = StringUtils.concat("full url: ", urlString, " protocol: ", protocol, "  host: ",
				host, "  port: ", String.valueOf(port), " uri: ", fileName);
		return urlBreakDownString;
	}

}
