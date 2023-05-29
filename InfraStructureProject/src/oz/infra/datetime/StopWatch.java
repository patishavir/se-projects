package oz.infra.datetime;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.number.NumberUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class StopWatch {
	private static Logger logger = JulUtils.getLogger();
	private long startTime;

	public StopWatch() {
		start();
	}

	public final void logElapsedTimeMessage() {
		logElapsedTimeMessage("elapsed time:");
	}

	public final void logElapsedTimeMessage(final String stopwatchMessage) {
		if (stopwatchMessage != null) {
			logger.info(StringUtils.concat(SystemUtils.getCallerClassAndMethodName(), OzConstants.BLANK,
					appendElapsedTimeToMessage(stopwatchMessage)));
		}
	}

	public final long getElapsedTimeLong() {
		return System.currentTimeMillis() - startTime;
	}

	public final String getElapsedTimeString() {
		long elapsedTimeInMiliSeconds = System.currentTimeMillis() - startTime;
		String elapsedTimeString = NumberUtils.FORMATTER_COMMAS.format(elapsedTimeInMiliSeconds)
				.concat(" milliseconds");
		return elapsedTimeString;

	}

	public final String appendElapsedTimeToMessage(final String stopwatchMessage) {
		String fullString = StringUtils.concat(stopwatchMessage, OzConstants.BLANK, getElapsedTimeString());
		return fullString;
	}

	public final void start() {
		startTime = System.currentTimeMillis();
		logger.finest("Stopwatch has started");
	}
}