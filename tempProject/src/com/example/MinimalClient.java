package com.example;

/*
 * Client.java - JMX client that interacts with the JMX agent. It gets
 * attributes and performs operations on the Exit MBean and the QueueSampler
 * MXBean example. It also listens for Exit MBean notifications.
 */

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.jmx.JmxUtils;
import oz.infra.jmx.exit.ExitHandler;
import oz.infra.jmx.exit.ExitHandlerMBean;
import oz.infra.logging.jul.JulUtils;

public class MinimalClient {
	private static Logger logger = JulUtils.getLogger();

	/*
	 * For simplicity, we declare "throws Exception". Real programs will usually
	 * want finer-grained exception handling.
	 */
	public static void main(String[] args) throws Exception {
		ccc();
	}

	private static void ddd() {
		try {
			// Create an RMI connector client and
			// connect it to the RMI connector server
			//
			logger.info("\nCreate an RMI connector client and " + "connect it to the RMI connector server");
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
			// Get an MBeanServerConnection
			//
			logger.info("\nGet an MBeanServerConnection");
			MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

			// Get domains from MBeanServer
			//
			logger.info("\nDomains:");
			String domains[] = mbsc.getDomains();
			Arrays.sort(domains);
			for (String domain : domains) {
				logger.info("\tDomain = " + domain);
			}

			logger.info("\nMBeanServer default domain = " + mbsc.getDefaultDomain());

			// Get MBean count
			//
			logger.info("\nMBean count = " + mbsc.getMBeanCount());

			// Query MBean names
			//
			logger.info("\nQuery MBeanServer MBeans:");
			Set<ObjectName> names = new TreeSet<ObjectName>(mbsc.queryNames(null, null));
			for (ObjectName name : names) {
				logger.info("\tObjectName = " + name);
			}

			// ----------------------
			// Manage the Exit MBean
			// ----------------------

			logger.info("\n>>> Perform operations on Exit MBean <<<");

			// Construct the ObjectName for the Exit MBean
			//
			ObjectName mbeanName = new ObjectName(ExitHandler.OBJECT_NAME_STRING);

			// Create a dedicated proxy for the MBean instead of
			// going directly through the MBean server connection
			//
			ExitHandlerMBean mbeanProxy = JMX.newMBeanProxy(mbsc, mbeanName, ExitHandlerMBean.class, true);

			// Add notification listener on Exit MBean
			//
			mbeanProxy.exit();
		} catch (MalformedObjectNameException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (MalformedURLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	private static void ccc() {
		try {
			ObjectName mbeanName = new ObjectName(ExitHandler.OBJECT_NAME_STRING);
			MBeanServerConnection mbsc = JmxUtils.startRmiClientConnector(OzConstants.LOCAL_HOST, 9999);
			ExitHandlerMBean mbeanProxy = JMX.newMBeanProxy(mbsc, mbeanName, ExitHandlerMBean.class, true);
			logger.info("perform exit ...");
			mbeanProxy.exit();
			logger.info("performed exit ...");
		} catch (Exception ex) {
			logger.warning("caught exception  ...");
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}
}
