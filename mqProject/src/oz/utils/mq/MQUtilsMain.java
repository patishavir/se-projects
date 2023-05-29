package oz.utils.mq;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.MQConstants;

public class MQUtilsMain {
	public static final String HOSTNAME = "hostname";
	public static final String PORT = "port";
	public static final String OUTPUT_QUEUE_NAME = "outputQName";
	public static final String QUEUE_MANAGER = "qManager";
	public static final String CHANNEL = "channel";
	public static final String CCSID = "ccsid";
	public static final String EXPIRY = "expiry";
	public static final String CORRELATIONID = "correlationId";
	public static final String MESSAGEID = "messageId";
	public static final String MESSAGE = "message";
	public static final int DEFAULT_CCSID = 1208;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Properties properties = loadPropertiesFile(args[0], "UTF-8");
		putMessage(properties);
	}

	public static int putMessage(final Properties putMessageProperties) {
		String hostname = putMessageProperties.getProperty(HOSTNAME);
		String portString = putMessageProperties.getProperty(PORT);
		int port = Integer.parseInt(portString);
		String qName = putMessageProperties.getProperty(OUTPUT_QUEUE_NAME);
		String qManager = putMessageProperties.getProperty(QUEUE_MANAGER);
		String channel = putMessageProperties.getProperty(CHANNEL);
		String ccsidString = putMessageProperties.getProperty(CCSID);
		int ccsid = DEFAULT_CCSID;
		if (ccsidString != null) {
			ccsid = Integer.parseInt(ccsidString);
		}
		String expiryString = putMessageProperties.getProperty(EXPIRY);
		String correlationIdString = putMessageProperties.getProperty(CORRELATIONID);
		String messageIdString = putMessageProperties.getProperty(MESSAGEID);
		String message = putMessageProperties.getProperty(MESSAGE);
		int returnCode = -1;

		System.out.println("starting putMessage method\n" + "hostname: " + hostname + " port:" + String.valueOf(port)
				+ " outputQName:" + qName + " qManager:" + qManager + " channel:" + channel + " expiry: " + expiryString
				+ " correlationId: " + correlationIdString + " messageId: " + messageIdString + "\nmessage: "
				+ message);
		try {
			// Create a connection to the QueueManager
			System.out.println("Connecting to queue manager: " + qManager);
			MQEnvironment.hostname = hostname;
			MQEnvironment.port = port;
			MQEnvironment.channel = channel;
			MQEnvironment.CCSID = ccsid;
			MQQueueManager qMgr = new MQQueueManager(qManager);
			// Set up the options on the queue we wish to open
			int openOptions = MQConstants.MQOO_OUTPUT;
			// Now specify the queue that we wish to open and the open options
			System.out.println("Accessing queue: " + qName);
			MQQueue queue = qMgr.accessQueue(qName, openOptions);
			// Define a simple WebSphere MQ Message ...
			MQMessage mqMessage = new MQMessage();
			mqMessage.characterSet = ccsid;

			if (expiryString != null && expiryString.length() > 0) {
				mqMessage.expiry = Integer.parseInt(expiryString);
			}
			if (correlationIdString != null && correlationIdString.length() > 0) {
				mqMessage.correlationId = correlationIdString.getBytes();
			}
			if (messageIdString != null && messageIdString.length() > 0) {
				mqMessage.messageId = messageIdString.getBytes();
			}

			mqMessage.writeString(message);

			// ... and write some text in UTF8 format
			// msg.writeUTF(message);
			// Specify the default put message options
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			pmo.options = CMQC.MQPMO_FAIL_IF_QUIESCING;
			// Put the message to the queue
			System.out.println("Sending a message...");
			queue.put(mqMessage, pmo);
			// Close the queue
			System.out.println("Closing the queue");
			queue.close();
			// Disconnect from the QueueManager
			System.out.println("Disconnecting from the Queue Manager");
			qMgr.disconnect();
			System.out.println("Done!");
			returnCode = 0;
		} catch (MQException mqex) {
			System.out.println("A WebSphere MQ Error occured : Completion Code " + mqex.completionCode + " Reason Code "
					+ mqex.reasonCode + " message: " + mqex.getMessage());
			mqex.printStackTrace();
			for (Throwable t = mqex.getCause(); t != null; t = t.getCause()) {
				System.out.println("... Caused by ");
				t.printStackTrace();
			}
		} catch (java.io.IOException ioex) {
			System.out.println("An IOException has occured: " + ioex.toString());
			ioex.printStackTrace();
		}
		return returnCode;

	}

	public static Properties loadPropertiesFile(final String propertiesFilePath, final String charsetString) {
		Properties properties = new Properties();
		try {
			InputStream inputStream = new FileInputStream(propertiesFilePath);
			properties.load(new InputStreamReader(inputStream, charsetString));
		} catch (Exception ex) {
			ex.printStackTrace();
			properties = null;
		}
		return properties;
	}
}
