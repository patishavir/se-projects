package oz.infra.ip;

import java.net.InetAddress;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;

public class IpAddressUtils {
	private static final Logger logger = JulUtils.getLogger();

	public static void getFreeIpAddresses(final String ipaddress, final int count) {
		// TODO Auto-generated method stub
		String[] ipStringArray = ipaddress.split(RegexpUtils.REGEXP_DOT);
		byte[] byteAddressArray = new byte[ipStringArray.length];
		for (int i = 0; i < byteAddressArray.length; i++) {
			int int1 = Integer.parseInt(ipStringArray[i]);
			byteAddressArray[i] = (byte) int1;
		}
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < count; j++) {
			String hostname = getHost(byteAddressArray);
			if (RegexpUtils.matches(hostname, RegexpUtils.REGEXP_IPV4_ADDRESS)) {
				sb.append("\n" + getIpAddressAsString(byteAddressArray) + "\t" + hostname
						+ "\t============== ip is free =====================");
			} else {
				sb.append("\n" + getIpAddressAsString(byteAddressArray) + "\t" + hostname);
			}
			byteAddressArray[byteAddressArray.length - 1] = (byte) ++byteAddressArray[byteAddressArray.length - 1];
		}
		logger.info(sb.toString());
	}

	private static String getHost(final byte[] address) {
		String hostname = null;
		try {
			InetAddress inetAddress = InetAddress.getByAddress(address);
			hostname = inetAddress.getCanonicalHostName();
			logger.finest(inetAddress.getCanonicalHostName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return hostname;
	}

	public static String getIpAddressAsString(final byte[] byteAddressArray) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < byteAddressArray.length; i++) {
			int int1 = byteAddressArray[i];
			if (int1 < 0) {
				int1 = OzConstants.INT_256 + int1;
			}
			sb.append(String.valueOf(int1));
			if (i < byteAddressArray.length - 1) {
				sb.append(OzConstants.DOT);
			}
		}
		return sb.toString();
	}
}
