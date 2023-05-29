package oz.utils.modifyear;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.string.StringUtils;

public class ModifyEarParameters {

	private static String rootFolderPath = null;
	private static String workDirPath = null;
	private static String earFilePath = null;
	private static String expandedEarFolderName = null;
	private static String warFileName = "MatafServer.war";
	private static String bttRuntimeMonitorFileName = "BTTRuntimeMonitor.war";
	private static String expandedWarFolderName = null;
	private static String expandedBTTRuntimeMonitorFolderName = null;
	private static String applicationCode = null;
	private static String batchEditorArgsFolderPath = null;

	private static String warFilePath = null;
	private static String bttRuntimeMonitorFilePath = null;
	private static String expandedEarFolderPath = null;
	private static String expandedWarFolderPath = null;
	private static String expandedBTTRuntimeMonitorFolderPath = null;

	private static Logger logger = JulUtils.getLogger();

	public static String getApplicationCode() {
		return applicationCode;
	}

	public static String getBatchEditorArgsFolderPath() {
		return batchEditorArgsFolderPath;
	}

	public static String getBttRuntimeMonitorFilePath() {
		return bttRuntimeMonitorFilePath;
	}

	public static String getEarFilePath() {
		return earFilePath;
	}

	public static String getExpandedBTTRuntimeMonitorFolderName() {
		return expandedBTTRuntimeMonitorFolderName;
	}

	public static String getExpandedBTTRuntimeMonitorFolderPath() {
		return expandedBTTRuntimeMonitorFolderPath;
	}

	public static String getExpandedEarFolderPath() {
		return expandedEarFolderPath;
	}

	public static String getExpandedWarFolderPath() {
		return expandedWarFolderPath;
	}

	public static String getRootFolderPath() {
		return rootFolderPath;
	}

	public static String getWarFilePath() {
		return warFilePath;
	}

	public static String getWorkDirPath() {
		return workDirPath;
	}

	public static void processParameters(final String propertiesFilePath) {
		rootFolderPath = ParametersUtils.processPatameters(propertiesFilePath, ModifyEarParameters.class);

		batchEditorArgsFolderPath = PathUtils.getFullPath(rootFolderPath, batchEditorArgsFolderPath);
		logger.info(StringUtils.concat("batchEditorArgsFolderPath: ", batchEditorArgsFolderPath));

		expandedEarFolderPath = PathUtils.getFullPath(workDirPath, expandedEarFolderName);
		warFilePath = PathUtils.getFullPath(expandedEarFolderPath, warFileName);
		bttRuntimeMonitorFilePath = PathUtils.getFullPath(expandedEarFolderPath, bttRuntimeMonitorFileName);

		expandedWarFolderPath = PathUtils.getFullPath(workDirPath, expandedWarFolderName);
		expandedBTTRuntimeMonitorFolderPath = PathUtils.getFullPath(workDirPath, expandedBTTRuntimeMonitorFolderName);
	}

	public static void setApplicationCode(String applicationCode) {
		ModifyEarParameters.applicationCode = applicationCode;
	}

	public static void setBatchEditorArgsFolderPath(String batchEditorArgsFolderPath) {
		ModifyEarParameters.batchEditorArgsFolderPath = batchEditorArgsFolderPath;
	}

	public static void setEarFilePath(String earFilePath) {
		ModifyEarParameters.earFilePath = earFilePath;
	}

	public static void setExpandedBTTRuntimeMonitorFolderName(String expandedBTTRuntimeMonitorFolderName) {
		ModifyEarParameters.expandedBTTRuntimeMonitorFolderName = expandedBTTRuntimeMonitorFolderName;
	}

	public static void setExpandedEarFolderName(String expandedEarFolderName) {
		ModifyEarParameters.expandedEarFolderName = expandedEarFolderName;
	}

	public static void setExpandedWarFolderName(String expandedWarFolderName) {
		ModifyEarParameters.expandedWarFolderName = expandedWarFolderName;
	}

	public static void setRootFolderPath(String rootFolderPath) {
		ModifyEarParameters.rootFolderPath = rootFolderPath;
	}

	public static void setWarFileName(String warFileName) {
		ModifyEarParameters.warFileName = warFileName;
	}

	public static void setWorkDirPath(String workDirPath) {
		ModifyEarParameters.workDirPath = workDirPath;
		File workDirFile = new File(workDirPath);
		if (!workDirFile.exists()) {
			workDirFile.mkdirs();
		}
	}
}
