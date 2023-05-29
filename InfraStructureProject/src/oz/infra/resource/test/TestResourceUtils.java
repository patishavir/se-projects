package oz.infra.resource.test;

import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class TestResourceUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		try {
			logger.info(Locale.getDefault().toString());
			Properties properties = oz.infra.resource.ResourceUtils
					.loadResource("oz/infra/resource/test/test.properties");
			System.out.println(properties.get("key3"));
			System.out.println("ברוך הבא");
			// logger.info(PropertiesUtils.printProperties(properties));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
