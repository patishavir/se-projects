package oz.monitor.monitors;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.datetime.DateTimeUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.string.StringUtils;
import oz.monitor.common.OzMonitorResponse;

public class FileSizeMonitor extends AbstractOzMonitor {
	private static Logger logger = JulUtils.getLogger();
	private String resource;
	private String filePath;
	private File file2Monitor = null;
	private String ignoreIfModifiedMoreThanSecondsAgo = null;
	private long ignoreIfModifiedMoreThanSecondsAgoLong = Long.MAX_VALUE;
	private long initialFileSize = -1;
	private long previousFileSize = -1;
	private boolean fileSizeChangesOK = false;
	private boolean file2MonitorExists = false;

	public FileSizeMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this, this.getClass().getSuperclass());
		logger.info(
				"Starting monitor " + this.getClass().getName() + " resource:" + resource + " filePath: " + filePath);
		file2Monitor = new File(filePath);
		if (!file2Monitor.exists()) {
			logger.severe(filePath + " does not exists !");
		}
		initialFileSize = file2Monitor.length();
	}

	public final OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		boolean isResourceAvailable = false;
		String ozMonitorMessage = filePath + " does not exist !";
		logger.finest("Monitoring resource: " + resource + ". file path: " + filePath);
		file2MonitorExists = file2Monitor.exists();
		if (file2MonitorExists) {
			long currentFileLength = file2Monitor.length();
			long lastModified = file2Monitor.lastModified();
			long diffInSeconds = DateTimeUtils.getDifferenceInSeconds(lastModified);
			isResourceAvailable = ((currentFileLength == initialFileSize)
					|| (diffInSeconds > ignoreIfModifiedMoreThanSecondsAgoLong)) && (!fileSizeChangesOK);
			previousFileSize = initialFileSize;
			initialFileSize = currentFileLength;
			if (isResourceAvailable) {
				ozMonitorMessage = StringUtils.concat(filePath, " size has not changed. Currently ",
						String.valueOf(currentFileLength), ".");
			} else {
				ozMonitorMessage = StringUtils.concat(filePath, " size has changed from ",
						String.valueOf(previousFileSize), " to ", String.valueOf(currentFileLength), " !");
			}
		}
		return generateOzMonitorResponse(isResourceAvailable, ozMonitorMessage, null);
	}

	public final void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setIgnoreIfModifiedMoreThanSecondsAgo(String ignoreIfModifiedMoreThanSecondsAgo) {
		this.ignoreIfModifiedMoreThanSecondsAgo = ignoreIfModifiedMoreThanSecondsAgo;
		if (ignoreIfModifiedMoreThanSecondsAgo != null && ignoreIfModifiedMoreThanSecondsAgo.length() > 0
				&& StringUtils.isJustDigits(ignoreIfModifiedMoreThanSecondsAgo)) {
			ignoreIfModifiedMoreThanSecondsAgoLong = Long.parseLong(this.ignoreIfModifiedMoreThanSecondsAgo);
			logger.info(StringUtils
					.concat("ignoreIfModifiedMoreThanSecondsAgo:" + this.ignoreIfModifiedMoreThanSecondsAgo));
		}
	}
}
