package oz.infra.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.exception.sql.SqlExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public final class ExceptionUtils {
	private static final Logger logger = JulUtils.getLogger();

	public static String getStackTrace(final Throwable throwable) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		return writer.toString();
	}

	public static String printMessageAndStackTrace(final Exception ex) {
		String messagePrefix = StringUtils.concat(SystemUtils.getCallerClassName(), OzConstants.BLANK,
				SystemUtils.getCallerMethodName(), OzConstants.BLANK);
		StringBuilder sb = new StringBuilder(messagePrefix);
		sb.append(OzConstants.LINE_FEED);
		String exMessage = ex.getMessage();
		sb.append(exMessage);
		sb.append(OzConstants.LINE_FEED);
		if (ex instanceof SQLException) {
			sb.append(StringUtils.repeatString(OzConstants.GREATER_THAN, OzConstants.INT_10));
			sb.append(OzConstants.BLANK);
			sb.append(SqlExceptionUtils.explainMessage((SQLException) ex));
			sb.append(OzConstants.BLANK);
			sb.append(StringUtils.repeatString(OzConstants.LESS_THAN, OzConstants.INT_10));
			sb.append(OzConstants.LINE_FEED);
		}
		sb.append(OzConstants.LINE_FEED);
		sb.append(getStackTrace(ex));
		String message = sb.toString();
		logger.warning(OzConstants.LINE_FEED + message);
		return message;
	}

	private ExceptionUtils() {
		super();
	}
}
