package oz.infra.net.resolve.test;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.net.resolve.ResolveUtils;

public class TestResolveUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		logger.info(ResolveUtils.getHostName("10.12.101.197"));
		logger.info(ResolveUtils.getCanonicalHostName("10.12.101.197"));
	}
}
