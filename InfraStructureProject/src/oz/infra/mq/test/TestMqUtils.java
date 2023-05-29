package oz.infra.mq.test;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.mq.MQUtils;
import oz.infra.properties.PropertiesUtils;

public class TestMqUtils {
	private static Logger logger = JulUtils.getLogger();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testWriteMessage();
	}

	private static void testWriteMessage() {
		String propertiesFilePath = ".\\src\\oz\\infra\\mq\\test\\args\\writemessage.properties";
		Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		PropertiesUtils.printProperties(properties, Level.INFO);
		int returnCode = MQUtils.putMessage(properties);
		logger.info("returnCode: " + String.valueOf(returnCode));
	}

}
