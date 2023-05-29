package oz.logback;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

public class LogbackTest4 {

	public static void main(String[] args) {
		LoggerContext logCtx = (LoggerContext) LoggerFactory.getILoggerFactory();
		PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
		logEncoder.setContext(logCtx);
		logEncoder.setPattern("%-12date{YYYY-MM-dd HH:mm:ss.SSS} %-5level - %msg%n");
		logEncoder.start();
		ConsoleAppender<ILoggingEvent> logConsoleAppender = new ConsoleAppender<ILoggingEvent>();
		logConsoleAppender.setContext(logCtx);
		logConsoleAppender.setName("console");
		logConsoleAppender.setEncoder(logEncoder);
		logConsoleAppender.start();
		logEncoder = new PatternLayoutEncoder();
		logEncoder.setContext(logCtx);
		logEncoder.setPattern("%-12date{YYYY-MM-dd HH:mm:ss.SSS} %-5level - %msg%n");
		logEncoder.start();
		RollingFileAppender<ILoggingEvent> logFileAppender = new RollingFileAppender<ILoggingEvent>();
		logFileAppender.setContext(logCtx);
		logFileAppender.setName("logFile");
		logFileAppender.setEncoder(logEncoder);
		logFileAppender.setAppend(true);
		logFileAppender.setFile("c:/temp/logfile.log");
		TimeBasedRollingPolicy logFilePolicy = new TimeBasedRollingPolicy<>();
		logFilePolicy.setContext(logCtx);
		logFilePolicy.setParent(logFileAppender);
		logFilePolicy.setFileNamePattern("logs/logfile-%d{yyyy-MM-dd_HH}.log");
		logFilePolicy.setMaxHistory(7);
		logFilePolicy.start();
		logFileAppender.setRollingPolicy(logFilePolicy);
		logFileAppender.start();
		Logger log = logCtx.getLogger("Main");
		log.setAdditive(false);
		log.setLevel(Level.INFO);
		log.addAppender(logConsoleAppender);
		log.addAppender(logFileAppender);
	}
}
