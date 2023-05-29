package oz.infra.logging.logback.test;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import oz.infra.logging.logback.LogBackUtils;

public class LogbackTest {

	public static void main(String[] args) {
		// doTest();
		doConfigure("/oz/infra/logging/logback/configuration/logback.xml");
		// doConfigure("logback.xml");

	}

	private static void doTest() {
		Logger logger = LogBackUtils.getLogger();
		// Logger logger = LoggerFactory
		// .getLogger("chapters.introduction.HelloWorld2");
		logger.debug("Hello debug.");
		logger.info("Hello info.");
		logger.error("Hello error.");
		logger.warn("Hello warn");

		// print internal state
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		StatusPrinter.print(lc);
	}

	private static void doConfigure(final String url) {
		LoggerContext context = (LoggerContext) LoggerFactory
				.getILoggerFactory();

		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(context);
			// Call context.reset() to clear any previous configuration, e.g.
			// default
			// configuration. For multi-step configuration, omit calling
			// context.reset().
			context.reset();
			InputStream is = LogbackTest.class.getResourceAsStream(url);
			System.out.println(is.toString());
			configurator.doConfigure(is);
		} catch (JoranException je) {
			// StatusPrinter will handle this
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(context);
		Logger logger = LogBackUtils.getLogger();
		logger.debug("Hello debug.");
		logger.info("Hello info.");
		logger.error("Hello error.");
		logger.warn("Hello warn");
		logger.info("Entering application.");

		logger.info("Exiting application.");
	}
}
