package oz.loganalyzer;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.messages.OzMessages;
import oz.infra.system.SystemUtils;
import oz.loganalyzer.reports.ReportsGenerator;

public class LogAnalyzerMain {
	/**
	 * @param args
	 */
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		logger.info(SystemUtils.getRunInfo());
		if (!(args.length > 0)) {
			SystemUtils.printMessageAndExit(OzMessages.WRONG_NUMBER_OF_PARAMETERS + OzMessages.PROCCESSING_TERMINATED,
					OzConstants.EXIT_STATUS_ABNORMAL);
		}
		// TODO Auto-generated method stub
		SystemUtils.validateClassPath();
		String logAnalyzerPropertiesFilePath = args[0];
		LogAnalyzerParameters.processParameters(logAnalyzerPropertiesFilePath);
		ReportsGenerator.generateReports();
	}
}
