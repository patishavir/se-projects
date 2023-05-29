package oz.loganalyzer;

public class ServerNameEnvironment {
	private String serverName = null;
	private String environment = null;

	public ServerNameEnvironment(final String serverName, final String environment) {
		super();
		this.serverName = serverName;
		this.environment = environment;
	}

	public final String getEnvironment() {
		return environment;
	}

	public final String getServerName() {
		return serverName;
	}

	@Override
	public String toString() {
		return "ServerNameEnvironment [serverName=" + serverName + ", environment=" + environment
				+ "]";
	}
}
