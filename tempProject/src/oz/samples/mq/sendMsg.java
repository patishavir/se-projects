package oz.samples.mq;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.MQConstants;

public class SendMsg{
public void sendMsg() {
        MQQueue queue = null;
        MQQueueManager queueManager = null;
        MQMessage mqMessage = null;
        MQPutMessageOptions pmo = null;
        System.out.println("Entering..");
        try {
            MQEnvironment.hostname = "x.x.x.x";
            MQEnvironment.channel = "xxx.SVRCONN";
            MQEnvironment.port = 9999;


            queueManager = new MQQueueManager("XXXQMANAGER");
            int openOptions = MQConstants.MQOO_OUTPUT;      
            queue = queueManager.accessQueue("XXX_QUEUENAME", openOptions, null, null, null);

            pmo = new MQPutMessageOptions(); 
            pmo.options = CMQC.MQGMO_SYNCPOINT;


            String input = "testing";
            System.out.println("sending messages....");
            for (int i = 0; i < 10; i++) {
                input = input + ": " + i;
                mqMessage = new MQMessage();
                mqMessage.writeString(input);
                System.out.println("Putting message: " + i);
                queue.put(mqMessage, pmo);

            }
            queueManager.commit();
            System.out.println("Exiting..");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println("rolling back messages");
                if (queueManager != null)
                    queueManager.backout();
            } catch (MQException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (queue != null)
                    queue.close();
                if (queueManager != null)
                    queueManager.close();
            } catch (MQException e) {
                e.printStackTrace();
            }
        }
    }
}