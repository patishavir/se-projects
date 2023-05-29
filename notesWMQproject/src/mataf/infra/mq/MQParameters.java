package mataf.infra.mq;

public class MQParameters {
	private static String mqHostName = "matafmqt";
	private static int port = 1417;
	private static String mqChannel = "SERVER.CHANNEL";
	private static String mqManager = "Q538ML02";
	private static String mqQnameto = "T05.GAS_LN2MF";
	private static String mqQnameFrom = "T05.GAS_MF2LN";
	public static String getMqChannel() {
		return mqChannel;
	}
	public static String getMqHostName() {
		return mqHostName;
	}
	public static String getMqManager() {
		return mqManager;
	}
	public static String getMqQnameFrom() {
		return mqQnameFrom;
	}
	public static String getMqQnameto() {
		return mqQnameto;
	}
	public static int getPort() {
		return port;
	}
	public static void setMqChannel(String mqChannel) {
		MQParameters.mqChannel = mqChannel;
	}
	public static void setMqHostName(String mqHostName) {
		MQParameters.mqHostName = mqHostName;
	}
	public static void setMqManager(String mqManager) {
		MQParameters.mqManager = mqManager;
	}
	public static void setMqQnameFrom(String mqQnameFrom) {
		MQParameters.mqQnameFrom = mqQnameFrom;
	}
	public static void setMqQnameto(String mqQnameto) {
		MQParameters.mqQnameto = mqQnameto;
	}
	public static void setPort(int port) {
		MQParameters.port = port;
	}
}