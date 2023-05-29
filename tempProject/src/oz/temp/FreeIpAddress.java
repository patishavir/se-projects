package oz.temp;

import java.net.InetAddress;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;

public class FreeIpAddress {
	private static final Logger logger = JulUtils.getLogger();

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String ipaddress = args[0];
		int count = Integer.parseInt(args[1]);
		String[] ipStringArray = ipaddress.split(RegexpUtils.REGEXP_DOT);
		byte[] byteAddressArray = new byte[ipStringArray.length];
		for (int i = 0; i < byteAddressArray.length; i++) {
			int int1 = Integer.parseInt(ipStringArray[i]);
			byteAddressArray[i] = (byte) int1;
		}
		for (int j = 0; j < count; j++) {
			String hostname = getHost(byteAddressArray);
			if (RegexpUtils.matches(hostname, RegexpUtils.REGEXP_IPV4_ADDRESS)) {
				logger.info(hostname + "============== ip is free =====================");
			} else {
				logger.info(hostname);
			}
			byteAddressArray[byteAddressArray.length - 1] = (byte) ++byteAddressArray[byteAddressArray.length - 1];
		}
	}

}
