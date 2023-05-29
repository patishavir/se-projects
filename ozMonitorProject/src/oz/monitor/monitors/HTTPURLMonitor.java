package oz.monitor.monitors;

import java.util.Properties;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.exception.ExceptionUtils;
import oz.infra.http.HTTPUtils;
import oz.infra.http.win.HttpWinClientUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.system.SystemUtils;
import oz.monitor.common.OzMonitorResponse;

public class HTTPURLMonitor extends AbstractOzMonitor {
	enum ExpectedResponseConditionEnum {
		STRING_EQUAL, STRING_CONTAINS, STRING_ENDS_WITH, INT_EQUAL, INT_GREATER_THAN;
	}

	private String urls;
	private int maxResponseTime = OzConstants.INT_1000;
	private String urlsAvailabilityRequirement = "ALL";
	private String expectedResponse = null;
	private ExpectedResponseConditionEnum expectedResponseConditionEnum = null;
	private String expectedResponseValue = null;
	private String[] urlsArray;
	private String isAre = "is";
	private String ozMonitorMessage = null;
	private String ozMonitorLongMessage = null;

	private StopWatch stopWatch = new StopWatch();

	public HTTPURLMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this, this.getClass().getSuperclass());
		//
		urlsArray = urls.split(OzConstants.COMMA);
		if (urlsArray.length > 1) {
			isAre = "are";
		}
		if (urlsArray.length == 1 && ((getResource() == null) || (getResource().length() == 0))) {
			setResource(urlsArray[0]);
			logger.info("resource set to: " + getResource());
		}
	}

	public final OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		boolean isResourceAvailable = false;
		ozMonitorMessage = null;
		logger.finest("Monitoring " + urls);
		for (int j = 0; (j < urlsArray.length) && (!isResourceAvailable); j++) {
			String fullUrl = urlsArray[j];
			ozMonitorMessage = urls + " " + isAre + " unavailable!";
			ozMonitorLongMessage = null;
			stopWatch.start();
			if (expectedResponse == null) {
				isResourceAvailable = procesSatusLine(fullUrl);
			} else {
				isResourceAvailable = procesExpectedResponse(fullUrl);
			}
			if ((isResourceAvailable && urlsAvailabilityRequirement.equalsIgnoreCase("any"))
					|| (!isResourceAvailable)) {
				break;
			}
		}
		if (ozMonitorLongMessage == null) {
			ozMonitorLongMessage = ozMonitorMessage;
		}
		return generateOzMonitorResponse(isResourceAvailable, ozMonitorMessage, ozMonitorLongMessage);
	}

	private boolean procesExpectedResponse(final String fullUrl) {
		boolean isResourceAvailable = false;
		try {
			String trimmedResponseString = null;
			if (SystemUtils.isWindowsFlavorOS()) {
				trimmedResponseString = HttpWinClientUtils.getProtectedPageContents(fullUrl);
			} else {
				trimmedResponseString = HTTPUtils.getPageContents(fullUrl).trim();
			}
			logger.finest("response: " + trimmedResponseString + " response length: "
					+ String.valueOf(trimmedResponseString.length()));
			long responseTime = stopWatch.getElapsedTimeLong();
			String shortMessageResponse = trimmedResponseString;
			if (trimmedResponseString != null) {
				switch (expectedResponseConditionEnum) {
				case STRING_EQUAL:
					isResourceAvailable = trimmedResponseString.equals(expectedResponseValue);
					break;
				case STRING_CONTAINS:
					isResourceAvailable = (trimmedResponseString
							.indexOf(expectedResponseValue) > OzConstants.STRING_NOT_FOUND);
					if (isResourceAvailable) {
						shortMessageResponse = expectedResponseValue;
					}
					break;
				case STRING_ENDS_WITH:
					isResourceAvailable = trimmedResponseString.endsWith(expectedResponseValue);
					if (isResourceAvailable) {
						shortMessageResponse = expectedResponseValue;
					}
					break;
				case INT_EQUAL:
					isResourceAvailable = (Integer.parseInt(trimmedResponseString) == Integer
							.parseInt(expectedResponseValue));
					break;
				case INT_GREATER_THAN:
					isResourceAvailable = (Integer.parseInt(trimmedResponseString) > Integer
							.parseInt(expectedResponseValue));
					break;
				}

				if (isResourceAvailable) {
					ozMonitorMessage = urls + " " + isAre + " available!";
				}
				ozMonitorMessage = ozMonitorMessage + " Response time: " + String.valueOf(responseTime)
						+ " milliseconds.";
				if (isResourceAvailable && (responseTime > maxResponseTime)) {
					ozMonitorMessage = ozMonitorMessage.concat(" Response time limit exceeded!");
				}
				ozMonitorLongMessage = ozMonitorMessage + " reponse: " + trimmedResponseString;
				ozMonitorMessage = ozMonitorMessage + " reponse: " + shortMessageResponse;
				logger.finest("ozMonitorLongMessage: " + ozMonitorLongMessage);
				logger.finest("shortMessageResponse: " + shortMessageResponse);
			}
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return isResourceAvailable;

	}

	private boolean procesSatusLine(final String fullUrl) {
		boolean isResourceAvailable = false;
		String userName = getUserName();
		StatusLine statusLine = null;
		if (userName == null && userName.length() == 0) {
			statusLine = HTTPUtils.getStatusLine(fullUrl);
		} else {
			String password = getPassword();
			statusLine = HTTPUtils.getProtectedPageStausLineWithBasicAuthRawHeaders(fullUrl, userName, password);
		}
		long responseTime = stopWatch.getElapsedTimeLong();
		if (statusLine != null) {
			isResourceAvailable = ((statusLine.getStatusCode() == HttpStatus.SC_OK));
			if (isResourceAvailable) {
				ozMonitorMessage = urls + " " + isAre + " available!";
			}
			ozMonitorMessage = ozMonitorMessage + " status code: " + String.valueOf(statusLine.getStatusCode()) + " "
					+ statusLine.getReasonPhrase() + ". Response time: " + String.valueOf(responseTime)
					+ " milliseconds.";
			if (isResourceAvailable && (responseTime > maxResponseTime)) {
				ozMonitorMessage = ozMonitorMessage.concat(" Response time limit exceeded!");
			}
			logger.info("ozMonitorLongMessage: " + ozMonitorMessage);
		}
		return isResourceAvailable;
	}

	public void setExpectedResponse(final String expectedResponse) {
		this.expectedResponse = expectedResponse;
		String[] expectedResponseArray = this.expectedResponse.split(OzConstants.COMMA);
		expectedResponseConditionEnum = ExpectedResponseConditionEnum.valueOf(expectedResponseArray[0]);
		expectedResponseValue = expectedResponseArray[1];
		logger.info("expectedResponse: " + this.expectedResponse);
	}

	public final void setMaxResponseTime(final int maxResponseTime) {
		this.maxResponseTime = maxResponseTime;
		logger.info("maxResponseTime: " + String.valueOf(maxResponseTime));
	}

	public final void setUrls(final String urls) {
		this.urls = urls;
	}

	public final void setUrlsAvailabilityRequirement(final String urlsAvailabilityRequirement) {
		this.urlsAvailabilityRequirement = urlsAvailabilityRequirement;
	}
}
