package oz.infra.logging.jul.formatters;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import oz.infra.datetime.DateTimeUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class OneLineFormatter extends Formatter {
	/**
	 * This method formats the given log record, in a java properties file style
	 */
	private static final String LF = SystemUtils.LINE_SEPARATOR;

	public final synchronized String format(final LogRecord record) {
		return StringUtils.concat(DateTimeUtils
				.formatCurrentTime(DateTimeUtils.DATE_FORMAT_HHmmss), " ",
				record.getLevel().getName(), " ", record.getSourceClassName(),
				" ", record.getSourceMethodName(), ":  ", record.getMessage(),
				LF);
	}
}
