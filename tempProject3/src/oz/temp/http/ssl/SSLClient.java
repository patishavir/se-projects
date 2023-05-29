package oz.temp.http.ssl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLClient {
	public static void main(String args[]) {
		try {
			// Mo 1 client socket den server voi so cong va dia chi xac dinh
			SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket sslsocket = (SSLSocket) factory.createSocket("s5381310.fibi.corp", 443);
			// Tao luong nhan va gui du lieu len server
			DataOutputStream os = new DataOutputStream(sslsocket.getOutputStream());
			
			// Gui du lieu len server
			String str = "GET /oded.html HTTP/1.1\r\n\r\n";
			os.writeBytes(str);
			os.flush();
			System.out.println("write done !!!");
			// Nhan du lieu da qua xu li tu server ve
			String responseStr;
			DataInputStream is = new DataInputStream(sslsocket.getInputStream());
			if ((responseStr = is.readUTF()) != null) {
				System.out.println("responseStr: " + responseStr);
			}
			os.close();
			is.close();
			sslsocket.close();
		} catch (UnknownHostException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}