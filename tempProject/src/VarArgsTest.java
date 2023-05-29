import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class VarArgsTest {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		m1("qqq", "www", "rrr");
		String[] sa = { "11111111", "222222222222", "qqqqqqqqqqqqq", "z" };
		m1(sa);
	}

	private static void m1(String... strs) {
		for (int i = 0; i < strs.length; i++) {
			logger.info(strs[i]);
		}
	}
}
