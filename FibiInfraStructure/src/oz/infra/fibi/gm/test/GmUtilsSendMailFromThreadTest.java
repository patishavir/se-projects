package oz.infra.fibi.gm.test;

import oz.infra.system.SystemUtils;

public class GmUtilsSendMailFromThreadTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] gmEnvironments = { "gmtest", "gmq" };
		GmUtilsSendMailTest gmUtilsSendMailTest = new GmUtilsSendMailTest(gmEnvironments);
		Thread thread = new Thread(gmUtilsSendMailTest);
		thread.setName(SystemUtils.getCallerClassAndMethodName());
		thread.start();
		// thread.join();
	}

}
