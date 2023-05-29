package oz.monitor.jmx.client;

import java.util.logging.Logger;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.jmx.JmxUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.monitor.OzMonitorMBean;
import oz.monitor.OzMonitorParameters;
import oz.monitor.common.OzMonitorUtils;

public class OzMonitorJmxClient {
	private static enum Mbean_Operation {
		EXIT, GET_NUMBER_OF_MONITORS, GET_MONITORS_STATUS_REPORT, EMAIL_MONITORS_STATUS_REPORT
	}

	private static MBeanServerConnection mbsc = null;
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		SystemPropertiesUtils.printSystemProperties();
		String[] parameters = { args[0] };
		OzMonitorParameters.processInputParameters(parameters);
		String objectNameString = OzMonitorParameters.getObjectNameString();
		String operation = args[1];
		Mbean_Operation mbeanOperation = Mbean_Operation.valueOf(operation);
		String host = null;
		StringBuilder sb = new StringBuilder(OzConstants.LINE_FEED);
		String[] hostsArray = args[2].split(OzConstants.COMMA);
		int hostsProcessed = 0;
		for (int i = 0; i < hostsArray.length; i++) {
			try {
				ObjectName mbeanName = new ObjectName(objectNameString);
				host = hostsArray[i].trim();
				mbsc = JmxUtils.startRmiClientConnector(host, OzMonitorParameters.getJmxPort());
				OzMonitorMBean mbeanProxy = JMX.newMBeanProxy(mbsc, mbeanName, OzMonitorMBean.class, true);
				switch (mbeanOperation) {
				case EXIT: {
					sb.append(StringUtils.concat("\nPerforming Operation ", operation, " for ", objectNameString,
							" on host: ", host));
					mbeanProxy.exit();
					break;
				}
				case GET_NUMBER_OF_MONITORS: {
					int numberOfMonitors = mbeanProxy.getNumberOfMonitors();
					sb.append(StringUtils.concat("\nnumberOfMonitors on host ", host, ": ",
							String.valueOf(numberOfMonitors)));
					break;
				}
				case GET_MONITORS_STATUS_REPORT: {
					String monitorsStatusReport = mbeanProxy.getMonitorsStatusReport();
					sb.append(StringUtils.concat("\n*** Monitors status report for host ", host, ": \n",
							monitorsStatusReport));
					break;
				}
				case EMAIL_MONITORS_STATUS_REPORT: {
					String monitorsStatusReport = mbeanProxy.getMonitorsStatusReport();
					if (monitorsStatusReport.trim().length() > OzConstants.INT_5) {
						hostsProcessed++;
					}
					sb.append(StringUtils.concat("\n*** Monitors status report for host ", host, ": \n",
							monitorsStatusReport));
					break;
				}
				}
			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
			}
			logger.info(StringUtils.concat("Operation ", operation, " has been performed for ", objectNameString,
					" on host: ", host));
		}
		logger.info(sb.toString());
		if (mbeanOperation == Mbean_Operation.EMAIL_MONITORS_STATUS_REPORT) {
			String hostsProcessedString = StringUtils.concat(String.valueOf(hostsProcessed), " hosts out of ",
					String.valueOf(hostsArray.length), " have been processed.");
			sb.append(StringUtils.concat("\n", hostsProcessedString));
			String[] alertsArray = { "gmemail" };
			OzMonitorUtils.sendAlerts(StringUtils.concat("Monitors status report. ", hostsProcessedString),
					sb.toString(), alertsArray, null);
		}
	}

	private static Object invoke(final ObjectName mbeanName, final String operation) throws Exception {
		Object obj = mbsc.invoke(mbeanName, // MBean name
				operation, null, // no parameters
				null // void signature
		);
		return obj;
	}
}
