package oz.monitor.monitors;

import java.io.File;
import java.util.Properties;

import oz.infra.reflection.ReflectionUtils;
import oz.infra.string.StringUtils;
import oz.monitor.common.OzMonitorResponse;

public class FileExistsMonitor extends AbstractOzMonitor {
	private String filePath;

	public FileExistsMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this, this.getClass().getSuperclass());
		logger.info(StringUtils.concat("Starting monitor ", this.getClass().getName(), " resource: ", getResource(),
				" filePath: ", filePath));
	}

	public final OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		File file2Monitor = new File(filePath);
		boolean isResourceAvailable = file2Monitor.exists();
		logger.finest("file2Monitor.exists() returned: " + String.valueOf(isResourceAvailable));
		String ozMonitorMessage = filePath + " does not exist !";
		if (isResourceAvailable) {
			ozMonitorMessage = filePath + " exists !";
		}
		OzMonitorResponse ozMonitorResponse = generateOzMonitorResponse(isResourceAvailable, ozMonitorMessage, null);
		return ozMonitorResponse;
	}

	public final void setFilePath(final String filePath) {
		this.filePath = filePath;
	}
}
