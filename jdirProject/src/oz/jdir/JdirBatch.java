/*
 * Created on 22/08/2005 @author Oded
 */
package oz.jdir;

import java.util.logging.Logger;

import oz.clearcase.infra.ClearCaseClass;

final class JdirBatch {

	private static DirMatchTable dirMatchTable;

	private final Logger logger = Logger.getLogger("JdirBatch");

	void process() {
		if (JdirParameters.isEnableCC()) {
			ClearCaseClass ccc = new ClearCaseClass();
			ClearCaseIntegration clearCaseIntegration = new ClearCaseIntegration();
			String errormessage = clearCaseIntegration.enableClearCaseIntegration(ccc);
			if (errormessage != null) {
				logger.severe("Failed to enable ClearCase integration in command line mode!\nProcessing terminated.");
				System.exit(-1);
			}
		}
		dirMatchTable = DirMatchTable.getDirMatchTable();
		dirMatchTable.buidMatchTable(true);

		/*
		 * performOperation method
		 */
		try {
			MultipleFileOperation
					.getMultipleFileOperation()
					.processMultipleFileOperation(JdirParameters.getSd(), JdirParameters.getDd(),
							null, JdirParameters.getOperation()).join();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info(ex.getMessage());
		}
		logger.info("Processing " + JdirParameters.getOperation()
				+ " completed! Program will exit.");
	}
}
