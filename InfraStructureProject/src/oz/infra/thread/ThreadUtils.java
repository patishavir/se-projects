package oz.infra.thread;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.collection.CollectionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;
import oz.infra.varargs.VarArgsUtils;

public final class ThreadUtils {
	private static Logger logger = JulUtils.getLogger();

	public static int getJvmThreadCount() {
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		CollectionUtils.printCollection(threadSet, Level.INFO);
		logger.info("Number of thread in current JVM is: " + String.valueOf(threadSet.size()));
		return threadSet.size();
	}

	public static String getStackTrace() {
		StringBuilder sb = new StringBuilder();
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		for (int i = 0; i < stackTraceElements.length; i++) {
			sb.append(stackTraceElements[i].toString());
			sb.append(SystemUtils.LINE_SEPARATOR);
		}
		return sb.toString();
	}

	public static void printStackTrace() {
		Thread.dumpStack();
	}

	public static void sleep(final long sleepIntervalMillis, final Level... levels) {
		Level level = VarArgsUtils.getMyArg(Level.INFO, levels);
		try {
			if (logger.isLoggable(level)) {
				StringBuilder sb = new StringBuilder(SystemUtils.getCallerClassAndMethodName());
				sb.append(" Start sleeping for " + String.valueOf(sleepIntervalMillis) + " milli seconds");
				logger.log(level, sb.toString());
			}
			Thread.sleep(sleepIntervalMillis);
		} catch (InterruptedException iex) {
			iex.printStackTrace();
			logger.info(iex.getMessage());
		}
	}

	private ThreadUtils() {
		super();
	}
}
