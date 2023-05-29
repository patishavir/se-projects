package oz.utils.autodeploy.snifit;

import java.io.File;
import java.util.logging.Handler;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.nio.NioUtils;
import oz.infra.operaion.Outcome;
import oz.infra.process.FileProcessor;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.string.StringUtils;
import oz.utils.autodeploy.common.AutoDeployGlobalParameters;

public class AutoDeploySnifitProcessor implements FileProcessor {

	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());
	private String timeStamp = null;
	private Handler fileHandler = null;
	private static final String INPROGRESS_FOLDER_NAME = "inProgress";

	@Override
	public Outcome processFile(final String filePath) {
		String inProgressFilePath = getInProgressFilePath(filePath);
		logger.info(inProgressFilePath);
		NioUtils.waitAndMove(filePath, inProgressFilePath);
		String[] parametersArray1 = { inProgressFilePath };
		SystemCommandExecutorResponse scer = SystemCommandExecutorRunner.run(parametersArray1);
		logger.info(scer.getExecutorResponse());
		return Outcome.FAILURE;
	}

	private static String getInProgressFilePath(final String filePath) {
		logger.info(filePath);
		String grandParentFolderPath = PathUtils.getGrandParentFolderPath(filePath);
		String inProgressFolderPath = PathUtils.getFullPath(grandParentFolderPath, INPROGRESS_FOLDER_NAME);
		File file = new File(filePath);
		String fileName = PathUtils.getFileNameWithoutExtension(file);
		String extension = PathUtils.getFileExtension(filePath);
		String tempString = PathUtils.getTemporaryFileName();
		String inProgressFilePath = PathUtils.getFullPath(inProgressFolderPath,
				fileName + OzConstants.UNDERSCORE + tempString + OzConstants.DOT + extension);
		return inProgressFilePath;
	}

	private void startAnewLog(final String applicationName) {
		String logFileName = StringUtils.concat(applicationName, OzConstants.UNDERSCORE, timeStamp,
				OzConstants.ERR_SUFFIX);
		String logFilePath = PathUtils.getFullPath(AutoDeployGlobalParameters.getLogsFolder(), logFileName);
		File logfile = new File(logFilePath);
		fileHandler = JulUtils.addFileHandler(logfile.getAbsolutePath());
	}
}