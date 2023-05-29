package oz.infra.clearcase.test;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.clearcase.ClearCaseUtils;
import oz.infra.constants.OzConstants;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.string.StringUtils;

public class ClearCaseUtilsTest {
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
		// testRemoveChangeSet();
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
		testGetViewType();
	}

	private static void testGetChangeSet() {
		File viewContextFile = new File("M:/tmpProj01_Fix_Dyn/tmpVOB");
		String[] changeSetArray = ClearCaseUtils.getChangeSet("Activity_4Fix_Stream@\\tmpPVOB", viewContextFile);
		ArrayUtils.printArray(changeSetArray, OzConstants.LINE_FEED);
	}

	private static void testStartView() {
		// String viewTag = "tmpProj01_Fix_Dyn";
		// String ccViewRootFolder = "M:\\tmpProj01_Fix_Dyn\\tmpVOB";
		String viewTag = "s177571_tmpProj01_Dev";
		String ccViewRootFolder = "C:/CCViews/tmpProj01_Dev/tmpVOB";
		boolean waitParam = true;
		ClearCaseUtils.startView(viewTag, ccViewRootFolder, waitParam);
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
		ListUtils.getAsTitledDelimitedString(componentsList, "latest baseline list. size:\n" + String.valueOf(componentsList.size()),
				Level.INFO, OzConstants.LINE_FEED);
	}

	private static void testGetViewProperties() {
		// ClearCaseUtils.getViewProperties("C:\\CCViews\\tmpProj01_Dev");
		PropertiesUtils.printProperties(ClearCaseUtils.getViewPropertiesByViewRoot("C:\\CCViews\\common_Dev"),
				Level.INFO);
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
		//
		PropertiesUtils.printProperties(ClearCaseUtils.getViewPropertiesByViewTag("s177571_common_Dev"), Level.INFO);
		logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, OzConstants.INT_100));
		//
		PropertiesUtils.printProperties(ClearCaseUtils.getViewPropertiesByViewRoot("M:\\tmpProj01_Fix_Dyn\\tmpVOB"),
				Level.INFO);
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

	private static void testMkactivity() {
		String[] mkactivityParametersArray = { "-headline", "headheadheadhead", "-in", "stream:tmpProj01_Dev@\\tmpPVOB",
				"-f" };
		SystemCommandExecutorResponse systemCommandExecutorResponse = ClearCaseUtils
				.mkactivity(mkactivityParametersArray);
		logger.info("Activity Name: " + ClearCaseUtils.getActivityName(systemCommandExecutorResponse));
		String[] mkactivityParametersArray1 = { "-headline", "headheadheadheadddd", "-f" };
		SystemCommandExecutorResponse systemCommandExecutorResponse1 = ClearCaseUtils
				.mkactivity(mkactivityParametersArray1, new File("c:/CCViews/tmpProj01_Dev"));
		logger.info("Activity Name: " + ClearCaseUtils.getActivityName(systemCommandExecutorResponse1));
	}

	private static void testRemoveChangeSet() {
		String viewContext = "M:\\tmpProj01_Dev_Dyn\\tmpVOB\\tmpComp01";
		String logFilePath = "c:\\temp\\removeChangeSet.log";
		ClearCaseUtils.removeChangeSet("DataStage_deployment_20131218.3@\\tmpPVOB", logFilePath, viewContext);
	}

	private static void testRunClearToolCommand() {
		// String[] params = { ClearCaseUtils.getClearToolPath(), "lsactivity",
		// "-cview" };
		String[] params = { ClearCaseUtils.getClearToolPath(), "lsactivity", "-cact", "-fmt", "%[name]p" };
		SystemCommandExecutorResponse scer = ClearCaseUtils.runClearToolCommand(params,
				new File("C:\\CCViews\\tmpProj01_Dev"));
		logger.info(scer.getExecutorResponse().toString());
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

	private static void testUpdateCurrentView() {
		ClearCaseUtils.updateCurrentView(new File("C:\\CCViews\\tmpProj01_Dev"));
		ClearCaseUtils.updateCurrentView("C:\\CCViews\\tmpProj01_Dev");
	}
}
