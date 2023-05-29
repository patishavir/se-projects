package oz.infra.junit;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class JunitUtils {
	public static final String SUCCESS = "success: ";
	public static final String FAILURE = "failed !!!: ";
	private static Logger logger = JulUtils.getLogger();

	public static void startTest() {
		logger.info("Starting  test <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}

	public static void endTest() {
		logger.info("End of test >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

}
