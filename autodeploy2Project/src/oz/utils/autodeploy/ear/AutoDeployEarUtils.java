package oz.utils.autodeploy.ear;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.io.filefilter.FileFilterIsFileAndRegExpression;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.operaion.Outcome;
import oz.infra.print.PrintUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.snifit.SnifitUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class AutoDeployEarUtils {
	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());

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
			stopWatch.logElapsedTimeMessage(StringUtils.concat("Operation has completed int "));
			logger.info(StringUtils.concat(String.valueOf(successfulInstalls),
					" applications have been successfully installed"));
			logger.warning(StringUtils.concat(String.valueOf(failedlInstalls), " applications have failed to install"));
		} else {
			logger.warning("No ear files to install !");
		}
		logger.info(PrintUtils.getSeparatorLine("Ear files deployment has completed !", 2, 0));
	}

	public static void processEarsPatameter(final String[] args) {
		logger.info(StringUtils.concat("porcessing ", ArrayUtils.getAsDelimitedString(args, OzConstants.BLANK)));
		String param1 = args[1];
		File param1File = new File(param1);
		if (param1File.isDirectory()) {
			FileFilter fileFiler = new FileFilterIsFileAndRegExpression(RegexpUtils.REGEXP_EAR_OR_WAR_FILE);
			File[] earFiles = param1File.listFiles(fileFiler);
			logger.info("folder contents: ".concat(ArrayUtils.getAsDelimitedString(earFiles)));
			String[] earFilesPathes = new String[earFiles.length + 1];
			for (int i = 0; i < earFiles.length; i++) {
				earFilesPathes[i + 1] = earFiles[i].getAbsolutePath();
			}
			deployEarFiles(earFilesPathes);
		} else {
			deployEarFiles(args);
		}
	}

	static String getSnifitVersion(final EarFileParameters earFileParameters) {
		String snifitVersion = null;
		if (SystemUtils.isWindowsFlavorOS()) {
			String[] getSnifitVersionParametersArray = earFileParameters.getGetSnifitVersionParametersArray();
			if (getSnifitVersionParametersArray != null) {
				String getVersionUrl = getSnifitVersionParametersArray[1].replaceAll("%CONTEXTROOT%",
						earFileParameters.getApplicationName());
				getVersionUrl = getVersionUrl.replaceAll("MatafEAR", "MatafServer");
				int retryCount = Integer.parseInt(getSnifitVersionParametersArray[2]);
				int retryInterval = Integer.parseInt(getSnifitVersionParametersArray[3]);
				snifitVersion = SnifitUtils.getVersionProtected(getSnifitVersionParametersArray[0], getVersionUrl,
						retryCount, retryInterval);
				if (snifitVersion != null) {
					logger.info("snifit Version: ".concat(snifitVersion));
				} else {
					logger.warning("failed to get snifit version");
				}
			}
		}
		return snifitVersion;
	}
}
