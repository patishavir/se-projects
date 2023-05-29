package oz.monitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.ObjectName;

import oz.infra.collection.CollectionUtils;
import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.monitor.common.OzMonitorUtils;
import oz.monitor.monitors.AbstractOzMonitor;
import oz.monitor.monitors.OzMonitorDispatcher;

public class OzMonitor implements OzMonitorMBean {
	private static List<OzMonitorDispatcher> ozMonitorDispatcherList = new ArrayList<OzMonitorDispatcher>();
	private static Logger logger = JulUtils.getLogger();
	private int numberOfMonitors = 0;

	public final void exit() {
		logger.warning(StringUtils.concat(" Terminating ", SystemUtils.getLocalHostName(), " ..."));
		try {
			ObjectName mbeanName = new ObjectName(OzMonitorParameters.getObjectNameString());
			OzMonitorParameters.getMbeanServer().unregisterMBean(mbeanName);
			System.exit(OzConstants.EXIT_STATUS_OK);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	public final String getMonitorsStatusReport() {
		logger.finest("Starting ...");
		final String delimiter = OzConstants.TAB;
		List<String> statusReportList = new ArrayList<String>();
		for (OzMonitorDispatcher ozMonitorDispatcher : ozMonitorDispatcherList) {
			StringBuilder sb = new StringBuilder();
			sb.append(ozMonitorDispatcher.getAbstractOzMonitor().getResource());
			sb.append(delimiter);
			sb.append(ozMonitorDispatcher.getLastInvokationTimeStamp());
			sb.append(OzConstants.LINE_FEED);
			statusReportList.add(sb.toString());
		}
		CollectionUtils.printCollection(statusReportList, Level.FINEST);
		Collections.sort(statusReportList);
		CollectionUtils.printCollection(statusReportList, Level.FINEST);
		logger.finest("Going back ...");
		return CollectionUtils.getAsDelimitedString(statusReportList);
	}

	public final int getNumberOfMonitors() {
		logger.info("numberOfMonitors: " + String.valueOf(numberOfMonitors));
		return numberOfMonitors;
	}

	final void startMonitoring() {
		Collection<Properties> monitorsCollection = OzMonitorParameters.getMonitorsMap().values();
		for (Properties monitorProperties : monitorsCollection) {
			OzMonitorDispatcher ozMonitorDispatcher = new OzMonitorDispatcher(monitorProperties);
			AbstractOzMonitor abstractOzMonitor = ozMonitorDispatcher.getAbstractOzMonitor();
			if (abstractOzMonitor != null) {
				new Thread(ozMonitorDispatcher).start();
				ozMonitorDispatcherList.add(ozMonitorDispatcher);
				logger.info(StringUtils.concat("resource: ", abstractOzMonitor.getResource()));
				numberOfMonitors++;
			}
		}
		OzMonitorUtils.sendHeartBeatMessage(null);
	}

}
