package oz.monitor.monitors;

import java.util.Properties;

import oz.infra.reflection.ReflectionUtils;
import oz.infra.system.performance.PerformanceUtils;
import oz.infra.system.performance.sna.StatusDependentLu;
import oz.monitor.common.OzMonitorResponse;

public class SnaLusMonitor extends AbstractOzMonitor {
	private int freeLus;
	private int activeLus;
	private int totalLus;

	public SnaLusMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this, this.getClass().getSuperclass());
	}

	private void getStatus_dependent_lu() {
		StatusDependentLu status_dependent_lu = PerformanceUtils.getSnaStatusDependentLu();
		freeLus = status_dependent_lu.getFreeLus();
		activeLus = status_dependent_lu.getActiveLus();
		totalLus = status_dependent_lu.getTotalLus();
	}

	public final OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		logger.info("Monitoring: " + getResource());
		getStatus_dependent_lu();
		StringBuilder ozMonitorMessageSB = new StringBuilder();
		ozMonitorMessageSB.append("Free LUs: ");
		ozMonitorMessageSB.append(String.valueOf(freeLus));
		ozMonitorMessageSB.append(" Active LUs: ");
		ozMonitorMessageSB.append(String.valueOf(activeLus));
		ozMonitorMessageSB.append(" Total LUs: ");
		ozMonitorMessageSB.append(totalLus);
		String ozMonitorMessage = ozMonitorMessageSB.toString();
		boolean isResourceAvailable = (freeLus > 0);
		return generateOzMonitorResponse(isResourceAvailable, ozMonitorMessage, null);
	}
}
