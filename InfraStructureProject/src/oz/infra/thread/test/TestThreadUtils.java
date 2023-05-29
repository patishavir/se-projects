package oz.infra.thread.test;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.thread.ThreadUtils;

public class TestThreadUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testGetStackTrace();
	}

	private static void testGetStackTrace() {
		logger.info(ThreadUtils.getStackTrace());
	}
}
