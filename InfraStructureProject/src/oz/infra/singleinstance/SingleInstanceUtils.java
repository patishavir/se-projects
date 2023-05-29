package oz.infra.singleinstance;

import java.net.ServerSocket;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.properties.PropertiesUtils;
import oz.infra.socket.SocketUtils;

public class SingleInstanceUtils {
	public static final String SINGLE_INSTANCE_PORT = "singleInstancePort";

	private static Logger logger = JulUtils.getLogger();

	public static Outcome confirmSingleInstanceRun(final int port) {
		ServerSocket serverSocket = SocketUtils.getServerSocket(port);
		Outcome outcome = Outcome.FAILURE;
		if (serverSocket == null) {
			logger.warning("Multiple instances not allowed. Process will exit.");
			System.exit(OzConstants.EXIT_STATUS_ABNORMAL);
		} else {
			logger.info("Running single instance check has completeed OK. proceeding ...");
			outcome = Outcome.SUCCESS;
		}
		return outcome;
	}

	public static Outcome confirmSingleInstanceRun(final Properties properties) {
		String portStr = properties.getProperty(SINGLE_INSTANCE_PORT);
		int port = Integer.parseInt(portStr);
		return confirmSingleInstanceRun(port);
	}

	public static Outcome confirmSingleInstanceRun(final String propertiesFilePath) {
		Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		return confirmSingleInstanceRun(properties);

	}
}
