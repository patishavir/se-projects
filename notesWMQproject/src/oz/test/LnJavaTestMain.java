package oz.test;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class LnJavaTestMain {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str2 = dupString(args[0]);
		logger.info("str2: " + str2);
	}

	public static String dupString(final String str1) {
		return str1 + " X " + str1;
	}
}
