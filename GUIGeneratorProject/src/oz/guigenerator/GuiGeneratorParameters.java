package oz.guigenerator;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;

public class GuiGeneratorParameters {
	private static String setVariableCommand = "set";
	private static String tempFolder = SystemPropertiesUtils.getTempDir();
	private static Logger logger = JulUtils.getLogger();
	static {
		logger.finest("osname: " + SystemPropertiesUtils.getOsName());
		if (SystemPropertiesUtils.getOsName().equals(OzConstants.AIX)) {
			setVariableCommand = "export";
		}
	}

	public static String getSetVariableCommand() {
		return setVariableCommand;
	}

	public static String getTempFolder() {
		return tempFolder;
	}
}
