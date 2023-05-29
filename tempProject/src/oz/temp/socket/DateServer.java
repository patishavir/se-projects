package oz.temp.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * A TCP server that runs on port 9090. When a client connects, it sends the
 * client the current date and time, then closes the connection with that
 * client. Arguably just about the simplest server you can write.
 */
public class DateServer {
	/**
	 * Runs the server.
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("start listening to port 9090.");
		ServerSocket listener = new ServerSocket(9090);
		try {
			while (true) {
				System.out.println("start accept loop on socket.");
				Socket socket = listener.accept();
				System.out.println("accept on socket port 9090 has completed.");
				try {
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					out.println(new Date().toString());
				} finally {
					socket.close();
					System.out.println("socket closed.\n");
				}
			}
		} finally {
			System.out.println("finally: listener to port 9090 has closed.");
			listener.close();
		}
	}
}