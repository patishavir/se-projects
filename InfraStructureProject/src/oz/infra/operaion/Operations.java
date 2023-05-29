package oz.infra.operaion;

import java.util.Properties;

public enum Operations {
	FORCE_SINGLE_INSTANCE("oz.infra.singleinstance.SingleInstanceUtils", "confirmSingleInstanceRun", null), WATCH(null, null, null), LOCALCOPY(null, null, null), REMOTECOPY("JschUtils", "scp", null), EMAIL(null, null,
			null);
	private final String className;
	private final String methodName;
	private Properties properties;

	private Operations(final String className, final String methodName, final Properties properties) {
		this.className = className;
		this.methodName = methodName;
		this.properties = properties;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
