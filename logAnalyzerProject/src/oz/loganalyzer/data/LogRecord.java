package oz.loganalyzer.data;

import java.util.Map;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;
import oz.infra.print.PrintUtils.PrintOption;
import oz.infra.regexp.RegexpUtils;
import oz.infra.string.StringUtils;
import oz.loganalyzer.LogAnalyzerParameters;
import oz.loganalyzer.ServerNameEnvironment;

public class LogRecord {
	private static final Map<String, ServerNameEnvironment> serverNameEnvironmentMap = LogAnalyzerParameters
			.getServerNameEnvironmentMap();
	private static Logger logger = JulUtils.getLogger();
	private static String[][] fieldNames = { { "date", "date" }, { "time", "time" }, { "hour", "hour" },
			{ "bytesSent", "bytesSent" }, { "remoteHost", "remoteHost" },
			{ "responseTimeMicroSeconds", "responseTimeMicroSeconds" }, { "status", "status" }, { "uri", "uri" },
			{ "applicationServer", "applicationServer" } };
	private String line = null;
	private String date = null;
	private String time = null;
	private String hour = null;
	private String hhmm = null;
	private int bytesSent = 0;
	private String remoteHost = null;
	private int responseTimeMicroSeconds = 0;
	private int status = Integer.MIN_VALUE;
	private String uri = null;
	private String application = null;
	private String applicationServer = "";
	private String environment = "";
	private boolean validRecord = false;

	public LogRecord(final String line) {
		this.line = line;
		String[] logLineArray = line.split(RegexpUtils.REGEXP_WHITE_SPACE);
		// logger.info("logLineArray.length: " +
		// String.valueOf(logLineArray.length) + " line: " + line);
		// logger.info("Caller method: " + SystemUtils.getCallerClassName()
		// + SystemUtils.getCallerMethodName());
		try {
			if (StringUtils.isJustDigits(logLineArray[logLineArray.length - 1])) {
				boolean processApplicationSever = (logLineArray.length == 10);
				remoteHost = logLineArray[0];
				date = logLineArray[1].substring(1, 12);
				time = logLineArray[1].substring(13);
				hour = time.substring(0, 2);
				hhmm = time.substring(0, 5);
				uri = logLineArray[4];
				int applicationNameStart = logLineArray[4].indexOf(OzConstants.SLASH) + 1;
				int applicationNameEnd = logLineArray[4].indexOf(OzConstants.SLASH, applicationNameStart);
				if (applicationNameEnd == OzConstants.STRING_NOT_FOUND) {
					applicationNameEnd = logLineArray[4].indexOf(OzConstants.DOT, applicationNameStart);
					if (applicationNameEnd == OzConstants.STRING_NOT_FOUND) {
						applicationNameEnd = logLineArray[4].length();
					}
				}
				application = logLineArray[4].substring(applicationNameStart, applicationNameEnd);
				String statusString = logLineArray[6];
				if (StringUtils.isJustDigits(statusString)) {
					status = Integer.parseInt(logLineArray[6]);
				} else {
					logger.info(line);
				}
				if (!logLineArray[7].equalsIgnoreCase(OzConstants.MINUS_SIGN)) {
					bytesSent = Integer.parseInt(logLineArray[7]);
				}

				if (processApplicationSever) {
					applicationServer = logLineArray[8];
					ServerNameEnvironment serverNameEnvironment = serverNameEnvironmentMap.get(applicationServer);
					if (serverNameEnvironment != null) {
						environment = serverNameEnvironment.getEnvironment();
					}
					if (environment == "") {
						if (logLineArray.length == 10 && status != 304
								&& !applicationServer.equals(OzConstants.MINUS_SIGN)) {
							logger.warning("No Environment for application server: " + applicationServer
									+ " log record: " + line.trim());
						}
					}
				}
				if (status != OzConstants.HTTP_STATUS_500) {
					responseTimeMicroSeconds = Integer.parseInt(logLineArray[logLineArray.length - 1]);
				} else {
					logger.warning("startus code 500: " + line);
				}
				validRecord = true;
			}
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	public String getApplication() {
		return application;
	}

	public final String getApplicationServer() {
		return applicationServer;
	}

	public int getBytesSent() {
		return bytesSent;
	}

	public String getDate() {
		return date;
	}

	public String getEnvironment() {
		return environment;
	}

	public String getHeader() {
		return PrintUtils.printObjectFields(this, fieldNames, OzConstants.COMMA, PrintOption.HEADER_ONLY);
	}

	public String getHhmm() {
		return hhmm;
	}

	public String getHour() {
		return hour;
	}

	public String getLine() {
		return line;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public int getResponseTimeMicroSeconds() {
		return responseTimeMicroSeconds;
	}

	// LogFormat "%h %l %u %t \"%r\" %>s %b %{RH}e %{WAS}e %D" was
	// %...a: Remote IP-address
	// %...A: Local IP-address
	// %...B: Bytes sent, excluding HTTP headers.
	// %...b: Bytes sent, excluding HTTP headers. In CLF format
	// i.e. a '-' rather than a 0 when no bytes are sent.
	// %...c: Connection status when response was completed.
	// 'X' = connection aborted before the response completed.
	// '+' = connection may be kept alive after the response is sent.
	// '-' = connection will be closed after the response is sent.
	// %...{FOOBAR}e: The contents of the environment variable FOOBAR
	// %...f: Filename
	// %...h: Remote host
	// %...H The request protocol
	// %...{Foobar}i: The contents of Foobar: header line(s) in the request
	// sent to the server.
	// %...l: Remote logname (from identd, if supplied)
	// %...m The request method
	// %...{Foobar}n: The contents of note "Foobar" from another module.
	// %...{Foobar}o: The contents of Foobar: header line(s) in the reply.
	// %...p: The canonical Port of the server serving the request
	// %...P: The process ID of the child that serviced the request.
	// %...q The query string (prepended with a ? if a query string exists,
	// otherwise an empty string)
	// %...r: First line of request
	// %...s: Status. For requests that got internally redirected, this is
	// the status of the *original* request --- %...>s for the last.
	// %...t: Time, in common log format time format (standard english format)
	// %...{format}t: The time, in the form given by format, which should
	// be in strftime(3) format. (potentially localized)
	// %...T: The time taken to serve the request, in seconds.
	// %...u: Remote user (from auth; may be bogus if return status (%s) is 401)
	// %...U: The URL path requested, not including any query string.
	// %...v: The canonical ServerName of the server serving the request.
	// %...V: The server name according to the UseCanonicalName setting.

	public final int getStatus() {
		return status;
	}

	public String getTime() {
		return time;
	}

	public final String getUri() {
		return uri;
	}

	public boolean isValidRecord() {
		return validRecord;
	}

	public void setBytesSent(int bytesSent) {
		this.bytesSent = bytesSent;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public void setResponseTimeMicroSeconds(int responseTimeMicroSeconds) {
		this.responseTimeMicroSeconds = responseTimeMicroSeconds;
	}

	// public String toString() {
	// StringBuilder sb = new StringBuilder();
	// sb.append("Time: " + time);
	// sb.append(" remote host: " + remoteHost);
	// sb.append(" responseTimeMicroSeconds: "
	// + String.valueOf(responseTimeMicroSeconds));
	// sb.append(" status: " + status);
	// sb.append(" bytesSent: " + String.valueOf(bytesSent));
	// sb.append(" uri: " + uri);
	// logger.finest(sb.toString());
	// return (sb.toString());
	// }
	public String toString() {
		return line;
	}

	public String toString(final String delimiter) {
		return PrintUtils.printObjectFields(this, fieldNames, delimiter, PrintOption.DATA_ONLY);
	}
}
