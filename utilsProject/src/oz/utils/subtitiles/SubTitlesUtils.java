package oz.utils.subtitiles;

import java.util.logging.Logger;

import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class SubTitlesUtils {
	private static Logger logger = JulUtils.getLogger();

	private static void adjustSubtitlesFile(final String subtitlesFilePath, final String secondsString) {
		long adjustmentInMilliSeconds = Long.parseLong(secondsString);
		logger.info("seconds: " + String.valueOf(adjustmentInMilliSeconds));
		String[] inputArray = FileUtils.readTextFile2Array(subtitlesFilePath);
		String[] outputArray = new String[inputArray.length];
		StringBuilder sb = new StringBuilder();
		String lineSeparator = SystemUtils.LINE_SEPARATOR;
		for (int i = 0; i < inputArray.length; i++) {
			String line = inputArray[i];
			logger.finest(line);
			if (line.indexOf("-->") > 0) {
				String fromTime = line.substring(0, 12);
				String toTime = line.substring(17, 29);
				String newFrom = adjustTimeStamp(fromTime, adjustmentInMilliSeconds);
				String newTo = adjustTimeStamp(toTime, adjustmentInMilliSeconds);
				logger.finest(fromTime + "   " + toTime);
				String line1 = StringUtils.overlay(newFrom, line, 0);
				String line2 = StringUtils.overlay(newTo, line1, 17);
				logger.finest(line2);
				sb.append(line2);
			} else {
				sb.append(line);
			}
			sb.append(lineSeparator);
		}
		FileUtils.writeFile(subtitlesFilePath + ".txt", sb.toString());
		logger.info("Processing of " + String.valueOf(inputArray.length) + " records has completed.");
	}

	private static String adjustTimeStamp(final String timeStamp, final long adjustmentInMilliSeconds) {
		long milliSeconds = DateTimeUtils.getMillisFromTimeString(timeStamp, DateTimeUtils.DATE_FORMAT_HHmmssCommaSSS);
		milliSeconds += adjustmentInMilliSeconds;
		String adjustedTimeStamp = DateTimeUtils.formatTime(milliSeconds, DateTimeUtils.DATE_FORMAT_HHmmssCommaSSS);
		logger.finest("timeStamp " + timeStamp + " adjustedTimeStamp " + adjustedTimeStamp);
		return adjustedTimeStamp;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// adjustTime(args[0], args[1]);
		//adjustSubtitlesFile(args[0], args[1]);
		adjustSubtitlesFile(args[0],args[1]);
	}
}
