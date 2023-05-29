package oz.samples.mq;
import java.io.IOException;
import java.util.Hashtable;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.MQSimpleConnectionManager;
import com.ibm.mq.constants.MQConstants;

public class SendRecive {
   
	
	public static final String encoding = "iso-8859-8";
	private MQQueueManager qmgr = null;
	private MQQueue qQueueRead = null;
	private MQQueue qQueueWrite = null;
	private MQPutMessageOptions pmo = null;
	private MQGetMessageOptions gmo = null;
	private MQMessage qMessageWrite = null;
	private MQMessage qMessageRead = null;
	private int openOptionWrite;
	private int openOptionRead;
	
	private String queueManagerName = "";
	private String hostName = "10.18.188.35";
	private String channel = "MQSERVER";
	private int port 		=	1414;
	
	private String qQueueReadName = "ODED.TEST.RESP";
	private String qQueueWriteName = "ODED.TEST.REQ";
	private String replyToQueueName = "ODED.TEST.RESP";
	
	private int waitInterval = 30;
	
	
	private byte[] answer = null;
	private byte[] sendMessage	=	null;
	
	
	private static MQSimpleConnectionManager mcm = null;
	
	static {
		mcm = new MQSimpleConnectionManager();
	    mcm.setActive(MQSimpleConnectionManager.MODE_ACTIVE);
	    mcm.setTimeout(3600000); // millisec
	    mcm.setMaxConnections(30);
	    mcm.setMaxUnusedConnections(50);
	    
	}

	public static void main(String[] argv) {
		try{
			if (argv.length < 0){
				System.out.println("use SendRecive [String hostName,int port,String channelName,String writeQ, String readQ,String message,int waitIntervel]" );
				System.exit(0);	
			}
		//	SendRecive sr = new SendRecive(argv[0], Integer.parseInt(argv[1]), argv[2], argv[4], argv[5],  Integer.parseInt(argv[7]));
			SendRecive sr = new SendRecive("s5380503", Integer.parseInt("1414"),"CHAN1","ODED.TEST.REQ",  "ODED.TEST.RESP",  Integer.parseInt("10"));
			if (sr.send("helloWmq".getBytes()) != null){
				System.out.println(new String(sr.getAnswer()));
			}else{
				System.out.println("There is some problem");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * constructor comment.
	 */
	public SendRecive(String hostName,int port,String channelName,String writeQ, String readQ,int waitIntervel) throws MQException{
		if (hostName != null)
			this.hostName = hostName;
		if (port != 0)
			this.port = port;	
			
		if (writeQ != null)
			this.qQueueWriteName = writeQ;
			
		if (readQ != null) {
			this.qQueueReadName = readQ;
			this.replyToQueueName = readQ;
		}
		if (channelName != null)
			this.channel = channelName;		
		this.waitInterval = waitIntervel*1000;
		init();
	}


	public void destroy() {
		try {
			if (qQueueWrite != null)
				qQueueWrite.close();
			if (qQueueRead != null)
				qQueueRead.close();
			if (qmgr != null)
				qmgr.disconnect();
		} catch (MQException e) {
			;
		}
	}


	private void init() throws MQException {

		try {
			
		    Hashtable<Object,Object> env = new Hashtable<Object,Object>();
		  
		    env.put(MQConstants.HOST_NAME_PROPERTY,hostName);
		    env.put(MQConstants.CHANNEL_PROPERTY,channel);
		    env.put(MQConstants.PORT_PROPERTY,port);

			// connect to the qmgr
			qmgr = new MQQueueManager(queueManagerName,env,mcm);
			System.out.println(qmgr.getAttributeString(MQConstants.MQCA_Q_MGR_NAME,48));
			// Set the openOptionWrite
			openOptionWrite = MQConstants.MQOO_OUTPUT | MQConstants.MQOO_FAIL_IF_QUIESCING;
			// Set the  openOptionRead
			openOptionRead = MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_FAIL_IF_QUIESCING;
			// Set the pmo
			pmo = new MQPutMessageOptions();
			pmo.options = MQConstants.MQPMO_NO_SYNCPOINT | MQConstants.MQPMO_FAIL_IF_QUIESCING;
			// Set the gmo
			gmo = new MQGetMessageOptions();
			gmo.options = MQConstants.MQGMO_WAIT | MQConstants.MQGMO_CONVERT | MQConstants.MQGMO_FAIL_IF_QUIESCING;
			gmo.waitInterval = waitInterval;

			// Set The Write queue
			qQueueWrite = qmgr.accessQueue(qQueueWriteName, openOptionWrite, null, null, null);
			// Set the Write message   
			qMessageWrite = new MQMessage();
			qMessageWrite.expiry = 600; // 1 minute
			qMessageWrite.persistence = MQConstants.MQPER_NOT_PERSISTENT;
			qMessageWrite.encoding = MQConstants.MQENC_NATIVE;
			qMessageWrite.format = MQConstants.MQFMT_STRING;
			qMessageWrite.replyToQueueName = replyToQueueName;
			qMessageWrite.characterSet = 916;
			// open a Queue for read
			qQueueRead = qmgr.accessQueue(qQueueReadName, openOptionRead, null, null, null);

			// Set the qMessageRead
			qMessageRead = new MQMessage();
			qMessageRead.characterSet = 916;

		} catch (MQException e) {
			throw e;
		}

	} /**
	 * Insert the method's description here.
	 * Creation date: (06/06/2002 12:36:43)
	 * @param args java.lang.String[]
	 */
	
	public byte[] send(byte[] message) throws IOException, MQException {

		this.sendMessage = message;
		

		try {

			
             
			qMessageWrite.clearMessage();
			qMessageWrite.correlationId = MQConstants.MQCI_NONE;
			qMessageWrite.messageId = MQConstants.MQCI_NONE;
			//qMessageWrite.writeBytes(this.sendMessage);
			qMessageWrite.write(this.sendMessage);
			qQueueWrite.put(qMessageWrite, pmo);
			
			
			// set corel
			qMessageRead.correlationId = qMessageWrite.messageId;
			qMessageRead.messageId = MQConstants.MQCI_NONE;
			// read
			qQueueRead.get(qMessageRead, gmo);
			this.answer = new byte[qMessageRead.getDataLength()];
			qMessageRead.readFully(this.answer);
			  	
		
		} catch (MQException ex) {
			throw ex;
		} catch (IOException ex) {
			throw ex;
		} finally {
			destroy();
		}
		
		return(this.answer);
	}
	public byte[] getAnswer(){
		return this.answer;
		
	}
	public static void usage(){
		System.out.println("use [SERVER NAME/IP] [SERVER PORT(1414)] [QMGR NAME(D) [CHANNEL NAME] [WRITE Q],[REPLY Q],[MESSAGE TO WRITE] ,[WAIT INTERVAL]");		
	}
}