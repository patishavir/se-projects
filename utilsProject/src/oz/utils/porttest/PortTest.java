package oz.utils.porttest;

import java.util.Properties;

import oz.infra.http.HTTPUtils;
import oz.infra.properties.PropertiesUtils;

public class PortTest {
	public static final void main(final String[] args) {
		String connectionPropertiesFilePath = args[0];
		int port = Integer.parseInt(args[1]);
		Runnable serverSocketRunnable = new ServerSocketRunnable(port);
		Thread thread = new Thread(serverSocketRunnable);
		thread.start();
		Properties connectionProperties = PropertiesUtils
				.loadPropertiesFile(connectionPropertiesFilePath);
		HTTPUtils.getPageContents(connectionProperties);

	}
}
