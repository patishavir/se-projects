package oz.clearcase.stream.bl2stream;

import java.io.File;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import oz.clearcase.infra.ClearCaseUtils;
import oz.clearcase.infra.ClearToolCommand;

public class BL2StreamProcessor {
	/**
	 * @param args
	 */
	private String[] clearToolparams;
	private String pVOB = "@\\" + BL2StreamParameters.getPVOB();
	private String stream = BL2StreamParameters.getStream();
	private String parentStream = BL2StreamParameters.getParentStream();
	private String newBaseline = BL2StreamParameters.getNewBaseLine();
	private boolean removeViews = BL2StreamParameters.isRemoveViews();
	private String suffix = BL2StreamParameters.getSuffix();
	private String renamedStream = stream + "_" + suffix;
	private ClearToolCommand ctc = new ClearToolCommand();
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/*
	 * validateParameters method
	 */
	void validateParameters() {
		/*
		 * validate pvob
		 */
		clearToolparams = new String[] { "", "lsvob", pVOB.substring(1) };
		ctc.runClearToolCommand(clearToolparams, false);
		/*
		 * validate newBaseLine
		 */
		clearToolparams = new String[] { "", "lsbl", "baseline:" + newBaseline + pVOB };
		ctc.runClearToolCommand(clearToolparams, false);
		/*
		 * validate parentStream
		 */
		clearToolparams = new String[] { "", "lsstream", "stream:" + parentStream + pVOB };
		ctc.runClearToolCommand(clearToolparams, false);
		/*
		 * validate stream
		 */
		clearToolparams = new String[] { "", "lsstream", "stream:" + stream + pVOB };
		ctc.runClearToolCommand(clearToolparams, false);
		/*
		 * make sure that newBaseline is not already the stream's foundation BL
		 */
		if (!BL2StreamParameters.isSkipCurrentFoundationBaselineCheck()) {
			clearToolparams = new String[] { "", "lsstream", "-fmt", "%[found_bls]p", stream + pVOB };
			String currentFoundationBaseline = ctc.runClearToolCommand(clearToolparams, false)
					.getStdOut();
			if (newBaseline.equals(currentFoundationBaseline)) {
				terminate("Baseline " + currentFoundationBaseline
						+ " is already the foundation of " + stream + pVOB);
			}
		}
	}

	/*
	 * process
	 */
	final void process() {
		if (BL2StreamParameters.isRebaseParentFirst()) {
			/*
			 * make sure parent has views
			 */

			String[] parentViewTagsArray = ClearCaseUtils.getStreamViewTags(stream + pVOB);

			/*
			 * make sure that newBaseline is not already the parent stream's
			 * foundation BL
			 */
			clearToolparams = new String[] { "", "lsstream", "-fmt", "%[found_bls]p",
					parentStream + pVOB };
			String parentFoundationBaseline = ctc.runClearToolCommand(clearToolparams, false)
					.getStdOut();
			if (newBaseline.equals(parentFoundationBaseline)) {
				terminate("Baseline " + parentFoundationBaseline + " is already the foundation of "
						+ parentStream + pVOB);
			}
			/*
			 * rebase parent
			 */
			clearToolparams = new String[] { "", "rebase", "-baseline", newBaseline, "-view",
					parentViewTagsArray[0], "-complete", "-ok" };
			ctc.runClearToolCommand(clearToolparams, false);
		}
		/*
		 * rename current stream
		 */
		clearToolparams = new String[] { "", "rename", "stream:" + stream + pVOB,
				"stream:" + renamedStream + pVOB };
		ctc.runClearToolCommand(clearToolparams, false);
		/*
		 * rename current branch
		 */
		clearToolparams = new String[] { "", "rename", "brtype:" + stream + pVOB,
				"brtype:" + renamedStream + pVOB };
		ctc.runClearToolCommand(clearToolparams, false);
		/*
		 * processView
		 */
		processViews();
		/*
		 * check whether stream is locked
		 */
		clearToolparams = new String[] { "", "lsstream", "-fmt", "%[locked]p",
				"stream:" + renamedStream + pVOB };
		String streamLockStatus = ctc.runClearToolCommand(clearToolparams, true).getStdOut();
		logger.info(renamedStream + " streamLockStatus: " + streamLockStatus);
		/*
		 * lock stream
		 */
		if (streamLockStatus.equalsIgnoreCase("unlocked")) {
			clearToolparams = new String[] { "", "lock", "-obsolete", "-nc",
					"stream:" + renamedStream + pVOB };
			ctc.runClearToolCommand(clearToolparams, false);
		}
		/*
		 * create new stream
		 */
		clearToolparams = new String[] { "", "mkstream", "-in", "stream:" + parentStream + pVOB,
				"-nc", "-baseline", "baseline:" + newBaseline + pVOB, "stream:" + stream + pVOB };
		ctc.runClearToolCommand(clearToolparams, false);
	}

	/*
	 * processViews
	 */
	final void processViews() {
		clearToolparams = new String[] { "", "lsstream", "-fmt", "%[views]p", renamedStream + pVOB };
		String viewTags = ctc.runClearToolCommand(clearToolparams, false).getStdOut().trim();
		if (viewTags.length() > 0) {
			String[] viewTagsArray = viewTags.split(" ");
			for (int i = 0; i < viewTagsArray.length; i++) {
				logger.finest("view tag: " + viewTagsArray[i]);
				String viewTag = viewTagsArray[i];
				clearToolparams = new String[] { "", "lsview", viewTag };
				String lsViewLine = ctc.runClearToolCommand(clearToolparams, false).getStdOut();
				String[] lsviewLineArray = lsViewLine.split(" ");
				String viewStorage = lsviewLineArray[lsviewLineArray.length - 1];
				logger.finest("viewStorage: " + viewStorage);
				if (removeViews) {
					/*
					 * remove view
					 */
					clearToolparams = new String[] { "", "rmview", viewStorage };
					ctc.runClearToolCommand(clearToolparams, false);
				} else {
					clearToolparams = new String[] { "", "setactivity", "-nc", "-view", viewTag,
							"-none" };
					ctc.runClearToolCommand(clearToolparams, false);
					clearToolparams = new String[] { "", "endview", "-server", viewTag };
					ctc.runClearToolCommand(clearToolparams, false);
					clearToolparams = new String[] { "", "rmtag", "-view", viewTag };
					ctc.runClearToolCommand(clearToolparams, false);
					clearToolparams = new String[] { "", "unregister", "-view", viewStorage };
					ctc.runClearToolCommand(clearToolparams, false);
					File viewStorageFile = new File(viewStorage);
					if (!viewStorageFile.exists()) {
						terminate("View storage does not exit!");
					}
					if (!viewStorageFile.isDirectory()) {
						terminate("View storage should be a directory!");
					}
					String viewStorageName = viewStorageFile.getName();
					logger.finest("viewStorageParent: " + viewStorageFile.getParent());
					logger.finest("viewStorageName: " + viewStorageName);
					String renameViewTag = viewTag + "_" + suffix;
					String renameViewStorageString = viewStorageFile.getParent() + File.separator
							+ renameViewTag + ".vws";
					viewStorageFile.renameTo(new File(renameViewStorageString));
					clearToolparams = new String[] { "", "register", "-view",
							renameViewStorageString };
					ctc.runClearToolCommand(clearToolparams, false);
					clearToolparams = new String[] { "", "mktag", "-view", "-tag", renameViewTag,
							renameViewStorageString };
					ctc.runClearToolCommand(clearToolparams, false);
				}
			}
		}
	}

	/*
	 * terminate
	 */
	void terminate(final String terminationMessag) {
		logger.severe(terminationMessag + ". Processing terminated !");
		JOptionPane.showMessageDialog(null, terminationMessag, "Validation failure",
				JOptionPane.ERROR_MESSAGE);
		System.exit(-1);
	}
}
