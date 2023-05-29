package oz.monitor.monitors;

import java.util.Properties;

import oz.infra.reflection.ReflectionUtils;
import oz.infra.regexp.RegexpUtils;
import oz.monitor.common.OzMonitorResponse;
import oz.monitor.common.OzMonitorUtils;

public class RunScriptMonitor extends AbstractOzMonitor {
	private String script;
	private String[] scriptParameters;

	public RunScriptMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this, this.getClass().getSuperclass());
	}

	public OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		logger.finest("Monitoring " + scriptParameters[0] + " resource: " + getResource());
		return OzMonitorUtils.runCommand(this, scriptParameters,
				ozMonitorDispatcher.isBooleanMonitorAvailabilityChange());
	}

	public final void setScript(final String script) {
		scriptParameters = script.trim().split(RegexpUtils.REGEXP_WHITE_SPACE);
	}
}
