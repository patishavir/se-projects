package oz.monitor.monitors;

import java.net.URL;
import java.util.Properties;

import oz.infra.datetime.StopWatch;
import oz.infra.exception.ExceptionUtils;
import oz.infra.http.OzHttpUrl;
import oz.infra.reflection.ReflectionUtils;
import oz.monitor.common.OzMonitorResponse;

public class HttpConnectionTimeoutMonitor extends AbstractOzMonitor {
	private String url = null;
	private URL httpUrl = null;
	private int connectTimeoutinMilliseconds = Integer.MIN_VALUE;
	private int readTimeoutinMilliseconds = Integer.MIN_VALUE;

	public HttpConnectionTimeoutMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this, this.getClass().getSuperclass());
	}

	public OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		boolean resourceStatus = false;
		OzHttpUrl ozHttpUrl = new OzHttpUrl();
		StopWatch stopWatch = new StopWatch();
		resourceStatus = ozHttpUrl.openConnection(connectTimeoutinMilliseconds, readTimeoutinMilliseconds, httpUrl);
		String ozMonitorMessage = httpUrl.toString() + " has been successfully opened. "
				+ stopWatch.getElapsedTimeString();
		if (!resourceStatus) {
			ozMonitorMessage = httpUrl.toString() + ": " + ozHttpUrl.getExceptionMessage();
		}
		return new OzMonitorResponse(resourceStatus, ozMonitorMessage, null);
	}

	public void setConnectTimeoutinMilliseconds(final int connectTimeoutinMilliseconds) {
		this.connectTimeoutinMilliseconds = connectTimeoutinMilliseconds;
	}

	public void setReadTimeoutinMilliseconds(final int readTimeoutinMilliseconds) {
		this.readTimeoutinMilliseconds = readTimeoutinMilliseconds;
	}

	public void setUrl(final String url) {
		this.url = url;
		try {
			httpUrl = new URL(url);
		} catch (Exception ex) {
			ExceptionUtils.getStackTrace(ex);
		}
	}

}
