package oz.infra.logging.logback;

import java.io.File;

import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import oz.infra.system.SystemUtils;

public class LogBackUtils {
	public static Logger getLogger() {
		return getLogger(SystemUtils.getCallerClassName());
	}

	public static Logger getLogger(final String loggerName) {
		Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
		// logger.setLevel(Level.DEBUG);
		// logger.setAdditive(false);
		return logger;
	}

	public static FileAppender<ILoggingEvent> getFileAppender(final String string, final String file) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		PatternLayoutEncoder ple = new PatternLayoutEncoder();
		ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
		ple.setContext(lc);
		ple.start();
		FileAppender<ILoggingEvent> fileAppender = new FileAppender<ILoggingEvent>();
		fileAppender.setFile(file);
		fileAppender.setEncoder(ple);
		fileAppender.setContext(lc);
		fileAppender.start();
		return fileAppender;
	}

	private static void initializeLogback(final String logFilePath) {
		File logbackFile = new File(logFilePath);
		System.setProperty("logback.configurationFile", logbackFile.getAbsolutePath());
		StaticLoggerBinder loggerBinder = StaticLoggerBinder.getSingleton();
		LoggerContext loggerContext = (LoggerContext) loggerBinder.getLoggerFactory();
		loggerContext.reset();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(loggerContext);
		try {
			configurator.doConfigure(logbackFile);
		} catch (JoranException ex) {
			ex.printStackTrace();
		}
	}

	public static Logger createLogger(final String name) {
		ch.qos.logback.classic.Logger templateLogger = (ch.qos.logback.classic.Logger) getLogger("com.myapp");
		LoggerContext context = templateLogger.getLoggerContext();
		String logDir = context.getProperty("HOME_PATH");

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setPattern(context.getProperty("DEFAULT_PATTERN"));
		encoder.setContext(context);
		DefaultTimeBasedFileNamingAndTriggeringPolicy<ILoggingEvent> timeBasedTriggeringPolicy = new DefaultTimeBasedFileNamingAndTriggeringPolicy<ILoggingEvent>();
		timeBasedTriggeringPolicy.setContext(context);
		TimeBasedRollingPolicy<ILoggingEvent> timeBasedRollingPolicy = new TimeBasedRollingPolicy<ILoggingEvent>();
		timeBasedRollingPolicy.setContext(context);
		timeBasedRollingPolicy.setFileNamePattern(logDir + name + ".log."
				+ context.getProperty("MYAPP_ROLLING_TEMPLATE"));
		timeBasedRollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(timeBasedTriggeringPolicy);
		timeBasedTriggeringPolicy.setTimeBasedRollingPolicy(timeBasedRollingPolicy);
		RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();
		rollingFileAppender.setAppend(true);
		rollingFileAppender.setContext(context);
		rollingFileAppender.setEncoder(encoder);
		rollingFileAppender.setFile(logDir + name + ".log");
		rollingFileAppender.setName(name + "Appender");
		rollingFileAppender.setPrudent(false);
		rollingFileAppender.setRollingPolicy(timeBasedRollingPolicy);
		rollingFileAppender.setTriggeringPolicy(timeBasedTriggeringPolicy);
		timeBasedRollingPolicy.setParent(rollingFileAppender);
		encoder.start();
		timeBasedRollingPolicy.start();
		rollingFileAppender.stop();
		rollingFileAppender.start();

		ch.qos.logback.classic.Logger logbackLogger = (ch.qos.logback.classic.Logger) getLogger(name);
		logbackLogger.setLevel(templateLogger.getLevel());
		logbackLogger.setAdditive(false);
		logbackLogger.addAppender(rollingFileAppender);
		return logbackLogger;
	}
}