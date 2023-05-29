package oz.infra.net.ip.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import org.junit.Test;

import oz.infra.logging.jul.JulUtils;
import oz.infra.net.ip.IpUtils;

public class JUnitTestIpUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	@Test
	public void TestIpUtils() {
		assertTrue(IpUtils.isIpAddressValid("10.1.1.1"));
		assertTrue(IpUtils.isIpAddressValid("0.0.0.0"));
		assertTrue(IpUtils.isIpAddressValid("254.254.254.254"));
		assertTrue(IpUtils.isIpAddressValid("255.255.255.255"));
		assertTrue(IpUtils.isIpAddressValid("222.222.222.222"));
		assertFalse(IpUtils.isIpAddressValid("222.222.222.2222"));
		assertFalse(IpUtils.isIpAddressValid("10.1.1.1.1"));
		assertFalse(IpUtils.isIpAddressValid("10.1.1.256"));
		assertFalse(IpUtils.isIpAddressValid("10.1.-1.25"));
		assertFalse(IpUtils.isIpAddressValid("YYY"));
	}
}
