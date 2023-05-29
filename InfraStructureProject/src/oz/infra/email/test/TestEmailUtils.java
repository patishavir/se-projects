package oz.infra.email.test;

import java.util.Properties;

import oz.infra.email.EmailUtils;
import oz.infra.properties.PropertiesUtils;

public class TestEmailUtils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testSendHtmlEmail();
		testSendHtmlEmail2(
				"./args/EmailUtils/MatafEAR_T_UK1.properties");

	}

	private static void testSendHtmlEmail() {
		Properties properties = new Properties();
		properties.setProperty("hostname", "mail.fibi.co.il");
		properties.setProperty("debug", "no");
		// properties.setProperty("from", "Zimerman.O@fibi.co.il,From Oded
		// Zimerman");
		// properties.setProperty("to", "Zimerman.O@fibi.co.il,To Oded
		// Zimerman");
		properties.setProperty("from", "Zimerman.O@fibi.co.il");
		properties.setProperty("to", "Zimerman.O@fibi.co.il");
		properties.setProperty("subject", "my subject");
		properties.setProperty("htmlMsg", "my html message");
		EmailUtils.sendHtmlEmail(properties);
	}

	private static void testSendHtmlEmail2(final String propertiesFilePath) {
		Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		EmailUtils.sendHtmlEmail(properties);
	}
}
