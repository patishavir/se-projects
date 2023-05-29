package mataf.infra.mq;

import java.io.File;
import java.util.logging.Logger;

import com.ibm.mq.MQMessage;

import oz.infra.bytes.ByteUtils;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;
import oz.infra.thread.ThreadUtils;

public class MQMain {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePath = args[0];
		File file = new File(filePath);
		String messageText = FileUtils.readTextFile(file);
		putGet(messageText);
	}

	public static String putGet(final String messageText) {
		MQMessage mqMessage = put(messageText);
		ThreadUtils.sleep(1000L);
		get(mqMessage);
		return null;
	}

	private static MQMessage put(final String messageText) {
		MQPut mqPut = new MQPut();
		mqPut.initialize(MQParameters.getMqHostName(), MQParameters.getPort(), MQParameters.getMqChannel(),
				MQParameters.getMqManager(), MQParameters.getMqQnameto());
		mqPut.createMessage(messageText);
		MQMessage mqMessage = mqPut.sendMessage();
		mqPut.logError();
		// logger.info("correlationId: " + new String(mqMessage.correlationId) +
		// " messageId: "
		// + new String(mqMessage.messageId) + SystemUtils.LINE_SEPARATOR +
		// "totol message length: "
		// + mqMessage.getTotalMessageLength());
		logger.info("correlationId: " + ByteUtils.byteArrayToHexString(mqMessage.correlationId) + " messageId: "
				+ ByteUtils.byteArrayToHexString(mqMessage.messageId) + SystemUtils.LINE_SEPARATOR
				+ "totol message length: " + mqMessage.getTotalMessageLength());
		return mqMessage;
	}

	private static void get(final MQMessage mqMessage) {
		MQGet mqGet = new MQGet();
		mqGet.initialize(MQParameters.getMqHostName(), MQParameters.getPort(), MQParameters.getMqChannel(),
				MQParameters.getMqManager(), MQParameters.getMqQnameFrom());
		mqGet.get(mqMessage.correlationId, mqMessage.messageId);

	}
}