package oz.infra.snifit.test;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.snifit.SnifitUtils;

public class TestSnifitUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// logger.info(SnifitUtils.getVersion("http://snif-http-t:81/MatafServer_T_UE/generalClientDefines.properties"));
		runGetVersionProtected(args);
	}

	private static void runGetVersionProtected(final String[] args) {
		String propertiesFilePath = args[0];
		String url = args[1];
		int retryCount = Integer.parseInt(args[2]);
		int retryInterval = Integer.parseInt(args[3]);
		logger.info(SnifitUtils.getVersionProtected(propertiesFilePath, url, retryCount, retryInterval));
	}
}