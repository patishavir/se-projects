package oz.monitor;

public interface OzMonitorMBean {
	public void exit();

	public int getNumberOfMonitors();

	public String getMonitorsStatusReport();
}
