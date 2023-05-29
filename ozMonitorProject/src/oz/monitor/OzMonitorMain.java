package oz.monitor;

import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.jmx.JmxUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public final class OzMonitorMain {
	private static Logger logger = JulUtils.getLogger();

	private OzMonitorMain() {
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		logger.finest("Start monitoring ...");
		SystemUtils.validateClassPath();
		OzMonitorParameters.processInputParameters(args);
		registerMbean();
	}

	private static void registerMbean() {
		try {
			int port = OzMonitorParameters.getJmxPort();
			MBeanServer mbeanServer = JmxUtils.startRmiServerConnector(OzConstants.LOCAL_HOST, port);
			OzMonitorParameters.setMbeanServer(mbeanServer);
			String objectNameString = OzMonitorParameters.getObjectNameString();
			logger.info("objectName: " + objectNameString);
			ObjectName mbeanName = new ObjectName(objectNameString);
			OzMonitor ozMonitor = new OzMonitor();
			mbeanServer.registerMBean(ozMonitor, mbeanName);
			ozMonitor.startMonitoring();
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}
}
