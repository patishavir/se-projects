package oz.temp.nachoom;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

public class mqGetPut {
	static int MQOK = 0;
	static int MQWarning = 1;
	static int MQError = 2;
	int MQNoMessage = 2033;
	static int MQConnectorVersion = 101;

	private MQQueueManager MQQM;
	private MQQueue putMQQ;
	private MQQueue getMQQ;
	private MQMessage putMQM;
	private MQMessage getMQM;
	private MQPutMessageOptions MQPMO;
	private MQGetMessageOptions MQGMO;

	private int MQErrorCode;
	private String MQErrorMessage;
	private String MQErrorReason;

	private boolean fNoMessages;

	private boolean putQueue = false;
	private boolean getQueue = false;

	public mqGetPut() {

	}

	public void initialize(String mqHostName, int mqPort, String mqChannel, String mqManager, String mqQnamePut,
			String mqQnameGet) {
		try {
			this.MQErrorCode = -1;
			this.setMQErrorMessage("לא ניתן ליצור קשר למחשב מרכזי בזמן אתחול");
			MQEnvironment.hostname = mqHostName;
			MQEnvironment.port = mqPort;
			MQEnvironment.channel = mqChannel;
			MQEnvironment.properties.put(MQConstants.TRANSPORT_PROPERTY, MQConstants.TRANSPORT_MQSERIES);

			this.MQQM = new MQQueueManager(mqManager);

			putQueue = true;
			int openOptionsPut = MQConstants.MQGMO_FAIL_IF_QUIESCING | MQConstants.MQOO_OUTPUT;
			this.putMQQ = MQQM.accessQueue(mqQnamePut, openOptionsPut, null, null, null);

			getQueue = true;
			int openOptionsGet = MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_FAIL_IF_QUIESCING;
			this.getMQQ = MQQM.accessQueue(mqQnameGet, openOptionsGet, null, null, null);

			ClearErrorCodes();
		} catch (MQException ex) {
			writeMqErrorCodes(ex);
		}

	}

	public void initializePut(final String mqHostName, int mqPort, String mqChannel, String mqManager, String mqQnamePut) {
		try {
			this.MQErrorCode = -1;
			this.setMQErrorMessage("לא ניתן ליצור קשר למחשב מרכזי בזמן אתחול");
			MQEnvironment.hostname = mqHostName;
			MQEnvironment.port = mqPort;
			MQEnvironment.channel = mqChannel;
			MQEnvironment.properties.put(MQConstants.TRANSPORT_PROPERTY, MQConstants.TRANSPORT_MQSERIES);

			this.MQQM = new MQQueueManager(mqManager);

			putQueue = true;
			int openOptions = MQConstants.MQGMO_FAIL_IF_QUIESCING | MQConstants.MQOO_OUTPUT;
			this.putMQQ = MQQM.accessQueue(mqQnamePut, openOptions, null, null, null);
			getQueue = false;

			ClearErrorCodes();
		} catch (MQException ex) {
			writeMqErrorCodes(ex);
		}

	}

	public void initializeGet(String mqHostName, int mqPort, String mqChannel, String mqManager, String mqQnameGet) {
		try {
			this.MQErrorCode = -1;
			this.setMQErrorMessage("לא ניתן ליצור קשר למחשב מרכזי בזמן אתחול");
			MQEnvironment.hostname = mqHostName;
			MQEnvironment.port = mqPort;
			MQEnvironment.channel = mqChannel;
			MQEnvironment.properties.put(MQConstants.TRANSPORT_PROPERTY, MQConstants.TRANSPORT_MQSERIES);

			this.MQQM = new MQQueueManager(mqManager);

			putQueue = false;
			getQueue = true;
			int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_FAIL_IF_QUIESCING;
			this.getMQQ = MQQM.accessQueue(mqQnameGet, openOptions, null, null, null);

			ClearErrorCodes();
		} catch (MQException ex) {
			writeMqErrorCodes(ex);
		}

	}

	public void createMessage(String text) {
		if (putQueue == true) {
			try {
				putMQM = new MQMessage();
				putMQM.format = MQConstants.MQFMT_STRING;
				// putMQM.characterSet=424;
				MQPMO = new MQPutMessageOptions();
				putMQM.writeUTF(text);
			} catch (Exception ex) {
				writeErrorCodes(ex);
			}
		}
	}

	public void createMessage(String text, String correlationID, String messageID) {
		if (putQueue == true) {
			try {
				putMQM = new MQMessage();
				// putMQM.characterSet=424;
				putMQM.format = MQConstants.MQFMT_STRING;
				MQPMO = new MQPutMessageOptions();
				putMQM.writeUTF(text);
				if (correlationID != "") {
					putMQM.correlationId = correlationID.getBytes();
				}
				if (messageID != "") {
					putMQM.messageId = messageID.getBytes();
				}
			} catch (Exception ex) {
				writeErrorCodes(ex);
			}
		}
	}

	public void appendTextToMessage(String text) {
		if (putQueue == true) {
			try {
				putMQM.writeUTF(text);
			} catch (Exception ex) {
				writeErrorCodes(ex);
			}
		}
	}

	public String sendMessage() {
		if (putQueue == true) {
			try {
				putMQQ.put(putMQM, MQPMO);
				MQQM.commit();
				return new String(putMQM.messageId).trim();
			} catch (MQException ex) {
				writeMqErrorCodes(ex);
			}
		}
		return "";
	}

	public String put(String text, int charSet) {
		if (putQueue == true) {
			try {
				putMQM = new MQMessage();
				// putMQM.characterSet=424;
				putMQM.characterSet = charSet;
				putMQM.format = MQConstants.MQFMT_STRING;
				this.MQPMO = new MQPutMessageOptions();
				// putMQM.writeUTF(text.trim());
				putMQM.writeString(text);

				putMQQ.put(putMQM, MQPMO);
				MQQM.commit();
				return new String(putMQM.messageId).trim();
			} catch (MQException ex) {
				writeMqErrorCodes(ex);
			} catch (Exception ex) {
				writeErrorCodes(ex);
			}
		}
		return "";
	}

	public String put424(String text) {
		return (put(text, 424));
	}

	public String put(String text) {
		return (put(text, 916));
	}

	public String put(String text, String correlationID, String messageID, int charSet) {
		if (putQueue == true) {
			try {
				putMQM = new MQMessage();
				// putMQM.characterSet=424;
				putMQM.characterSet = charSet;
				putMQM.format = MQConstants.MQFMT_STRING;
				MQPMO = new MQPutMessageOptions();
				if (correlationID != "") {
					putMQM.correlationId = correlationID.getBytes();
				}
				if (messageID != "") {
					putMQM.messageId = messageID.getBytes();
				}
				// putMQM.writeUTF(text.trim());
				putMQM.writeString(text);

				putMQQ.put(this.putMQM, this.MQPMO);
				MQQM.commit();
				return new String(putMQM.messageId).trim();
			} catch (MQException ex) {
				writeMqErrorCodes(ex);
			} catch (Exception ex) {
				writeErrorCodes(ex);
			}
		}
		return "";
	}

	public String put(String text, String correlationID, String messageID) {
		return (put(text, correlationID, messageID, 916));
	}

	public String put424(String text, String correlationID, String messageID) {
		return (put(text, correlationID, messageID, 424));
	}

	public void get() {
		if (getQueue == true) {
			try {
				this.setFNoMessages(false);
				this.getMQM = new MQMessage();
				// getMQM.characterSet=424;
				this.MQGMO = new MQGetMessageOptions();
				MQGMO.options = MQConstants.MQGMO_WAIT | MQConstants.MQGMO_FAIL_IF_QUIESCING;
				MQGMO.waitInterval = 5000;
				getMQQ.get(getMQM, MQGMO);

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
	}

	public void get(String correlationID, String messageID) {
		if (getQueue == true) {
			try {
				this.setFNoMessages(false);
				this.getMQM = new MQMessage();
				// getMQM.characterSet=424;
				if (correlationID != "") {
					getMQM.correlationId = correlationID.getBytes();
				}
				if (messageID != "") {
					getMQM.messageId = messageID.getBytes();
				}
				this.MQGMO = new MQGetMessageOptions();
				MQGMO.options = MQConstants.MQGMO_WAIT | MQConstants.MQGMO_FAIL_IF_QUIESCING;
				MQGMO.waitInterval = 5000;
				getMQQ.get(getMQM, MQGMO);

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
	}

	public String read(int BytesAmount) {
		if (getQueue == true) {
			try {
				if (getMQM.getDataLength() < BytesAmount) {
					return (getMQM.readStringOfByteLength(getMQM.getDataLength()));
				} else {
					return (getMQM.readStringOfByteLength(BytesAmount));
				}
			} catch (Exception ex) {
				writeErrorCodes(ex);
			}
		}
		return "";
	}

	public long getDepth() {
		if (getQueue == true) {
			try {
				setFNoMessages(false);
				long depth = getMQQ.getCurrentDepth();
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
		}
		setFNoMessages(true);
		return 0;
	}

	public void setFNoMessages(boolean fNoMessages) {
		this.fNoMessages = fNoMessages;
	}

	public boolean NoMessages() {
		return fNoMessages;
	}

	private void writeMqErrorCodes(MQException ex) {
		this.MQErrorCode = ex.completionCode;
		this.MQErrorReason = ex.reasonCode + "";
		this.setMQErrorMessage(ex.getMessage());
	}

	private void writeErrorCodes(Exception ex) {
		this.MQErrorCode = 1;
		this.MQErrorReason = "1";
		this.setMQErrorMessage(ex.toString());
	}

	public void ClearErrorCodes() {
		this.MQErrorCode = 0;
		this.MQErrorReason = "";
		this.setMQErrorMessage("");
	}

	public int errorCode() {
		return this.MQErrorCode;
	}

	public String errorReason() {
		return this.MQErrorReason;
	}

	public void setMQErrorMessage(String mQErrorMessage) {
		MQErrorMessage = mQErrorMessage;
	}

	public String getMQErrorMessage() {
		return MQErrorMessage;
	}

	public void disconnect() {
		try {
			MQQM.commit();
			MQQM.disconnect();
		} catch (MQException ex) {
			writeMqErrorCodes(ex);
		}
	}

}
