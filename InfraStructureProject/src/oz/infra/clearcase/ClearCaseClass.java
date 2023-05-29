package oz.infra.clearcase;

import java.io.File;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import oz.infra.logging.jul.JulUtils;
import oz.infra.run.RunExec;

public class ClearCaseClass {
	private static String clearToolExePath = ClearCaseUtils.getClearToolPath();
	private static final String DIRECTORY_VERSION = "directory version";
	private static Logger logger = JulUtils.getLogger();
	private static final String msgClearCaseAlreayUnderSC = "File is already under source control";
	private static final String msgClearCaseHostinfoFailed = "ClearCase hostinfo could not be retrieved.\nClearCase is not running on this machine!";
	private static final String msgClearCaseNoFilesToAdd = "No files to add to source control!";
	private static final String CHECKEDOUT = "CHECKEDOUT";
	private static final String RULE = "Rule:";
	private static final String MKBRANCH = "-mkbranch";
	private static final String TEXT_HIJACKED = "[hijacked]";
	private static final String VIEW_PRIVATE_OBJECT = "view private object";
	private static final String NEW_LINE = "\n";

	private String clearCaseMessage;
	private String clearCaseOut;
	private String clearCaseErr;

	private int clearCaseReturnCode;

	public static final boolean doesClearToolExeExists() {
		logger.fine("**** Entering doesClearToolExeExists method ****");
		return new File(clearToolExePath).isFile();
	}

	public final ClearCaseAttributes[] buildClearCaseAttributesTable(
			final String dirName) {
		logger.info("**** Entering buildClearCaseAttributesTable method **** "
				+ dirName);
		String[] params = { clearToolExePath, "ls", "-recurse", "-visible",
				dirName };
		if (!runCCCommand(params, true)) {
			return null;
		}
		logger.finest("clearCaseOut.length=" + clearCaseOut.length());
		String[] outputArray = clearCaseOut.split(NEW_LINE);
		ClearCaseAttributes[] clearCaseAttributes = new ClearCaseAttributes[outputArray.length];
		logger.finer("outputArray.length=" + outputArray.length);
		int i = 0;
		/*
		 * Loop
		 */
		logger.finest("starting loop");
		if (clearCaseOut.length() > 0) {
			while (i < outputArray.length) {
				String lsOutput = outputArray[i];
				clearCaseAttributes[i] = new ClearCaseAttributes();
				int aaIndex = lsOutput.lastIndexOf("@@");
				if (aaIndex == -1) {
					clearCaseAttributes[i].setCCmaganaged(false);
					clearCaseAttributes[i].setFileFullPath(lsOutput.substring(
							lsOutput.indexOf(dirName), lsOutput.length()));
				} else {
					int ruleIndex = lsOutput.lastIndexOf(RULE);
					int mkbranchIndex = lsOutput.lastIndexOf(MKBRANCH);
					int checkedoutIndex = lsOutput.lastIndexOf(CHECKEDOUT);
					clearCaseAttributes[i].setCCmaganaged(true);
					clearCaseAttributes[i].setFileFullPath(lsOutput.substring(
							lsOutput.indexOf(dirName), aaIndex));
					logger.finest("lsOutput=" + lsOutput + " dirName= "
							+ dirName + " lsOutput.length="
							+ String.valueOf(lsOutput.length()) + " i="
							+ String.valueOf(i));
					if (ruleIndex <= aaIndex || ruleIndex <= 1) {
						logger.severe("Rule index not greater than aaIndex !!!");
						System.exit(-1);
					}
					if (mkbranchIndex > ruleIndex) {
						clearCaseAttributes[i].setFromFoundationBaseline(true);
					}
					if (ruleIndex < checkedoutIndex) {
						clearCaseAttributes[i].setCheckedOut(true);
					} else if (lsOutput.indexOf(TEXT_HIJACKED) != -1) {
						clearCaseAttributes[i].setHijacked(true);
					}
				}
				i++;
			}
		}
		logger.info("**** Leaving buildClearCaseAttributesTable method **** "
				+ dirName);
		return clearCaseAttributes;
	}

	/*
	 * checkIn
	 */
	public final boolean checkIn(final String elementName) {
		logger.fine(NEW_LINE);
		logger.fine("**** Entering checkIn method **** " + elementName);
		String[] params1 = { clearToolExePath, "checkin", "-nc", "-identical",
				elementName };
		return (runCCCommand(params1, true));
	}

	/*
	 * Checkout
	 */
	public final boolean checkOut(final String elementName) {
		logger.fine(NEW_LINE + "**** Entering checkout method **** "
				+ elementName);
		String[] params = { clearToolExePath, "ls", "-d", "-l", elementName };
		if (!runCCCommand(params, true)) {
			return false;
		}
		if (clearCaseOut.indexOf(VIEW_PRIVATE_OBJECT) == 0) {
			return false;
		}
		int ruleIndex = clearCaseOut.lastIndexOf(RULE);
		if ((ruleIndex > 0)
				&& (ruleIndex < clearCaseOut.lastIndexOf(CHECKEDOUT))) {
			return true;
		}
		String[] params1 = { clearToolExePath, "checkout", "-nwarn", "-nc",
				elementName };
		boolean returnCode = (runCCCommand(params1, true) && clearCaseOut
				.indexOf("Checked out") != -1);
		if (!returnCode) {
			logger.warning(clearCaseOut);
		}
		return returnCode;
	}

	/*
	 * lsview
	 */
	public final boolean clearToolCommand(final String[] params) {
		logger.fine(NEW_LINE + "**** Entering clearToolCommand method ****");
		params[0] = clearToolExePath;
		return runCCCommand(params, true);
	}

	public final String getClearCaseErr() {
		return clearCaseErr;
	}

	/*
	 * getClearCaseHostInfo
	 */
	public final boolean getClearCaseHostInfo() {
		String[] params = { clearToolExePath, "hostinfo", "-l" };
		return runCCCommand(params, true);
	}

	public final String getClearCaseMessage() {
		return clearCaseMessage;
	}

	public final String getClearCaseOut() {
		return clearCaseOut;
	}

	/*
	 * getClearToolExePath
	 */
	public final String getClearToolExePath() {
		return clearToolExePath;
	}

	/*
	 * isElementManaged
	 */
	public final boolean isElementManaged(final String elementName) {
		logger.fine("**** Entering isElementManaged method ****");
		if (elementName == null) {
			return false;
		}
		String[] params = { clearToolExePath, "ls", "-d", "-l", elementName };
		return (runCCCommand(params, false) && (clearCaseOut
				.indexOf(VIEW_PRIVATE_OBJECT) == -1));
	}

	/*
	 * lscheckout
	 */
	final boolean lscheckout(final String elementName) {
		logger.fine(NEW_LINE);
		logger.fine("**** Entering lscheckout method ****");
		if (!isElementManaged(elementName)) {
			return false;
		}
		String[] params = { clearToolExePath, "lscheckout", "-s", "-cview",
				"-r", elementName };
		return runCCCommand(params, true);
	}

	/*
	 * 
	 */
	public final boolean lsvtree(final String elementName) {
		logger.fine(NEW_LINE);
		logger.fine("**** Entering lsvtree method **** " + elementName);
		String[] params = { clearToolExePath, "lsvtree", "-g", "-a",
				elementName };
		return (runCCCommand(params, true));
	}

	/*
	 * mkelem
	 */
	public final boolean mkelem(final String fileTo1P, final String fileTo2P) {
		if (!isElementManaged(fileTo1P)) {
			return false;
		}
		String tmpFileTo1 = fileTo1P;
		String tmpFileTo2 = fileTo2P;
		logger.fine(NEW_LINE + "**** Entering mkelem method **** " + tmpFileTo1
				+ " + " + tmpFileTo2);
		// force tmpFileTo1 to end with file separator and tmpFileTo2 not
		// tostart with a file separator
		boolean checkIfManaged = true;
		if (tmpFileTo1.lastIndexOf(File.separator) != (tmpFileTo1.length() - 1)) {
			tmpFileTo1 = tmpFileTo1 + File.separator;
		}
		if (tmpFileTo2.length() > 0
				&& tmpFileTo2.substring(0, 1).equals(File.separator)) {
			tmpFileTo2 = tmpFileTo2.substring(1, tmpFileTo2.length());
		}
		logger.fine("**** Inside mkelem method **** " + tmpFileTo1 + " + "
				+ tmpFileTo2);
		int i, l;
		while (tmpFileTo2.indexOf(File.separator) != -1) {
			l = tmpFileTo2.length();
			i = tmpFileTo2.indexOf(File.separator);
			tmpFileTo1 = tmpFileTo1 + tmpFileTo2.substring(0, i);
			tmpFileTo2 = tmpFileTo2.substring(i + 1, l);
			logger.fine("mkelem tmpFileTo1=" + tmpFileTo1
					+ " mkelem tmpFileTo2=" + tmpFileTo2);
			if (checkIfManaged) {
				if (!isElementManaged(tmpFileTo1)) {
					mkelem1(tmpFileTo1);
					checkIfManaged = false;
				}
			} else {
				mkelem1(tmpFileTo1);
			}
			tmpFileTo1 = tmpFileTo1 + File.separator;
		}
		mkelem1(tmpFileTo1 + tmpFileTo2);
		return true;
	}

	/*
	 * mkelem1
	 */
	final boolean mkelem1(final String elementName) {
		logger.fine("mkelem1: " + elementName);
		if (!checkOut(new File(elementName).getParentFile().getAbsolutePath())) {
			return false;
		}
		String[] params = { clearToolExePath, "mkelem", "-ptime", "-nc",
				elementName };
		return (runCCCommand(params, true));
	}

	/*
	 * mv
	 */
	public final boolean mv(final String elementNameIn,
			final String elementNameOut) {
		if (!checkOut(new File(elementNameIn).getParentFile().getAbsolutePath())) {
			return false;
		}
		String[] params = { clearToolExePath, "mv", "-nc", elementNameIn,
				elementNameOut };
		return (runCCCommand(params, true) && (clearCaseOut.indexOf("Moved") == 0));
	}

	/*
	 * recursiveAdd2
	 */
	public final boolean recursiveAdd2(final String fileTo1,
			final String fileTo2) {
		logger.fine(NEW_LINE);
		logger.fine("**** Entering recursiveAdd2 method **** " + fileTo1
				+ " + " + fileTo2);
		String paramFileString = fileTo1 + File.separator + fileTo2;
		logger.fine("paramFileString=" + paramFileString);
		File tmpFile = new File(paramFileString);
		if (!tmpFile.exists()) {
			return false;
		}
		if (tmpFile.isFile() && isElementManaged(paramFileString)) {
			return true;
		}
		if (tmpFile.isFile() && !isElementManaged(paramFileString)) {
			return mkelem(fileTo1, fileTo2);
		}
		if (tmpFile.isDirectory() && !isElementManaged(paramFileString)) {
			mkelem(fileTo1, fileTo2);
		}
		String[] params = { clearToolExePath, "ls", "-r", paramFileString };
		RunExec re = new RunExec();
		if (re.runCommand(params) != 0) {
			return false;
		} else {
			if (re.getOutputString().equals("")) {
				clearCaseMessage = msgClearCaseNoFilesToAdd;
				return false;
			}
			String[] outputArray = (re.getOutputString()).split(NEW_LINE);
			logger.fine("outputArray.length=" + outputArray.length);
			int i = 0;
			while (i < outputArray.length) {
				logger.fine(outputArray[i] + "  outputArray[i].length()="
						+ outputArray[i].length());
				if (outputArray[i].length() > fileTo1.length()
						&& outputArray[i].indexOf(RULE) == -1) {
					logger.fine("recursiveAdd2 "
							+ fileTo1
							+ " +  "
							+ outputArray[i].substring(fileTo1.length(),
									outputArray[i].length()));
					mkelem(fileTo1, outputArray[i].substring(fileTo1.length(),
							outputArray[i].length()));
				}
				i++;
			}
		}
		return true;
	}

	/*
	 * recursiveCheckin
	 */
	public final boolean recursiveCheckin(final String elementName) {
		logger.fine(NEW_LINE);
		logger.fine("**** Entering recursiveCheckin method **** " + elementName);
		if (!lscheckout(elementName) || clearCaseOut.length() == 0) {
			return false;
		}
		String[] outputArray = clearCaseOut.split(NEW_LINE);
		logger.finest("recursiveCheckin: Number of files=" + outputArray.length);
		int i = 0;
		while (i < outputArray.length) {
			if (outputArray[i].length() > 0) {
				checkIn(outputArray[i]);
			}
			i++;
		}
		return true;
	}

	/*
	 * rmname
	 */
	public final boolean rmname(final String elementName) {
		logger.fine(NEW_LINE);
		logger.fine("**** Entering rmname method **** " + elementName);
		if (!checkOut(new File(elementName).getParentFile().getAbsolutePath())) {
			return false;
		}
		String[] params = { clearToolExePath, "rmname", "-nc", elementName };
		return (runCCCommand(params, true));
	}

	/*
	 * runCCCommand
	 */
	private boolean runCCCommand(final String[] params, final boolean verbose) {
		clearCaseMessage = "";
		RunExec re = new RunExec();
		clearCaseReturnCode = re.runCommand(params);
		clearCaseOut = re.getOutputString();
		clearCaseErr = re.getErrString();
		if (clearCaseReturnCode == 0) {
			String outputString = "";
			for (int i = 0; i < params.length; i++) {
				outputString += params[i] + " ";
			}
			outputString += "   Return code: "
					+ String.valueOf(clearCaseReturnCode);
			logger.finer(outputString);
			clearCaseMessage = "ClearCase " + params[1]
					+ " operation completed sucsessfully";
			return true;
		}
		if (verbose) {
			logger.warning(clearCaseErr);
			clearCaseMessage = "ClearCase " + params[1] + " operation failed";
			JOptionPane.showMessageDialog(null, clearCaseErr,
					"ClearCase operation failed", JOptionPane.ERROR_MESSAGE);
			// if (SwingUtilities.isEventDispatchThread()) {
			// JOptionPane.showMessageDialog(null, clearCaseErr,
			// "ClearCase operation failed", JOptionPane.ERROR_MESSAGE);
			// } else {
			// JOptionPaneShowMessageDialog jOptionPaneShowMessageDialog = new
			// JOptionPaneShowMessageDialog (null, clearCaseErr,
			// "ClearCase operation failed", JOptionPane.ERROR_MESSAGE);
			// try {
			// SwingUtilities.invokeAndWait(jOptionPaneShowMessageDialog);
			// }
			// catch (Exception e) {
			// e.printStackTrace();
			// }
			//
			// }
		}
		return false;
	}

	/*
	 * setClearToolExePath
	 */
	public final void setClearToolExePath(final String clearToolExePathP) {
		ClearCaseClass.clearToolExePath = clearToolExePathP;
	}

	public final boolean uncheckOut(final String elementName) {
		logger.fine(NEW_LINE);
		logger.fine("**** Entering uncheckout method **** " + elementName);
		String[] params = { clearToolExePath, "uncheckout", "-keep",
				elementName };
		return (runCCCommand(params, true));
	}
}