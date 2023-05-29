package oz.utils.load;

import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;
import oz.utils.load.generators.AbstractLoadGenerator;

public class LoadGeneratorMain {
	private static Logger logger = JulUtils.getLogger();
	private static String rootFolderPath = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String propertiesfilesFolderPath = args[0];
		rootFolderPath = propertiesfilesFolderPath;
		startLoadGeneration(propertiesfilesFolderPath);
	}

	private static void startLoadGeneration(final String propertiesfilesFolderPath) {
		HashMap<String, Properties> loadGeneratorPropertiesMap = PropertiesUtils
				.loadPropertiesFilesFromFolder(propertiesfilesFolderPath);
		Collection<Properties> loadGenerationCollection = loadGeneratorPropertiesMap.values();
		int numberOfGenerators = 0;
		for (Properties loadGenerationProperties : loadGenerationCollection) {
			String numberOfObjectsString = loadGenerationProperties.getProperty("numberOfObjects");
			int numberOfObjects = 1;
			if (numberOfObjectsString != null && StringUtils.isJustDigits(numberOfObjectsString)) {
				numberOfObjects = Integer.parseInt(numberOfObjectsString);
			}
			for (int i = 0; i < numberOfObjects; i++) {
				LoadGenerationDispatcher loadGenerationDispatcher = new LoadGenerationDispatcher(
						loadGenerationProperties);
				AbstractLoadGenerator abstractLoadGenerator = loadGenerationDispatcher
						.getAbstractLoadGenerator();
				if (abstractLoadGenerator != null) {
					new Thread(loadGenerationDispatcher).start();
					numberOfGenerators++;
				}
			}
		}
		logger.info(StringUtils.concat(String.valueOf(numberOfGenerators),
				" generators have been started ..."));
	}

	public static String getRootFolderPath() {
		return rootFolderPath;
	}
}
