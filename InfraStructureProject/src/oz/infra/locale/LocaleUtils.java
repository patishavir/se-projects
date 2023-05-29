package oz.infra.locale;

import java.util.Locale;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.logging.jul.JulUtils;

public class LocaleUtils {
	private static Logger logger = JulUtils.getLogger();

	public static Locale getDefaultLocale() {
		Locale defaultLocale = Locale.getDefault();
		logger.info("Default Locale: " + defaultLocale.toString());
		return defaultLocale;
	}
	public static Locale[] getAvailableLocales() {
		Locale[] availableLocales = Locale.getAvailableLocales();
		ArrayUtils.printArray(availableLocales);
		return availableLocales;
	}
}
