package oz.clearcase.setactivity;

import java.util.logging.Logger;

import oz.clearcase.infra.ClearToolCommand;
import oz.clearcase.infra.ClearToolResults;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public class SetActivityMain {

	/**
	 * @param args
	 */
	private static String[] clearToolparams;
	private static ClearToolCommand ctc = new ClearToolCommand(false, false);
	private static ClearToolResults clearToolResults;
	private static int returnCode = -1;
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 1) {
			clearToolparams = new String[] { "", "lsstream", "-fmt", "%[views]p", args[0] };
			clearToolResults = ctc.runClearToolCommand(clearToolparams, false);
			returnCode = clearToolResults.getReturnCode();
			if (returnCode == 0) {
				String viewTags = clearToolResults.getStdOut().trim();

				if (viewTags != null && viewTags.length() > 0) {
					String[] viewTagsArray = viewTags.split(" ");
					int viewCounter = 0;
					for (String viewTag : viewTagsArray) {
						viewCounter++;
						clearToolparams = new String[] { "", "setactivity", "-nc", "-view",
								viewTag, "-none" };
						clearToolResults = ctc.runClearToolCommand(clearToolparams, false);
						if (clearToolResults.getStdOut().length() > 0) {
							logger.info(clearToolResults.getStdOut());
						}
						if (clearToolResults.getStdErr().length() > 0) {
							logger.info(clearToolResults.getStdErr());
						}
					}
					logger.info("Processing completed for " + String.valueOf(viewCounter)
							+ " views");
				}
			} else {
				SystemUtils.printMessageAndExit(clearToolResults.getStdErr()
						+ "\nProcessing has been aborted!", returnCode);
			}
		} else {
			SystemUtils.printMessageAndExit(
					"One parameter should be supplied.\nProcessing has been aborted!", -1);
		}

	}
}
