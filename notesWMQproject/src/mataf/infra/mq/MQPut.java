package mataf.infra.mq;

import java.util.logging.Logger;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

import oz.infra.logging.jul.JulUtils;

public class MQPut {

	static int MQOK = 0;
	static int MQWarning = 1;
	static int MQError = 2;
	static int MQConnectorVersion = 101;

	private static Logger logger = JulUtils.getLogger();
	private MQQueueManager MQQM;
	private MQQueue MQQ;
	private MQMessage MQM;

	private MQPutMessageOptions MQPMO;
	private int MQErrorCode;
	private String MQErrorMessage;

	private String MQErrorReason;

	public MQPut() {

	}

	public void appendTextToMessage(String text) {
		try {
			MQM.writeUTF(text);
		} catch (Exception ex) {
			writeErrorCodes(ex);
		}
	}

	public void ClearErrorCodes() {
		this.MQErrorCode = 0;
		this.MQErrorReason = "";
		this.setMQErrorMessage("");
	}

	public void createMessage(String text) {
		try {
			MQM = new MQMessage();
			MQM.format = MQConstants.MQFMT_STRING;
			MQPMO = new MQPutMessageOptions();
			MQM.writeUTF(text);
		} catch (Exception ex) {
			writeErrorCodes(ex);
		}
	}

	public void createMessage(String text, String correlationID, String messageID) {
		try {
			MQM = new MQMessage();
			MQM.format = MQConstants.MQFMT_STRING;
			MQPMO = new MQPutMessageOptions();
			MQM.writeUTF(text);
			if (correlationID != "") {
				MQM.correlationId = correlationID.getBytes();
			}
			if (messageID != "") {
				MQM.messageId = messageID.getBytes();
			}
		} catch (Exception ex) {
			writeErrorCodes(ex);
		}
	}

	public void disconnect() {
		try {
			MQQM.commit();
			MQQM.disconnect();
		} catch (MQException ex) {
			writeMqErrorCodes(ex);
		}
	}

	public int errorCode() {
		return this.MQErrorCode;
	}

	public String errorReason() {
		return this.MQErrorReason;
	}

	public String getMQErrorMessage() {
		return MQErrorMessage;
	}

	public void initialize(String mqHostName, int mqPort, String mqChannel, String mqManager, String mqQname) {
		try {
			this.MQErrorCode = -1;
			this.setMQErrorMessage("Cannot create MQSession, fatal error");
			MQEnvironment.hostname = mqHostName;
			MQEnvironment.port = mqPort;
			MQEnvironment.channel = mqChannel;
			MQEnvironment.properties.put(MQConstants.TRANSPORT_PROPERTY, MQConstants.TRANSPORT_MQSERIES);

			this.MQQM = new MQQueueManager(mqManager);

			int openOptions = MQConstants.MQGMO_FAIL_IF_QUIESCING | MQConstants.MQOO_OUTPUT;
			this.MQQ = MQQM.accessQueue(mqQname, openOptions, null, null, null);
			ClearErrorCodes();
		} catch (MQException ex) {
			writeMqErrorCodes(ex);
		}
	}

	public String put(String text) {
		try {
			MQM = new MQMessage();
			MQM.format = MQConstants.MQFMT_STRING;
			this.MQPMO = new MQPutMessageOptions();
			MQM.writeUTF(text);

			MQQ.put(MQM, MQPMO);
			MQQM.commit();
			return new String(MQM.messageId).trim();
		} catch (MQException ex) {
			writeMqErrorCodes(ex);
		} catch (Exception ex) {
			writeErrorCodes(ex);
		}
		return "";
	}

	public String put(String text, String correlationID, String messageID) {
		try {
			MQM = new MQMessage();
			MQM.format = MQConstants.MQFMT_STRING;
			MQPMO = new MQPutMessageOptions();
			if (correlationID != "") {
				MQM.correlationId = correlationID.getBytes();
			}
			if (messageID != "") {
				MQM.messageId = messageID.getBytes();
			}
			MQM.writeUTF(text);

			MQQ.put(this.MQM, this.MQPMO);
			MQQM.commit();
			return new String(MQM.messageId).trim();
		} catch (MQException ex) {
			writeMqErrorCodes(ex);
		} catch (Exception ex) {
			writeErrorCodes(ex);
		}
		return "";
	}

	public MQMessage sendMessage() {
		try {
			MQQ.put(MQM, MQPMO);
			MQQM.commit();
			return MQM;
		} catch (MQException ex) {
			writeMqErrorCodes(ex);
		}
		return null;
	}

	public void setMQErrorMessage(String mQErrorMessage) {
		MQErrorMessage = mQErrorMessage;
	}

	private void writeErrorCodes(Exception ex) {
		this.MQErrorCode = 1;
		this.MQErrorReason = "1";
		this.setMQErrorMessage(ex.toString());
	}

	private void writeMqErrorCodes(MQException ex) {
		this.MQErrorCode = ex.completionCode;
		this.MQErrorReason = ex.reasonCode + "";
		this.setMQErrorMessage(ex.getMessage());
	}

	public void logError() {
		logger.info("MQErrorCode: " + String.valueOf(MQErrorCode) + " MQErrorMessage: " + MQErrorMessage
				+ " MQErrorReason: " + MQErrorReason);
	}

}