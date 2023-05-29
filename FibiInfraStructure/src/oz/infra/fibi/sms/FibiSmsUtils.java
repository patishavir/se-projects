package oz.infra.fibi.sms;

import oz.infra.fibi.FibiParams;
import oz.infra.mq.httpbridge.WmqBridge4HttpUtils;
import oz.infra.operaion.Outcome;
import oz.infra.varargs.VarArgsUtils;

public class FibiSmsUtils {

	public static Outcome sendSms(final String messageTest, final String numberPrefix, final String number,
			final String... urls) {
		String url = VarArgsUtils.getMyArg(FibiParams.WMQ_BRIDGE_4HTTP_URL_PROD + FibiParams.SEND_SMS_QUEUE, urls);
		SmsMessage smsMessage = new SmsMessage(messageTest, numberPrefix, number);
		String message = smsMessage.getAsString();
		Outcome outcome =  WmqBridge4HttpUtils.put(url, message);
		return outcome;
		
	}
}
