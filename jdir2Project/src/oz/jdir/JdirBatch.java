/*
 * Created on 22/08/2005 @author Oded
 */
package oz.jdir;

import java.util.logging.Logger;

final class JdirBatch {

	private static DirMatchTable dirMatchTable;

	private final Logger logger = Logger.getLogger("JdirBatch");

	void process() {

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
