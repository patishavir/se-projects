package oz.infra.fibi;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.net.ip.IpUtils;
import oz.infra.net.resolve.ResolveUtils;
import oz.infra.string.StringUtils;

public class FibiUtils {
	private static Properties branchNameProperties = null;
	private static final String SIMPLE_MAIL_FIBI_PROPERTIES_FILE = "oz.infra.fibi.email.propertyfiles.simpleMail.properties";
	private static final String HTML_MAIL_FIBI_PROPERTIES_FILE = "oz.infra.fibi.email.propertyfiles.htmlMail.properties";
	private static Map<String, String> branchNetworkSegentsMap = new HashMap<String, String>();
	private static Map<String, String> ipAddressToBranchMap = new TreeMap<String, String>();
	private static Logger logger = JulUtils.getLogger();

	static {
		// Fibi HQ
		branchNetworkSegentsMap.put("10.35.101", "046-T1");
		branchNetworkSegentsMap.put("10.35.102", "046-T2");
		branchNetworkSegentsMap.put("10.35.127", "298-T3");
		branchNetworkSegentsMap.put("10.35.128", "298-T5");
		branchNetworkSegentsMap.put("10.35.129", "060-T7");
		branchNetworkSegentsMap.put("10.27.102", "067-A3");
		branchNetworkSegentsMap.put("10.35.130", "075-KM");
		branchNetworkSegentsMap.put("10.35.131", "116-RT");
		branchNetworkSegentsMap.put("10.35.132", "129");
		// Otzar
		branchNetworkSegentsMap.put("10.73.110", "321");
		branchNetworkSegentsMap.put("10.73.111", "387");
		branchNetworkSegentsMap.put("10.73.112", "388");
		branchNetworkSegentsMap.put("10.73.113", "389");
		branchNetworkSegentsMap.put("10.73.114", "389");
		// beinleumi call
		branchNetworkSegentsMap.put("10.27.171", "295");
	}

	public static String getBranchName(final String branchNumber) {
		if (branchNameProperties == null) {
			branchNameProperties = oz.infra.resource.ResourceUtils
					.loadResource("oz/infra/fibi/properties/branches.properties");
		}
		return branchNameProperties.getProperty(branchNumber);
	}

	@Deprecated
	public static String lookupBranchNumberUsingIPaddress(final String ipAddress) {
		final String lookupFailed = "LOOKUP_FAILED";
		String hostName = "???";
		String branchNumber = ipAddressToBranchMap.get(ipAddress);
		if (branchNumber == null) {
			hostName = ResolveUtils.getHostName(ipAddress);
			branchNumber = hostName.substring(1, 4);
			if (!hostName.equalsIgnoreCase(ipAddress) && hostName.toLowerCase().startsWith("w")
					&& StringUtils.isJustDigits(branchNumber)) {
				ipAddressToBranchMap.put(ipAddress, branchNumber);
			} else {
				branchNumber = null;
				ipAddressToBranchMap.put(ipAddress, lookupFailed);
			}
		} else if (branchNumber.equals(lookupFailed)) {
			branchNumber = null;
		}
		String calcBranchNumber = calcBranchNumberFromIPaddress(ipAddress);
		logger.info("ipAddress: " + ipAddress + " Hostname: " + hostName + " branchNumber: "
				+ branchNumber + " calcBranchNumber: " + calcBranchNumber);
		if (calcBranchNumber != null && branchNumber != null
				&& !branchNumber.equals(calcBranchNumber)) {
			logger.warning("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
		return branchNumber;
	}

	public static String calcBranchNumberFromIPaddress(final String ipAddress) {
		String branchNumber = null;
		boolean validIpAddress = IpUtils.isIpAddressValid(ipAddress);
		int[] ipAddressIntArray = IpUtils.getIpAddressAsIntArray(ipAddress);
		if (validIpAddress && (ipAddressIntArray[0] == 10)
				&& ((ipAddressIntArray[1] >= 170) && (ipAddressIntArray[1] <= 179))) {
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMinimumIntegerDigits(2);
			String firstDigit = String.valueOf(ipAddressIntArray[1] % 170);
			String lastDigits = String.valueOf(numberFormat.format(ipAddressIntArray[2] % 100));
			branchNumber = firstDigit + lastDigits;
			logger.finest("IP address: " + ipAddress + " calculated branchNumber: " + branchNumber);
		} else {
			String leftMost3Octets = IpUtils.getLeftMost3Octets(ipAddress);
			branchNumber = branchNetworkSegentsMap.get(leftMost3Octets);
		}
		return branchNumber;
	}

	public static Properties getFbiMailProperties() {
		Properties mailProperties = new Properties();
		mailProperties.put("hostname", FibiParams.FIBI_SMTP_SERVER_HOST);
		mailProperties.put("from", "s177571@fibi.corp,Oded Zimerman");
		mailProperties.put("to", "s177571@fibi.corp,Oded Zimerman");
		mailProperties.put("attachmentPath", "");
		mailProperties.put("debug", "no");
		return mailProperties;
	}

	public static String getFibiSmtpServerHost() {
		return FibiParams.FIBI_SMTP_SERVER_HOST;
	}
}
