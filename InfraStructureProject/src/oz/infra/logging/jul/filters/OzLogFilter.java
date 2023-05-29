/*
 * Created on 24/08/2005
 *
 */
package oz.infra.logging.jul.filters;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

/**
 * @author Oded
 */
public class OzLogFilter implements Filter {
	public final boolean isLoggable(final LogRecord record) {
		boolean loggable = record != null && record.getSourceClassName() != null
				&& record.getSourceClassName().startsWith("oz.");
		return loggable;
	}
}