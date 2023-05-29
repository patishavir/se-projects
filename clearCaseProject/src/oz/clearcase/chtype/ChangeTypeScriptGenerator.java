package oz.clearcase.chtype;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.system.SystemUtils;

public class ChangeTypeScriptGenerator {
	private static final String COMPRESSED_FILE = "compressed_file";
	private static final Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String folderPath = "M:\\ChiefProg_Dev_Dyn\\chiefProgVOB\\chiefProgComp\\IIB";
		String fileNameSuffix = ".bar";
		String scriptFolderPath = "c:\\temp";
		String fileList = getFileTypes(folderPath, fileNameSuffix);
		generateChtypeScript(fileList, scriptFolderPath);

	}

	private static String getFileTypes(final String folderPath, final String fileNameSuffix) {
		String[] parametersArray = { "cleartool.exe", "find", folderPath, "-name", "*" + fileNameSuffix, "-exec",
				"cleartool.exe file %CLEARCASE_PN%" };
		logger.info(ArrayUtils.getAsDelimitedString(parametersArray, OzConstants.BLANK));
		SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(parametersArray);
		logger.info(scer.getExecutorResponse());
		return scer.getStdout();
	}

	private static void generateChtypeScript(final String fileList, final String scriptFolder) {
		String[] filesTypeArray = fileList.split(OzConstants.LINE_FEED);
		logger.info("found " + String.valueOf(filesTypeArray.length) + " files.");
		StringBuilder sb = new StringBuilder(SystemUtils.LINE_SEPARATOR);
		for (String fileEntry : filesTypeArray) {
			logger.finest("file entry: " + fileEntry);
			String[] fileEntryArray = fileEntry.split(OzConstants.BLANK);
			String fileType = fileEntryArray[fileEntryArray.length - 1];
			String filePath = fileEntryArray[0].substring(0, fileEntryArray[0].length() - 1);
			if (!fileType.equals(COMPRESSED_FILE)) {
				logger.info("file path: " + filePath + "  file type: " + fileType);
				String commandLine = "cleartool.exe chtype -ncomment -force -pname binary_delta_file " + filePath + SystemUtils.LINE_SEPARATOR;
				sb.append(commandLine);
			}
		}
		sb.append(OzConstants.PAUSE);
		sb.append(SystemUtils.LINE_SEPARATOR);
		logger.info(sb.toString());
		String scriptFilePath = scriptFolder + File.separator + ChangeTypeScriptGenerator.class.getSimpleName() + OzConstants.BAT_SUFFIX;
		FileUtils.writeFile(scriptFilePath, sb.toString());
	}
}
