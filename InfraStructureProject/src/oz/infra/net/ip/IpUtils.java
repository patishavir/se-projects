package oz.infra.net.ip;

import java.net.InetAddress;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;

public class IpUtils {
	private static Logger logger = JulUtils.getLogger();

	public static String getIpAddress(final String host) {
		String hostAddress = null;
		try {
			InetAddress inetAddress = InetAddress.getByName(host);
			hostAddress = inetAddress.getHostAddress();
			logger.finest("host: " + host + " ipAddress: " + hostAddress);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return hostAddress;
	}

	public static int[] getIpAddressAsIntArray(final String ipAddress) {
		String[] octetsArray = getIpAddressAsOctetsArray(ipAddress);
		int[] intArray = new int[octetsArray.length];
		for (int i = 0; i < octetsArray.length; i++) {
			intArray[i] = Integer.parseInt(octetsArray[i]);
		}
		return intArray;
	}

	public static String[] getIpAddressAsOctetsArray(final String ipAddress) {
		String[] octetsArray = null;
		if (ipAddress != null) {
			octetsArray = ipAddress.split(OzConstants.BACK_SLASH + OzConstants.DOT);
		}
		return octetsArray;
	}

	public static String getLeftMost3Octets(final String ipAddress) {
		String leftMost3Octets = null;
		if (ipAddress != null && isIpAddressValid(ipAddress)) {
			leftMost3Octets = ipAddress.substring(0, ipAddress.lastIndexOf(OzConstants.DOT));
		}
		return leftMost3Octets;
	}

	public static boolean isIpAddressValid(final String ipAddress) {
		String[] octetsArray = getIpAddressAsOctetsArray(ipAddress);
		int[] intArray = new int[octetsArray.length];
		boolean validIpAddress = (octetsArray.length == 4);
		for (int i = 0; i < octetsArray.length && validIpAddress; i++) {
			int num = Integer.parseInt(octetsArray[i]);
			intArray[i] = num;
			if (num < 0 || num > 255) {
				validIpAddress = false;
				break;
			}
		}
		if (!validIpAddress) {
			logger.warning("ipAddress: " + ipAddress + " is an invalid ip address!");
		}
		return validIpAddress;
	}
}
