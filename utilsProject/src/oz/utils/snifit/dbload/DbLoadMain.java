package oz.utils.snifit.dbload;

import java.util.logging.Logger;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;
import oz.utils.snifit.dbload.watchers.DbLoadProcessor;

public class DbLoadMain {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		logger.info(SystemUtils.getRunInfo());
		if (SystemUtils.validateClassPath()) {
			DbLoadParameters.processParameters(args[0]);
			try {
				new DbLoadProcessor();
			} catch (Exception ex) {
				logger.warning(ExceptionUtils.printMessageAndStackTrace(ex));
			}
		} else {
			logger.warning("Invalid classpath. Processing has been teminated.");
		}
	}
}
