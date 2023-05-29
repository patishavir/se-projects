package oz.clearcase.stream.bl2stream;

import oz.infra.datetime.DateTimeUtils;

public class BL2StreamParameters {
	private static String stream = null;

	private static String parentStream = null;

	private static String newBaseLine = null;

	private static String pVOB = null;

	private static String suffix = null;

	private static boolean removeViews = false;

	private static boolean rebaseParentFirst = false;

	private static boolean skipCurrentFoundationBaselineCheck = false;

	private static boolean bl2StreamDebug = true;

	private static String logFileFolderPath = "c:\\temp\\BL2Stream\\";

	static void getParameters() {
		stream = System.getenv("stream");
		parentStream = System.getenv("parentStream");
		newBaseLine = System.getenv("newBaseLine");
		pVOB = System.getenv("pVOB");
		String removeViewsString = System.getenv("removeViews");
		String rebaseParentFirstString = System.getenv("rebaseParentFirst");
		String skipCurrentFoundationBaselineCheckString = System
				.getenv("skipCurrentFoundationBaselineCheck");
		String bl2StreamDebugString = System.getenv("bl2StreamDebug");
		removeViews = (removeViewsString != null) && removeViewsString.equalsIgnoreCase("yes");
		rebaseParentFirst = (rebaseParentFirstString != null)
				&& rebaseParentFirstString.equalsIgnoreCase("yes");
		skipCurrentFoundationBaselineCheck = (skipCurrentFoundationBaselineCheckString != null)
				&& skipCurrentFoundationBaselineCheckString.equalsIgnoreCase("yes");
		bl2StreamDebug = (bl2StreamDebugString != null)
				&& bl2StreamDebugString.equalsIgnoreCase("yes");
		suffix = DateTimeUtils.formatDate("yyyy-MM-dd_hh-mm");

		// bl2StreamDebug = true;
		//
		// if (bl2StreamDebug) {
		// pVOB = "tempPVOB01";
		// newBaseLine = "tempProj01_22_04_2007.3992";
		// stream = "tempProj01_Fix";
		// parentStream = "tempProj01_Dev";
		// removeViews = false;
		// rebaseParentFirst = false;
		// }

	}

	public static final String getNewBaseLine() {
		return newBaseLine;
	}

	public static final String getParentStream() {
		return parentStream;
	}

	public static final String getPVOB() {
		return pVOB;
	}

	public static final String getStream() {
		return stream;
	}

	public static final boolean isRemoveViews() {
		return removeViews;
	}

	public static final String getSuffix() {
		return suffix;
	}

	public static final boolean isBl2StreamDebug() {
		return bl2StreamDebug;
	}

	public static final boolean isRebaseParentFirst() {
		return rebaseParentFirst;
	}

	public static final String getLogFileFolderPath() {
		return logFileFolderPath;
	}

	public static boolean isSkipCurrentFoundationBaselineCheck() {
		return skipCurrentFoundationBaselineCheck;
	}
}
