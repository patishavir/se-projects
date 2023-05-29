package oz.infra.logging.jul.formatters;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class EmptyForrmatter extends Formatter {
	private static final String LF = SystemUtils.LINE_SEPARATOR;

	public final synchronized String format(final LogRecord record) {
		return StringUtils.concat(record.getMessage(),LF);
	}
}

