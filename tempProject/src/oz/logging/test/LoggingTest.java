package oz.logging.test;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class LoggingTest {
	private static final Logger logger = Logger.getLogger("error.report");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testHandler();
	}

	private static void testHandler() {
		Logger rootLogger = Logger.getLogger("");
		Handler[] handlers = rootLogger.getHandlers();
		for (Handler handle1 : handlers) {
			logger.info(handle1.getClass().getCanonicalName());
		}
	}

	private static void testFileLogger() {
		try {
			logger.addHandler(new FileHandler("c:\\temp\\error.log"));
			logger.setUseParentHandlers(false);
			logger.info("errorrrrrrrrrrrrrrrrr... ");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("all done !");
	}
}
