package oz.infra.logging.jul.test;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.filters.OzLogFilter;
import oz.infra.logging.jul.formatters.EmptyForrmatter;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.string.StringUtils;

public class TestJulUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// testFormatter();
		// testGetLogger();
		// testAddRemoveHandler();
		// testAddRemoveHandlerByCanonicalNameName();
		// testAddRemoveHandler1();
		// testCloseHandlers();
		// JulUtils.logBuildInfo();
		// testFileHandlerLogger();
		// testLogNull();
		// testMyFilter();
		// logger.info("bla bla bla ...");
		// logger.info("bahh bahh bahh ...");
		// testEmptyFormatter();
		// testSwitchFileHandler();
		// test2Logs();
		testGetSummaryLogFilePathFromEnv();
	}

	private static void testGetSummaryLogFilePathFromEnv() {
		logger.info(JulUtils.getSummaryLogFilePathFromEnv());
	}

	private static void test2Logs() {
		Logger log1 = JulUtils.getLogger("1", false);
		Logger log2 = JulUtils.getLogger("2", true);
		Handler fileHandler1 = JulUtils.getFileHandler("c:/temp/1.log");
		Handler fileHandler2 = JulUtils.getFileHandler("c:/temp/2.log");
		Handler fileHandler3 = JulUtils.getFileHandler("c:/temp/3.log");
		JulUtils.addHandler(fileHandler1, log1);
		JulUtils.addHandler(fileHandler2, log2);
		JulUtils.addHandler(fileHandler3, logger);

		log1.info("1111111111111111111111111111111");
		log2.info("2222222222222222222222222222222");
		logger.info("33333333333333333333333333333");
	}

	private static void testSwitchFileHandler() {
		FileHandler fileHandler = null;
		for (int i = 0; i < OzConstants.INT_10; i++) {
			String logFilePath = StringUtils.concat("c:\\temp\\", String.valueOf(i), OzConstants.ERR_SUFFIX);
			fileHandler = JulUtils.switchFileHandler(fileHandler, logFilePath);
			logger.info(String.valueOf(i));
		}
		logger.info(JulUtils.listRootHandlers().toString());
	}

	private static void testEmptyFormatter() {
		Logger emptyLogger = JulUtils.getLogger(new EmptyForrmatter());
		emptyLogger.info("test empty formatter 1");
		emptyLogger.info("test empty formatter 2");
		emptyLogger.info("test empty formatter 3");
	}

	private static void testFormatter() {
		Logger l1 = JulUtils.getLogger("oz.infra.logging.test.l1");
		l1.info("bla bla ");
		Logger l2 = JulUtils.getLogger("oz.infra.logging.test.l2", new OneLineFormatterWithDatePrefix());
		l2.info("bla bla bal + date prefix");
	}

	private static void testAddRemoveHandler() {
		logger.info(JulUtils.listRootHandlers().toString());
		Handler handler = JulUtils.addFileHandler("c:\\temp\\testAddRemoveHandler.log");
		logger.info(JulUtils.listRootHandlers().toString());
		JulUtils.addHandler(handler);
		logger.info(JulUtils.listRootHandlers().toString());
		logger.info("test 1 ...");
		JulUtils.removeHandler(handler);
		logger.info("test 2 ...");
		logger.info(JulUtils.listRootHandlers().toString());
	}

	private static void testAddRemoveHandlerByCanonicalNameName() {
		logger.info(JulUtils.listRootHandlers().toString());
		Handler handler = JulUtils.addFileHandler("c:\\temp\\testAddRemoveHandler.log");
		logger.info(JulUtils.listRootHandlers().toString());
		JulUtils.addHandler(handler);
		logger.info(JulUtils.listRootHandlers().toString());
		logger.info("test 1 ...");
		JulUtils.removeHandlers(JulUtils.getFileHandlerCanonicalName());
		logger.info("test 2 ...");
		logger.info(JulUtils.listRootHandlers().toString());
	}

	private static void testAddRemoveHandler1() {
		Handler handler = JulUtils.getFileHandler("c:/temp/123456789.log", true);
		JulUtils.addHandler(handler);
		logger.info("test 1 ...");
		JulUtils.removeHandler(handler);
		logger.info("test 2 ...");
		JulUtils.closeHandlers();
	}

	private static void testCloseHandlers() {
		JulUtils.addFileHandler("c:\\temp\\testCloseLogger.log");
		logger.info("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG   ...");
		StringBuilder sb = null;
		sb = JulUtils.closeHandlers(logger);
		logger.info(sb.toString());
		sb = JulUtils.closeHandlers(JulUtils.FILE_HANDLER_CLASS_NAME);
		logger.info(sb.toString());
		sb = JulUtils.closeHandlers();
		logger.info(sb.toString());
		logger.info("end of testCloseHandlers ...");
	}

	private static void testFileHandlerLogger() {
		FileHandler fileHandler = JulUtils.getFileHandler("c:\\temp\\f.log", true);
		JulUtils.addHandler(fileHandler);
		logger.info("xxx");
		logger.info("yyy");
		logger.info("ffffffffffffffhhhhhhhhhhhhhhhhh");
	}

	private static void testGetLogger() {
		logger.info("zvl 123");
		Logger dlogger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());
		dlogger.info("zvl 456");

	}

	private static void testLogNull() {
		logger.info("null");
	}

	private static void testMyFilter() {
		logger.info(String.valueOf(new OzLogFilter().isLoggable(null)));
		logger.info(String.valueOf(new OzLogFilter().isLoggable(new LogRecord(Level.FINE, "java."))));
	}
}
