package oz.monitor.monitors.debug;

import java.util.Properties;

import oz.infra.reflection.ReflectionUtils;
import oz.monitor.common.OzMonitorResponse;
import oz.monitor.monitors.AbstractOzMonitor;
import oz.monitor.monitors.OzMonitorDispatcher;

public class SuccessMonitor extends AbstractOzMonitor {
	public SuccessMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this.getClass().getSuperclass());
	}

	public final OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		boolean resourceStatus = true;
		String ozMonitorMessage = "Resource is not available";
		return new OzMonitorResponse(resourceStatus, ozMonitorMessage, null);
	}
}
