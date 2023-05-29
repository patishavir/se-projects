package oz.infra.mq.httpbridge.test;

import oz.infra.mq.httpbridge.WmqBridge4HttpUtils;

public class TestWmqBridge4HttpUtils {
	public static final String WMQ_BRIDGE_4HTTP_CONTEXT_ROOT = "http://main.intranet.fibi.corp/Http2MQ/msg/queue/";
	public static final String SEND_SMS_QUEUE = "SMS_IN";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testPut();
	}

	private static void testPut() {
		String url = WMQ_BRIDGE_4HTTP_CONTEXT_ROOT + SEND_SMS_QUEUE;
		String message = "<?xml version=\"1.0\" encoding=\"iso-8859-8\"?><T50CSMSM><T50CSMSM-ISURGENT>1</T50CSMSM-ISURGENT><T50CSMSM-DATA>מה קורה נשמה אבודה ויקרה של ברבור  איך ??? </T50CSMSM-DATA><T50CSMSM-CEL-PREF>052</T50CSMSM-CEL-PREF><T50CSMSM-CEL-NUM>4473919</T50CSMSM-CEL-NUM></T50CSMSM>";
		WmqBridge4HttpUtils.put(url, message);
	}
}
