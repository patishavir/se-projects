package oz.infra.jmx;

import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Logger;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.jmx.exit.ExitHandler;
import oz.infra.jmx.exit.ExitHandlerMBean;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class JmxUtils {
	public static final String JMX_RMI_SERVICE_URL = "service:jmx:rmi:///jndi/rmi://%host%:%port%/jmxrmi";
	private static Logger logger = JulUtils.getLogger();

	public static String getRmiServiceUrl(final int port) {
		String rmiServiceUrl = JMX_RMI_SERVICE_URL.replaceAll("%host%", OzConstants.LOCAL_HOST);
		rmiServiceUrl = rmiServiceUrl.replaceAll("%port%", String.valueOf(port));
		return rmiServiceUrl;
	}

	public static String getRmiServiceUrl(final String host, final int port) {
		String rmiServiceUrl = JMX_RMI_SERVICE_URL.replaceAll("%host%", host);
		rmiServiceUrl = rmiServiceUrl.replaceAll("%port%", String.valueOf(port));
		return rmiServiceUrl;
	}

	public static void performExit(final String hostName, final int port) {
		try {
			ObjectName mbeanName = new ObjectName(ExitHandler.OBJECT_NAME_STRING);
			MBeanServerConnection mbsc = JmxUtils.startRmiClientConnector(hostName, port);
			logger.info(StringUtils.concat("MBeanServerConnection: ", mbsc.toString()));
			ExitHandlerMBean mbeanProxy = JMX.newMBeanProxy(mbsc, mbeanName, ExitHandlerMBean.class, true);
			logger.info(StringUtils.concat("mbeanProxy: ", mbeanProxy.toString()));
			logger.info(StringUtils.concat("perform exit.  host: ", hostName, " port: ", String.valueOf(port)));
			mbeanProxy.exit();
			logger.info(StringUtils.concat("performed exit.  host: ", hostName, " port: ", String.valueOf(port)));
		} catch (Exception ex) {
			logger.warning("caught exception  ...");
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	public static MBeanServer registerMbean(final int port, final String objectNameString) {
		MBeanServer mbeanServer = null;
		try {
			mbeanServer = JmxUtils.startRmiServerConnector(OzConstants.LOCAL_HOST, port);
			logger.info("objectName: " + objectNameString);
			ObjectName mbeanName = new ObjectName(objectNameString);
			ExitHandler exithandler = new ExitHandler();
			mbeanServer.registerMBean(exithandler, mbeanName);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return mbeanServer;
	}

	public static MBeanServerConnection startRmiClientConnector(final int port) {
		logger.info("\nCreate an RMI connector client and connect it to the RMI connector server");
		return startRmiClientConnector(OzConstants.LOCAL_HOST, port);
	}

	//
	// Create an RMI connector client and
	// connect it to the RMI connector server
	//
	public static MBeanServerConnection startRmiClientConnector(final String host, final int port) {
		logger.info("\nCreate an RMI connector client and connect it to the RMI connector server");
		MBeanServerConnection mbsc = null;
		String url = getRmiServiceUrl(host, port);
		try {
			JMXServiceURL jmxServiceURL = new JMXServiceURL(url);
			JMXConnector jmxc = JMXConnectorFactory.connect(jmxServiceURL, null);
			logger.info("\nGet an MBeanServerConnection");
			mbsc = jmxc.getMBeanServerConnection();
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return mbsc;
	}

	public static MBeanServer startRmiServerConnector(final int port) {
		// Get the Platform MBean Server
		return startRmiServerConnector(OzConstants.LOCAL_HOST, port);
	}

	public static MBeanServer startRmiServerConnector(final String host, final int port) {
		// Get the Platform MBean Server
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		String url = getRmiServiceUrl(host, port);
		try {
			LocateRegistry.createRegistry(port);
			JMXServiceURL jmxServiceURL = new JMXServiceURL(url);
			JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, null, mbs);
			cs.start();
			logger.info(StringUtils.concat("JMXConnectorServer  has started. host: ", host, " Port: ",
					String.valueOf(port)));
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return mbs;
	}
}
