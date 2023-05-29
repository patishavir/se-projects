package oz.sqlrunner;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.system.SystemUtils;
import oz.sqlrunner.gui.SQLRunnerJFrame;

public class SQLRunnerMain {

	/**
	 * @param args
	 */
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		if (args.length > 2) {
			String logFilePath = args[2];
			JulUtils.addFileHandler(logFilePath);
		}
		logger.info(SystemUtils.getRunInfo());
		String databasePropertiesFilePath = args[0];
		String sqlRunnerProperiesFilePath = args[1];
		ParametersUtils.processPatameters(sqlRunnerProperiesFilePath, SQLRunnerStaticParameters.class);
		SQLRunnerStaticParameters.setDatabasePropertiesFilePath(databasePropertiesFilePath);
		new SQLRunnerJFrame();
	}
}
