package oz.demo.findbugs;

import java.util.ArrayList;
import java.util.logging.Logger;

import oz.demo.burn.BurnUtils;

/**
 * 
 * @author s177571
 * 
 */
public class FindBugsDemo implements Cloneable {
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(FindBugsDemo.class
			.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int length = M4("1111111111111222222222222222222", 12000).length();
		logger.info("length=" + String.valueOf(length));
		logger.info("generated " + String.valueOf(M9(7777).size())
				+ " objects.");
		int i = 1;
		String[] strArray = { "one", "two" };
		m8(i, strArray);
		logger.info("i = " + String.valueOf(i));
		logger.info("strArray[0]= " + strArray[0]);
		M9(1000);

	}

	/**
	 * 
	 * @param strParam
	 */
	private static void M0(String strParam) {
		logger.info(strParam.toLowerCase());
	}

	private static void M1(boolean bolparam) {
		while (bolparam) {
			logger.info(Boolean.valueOf(bolparam).toString());
		}

	}

	/**
	 * 
	 * @param b1
	 * @param b21
	 * @param b31
	 * @param b41
	 * @param b51
	 * @param b61
	 * @param b71
	 * @param b81
	 * @param b91
	 * @param b101
	 * @param b111
	 * @param b121
	 * @param b131
	 * @return
	 */

	final public static Boolean M3(boolean b1, boolean b21, boolean b31,
			boolean b41, boolean b51, boolean b61, boolean b71, boolean b81,
			boolean b91, boolean b101, boolean b111, boolean b121, boolean b131) {

		try {
			String str1 = "sss";
			if (str1 == "bbb")
				logger.info("===");
			while (b1) {
				logger.info(Boolean.valueOf(b1).toString());
			}
		} catch (Exception ex) {

		}
		return null;

	}

	/**
	 * 
	 * @param str1
	 * @param iterations
	 * @return
	 */
	public static String M4(String str1, int iterations) {
		return BurnUtils.burnCpu(str1, iterations);

	}

	public static void M5(int i) {
		switch (i) {
		case 1:
			logger.info("1");
		case 2:
			logger.info("2");
			break;
		}

	}

	/**
	 * 
	 */
	public static void M6() {

		int i = 7;
		i = i + 17;
	}

	public static void M7() {

		String str = null;
		String message = null;
		String param = null;
		if (str.toLowerCase().equalsIgnoreCase("111")) {
			logger.info(message);
		}
		boolean bol = true;
		while (bol) {
			logger.info(message);
		}
		M0(param);
		M1(bol);
		M6();

	}

	/**
	 * 
	 * @param iii
	 * @param sss
	 */
	public static void m8(int iii, final String[] sss) {

		iii = 77;
		sss[0] = "00000000000";

	}

	public static ArrayList<BurnUtils> M9(int iterations) {
		return BurnUtils.burnMemory(iterations);
	}
}
