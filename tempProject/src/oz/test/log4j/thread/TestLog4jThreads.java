package oz.test.log4j.thread;

import oz.infra.thread.ThreadUtils;

public class TestLog4jThreads {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testThreads();
	}

	private static void testThreads() {
		// String fname =
		// "C:\\oj\\projects\\zvlProject\\src\\oz\\test\\log4j\\thread\\config\\log4j_subscriptions.properties";
		// String fname =
		// "C:/oj/projects/zvlProject/src/oz/test/log4j/thread/config/log4j_subscriptions.properties";
		// String fname =
		// "C://oj//projects//zvlProject//src//oz//test//log4j//thread//config//log4j_subscriptions.properties";
		String fname = "C:///oj///projects///zvlProject///src///oz///test///log4j///thread///config///log4j_subscriptions.properties";
		String loggerNm = "KG_TRACE";
		ThreadUtils.getJvmThreadCount();
		Log.init(fname, loggerNm);
		ThreadUtils.getJvmThreadCount();
		for (int i = 0; i < 100; i++) {
			Log.init(fname, loggerNm);
		}
		ThreadUtils.getJvmThreadCount();
	}
}
