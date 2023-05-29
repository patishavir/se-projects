package oz.clearcase.infra;

import java.io.File;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import oz.infra.logging.jul.JulUtils;
import oz.infra.run.RunExec;

public class ClearToolCommand {
	private static String clearToolPath = ClearCaseUtils.getClearToolPath();

	private Logger logger = JulUtils.getLogger();

	/*
	 * runClearToolCommand
	 */
	private boolean debugMode = false;
	private boolean interactWithUser = true;

	public ClearToolCommand(final String pClearToolPath, final boolean pDebugMode) {
		if (pClearToolPath != null && pClearToolPath.length() > 0) {
			File clearToolFile = new File(pClearToolPath);
			if (clearToolFile.exists()) {
				clearToolPath = pClearToolPath;
			}
		}
		this.debugMode = pDebugMode;
	}

	public ClearToolCommand(final boolean pDebugMode) {
		this.debugMode = pDebugMode;
	}

	public ClearToolCommand(final boolean pDebugMode, final boolean interactWithUser) {
		this.debugMode = pDebugMode;
		this.interactWithUser = interactWithUser;
	}

	public ClearToolCommand() {
		super();
	}

	public final ClearToolResults runClearToolCommand(final String[] clearToolparams,
			final boolean acceptFailure) {
		if (clearToolparams[0] == null || clearToolparams[0].length() == 0) {
			clearToolparams[0] = clearToolPath;
		}
		StringBuffer ctParamsStringBuffer = new StringBuffer();
		for (int i = 0; i < clearToolparams.length; i++) {
			ctParamsStringBuffer.append(clearToolparams[i] + " ");
		}
		String ctParamsString = ctParamsStringBuffer.toString();
		logger.fine(ctParamsString);
		if (debugMode) {
			JOptionPane.showMessageDialog(null, ctParamsString, "Cleartool command",
					JOptionPane.INFORMATION_MESSAGE);
			logger.info(ctParamsString);
		}
		RunExec re = new RunExec();
		int clearToolReturnCode = re.runCommand(clearToolparams);
		String cleartoolOutput = re.getOutputString();
		String cleartoolErr = re.getErrString();
		if ((clearToolReturnCode == 0) || acceptFailure) {

			logger.fine("Return code:" + String.valueOf(clearToolReturnCode));
			logger.fine("Out:" + cleartoolOutput);
			if (re.getErrString().length() > 0) {
				logger.fine("Err:" + cleartoolErr);
			}

		} else {
			logger.severe(cleartoolOutput);
			logger.severe(cleartoolErr);
			Object[] options = { "OK", "CANCEL" };
			if (interactWithUser) {
				int selectedOptionIndex = JOptionPane.showOptionDialog(null, ctParamsString
						+ "\nCleartool command failed.\nProceed anyway ?",
						"Cleartool command failed", JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				if (selectedOptionIndex != 0) {
					terminate();
				}
			}
		}
		return new ClearToolResults(clearToolReturnCode, cleartoolOutput, cleartoolErr);
	}

	/*
	 * terminate
	 */
	private void terminate() {
		logger.severe("Cleartool command failed. Processing terminated !");
		// JOptionPane.showMessageDialog(null, ccc.getClearCaseErr(), "Cleartool
		// command failed", JOptionPane.ERROR_MESSAGE);
		System.exit(-1);
	}
}
