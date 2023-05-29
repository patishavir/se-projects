package oz.infra.mq;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.system.SystemUtils;

public final class MQUtilsKeep {
	public static final String HOSTNAME = "hostname";
	public static final String PORT = "port";
	public static final String OUTPUT_QUEUE_NAME = "outputQName";
	public static final String QUEUE_MANAGER = "qManager";
	public static final String CHANNEL = "channel";
	public static final String CCSID = "ccsid";
	public static final String MESSAGE = "message";

	private static final int DEFAULT_CCSID = 1208;
	private static Logger logger = JulUtils.getLogger();

	private MQUtilsKeep() {
	}

	public static int putMessage(final Properties writeMessageProperties) {
		String hostname = writeMessageProperties.getProperty(HOSTNAME);
		String portString = writeMessageProperties.getProperty(PORT);
		int port = Integer.parseInt(portString);
		String outputQName = writeMessageProperties.getProperty(OUTPUT_QUEUE_NAME);
		String qManager = writeMessageProperties.getProperty(QUEUE_MANAGER);
		String channel = writeMessageProperties.getProperty(CHANNEL);
		String ccsidString = writeMessageProperties.getProperty(CCSID);
		int ccsid = DEFAULT_CCSID;
		if (ccsidString != null) {
			ccsid = Integer.parseInt(ccsidString);
		}
		String message = writeMessageProperties.getProperty(MESSAGE);
		return putMessage(hostname, port, outputQName, qManager, channel, ccsid, message, writeMessageProperties);
	}

	public static int putMessage(final String hostname, final int port, final String outputQName,
			final String qManager, final String channel, final int ccsid, final String message,
			final Properties... writeMessageProperties) {
		int returnCode = -1;
		logger.info("hostname: " + hostname + " port:" + String.valueOf(port) + " outputQName:" + outputQName
				+ " qManager:" + qManager + " channel:" + channel + "\nmessage: " + message);
		try {
			logger.fine("starting " + SystemUtils.getCurrentMethodName());
			// Create a connection to the queue manager
			MQEnvironment.hostname = hostname;
			MQEnvironment.port = port;
			MQEnvironment.channel = channel;
			MQEnvironment.CCSID = ccsid;
			MQQueueManager qMgr = new MQQueueManager(qManager);
			// the options on the queue
			int openOptions = MQC.MQOO_OUTPUT;
			// specify the queue
			MQQueue queue = qMgr.accessQueue(outputQName, openOptions, null, null, null);
			// Define a simple message, and write some text in String format:
			MQMessage mqMessage = new MQMessage();
			mqMessage.characterSet = ccsid;
			// mqMessage.writeUTF(message);
			mqMessage.writeString(message);
			// specify the message options...
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			// accept
			// put the message on the queue
			queue.put(mqMessage, pmo);
			// Close the queue...
			queue.close();
			// Disconnect from the queue manager
			qMgr.disconnect();
			logger.info(SystemUtils.getCallerMethodName() + " has completed successfully");
			returnCode = 0;
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			if (writeMessageProperties.length > 0) {
				PropertiesUtils.printProperties(writeMessageProperties[0], Level.WARNING);
			}
		}
		return returnCode;
	}
}
