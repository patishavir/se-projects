package oz.temp.hosts;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.net.ip.IpUtils;
import oz.infra.system.SystemUtils;

public class HostsCompare {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		doStuff("localhost");
		doStuff("w284uea8");
		doStuff("w284uea8.fibi.corp");
		doStuff("10.12.101.197");

		doStuff("s5380440");
		doStuff("S5380440");
		doStuff("S5380440.FIBI.CORP");
		doStuff("MATAFcc");
		doStuff("matafcc.FIBI.CORP");
		doStuff("10.18.189.183");
	//	doStuff("99910.12888.101777.197666");
	}

	private static void doStuff(final String hostName) {
		IpUtils.getIpAddress(hostName);
		SystemUtils.isCurrentHost(hostName);
		// try {
		// logger.info("hostName: " + hostName + " ip address: " + InetAddress.getByName(hostName));
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
	}
}
