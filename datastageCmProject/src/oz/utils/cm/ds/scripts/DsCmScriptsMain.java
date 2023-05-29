package oz.utils.cm.ds.scripts;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class DsCmScriptsMain {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		String propertiesFilePath = args[0];
		haavara(propertiesFilePath);
	}

	private static void haavara(final String propertiesFilePath) {
		DsCmScriptsParameters.processParameters(propertiesFilePath);
		DsCmScriptsProcessor.processScripts();
	}
}
