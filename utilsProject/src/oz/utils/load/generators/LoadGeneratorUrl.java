package oz.utils.load.generators;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.http.HTTPUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.reflection.ReflectionUtils;

public class LoadGeneratorUrl extends AbstractLoadGenerator {
	private String url = null;
	private static Logger logger = JulUtils.getLogger();

	public LoadGeneratorUrl(final Properties properties) {
		ReflectionUtils.setFieldsFromProperties(properties, this);
	}

	public void generateLoad() {
		String pageContents = HTTPUtils.getPageContents(url);
		logger.info(pageContents);
	}

	public void setUrl(String url) {
		this.url = url;
		logger.info(url);
	}
}
