package oz.test.infra;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class TestEnvironmentUtils {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		logger.info("starting ... ");
		StringUtils.substituteEnvironmentVariables("%COMPUTERNAME%");
		StringUtils.substituteEnvironmentVariables("yyy%%zzz123%PUTERNAME%xxx");
		StringUtils.substituteEnvironmentVariables("yyy%%zzz123%PUTERNAME%%USERNAME%xxx");
		Map<String, String> map = new HashMap<String, String>();
		map.put("x1", "177");
		map.put("zzzx1", "18877");
		StringUtils.substituteVariables("%x1%rrrrr%zzzx1%000", map);
	}
}
