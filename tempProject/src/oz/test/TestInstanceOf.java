package oz.test;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.thread.ThreadUtils;

public class TestInstanceOf {
	private static Logger logger = JulUtils.getLogger("bla");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[][] ar2 = new String[10][20];
		testInstanceOf(ar2);
		logger.info("all done !");
		ThreadUtils.sleep(1000, Level.INFO);
	}

	private static void testInstanceOf(final Object obj) {
		if (obj instanceof String[][]) {
			logger.info("String[][]");
			String[][] ar2 = (String[][]) obj;
		}
		if (obj instanceof String[]) {
			logger.info("String[]");
			String[] ar1 = (String[]) obj;
		}
		if (obj instanceof ArrayList) {
			logger.info("ArrayList");
			ArrayList ar2 = (ArrayList) obj;
		}
	}
}
