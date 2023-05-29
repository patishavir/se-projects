package oz.infra.number.test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.logging.jul.JulUtils;
import oz.infra.number.NumberUtils;
import oz.infra.varargs.VarArgsUtils;

public class TestNumberUtls {
	private static Logger logger = JulUtils.getLogger();

	private static void getNumberOfPrimes(final int number, final String debugStr) {
		String debugString = VarArgsUtils.getMyArg(OzConstants.NO, debugStr);
		boolean debug = debugString.equalsIgnoreCase(OzConstants.YES);
		StopWatch stopWatch = new StopWatch();
		int primeCount = 0;
		for (int i = 2; i <= number; i++) {
			if (NumberUtils.isPrime(i, debug)) {
				primeCount++;
			}
		}
		logger.info("number: " + String.valueOf(number) + " number of Primes : " + String.valueOf(primeCount)
				+ " elapsed time: " + stopWatch.getElapsedTimeString());
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// The 0 symbol shows a digit or 0 if no digit present
		// testIsPrime(27, true);
		// testIsPrime(29, true);
		// testIsPrime(31, true);
		// testIsPrime(33, true);
		// getNumberOfPrimes(Integer.parseInt(args[0]), OzConstants.YES);
		testFormatNumberString();
		
	}
	private static void testFormatNumberString () {
		logger.info(NumberUtils.formatNumberString("12"));
		logger.info(NumberUtils.formatNumberString("123456789"));
		logger.info(NumberUtils.formatNumberString("123456789012"));
		logger.info(NumberUtils.formatNumberString("1234567890120"));
		logger.info(NumberUtils.formatNumberString("12345x6789"));
		logger.info(NumberUtils.formatNumberString("1234567.89"));
	}

	private static void testFormatting() {
		logger.info(NumberUtils.FORMATTER_COMMAS.format(123456789));
		logger.info(NumberUtils.FORMATTER_COMMAS.format(1234));
		logger.info(NumberUtils.FORMATTER_COMMAS.format(12));
		System.exit(0);
		NumberFormat formatter = new DecimalFormat("000000");
		String s = formatter.format(-1234.567); // -001235
		// notice that the number was rounded up
		logger.info(s);
		// The # symbol shows a digit or nothing if no digit present
		formatter = new DecimalFormat("##");
		s = formatter.format(-1234.567); // -1235
		s = formatter.format(0); // 0
		formatter = new DecimalFormat("##00");
		s = formatter.format(0); // 00

		// The . symbol indicates the decimal point
		formatter = new DecimalFormat(".00");
		s = formatter.format(-.567); // -.57
		formatter = new DecimalFormat("0.00");
		s = formatter.format(-.567); // -0.57
		formatter = new DecimalFormat("#.#");
		s = formatter.format(-1234.567); // -1234.6
		formatter = new DecimalFormat("#.######");
		s = formatter.format(-1234.567); // -1234.567
		formatter = new DecimalFormat(".######");
		s = formatter.format(-1234.567); // -1234.567
		formatter = new DecimalFormat("#.000000");
		s = formatter.format(-1234.567); // -1234.567000

		// The , symbol is used to group numbers
		formatter = new DecimalFormat("#,###,###");
		s = formatter.format(-1234.567); // -1,235
		s = formatter.format(-1234567.890); // -1,234,568

		// The ; symbol is used to specify an alternate pattern for negative
		// values
		formatter = new DecimalFormat("#;(#)");
		s = formatter.format(-1234.567); // (1235)

		// The ' symbol is used to quote literal symbols
		formatter = new DecimalFormat("'#'#");
		s = formatter.format(-1234.567); // -#1235
		formatter = new DecimalFormat("'abc'#");
		s = formatter.format(-1234.567); // -abc1235
	}

	private static void testIsPrime(final int number, final boolean debug) {
		NumberUtils.isPrime(number, debug);
	}

}
