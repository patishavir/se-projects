package oz.monitor.monitors;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;

import oz.infra.array.ArrayUtils;
import oz.infra.io.PathUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.system.SystemUtils;
import oz.monitor.OzMonitorParameters;
import oz.monitor.common.OzMonitorResponse;

public class TerminationMonitor extends AbstractOzMonitor {
	private String hostsToTerminateFolderPath;
	private File hostsToTerminateFolderFile;
	private static final String MY_HOST_NAME = SystemUtils.getLocalHostName();

	public TerminationMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this, this.getClass().getSuperclass());
		hostsToTerminateFolderPath = PathUtils.getFullPath(OzMonitorParameters.getRootFolderPath(),
				hostsToTerminateFolderPath);
		hostsToTerminateFolderFile = new File(hostsToTerminateFolderPath);
		logger.info("myHostName: " + MY_HOST_NAME + " hostsToTerminateFolderPath: " + hostsToTerminateFolderPath);
	}

	public final OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		File[] hostsToTerminateArray = hostsToTerminateFolderFile.listFiles();
		ArrayUtils.printArray(hostsToTerminateArray, Level.FINEST);
		for (File hostFile1 : hostsToTerminateArray) {
			String hostName1 = hostFile1.getName().toLowerCase();
			logger.finest("hostFile1 " + hostFile1.getName() + " myHostName: " + MY_HOST_NAME);
			if (MY_HOST_NAME.toLowerCase().indexOf(hostName1) >= 0) {
				logger.warning(" Terminating " + MY_HOST_NAME + " ...");
				hostFile1.delete();
				System.exit(0);
			}
		}
		String ozMOnitorMessage = this.getClass().getSimpleName() + " is active !";
		return new OzMonitorResponse(true, ozMOnitorMessage, null);
	}

	public final void setHostsToTerminateFolderPath(final String hostsToTerminateFolderPath) {
		this.hostsToTerminateFolderPath = hostsToTerminateFolderPath;
	}
}
