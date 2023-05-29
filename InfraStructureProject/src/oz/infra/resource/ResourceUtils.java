package oz.infra.resource;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;

public class ResourceUtils {
	private static Logger logger = JulUtils.getLogger();

	public static Properties loadResource(final String propertiesFilePath) {
		Properties properties = null;
		try {
			InputStream inputStream = null;
			inputStream = ResourceUtils.class.getClassLoader().getResourceAsStream(
					propertiesFilePath);
			properties = new Properties();
			properties.load(inputStream);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		PropertiesUtils.printProperties(properties, Level.FINEST);
		return properties;
	}

}
