package oz.temp.ssl.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.Socket;
import java.security.Certificate;
import java.security.cert.X509Certificate;

import org.apache.http.conn.ssl.SSLSocketFactory;

public class SslTest1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		performSslTest();
	}
	private static void performSslTest() {
		 System.setProperty("javax.net.ssl.trustStore", "/home/user/.truststore");
		    System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		    SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		    Socket s = ssf.createSocket("127.0.0.1", 8362);
		    SSLSession session = ((SSLSocket) s).getSession();
		    Certificate[] cchain = session.getPeerCertificates();
		    System.out.println("The Certificates used by peer");
		    for (int i = 0; i < cchain.length; i++) {
		        System.out.println(((X509Certificate) cchain[i]).getSubjectDN());
		    }
		    System.out.println("Peer host is " + session.getPeerHost());
		    System.out.println("Cipher is " + session.getCipherSuite());
		    System.out.println("Protocol is " + session.getProtocol());
		    System.out.println("ID is " + new BigInteger(session.getId()));
		    System.out.println("Session created in " + session.getCreationTime());
		    System.out.println("Session accessed in "
		            + session.getLastAccessedTime());
		    BufferedReader in = new BufferedReader(new InputStreamReader(
		            s.getInputStream()));
		    boolean isChatOver = false;
		    while(!isChatOver) {
		        if(in.ready()) {
		            Thread.sleep(5000);
		        }
		        String nextChat = in.readLine();
		        System.out.println("server says : " + nextChat);
		        if("bye".equalsIgnoreCase(nextChat.trim())) {
		            System.out.println("**************************************Closing Session.*********************************************");
		            isChatOver = true;
		        }
		    }
		    in.close();

	}
}
