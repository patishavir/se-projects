package oz.clearcase.infra.test;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.clearcase.infra.ClearCaseUtils;
import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.properties.PropertiesUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.env.EnvironmentUtils;

public class ClearCaseUtilsTest {
	private static final String VIEW_TAG = "viewTag";
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// testRunClearToolCommand();
		// System.exit(0);
		// testGetStreamComponents();
		// testGetStreamLatestBls();
		// logger.info(ClearCaseUtils.getClearCaseHome());
		// logger.info(ClearCaseUtils.getClearToolPath());
		// testSetClearCaseEnvironmentVariables();
		// testGetViewProperties();
		testRemoveChangeSet();
		// testGetChangeSet();
		// testUpdateCurrentView();
		// testMkactivity();
		// testGetViewTag();
		// testGetViewType();
		//
		// testRunClearToolCommand();
		// testMkactivity();
		// testGetComponentsInPVOB();
		// testGetNumberOfAvailableLicenses();
		// testGetNumberOfActiveUsers();
		// testGetComponentRootDirectory();
		// String[] componentsl =
		// ClearCaseUtils.getComponentsSelectorsInStream("Snifit_BTT612_Dev");
		// ArrayUtils.printArray(componentsl);
		// String[] componentn =
		// ClearCaseUtils.getComponentsNamesInStream("Snifit_BTT612_Dev");
		// ArrayUtils.printArray(componentn);
		//
		// ArrayUtils.printArray(ClearCaseUtils.getPvobs(), "\n", "Pvobs: ",
		// Level.INFO);
		// logger.info(ClearCaseUtils.getClearCaseHome());
		// logger.info(ClearCaseUtils.getClearToolPath());
		// logger.info(String.valueOf(ClearCaseUtils.isStreamReadOnly("Snifit_BTT612_Dev@\\projects")));
		// logger.info(ClearCaseUtils.getParentStream("Snifit_BTT612_Dev@\\projects"));
		// ClearCaseUtils.cloneStream("\\tmpPVOB", "tmpProj01_Fix023",
		// "tmpProj01_Fix023C", "READONY");
		// testStartView();
		// testGetViewProperties();
		// testGetViewType();
		// testGetActivitySetInView("tmpProj01_Dev_Dyn");
		// testClearActivityFromView("tmpProj01_Dev_Dyn");
		// testGetActivitySetInView("tmpProj01_Dev_Dyn");
		// testMkactivity();
		// testDsScenario("M:\\s177571_tmpProj01_Dev_Dyn\\tmpVOB\\tmpComp\\deployments");
		// testIsViewExists("xxx");
		// testIsViewExists("tmpProj01_Dev_Dyn");
		// testMkView1();
		// testDsProcess();

	}

	private static void testClearActivityFromView(final String viewTag) {
		ClearCaseUtils.clearActivityFromView(viewTag);
	}

	private static void testDsProcess() {
		String viewTag = "s177571_tmpProj01_Dev_Dyn";
		String stream = "stream:tmpProj01_Dev@\\tmpPVOB";
		String stgloc = "Views";
		String viewFolderPath = "M:\\s177571_tmpProj01_Dev_Dyn\\tmpVOB";
		String activityName = viewTag;
		boolean viewExists = ClearCaseUtils.isViewExists(viewTag);
		if (!viewExists) {
			testMkView(viewTag, stream, stgloc);
		}
		ClearCaseUtils.startView(viewTag, viewFolderPath, true);
		String currentActivity = ClearCaseUtils.getActivitySetInView(viewTag);
		if (!currentActivity.equals(activityName)) {
			ClearCaseUtils.clearActivityFromView(viewTag);
		}
		boolean activityExists = ClearCaseUtils.isActivityExists(viewTag, activityName);
		if (!activityExists) {
			// mkact/ivity [ –c/omment comment | –cfi/le pname | –cq/uery | –cqe/ach | –nc/omment ] [ –hea/dline headline ] [ –in stream-selector ] [ –nse/t ] [ –f/orce ] [
			// * activity-selector ...]
			String[] clearToolParameters = { "-in", stream, activityName };
			ClearCaseUtils.mkactivity(clearToolParameters, new File(viewFolderPath));
		}
		ClearCaseUtils.setactivityInView(viewTag, activityName);

	}

	private static void testDsScenario(final String ccViewRootFolderPath) {
		String viewTag = EnvironmentUtils.getActualEnvVarValue(null, VIEW_TAG);
		String activitySetInViewName = ClearCaseUtils.getActivitySetInView(viewTag);
		String activityName4DsCC = SystemPropertiesUtils.getUserName().toLowerCase();
		if (!activitySetInViewName.equals(activityName4DsCC)) {
			if (activitySetInViewName.length() > OzConstants.INT_0) {
				Outcome outcome = ClearCaseUtils.clearActivityFromView(viewTag);
				logger.info("outcome: " + outcome.toString());
			}
			String[] allActivitiesInStream = ClearCaseUtils.getAllActivitiesInStream(viewTag);
			boolean activity4DsCCExists = ClearCaseUtils.isActivityExists(viewTag, activityName4DsCC);

			if (!activity4DsCCExists) {
				File directory = new File(ccViewRootFolderPath);
				String[] clearToolParameters = { activityName4DsCC };
				ClearCaseUtils.mkactivity(clearToolParameters, directory);
			}
		}
	}

	private static void testGetActivitySetInView(final String viewTag) {
		logger.info(ClearCaseUtils.getActivitySetInView(viewTag));
	}

	private static void testGetChangeSet() {
		File viewContextFile = new File("M:/tmpProj01_Fix_Dyn/tmpVOB");
		String[] changeSetArray = ClearCaseUtils.getChangeSet("Activity_4Fix_Stream@\\tmpPVOB", viewContextFile);
		ArrayUtils.printArray(changeSetArray, OzConstants.LINE_FEED);
	}

	private static void testGetComponentRootDirectory() {
		logger.info(ClearCaseUtils.getComponentRootDirectory("ggg"));
		logger.info(ClearCaseUtils.getComponentRootDirectory("ArvuyotServer@\\projects"));
		logger.info(ClearCaseUtils.getComponentRootDirectory("Utils@\\projects"));
		logger.info(ClearCaseUtils.getComponentRootDirectory("Snifit_BL@\\projects"));
		String[] components = { "Utils@\\projects", "ArvuyotServer@\\projects", "AshraiClient@\\projects", "ppp" };
		String[] rootDirectories = ClearCaseUtils.getComponentsRootDirectory(components);
		ArrayUtils.printArray(rootDirectories, "\n", "Components root directory:\n", Level.INFO);
	}

	private static void testGetComponentsInPVOB() {
		String pvob = "\\projects";
		String[] componentsArray = ClearCaseUtils.getComponentsInPVOB(pvob);
		ArrayUtils.printArray(componentsArray, "\n", "Components in pvob " + pvob + "\n", Level.INFO);
		logger.info("Found " + String.valueOf(componentsArray.length) + " components.");
	}

	private static void testGetNumberOfActiveUsers() {
		String[] clearLicenseArray = ClearCaseUtils.getClearLicenseArray();
		logger.info("Active users: ".concat(String.valueOf(ClearCaseUtils.getNumberOfActiveUsers(clearLicenseArray))));
	}

	private static void testGetNumberOfAvailableLicenses() {
		String[] clearLicenseArray = ClearCaseUtils.getClearLicenseArray();
		int numberOfAvailableLicenses = ClearCaseUtils.getNumberOfAvailableLicenses(clearLicenseArray);
		logger.info("numberOfAvailableLicenses: " + String.valueOf(numberOfAvailableLicenses));
	}

	private static void testGetStreamComponents() {
		String streamSelector = "stream:Snifit_BTT710_Int@\\projects";
		String currentDir = "M:/Snifit_BTT710_Int_Dyn";
		String dynamicViewTag = "Snifit_BTT710_Int_Dyn";
		List<String> componentsList = ClearCaseUtils.getStreamComponents(streamSelector, currentDir, dynamicViewTag);
		ListUtils.getAsTitledDelimitedString(componentsList, "components list\n", Level.INFO, OzConstants.LINE_FEED);
	}

	private static void testGetStreamLatestBls() {
		String streamSelector = "stream:Snifit_BTT710_Int@\\projects";
		String currentDir = "M:/Snifit_BTT710_Int_Dyn";
		String dynamicViewTag = "Snifit_BTT710_Int_Dyn";
		List<String> componentsList = ClearCaseUtils.getStreamLatestBls(streamSelector, currentDir, dynamicViewTag);
		ListUtils.getAsTitledDelimitedString(componentsList, "latest baseline list. size:\n" + String.valueOf(componentsList.size()), Level.INFO,
				OzConstants.LINE_FEED);
	}

	private static void testGetViewProperties() {
		// ClearCaseUtils.getViewProperties("C:\\CCViews\\tmpProj01_Dev");
		PropertiesUtils.printProperties(ClearCaseUtils.getViewPropertiesByViewRoot("C:\\CCViews\\common_Dev"), Level.INFO);
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
		//
		PropertiesUtils.printProperties(ClearCaseUtils.getViewPropertiesByViewTag("s177571_common_Dev"), Level.INFO);
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
		//
		PropertiesUtils.printProperties(ClearCaseUtils.getViewPropertiesByViewRoot("M:\\tmpProj01_Fix_Dyn\\tmpVOB"), Level.INFO);
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
		//
		PropertiesUtils.printProperties(ClearCaseUtils.getViewPropertiesByViewTag("tmpProj01_Fix_Dyn"), Level.INFO);
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
	}

	private static void testGetViewTag() {
		ClearCaseUtils.getViewTag("c:\\ccviews");
		ClearCaseUtils.getViewTag("M:\\applinX_Dev_Dyn");
		ClearCaseUtils.getViewTag("C:\\CCViews\\tmpProj01_Dev");
	}

	private static void testGetViewType() {
		ClearCaseUtils.getViewTypeByViewRootFolder("c:\\ccviews");
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
		ClearCaseUtils.getViewTypeByViewRootFolder("M:\\applinX_Dev_Dyn");
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
		ClearCaseUtils.getViewTypeByViewRootFolder("C:\\CCViews\\tmpProj01_Dev");
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
		ClearCaseUtils.getViewTypeByViewRootFolder("M:/tmpProj01_Fix_Dyn/tmpVOB");
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
		ClearCaseUtils.getViewTypeByViewTag("tmpProj01_Fix_Dyn");
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));

	}

	private static void testIsViewExists(final String viewTag) {
		boolean result = ClearCaseUtils.isViewExists(viewTag);
		if (result) {
			logger.info(viewTag + " exists !");
		} else {
			logger.info(viewTag + " does not exists !");
		}
	}

	private static void testMkactivity() {
		// String[] mkactivityParametersArray = { "-headline", "headheadheadhead", "-in", "stream:tmpProj01_Dev@\\tmpPVOB", "-f" };
		// SystemCommandExecutorResponse scer = ClearCaseUtils.mkactivity(mkactivityParametersArray);
		// logger.info("Activity Name: 00000000000 " + ClearCaseUtils.getActivityName(scer));
		String[] mkactivityParametersArray1 = { "-headline", "headheadheadheadddd", "-f" };
		SystemCommandExecutorResponse scer1 = ClearCaseUtils.mkactivity(mkactivityParametersArray1, new File("c:/CCViews/tmpProj01_Dev"));
		logger.info("Activity Name: " + ClearCaseUtils.getActivityName(scer1));
		// String[] mkactivityParametersArray2 = { "-headline", "datastage move to production", ClearCaseUtils.getDefaultActivityName() };
		// SystemCommandExecutorResponse scer2 = ClearCaseUtils.mkactivity(mkactivityParametersArray2,
		// new File("c:/CCViews/tmpProj01_Dev"));
		// logger.info(scer2.getExecutorResponse());
	}

	private static void testMkView(final String viewTag, final String stream, final String stgloc, final String... snapShotPname) {
		Outcome outcome = ClearCaseUtils.mkview(viewTag, stream, stgloc, snapShotPname);
		logger.info(outcome.toString());
	}

	private static void testMkView1() {
		testMkView("viewTag", "stream", "stgloc");
		testMkView("tmpProj01_Dev_Dyn", "stream:tmpProj01_Dev@\\tmpPVOB", "Views");
		testMkView("s177571_tmpProj01_Dev_Dyn", "tmpProj01_Dev@\\tmpPVOB", "Views");
		testMkView("s177571_tmpProj01_Dev_snap", "tmpProj01_Dev@\\tmpPVOB", "Views", "c:\\ccviews\\tmpProj01_Dev_snap");
	}

	private static void testRemoveChangeSet() {
		String viewContext = "M:\\tmpProj01_Dev_Dyn\\tmpVOB\\tmpComp01";
		viewContext = "M:\\tmpProj01_Dev_Dyn_S177571";
		String logFilePath = "c:\\temp\\removeChangeSet.log";
		String activitySelector = "DataStage_deployment_20131218.3@\\tmpPVOB";
		activitySelector = "tmpProj01_Dev_Dyn_S177571_2019";
		ClearCaseUtils.removeChangeSet(activitySelector, logFilePath, viewContext);
	}

	private static void testRunClearToolCommand() {
		// String[] params = { ClearCaseUtils.getClearToolPath(), "lsactivity",
		// "-cview" };
		String[] params = { ClearCaseUtils.getClearToolPath(), "lsactivity", "-cact", "-fmt", "%[name]p" };
		SystemCommandExecutorResponse scer = ClearCaseUtils.runClearToolCommand(params, new File("C:\\CCViews\\tmpProj01_Dev"));
		logger.info(scer.getExecutorResponse());
		// String[] params = { ClearCaseUtils.getClearToolPath(), "lsview",
		// "-cview" };
		// ClearCaseUtils.runClearToolCommand(params, new
		// File("C:\\CCViews\\tmpProj01_Dev"));
		// String[] params1 = { ClearCaseUtils.getClearToolPath(), "lsview",
		// "-l", "-cview" };
		// ClearCaseUtils.runClearToolCommand(params1, new
		// File("C:\\CCViews\\tmpProj01_Dev"));
	}

	private static void testSetClearCaseEnvironmentVariables() {
		ClearCaseUtils.setClearCaseEnvironmentVariables();
	}

	private static void testStartView() {
		// String viewTag = "tmpProj01_Fix_Dyn";
		// String ccViewRootFolder = "M:\\tmpProj01_Fix_Dyn\\tmpVOB";
		String viewTag = "s177571_tmpProj01_Dev";
		String ccViewRootFolder = "C:/CCViews/tmpProj01_Dev/tmpVOB";
		boolean waitParam = true;
		ClearCaseUtils.startView(viewTag, ccViewRootFolder, waitParam);
	}

	private static void testUpdateCurrentView() {
		ClearCaseUtils.updateCurrentView(new File("C:\\CCViews\\tmpProj01_Dev"));
		ClearCaseUtils.updateCurrentView("C:\\CCViews\\tmpProj01_Dev");
	}
}
