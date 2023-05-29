package oz.test.properties;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;

public class LoadProperties {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		l();

	}

	private static void l() {
		Properties p = new Properties();
		InputStream is = LoadProperties.class.getClassLoader()
				.getResourceAsStream(
						"oz/infra/datetime/hebrewMonthes.properties");
		if (is == null) {
			logger.info("null");
		} else {
			logger.info("not null");
		}
		try {
			p.load(is);
			PropertiesUtils.printProperties(p);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info("all done!");

	}

}
