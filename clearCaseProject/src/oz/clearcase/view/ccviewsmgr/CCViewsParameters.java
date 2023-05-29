package oz.clearcase.view.ccviewsmgr;

import oz.clearcase.infra.ClearCaseUtils;
import oz.clearcase.view.ccviewsmgr.gui.CCViewsJDialog;
import oz.clearcase.view.ccviewsmgr.listeners.CCViewsMenuBarActionListener;

public class CCViewsParameters {
	private static String userName = null;
	private static boolean showDetails = false;
	private static boolean fileInput = false;
	private static boolean enableAdminOperations = false;
	private static boolean generateScripts = false;
	private static String viewTagFilter = "";
	private static String viewStorageFilter = "";
	private static String optionsGuiXmlFile = "CCViewsMgr.xml";
	private static String detailedFilePath = "C:\\Temp\\lsview-l-prop.txt";
	private static String nonDetailedFilePath = "C:\\Temp\\lsview.txt";
	private static String scriptFilePath = "C:\\Temp\\ccViewsMgrScripts.bat";
	private static String script2Run = null;
	private static String ClearCaseHome = ClearCaseUtils.getClearCaseHome();
	private static String ccperlPath = ClearCaseHome + "\\bin\\ccperl.exe";
	private static String regenViewDotDatPath = ClearCaseHome
			+ "\\etc\\utils\\regen_view_dot_dat.pl";

	public static final String getCcperlPath() {
		return ccperlPath;
	}

	public static String getDetailedFilePath() {
		return detailedFilePath;
	}

	public static String getNonDetailedFilePath() {
		return nonDetailedFilePath;
	}

	public static final String getOptionsGuiXmlFile() {
		return optionsGuiXmlFile;
	}

	public static final String getRegenViewDotDatPath() {
		return regenViewDotDatPath;
	}

	public static final String getScript2Run() {
		return script2Run;
	}

	public static final String getScriptFilePath() {
		return scriptFilePath;
	}

	/**
	 * @return Returns the userName.
	 */
	protected static String getUserName() {
		return userName;
	}

	public static final String getViewStorageFilter() {
		return viewStorageFilter;
	}

	public static final String getViewTagFilter() {
		return viewTagFilter;
	}

	public static final boolean isEnableAdminOperations() {
		return enableAdminOperations;
	}

	//
	public static boolean isFileInput() {
		return fileInput;
	}

	public static boolean isGenerateScripts() {
		return generateScripts;
	}

	/**
	 * @return Returns the showDetails.
	 */
	public static boolean isShowDetails() {
		return showDetails;
	}

	public static final void setDetailedFilePath(final String detailedFilePathP) {
		CCViewsParameters.detailedFilePath = detailedFilePathP;
	}

	public static final void setEnableAdminOperations(final String enableAdminOperations) {
		CCViewsParameters.enableAdminOperations = enableAdminOperations.equalsIgnoreCase("yes");
		CCViewsJDialog.setCCViewsJDialog(null);
	}

	public static void setFileInput(final String fileInputP) {
		CCViewsParameters.fileInput = fileInputP.equalsIgnoreCase("yes");
	}

	public static final void setGenerateScripts(boolean generateScripts) {
		CCViewsParameters.generateScripts = generateScripts;
	}

	public static void setGenerateScripts(final String generateScriptsP) {
		CCViewsParameters.generateScripts = generateScriptsP.equalsIgnoreCase("yes");
	}

	public static final void setNonDetailedFilePath(final String undetailedFilePathP) {
		CCViewsParameters.nonDetailedFilePath = undetailedFilePathP;
	}

	public static final void setOptionsGuiXmlFile(final String optionsGuiXmlFileP) {
		CCViewsParameters.optionsGuiXmlFile = optionsGuiXmlFileP;
	}

	public static final void setScript2Run(final String script2Run) {
		CCViewsParameters.script2Run = script2Run;
	}

	public static final void setScriptFilePath(final String scriptFilePath) {
		CCViewsParameters.scriptFilePath = scriptFilePath;
	}

	/**
	 * @param showDetails
	 *            The showDetails to set.
	 */
	public static void setShowDetails(final boolean showDetailsP) {
		CCViewsParameters.showDetails = showDetailsP;
	}

	/**
	 * @param userName
	 *            The userName to set.
	 */
	public static void setUserName(final String userNameP) {
		String oldUserName = userName;
		CCViewsParameters.userName = userNameP;
		if ((oldUserName != null) && (!oldUserName.equals(userName))) {
			CCViewsMenuBarActionListener.refresh();
		}
	}

	public static final void setViewStorageFilter(final String viewStorageFilterP) {
		String oldViewStorageFilter = viewStorageFilter;
		viewStorageFilter = viewStorageFilterP;
		if (!oldViewStorageFilter.equals(viewStorageFilter)) {
			CCViewsMenuBarActionListener.refresh();
		}
	}

	public static final void setViewTagFilter(final String viewTagFilterP) {
		String oldViewTagFilter = viewTagFilter;
		CCViewsParameters.viewTagFilter = viewTagFilterP;
		if (!oldViewTagFilter.equals(viewTagFilter)) {
			CCViewsMenuBarActionListener.refresh();
		}
	}
}