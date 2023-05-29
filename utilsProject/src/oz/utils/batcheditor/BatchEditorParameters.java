package oz.utils.batcheditor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.datetime.StopWatch;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;

public class BatchEditorParameters {
	private static Logger logger = JulUtils.getLogger();

	public static void processParameters(final String parameterFolderPath) {
		List<String> propertyfilesPathList = new ArrayList<String>();
		File patameterFile = new File(parameterFolderPath);
		if (patameterFile.isDirectory()) {
			File[] files = patameterFile.listFiles();
			for (File file1 : files) {
				if (file1.isFile()) {
					propertyfilesPathList.add(file1.getAbsolutePath());
				}
			}
		} else {
			logger.warning(StringUtils.concat(parameterFolderPath, " is not a directory. No processing performed !"));
		}
		processPatameters(propertyfilesPathList);
	}

	public static void processParameters(final String[] args) {
		processParameters(args[0]);
	}

	public static void processPatameters(final List<String> propertyfilesPathList) {
		StopWatch stopWatch = new StopWatch();
		int filesProcessed = 0;
		int filesModified = 0;
		for (String propertiesFilePath : propertyfilesPathList) {
			logger.info("Processing batch editor parameters file: " + propertiesFilePath);
			Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
			FileModification fileModification = new FileModification(properties);
			boolean fileModified = fileModification.doProcess();
			if (fileModified) {
				filesModified++;
			}
			filesProcessed++;
		}
		String message = StringUtils.concat(String.valueOf(filesProcessed), " files have been processed, ",
				String.valueOf(filesModified), " have been modified in ");
		stopWatch.logElapsedTimeMessage(message);
	}
}
