import java.util.logging.Logger;

import com.ibm.mq.MQC;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;

public class write {
	public void write(String msgString, boolean syncpoint, String replyToQueueName) {
		MilliTimer timer = null;

		if (m_loggingOn) {
			Logger.log("Queue.write()", Logger.DEBUG);
			timer = new MilliTimer();
		}

		MQQueue queue = null;
		byte[] correlationID = null;

		try {
			if (m_disconnect || m_tryingAlternate) {
				init(m_hostName, m_channel, m_queueMgrName, m_queueName);
				m_tryingAlternate = false;
			}

			queue = new MQQueue(m_queueMgr, m_queueName, MQC.MQOO_OUTPUT, null, null, null);

			MQMessage msg = new MQMessage();

			msg.format = MQC.MQFMT_STRING;

			msg.expiry = m_expiry;

			msg.writeString(msgString);

			correlationID = createUUID();

			System.out.println("write -1: correlationID is: " + correlationID);

			msg.correlationId = correlationID;

			System.out.println("write -2 " + msg.correlationId);

			MQPutMessageOptions pmo = new MQPutMessageOptions();

			if (syncpoint) {
				pmo.options = pmo.options | MQC.MQPMO_SYNCPOINT;
			}

			msg.report = MQC.MQRO_PASS_CORREL_ID;
			msg.replyToQueueName = replyToQueueName;

			queue.put(msg, pmo);

			System.out.println("write -3 " + msg.correlationId);

			if (m_loggingOn) {
				timer.stop();
				Logger.log("elapsed time for Queue.write() = [" + timer.elapsed() + "]", Logger.DEBUG);
			}
		} catch (MQException e) {
			if (hasAlternates()) {
				if (m_index >= getAltCount()) {
					throw new RuntimeException("Queue.write(): m_index >= getAltCount() (no more alternates to try)");
				}

				m_hostName = m_hostNames[m_index];
				m_channel = m_channels[m_index];
				m_queueMgrName = m_queueMgrNames[m_index];
				m_queueName = m_queueNames[m_index];
				m_index++;
				m_tryingAlternate = true;

				if (m_loggingOn) {
					Logger.log("Queue.write(): trying alternate", Logger.INFO);
				}

				write(msgString, syncpoint);
			} else {
				throw new BaseException(e);
			}
		} catch (Throwable t) {
			throw new BaseException(t);
		} finally {
			try {
				if ((queue != null) && (queue.isOpen) // &&
						// (!syncpoint)
				) {
					queue.close();
				}

				if (m_disconnect) {
					if (syncpoint) {
						throw new RuntimeException("Queue.write(): m_disconnect && syncpoint");
					} else {
						close();
					}
				}
			} catch (Throwable t) {
				throw new BaseException(t);
			}
		}
	}

}