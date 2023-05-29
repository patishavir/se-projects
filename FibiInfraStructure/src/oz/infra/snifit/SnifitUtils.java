package oz.infra.snifit;

import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.http.HTTPUtils;
import oz.infra.http.win.HttpWinClientUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;
import oz.infra.thread.ThreadUtils;

public class SnifitUtils {
	private static final String MATAF_VERSION = "matafVersion=";
	private static Logger logger = JulUtils.getLogger();

	private static String getVersion(final String pageContens) {
		String version = null;
		// "http://snif-http-t:81/MatafServer_T_UE/generalClientDefines.properties"
		if (pageContens != null && pageContens.length() > 0) {
			String[] pageContensArray = pageContens.split(OzConstants.LINE_FEED);
			for (String line : pageContensArray) {
				if (line.startsWith(MATAF_VERSION)) {
					version = line.substring(MATAF_VERSION.length()).trim();
				}
			}
		}
		return version;
	}

	private static String getVersion(final String url, final int retryCount, final int retryInterval) {
		String version = null;
		for (int i = 0; i < retryCount; i++) {
			String pageContens = HTTPUtils.getPageContents(url);
			version = SnifitUtils.getVersion(pageContens);
			if (version != null) {
				break;
			}
			ThreadUtils.sleep(retryInterval, Level.INFO);
		}
		return version;
	}

	public static String getVersionProtected(final String propertiesFilePath, final String url, final int retryCount,
			final int retryInterval) {
		String version = null;
		if (SystemUtils.isWindowsFlavorOS()) {
			for (int i = 0; i < retryCount; i++) {
				String pageContents = HttpWinClientUtils.getProtectedPageContents(url);
				// String pageContens =
				// SpnegoUtils.getProtectedPageContents(propertiesFilePath,
				// url);
				version = SnifitUtils.getVersion(pageContents);
				if (version != null) {
					break;
				}
				ThreadUtils.sleep(retryInterval, Level.INFO);
			}
		}
		return version;
	}
}
