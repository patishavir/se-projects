package oz.clearcase.infra;

import java.io.File;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.activedirectory.ActiveDirectoryUtils;
import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.io.FileDirectoryEnum;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.properties.PropertiesUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.infra.system.env.EnvironmentUtils;
import oz.infra.thread.ThreadUtils;
import oz.infra.varargs.VarArgsUtils;

public class ClearCaseUtils {
	private static enum NewStreamDisposition {
		READONY, READWRITE, SAME_AS_SOURCE
	}

	public enum VIEWTYPE_ENUM {
		DYNAMIC, NOT_A_VIEW, SNAPSHOT
	}

	private static final Logger logger = JulUtils.getLogger();
	private static final String CLEARCASE_GROUPS = "CLEARCASE_GROUPS";
	private static final String CLEARCASE_PRIMARY_GROUP = "CLEARCASE_PRIMARY_GROUP";
	private static final String DEFAULT_VIEW_TAG = setDefaultViewTag();

	private static final String LINE_SEPARATOR = "\n";

	private static final String SNAPSHOT = "snapshot";
	private static final String STREAM_NAME = "streamName";
	private static final String TAG = "Tag";
	private static final String VIEW_ATTRIBUTES = "View attributes";
	private static final String MVFS = "MVFS";

	public static Outcome clearActivityFromView(final String viewTag) {
		Outcome outcome = Outcome.FAILURE;
		String[] clearToolParams = { getClearToolPath(), ClearToolSubCommands.SETACTIVITY, "-view", viewTag, "-none" };
		SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(clearToolParams);
		String clearToolParamsString = ArrayUtils.getAsDelimitedString(clearToolParams, OzConstants.BLANK);
		logger.info(StringUtils.concat(clearToolParamsString, SystemUtils.LINE_SEPARATOR, scer.getExecutorResponse()));
		if (scer.getReturnCode() == 0) {
			outcome = Outcome.SUCCESS;
		}
		return outcome;
	}

	public static int cloneStream(final String pvob, final String stream2Clone, final String newStream, final String newStreamDispositionString) {
		NewStreamDisposition newStreamDisposition = NewStreamDisposition.valueOf(newStreamDispositionString);
		String stream2CloneFullName = stream2Clone + "@" + pvob;
		logger.info("Clone stream " + stream2CloneFullName + " as " + newStream);
		// make sure that stream2Clone exists
		String[] clearToolparams = new String[] { "", "lsstream", stream2Clone + "@" + pvob };
		ClearToolCommand clearToolCommand = new ClearToolCommand();
		ClearToolResults clearToolResults = clearToolCommand.runClearToolCommand(clearToolparams, true);
		if (clearToolResults.getReturnCode() != 0) {
			logger.severe(clearToolResults.getStdErr());
			System.exit(OzConstants.EXIT_STATUS_ABNORMAL);
		}
		// make sure that newStream does not exists
		clearToolparams = new String[] { "", "lsstream", newStream + "@" + pvob };
		clearToolResults = clearToolCommand.runClearToolCommand(clearToolparams, true);
		if (clearToolResults.getReturnCode() == 0) {
			logger.severe(newStream + "@" + pvob + " already exists!");
			System.exit(OzConstants.EXIT_STATUS_ABNORMAL);
		}
		boolean isSourceStreamReadOnly = isStreamReadOnly(stream2CloneFullName);
		logger.info("isReadOnly = " + String.valueOf(isSourceStreamReadOnly));
		String[] foundationBAseLines = getStreamFoundationBaselines(stream2CloneFullName);
		String parentStream = getParentStream(stream2CloneFullName);
		logger.info("parent Stream: " + parentStream);
		StringBuilder foundationBaseLineStringBuilder = new StringBuilder();
		for (int i = 0; i < foundationBAseLines.length; i++) {
			foundationBaseLineStringBuilder.append(foundationBAseLines[i]);
			if (foundationBAseLines.length > 1 && i < foundationBAseLines.length - 1) {
				foundationBaseLineStringBuilder.append(",");
			}
		}
		logger.info("Foundation Baselines: " + foundationBaseLineStringBuilder.toString());
		String delimiter = "!";
		String ccParamsString = StringUtils.concat(delimiter, delimiter, "mkstream", delimiter, "-in", delimiter, parentStream, "@", pvob, delimiter,
				"-baseline", delimiter, foundationBaseLineStringBuilder.toString(), delimiter);
		if ((isSourceStreamReadOnly && newStreamDisposition == NewStreamDisposition.SAME_AS_SOURCE)
				|| newStreamDisposition == NewStreamDisposition.READONY) {
			ccParamsString = ccParamsString.concat("-readonly" + delimiter);
		}
		ccParamsString = ccParamsString.concat(newStream + "@" + pvob);
		logger.info(ccParamsString);
		clearToolResults = clearToolCommand.runClearToolCommand(ccParamsString.split(delimiter), true);
		logger.info(clearToolResults.getStdOut());
		logger.info(clearToolResults.getStdErr());
		return clearToolResults.getReturnCode();
	}

	public static void createRmactivityBatFile(final String viewPath, final String pvob, final String batFilePath) {
		String[] clearToolParametersArray = { getClearToolPath(), "lsactivity", "-fmt", "%n\n", "-cview" };
		SystemCommandExecutorResponse scer = runClearToolCommand(clearToolParametersArray, new File(viewPath));
		logger.finest(scer.getExecutorResponse());
		String[] activityArry = scer.getStdout().toString().split(OzConstants.LINE_FEED);
		StringBuilder sb = new StringBuilder();
		for (String activity : activityArry) {
			sb.append("cleartool.exe");
			sb.append(" ");
			sb.append("rmactivity");
			sb.append(" -f ");
			sb.append(activity);
			sb.append("@\\");
			sb.append(pvob);
			sb.append("\nRem pause\n");
		}
		sb.append("pause");
		logger.info(sb.toString());
		FileUtils.writeFile(batFilePath, sb.toString());
	}

	public static void createRmelemBatFile(final String folderPath2Remove, final String batFilePath) {
		StringBuilder sb = new StringBuilder();
		List<File> fileList = FolderUtils.getRecursivelyAllFiles(folderPath2Remove);
		if (fileList != null) {
			for (File file1 : fileList) {
				sb.append("cleartool.exe rmelem  -f ");
				sb.append("\"");
				sb.append(file1.getAbsolutePath());
				sb.append("\"");
				sb.append(OzConstants.LINE_FEED);
			}
		}
		List<String> subFolderList = FolderUtils.getRecursivelyAllSubFoldersPaths(folderPath2Remove);
		if (subFolderList != null) {
			Collections.sort(subFolderList);
			for (int i = subFolderList.size(); i > 0; i--) {
				sb.append("cleartool.exe rmelem  -f ");
				sb.append(subFolderList.get(i - 1));
				sb.append(OzConstants.LINE_FEED);

			}
		}
		sb.append("pause");
		sb.append(OzConstants.LINE_FEED);
		sb.append(StringUtils.concat("explorer.exe ", folderPath2Remove, OzConstants.LINE_FEED));
		logger.info(StringUtils.concat(batFilePath, " contents:\n", sb.toString()));
		FileUtils.writeFile(batFilePath, sb.toString());
	}

	public static SystemCommandExecutorResponse endView(final String viewTag, final String ccViewRootFolderPath, final Boolean... waitParam) {
		Boolean wait = VarArgsUtils.getMyArg(true, waitParam);
		SystemCommandExecutorResponse scer = null;
		if (viewTag != null && viewTag.length() > 0) {
			String[] parametersArray1 = { getClearToolPath(), ClearToolSubCommands.ENDVIEW, "-server", viewTag };
			scer = SystemCommandExecutorRunner.run(parametersArray1);
			logger.info("endView " + viewTag + " outcome: " + scer.getExecutorResponse());
			if (scer.getReturnCode() == OzConstants.EXIT_STATUS_OK && wait && ccViewRootFolderPath != null) {
				File ccViewRootFolder = new File(ccViewRootFolderPath);
				final int endViewLoopControl = OzConstants.INT_25;
				final long sleepInterval = 1000L;
				for (int i = 0; i < endViewLoopControl; i++) {
					if (!ccViewRootFolder.isDirectory()) {
						logger.info(StringUtils.concat("folder ", ccViewRootFolderPath, " is no longer available."));
						break;
					} else {
						logger.info("waiting for targer folder " + ccViewRootFolderPath + " to become unavailabe. Iteration: " + String.valueOf(i));
						ThreadUtils.sleep(sleepInterval, Level.INFO);
					}
				}
			}
		} else {
			logger.warning("viewTag parameter is null or zero length");
		}
		return scer;
	}

	public static String getActivityName(final SystemCommandExecutorResponse systemCommandExecutorResponse) {
		String stdoutString = systemCommandExecutorResponse.getStdout().toString();
		final String createdActivity = "Created activity ";
		String activityName = null;
		logger.finest("---" + stdoutString.substring(stdoutString.length() - 2) + "---");
		if (stdoutString.startsWith(createdActivity)) {
			activityName = stdoutString.substring(createdActivity.length(), stdoutString.length() - 2);
		}

		return activityName;
	}

	public static String getActivitySetInCurrentView(final String viewPath) {
		String activity = null;
		String[] params = { ClearCaseUtils.getClearToolPath(), ClearToolSubCommands.LSACTIVITY, "-cact", "-fmt", "%n" };
		SystemCommandExecutorResponse scer = ClearCaseUtils.runClearToolCommand(params, new File(viewPath));
		if (scer.getReturnCode() == 0 && scer.getStderr().length() == 0) {
			activity = scer.getStdout();
		}
		return activity;
	}

	public static String getActivitySetInView(final String viewTag) {
		String activityName = null;
		if (viewTag != null) {
			String[] clearToolParams = { getClearToolPath(), ClearToolSubCommands.LSACTIVITY, "-s", "-view", viewTag, "-cact" };
			SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(clearToolParams);
			logger.finest(scer.getExecutorResponse());
			if (scer.getReturnCode() > OzConstants.EXIT_STATUS_OK) {
				logger.info(scer.getStderr());
			}
			activityName = scer.getStdout();
		}
		logger.info("viewTag: " + viewTag + " activity set in view: " + activityName);
		return activityName;
	}

	public static String[] getAllActivitiesInStream(final String viewTag) {
		String[] clearToolParams = { getClearToolPath(), ClearToolSubCommands.LSACTIVITY, "-s", "-view", viewTag };
		SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(clearToolParams);
		String[] allActivitiesInStream = null;
		if (scer != null && scer.getStdout() != null) {
			logger.info(scer.getExecutorResponse());
			allActivitiesInStream = scer.getStdout().split(OzConstants.LINE_FEED);
		}
		return allActivitiesInStream;
	}

	public static final String[] getChangeSet(final String activity, final File currentViewFile) {
		String[] clearToolparams = { getClearToolPath(), "lsactivity", "-fmt", "%[versions]Cp", "activity:" + activity };
		List<String> parametersList = new ArrayList<String>(Arrays.asList(clearToolparams));
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(parametersList, currentViewFile);
		String[] changeSetArray = null;
		if (systemCommandExecutorResponse.getReturnCode() != 0 || systemCommandExecutorResponse.getStderr().length() > 0) {
			logger.warning(systemCommandExecutorResponse.getStderr());
		} else {
			if (systemCommandExecutorResponse.getStdout().length() == 0) {
				changeSetArray = null;
			} else {
				changeSetArray = systemCommandExecutorResponse.getStdout().toString().split(", ");
			}
		}
		return changeSetArray;
	}

	public static String getClearCaseHome() {
		String[] clearCaseHomes = { "c:\\Progra~1\\Rational\\ClearCase", "C:\\Progra~1\\ibm\\RationalSDLC\\ClearCase",
				"C:\\Progra~2\\ibm\\RationalSDLC\\ClearCase" };
		String clearCaseHome = FileUtils.getExisting(clearCaseHomes, FileDirectoryEnum.DIRECTORY);
		return clearCaseHome;
	}

	public static String[] getClearLicenseArray() {
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(ClearCaseUtils.getClearLicensePath(), null);
		String[] clearLicenseArray = systemCommandExecutorResponse.getStdout().toString().split(OzConstants.LINE_FEED);
		return clearLicenseArray;
	}

	public static String getClearLicensePath() {
		return getClearCaseHome() + "\\bin\\clearlicense.exe";
	}

	public static String getClearToolPath() {
		return getClearCaseHome() + "\\bin\\cleartool.exe";
	}

	public static String getComponentRootDirectory(final String component) {
		String componentRoot = null;
		String[] parametersArray = { getClearToolPath(), "lscomp", "-fmt", "%[root_dir]p", component };
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(parametersArray);
		String stdoutString = systemCommandExecutorResponse.getStdout().toString();
		stdoutString = StringUtils.removeTrailingSubString(stdoutString.trim(), "\n");
		if (systemCommandExecutorResponse.getReturnCode() == 0 && systemCommandExecutorResponse.getStderr().length() == 0) {
			componentRoot = stdoutString;
			logger.finest(StringUtils.concat("*", stdoutString, "*"));
		}
		return componentRoot;
	}

	/*
	 * getComponentsInPVOB
	 */
	public static String[] getComponentsInPVOB(final String pvob) {
		ClearToolCommand ctc = new ClearToolCommand();
		String[] clearToolparams = { null, "lscomp", "-s", "-invob", pvob };
		ClearToolResults ctr = ctc.runClearToolCommand(clearToolparams, true);
		if (ctr.getReturnCode() != 0) {
			if (ctr.getStdErr().length() > 0) {
				logger.warning("Err:" + ctr.getStdErr());
			}
			logger.severe("Cleartool lscomp operation failed. Processing has been terminated !");
			return null;
		}
		logger.finest(ctr.getStdOut());
		return ctr.getStdOut().split("\n");
	}

	public static String[] getComponentsNamesInStream(final String stream) {
		String[] componentsNames = null;
		String[] componentsSelectors = getComponentsSelectorsInStream(stream);
		if (componentsSelectors != null) {
			componentsNames = new String[componentsSelectors.length];
			for (int i = 0; i < componentsNames.length; i++) {
				componentsNames[i] = componentsSelectors[i].substring(0, componentsSelectors[i].indexOf(OzConstants.AT_SIGN));
			}
		}
		return componentsNames;
	}

	public static String[] getComponentsRootDirectory(final String[] components) {
		String[] componentsRoot = new String[components.length];
		for (int i = 0; i < components.length; i++) {
			componentsRoot[i] = getComponentRootDirectory(components[i]);
		}
		return componentsRoot;
	}

	public static String[] getComponentsSelectorsInStream(final String stream) {
		String myStream = stream;
		if (stream.indexOf(OzConstants.AT_SIGN) == OzConstants.STRING_NOT_FOUND) {
			String streamPvob = getStreamPvob(myStream);
			myStream = StringUtils.concat(myStream, OzConstants.AT_SIGN, streamPvob);
			logger.finest(myStream);
		}
		String[] parametersArray = { getClearToolPath(), "lsstream", "-fmt", "%[components]CXp", myStream };
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(parametersArray);
		String stdoutString = systemCommandExecutorResponse.getStdout().toString();
		logger.finest(stdoutString);
		String[] components = stdoutString.split("component:");
		logger.finest(String.valueOf(components.length));
		ArrayUtils.printArray(components, "\n", "components in " + myStream, Level.FINEST);
		return ArrayUtils.selectArrayRowsByMinimalLength(components, 1);
	}

	public static String getDefaultActivityName() {
		return DEFAULT_VIEW_TAG;
	}

	public static String getDefaultViewTag() {
		return DEFAULT_VIEW_TAG;
	}

	public static String getMvfsDriveLetter() {
		String mvfsDriveLetter = null;
		Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
		try {
			for (Path path : dirs) {
				FileStore fileStore = Files.getFileStore(path);
				logger.info("path: " + path + " stirng: " + fileStore.toString() + " name: " + fileStore.name() + " type: " + fileStore.type());
				if (fileStore.type().equals(MVFS)) {
					mvfsDriveLetter = path.getRoot().toString().substring(0, 1);
					break;
				}
			}
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return mvfsDriveLetter;
	}

	public static int getNumberOfActiveUsers(final String[] clearLicenseArray) {
		final String currentActiveUsersString = "Current active users:";
		int numberOfActiveUsers = 0;
		for (int i = 0; i < clearLicenseArray.length; i++) {
			int index = clearLicenseArray[i].indexOf(currentActiveUsersString);
			if (index > OzConstants.STRING_NOT_FOUND) {
				String[] lineBreakDown = clearLicenseArray[i].trim().split(RegexpUtils.REGEXP_WHITE_SPACE);
				numberOfActiveUsers = Integer.parseInt(lineBreakDown[lineBreakDown.length - 1]);
			}
		}
		return numberOfActiveUsers;
	}

	public static int getNumberOfAvailableLicenses(final String[] clearLicenseArray) {
		final String currentActiveUsersString = "Available licenses:";
		int numberOfAvailableLicenses = Integer.MAX_VALUE;
		for (int i = 0; i < clearLicenseArray.length; i++) {
			int index = clearLicenseArray[i].indexOf(currentActiveUsersString);
			if (index > OzConstants.STRING_NOT_FOUND) {
				String[] lineBreakDown = clearLicenseArray[i].trim().split(RegexpUtils.REGEXP_WHITE_SPACE);
				numberOfAvailableLicenses = Integer.parseInt(lineBreakDown[lineBreakDown.length - 1]);
			}
		}
		return numberOfAvailableLicenses;
	}

	public static final String getParentStream(final String stream) {
		String[] clearToolparams = new String[] { getClearToolPath(), "lsstream", "-ancestor", "-depth", "2", stream };
		ClearToolCommand clearToolCommand = new ClearToolCommand();
		ClearToolResults clearToolResults = clearToolCommand.runClearToolCommand(clearToolparams, true);
		if (clearToolResults.getReturnCode() != 0) {
			return null;
		} else {
			String stdout = clearToolResults.getStdOut();
			if (stdout.startsWith("*")) {
				stdout = stdout.substring(1);
			}
			return stdout.substring(0, stdout.indexOf("stream")).trim();
		}
	}

	/*
	 * 
	 */
	public static final String[] getPvobs() {
		final boolean dontAcceptCleartoolFailure = false;
		ClearToolCommand ctc = new ClearToolCommand(null, false);
		String[] clearToolparams = new String[] { "", "lsvob" };
		ClearToolResults ctResults = ctc.runClearToolCommand(clearToolparams, dontAcceptCleartoolFailure);
		if (ctResults.getReturnCode() != 0) {
			return null;
		}
		String ctStdout = ctResults.getStdOut();
		String[] vobsArray = ctStdout.split(LINE_SEPARATOR);
		StringBuffer pvobsStringBuffer = new StringBuffer();
		for (int i = 0; i < vobsArray.length; i++) {
			if (vobsArray[i].endsWith("(ucmvob)")) {
				if (pvobsStringBuffer.length() > 0) {
					pvobsStringBuffer.append(LINE_SEPARATOR);
				}
				pvobsStringBuffer.append(vobsArray[i]);
			}
		}
		String pvobsString = pvobsStringBuffer.toString();
		String[] pvobsArray = pvobsString.split(LINE_SEPARATOR);
		StringBuffer pvob1StringBuffer = new StringBuffer();
		for (int i = 0; i < pvobsArray.length; i++) {
			String[] pvob1Array = pvobsArray[i].split("\\s+");
			if (pvob1StringBuffer.length() > 0) {
				pvob1StringBuffer.append(LINE_SEPARATOR);
			}
			pvob1StringBuffer.append(pvob1Array[1]);
		}
		String pvob1String = pvob1StringBuffer.toString();
		return pvob1String.split(LINE_SEPARATOR);
	}

	public static List<String> getStreamComponents(final String streamSelector, final String currentDir, final String... dynamicViewTag) {
		String viewTag = null;
		if (dynamicViewTag != null && dynamicViewTag.length > 0) {
			viewTag = dynamicViewTag[0];
		}
		if (viewTag != null) {
			List<String> parameterList = ListUtils.getItemsAsList(ClearCaseUtils.getClearToolPath(), "startview", viewTag);
			SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(parameterList, new File(currentDir));
		}
		List<String> parameterList = ListUtils.getItemsAsList(ClearCaseUtils.getClearToolPath(), "lsstream", "-fmt", "\"%[components]CXp\"",
				streamSelector);
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(parameterList, new File(currentDir));
		String[] components = systemCommandExecutorResponse.getStdout().toString().split(OzConstants.COMMA);
		ArrayUtils.printArray(components, OzConstants.LINE_FEED, "components list", Level.FINEST);
		return Arrays.asList(components);
	}

	public static final String[] getStreamFoundationBaselines(final String stream) {
		ClearToolCommand clearToolCommand = new ClearToolCommand();
		ClearToolResults clearToolResults;
		String[] clearToolparams = new String[] { "", "lsstream", "-fmt", "%[found_bls]p\n", "stream:" + stream };
		clearToolResults = clearToolCommand.runClearToolCommand(clearToolparams, false);
		String[] streamFoundationBaselines = clearToolResults.getStdOut().split(" ");
		return streamFoundationBaselines;
	}

	public static List<String> getStreamLatestBls(final String streamSelector, final String currentDir, final String... dynamicViewTag) {
		String viewTag = null;
		if (dynamicViewTag != null && dynamicViewTag.length > 0) {
			viewTag = dynamicViewTag[0];
		}
		if (viewTag != null) {
			List<String> parameterList = ListUtils.getItemsAsList(ClearCaseUtils.getClearToolPath(), "startview", viewTag);
			SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(parameterList, new File(currentDir));
		}
		List<String> parameterList = ListUtils.getItemsAsList(ClearCaseUtils.getClearToolPath(), "lsstream", "-fmt", "\"%[latest_bls]p\"",
				streamSelector);
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(parameterList, new File(currentDir));
		String[] latestsBaseLines = systemCommandExecutorResponse.getStdout().toString().split(OzConstants.BLANK);
		ArrayUtils.printArray(latestsBaseLines, OzConstants.LINE_FEED, "components list", Level.FINEST);
		return Arrays.asList(latestsBaseLines);
	}

	/*
	 * Get stream PVOB
	 */
	public static final String getStreamPvob(final String myStream) {
		int pvobCounter = 0;
		int pvobIndex = 1;
		String[] pVobs = ClearCaseUtils.getPvobs();
		String myPvob = null;
		String clearToolPath = ClearCaseUtils.getClearToolPath();
		for (int i = 0; i < pVobs.length; i++) {
			String streamFullName = myStream + "@" + pVobs[i];
			logger.fine("streamFullName: " + streamFullName);
			List<String> clearToolparams = Arrays.asList(clearToolPath, "lsstream", streamFullName);
			SystemCommandExecutorResponse systemCommandExecutorResponse = ClearCaseUtils.runClearToolCommand(clearToolparams);
			if (systemCommandExecutorResponse.getReturnCode() == 0 && systemCommandExecutorResponse.getStdout().length() > 0
					&& systemCommandExecutorResponse.getStderr().length() == 0) {
				pvobCounter++;
				pvobIndex = i;
			}
			logger.fine(systemCommandExecutorResponse.getExecutorResponse());
		}
		ArrayUtils.printArray(pVobs, OzConstants.LINE_FEED, "pvobs:\n", Level.FINEST);
		switch (pvobCounter) {
		case 1:
			myPvob = pVobs[pvobIndex];
			break;
		case 0:
			logger.warning("Error: Pvob not found for stream " + myStream + "! ");
			break;
		default:
			logger.warning("Error: Stream name is not unique for stream " + myStream + "! ");
			break;
		}
		logger.info("stream: " + myStream + " pvob: " + myPvob);
		return myPvob;
	}

	public static final String[] getStreamViewTags(final String stream) {
		/*
		 * get stream view tags
		 */
		String[] clearToolparams = new String[] { "", "lsstream", "-fmt", "%[views]p", stream };
		ClearToolCommand clearToolCommand = new ClearToolCommand();
		String viewTags = clearToolCommand.runClearToolCommand(clearToolparams, false).getStdOut().trim();
		if (viewTags.length() == 0) {
			return null;
		} else {
			return viewTags.split(" ");
		}

	}

	public static void getUserUsageStats(final String[] clearLicenseArray, final Map<String, Integer> clearCaseUserUsageStatsMap,
			final Map<String, String> clearCaseAlreadyCountedUsageMap) {
		final String activeUsersString = "ACTIVE users:";
		int activeUsersLineindex = OzConstants.STRING_NOT_FOUND;
		for (int i = 0; i < clearLicenseArray.length; i++) {
			int activeUsersStringindex = clearLicenseArray[i].indexOf(activeUsersString);
			if (activeUsersStringindex > OzConstants.STRING_NOT_FOUND) {
				activeUsersLineindex = i;
			}
		}
		if (activeUsersLineindex > 0) {
			for (int i = activeUsersLineindex + 2; i < clearLicenseArray.length; i++) {
				if (clearLicenseArray[i].trim().length() == 0) {
					break;
				}
				String[] lineArray = clearLicenseArray[i].split(RegexpUtils.REGEXP_WHITE_SPACE);
				String username = lineArray[1];
				String licenseExpirationTimeStamp = lineArray[lineArray.length - 1].substring(0, 5);
				Integer currentCount = clearCaseUserUsageStatsMap.get(username);
				if (currentCount == null) {
					clearCaseUserUsageStatsMap.put(username, new Integer(1));
					clearCaseAlreadyCountedUsageMap.put(username, licenseExpirationTimeStamp);
				} else {
					String previousTimeStamp = clearCaseAlreadyCountedUsageMap.get(username);
					if (!licenseExpirationTimeStamp.equals(previousTimeStamp)) {
						int currentCountInt = clearCaseUserUsageStatsMap.get(username);
						clearCaseUserUsageStatsMap.put(username, currentCountInt + 1);
						clearCaseAlreadyCountedUsageMap.put(username, licenseExpirationTimeStamp);
					}
				}
				ArrayUtils.printArray(lineArray, Level.FINEST);
			}
		}
	}

	private static Properties getViewProperties(final SystemCommandExecutorResponse scer, final String viewIdentifier) {
		Properties viewProperties = null;
		if (scer.getReturnCode() == 0 && scer.getStdout() != null && scer.getStdout().length() > 0) {
			String stdout = scer.getStdout().toString();
			String[] stdoutArray = stdout.split(OzConstants.LINE_FEED);
			logger.finest(String.valueOf(stdoutArray.length));
			viewProperties = new Properties();
			for (String line : stdoutArray) {
				if (line.trim().length() > 0) {
					int firstColonPtr = line.indexOf(OzConstants.COLON);
					logger.finest("firstColonPtr: " + String.valueOf(firstColonPtr) + "\nline: " + line);
					String key = line.substring(0, firstColonPtr);
					String value = line.substring(firstColonPtr + 1);
					viewProperties.put(key.trim(), value.trim());
				}
			}
			logger.finest(PropertiesUtils.getAsDelimitedString(viewProperties));
		} else {
			logger.warning(viewIdentifier + " is not a clearCase view ...");
		}
		return viewProperties;
	}

	public static Properties getViewPropertiesByViewRoot(final String viewPath) {
		List<String> params = Arrays.asList(ClearCaseUtils.getClearToolPath(), ClearToolSubCommands.LSVIEW, "-l", "-cview");
		SystemCommandExecutorResponse scer = ClearCaseUtils.runClearToolCommand(params, new File(viewPath));
		return getViewProperties(scer, viewPath);
	}

	public static Properties getViewPropertiesByViewTag(final String viewTag) {
		List<String> params = Arrays.asList(ClearCaseUtils.getClearToolPath(), ClearToolSubCommands.LSVIEW, "-l", viewTag);
		SystemCommandExecutorResponse scer = ClearCaseUtils.runClearToolCommand(params);
		return getViewProperties(scer, viewTag);
	}

	public static String getViewTag(final String viewPath) {
		Properties viewProperties = getViewPropertiesByViewRoot(viewPath);
		String viewTag = null;
		if (viewProperties != null) {
			viewTag = (String) viewProperties.get(TAG);
		}
		logger.info("viewTag: " + viewTag);
		return viewTag;
	}

	public static VIEWTYPE_ENUM getViewType(final Properties viewProperties) {
		VIEWTYPE_ENUM outcome = VIEWTYPE_ENUM.NOT_A_VIEW;
		String attributes = null;
		if (viewProperties != null) {
			attributes = (String) viewProperties.get(VIEW_ATTRIBUTES);
			if (attributes.indexOf(SNAPSHOT) > OzConstants.STRING_NOT_FOUND) {
				outcome = VIEWTYPE_ENUM.SNAPSHOT;
			} else {
				outcome = VIEWTYPE_ENUM.DYNAMIC;
			}
		}
		logger.info("\nattributes: " + attributes + "\noutcome: " + outcome.toString());
		return outcome;
	}

	public static VIEWTYPE_ENUM getViewTypeByViewRootFolder(final String viewPath) {
		VIEWTYPE_ENUM outcome = VIEWTYPE_ENUM.NOT_A_VIEW;
		Properties viewProperties = getViewPropertiesByViewRoot(viewPath);
		return getViewType(viewProperties);
	}

	public static VIEWTYPE_ENUM getViewTypeByViewTag(final String viewPath) {
		Properties viewProperties = getViewPropertiesByViewTag(viewPath);
		return getViewType(viewProperties);
	}

	public static boolean isActivityExists(final String viewTag, final String activityName) {
		String[] allActivitiesInStream = ClearCaseUtils.getAllActivitiesInStream(viewTag);
		boolean activityExists = false;
		for (String activityName1 : allActivitiesInStream) {
			logger.finest(activityName1);
			if (activityName1.equals(activityName)) {
				activityExists = true;
				break;
			}
		}
		return activityExists;
	}

	public static final boolean isStreamReadOnly(final String stream) {
		String[] clearToolparams = new String[] { getClearToolPath(), "lsstream", "-fmt", "%[read_only]p", stream };
		ClearToolCommand clearToolCommand = new ClearToolCommand();
		ClearToolResults clearToolResults = clearToolCommand.runClearToolCommand(clearToolparams, true);
		return (clearToolResults.getReturnCode() == 0 && clearToolResults.getStdOut().equalsIgnoreCase("read only")
				&& clearToolResults.getStdErr().length() == 0);
	}

	public static boolean isViewExists(final String viewTag) {
		List<String> params = Arrays.asList(ClearCaseUtils.getClearToolPath(), ClearToolSubCommands.LSVIEW, "-s", viewTag);
		SystemCommandExecutorResponse scer = ClearCaseUtils.runClearToolCommand(params);
		boolean viewExists = viewTag.equals(scer.getStdout().trim());
		if (viewExists) {
			logger.info("view " + viewTag + " exists !");
		} else {
			logger.info("view " + viewTag + " does not exists !");
		}
		return viewExists;
	}

	public static SystemCommandExecutorResponse mkactivity(final List<String> clearToolParametersList, final File... directory) {
		/*
		 * mkact/ivity [ –c/omment comment | –cfi/le pname | –cq/uery | –cqe/ach | –nc/omment ] [ –hea/dline headline ] [ –in stream-selector ] [ –nse/t ] [ –f/orce ] [
		 * activity-selector ...]
		 */
		clearToolParametersList.add(0, ClearToolSubCommands.MKACTIVITY);
		clearToolParametersList.add(0, getClearToolPath());
		ListUtils.getAsDelimitedString(clearToolParametersList, Level.INFO);
		SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(clearToolParametersList, directory);
		logger.info(scer.getExecutorResponse());
		return scer;
	}

	public static SystemCommandExecutorResponse mkactivity(final String[] clearToolParameters, final File... directory) {
		List<String> clearToolParametersList = ArrayUtils.getAsList(clearToolParameters);
		return mkactivity(clearToolParametersList, directory);
	}

	public static Outcome mkview(final String viewTag, final String stream, final String stgloc, final String... snapShotPnames) {
		List<String> params = new ArrayList<String>(
				Arrays.asList(ClearCaseUtils.getClearToolPath(), ClearToolSubCommands.MKVIEW, "-tag", viewTag, "-stream", stream, "-stgloc", stgloc));
		String snapShotPname = OzConstants.EMPTY_STRING;
		if (snapShotPnames.length > 0) {
			snapShotPname = snapShotPnames[0];
			params.add(snapShotPname);
			params.add(2, "-snapshot");
		}
		SystemCommandExecutorResponse scer = ClearCaseUtils.runClearToolCommand(params);
		logger.info(scer.getExecutorResponse());
		Outcome outcome = Outcome.FAILURE;
		String logMessage = "mkview command with parameters viewTag: " + viewTag + " stream: " + stream + " stgloc: " + stgloc;
		if (snapShotPname.length() > 0) {
			logMessage = logMessage + " snapShotPname: " + snapShotPname;
		}
		if (scer.getReturnCode() == 0) {
			outcome = Outcome.SUCCESS;
			logger.info(logMessage + " has completed successfully !");
		} else {
			logger.info(logMessage + " has failed !");
		}
		return outcome;
	}

	public static final boolean removeChangeSet(final String activity, final String logFilePath, final String currentView) {
		JulUtils.addFileHandler(logFilePath);
		logger.info("Starting to remove changeset for activity " + activity);
		ClearToolResults clearToolResults = null;
		ClearToolCommand clearToolCommand = new ClearToolCommand();
		boolean returnCode = true;
		String[] clearToolparams = new String[] { getClearToolPath(), "lsactivity", "-fmt", "%[versions]Cp", "activity:" + activity };
		File currentViewFile = new File(currentView);
		List<String> parametersList = new ArrayList<String>(Arrays.asList(clearToolparams));
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(parametersList, currentViewFile);
		if (systemCommandExecutorResponse.getReturnCode() == 0) {
			String[] changeSetArray = getChangeSet(activity, currentViewFile);
			int changeSetArrayLength = 0;
			if (changeSetArray != null && changeSetArray.length > 0) {
				changeSetArrayLength = changeSetArray.length;
				logger.info("Change set size: " + String.valueOf(changeSetArrayLength));
				Arrays.sort(changeSetArray, String.CASE_INSENSITIVE_ORDER);
				clearToolparams = new String[] { "", "rmver", "-force", "-xbranch", "-xlabel", "-xattr", "-xhlink", "-nc", "" };

				for (int i = changeSetArrayLength - 1; i >= 0; i--) {
					logger.finer("versionExtendedPath: " + changeSetArray[i]);
					int versionSuffixIndex = changeSetArray[i].indexOf("@@");
					if (versionSuffixIndex > 0 && changeSetArray[i].indexOf("lost+found") == -1) {
						String versionFilePath = changeSetArray[i].substring(0, versionSuffixIndex);
						logger.finest("versionFilePath: " + versionFilePath);
						File versionFile = new File(versionFilePath);
						if (versionFile.exists() && versionFile.isFile()) {
							clearToolparams[clearToolparams.length - 1] = changeSetArray[i];
							ArrayUtils.printArray(clearToolparams);
							clearToolResults = clearToolCommand.runClearToolCommand(clearToolparams, false);
							if (clearToolResults.getReturnCode() != 0) {
								returnCode = false;
								logger.warning("cleartool return code: " + clearToolResults.getReturnCode());
							}
							if (clearToolResults.getStdErr().length() > 0) {
								logger.warning(clearToolResults.getStdErr());

							}
						}
					}
				}
				for (int i = changeSetArrayLength - 1; i >= 0; i--) {
					int versionSuffixIndex = changeSetArray[i].indexOf("@@");
					if (versionSuffixIndex > 0 && changeSetArray[i].indexOf("lost+found") == -1) {
						String versionFilePath = changeSetArray[i].substring(0, versionSuffixIndex);
						logger.finest("versionFilePath: " + versionFilePath);
						File versionFile = new File(versionFilePath);
						if (versionFile.exists() && !versionFile.isFile()) {
							clearToolparams[clearToolparams.length - 1] = changeSetArray[i];
							ArrayUtils.printArray(clearToolparams);
							clearToolResults = clearToolCommand.runClearToolCommand(clearToolparams, false);
							if (clearToolResults.getReturnCode() != 0) {
								returnCode = false;
								logger.warning("cleartool return code: " + clearToolResults.getReturnCode());
							}
							if (clearToolResults.getStdErr().length() > 0) {
								logger.warning(clearToolResults.getStdErr());

							}
						}
					}
				}
			} else {
				logger.warning("Change set for activity " + activity + " is empty!");
			}
			logger.info("removeChangeSet set for activity " + activity + " completed. return code: " + String.valueOf(returnCode));
		} else {
			logger.warning(clearToolResults.getStdErr());
			returnCode = false;
		}
		return returnCode;
	}

	public static SystemCommandExecutorResponse runClearToolCommand(final List<String> clearToolParametersList, final File... directory) {
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(clearToolParametersList, directory);
		logger.finest(systemCommandExecutorResponse.getExecutorResponse());
		return systemCommandExecutorResponse;
	}

	public static SystemCommandExecutorResponse runClearToolCommand(final String[] clearToolParametersArray, final File... directory) {
		List<String> clearToolParametersList = new ArrayList<String>(Arrays.asList(clearToolParametersArray));
		return runClearToolCommand(clearToolParametersList, directory);
	}

	public static SystemCommandExecutorResponse setactivity(final List<String> clearToolParametersList, final File... files) {
		/*
		 * setact/ivity [ –c/omment comment | –cfi/le pname | –cq/uery | –nc/omment ] [ –vie/w view-tag ] { –none | activity-selector }
		 */
		clearToolParametersList.add(0, ClearToolSubCommands.SETACTIVITY);
		clearToolParametersList.add(0, getClearToolPath());
		ListUtils.getAsDelimitedString(clearToolParametersList, OzConstants.BLANK, Level.INFO);
		SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(clearToolParametersList, files);
		logger.info(scer.getExecutorResponse());
		return scer;
	}

	public static SystemCommandExecutorResponse setactivity(final String[] clearToolParameters, final File... files) {
		List<String> clearToolParametersList = ArrayUtils.getAsList(clearToolParameters);
		return setactivity(clearToolParametersList, files);
	}

	public static Outcome setactivityInView(final String viewTag, final String activityName) {
		String currentActivity = getActivitySetInCurrentView(viewTag);
		if (currentActivity != null && currentActivity.length() > 0 && !currentActivity.equalsIgnoreCase(activityName)) {
			clearActivityFromView(viewTag);
		}
		return Outcome.SUCCESS;
	}

	public static void setClearCaseEnvironmentVariables() {
		final String prefixRegExp = "^ClearCase";
		final String clearCaseUsers = "ClearCase_Users";
		final String clearAdmins = "ClearCaseAdmins";

		String[] groups = ActiveDirectoryUtils.getMyGroups();
		String[] ccGroups = ArrayUtils.selectArrayRowsByRegExpression(groups, prefixRegExp, true);
		// String[] ccGroups = { "cc1", "cc2", "cc3" };
		if (ccGroups.length > 0) {
			ArrayUtils.printArray(ccGroups, OzConstants.LINE_FEED, "clearCase groups:\n", Level.FINEST);
			String userDomain = EnvironmentUtils.getEnvironmentVariable("USERDOMAIN");
			String clearCasePrimaryGroup = StringUtils.concat(userDomain, OzConstants.BACK_SLASH, clearCaseUsers);
			if (ArrayUtils.isObjectInArray(ccGroups, clearAdmins)) {
				clearCasePrimaryGroup = StringUtils.concat(userDomain, OzConstants.BACK_SLASH, clearAdmins);
			}
			EnvironmentUtils.setWindowsEnvironmentVariable(CLEARCASE_PRIMARY_GROUP, clearCasePrimaryGroup);
			StringBuilder sb = new StringBuilder(StringUtils.concat(userDomain, OzConstants.BACK_SLASH, clearCaseUsers));

			for (String ccGroup1 : ccGroups) {
				sb.append(StringUtils.concat(OzConstants.SEMICOLON, userDomain, OzConstants.BACK_SLASH, ccGroup1));
			}
			String clearCaseGoups = sb.toString();
			EnvironmentUtils.setWindowsEnvironmentVariable(CLEARCASE_GROUPS, clearCaseGoups);
		} else {
			logger.warning("user does not belong to any ClearCase group!");
		}
	}

	private static String setDefaultViewTag() {
		String defaultViewTag = null;
		String userName = SystemPropertiesUtils.getUserName().toLowerCase();
		String streamName = EnvironmentUtils.getActualEnvVarValue(null, STREAM_NAME);
		if (streamName == null) {
			logger.warning(STREAM_NAME + " environment variable not set !");
		} else {
			defaultViewTag = userName + OzConstants.UNDERSCORE + streamName;
			logger.info("defaultViewTag: ".concat(defaultViewTag));
		}
		return defaultViewTag;
	}

	public static void setView(final String viewTag) {
		String[] parametersArray1 = { getClearToolPath(), ClearToolSubCommands.SET_VIEW, viewTag };
		SystemCommandExecutorResponse systemCommandExecutorResponse1 = SystemCommandExecutorRunner.run(parametersArray1);
		logger.info(systemCommandExecutorResponse1.getExecutorResponse());
	}

	public static SystemCommandExecutorResponse startView(final String viewTag, final String ccViewRootFolderPath, final Boolean... waitParam) {
		Boolean wait = VarArgsUtils.getMyArg(true, waitParam);
		SystemCommandExecutorResponse scer = null;
		if (viewTag != null && viewTag.length() > 0) {
			String[] parametersArray1 = { getClearToolPath(), ClearToolSubCommands.START_VIEW, viewTag };
			scer = SystemCommandExecutorRunner.run(parametersArray1);
			logger.info("startView " + viewTag + " outcome: " + scer.getExecutorResponse());
			if (scer.getReturnCode() == OzConstants.EXIT_STATUS_OK && wait && ccViewRootFolderPath != null) {
				File ccViewRootFolder = new File(ccViewRootFolderPath);
				final int startViewLoopControl = OzConstants.INT_25;
				final long sleepInterval = 1000L;
				for (int i = 0; i < startViewLoopControl; i++) {
					if (ccViewRootFolder.isDirectory()) {
						logger.info(StringUtils.concat("folder ", ccViewRootFolderPath, " is availabe now."));
						break;
					} else {
						logger.info("waiting for targer folder " + ccViewRootFolderPath + " to become availabe. Iteration: " + String.valueOf(i));
						ThreadUtils.sleep(sleepInterval, Level.INFO);
					}
				}
			}
		} else {
			logger.warning("viewTag parameter is null or zero length");
		}
		return scer;
	}

	public static SystemCommandExecutorResponse updateCurrentView(final File currentView) {
		String[] clearToolParameters = { getClearToolPath(), "update", "-overwrite", "-ptime" };
		List<String> parametersList = new ArrayList<String>(Arrays.asList(clearToolParameters));
		SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner.run(parametersList, currentView);
		logger.info(systemCommandExecutorResponse.getExecutorResponse());
		return systemCommandExecutorResponse;
	}

	public static SystemCommandExecutorResponse updateCurrentView(final String currentViewPath) {
		return updateCurrentView(new File(currentViewPath));
	}
}
