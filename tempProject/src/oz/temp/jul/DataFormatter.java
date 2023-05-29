package oz.temp.jul;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DataFormatter extends Formatter {
	@Override
	public synchronized String format(LogRecord record) {
		String formattedMessage = formatMessage(record);
		String throwable = "";
		String outputFormat = "%1$s, %2$s \n %3$s"; // Also adding for logging exceptions
		if (record.getThrown() != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println();
			record.getThrown().printStackTrace(pw);
			pw.close();
			throwable = sw.toString();
		}
		// return String.format(outputFormat, record.getLevel().getName(), formattedMessage, throwable);
		// return formattedMessage(record);
		return null;
	}
}