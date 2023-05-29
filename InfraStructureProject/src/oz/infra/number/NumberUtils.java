package oz.infra.number;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Logger;

import oz.infra.datetime.StopWatch;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class NumberUtils {
	public static final NumberFormat FORMATTER_00 = new DecimalFormat("00");
	public static final NumberFormat FORMATTER_0000000000 = new DecimalFormat("0000000000");
	public static final NumberFormat FORMATTER_00_00 = new DecimalFormat("#########0.00");
	public static final NumberFormat FORMATTER_COMMAS = new DecimalFormat("###,###,###,###");
	private static Logger logger = JulUtils.getLogger();

	public static String format(final double number, final NumberFormat formatter) {
		return formatter.format(number);
	}

	public static String format(final float number, final NumberFormat formatter) {
		return formatter.format(number);
	}

	public static String format(final int number, final NumberFormat formatter) {
		return formatter.format(number);
	}

	public static String format(final long number, final NumberFormat formatter) {
		return formatter.format(number);
	}

	public static String formatMinMaxDigits(final int number, final int minimumIntegerDigits,
			final int maximumIntegerDigits) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumIntegerDigits(minimumIntegerDigits);
		numberFormat.setMaximumIntegerDigits(maximumIntegerDigits);
		return numberFormat.format(number);
	}

	public static String formatNumberString(final String inputNumberString) {
		String string2return = inputNumberString;
		if (StringUtils.isJustDigits(inputNumberString)) {
			long longNumber = Long.parseLong(inputNumberString);
			string2return = FORMATTER_COMMAS.format(longNumber);
		}
		return string2return;
	}

	public static boolean isPrime(final long number, final boolean debug) {
		StopWatch stopWatch = new StopWatch();
		boolean outcome = true;
		// check if n is a multiple of 2
		if (number > 2 && number % 2 == 0) {
			outcome = false;
		} else {
			// if not, then just check the odds
			double loopLimit = Math.sqrt(number);
			for (int i = 3; i <= loopLimit; i += 2) {
				if (number % i == 0) {
					outcome = false;
					break;
				}
			}
		}
		if (debug) {
			logger.info("number: " + String.valueOf(number) + " outcome: " + String.valueOf(outcome) + " elapsed time: "
					+ stopWatch.getElapsedTimeString());
		}
		return outcome;
	}
}
