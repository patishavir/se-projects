package oz.utils.was.autodeploy;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.io.filefilter.FileFilterIsFileAndRegExpression;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.operaion.Outcome;
import oz.infra.print.PrintUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.string.StringUtils;

public class AutoDeployEarUtils {
	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());

	public static void processEarsPatameter(final String[] args) {
		String param1 = args[1];
		File param1File = new File(param1);
		if (param1File.isDirectory()) {
			FileFilter fileFiler = new FileFilterIsFileAndRegExpression(RegexpUtils.REGEXP_EAR_OR_WAR_FILE);
			File[] earFiles = param1File.listFiles(fileFiler);
			String[] earFilesPathes = new String[earFiles.length + 1];
			for (int i = 0; i < earFiles.length; i++) {
				earFilesPathes[i + 1] = earFiles[i].getAbsolutePath();
			}
			deployEarFiles(earFilesPathes);
		} else {
			deployEarFiles(args);
		}
	}

	private static void deployEarFiles(final String[] earFilesPaths) {
		StopWatch stopWatch = new StopWatch();
		int earPtr;
		int successfulInstalls = 0;
		int failedlInstalls = 0;
		if (earFilesPaths.length > 1) {
			for (earPtr = 1; earPtr < earFilesPaths.length; earPtr++) {
				String earFilePath = earFilesPaths[earPtr];
				if (earFilePath.equals(OzConstants.NUMBER_SIGN) || earFilePath.startsWith(OzConstants.NUMBER_SIGN)) {
					break;
				}
				Outcome outcome = new AutoDeployEarProcessor().processFile(earFilePath);
				if (outcome.equals(Outcome.SUCCESS)) {
					successfulInstalls++;
				} else {
					failedlInstalls++;
				}
			}
			logger.info(StringUtils.concat("Operation has completed in ", stopWatch.getElapsedTimeString()));
			logger.info(StringUtils.concat(String.valueOf(successfulInstalls),
					" applications have been successfully installed"));
			logger.warning(StringUtils.concat(String.valueOf(failedlInstalls), " applications have failed to install"));
		} else {
			logger.warning("No ear files to install !");
		}
		logger.info(PrintUtils.getSeparatorLine("all done !", OzConstants.EQUAL_SIGN));
	}
}
