package oz.temp.singleInstance;

import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.singleinstance.SingleInstanceUtils;
import oz.infra.thread.ThreadUtils;

public class SingleInstance {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		long sleepTime = Long.parseLong(args[0]);
		int port = Integer.parseInt(args[1]);
		SingleInstanceUtils.confirmSingleInstanceRun(port);
		ThreadUtils.sleep(sleepTime, Level.INFO);
	}
}