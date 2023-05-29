package oz.infra.httpserver;

public class AccessLogRecord {
	/*
	 * LogFormat "{\"remoteHost\": \"%h\" , \"timeStamp\":  \"%{%d/%b/%Y:%T}t-%{msec_frac}t\",  \"requestFirstLine\": \"%r\" , \"status
	 * \": \"%>s\" , \"bytes\": %b ,\"websphereApplicationServer\": \"%{WAS}e\" ,\"responseTime\":%D ,\"referer\": \"%{Re ferer}i\" ,\"
	 * host\": \"%{Host}i\" ,\"jsessionId\": \"%{JSESSIONID}C\"   ,\"userAgent\": \"%{User-agent}i]\" }" json
	 */

	private String remoteHost = null;
	private String timeStamp = null;
	private String requestFirstLine = null;
	private String status = null;
	private String bytes = null;
	private String websphereApplicationServer = null;
	private String responseTime = null;
	private String referer = null;
	private String host = null;
	private String jsessionId = null;
	private String userAgent = null;

	public String getAsString() {
		return "\nremoteHost: " + remoteHost + "\ntimeStamp: " + timeStamp + "\nrequestFirstLine: " + requestFirstLine + "\nstatus: " + status
				+ "\nbytes: " + bytes + "\nwebsphereApplicationServer: " + websphereApplicationServer + "\nresponse time: " + responseTime
				+ "\nreferer: " + referer + "\nhost: " + host + "\njsessionId: " + jsessionId + "\nuserAgent: " + userAgent;

	}

	public String getBytes() {
		return bytes;
	}

	public String getHost() {
		return host;
	}

	public String getJsessionId() {
		return jsessionId;
	}

	public String getReferer() {
		return referer;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public String getRequestFirstLine() {
		return requestFirstLine;
	}

	public String getResponseTime() {
		return responseTime;
	}

	public String getStatus() {
		return status;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getWebsphereApplicationServer() {
		return websphereApplicationServer;
	}
}