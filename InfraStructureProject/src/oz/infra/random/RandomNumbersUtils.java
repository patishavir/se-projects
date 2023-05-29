package oz.infra.random;

import java.util.Random;

public class RandomNumbersUtils {
	public static double getRandomDoubleBetweenRange(double lowValue, double highValue) {
		double x = (Math.random() * ((highValue - lowValue) + 1)) + lowValue;
		return x;
	}

	public static int getRandomIntWithinRange(int lowValue, int highValue) {
		Random r = new Random();
		return r.nextInt((highValue - lowValue) + 1) + lowValue;
	}

	public static int getRandomIntLessThan(final int highValue) {
		Random random = new Random();
		int i = random.nextInt(highValue);
		return i;
	}
}
