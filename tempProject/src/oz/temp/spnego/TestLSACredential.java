package oz.temp.spnego;

import sun.security.krb5.Credentials;
import sun.security.krb5.internal.Ticket;

public class TestLSACredential {
	public static void main(String[] args) {
		Credentials cred = Credentials.acquireDefaultCreds();

		System.out.println("==============CREDENTIAL START====================");
		System.out.println("Client : " + cred.getClient().getName());
		System.out.println("Server : " + cred.getServer().getName());
		// Session key type
		// DES_CBC_MD5 3
		// RC4_HMAC 23
		System.out.println("Session key type : " + cred.getSessionKey().getEType());
		System.out.println("Start time : " + cred.getStartTime().getTime() + " GMT time : "
				+ cred.getStartTime().toGMTString() + " Local time : " + cred.getStartTime().toLocaleString());
		System.out.println("End time : " + cred.getEndTime().getTime() + " GMT time : "
				+ cred.getEndTime().toGMTString() + " Local time : " + cred.getEndTime().toLocaleString());
		System.out.println("AUth time : " + cred.getAuthTime().getTime() + " GMT time : "
				+ cred.getAuthTime().toGMTString() + " Local time : " + cred.getAuthTime().toLocaleString());
		// Print ticket info
		Ticket tkt = cred.getTicket();
		System.out.println("Ticket realm : " + tkt.toString());
		System.out.println("Ticket server : " + tkt.sname.getName());
		System.out.println("Ticket flags  : " + cred.getTicketFlags());
		System.out.println("=============CREDENTIAL END=========================");
	}
}