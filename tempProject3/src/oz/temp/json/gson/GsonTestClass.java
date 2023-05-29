package oz.temp.json.gson;

public class GsonTestClass {
	private String remoteHost;
	private String timeStamp;
	private String requestFirstLine;
	private int status;
	private String websphereApplicationServer;
	private int responseTime;
	private String referer;
	private String host;
	private String jsessionId;
	private String userAgent;
	public String getRemoteHost() {
		return remoteHost;
	}
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getRequestFirstLine() {
		return requestFirstLine;
	}
	public void setRequestFirstLine(String requestFirstLine) {
		this.requestFirstLine = requestFirstLine;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getWebsphereApplicationServer() {
		return websphereApplicationServer;
	}
	public void setWebsphereApplicationServer(String websphereApplicationServer) {
		this.websphereApplicationServer = websphereApplicationServer;
	}
	public int getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getJsessionId() {
		return jsessionId;
	}
	public void setJsessionId(String jsessionId) {
		this.jsessionId = jsessionId;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/*
	 * 
	 * "{\"remoteHost\": \"10.12.101.197\" , \"timeStamp\":  \"05/Feb/2019:13:52:44-389\",  \"requestFirstLine\": \"GET /favicon.ico HTTP/1.1\" , \"status\": \"403\" , \"bytes\": 286 ,\"websphereApplicationServer\": \"-\" ,\"responseTime\":261 ,\"referer\": \"-\" "
	 * +",\"host\": \"suswastest2.fibi.corp\" ,\"jsessionId\": \"-\"   ,\"userAgent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.110 Safari/537.36]\" }"
	 * ;
	 */
}
