package oz.temp.sms;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class MqBridgeSample {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			URL urlPUT = new URL("http://main.intranet.fibi.corp/Http2MQ/msg/queue/SMS_IN");
			String message = "<?xml version=\"1.0\" encoding=\"iso-8859-8\"?><T50CSMSM><T50CSMSM-ISURGENT>1</T50CSMSM-ISURGENT><T50CSMSM-DATA>Hi FIBI Test ezuzi # 2 </T50CSMSM-DATA><T50CSMSM-CEL-PREF>052</T50CSMSM-CEL-PREF><T50CSMSM-CEL-NUM>4473919</T50CSMSM-CEL-NUM>		</T50CSMSM>";

			logger.info("Try to write message ... ");
			HttpURLConnection http = (HttpURLConnection) urlPUT.openConnection();
			http.setDoInput(true);
			http.setDoOutput(true);
			http.setRequestMethod("POST");
			http.setConnectTimeout(1000);
			http.setReadTimeout(1000);
			http.setRequestProperty("x-msg-require-headers", "ALL");
			http.setRequestProperty("x-msg-expiry", "60000");
			// http.setRequestProperty("x-msg-replyTo",
			// "/msg/queue/T05.GAS_MF2LN");
			http.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
			OutputStream os = http.getOutputStream();

			os.write(message.getBytes("utf-8"));
			// Read Response
			Map<String, List<String>> responseHeader = http.getHeaderFields();
			List<String> msgID = responseHeader.get("x-msg-msgId");
			int status = http.getResponseCode();
			logger.info("status: " + String.valueOf(status));
			http.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
