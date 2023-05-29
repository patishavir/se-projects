package oz.infra.thread;

import java.util.logging.Logger;

public class SynchronizingThread extends Thread {
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public static SynchronizingThread getSynchronizingThread() {
		SynchronizingThread synchronizingThread = new SynchronizingThread();
		synchronizingThread.start();
		return synchronizingThread;
	}

	public void run() {
		final long yearInMilliSeconds = 1000 * 3600 * 24 * 365;
		setName("Synchronizing Thread");
		logger.info("Starting thread ...");
		try {
			sleep(yearInMilliSeconds);
		} catch (InterruptedException iex) {
			logger.info(iex.getMessage());
		}
	}
}
