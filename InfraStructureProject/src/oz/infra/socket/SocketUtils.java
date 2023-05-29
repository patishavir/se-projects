package oz.infra.socket;

import java.net.ServerSocket;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class SocketUtils {
	private static Logger logger = JulUtils.getLogger();

	public static ServerSocket getServerSocket(final int port) {
		ServerSocket serverSocket = null;
		try {
			logger.info("open server socket. port: ".concat(String.valueOf(port)));
			serverSocket = new ServerSocket(port);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
		return serverSocket;
	}

	public static void closeServerSocket(final ServerSocket serverSocket) {
		try {
			serverSocket.close();
			logger.info("socket closed.");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
	}
}
