package oz.infra.runtime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.number.NumberUtils;
import oz.infra.system.SystemUtils;

public class RunTimeUtils {
	private static Logger logger = JulUtils.getLogger();

	public static String displayMemoryInfo(final Level level, final String... message) {
		StringBuilder sb = new StringBuilder();
		if (message != null && message.length > 0) {
			sb.append(message[0]);
			sb.append(OzConstants.TAB);
		}
		sb.append("\nMax memory: ");
		sb.append(NumberUtils.format(Runtime.getRuntime().maxMemory(), NumberUtils.FORMATTER_COMMAS));
		sb.append("\ntotal memory: ");
		sb.append(NumberUtils.format(Runtime.getRuntime().totalMemory(), NumberUtils.FORMATTER_COMMAS));
		sb.append("\nfree memory: ");
		sb.append(NumberUtils.format(Runtime.getRuntime().freeMemory(), NumberUtils.FORMATTER_COMMAS));
		logger.log(level, sb.toString());
		return sb.toString();
	}

	public static void gc(final Level level) {
		Runtime.getRuntime().gc();
		logger.log(level, "Garbage collection has been initialed ...");
	}

	public static void exec(final String command) {
		try {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}

	}

	/**
	 * Handle communication with a process, reading its output/error and feeding
	 * its input
	 * 
	 * @param process
	 *            The process to execute
	 * @param _in
	 *            Reader that will feed the input pipe of the process
	 * @param out
	 *            Writer that will receive the output of the process
	 * @param err
	 *            Writer that will receive the error pipe of the process
	 */
	public static void communicate(Process process, final Reader _in, final Writer out, final Writer err) {
		final String newline = SystemUtils.LINE_SEPARATOR;
		// Buffer the input reader
		final BufferedReader in = new BufferedReader(_in);

		// Final versions of the the params, to be used within the threads
		final BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
		final BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		final BufferedWriter stdIn = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

		// Thread that reads std out and feeds the writer given in input
		new Thread() {
			@Override
			public void run() {
				String line;
				try {
					while ((line = stdOut.readLine()) != null) {
						out.write(line + newline);
					}
				} catch (Exception e) {
					throw new Error(e);
				}
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					/* Who cares ? */ }
			}
		}.start(); // Starts now

		// Thread that reads std err and feeds the writer given in input
		new Thread() {
			@Override
			public void run() {
				String line;
				try {
					while ((line = stdErr.readLine()) != null) {
						err.write(line + newline);
					}
				} catch (Exception e) {
					throw new Error(e);
				}
				try {
					err.flush();
					err.close();
				} catch (IOException e) {
					/* Who cares ? */ }
			}
		}.start(); // Starts now

		// Thread that reads the std in given in input and that feeds the input
		// of the process
		new Thread() {
			@Override
			public void run() {
				String line;
				try {
					while ((line = in.readLine()) != null) {
						stdIn.write(line + newline);
					}
				} catch (Exception e) {
					throw new Error(e);
				}

				try {
					stdIn.flush();
					stdIn.close();
				} catch (IOException e) {
					/* Who cares ? */ }
			}
		}.start(); // Starts now

		// Wait until the end of the process
		try {
			process.waitFor();
		} catch (Exception e) {
			throw new Error(e);
		}

	} // End of #communicate
}
