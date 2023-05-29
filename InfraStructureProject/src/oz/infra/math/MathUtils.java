package oz.infra.math;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.logging.Logger;

import oz.infra.string.StringUtils;

public class MathUtils {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	Random random = new Random();

	public final String getRandomNumberAsString(final int randomNumberStringlength) {
		int randomInt = random.nextInt();
		DecimalFormat formatter = new DecimalFormat(StringUtils.repeatChar('0',
				randomNumberStringlength));
		String randomIntString = formatter.format(randomInt);
		logger.finest("Random : " + randomIntString);
		return randomIntString.substring(randomIntString.length() - randomNumberStringlength);

	}

}
