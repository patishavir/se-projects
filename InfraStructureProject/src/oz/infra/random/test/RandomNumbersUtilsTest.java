package oz.infra.random.test;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.random.RandomNumbersUtils;

public class RandomNumbersUtilsTest {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntWithinRange(1, 50)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntWithinRange(1, 50)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntWithinRange(1, 50)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntWithinRange(1, 50)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntWithinRange(1, 50)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntWithinRange(1, 50)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntWithinRange(1, 50)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntWithinRange(1, 50)) + OzConstants.LINE_FEED);

		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntLessThan(50)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntLessThan(50)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntLessThan(50)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntLessThan(50)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntLessThan(50)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntLessThan(50)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomIntLessThan(50)) + OzConstants.LINE_FEED);
		
		logger.info(String.valueOf(RandomNumbersUtils.getRandomDoubleBetweenRange(1.00D, 50.00D)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomDoubleBetweenRange(1.00D, 50.00D)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomDoubleBetweenRange(1.00D, 50.00D)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomDoubleBetweenRange(1.00D, 50.00D)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomDoubleBetweenRange(1.00D, 50.00D)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomDoubleBetweenRange(1.00D, 50.00D)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomDoubleBetweenRange(1.00D, 50.00D)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomDoubleBetweenRange(1.00D, 50.00D)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomDoubleBetweenRange(1.00D, 50.00D)));
		logger.info(String.valueOf(RandomNumbersUtils.getRandomDoubleBetweenRange(1.00D, 50.00D)));
	}

}
