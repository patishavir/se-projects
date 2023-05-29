package oz.clearcase.view.mkview;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import oz.clearcase.infra.ClearCaseUtils;
import oz.clearcase.infra.ClearToolCommand;
import oz.clearcase.infra.ClearToolResults;
import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.system.env.EnvironmentUtils;

public class MkviewHandler {
	private enum FolderExistsAction {
		Rename, Delete
	}

	private static Map<String, String> environmentMap = System.getenv();
	private static String addTagSuffixPermittedUsersFilePath = null;
	private static String targetRootFolder = "C:\\CCViews";
	private static String scriptFolder = "c:\\temp\\ccScripts";
	private static String stgloc = EnvironmentUtils.getActualEnvVarValue("Views", "stgloc");
	private static String targetFolderFullPath;
	private static File targetFolderFile = null;
	private static boolean isDynamicView = false;
	private static boolean isReadOnlyView = false;
	private static boolean addTagSuffix = false;
	private static boolean permittedUser = false;
	private static String userName = System.getenv("USERNAME");
	private static String computerName = System.getenv("COMPUTERNAME");
	private static String[] loadRules = null;
	private static String myStream = null;
	private static String myStreamSelector = null;
	private static String mypVob = null;
	private static String myViewTag = null;
	private static String viewTagSuffix = null;
	private static String myViewStorage = null;
	private static String viewFolderName = null;
	private static ClearToolCommand ctc = null;
	private static String[] clearToolparams = null;
	private static ClearToolResults ctResults = null;
	private static boolean viewExists;
	private static boolean isViewOnCurrentMachine = false;
	private static boolean addRDCommand = false;
	private static boolean acceptCleartoolFailure = true;
	private static boolean dontAcceptCleartoolFailure = false;
	private static final String MKVIEW_COMMAND = "mkview";
	private static final String FILE_SEPARATOR = File.separator;
	private static final String LINE_SEPARATOR = SystemUtils.LINE_SEPARATOR;
	private static Logger logger = JulUtils.getLogger();

	/*
	 * 
	 */
	private static void buildMkviewBat() {
		clearToolparams = new String[] { ClearCaseUtils.getClearToolPath(), " " + MKVIEW_COMMAND, " -snapshot",
				" -tag ", myViewTag, " -ptime", " -stream ", myStreamSelector, " -stgloc ", stgloc, " ",
				targetFolderFullPath };
		StringBuffer mkViewBatStringBuffer = new StringBuffer();
		mkViewBatStringBuffer.append("@echo off" + LINE_SEPARATOR);
		if (addRDCommand) {
			mkViewBatStringBuffer.append("RD /S /Q " + targetFolderFullPath + LINE_SEPARATOR);
		}
		mkViewBatStringBuffer.append("set startTime=%TIME%" + LINE_SEPARATOR);
		mkViewBatStringBuffer.append(StringUtils.concat(clearToolparams));
		mkViewBatStringBuffer.append(LINE_SEPARATOR);
		mkViewBatStringBuffer.append("echo ERRORLEVEL=%ERRORLEVEL%" + LINE_SEPARATOR);
		for (int i = 0; i < loadRules.length; i++) {
			logger.finest(loadRules[i]);
			String suppressLogGeneration = "-log NUL ";
			if (loadRules[i].length() > 0) {
				String addLoadRuleCommand = StringUtils.concat(ClearCaseUtils.getClearToolPath(),
						" update -add_loadrules -ptime ", suppressLogGeneration, targetFolderFullPath, loadRules[i],
						LINE_SEPARATOR);
				mkViewBatStringBuffer.append(addLoadRuleCommand);
			}
		}
		mkViewBatStringBuffer.append(LINE_SEPARATOR + "echo ERRORLEVEL=%ERRORLEVEL%" + LINE_SEPARATOR);
		mkViewBatStringBuffer.append("echo      Start time=%startTime%" + LINE_SEPARATOR);
		mkViewBatStringBuffer.append("echo Completion Time=%Time%" + LINE_SEPARATOR);
		String mkViewBatString = mkViewBatStringBuffer.toString();
		String batFilePath = scriptFolder + FILE_SEPARATOR + myStream + ".bat";
		File batFileRootFile = new File(scriptFolder);
		if (!batFileRootFile.exists()) {
			batFileRootFile.mkdirs();
		}
		try {
			logger.finest("\nbatFilePath: " + batFilePath + "\nContens:\n" + mkViewBatString);
			FileUtils.writeFile(new File(batFilePath), mkViewBatString);
			logger.finest("Bat file has been successfully written to " + batFilePath);
		} catch (Exception ex) {
			terminate(-1, ex.getMessage());
		}
	}

	/*
	 * 
	 */
	public static void doMkview(final String[] args) {
		ClearCaseUtils.setClearCaseEnvironmentVariables();
		processParameters(args);
		ctc = new ClearToolCommand(null, false);
		mypVob = ClearCaseUtils.getStreamPvob(myStream);
		myStreamSelector = myStream + "@" + mypVob;
		logger.info("Stream found: " + myStreamSelector);
		processLoadRules();
		File targetRootFolderFile = new File(targetRootFolder);
		if (!targetRootFolderFile.exists()) {
			targetRootFolderFile.mkdir();
		}
		myViewTag = userName.toLowerCase() + "_" + myStream;
		if (permittedUser && addTagSuffix) {
			myViewTag = myViewTag + viewTagSuffix;
		}
		clearToolparams = new String[] { ClearCaseUtils.getClearToolPath(), "lsview", myViewTag };
		ctResults = ctc.runClearToolCommand(clearToolparams, acceptCleartoolFailure);
		viewExists = (ctResults.getReturnCode() == 0);
		if (viewExists) {
			myViewStorage = ctResults.getStdOut().split("\\s+")[2];
		}
		targetFolderFullPath = targetRootFolder + FILE_SEPARATOR + viewFolderName;
		targetFolderFile = new File(targetFolderFullPath);
		boolean folderExists = targetFolderFile.exists();
		if (folderExists && isViewOnCurrentMachine()) {
			isViewOnCurrentMachine = true;
		}
		if (viewExists) {
			processViewExists();
		}
		if (targetFolderFile.exists()) {
			processFolderExists();
		}
		buildMkviewBat();
	}

	private static Object getFolderExistsAction() {
		Object[] possibleActions = { "Rename", "Delete", "Abort" };
		StringBuffer folderExistsStringBuffer = new StringBuffer();
		folderExistsStringBuffer.append(
				"<html><font color=\"navy\" size=\"4\">Folder " + targetFolderFullPath + " exists on this machine.<br>");
		folderExistsStringBuffer.append("Choose from the drop down list box what you wants to do about it.<br>");
		folderExistsStringBuffer
				.append("Options are:<UL><LI></font><font size=\"4\" color=\"green\">Rename the folder.<LI></font>");
		folderExistsStringBuffer.append(
				"<font size=\"4\" color=\"red\">Delete the folder.<LI></font><font size=\"4\" color=\"black\">Abort make view operation.");
		folderExistsStringBuffer.append("</UL></html>");
		Object selectedValue = JOptionPane.showInputDialog(null, folderExistsStringBuffer.toString(),
				"View folder options", JOptionPane.INFORMATION_MESSAGE, null, possibleActions, possibleActions[0]);
		return selectedValue;
	}

	/*
	 * ct lscomp -fmt "%[root_dir]p" HRLevC@\HRPvob01 is view on current machine
	 */
	private static final boolean isViewOnCurrentMachine() {
		boolean isViewOnCurrentMachine = false;
		File viewDotDatFile = new File(targetFolderFullPath + FILE_SEPARATOR + "view.dat");
		boolean viewDotDatFileExists = viewDotDatFile.exists();
		if (viewDotDatFileExists) {
			String viewDotDatString = "view.dat file could not be read!";
			if (viewDotDatFileExists) {
				viewDotDatString = FileUtils.readTextFile(viewDotDatFile);
			}
			logger.finest(viewDotDatString);
			String view_uuidString = "view_uuid:";
			if (viewDotDatString.indexOf(view_uuidString) > 0) {
				String[] view_uuidStringArray = viewDotDatString.split(view_uuidString);
				String view_uuid = view_uuidStringArray[1];
				logger.finest(view_uuid);
				clearToolparams = new String[] { ClearCaseUtils.getClearToolPath(), "lsview", "-uuid", view_uuid };
				ctResults = ctc.runClearToolCommand(clearToolparams, acceptCleartoolFailure);
				if (ctResults.getReturnCode() == 0) {
					String[] lsviewStringArray = ctResults.getStdOut().split("\\s+");
					String viewTagOnCurrentMachine = lsviewStringArray[1];
					isViewOnCurrentMachine = viewTagOnCurrentMachine.equals(myViewTag);
				}
			}
		}
		return isViewOnCurrentMachine;
	}

	/*
	 * processFolderExists
	 */
	private static void processFolderExists() {
		Object selectedValue = getFolderExistsAction();
		if (selectedValue == null) {
			terminate(-1, "You have choosen to abort make view operation");
		}
		FolderExistsAction folderExistsAction = FolderExistsAction.valueOf(selectedValue.toString());
		switch (folderExistsAction) {
		case Rename:
			String currentDate = DateTimeUtils.formatDate("yyyy-MM-dd_hh-mm");
			String renamedFolderPath = targetFolderFullPath + "." + currentDate;
			boolean renameResult = FileUtils.renameFolder(targetFolderFullPath, renamedFolderPath);
			if (renameResult) {
				logger.info("Folder " + targetFolderFullPath + " has ben renamed to " + renamedFolderPath);
			} else {
				terminate(-1, "Rename failed for " + targetFolderFullPath + ". Processing aborted !");
			}
			break;
		case Delete:
			addRDCommand = true;
		}
	}

	/*
	 * Validate load rules
	 */
	private static void processLoadRules() {
		for (int i = 0; i < loadRules.length; i++) {
			String[] loadRule1Array = loadRules[i].split("\\" + File.separator);
			String component = StringUtils.concat(loadRule1Array[0], OzConstants.AT_SIGN, mypVob);
			String rootDirectory = ClearCaseUtils.getComponentRootDirectory(component);
			logger.finest(StringUtils.concat("rootDirectory: ", rootDirectory));
			if (rootDirectory != null) {
				logger.finest(rootDirectory);
				loadRule1Array[0] = rootDirectory;
				if (rootDirectory.length() == 0) {
					logger.warning(StringUtils.concat(component, " has no root directory!"));
					loadRules[i] = "";
				} else {
					loadRules[i] = StringUtils.join(loadRule1Array, File.separator);
				}
			}
		}
	}
	/*
	 * Process parameters
	 */
	private static void processParameters(final String[] args) {
		myStream = args[0];
		logger.info("Stream: " + myStream);
		viewFolderName = myStream;
		if (args.length > 1) {
			loadRules = ArrayUtils.selectArrayRowsByStartingRow(args, 1);
		} else {
			String loadRulesKey = "loadRules";
			String loadRulesString = environmentMap.get(loadRulesKey);
			if (loadRulesString != null) {
				logger.info(loadRulesString);
				loadRules = loadRulesString.split(OzConstants.COMMA);
			} else {
				loadRules = ClearCaseUtils.getComponentsNamesInStream(myStream);
			}
		}
		if (environmentMap.containsKey("targetRootFolder")) {
			targetRootFolder = environmentMap.get("targetRootFolder").trim();
		}
		if (environmentMap.containsKey("mkDynamicView")) {
			isDynamicView = environmentMap.get("mkDynamicView").trim().equalsIgnoreCase("yes");
		}
		if (environmentMap.containsKey("mkReadOnlyView")) {
			isReadOnlyView = environmentMap.get("mkReadOnlyView").trim().equalsIgnoreCase("yes");
		}
		if (environmentMap.containsKey("addTagSuffix")) {
			addTagSuffix = environmentMap.get("addTagSuffix").trim().equalsIgnoreCase("yes");
		}
		if (environmentMap.containsKey("stgloc")) {
			stgloc = environmentMap.get("stgloc").trim();
		}
		if (environmentMap.containsKey("javaLoggingLevel")) {
			String javaLoggingLevel = environmentMap.get("javaLoggingLevel").trim();
			JulUtils.setLoggingLevel(javaLoggingLevel);
		}
		if (environmentMap.containsKey("scriptFolder")) {
			scriptFolder = environmentMap.get("scriptFolder").trim();
		}
		if (environmentMap.containsKey("addTagSuffixPermittedUsersFilePath")) {
			addTagSuffixPermittedUsersFilePath = environmentMap.get("addTagSuffixPermittedUsersFilePath").trim();
		}
		if (environmentMap.containsKey("viewFolderName")) {
			viewFolderName = environmentMap.get("viewFolderName").trim();
		}
		if (addTagSuffixPermittedUsersFilePath != null) {
			File permittedUsersFile = new File(addTagSuffixPermittedUsersFilePath);
			if (permittedUsersFile.exists()) {
				String permittedUsersString = FileUtils.readTextFile(permittedUsersFile);
				permittedUsersString = permittedUsersString.toLowerCase();
				permittedUser = permittedUsersString.indexOf(userName) > -1;
			}
		}
		viewTagSuffix = "_" + computerName.substring(computerName.length() - 4);
	}

	/*
	 * Process view exists
	 */
	private static void processViewExists() {
		Object[] possibleActions = { "Remove", "Abort" };
		if (permittedUser) {
			Object[] possibleActions4PermittedUsers = { "Remove", "addTagSuffix", "Abort" };
			possibleActions = possibleActions4PermittedUsers;
		}
		StringBuffer viewExistsStringBuffer = new StringBuffer();
		viewExistsStringBuffer.append("<html><font color=\"navy\" size=\"4\">View " + myViewTag + " already exists ");
		if (isViewOnCurrentMachine) {
			viewExistsStringBuffer.append("on this machine.");
		}
		viewExistsStringBuffer.append(".<br>Choose from the drop down list box what you wants to do about it.");
		viewExistsStringBuffer.append("<br>Options are:<UL><LI></font><font size=\"4\" color=\"red\">Remove view.");
		if (permittedUser) {
			viewExistsStringBuffer.append("<LI></font><font size=\"4\" color=\"green\">Add suffix to user tag.");
		}
		viewExistsStringBuffer.append("<LI></font><font size=\"4\" color=\"black\">Abort make view operation.");
		viewExistsStringBuffer.append("</UL></html>");
		Object selectedValue = JOptionPane.showInputDialog(null, viewExistsStringBuffer.toString(),
				"View folder options", JOptionPane.INFORMATION_MESSAGE, null, possibleActions, possibleActions[0]);
		if (selectedValue == null || selectedValue.toString().equalsIgnoreCase("Abort")) {
			terminate(-1, "You have choosen to abort make view operation");
		} else if (selectedValue.toString().equalsIgnoreCase("Remove")) {
			logger.finest("The user has chosen to remove the view");
			String rmviewPath = myViewStorage;
			if (isViewOnCurrentMachine) {
				rmviewPath = targetFolderFullPath;
			} else {
				rmviewPath = myViewStorage;
				if (rmviewPath.startsWith(computerName + ":")) {
					rmviewPath = rmviewPath.substring(computerName.length() + 1);
				}
			}
			clearToolparams = new String[] { ClearCaseUtils.getClearToolPath(), "rmview", "-force", rmviewPath };
			ctResults = ctc.runClearToolCommand(clearToolparams, dontAcceptCleartoolFailure);
		} else if (selectedValue.toString().equalsIgnoreCase("addTagSuffix")) {
			myViewTag = myViewTag + viewTagSuffix;
		}
	}

	private static void terminate(final int exitCode, final String exitMessage) {
		logger.warning(exitMessage);
		System.exit(exitCode);
	}

	private MkviewHandler() {

	}
}
