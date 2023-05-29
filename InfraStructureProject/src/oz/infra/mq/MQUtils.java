package oz.infra.mq;

import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.MQConstants;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
// import static oz.infra.mq.MQPropertiesKeys.*;
import static oz.infra.mq.MQPropertiesKeys.*;

public class MQUtils {

	private static Logger logger = JulUtils.getLogger();

	private MQUtils() {
	}

	public static int putMessage(final Properties putMessageProperties) {
		String hostname = putMessageProperties.getProperty(MQPropertiesKeys.HOSTNAME);
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
		String message = putMessageProperties.getProperty(MESSAGE);
		int returnCode = -1;

		logger.info("starting putMessage method\n" + "hostname: " + hostname + " port:" + String.valueOf(port)
				+ " outputQName:" + qName + " qManager:" + qManager + " channel:" + channel + "\nmessage: " + message
				+ " expiry: " + expiryString);
		try {
			// Create a connection to the QueueManager
			logger.info("Connecting to queue manager: " + qManager);
			MQEnvironment.hostname = hostname;
			MQEnvironment.port = port;
			MQEnvironment.channel = channel;
			MQEnvironment.CCSID = ccsid;
			MQQueueManager qMgr = new MQQueueManager(qManager);
			// Set up the options on the queue we wish to open
			int openOptions = MQConstants.MQOO_OUTPUT;
			// Now specify the queue that we wish to open and the open options
			logger.info("Accessing queue: " + qName);
			MQQueue queue = qMgr.accessQueue(qName, openOptions);
			// Define a simple WebSphere MQ Message ...
			MQMessage mqMessage = new MQMessage();
			mqMessage.characterSet = ccsid;

			if (expiryString != null && expiryString.length() > 0) {
				mqMessage.expiry = Integer.parseInt(expiryString);
			}
			mqMessage.writeString(message);

			// ... and write some text in UTF8 format
			// msg.writeUTF(message);
			// Specify the default put message options
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			pmo.options = CMQC.MQPMO_FAIL_IF_QUIESCING;
			// Put the message to the queue
			logger.info("Sending a message...");
			queue.put(mqMessage, pmo);
			// Close the queue
			logger.info("Closing the queue");
			queue.close();
			// Disconnect from the QueueManager
			logger.info("Disconnecting from the Queue Manager");
			qMgr.disconnect();
			logger.info("Done!");
			returnCode = 0;
		} catch (MQException mqex) {
			logger.warning("A WebSphere MQ Error occured : Completion Code " + mqex.completionCode + " Reason Code "
					+ mqex.reasonCode + " message: " + mqex.getMessage());
			mqex.printStackTrace();
			for (Throwable t = mqex.getCause(); t != null; t = t.getCause()) {
				logger.info("... Caused by ");
				t.printStackTrace();
			}
		} catch (java.io.IOException ioex) {
			logger.info("An IOException has occured: " + ioex.toString());
			ExceptionUtils.printMessageAndStackTrace(ioex);
		}
		return returnCode;

	}

	public static int putMessage(final String hostname, final int port, final String qName, final String qManager,
			final String channel, final int ccsid, final String message, final Properties... writeMessageProperties) {
		int returnCode = -1;
		logger.info("starting putMessage method\n" + "hostname: " + hostname + " port:" + String.valueOf(port)
				+ " outputQName:" + qName + " qManager:" + qManager + " channel:" + channel + "\nmessage: " + message);
		try {
			// Create a connection to the QueueManager
			logger.info("Connecting to queue manager: " + qManager);
			MQEnvironment.hostname = hostname;
			MQEnvironment.port = port;
			MQEnvironment.channel = channel;
			MQEnvironment.CCSID = ccsid;
			MQQueueManager qMgr = new MQQueueManager(qManager);
			// Set up the options on the queue we wish to open
			int openOptions = MQConstants.MQOO_OUTPUT;
			// Now specify the queue that we wish to open and the open options
			logger.info("Accessing queue: " + qName);
			MQQueue queue = qMgr.accessQueue(qName, openOptions);
			// Define a simple WebSphere MQ Message ...
			MQMessage mqMessage = new MQMessage();
			mqMessage.characterSet = ccsid;
			mqMessage.writeString(message);

			// ... and write some text in UTF8 format
			// msg.writeUTF(message);
			// Specify the default put message options
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			pmo.options = CMQC.MQPMO_FAIL_IF_QUIESCING;
			// Put the message to the queue
			logger.info("Sending a message...");
			queue.put(mqMessage, pmo);
			// Close the queue
			logger.info("Closing the queue");
			queue.close();
			// Disconnect from the QueueManager
			logger.info("Disconnecting from the Queue Manager");
			qMgr.disconnect();
			logger.info("Done!");
			returnCode = 0;
		} catch (MQException mqex) {
			logger.warning("A WebSphere MQ Error occured : Completion Code " + mqex.completionCode + " Reason Code "
					+ mqex.reasonCode + " message: " + mqex.getMessage());
			mqex.printStackTrace();
			for (Throwable t = mqex.getCause(); t != null; t = t.getCause()) {
				logger.info("... Caused by ");
				t.printStackTrace();
			}
		} catch (java.io.IOException ioex) {
			logger.info("An IOException has occured: " + ioex.toString());
			ExceptionUtils.printMessageAndStackTrace(ioex);
		}
		return returnCode;
	}
}
