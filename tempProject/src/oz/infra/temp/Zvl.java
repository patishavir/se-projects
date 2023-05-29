package oz.infra.temp;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class Zvl {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String x = "";
		for (int i = 0; i < 10; i++) {
			x = x.concat("x");
			x.hashCode();
			logger.info("String: " + x + " hashcode: " + String.valueOf(x.hashCode()));
		}

	}
}
