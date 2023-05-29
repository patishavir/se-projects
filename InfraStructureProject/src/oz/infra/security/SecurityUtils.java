package oz.infra.security;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;

public class SecurityUtils {
	private static final String JAVA_SECURITY_AUTH_LOGIN_CONFIG = "java.security.auth.login.config";
	private static Logger logger = JulUtils.getLogger();

	public static int setJaasLoginConfigFilePath(final String jaasLoginConfigFilePath) {
		int returnCode = OzConstants.EXIT_STATUS_OK;
		File jaasLoginConfigFile = new File(jaasLoginConfigFilePath);
		if (jaasLoginConfigFile.isFile()) {
			System.setProperty(JAVA_SECURITY_AUTH_LOGIN_CONFIG, jaasLoginConfigFilePath);
		} else {
			logger.warning(jaasLoginConfigFilePath + " does not exist !");
			returnCode = OzConstants.EXIT_STATUS_ABNORMAL;
		}
		return returnCode;
	}
}
