package oz.infra.debug;

import oz.infra.constants.OzConstants;
import oz.infra.system.env.EnvironmentUtils;

public class DebugUtils {
	private static final String DEBUG_MODE = "debugMode";
	private static Boolean debugMode = null;

	public static boolean isDebugMode() {
		if (debugMode == null) {
			String envVar = EnvironmentUtils.getEnvironmentVariable(DEBUG_MODE);
			if (envVar != null
					&& (envVar.equalsIgnoreCase(OzConstants.YES) || envVar.equalsIgnoreCase(OzConstants.TRUE))) {
				debugMode = Boolean.TRUE;
			} else {
				debugMode = Boolean.FALSE;
			}
		}
		return debugMode;
	}
}
