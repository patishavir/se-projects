package oz.infra.jmx.test;

import oz.infra.jmx.exit.ExitHandler;
import oz.infra.thread.ThreadUtils;

public class TestJmxUtilsServer {
	/*
	 * -Dcom.sun.management.jmxremote.port=9999
	 * -Dcom.sun.management.jmxremote.authenticate=false
	 * -Dcom.sun.management.jmxremote.ssl=false optional
	 * -Dcom.sun.management.jmxremote.local.only=false
	 * -Dcom.sun.management.jmxremote -Djava.rmi.server.hostname=127.0.0.1
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		new ExitHandler();
		ThreadUtils.sleep(Long.MAX_VALUE);
	}
}