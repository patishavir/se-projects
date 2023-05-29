package oz.monitor.monitors;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.datetime.DateTimeUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.monitor.common.OzMonitorResponse;

public class FileModificationTimeMonitor extends AbstractOzMonitor {
	private static Logger logger = JulUtils.getLogger();
	private String resource;
	private String filePath;
	private File file2Monitor = null;
	private long initialModificationTimeStamp = -1;
	private long currentModificationTimeStamp = -1;
	private long previousModificationTimeStamp = -1;
	private boolean fileModificationTimeChangesOK = false;
	private boolean file2MonitorExists = false;

	public FileModificationTimeMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this, this.getClass().getSuperclass());
		logger.info(
				"Starting monitor " + this.getClass().getName() + " resource:" + resource + " filePath: " + filePath);
		file2Monitor = new File(filePath);
		if (!file2Monitor.exists()) {
			logger.severe(filePath + " does not exists !");
		}
		initialModificationTimeStamp = file2Monitor.lastModified();
	}

	public final OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		boolean isResourceAvailable = false;
		String ozMonitorMessage = filePath + " does not exist !";
		logger.finest("Monitoring resource: " + resource + ". file path: " + filePath);
		file2MonitorExists = file2Monitor.exists();
		if (file2MonitorExists) {
			currentModificationTimeStamp = file2Monitor.lastModified();
			isResourceAvailable = (currentModificationTimeStamp == initialModificationTimeStamp)
					&& (!fileModificationTimeChangesOK);
			previousModificationTimeStamp = initialModificationTimeStamp;
			initialModificationTimeStamp = currentModificationTimeStamp;
			if (isResourceAvailable) {
				ozMonitorMessage = filePath + " modification time has not changed. Currently " + DateTimeUtils
						.formatDate(currentModificationTimeStamp, DateTimeUtils.DATE_FORMAT_ddMMyyyy_HHmmssSSS) + ".";
			} else {
				ozMonitorMessage = filePath + " modification time changed from "
						+ DateTimeUtils.formatDate(previousModificationTimeStamp,
								DateTimeUtils.DATE_FORMAT_ddMMyyyy_HHmmssSSS)
						+ " to " + DateTimeUtils.formatDate(currentModificationTimeStamp,
								DateTimeUtils.DATE_FORMAT_ddMMyyyy_HHmmssSSS)
						+ " !";
			}
		}
		return generateOzMonitorResponse(isResourceAvailable, ozMonitorMessage, null);
	}

	public final void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
