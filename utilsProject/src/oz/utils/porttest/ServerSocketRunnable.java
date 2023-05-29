package oz.utils.porttest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class ServerSocketRunnable implements Runnable {
	private int port = 0;
	private ServerSocket providerSocket;
	private Socket socket = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String message;
	private static Logger logger = JulUtils.getLogger();

	ServerSocketRunnable(final int port) {
		this.port = port;
	}

	public final void run() {
		try {
			// 1. creating a server socket
			providerSocket = new ServerSocket(port, 10);
			// 2. Wait for connection
			logger.info("Waiting for connection on port: " + String.valueOf(port));
			socket = providerSocket.accept();
			logger.info("Connection received from " + socket.getInetAddress() + " hostname: "
					+ socket.getInetAddress().getHostName());
			// 3. get Input and Output streams
			in = new ObjectInputStream(socket.getInputStream());
			// 4. The two parts communicate via the input and output streams
			try {
				message = (String) in.readObject();
				logger.info("client >> " + message);
				if (message.startsWith(String.valueOf(port))) {
					logger.info("Test succeeded !");
					System.exit(0);
				} else {
					logger.info("Test failed !");
					System.exit(-1);
				}
			} catch (ClassNotFoundException classnot) {
				logger.warning("Data received in unknown format");
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				providerSocket.close();
				System.exit(-2);
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
}
