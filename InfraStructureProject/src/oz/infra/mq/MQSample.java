package oz.infra.mq;

// ====================================================================== 
// Licensed Materials - Property of IBM 
// 5639-C34 
// (c) Copyright IBM Corp. 1995, 2002 
// ====================================================================== 
// WebSphere MQ classes for Java sample application 
// 
// This sample runs as a Java application using the command :- java MQSample 
import com.ibm.mq.MQC;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

public class MQSample {
	private String qManager = "your_Q_manager";
	// define name of queue manager to connect to.
	private MQQueueManager qMgr;

	// define a queue manager object
	public static void main(final String[] args) {
		new MQSample();
	}

	public MQSample() {
		try { // Create a connection to the queue manager
			qMgr = new MQQueueManager(qManager);
			// Set up the options on the queue we wish to open...
			// Note. All WebSphere MQ Options are prefixed with MQC in Java.
			int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT;
			// Now specify the queue that we wish to open,
			// and the open options...
			MQQueue system_default_local_queue = qMgr.accessQueue("SYSTEM.DEFAULT.LOCAL.QUEUE",
					openOptions);
			// Define a simple WebSphere MQ message, and write some text in UTF
			// format..
			MQMessage hello_world = new MQMessage();
			hello_world.writeUTF("Hello World!");
			// specify the message options...
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			// accept the
			// defaults,
			// same as MQPMO_DEFAULT

			// put the message on the queue
			system_default_local_queue.put(hello_world, pmo);
			// get the message back again...
			// First define a WebSphere MQ message buffer to receive the message
			// into..
			MQMessage retrievedMessage = new MQMessage();
			retrievedMessage.messageId = hello_world.messageId;
			// Set the get message options...
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			// accept the defaults
			// same as MQGMO_DEFAULT
			// get the message off the queue...
			system_default_local_queue.get(retrievedMessage, gmo);
			// And prove we have the message by displaying the UTF message text
			String msgText = retrievedMessage.readUTF();
			System.out.println("The message is: " + msgText);
			// Close the queue...
			system_default_local_queue.close();
			// Disconnect from the queue manager
			qMgr.disconnect();
		}
		// If an error has occurred in the above, try to identify what went
		// wrong
		// Was it a WebSphere MQ error?
		catch (MQException ex) {
			System.out.println("A WebSphere MQ error occurred : Completion code "
					+ ex.completionCode + " Reason code " + ex.reasonCode);
		} // Was it a Java buffer space error?
		catch (java.io.IOException ex) {
			System.out.println("An error occurred whilst writing to the message buffer: " + ex);
		}
	}
}
// end of sample
