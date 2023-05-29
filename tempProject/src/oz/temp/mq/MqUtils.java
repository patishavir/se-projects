package oz.temp.mq;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.pcf.CMQC;
import com.ibm.mq.pcf.CMQCFC;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;

public class MqUtils {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// xxx();
		yyy();
	}

	private static void xxx() {
		int depth = 0;
		MQQueueManager qMgr; // define a queue manager object
		String mqHost = "10.18.188.152";
		String mqPort = "1414";
		String mqChannel = "SERVER.CHANNEL";
		String mqQMgr = "Q538ML00";
		String mqQueue = "CICST.BRIDGE.QUEUE";
		try {
			// Set up MQSeries environment
			MQEnvironment.hostname = mqHost;
			MQEnvironment.port = Integer.valueOf(mqPort).intValue();
			MQEnvironment.channel = mqChannel;
			MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES);
			qMgr = new MQQueueManager(mqQMgr);
			System.out.println("got qMgr: " + qMgr.toString());
			// int openOptions = MQC.MQOO_BROWSE;
			// MQQueue destQueue = qMgr.accessQueue(mqQueue, openOptions);
			int openOptions = 0;
			MQQueue destQueue = qMgr.accessQueue(mqQueue, 0);
			System.out.println("got destQueue: " + destQueue.toString());
			depth = destQueue.getCurrentDepth();
			destQueue.close();
			qMgr.disconnect();
		} catch (Exception err) {
			err.printStackTrace();
		}

	}

	private static void yyy() {
		int depth = 0;
		MQQueueManager qMgr = null; // define a queue manager object
		String mqHost = "10.18.188.152";
		String mqPort = "1414";
		String mqChannel = "SERVER.CHANNEL";
		String mqQMgr = "Q538ML00";
		String mqQueue = "CICST.BRIDGE.QUEUE";
		try {
			// Set up MQSeries environment
			MQEnvironment.hostname = mqHost;
			MQEnvironment.port = Integer.valueOf(mqPort).intValue();
			MQEnvironment.channel = mqChannel;
			MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES);
			qMgr = new MQQueueManager(mqQMgr);
			System.out.println("got qMgr: " + qMgr.toString());
		} catch (Exception err) {
			err.printStackTrace();
		}

		// Create PCF Message Agent
		PCFMessageAgent pcfAgent = null;
		try {
			pcfAgent = new PCFMessageAgent(qMgr);

			// Prepare PCF command to inquire queue status (status type)
			PCFMessage inquireQueueStatus = new PCFMessage(CMQCFC.MQCMD_INQUIRE_Q_STATUS);
			inquireQueueStatus.addParameter(CMQC.MQCA_Q_NAME, "name of queue to inquire");
			inquireQueueStatus.addParameter(CMQCFC.MQIACF_Q_STATUS_TYPE, CMQCFC.MQIACF_Q_STATUS);
			inquireQueueStatus.addParameter(CMQCFC.MQIACF_Q_STATUS_ATTRS,
					new int[] { CMQC.MQCA_Q_NAME, CMQC.MQIA_CURRENT_Q_DEPTH, CMQCFC.MQCACF_LAST_GET_DATE,
							CMQCFC.MQCACF_LAST_GET_TIME, CMQCFC.MQCACF_LAST_PUT_DATE, CMQCFC.MQCACF_LAST_PUT_TIME,
							CMQCFC.MQIACF_OLDEST_MSG_AGE, CMQC.MQIA_OPEN_INPUT_COUNT, CMQC.MQIA_OPEN_OUTPUT_COUNT,
							CMQCFC.MQIACF_UNCOMMITTED_MSGS });

			PCFMessage[] xxx = pcfAgent.send(inquireQueueStatus);

			// Prepare PCF command to reset queue statistics
			PCFMessage queueResetStats = new PCFMessage(CMQCFC.MQCMD_RESET_Q_STATS);
			queueResetStats.addParameter(CMQC.MQCA_Q_NAME, "CICST.BRIDGE.QUEUE");

			PCFMessage[] pcfResp3 = pcfAgent.send(queueResetStats);

			int queueMsgDeqCount = pcfResp3[0].getIntParameterValue(CMQC.MQIA_MSG_DEQ_COUNT);
			int queueMsgEnqCount = pcfResp3[0].getIntParameterValue(CMQC.MQIA_MSG_ENQ_COUNT);
		} catch (Exception ex) {
			System.err.println("PCF Message Agent creation ended with reason code " + ex.toString());
			// return mqe.reasonCode;
		}

	}

}
