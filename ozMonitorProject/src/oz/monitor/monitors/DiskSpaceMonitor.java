package oz.monitor.monitors;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.io.filesystem.FileSystemDiskUsage;
import oz.infra.io.filesystem.FileSystemsUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.system.SystemUtils;
import oz.monitor.common.OzMonitorResponse;

public class DiskSpaceMonitor extends AbstractOzMonitor {
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private String percentUsedThreshold = "90";
	private String excludedFileSystems = null;
	private int percentUsedThresholdInt = OzConstants.INT_100;
	private String[] excludedFileSystemsArray = null;

	public DiskSpaceMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this, this.getClass().getSuperclass());
		percentUsedThresholdInt = Integer.parseInt(percentUsedThreshold);
		if (excludedFileSystems != null) {
			excludedFileSystemsArray = excludedFileSystems.split(OzConstants.COMMA);
		}
		logger.info("Starting " + this.getClass().getSimpleName());
	}

	public final OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		boolean diskSpaceIsAvailable = true;
		StringBuilder messageStringBuilder = new StringBuilder();
		StringBuilder longMessageStringBuilder = new StringBuilder();
		for (FileSystemDiskUsage fileSystemDiskUsage : FileSystemsUtils.getFileSystemsUsageList()) {
			boolean disk1SpaceIsAvailable = ArrayUtils.isObjectInArray(excludedFileSystemsArray,
					fileSystemDiskUsage.getName())
					|| ((fileSystemDiskUsage.getTotalSpaceBytes() > 0)
							&& (fileSystemDiskUsage.getPrecentageUsed() < percentUsedThresholdInt));
			if (disk1SpaceIsAvailable) {
				String longMessage = fileSystemDiskUsage.getName() + " " + fileSystemDiskUsage.getPrecentageUsed()
						+ "% " + fileSystemDiskUsage.getTotalSpaceBytes()
						+ " **************** File system is not full !";
				logger.finest(longMessage);
				longMessageStringBuilder.append(SystemUtils.LINE_SEPARATOR + longMessage);
			} else {
				diskSpaceIsAvailable = false;
				String message = fileSystemDiskUsage.getName() + " is " + fileSystemDiskUsage.getPrecentageUsed() + "% "
						+ fileSystemDiskUsage.getTotalSpaceBytes() + " full !";
				logger.info(message);
				messageStringBuilder.append(SystemUtils.LINE_SEPARATOR + message);
			}
		}
		if (diskSpaceIsAvailable) {
			messageStringBuilder.append(this.getClass().getSimpleName() + " is active !");
		}
		return generateOzMonitorResponse(diskSpaceIsAvailable, messageStringBuilder.toString(),
				messageStringBuilder.toString() + SystemUtils.LINE_SEPARATOR + longMessageStringBuilder.toString());
	}

	public final void setExcludedFileSystems(String excludedFileSystems) {
		this.excludedFileSystems = excludedFileSystems;
	}

	public final void setPercentUsedThreshold(String percentUsedThreshold) {
		this.percentUsedThreshold = percentUsedThreshold;
	}
}
