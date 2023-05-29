package oz.sqlrunner.handlers;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public final class ExitHandler {
	private static Logger logger = JulUtils.getLogger();
	
	private ExitHandler() {
		super();
	}

	public static void doExit(final String message) {
		logger.info(message);
		ConnectionHandler.getConnectionHandler().disConnect();

	}
}
