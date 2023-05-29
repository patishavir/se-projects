package oz.infra.mq.httpbridge;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;

public class WmqBridge4HttpUtils {

	private static Logger logger = JulUtils.getLogger();

	public static Outcome put(final String url, final String message) {
		Outcome outcome = Outcome.SUCCESS;
		try {
			URL urlPUT = new URL(url);
			logger.info("starting ... url: " + url + " message: " + message);
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
			if (status != OzConstants.HTTP_STATUS_200) {
				outcome = Outcome.FAILURE;
			}
			http.disconnect();
		} catch (Exception ex) {
			outcome = Outcome.FAILURE;
			ex.printStackTrace();
		}
		return outcome;
	}
}
