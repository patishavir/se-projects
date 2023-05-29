package oz.infra.debug.test;

import java.util.logging.Logger;

import oz.infra.debug.DebugUtils;
import oz.infra.logging.jul.JulUtils;

public class TestDebugUtils {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info(String.valueOf(DebugUtils.isDebugMode()));
	}
}
