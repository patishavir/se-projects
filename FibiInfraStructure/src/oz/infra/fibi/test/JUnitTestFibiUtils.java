package oz.infra.fibi.test;

import java.util.logging.Logger;

import junit.framework.Assert;

import org.junit.Test;

import oz.infra.fibi.FibiUtils;
import oz.infra.logging.jul.JulUtils;

public class JUnitTestFibiUtils {
	private static Logger logger = JulUtils.getLogger();

	@Test
	public void testIp2BranchNumberConversion() {
		String ipAddress = "10.170.121.12";
		String branch = FibiUtils.calcBranchNumberFromIPaddress(ipAddress);
		logger.info("IP address: " + ipAddress + "  branch number: " + branch);
		Assert.assertEquals(branch, "021");
		
		 ipAddress = "10.171.101.123";
		 branch = FibiUtils.calcBranchNumberFromIPaddress(ipAddress);
		 logger.info("IP address: " + ipAddress + "  branch number: " +
		 branch);
		 Assert.assertNull(branch);
		 //
		 ipAddress = "10.179.100.123";
		 branch = FibiUtils.calcBranchNumberFromIPaddress(ipAddress);
		 logger.info("IP address: " + ipAddress + "  branch number: " +
		 branch);
		 Assert.assertEquals(branch, "900");
		 //
		 ipAddress = "10.170.1.123";
		 branch = FibiUtils.calcBranchNumberFromIPaddress(ipAddress);
		 logger.info("IP address: " + ipAddress + "  branch number: " +
		 branch);
		 Assert.assertEquals(branch, "001");
		
//		ipAddress = "10.27.171.123";
//		branch = FibiUtils.calcBranchNumberFromIPaddress(ipAddress);
//		logger.info("IP address: " + ipAddress + "  branch number: " + branch);
//		Assert.assertEquals(branch, "295");
//		//
//		ipAddress = "10.27.171.123";
//		branch = FibiUtils.calcBranchNumberFromIPaddress(ipAddress);
//		logger.info("IP address: " + ipAddress + "  branch number: " + branch);
//		Assert.assertEquals(branch, "295");
	}

//	@Test
//	public void testFibiSimpleEmail() {
//		Properties properties = FibiUtils.getFbiMailProperties();
//		properties.setProperty("subject", "test simple subject");
//		properties.setProperty("message", "Test simple message");
//		String simpleMailMessageId = EmailUtils.sendSimpleEmail(properties);
//		logger.info(simpleMailMessageId);
//		Assert.assertTrue(simpleMailMessageId.indexOf(".JavaMail.") > 0);
//	}

//	@Test
//	public void testFibiHtmlEmail() {
//		Properties properties = FibiUtils.getFbiMailProperties();
//		properties.setProperty("subject", "test html subject");
//		properties.setProperty("htmlMsg", "Test html message");
//		properties.setProperty("textMsg", "Test html text message");
//		String htmlEmailMessageId = EmailUtils.sendHtmlEmail(properties);
//		logger.info(htmlEmailMessageId);
//		Assert.assertTrue(htmlEmailMessageId.indexOf(".JavaMail.") > 0);
//	}

}
