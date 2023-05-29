package oz.infra.system;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;

public class ExitThread extends Thread {
	private long milliSeconds = 0;
	private static Logger logger = JulUtils.getLogger();

	public ExitThread(final long millis) {
		this.milliSeconds = millis;
	}

	public void run() {
		try {
			logger.info("Will exit in " + String.valueOf(milliSeconds / OzConstants.INT_1000) + " seconds ...");
			Thread.sleep(milliSeconds);
			logger.info("exiting ...");
			System.exit(0);
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
