package oz.test.logback;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

public class LogBackConfig {

	public static void main(String[] args) {
		Logger logger = (Logger) LoggerFactory.getLogger("abc.xyz");

		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		// FileAppender<LoggingEvent> fileAppender =
		// (FileAppender<LoggingEvent>) logger.getAppender("file");
		// if (fileAppender != null) {
		// fileAppender.stop();
		// fileAppender.setFile("new.log");
		// PatternLayout pl = new PatternLayout();
		// pl.setPattern("%d %5p %t [%c:%L] %m%n)");
		// pl.setContext(lc);
		// pl.start();
		// fileAppender.setLayout(pl);
		// fileAppender.setContext(lc);
		// fileAppender.start();
		// }
	}
}