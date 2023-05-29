package oz.clearcase.fixprot;

import java.util.List;
import java.util.logging.Logger;

import oz.clearcase.cleartool.ClearToolUtils;
import oz.clearcase.infra.ClearCaseUtils;
import oz.clearcase.view.ViewObject;
import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class FixProtUtils {
	private static String targetFolder = "C:\\temp\\";
	private static Logger logger = JulUtils.getLogger();

	private static void generateFixProtViewsScript1(final List<ViewObject> viewsObjectList, final String groupParam,
			final String pathFilter) {
		String group = "ClearCase_Users";
		// String group = "ClearCaseAdmins";
		StringBuilder sb = new StringBuilder("cd /d " + getFixProtFolder());
		for (ViewObject view1 : viewsObjectList) {

			String viewStragePath = view1.getViewStragePath();
			String[] viewStragePathArray = viewStragePath.split("\\\\");
			String user = viewStragePathArray[viewStragePathArray.length - 2];

			if (viewStragePath.indexOf(pathFilter) > OzConstants.STRING_NOT_FOUND) {
				String command1 = "\nfix_prot.exe -force -r -chown " + user + " -chgrp " + group + " -chmod 770 "
						+ viewStragePath;
				String command2 = "\nfix_prot.exe -force -root -chown " + user + " -chgrp " + group + " "
						+ viewStragePath;
				sb.append(command1);
				sb.append(command2);
			}
		}
		logger.info(sb.toString());
		String scriptFilePath = targetFolder + groupParam + "_FixProtViewsScript.bat";
		FileUtils.writeFile(scriptFilePath, sb.toString());
	}

	public static void generateFixProtViewsScriptRecover() {
		String clearCaseHome = ClearCaseUtils.getClearCaseHome();
		List<ViewObject> viewsObjectList = ClearToolUtils.getViewsInfoList();
		StringBuilder sb = new StringBuilder("cd /d " + getFixProtFolder());
		for (ViewObject view1 : viewsObjectList) {
			String command1 = StringUtils.concat("\nfix_prot.exe -root -recover ", view1.getViewStragePath());
			sb.append(command1);

		}
		logger.info(sb.toString());
		FileUtils.writeFile("C:\\temp\\generateFixProtViewsScript.txt", sb.toString());
	}

	public static void generateFixProtViewsScripts() {
		List<ViewObject> viewsObjectList = ClearToolUtils.getViewsInfoList();
		// generateFixProtViewsScript1(viewsObjectList, "ClearCase_Users_Snifit", "\\snifitViews$\\");
		// generateFixProtViewsScript1(viewsObjectList, "ClearCase_Users", "\\Views$\\");
		 generateFixProtViewsScript1(viewsObjectList, "ClearCase_Users", "D797MC01");
	}

	public static String getFixProtFolder() {
		String clearCaseHome = ClearCaseUtils.getClearCaseHome();
		String fixProtFolderPath = clearCaseHome + "\\etc\\utils";
		return fixProtFolderPath;
	}
}
