package oz.monitor.common;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;
import oz.infra.system.SystemUtils;
import oz.monitor.OzMonitorParameters;
import oz.monitor.alert.AlertTypeEnum;
import oz.monitor.monitors.AbstractOzMonitor;

public final class OzMonitorUtils {
	private static Logger logger = JulUtils.getLogger();

	public static OzMonitorResponse runCommand(final AbstractOzMonitor abstractOzMonitor,
			final String[] actionScriptParameters) {
		return runCommand(abstractOzMonitor, actionScriptParameters, true);
	}

	public static OzMonitorResponse runCommand(final AbstractOzMonitor abstractOzMonitor,
			final String[] actionScriptParameters, final boolean monitorAvailabilityChange) {
		logger.finest(abstractOzMonitor.getResource());
		boolean isResourceAvailable = true;
		String ozMonitorMessage = null;
		String longOzMonitorMessage = OzConstants.EMPTY_STRING;
		try {
			SystemCommandExecutorResponse systemCommandExecutorResponse = SystemCommandExecutorRunner
					.run(actionScriptParameters);
			int scriptReturnCode = systemCommandExecutorResponse.getReturnCode();
			ozMonitorMessage = actionScriptParameters[0] + " completed. Return code: "
					+ String.valueOf(scriptReturnCode) + " .";
			logger.info(ozMonitorMessage);
			longOzMonitorMessage = "";
			String stdout = systemCommandExecutorResponse.getStdout().toString();
			if (stdout != null && stdout.length() > 0) {
				longOzMonitorMessage = SystemUtils.LINE_SEPARATOR + "Output:" + SystemUtils.LINE_SEPARATOR + stdout;
			}
			String stderr = systemCommandExecutorResponse.getStderr().toString();
			if (stderr != null && stderr.length() > 0) {
				longOzMonitorMessage = longOzMonitorMessage + SystemUtils.LINE_SEPARATOR + "Err:"
						+ SystemUtils.LINE_SEPARATOR + stderr;
			}
		} catch (Exception ex) {
			ozMonitorMessage = ex.getMessage();
			longOzMonitorMessage = ex.getMessage();
		}
		return abstractOzMonitor.generateOzMonitorResponse(isResourceAvailable, ozMonitorMessage, longOzMonitorMessage);
	}

	public static void sendAlerts(final String alertMessage, final String alertLongMessage, final String[] alertsArray,
			final String alertsRecipients) {
		final String alertType = "alertType";
		for (String currentAlert : alertsArray) {
			logger.info("Current alert: " + currentAlert);
			HashMap<String, Properties> alertsHashMap = OzMonitorParameters.getAlertsHashMap();
			logger.info("alertsHashMap size: " + String.valueOf(alertsHashMap.size()));
			Properties currentAlertProperties = alertsHashMap.get(currentAlert);
			if (currentAlertProperties != null) {
				String currentAlertType = currentAlertProperties.getProperty(alertType);
				logger.info("currentAlertType: " + currentAlertType);
				AlertTypeEnum.valueOf(currentAlertType).sendAlert(currentAlertProperties,
						SystemUtils.getLocalHostName() + " " + alertMessage, alertLongMessage, alertsRecipients);
			} else {
				logger.warning("Alert " + currentAlert + " is not defined !");
			}
		}
	}

	public static void sendHeartBeatMessage(final String alertsRecipients) {
		if (OzMonitorParameters.getSendHeartBeatMessageAt() != null
				&& OzMonitorParameters.getHeartBeatDestinations() != null) {
			String[] sendHearBeatMessageAtArray = OzMonitorParameters.getSendHeartBeatMessageAt()
					.split(OzConstants.COMMA);
			String[] alertsArray = OzMonitorParameters.getHeartBeatDestinations().split(OzConstants.COMMA);
			String hearBeatMessage = "Oz monitor is alive !";
			OzMonitorUtils.sendAlerts(hearBeatMessage, null, alertsArray, alertsRecipients);
			String nextHeartBeatHHMM = null;
			do {
				try {
					nextHeartBeatHHMM = null;
					if (sendHearBeatMessageAtArray.length == 1) {
						nextHeartBeatHHMM = sendHearBeatMessageAtArray[0];
					} else {
						nextHeartBeatHHMM = null;
						for (int i = 0; i < sendHearBeatMessageAtArray.length - 1; i++) {
							if (DateTimeUtils.isWithinTimeOfDayRange(sendHearBeatMessageAtArray[i],
									sendHearBeatMessageAtArray[i + 1])) {
								nextHeartBeatHHMM = sendHearBeatMessageAtArray[i + 1];
								String rangeMessage = "nextHeartBeatHHMM: " + nextHeartBeatHHMM + " range: "
										+ sendHearBeatMessageAtArray[i] + " - " + sendHearBeatMessageAtArray[i + 1];
								logger.finest(rangeMessage);
							}
						}
						if (nextHeartBeatHHMM == null) {
							nextHeartBeatHHMM = sendHearBeatMessageAtArray[0];
						}
					}
					long millisTillNextHeartBeat = DateTimeUtils.getMillisTillNextHHMM(nextHeartBeatHHMM);
					logger.info(" Next heartBeat at " + nextHeartBeatHHMM + ". sleeping for: "
							+ String.valueOf(millisTillNextHeartBeat / OzConstants.INT_1000) + " seconds !");
					Thread.sleep(millisTillNextHeartBeat);
					logger.finest("sending alerts !");
					if (DateTimeUtils.getDayOfWeek() != Calendar.SATURDAY) {
						OzMonitorUtils.sendAlerts(hearBeatMessage, null, alertsArray, alertsRecipients);
					}
				} catch (Exception ex) {
					logger.warning(ex.getMessage());
					ex.printStackTrace();
				}
			} while (true);
		}
	}

	private OzMonitorUtils() {
	}
}
