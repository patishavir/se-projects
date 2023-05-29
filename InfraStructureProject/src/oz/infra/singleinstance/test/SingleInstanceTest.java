package oz.infra.singleinstance.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.singleinstance.SingleInstanceUtils;
import oz.infra.thread.ThreadUtils;

public class SingleInstanceTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		runInstance(1000L, 56789);
		ThreadUtils.sleep(1000l, Level.INFO);
		 runInstance(1000L, 56789);
		logger.info("test is done");

	}

	private static void runInstance(final long sleepTime, final int port) {
		SingleInstanceUtils.confirmSingleInstanceRun(port);
		ThreadUtils.sleep(sleepTime, Level.INFO);
	}
}