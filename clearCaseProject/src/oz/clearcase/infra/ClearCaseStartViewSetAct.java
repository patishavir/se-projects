package oz.clearcase.infra;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;

public class ClearCaseStartViewSetAct {
	private static final Logger logger = JulUtils.getLogger();

	public static void startViewSetAct(final String viewTag, final String ccViewRootFolderPath, final String streamSelector, final String stgloc,
			final String activitySelector) {
		boolean viewExisits = ClearCaseUtils.isViewExists(viewTag);
		logger.info(String.valueOf(viewExisits));
		if (!viewExisits) {
			// make view
			Outcome outcome = ClearCaseUtils.mkview(viewTag, streamSelector, stgloc);
			if (outcome == Outcome.FAILURE) {
				logger.warning("mkview operation has failed for " + viewTag);
			}
		}
		SystemCommandExecutorResponse scer = ClearCaseUtils.startView(viewTag, ccViewRootFolderPath);
		boolean activityExists = ClearCaseUtils.isActivityExists(viewTag, activitySelector);
		logger.info("viewtag " + viewTag + " activity " + activitySelector + " exists: " + String.valueOf(activityExists));
		if (!activityExists) {
			String[] clearToolParameters = { activitySelector };
			ClearCaseUtils.mkactivity(clearToolParameters, new File(ccViewRootFolderPath));
		}
		String activitySetinView = ClearCaseUtils.getActivitySetInView(viewTag);
		if (activitySetinView.length() > 0) {
			logger.info("activitySetinView: " + activitySetinView);
		} else {
			logger.info("activitySetinView: " + "no activity set in view");
		}
		String[] clearToolParameters = { "-view", viewTag, activitySelector };
		ClearCaseUtils.setactivity(clearToolParameters, new File(ccViewRootFolderPath));

	}
}