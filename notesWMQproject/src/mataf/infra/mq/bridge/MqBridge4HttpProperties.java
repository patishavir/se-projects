package mataf.infra.mq.bridge;

public class MqBridge4HttpProperties {

	private String message = "<?xml version='1.0' encoding='UTF-8'?><T05C0GAK><HEADERK><USERIDK></USERIDK><TACHANAK>11</TACHANAK><BANKPAKIDK>31</BANKPAKIDK><SNIFPAKIDK>001</SNIFPAKIDK><BANKK>031</BANKK><SNIFK>001</SNIFK><MCHK>110086</MCHK><SCHK>105</SCHK><MATBEAK>001</MATBEAK><GILAYONK>10</GILAYONK><MFTDATAK>01</MFTDATAK></HEADERK><T05C0RSH><RSHAGRRK>2</RSHAGRRK><RSHBANKK>031</RSHBANKK><RSHSNIFK>001</RSHSNIFK><RSHMCHK>110086</RSHMCHK></T05C0RSH><T05C0QOT><QOTMATBEAK><QOTMATK><QOTTRK>20160511</QOTTRK><QOTSUGMATK>001</QOTSUGMATK><QOTBANKK>031</QOTBANKK></QOTMATK><QOTMATK><QOTTRK>20160511</QOTTRK><QOTSUGMATK>020</QOTSUGMATK><QOTBANKK>031</QOTBANKK></QOTMATK></QOTMATBEAK><QOTDATESK><QOTWORKDAYK>20160511</QOTWORKDAYK><QOTRIVONENDK>20160511</QOTRIVONENDK><QOTYEARENDK>20160511</QOTYEARENDK></QOTDATESK><QOTKFK></QOTKFK></T05C0QOT><T05C0CIF><CIFBANKK>031</CIFBANKK><CIFSNIFK>001</CIFSNIFK><CIFMCHK>110086</CIFMCHK></T05C0CIF><T05C0OBL><OBLAGRRK>2</OBLAGRRK><OBLBANKK>031</OBLBANKK><OBLSNIFK>001</OBLSNIFK><OBLMCHK>110086</OBLMCHK></T05C0OBL></T05C0GAK>";
	private String urlGET = "http://s5381355.fibi.corp/MQBridgeQ/msg/queue/T05.GAS_MF2LN";
	private String urlPUT = "http://s5381355.fibi.corp/MQBridgeQ/msg/queue/T05.GAS_LN2MF";
	private String replyTo = "/msg/queue/T05.GAS_MF2LN";
	private String expiry = "60000";
	private String connectTimeOutString = "1000";
	private String readTimeOutString = "1000";
	private String wait = "10000";

	public String getConnectTimeOutString() {
		return connectTimeOutString;
	}

	public String getExpiry() {
		return expiry;
	}

	public String getMessage() {
		return message;
		// return "xxx";
	}

	public String getReadTimeOutString() {
		return readTimeOutString;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public String getUrlGET() {
		return urlGET;
	}

	public String getUrlPUT() {
		return urlPUT;
	}

	public String getWait() {
		return wait;
	}
}
