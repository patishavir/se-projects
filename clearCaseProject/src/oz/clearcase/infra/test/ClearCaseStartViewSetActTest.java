package oz.clearcase.infra.test;

import java.io.File;
import java.util.logging.Logger;

import oz.clearcase.infra.ClearCaseStartViewSetAct;
import oz.clearcase.infra.ClearCaseUtils;
import oz.infra.logging.jul.JulUtils;

public class ClearCaseStartViewSetActTest {
	private static String viewTag = "tmpProj01_Fix_OZ_Dyn";
	private static String ccViewRootFolderPath = "M:" + File.separator + viewTag;
	private static String streamSelector = "stream:tmpProj01_Fix@\\tmpPVOB";
	private static String stgloc = "Views";
	private static String activitySelector = "myactivityname";
	private static final Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		logger.info(ClearCaseUtils.getMvfsDriveLetter());
		// ParametersUtils.processPatameters(args[0], ClearCaseStartViewSetAct.class);
		ClearCaseStartViewSetAct.startViewSetAct(viewTag, ccViewRootFolderPath,streamSelector,stgloc, activitySelector);
	}
}
