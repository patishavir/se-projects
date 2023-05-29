package oz.infra.run.processbuilder.test;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.system.SystemUtils;

public class TestSystemCommandExecutorResponse {
	private static final String LINE_SEPARATOR = SystemUtils.LINE_SEPARATOR;
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testGetSystemCommandExecutorResponse();
	}

	private static void testGetSystemCommandExecutorResponse() {
		int returnCode = 8;
		StringBuilder stdout = new StringBuilder();
		stdout.append("111                       ");
		stdout.append(LINE_SEPARATOR);
		stdout.append("2222                             ");
		stdout.append(LINE_SEPARATOR);

		StringBuilder stderr = new StringBuilder();
		stderr.append("111                       ");
		stderr.append(LINE_SEPARATOR);
		stderr.append("2222                             ");
		stderr.append(LINE_SEPARATOR);

		SystemCommandExecutorResponse systemCommandExecutorResponse = new SystemCommandExecutorResponse(returnCode,
				stdout.toString(), stderr.toString());
		logger.info(systemCommandExecutorResponse.getExecutorResponse() + "*");
		logger.info(systemCommandExecutorResponse.getExecutorResponse() + "*");
		SystemCommandExecutorResponse systemCommandExecutorResponse1 = new SystemCommandExecutorResponse(returnCode,
				null, stderr.toString());
		logger.info(systemCommandExecutorResponse1.getExecutorResponse() + "*");
		logger.info(systemCommandExecutorResponse1.getExecutorResponse() + "*");

		SystemCommandExecutorResponse systemCommandExecutorResponse2 = new SystemCommandExecutorResponse(returnCode,
				null, null);
		logger.info(systemCommandExecutorResponse2.getExecutorResponse() + "*");
		logger.info(systemCommandExecutorResponse2.getExecutorResponse() + "*");

	}
}
