package oz.samples.mq;

import com.ibm.mq.MQSimpleConnectionManager;

public class MqCoonectionPoolManagerSample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MQSimpleConnectionManager myConnMan = new MQSimpleConnectionManager();
		myConnMan.setActive(MQSimpleConnectionManager.MODE_ACTIVE);
		myConnMan.setTimeout(3600000);// millisecs
		myConnMan.setMaxConnections(33);
		myConnMan.setMaxUnusedConnections(50);
		myConnMan.setActive(MQSimpleConnectionManager.MODE_INACTIVE);

		// MQQueueManager qMgr = new MQQueueManager(getMQQueueManagerName(),
		// mqEnv, myConnMan);
		// MQQueue queue = qMgr.accessQueue(getMQQueueName(),qOpenOptions, null,
		// null, null);
	}
}
