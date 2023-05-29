package oz.test.junit;

import java.util.List;
import java.util.logging.Logger;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import oz.infra.array.test.JUnitTestArrayUtils;
import oz.infra.fibi.test.JUnitTestFibiUtils;
import oz.infra.list.test.JUnitTestListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.test.cryptography.TestEncryptDecrypt;

public class JUnitRunner {
	private static Logger logger = JulUtils.getLogger();
	private static int totalFailureCount = 0;
	private static int totalRunCount = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		runTest(JUnitTestArrayUtils.class);
		runTest(JUnitTestListUtils.class);
		runTest(TestEncryptDecrypt.class);
		runTest(JUnitTestFibiUtils.class);
		//
		printSummary();
		System.exit(totalFailureCount);
	}

	private static void runTest(final Class clazz) {
		Result result = org.junit.runner.JUnitCore.runClasses(clazz);
		int failureCount = result.getFailureCount();
		totalFailureCount += failureCount;
		int runCount = result.getRunCount();
		totalRunCount += runCount;
		logger.info("failureCount: " + String.valueOf(failureCount) + " runCount: "
				+ String.valueOf(runCount));
		List<Failure> failures = result.getFailures();
		for (Failure failure : failures) {
			logger.warning(failure.getDescription().toString() + " failed !!");
		}
	}

	private static void printSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append("*** Run completed. ");
		sb.append(String.valueOf(totalRunCount));
		sb.append(" test ran. ");
		sb.append(String.valueOf(totalFailureCount));
		sb.append(String.valueOf(" failed. ***"));
		logger.info(sb.toString());

	}
}
