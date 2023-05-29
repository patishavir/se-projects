package oz.utils.http;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.http.HTTPUtils;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.NioUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.thread.ThreadUtils;

public class HttpClientUtils {
	private static Logger logger = JulUtils.getLogger();

	enum HttpUtilsOptions {
		GetProtectedPageLoop
	};

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		logger.info(ArrayUtils.getAsDelimitedString(args));
		if (args.length > 0) {
			String HttpUtilsOptionString = args[0];
			HttpUtilsOptions httpUtilsOption = HttpUtilsOptions.valueOf(HttpUtilsOptionString);
			switch (httpUtilsOption) {
			case GetProtectedPageLoop:
				getProtectedPageLoop(args);
			}
		} else {
			logger.info("\nUsage: operationCode propertiesFilePath targetFolderPath sleepInterval nuberOfIterations");
		}
	}

	private static void getProtectedPageLoop(final String[] args) {
		String propertiesFilePath = args[1];
		Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		if (properties == null) {
			SystemUtils.printMessageAndExit(propertiesFilePath + " not found. \nExecution aborted.",
					OzConstants.EXIT_STATUS_ABNORMAL);
		}
		String targetFolderPath = args[2];
		FolderUtils.createFolderIfNotExists(targetFolderPath);
		long sleepIntervalMillis = Long.parseLong(args[3]) * 1000l;
		int iterationCount = Integer.parseInt(args[4]);
		if (iterationCount < 1) {
			iterationCount = Integer.MAX_VALUE;
		}
		for (int i = 0; i < iterationCount; i++) {
			logger.info(StringUtils.repeatString(OzConstants.EQUAL_SIGN, 120));
			String pageContents1 = HTTPUtils.getProtectedPageContentsWithPreemptiveBasicAuthentication(properties);
			String fullPath = getFilePath(targetFolderPath);
			logger.info(fullPath);
			NioUtils.writeString2File(fullPath, pageContents1);
			ThreadUtils.sleep(sleepIntervalMillis);
		}
		logger.info(String.valueOf(iterationCount) + " files have been saved to " + targetFolderPath);
	}

	private static String getFilePath(String folderPath) {
		String fileName = SystemUtils.getHostname() + OzConstants.UNDERSCORE + DateTimeUtils.getTimeStamp();
		String fullPath = PathUtils.getFullPath(folderPath, fileName + OzConstants.XML_SUFFIX);
		return fullPath;
	}
}