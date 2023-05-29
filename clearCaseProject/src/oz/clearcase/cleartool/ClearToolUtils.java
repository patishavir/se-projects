package oz.clearcase.cleartool;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import oz.clearcase.infra.ClearCaseUtils;
import oz.clearcase.infra.ClearToolSubCommands;
import oz.clearcase.view.ViewObject;
import oz.clearcase.vob.VobObject;
import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.string.StringUtils;

public class ClearToolUtils {
	private static final String TARGET_CC_SERVER = "s5380440";
	private static final String IfRightMachine = StringUtils.concat(OzConstants.LINE_FEED, "if NOT %COMPUTERNAME%", "==",
			TARGET_CC_SERVER.toUpperCase(), " exit", OzConstants.LINE_FEED);
	private static Logger logger = JulUtils.getLogger();

	public static void generateMoveViewsScript() {
		List<ViewObject> viewsObjectList = getViewsInfoList();
		StringBuilder sb = new StringBuilder(IfRightMachine);
		for (ViewObject view1 : viewsObjectList) {
			String command1 = StringUtils.concat(ClearCaseUtils.getClearToolPath(), OzConstants.BLANK,
					ClearToolSubCommands.RMTAG, " -view ", view1.getViewTag(), OzConstants.LINE_FEED);
			sb.append(command1);
			String command3 = StringUtils.concat(ClearCaseUtils.getClearToolPath(), OzConstants.BLANK,
					ClearToolSubCommands.UNREGISTER, " -view ", view1.getViewStragePath(), OzConstants.LINE_FEED);
			sb.append(command3);
			String globalPath = view1.getViewStragePath();
			String[] pnameArray = globalPath.split(RegexpUtils.REGEXP_BACK_SLASH);
			pnameArray[2] = TARGET_CC_SERVER;
			String targetViewStoragePath = StringUtils.unsplit(pnameArray, OzConstants.BACK_SLASH);
			String command4 = StringUtils.concat(ClearCaseUtils.getClearToolPath(), OzConstants.BLANK,
					ClearToolSubCommands.REGISTER, " -view ", targetViewStoragePath, OzConstants.LINE_FEED);
			sb.append(command4);
			String command5 = StringUtils.concat(ClearCaseUtils.getClearToolPath(), OzConstants.BLANK,
					ClearToolSubCommands.MKTAG, " -view -tag ", view1.getViewTag(), OzConstants.BLANK,
					targetViewStoragePath, OzConstants.LINE_FEED);
			sb.append(command5);
		}
		logger.info(sb.toString());
		FileUtils.writeFile("C:\\temp\\moveViewsScript.bat", sb.toString());
	}

	public static void generateMoveVobsScript() {
		List<VobObject> vobObjectList = getVobsInfoList();
		StringBuilder sb = new StringBuilder(IfRightMachine);
		for (VobObject vob1 : vobObjectList) {
			String command1 = StringUtils.concat(ClearCaseUtils.getClearToolPath(), OzConstants.BLANK,
					ClearToolSubCommands.UMOUNT, OzConstants.BLANK, vob1.getVobTag(), OzConstants.LINE_FEED);
			sb.append(command1);
			String command2 = StringUtils.concat(ClearCaseUtils.getClearToolPath(), OzConstants.BLANK,
					ClearToolSubCommands.RMTAG, " -vob -password password", OzConstants.BLANK, vob1.getVobTag(),
					OzConstants.LINE_FEED);
			sb.append(command2);
			String command3 = StringUtils.concat(ClearCaseUtils.getClearToolPath(), OzConstants.BLANK,
					ClearToolSubCommands.UNREGISTER, " -vob ", vob1.getVobGlobalPath(), OzConstants.LINE_FEED);
			sb.append(command3);
			String globalPath = vob1.getVobGlobalPath();
			String[] pnameArray = globalPath.split(RegexpUtils.REGEXP_BACK_SLASH);
			pnameArray[2] = TARGET_CC_SERVER;
			String targetGlobalPath = StringUtils.unsplit(pnameArray, OzConstants.BACK_SLASH);
			String command4 = StringUtils.concat(ClearCaseUtils.getClearToolPath(), OzConstants.BLANK,
					ClearToolSubCommands.REGISTER, " -vob ", targetGlobalPath, OzConstants.LINE_FEED);
			sb.append(command4);
			String command5 = StringUtils.concat(ClearCaseUtils.getClearToolPath(), OzConstants.BLANK,
					ClearToolSubCommands.MKTAG, " -vob -tag ", vob1.getVobTag(), " -public -password password ",
					targetGlobalPath, OzConstants.LINE_FEED);
			sb.append(command5);
		}
		logger.info(sb.toString());
		FileUtils.writeFile("C:\\temp\\moveVobsScript.bat", sb.toString());
		// cleartool umount \%myVob%
		// cleartool rmtag -vob -password password \%myVob%
		// cleartool unregister -vob \\%OldVobServer%\VOBs$\%myVob%.vbs
		// cleartool register -vob \\%myVobServer%\VOBs$\%myVob%.vbs
		// cleartool mktag -vob -tag \%myVob% -public -password password
		// \\%myVobServer%\VOBs$\%myVob%.vbs
		// mktag –vob –tag vob-tag [ –tco/mment tag-comment ]
		// [ –rep/lace | –reg/ion network-region ] [ –opt/ions mount-options ]
		// [ –pub/lic ] [ –pas/sword tag-registry-password ]
		//
		// [ –hos/t hostname –gpa/th global-storage-pname
		//
		// | –ngp/ath [ –hos/t hostname ] ] vob-storage-pname

	}

	public static void generateRemoveUnuseedViewsScript(final String filePath, final int cutOffYead,
			final String targetBatchFilePath) {
		final String tagLineStart = "Tag:";
		final String viewServerAccessPathLineStart = "View server access path:";
		final String lastAccessedLineStart = "Last accessed ";
		//
		String viewsString = FileUtils.readTextFile(filePath);
		logger.finest(viewsString);
		String[] viewsArray = viewsString.split(tagLineStart);
		logger.finest(String.valueOf(viewsArray.length));
		int viewCount = 0;
		StringBuilder sb = new StringBuilder();
		for (String view1String : viewsArray) {
			String viewServerAccessPath = null;
			String lastAccessed = null;
			String lastAccessedYear = null;
			String viewTag = null;
			String[] view1LinesArray = view1String.split(OzConstants.LINE_FEED);
			for (int i = 0; i < view1LinesArray.length; i++) {
				String view1Line = view1LinesArray[i].trim();
				if (i == 0) {
					viewTag = view1Line.trim();
				}
				if (view1Line.startsWith(viewServerAccessPathLineStart)) {
					viewServerAccessPath = view1Line.substring(viewServerAccessPathLineStart.length()).trim();
				}
				if (view1Line.startsWith(lastAccessedLineStart)) {
					lastAccessed = view1Line.substring(lastAccessedLineStart.length()).trim();
					lastAccessedYear = lastAccessed.split(OzConstants.MINUS_SIGN)[0];
				}
			}
			if (lastAccessedYear != null && Integer.parseInt(lastAccessedYear) <= cutOffYead) {
				sb.append(StringUtils.concat("\nRem view server access path: ", viewServerAccessPath,
						" year last accessed: ", lastAccessedYear));
				sb.append(StringUtils.concat(" \ncleartool.exe endview -server  ", viewTag));
				sb.append(StringUtils.concat(" \ncleartool.exe rmview -f ", viewServerAccessPath));
				viewCount++;
			}
		}
		sb.append("\n\nRem number of views: " + String.valueOf(viewCount));
		FileUtils.writeFile(targetBatchFilePath, sb.toString());
	}

	public static void generateRemoveViewsScript(final String noexistingUsersFilePasth) {
		int viewCount = 0;
		String[] nonExistingUsersArray = FileUtils.readTextFile2Array(noexistingUsersFilePasth);
		List<ViewObject> viewsObjectList = getViewsInfoList();
		StringBuilder sbLsview = new StringBuilder();
		StringBuilder sbRmview = new StringBuilder();
		for (ViewObject view1 : viewsObjectList) {
			String[] getViewStragePathArray = view1.getViewStragePath().split("\\\\");
			String user = getViewStragePathArray[getViewStragePathArray.length - 2];
			if (ArrayUtils.isObjectInArray(nonExistingUsersArray, user)) {
				// logger.info(user);
				sbLsview.append("\ncleartool.exe lsview -age -storage " + view1.getViewStragePath());
				sbRmview.append("\ncleartool.exe rmview  " + view1.getViewStragePath());
				viewCount++;
			}
		}
		sbLsview.append("\nrem " + String.valueOf(viewCount));
		logger.info(sbLsview.toString());
		FileUtils.writeFile("c:\\temp\\lsview.bat", sbLsview.toString());
		FileUtils.writeFile("c:\\temp\\rmview.bat", sbRmview.toString());
	}

	public static List<ViewObject> getViewsInfoList() {
		List<ViewObject> viewsObjectList = new ArrayList<ViewObject>();
		String[] commandArray = { ClearCaseUtils.getClearToolPath(), ClearToolSubCommands.LSVIEW };
		SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(commandArray);
		logger.finest(scer.getExecutorResponse());
		String stdOut = scer.getStdout();
		String[] lines = stdOut.split(OzConstants.LINE_FEED);
		for (String line1 : lines) {
			logger.finest(line1.concat(" ----"));
			String[] lineBreakDown = line1.trim().split(RegexpUtils.REGEXP_WHITE_SPACE);
			ViewObject viewObject = new ViewObject();
			viewObject.setViewTag(lineBreakDown[0]);
			viewObject.setViewStragePath(lineBreakDown[1]);
			viewsObjectList.add(viewObject);
		}
		logger.finest(
				OzConstants.LINE_FEED.concat(ListUtils.getAsDelimitedString(viewsObjectList, OzConstants.LINE_FEED)));
		return viewsObjectList;
	}

	public static List<VobObject> getVobsInfoList() {
		List<VobObject> vobObjectList = new ArrayList<VobObject>();
		String[] commandArray = { ClearCaseUtils.getClearToolPath(), ClearToolSubCommands.LSVOB };
		SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(commandArray);
		logger.finest(scer.getExecutorResponse());
		String stdOut = scer.getStdout();
		String[] lines = stdOut.split(OzConstants.LINE_FEED);
		for (String line1 : lines) {
			logger.finest(line1.concat(" ----"));
			String[] lineBreakDown = line1.trim().split(RegexpUtils.REGEXP_WHITE_SPACE);
			if (lineBreakDown[0].equals(OzConstants.ASTERISK)) {
				lineBreakDown = ArrayUtils.shift(lineBreakDown, 1);
			}
			VobObject vobObject = new VobObject();
			vobObject.setVobTag(lineBreakDown[0]);
			vobObject.setVobGlobalPath(lineBreakDown[1]);
			vobObjectList.add(vobObject);
		}
		logger.info(OzConstants.LINE_FEED.concat(ListUtils.getAsDelimitedString(vobObjectList, OzConstants.LINE_FEED)));
		return vobObjectList;
	}
}
