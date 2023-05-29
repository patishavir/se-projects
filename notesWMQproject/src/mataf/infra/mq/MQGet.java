package mataf.infra.mq;

import java.util.logging.Logger;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

import oz.infra.logging.jul.JulUtils;

public class MQGet {

	static int MQOK = 0;
	static int MQWarning = 1;
	static int MQError = 2;
	static int MQConnectorVersion = 101;
	private static Logger logger = JulUtils.getLogger();

	int MQNoMessage = 2033;
	private MQQueueManager MQQM;
	private MQQueue MQQ;
	private MQMessage MQM;

	private MQGetMessageOptions MQGMO;
	private int MQErrorCode;
	private String MQErrorMessage;
	private String MQErrorReason;
	private boolean fNoMessages;

	public MQGet() {

	}

	public void ClearErrorCodes() {
		this.MQErrorCode = 0;
		this.MQErrorReason = "";
		this.setMQErrorMessage("");
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

	public void get() {
		try {
			this.setFNoMessages(false);
			this.MQM = new MQMessage();
			this.MQGMO = new MQGetMessageOptions();
			MQGMO.options = MQConstants.MQGMO_WAIT | MQConstants.MQGMO_FAIL_IF_QUIESCING;
			MQGMO.waitInterval = 5000;
			MQQ.get(MQM, MQGMO);

		} catch (MQException ex) {
			if (ex.reasonCode == this.MQNoMessage) {
				this.setFNoMessages(true);
			} else {
				writeMqErrorCodes(ex);
			}
		} catch (Exception ex) {
			writeErrorCodes(ex);
		}
	}

	public void get(final byte[] correlationID, final byte[] messageID) {
		try {
			this.setFNoMessages(false);
			this.MQM = new MQMessage();
			// if (correlationID != "") {
			// MQM.correlationId = correlationID.getBytes();
			// }
			MQM.correlationId = MQConstants.MQCI_NONE;
			if (messageID != null) {
				MQM.messageId = messageID;
			}
			this.MQGMO = new MQGetMessageOptions();
			MQGMO.options = MQConstants.MQGMO_WAIT | MQConstants.MQGMO_FAIL_IF_QUIESCING;
			MQGMO.waitInterval = 5000;
			MQQ.get(MQM, MQGMO);
			logger.info(MQM.readUTF());
		} catch (MQException ex) {
			if (ex.reasonCode == this.MQNoMessage) {
				this.setFNoMessages(true);
			} else {
				writeMqErrorCodes(ex);
			}
		} catch (Exception ex) {
			writeErrorCodes(ex);
		}
	}

	public long getDepth() {
		try {
			setFNoMessages(false);
			long depth = MQQ.getCurrentDepth();
			if (depth == 0) {
				setFNoMessages(true);
			}
			return depth;
		} catch (MQException ex) {
			if (ex.reasonCode == this.MQNoMessage) {
				this.setFNoMessages(true);
			} else {
				writeMqErrorCodes(ex);
			}
		} catch (Exception ex) {
			writeErrorCodes(ex);
		}

		setFNoMessages(true);
		return 0;
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

			int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_FAIL_IF_QUIESCING;
			this.MQQ = MQQM.accessQueue(mqQname, openOptions, null, null, null);
			ClearErrorCodes();
		} catch (MQException ex) {
			writeMqErrorCodes(ex);
		}
	}

	public boolean NoMessages() {
		return fNoMessages;
	}

	public String read(int BytesAmount) {
		try {
			if (MQM.getDataLength() < BytesAmount) {
				return (MQM.readStringOfByteLength(MQM.getDataLength()));
			} else {
				return (MQM.readStringOfByteLength(BytesAmount));
			}
		} catch (Exception ex) {
			writeErrorCodes(ex);
		}
		return "";
	}

	public void setFNoMessages(boolean fNoMessages) {
		this.fNoMessages = fNoMessages;
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
}