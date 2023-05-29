package oz.infra.logging.jul.formatters;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.system.SystemUtils;

public class TimeStampFormatter extends Formatter {
	/**
	 * This method formats the given log record, in a java properties file style
	 */
	public synchronized String format(final LogRecord record) {
		String message = record.getMessage();
		StringBuilder logRecordBuffer = new StringBuilder(OzConstants.INT_50);
		logRecordBuffer.append(DateTimeUtils.getCurrentTime() + OzConstants.BLANK);
		logRecordBuffer.append(message + SystemUtils.LINE_SEPARATOR);
		return logRecordBuffer.toString();
	}
}
