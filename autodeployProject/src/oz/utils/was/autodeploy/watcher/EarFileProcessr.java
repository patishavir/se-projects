package oz.utils.was.autodeploy.watcher;

import static oz.infra.constants.OzConstants.DOT;
import static oz.infra.constants.OzConstants.INT_1000;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;
import oz.infra.string.StringUtils;
import oz.infra.thread.ThreadUtils;
import oz.utils.was.autodeploy.snifit.AutoDeployEarFileProcessor;
import oz.utils.was.autodeploy.snifit.AutoDeployParameters;

public class EarFileProcessr {

	private String filePathSuffix = null;
	private static String dateSuffix = null;
	private File sourceFile = null;
	private String sourceFileName = null;
	private String sourceFileType = null;
	private static final String QUIT_REQUEST = "quitAutoDeploy";
	private static final String EAR_SUFFIX = "ear";
	private FileHandler fileHandler = null;
	private static Logger logger = JulUtils.getLogger();

	private void processInvalidFile() {
		logger.warning(sourceFile.getAbsolutePath() + " is invalid and will not be processed.");
		File tagetFile = new File(AutoDeployParameters.getInvalidFolderPath() + File.separator + filePathSuffix + DOT
				+ dateSuffix);
		File tagetFileParent = tagetFile.getParentFile();
		FolderUtils.createFolderIfNotExists(tagetFileParent);
		boolean rc = sourceFile.renameTo(tagetFile);
		if (rc) {
			logger.warning(sourceFile.getAbsolutePath() + " has been moved to " + tagetFile.getAbsolutePath());
		} else {
			logger.warning("Failed to move " + sourceFile.getAbsolutePath() + " to " + tagetFile.getAbsolutePath());
		}
	}

	private void processValidFile(final String earFileFullPath) {
		String applicationNameSuffix = filePathSuffix.substring(0, filePathSuffix.indexOf(File.separator));
		String applicationName = sourceFileName + OzConstants.UNDERSCORE + applicationNameSuffix;
		String tagetEarFilePath = AutoDeployParameters.getInProgressdFolderPath() + File.separator + applicationName
				+ DOT + dateSuffix + DOT + EAR_SUFFIX;
		File tagetEarFile = new File(tagetEarFilePath);
		logger.info("target ear file: " + tagetEarFile.getAbsolutePath());
		boolean rc = false;
		final int retryCount = 10;
		for (int i = 0; (i < retryCount) && (!rc); i++) {
			ThreadUtils.sleep(INT_1000, Level.INFO);
			rc = sourceFile.renameTo(tagetEarFile);
		}
		if (rc) {
			logger.info(earFileFullPath + " has been move to " + tagetEarFilePath);
			new AutoDeployEarFileProcessor().processEarFile(tagetEarFilePath, applicationNameSuffix);
		} else {
			logger.warning("Failed to move " + earFileFullPath + " to "
					+ AutoDeployParameters.getInProgressdFolderPath());
		}
	}

	private void startAnewLog() {
		String applicationNameSuffix = filePathSuffix.substring(0, filePathSuffix.indexOf(File.separator));
		String logFileName = StringUtils.concat(applicationNameSuffix, OzConstants.UNDERSCORE, dateSuffix,
				OzConstants.ERR_SUFFIX);
		String logFilePath = PathUtils.getFullPath(AutoDeployParameters.getLogsFolder(), logFileName);
		fileHandler = JulUtils.switchFileHandler(fileHandler, logFilePath);
	}

	void processFile(final String filePathSuffix) {
		this.filePathSuffix = filePathSuffix;
		logger.info("filePathSuffix: " + filePathSuffix);
		dateSuffix = DateTimeUtils.getTimeStamp();
		String sourceEarFilePath = PathUtils.getFullPath(AutoDeployParameters.getInputEarFolderPath(), filePathSuffix);
		logger.info(PrintUtils.getSeparatorLine("processing ".concat(sourceEarFilePath)));
		sourceFile = new File(sourceEarFilePath);
		sourceFileType = PathUtils.getFileExtension(sourceEarFilePath);
		sourceFileName = PathUtils.getFileNameWithoutExtension(sourceFile);
		if (sourceFileName.equalsIgnoreCase(QUIT_REQUEST)) {
			logger.info("Quit request has been successully processed");
			FileUtils.deleteAndLogResult(sourceFile);
			System.exit(OzConstants.EXIT_STATUS_OK);
		}
		if (sourceFile.isFile()) {

		}
		startAnewLog();
		if (sourceFileType.toLowerCase().equals(EAR_SUFFIX)
				&& filePathSuffix.indexOf(File.separator) > OzConstants.STRING_NOT_FOUND) {
			processValidFile(sourceEarFilePath);
		} else {
			processInvalidFile();
		}
	}
}
