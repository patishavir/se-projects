package oz.samples.mq;

import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.constants.CMQC;

public class SendReceiveSample {
	public static void sendReceive() {
		try {
			// Send request message
			MQMessage requestMessage = new MQMessage();
			requestMessage.writeUTF("Request Message");
			MQQueue reqQueue = null;
			reqQueue.put(requestMessage);
			// Receive response
			MQMessage responseMessage = new MQMessage();
			responseMessage.correlationId = requestMessage.messageId;
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.matchOptions = CMQC.MQMO_MATCH_CORREL_ID;
			MQQueue respQueue = null;
			respQueue.get(responseMessage, gmo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
