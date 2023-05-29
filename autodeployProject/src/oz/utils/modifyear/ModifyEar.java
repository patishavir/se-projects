package oz.utils.modifyear;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.MapUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.env.EnvironmentUtils;
import oz.infra.zip.ZipUtils;
import oz.infra.zip4j.Zip4jUtils;
import oz.utils.batcheditor.BatchEditorParameters;

public class ModifyEar {
	private static final String PROD_CODES = "P,L,B";
	private static final String ALL = "ALL";
	private static final String NON_PROD = "NON_PROD";
	private static final String ENVIRONMENT_CODE = "ENVIRONMENT_CODE";
	private static final String APPLICATION_CODE = "APPLICATION_CODE";
	private static final String WORK_DIR_PATH = "workDirPath";
	private static Logger logger = JulUtils.getLogger();

	public static String compressEar() {
		ZipUtils.zipFolder(ModifyEarParameters.getWarFilePath(), ModifyEarParameters.getExpandedWarFolderPath(),
				ModifyEarParameters.getExpandedWarFolderPath());
		ZipUtils.zipFolder(ModifyEarParameters.getBttRuntimeMonitorFilePath(),
				ModifyEarParameters.getExpandedBTTRuntimeMonitorFolderPath(),
				ModifyEarParameters.getExpandedBTTRuntimeMonitorFolderPath());
		String targetEarFilePath = getTargetEarFilePath(ModifyEarParameters.getEarFilePath());
		ZipUtils.zipFolder(targetEarFilePath, ModifyEarParameters.getExpandedEarFolderPath(),
				ModifyEarParameters.getExpandedEarFolderPath());
		return targetEarFilePath;
	}

	public static void expandEar() {
		Zip4jUtils
				.extractAllFiles(ModifyEarParameters.getEarFilePath(), ModifyEarParameters.getExpandedEarFolderPath());
		Zip4jUtils
				.extractAllFiles(ModifyEarParameters.getWarFilePath(), ModifyEarParameters.getExpandedWarFolderPath());
		Zip4jUtils.extractAllFiles(ModifyEarParameters.getBttRuntimeMonitorFilePath(),
				ModifyEarParameters.getExpandedBTTRuntimeMonitorFolderPath());
	}

	private static String getTargetEarFilePath(final String earFilePath) {
		File earFile = new File(earFilePath);
		String earFileName = PathUtils.getFileNameWithoutExtension(earFile);
		String targetEarFilePath = PathUtils.getFullPath(earFile.getParent(), StringUtils.concat(earFileName,
				OzConstants.UNDERSCORE, ModifyEarParameters.getApplicationCode(), OzConstants.EAR_SUFFIX));
		return targetEarFilePath;
	}

	public static String modifyEar(final String propertiesFilePath, final String earFilePath,
			final String applicationCode) {
		ModifyEarParameters.processParameters(propertiesFilePath);
		ModifyEarParameters.setEarFilePath(earFilePath);
		ModifyEarParameters.setApplicationCode(applicationCode);
		String environmenyCode = applicationCode.substring(0, 1);
		expandEar();
		updateEar(environmenyCode, applicationCode);
		return compressEar();
	}

	private static void updateEar(final String environmenyCode, final String applicationCode) {
		Map<String, String> newEnvironmentVariableMap = new HashMap<String, String>();
		newEnvironmentVariableMap.put(ENVIRONMENT_CODE, environmenyCode);
		newEnvironmentVariableMap.put(APPLICATION_CODE, applicationCode);
		newEnvironmentVariableMap.put(WORK_DIR_PATH, ModifyEarParameters.getWorkDirPath());
		EnvironmentUtils.setJvmEnvironmentVariable(newEnvironmentVariableMap);
		MapUtils.printMap(newEnvironmentVariableMap, Level.INFO);
		String batchEditorArgsFolderPath = ModifyEarParameters.getBatchEditorArgsFolderPath();
		BatchEditorParameters.processParameters(PathUtils.getFullPath(batchEditorArgsFolderPath, ALL));
		boolean prodEnv = environmenyCode.indexOf(PROD_CODES) > OzConstants.STRING_NOT_FOUND;
		if (!prodEnv) {
			BatchEditorParameters.processParameters(PathUtils.getFullPath(batchEditorArgsFolderPath, NON_PROD));
			BatchEditorParameters.processParameters(PathUtils.getFullPath(batchEditorArgsFolderPath, environmenyCode));
		}
	}
}
