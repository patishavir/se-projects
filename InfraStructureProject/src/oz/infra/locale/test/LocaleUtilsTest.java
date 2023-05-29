package oz.infra.locale.test;

import java.util.logging.Logger;

import oz.infra.locale.LocaleUtils;
import oz.infra.logging.jul.JulUtils;

public class LocaleUtilsTest {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		testGetDefaultLocale();
		testGetAvailableLocales();
	}

	private static void testGetDefaultLocale() {
		logger.info(LocaleUtils.getDefaultLocale().toString());
	}

	private static void testGetAvailableLocales() {
		LocaleUtils.getAvailableLocales();
	}

}
