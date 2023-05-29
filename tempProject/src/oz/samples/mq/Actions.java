/*
 * Example WebSphere MQ LoadRunner script (written in Java)
 * 
 * Script Description: 
 *     This script puts a message on a queue, then gets a response message from 
 *     another queue.
 *
 * You will probably need to add the following jar files to your classpath
 *   - com.ibm.mq.jar
 *   - connector.jar
 *   - com.ibm.mq.jmqi.jar
 *   - com.ibm.mq.headers.jar
 *   - com.ibm.mq.commonservices.jar
 */

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

public class Actions {
	// Variables used by more than one method
	String queueMgrName = "TESTQMGR";
	String putQueueName = "TEST.INBOUND.QUEUE";
	String getQueueName = "TEST.REPLY.QUEUE";

	MQQueueManager queueMgr = null;
	MQQueue getQueue = null;
	MQQueue putQueue = null;
	MQPutMessageOptions pmo = new MQPutMessageOptions();
	MQGetMessageOptions gmo = new MQGetMessageOptions();
	MQMessage requestMsg = new MQMessage();
	MQMessage responseMsg = new MQMessage();
	String msgBody = null;

	public int init() throws Throwable {
		// Open a connection to the queue manager and the put/get queues
		try {
			// As values set in the MQEnvironment class take effect when the
			// MQQueueManager constructor is called, you must set the values
			// in the MQEnvironment class before you construct an MQQueueManager
			// object.
			MQEnvironment.hostname = "mqsvr.myloadtest.com";
			MQEnvironment.port = 1414;
			MQEnvironment.channel = "WMQTOOL.ADMIN.CLIENT";
			queueMgr = new MQQueueManager(queueMgrName);

			// Access the put/get queues. Note the open options used.
			putQueue = queueMgr.accessQueue(putQueueName, MQC.MQOO_BIND_NOT_FIXED | MQC.MQOO_OUTPUT);
			getQueue = queueMgr.accessQueue(getQueueName, MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT);
		} catch (Exception e) {
			lr.error_message("Error connecting to queue manager or accessing queues.");
			lr.exit(lr.EXIT_VUSER, lr.FAIL);
		}

		return 0;
	}// end of init

	public int action() throws Throwable {
        // This is an XML message that will be put on the queue. Could do some fancy 
        // things with XML classes here if necessary.
        // The message string can contain {parameters} if lr.eval_string() is used.
        msgBody = lr.eval_string("<TestMessage><value>{OrderNum}<value></TestMessage>"); 
 
        // Clear the message objects on each iteration.
        requestMsg.clearMessage();
        responseMsg.clearMessage();
 
        // Create a message object and put it on the request queue
        lr.start_transaction("test_message");
        try {
            pmo.options = MQC.MQPMO_NEW_MSG_ID; // The queue manager replaces the contents of the MsgId field in MQMD with a new message identifier.
            requestMsg.replyToQueueName = getQueueName; // the response should be put on this queue
            requestMsg.report=MQC.MQRO_PASS_MSG_ID; //If a report or reply is generated as a result of this message, the MsgId of this message is copied to the MsgId of the report or reply message.
            requestMsg.format = MQC.MQFMT_STRING; // Set message format. The application message data can be either an SBCS string (single-byte character set), or a DBCS string (double-byte character set). 
            requestMsg.messageType=MQC.MQMT_REQUEST; // The message is one that requires a reply.
            requestMsg.writeString(msgBody); // message payload
            putQueue.put(requestMsg, pmo);
        } catch(Exception e) {
        	lr.error_message("Error sending message.");
        	lr.exit(lr.EXIT_VUSER, lr.FAIL);
        }
 
        // Get the response message object from the response queue
        try {
            responseMsg.correlationId = requestMsg.messageId; // The Id to be matched against when getting a message from a queue
            gmo.matchOptions=MQC.MQMO_MATCH_CORREL_ID; // The message to be retrieved must have a correlation identifier that matches the value of the CorrelId field in the MsgDesc parameter of the MQGET call.
            gmo.options=MQC.MQGMO_WAIT; // The application waits until a suitable message arrives.
            gmo.waitInterval=60000; // timeout in ms
            getQueue.get(responseMsg, gmo);
 
            // Check the message content
            byte[] responseMsgData = responseMsg.readStringOfByteLength(responseMsg.getTotalMessageLength()).getBytes();
            String msg = new String(responseMsgData);
            lr.output_message(msg); // for debugging. Disable this for a load test.
            // TODO: add your own message checking here using string functions.
            // I have found that extracting XML fields and comparing them (rather than 
            // comparing the whole message body or substrings) is more resistant to change.
            // If no match is found, then lr.error_message() and lr.exit().
        } catch() {
            lr.error_message("Error receiving message.");
            lr.exit(lr.EXIT_VUSER, lr.FAIL);
        }
        lr.end_transaction("test_message", lr.AUTO);
 
        return 0;
    }//end of action

	public int end() throws Throwable {
		// Close all the connections
		try {
			putQueue.close();
			getQueue.close();
			queueMgr.close();
		} catch (Exception e) {
			lr.error_message("Exception in closing the connections");
			lr.exit(lr.EXIT_VUSER, lr.FAIL);
		}

		return 0;
	}// end of end
}