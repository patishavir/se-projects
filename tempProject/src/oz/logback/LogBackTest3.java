package oz.logback;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.util.StatusPrinter;

public class LogBackTest3 {
	public static void main(String[] args) {
		logbackInit();
	}

	public static void logbackInit() {
		Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		LoggerContext loggerContext = rootLogger.getLoggerContext();
		loggerContext.reset();
		RollingFileAppender<ILoggingEvent> rfAppender = new RollingFileAppender<ILoggingEvent>();
		rfAppender.setContext(loggerContext);
		rfAppender.setFile("c:/temp/logOutput.log");
		FixedWindowRollingPolicy fwRollingPolicy = new FixedWindowRollingPolicy();
		fwRollingPolicy.setContext(loggerContext);
		fwRollingPolicy.setFileNamePattern("logOutput-%i.log.zip");
		fwRollingPolicy.setParent(rfAppender);
		fwRollingPolicy.start();
		SizeBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy = new SizeBasedTriggeringPolicy<ILoggingEvent>();
		triggeringPolicy.setMaxFileSize("5MB");
		triggeringPolicy.start();
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(loggerContext);
		encoder.setPattern("%-4relative [%thread] %-5level %logger{35}	- %msg%n");
		encoder.start();
		rfAppender.setEncoder(encoder);
		rfAppender.setRollingPolicy(fwRollingPolicy);
		rfAppender.setTriggeringPolicy(triggeringPolicy);
		rfAppender.start();
		rootLogger.addAppender(rfAppender);
		// generate some output
		StatusPrinter.print(loggerContext);
		rootLogger.debug("hello tt");
	}
}
