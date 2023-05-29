package oz.monitor.monitors;

import java.io.File;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.datetime.TimePeriod;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.monitor.OzMonitorParameters;
import oz.monitor.common.OzMonitorResponse;
import oz.monitor.common.OzMonitorUtils;

public class OzMonitorDispatcher implements Runnable {
	private static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	private static Logger logger = JulUtils.getLogger();
	private String responsibleOranization = "Mataf";
	private String enableMonitorOnHost;
	private String disableMonitorOnHost;
	private String[] disableMonitorOnHostArray = null;
	private String startTimeHHMM = OzMonitorParameters.getDefaultStartTimeHHMM();
	private String endTimeHHMM = OzMonitorParameters.getDefaultEndTimeHHMM();
	private String excludeDaysInWeek = "7";
	private int[] excludeDaysInWeekArray;
	private String intervalInSeconds = "300,random";
	// private String alerts = "alertLog,sms,gmemail";
	private String alerts = null;
	private String monitorClass;
	private String repeatTestCount = "0";
	private String actionOnFailure = null;
	private String actionOnSuccess = null;
	private String repeatTestIntervalInSeconds = "300";
	private String monitorAvailabilityChange = "yes";
	private String lastInvokationTimeStamp = null;
	//
	private int intervalInMilliSeconds;
	private boolean randomInterval = false;
	private Random random;
	//
	private String[] alertsArray;
	private boolean booleanMonitorAvailabilityChange = true;
	//
	private int repeatTestCountInt = 0;
	private int repeatTestIntervalInSecondsInt = 0;
	//
	private AbstractOzMonitor abstractOzMonitor = null;
	private boolean previousAvailability = true;
	private boolean currentAvailability;
	private int statusChangeCount = 0;
	private long resourceUnavailableStartTime = 0;
	private long resourceUnavailableEndTime = 0;
	private long resourceDownTime = 0;
	private String alertsRecipients = null;
	private String log4jLogName = OzMonitorDispatcher.class.getName();
	private org.apache.log4j.Logger log4jLogger = null;
	private ArrayList<TimePeriod> downTimeArrayList = new ArrayList<TimePeriod>();

	public OzMonitorDispatcher(final Properties monitorProperties) {
		alerts = OzMonitorParameters.getDefaultAlerts();
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		processMonitorParameters();
		String myHostName = SystemUtils.getLocalHostName();
		String[] enableMonitorOnHostArray = null;
		String resource = monitorProperties.getProperty("resource");
		if (enableMonitorOnHost != null && enableMonitorOnHost.length() > 0) {
			enableMonitorOnHostArray = enableMonitorOnHost.toLowerCase().split(OzConstants.COMMA);
		}
		if (((enableMonitorOnHostArray == null) || StringUtils.getEntryContainedInString(enableMonitorOnHostArray,
				myHostName.toLowerCase()) > OzConstants.STRING_NOT_FOUND)
				&& (StringUtils.getEntryContainedInString(disableMonitorOnHostArray,
						myHostName.toLowerCase())) == OzConstants.STRING_NOT_FOUND) {
			log4jLogger = org.apache.log4j.Logger.getLogger(log4jLogName);
			try {
				Class<?> monitorClassObject = Class.forName(monitorClass);
				Class<?> propertiesClassObject = Class.forName("java.util.Properties");
				Constructor<?> monitorConstructor = monitorClassObject
						.getConstructor(new Class[] { propertiesClassObject });
				abstractOzMonitor = (AbstractOzMonitor) monitorConstructor
						.newInstance(new Object[] { monitorProperties });
				logger.info("Monitor " + monitorClass + " " + resource + " has started on " + myHostName + " ...");
			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
			}
		} else {
			logger.warning(StringUtils.concat("**** ", monitorClass, " ", resource, " is disabled for ", myHostName,
					"!", " ****"));
		}
	}

	public AbstractOzMonitor getAbstractOzMonitor() {
		return abstractOzMonitor;
	}

	public final String[] getAlertsArray() {
		return alertsArray;
	}

	public String getAlertsRecipients() {
		return alertsRecipients;
	}

	public String getLastInvokationTimeStamp() {
		return lastInvokationTimeStamp;
	}

	private void insertDownTimeRecord(final String resource, final String environment,
			final String responsibleOranization, final long resourceUnavailableStartTime) {
	}

	public boolean isBooleanMonitorAvailabilityChange() {
		return booleanMonitorAvailabilityChange;
	}

	private void performAction(final String actionScript, final String[] ozMonitorResponseParams) {
		if (actionScript != null) {
			String[] actionScriptParameters = actionScript.split(OzConstants.COMMA);
			ArrayUtils.printArray(actionScriptParameters, OzConstants.TAB, "Performing action ");
			String[] scriptParameters = actionScriptParameters;
			if (scriptParameters == null || scriptParameters[0] == null || scriptParameters[0].trim().length() == 0) {
				scriptParameters = ozMonitorResponseParams;
			}
			if (scriptParameters != null && scriptParameters[0] != null && scriptParameters[0].trim().length() > 0) {
				File actionScriptFile = new File(scriptParameters[0]);
				OzMonitorResponse ozMonitorResponse = null;
				if (actionScriptFile.isFile()) {
					ozMonitorResponse = OzMonitorUtils.runCommand(abstractOzMonitor, scriptParameters);
				} else {
					String[] classMethod = actionScriptParameters[0].split(OzConstants.HASH);
					String className = classMethod[0];
					String methodName = classMethod[1];
					logger.info(className);
					String[] parameters = ArrayUtils.shift(actionScriptParameters, 1);
					Object result = ReflectionUtils.invokeMethod(className, methodName, parameters);
					ozMonitorResponse = (OzMonitorResponse) result;
				}
				OzMonitorUtils.sendAlerts(ozMonitorResponse.getOzMonitorMessage(),
						ozMonitorResponse.getOzMonitorLongMessage(), alertsArray, alertsRecipients);
			}
		}
	}

	private OzMonitorResponse performResourceAvailabilityTest() {
		currentAvailability = false;
		OzMonitorResponse ozMonitorResponse = null;
		for (int currentRepeatCount = 0; (!currentAvailability)
				&& currentRepeatCount < (repeatTestCountInt + 1); currentRepeatCount++) {
			ozMonitorResponse = abstractOzMonitor.isResourceStatusOK(this);
			logger.info(StringUtils.concat("Performed availability test ", String.valueOf(currentRepeatCount),
					" out of ", String.valueOf(repeatTestCountInt), " ", ozMonitorResponse.getOzMonitorMessage()));
			lastInvokationTimeStamp = DateTimeUtils.getCurrentDate(DateTimeUtils.DATE_FORMAT_ddMMyyyy_HHmmss);
			currentAvailability = ozMonitorResponse.isResourceStatus();
			if (!currentAvailability && currentRepeatCount < repeatTestCountInt && repeatTestIntervalInSecondsInt > 0) {
				logger.finest("Going to sleep for " + String.valueOf(repeatTestIntervalInSeconds) + " seconds! ("
						+ String.valueOf(currentRepeatCount) + ").");
				try {
					Thread.sleep(repeatTestIntervalInSecondsInt * OzConstants.INT_1000);
				} catch (Exception ex) {
					ExceptionUtils.printMessageAndStackTrace(ex);
				}
			}
		}
		return ozMonitorResponse;
	}

	private void processMonitorParameters() {
		String[] intervalParametersArray = intervalInSeconds.split(OzConstants.COMMA);
		intervalInMilliSeconds = OzConstants.INT_1000 * Integer.parseInt(intervalParametersArray[0]);
		if (intervalParametersArray.length == 2) {
			randomInterval = intervalParametersArray[1].equalsIgnoreCase("random");
			random = new Random(System.nanoTime());
		}
		alertsArray = alerts.split(OzConstants.COMMA);
		ArrayUtils.printArray(alertsArray, OzConstants.TAB, "Alerts array:");
		//
		booleanMonitorAvailabilityChange = monitorAvailabilityChange.equalsIgnoreCase("yes");
		if (repeatTestCount != null && repeatTestCount.length() > 0) {
			repeatTestCountInt = Integer.parseInt(repeatTestCount);
		}
		if (repeatTestIntervalInSeconds != null && repeatTestIntervalInSeconds.length() > 0) {
			repeatTestIntervalInSecondsInt = Integer.parseInt(repeatTestIntervalInSeconds);
		}
		if (excludeDaysInWeek != null && excludeDaysInWeek.length() > 0) {
			String[] excludeDayInWeekStringArray = excludeDaysInWeek.split(OzConstants.COMMA);
			excludeDaysInWeekArray = new int[excludeDayInWeekStringArray.length];
			for (int i = 0; i < excludeDaysInWeekArray.length; i++) {
				excludeDaysInWeekArray[i] = Integer.parseInt(excludeDayInWeekStringArray[i]);
			}
		}
	}

	private void processResourseStatusChange(final OzMonitorResponse ozMonitorResponse) {
		if (statusChangeCount == 0) {
			previousAvailability = currentAvailability;
			statusChangeCount++;
		}
		if ((previousAvailability != currentAvailability) && booleanMonitorAvailabilityChange) {
			logger.finest("Starting " + SystemUtils.getCurrentMethodName());
			String myResource = abstractOzMonitor.getResource();
			String availabilityMessage;
			if (currentAvailability && resourceUnavailableStartTime > 0) {
				logger.info(myResource + " has become available.");
				resourceUnavailableEndTime = System.currentTimeMillis();
				resourceDownTime = resourceUnavailableEndTime - resourceUnavailableStartTime;
				availabilityMessage = myResource + " is up now. Has been down for "
						+ DateTimeUtils.formatTime(resourceDownTime) + " from "
						+ timeFormatter.format(resourceUnavailableStartTime) + " to "
						+ timeFormatter.format(resourceUnavailableEndTime);
				log4jLogger.info(availabilityMessage);
				downTimeArrayList.get(downTimeArrayList.size() - 1).setEndTime(resourceUnavailableEndTime);
				downTimeArrayList.get(downTimeArrayList.size() - 1).setDuration(resourceDownTime);
			} else {
				resourceUnavailableStartTime = System.currentTimeMillis();
				availabilityMessage = myResource + " is unavailable since "
						+ timeFormatter.format(resourceUnavailableStartTime);
				log4jLogger.warn(availabilityMessage);
				downTimeArrayList.add(new TimePeriod(resourceUnavailableStartTime));
				insertDownTimeRecord(myResource, OzMonitorParameters.getEnvironment(), responsibleOranization,
						resourceUnavailableStartTime);
			}
			OzMonitorUtils.sendAlerts(availabilityMessage, ozMonitorResponse.getOzMonitorLongMessage(), alertsArray,
					alertsRecipients);
			previousAvailability = currentAvailability;
		}
	}

	public final void run() {
		try {
			while (abstractOzMonitor != null) {
				boolean isWithingTimeRange = DateTimeUtils.isWithinTimeOfDayRange(startTimeHHMM, endTimeHHMM);
				boolean isDayOfWeekExcluded = ArrayUtils.isIntInArray(excludeDaysInWeekArray,
						DateTimeUtils.getCurrentDayInWeek());
				if (isWithingTimeRange && (!isDayOfWeekExcluded)) {
					OzMonitorResponse ozMonitorResponse = performResourceAvailabilityTest();
					String logMessage = StringUtils.concat(SystemUtils.getLocalHostName(), " ",
							ozMonitorResponse.getOzMonitorMessage());
					if (currentAvailability) {
						log4jLogger.info(logMessage);
						processResourseStatusChange(ozMonitorResponse);
						performAction(actionOnSuccess, ozMonitorResponse.getActionScriptParameters());
					} else {
						log4jLogger.warn(logMessage);
						if (ozMonitorResponse.isPerformSendAlerts()) {
							OzMonitorUtils.sendAlerts(ozMonitorResponse.getOzMonitorMessage(),
									ozMonitorResponse.getOzMonitorLongMessage(), alertsArray, alertsRecipients);
						}
						processResourseStatusChange(ozMonitorResponse);
						if (ozMonitorResponse.isPerformOnFailureAction()) {
							performAction(actionOnFailure, ozMonitorResponse.getActionScriptParameters());
						}
					}
				}
				long timeToSleep = intervalInMilliSeconds;
				if (randomInterval) {
					timeToSleep = timeToSleep + (long) (random.nextFloat() * (float) timeToSleep);
					logger.finest("intervalInMilliSeconds: " + String.valueOf(intervalInMilliSeconds)
							+ "  timeToSleep: " + String.valueOf(timeToSleep));
				}
				Thread.sleep(timeToSleep);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
	}

	public final void setActionOnFailure(final String actionOnFailure) {
		this.actionOnFailure = actionOnFailure;
	}

	public final void setActionOnSuccess(final String actionOnSuccess) {
		this.actionOnSuccess = actionOnSuccess;
	}

	public final void setAlerts(final String alerts) {
		this.alerts = alerts;
	}

	public final void setAlertsRecipients(final String alertsRecipients) {
		this.alertsRecipients = alertsRecipients;
	}

	public final void setDisableMonitorOnHost(final String disableMonitorOnHost) {
		this.disableMonitorOnHost = disableMonitorOnHost;
		if (this.disableMonitorOnHost != null && this.disableMonitorOnHost.length() > 0) {
			disableMonitorOnHostArray = this.disableMonitorOnHost.toLowerCase().split(OzConstants.COMMA);
		}
	}

	public final void setEnableMonitorOnHost(final String enableMonitorOnHost) {
		this.enableMonitorOnHost = enableMonitorOnHost;
	}

	public final void setEndTimeHHMM(final String endTimeHHMM) {
		this.endTimeHHMM = endTimeHHMM;
		logger.info(" endTimeHHMM: " + endTimeHHMM);
	}

	public final void setExcludeDaysInWeek(final String excludeDaysInWeek) {
		this.excludeDaysInWeek = excludeDaysInWeek;
	}

	public final void setIntervalInSeconds(final String intervalInSeconds) {
		this.intervalInSeconds = intervalInSeconds;
	}

	public final void setLog4jLogName(final String log4jLogName) {
		this.log4jLogName = log4jLogName;
	}

	public final void setMonitorAvailabilityChange(final String monitorAvailabilityChange) {
		this.monitorAvailabilityChange = monitorAvailabilityChange;
	}

	public final void setMonitorClass(final String monitorClass) {
		this.monitorClass = monitorClass;
	}

	public final void setRepeatTestCount(final String repeatTestCount) {
		this.repeatTestCount = repeatTestCount;
	}

	public final void setRepeatTestIntervalInSeconds(final String repeatTestIntervalInSeconds) {
		this.repeatTestIntervalInSeconds = repeatTestIntervalInSeconds;
	}

	public final void setResponsibleOranization(final String responsibleOranization) {
		this.responsibleOranization = responsibleOranization;
	}

	public final void setStartTimeHHMM(final String startTimeHHMM) {
		this.startTimeHHMM = startTimeHHMM;
		logger.info(" startTimeHHMM: " + startTimeHHMM);
	}
}
