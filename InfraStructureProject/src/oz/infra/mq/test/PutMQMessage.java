package oz.infra.mq.test;

import oz.infra.mq.MQUtils;

public class PutMQMessage {
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		String userName = System.getProperties().getProperty("user.name");
		String hostname = "localhost";
		int port = 1414;
		String qManager = "Qmgr01";
		String outputQName = "Q01";
		String channel = "SRVCONN_CHAN";
		String message = "Hello WMQ";
		int ccsid = 862;
		if (!userName.equalsIgnoreCase("oded")) {
			hostname = "matafmqt";
			port = 1416;
			outputQName = "gm.dev.events";
			qManager = "Q538GT00";
			channel = "CHAN1";
			message = "Hello WMQ Q538GT00";
			ccsid = 862;
		}
		MQUtils.putMessage(hostname, port, outputQName, qManager, channel, ccsid, message);
	}
}
