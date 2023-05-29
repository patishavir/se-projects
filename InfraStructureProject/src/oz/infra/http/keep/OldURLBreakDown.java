package oz.infra.http.keep;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.string.StringUtils;

public class OldURLBreakDown {
	private String fullUrl = null;
	private String protocol = null;
	private String host = null;
	private int port = Integer.MIN_VALUE;
	private String uri = null;
	private static Logger logger = JulUtils.getLogger();

	public OldURLBreakDown(final String fullUrl) {
		this.fullUrl = fullUrl;
		protocol = "http";
		port = OzConstants.INT_80;
		host = fullUrl;
		int colonFollowingProtocolIndex = fullUrl.indexOf(":");
		int hostIndex = colonFollowingProtocolIndex + OzConstants.INT_3;
		int slashFollowingHostIndex = fullUrl.indexOf(OzConstants.SLASH, hostIndex);
		if (colonFollowingProtocolIndex > -1) {
			protocol = fullUrl.substring(0, colonFollowingProtocolIndex);
			int beforPortColonIndex = fullUrl.indexOf(":", colonFollowingProtocolIndex + 1);
			if (beforPortColonIndex > colonFollowingProtocolIndex && beforPortColonIndex < slashFollowingHostIndex) {
				// port specified
				String portString = StringUtils.getSubStringMatchingPattern(fullUrl, RegexpUtils.REGEXP_DIGIT,
						beforPortColonIndex + 1);
				port = Integer.parseInt(portString);
				host = fullUrl.substring(hostIndex, beforPortColonIndex);
				uri = fullUrl.substring(beforPortColonIndex + portString.length() + 1);
			} else {
				// port not specified
				if (slashFollowingHostIndex < 0) {
					host = fullUrl.substring(hostIndex);
				} else {
					host = fullUrl.substring(hostIndex, slashFollowingHostIndex);
					uri = fullUrl.substring(slashFollowingHostIndex);
				}

			}
		}
		logger.finest("full url: " + fullUrl + " protocol: " + protocol + "  host: " + host + "  port: "
				+ String.valueOf(port) + " uri: " + uri);

	}

	public final String getProtocol() {
		return protocol;
	}

	public final String getHost() {
		return host;
	}

	public final int getPort() {
		return port;
	}

	public final String getUri() {
		return uri;
	}

	public String toString() {
		String urlBreakDownString = StringUtils.concat("full url: ", fullUrl, " protocol: ", protocol, "  host: ",
				host, "  port: ", String.valueOf(port), " uri: ", uri);
		return urlBreakDownString;
	}

}
