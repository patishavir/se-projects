/*
 * Created on 12/12/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package oz.infra.ozlogging;

import java.util.logging.Level;

import oz.infra.datetime.DateTimeUtils;

/**
 * @author Oded
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
class Logger {
	public static final int ALL = Integer.MIN_VALUE;
	public static final int FINEST = 300;
	public static final int FINER = 400;
	public static final int FINE = 500;
	public static final int CONFIG = 700;
	public static final int INFO = 800;
	public static final int WARNING = 900;
	public static final int SEVERE = 1000;
	private static int level = ALL;
	private static Logger logger = new Logger();

	public static Logger getLogger(String loggerId) {
		return logger;
	}

	public void severe(String msg) {
		if (SEVERE >= level)
			printMsg("Severe", msg);
	}

	public void warning(String msg) {
		if (WARNING >= level)
			printMsg("WARNING", msg);
	}

	public void info(String msg) {
		if (INFO >= level)
			printMsg("INFO", msg);
	}

	public void config(String msg) {
		if (CONFIG >= level)
			printMsg("CONFIG", msg);
	}

	public void fine(String msg) {
		if (FINE >= level)
			printMsg("FINE", msg);
	}

	public void finer(String msg) {
		if (FINER >= level)
			printMsg("FINER", msg);
	}

	public void finest(String msg) {
		if (FINEST >= level)
			printMsg("FINEST", msg);
	}

	private void printMsg(String msgLevel, String msg) {
		System.out.println(DateTimeUtils.getCurrentTime() + " " + msgLevel + " " + msg);
	}

	/**
	 * @param i
	 */
	public static void setLevel(Level level) {
		Logger.level = level.intValue();
	}
}