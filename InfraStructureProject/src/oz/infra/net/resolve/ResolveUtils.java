package oz.infra.net.resolve;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;

public class ResolveUtils {
	private static Logger logger = JulUtils.getLogger();

	public static String getHostName(final String ipAddress) {
		String hostname = null;
		try {
			InetAddress addr = InetAddress.getByName(ipAddress);
			hostname = addr.getHostName();
		} catch (UnknownHostException ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		logger.finest("hostname:" + hostname);
		return hostname;
	}

	public static String getCanonicalHostName(final String ipAddress) {
		String canonicalHostName = null;
		try {
			// Get hostname by textual representation of IP address
			InetAddress addr = InetAddress.getByName(ipAddress);
			// Get hostname by a byte array containing the IP address
			// byte[] ipAddr = new byte[]{127, 0, 0, 1};
			// addr = InetAddress.getByAddress(ipAddr);
			// Get canonical host name
			canonicalHostName = addr.getCanonicalHostName();
		} catch (UnknownHostException ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		logger.finest("Canonical host name:" + canonicalHostName);
		return canonicalHostName;
	}
}
