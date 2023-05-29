package mataf.infra.mq.bridge.sample;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class MqBridge4HttpSample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String message = "<?xml version='1.0' encoding='UTF-8'?><T05C0GAK><HEADERK><USERIDK></USERIDK><TACHANAK>11</TACHANAK><BANKPAKIDK>31</BANKPAKIDK><SNIFPAKIDK>001</SNIFPAKIDK><BANKK>031</BANKK><SNIFK>001</SNIFK><MCHK>110086</MCHK><SCHK>105</SCHK><MATBEAK>001</MATBEAK><GILAYONK>10</GILAYONK><MFTDATAK>01</MFTDATAK></HEADERK><T05C0RSH><RSHAGRRK>2</RSHAGRRK><RSHBANKK>031</RSHBANKK><RSHSNIFK>001</RSHSNIFK><RSHMCHK>110086</RSHMCHK></T05C0RSH><T05C0QOT><QOTMATBEAK><QOTMATK><QOTTRK>20160511</QOTTRK><QOTSUGMATK>001</QOTSUGMATK><QOTBANKK>031</QOTBANKK></QOTMATK><QOTMATK><QOTTRK>20160511</QOTTRK><QOTSUGMATK>020</QOTSUGMATK><QOTBANKK>031</QOTBANKK></QOTMATK></QOTMATBEAK><QOTDATESK><QOTWORKDAYK>20160511</QOTWORKDAYK><QOTRIVONENDK>20160511</QOTRIVONENDK><QOTYEARENDK>20160511</QOTYEARENDK></QOTDATESK><QOTKFK></QOTKFK></T05C0QOT><T05C0CIF><CIFBANKK>031</CIFBANKK><CIFSNIFK>001</CIFSNIFK><CIFMCHK>110086</CIFMCHK></T05C0CIF><T05C0OBL><OBLAGRRK>2</OBLAGRRK><OBLBANKK>031</OBLBANKK><OBLSNIFK>001</OBLSNIFK><OBLMCHK>110086</OBLMCHK></T05C0OBL></T05C0GAK>";
			// URL urlPUT = new
			// URL("http://s5381355.fibi.corp/MQBridge/msg/queue/T05.GAS_LN2MF");
			// URL urlGET = new
			// URL("http://s5381355.fibi.corp/MQBridge/msg/queue/T05.GAS_MF2LN");
			URL urlPUT = new URL("http://s5381355.fibi.corp/MQBridgeQ/msg/queue/T05.GAS_LN2MF");
			URL urlGET = new URL("http://s5381355.fibi.corp/MQBridgeQ/msg/queue/T05.GAS_MF2LN");
			System.out.println("message: " + message);
			System.out.println("Try to write message ... ");
			HttpURLConnection http = (HttpURLConnection) urlPUT.openConnection();
			http.setDoInput(true);
			http.setDoOutput(true);
			http.setRequestMethod("POST");
			http.setConnectTimeout(1000);
			http.setReadTimeout(1000);
			http.setRequestProperty("x-msg-require-headers", "ALL");
			http.setRequestProperty("x-msg-expiry", "60000");
			http.setRequestProperty("x-msg-replyTo", "/msg/queue/T05.GAS_MF2LN");
			http.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
			OutputStream os = http.getOutputStream();

			os.write(message.getBytes("utf-8"));

			// Read Response
			Map<String, List<String>> responseHeader = http.getHeaderFields();
			List<String> msgID = responseHeader.get("x-msg-msgId");
			int status = http.getResponseCode();
			http.disconnect();
			// Read Response
			if (status == 200 && msgID != null) {
				System.out.println("PUT message id is: " + msgID.get(0));
				System.out.println("Try to read response ...");
				http = (HttpURLConnection) urlGET.openConnection();
				http.setDoInput(true);
				http.setRequestMethod("DELETE");
				http.setRequestProperty("x-msg-msgId", msgID.get(0));
				http.setRequestProperty("x-msg-wait", "10000");
				// System.out.println(http.getResponseCode());
				responseHeader = http.getHeaderFields();
				// System.out.println(responseHeader);
				byte[] b = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int k;
				InputStream is = http.getInputStream();
				while ((k = is.read(b)) > 0) {
					baos.write(b, 0, k);
				}
				System.out.println("Response is: " + new String(baos.toByteArray(), "utf-8"));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}