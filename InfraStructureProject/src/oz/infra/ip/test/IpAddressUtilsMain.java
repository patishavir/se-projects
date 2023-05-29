package oz.infra.ip.test;

import oz.infra.ip.IpAddressUtils;

public class IpAddressUtilsMain {

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		IpAddressUtils.getFreeIpAddresses(args[0], Integer.parseInt(args[1]));
	}

}
