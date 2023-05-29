package oz.jdir;

import java.util.logging.Logger;

import oz.clearcase.infra.ClearCaseClass;

public class ClearCaseIntegration {
	private static final String ENABLECC_TEXT = "Enable CC";
	private static final String DISABLECC_TEXT = "Disable CC";
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public final String enableClearCaseIntegration(final ClearCaseClass ccc) {
		String errorMessage1;
		/*
		 * enable ClearCase integration processing
		 */
		if (!ClearCaseClass.doesClearToolExeExists()) {
			errorMessage1 = "Cleartool.exe not found.\n"
					+ "ClearCase integration cannot be enabled !";
			logger.warning(errorMessage1);
			return errorMessage1;
		} else if (!ccc.getClearCaseHostInfo()) {
			logger.warning(ccc.getClearCaseMessage());
			return ccc.getClearCaseMessage();
		}
		JdirParameters.enableClearCaseIntegration();
		logger.info("ClearCase integration is now enabled !\n");
		if (!JdirParameters.getSd().getFileList().isEmpty()
				&& !JdirParameters.getDd().getFileList().isEmpty()) {
			DirMatchTable.getDirMatchTable().buidMatchTable(true);
		}
		return null;
	}

	/*
	 * Disable ClearCase integration processing
	 */
	public final void disableClearCaseIntegration(final ClearCaseClass ccc) {
		if (JdirParameters.isClearCaseIntegrationEnabled()) {
			logger.fine("Disable ClearCase integration");
			JdirParameters.disableClearCaseIntegration();
			logger.info("ClearCase integration has been disabled !\n");
			if ((JdirParameters.getShowOption() != null)
					&& (!JdirParameters.getSd().getFileList().isEmpty())
					&& (!JdirParameters.getDd().getFileList().isEmpty())) {
				DirMatchTable.getDirMatchTable().buidMatchTable(true);
			}
			return;
		}
	}

	public static final String getENABLECC_TEXT() {
		return ENABLECC_TEXT;
	}

	public static final String getDISABLECC_TEXT() {
		return DISABLECC_TEXT;
	}
}