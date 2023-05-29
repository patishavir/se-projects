package oz.monitor.monitors;

import java.util.logging.Logger;

import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.datetime.DateTimeUtils;
import oz.infra.logging.jul.JulUtils;
import oz.monitor.common.OzMonitorResponse;

public abstract class AbstractOzMonitor {
	protected static Logger logger = JulUtils.getLogger();
	private String resource;
	private String resourceIsOKMessage;
	private String resourceIsNotOKMessage;
	private String userName;
	private String password;
	private String decryptedPassword = null;
	private String encryptionMethod;
	private long lastAlertTimeStamp = 0;
	private String delayBetweenAlerts = null;
	protected long delayBetweenAlertsLong = 2 * DateTimeUtils.MILLIS_IN_AN_HOUR;

	protected AbstractOzMonitor() {
		logger.info("Constructing " + this.getClass().getName() + " ...");
	}

	public final OzMonitorResponse generateOzMonitorResponse(final boolean resourceIsAvailable,
			final String messageParameter, final String longMeassageParameter) {
		String ozMonitorMessage = null;
		if (resourceIsAvailable) {
			ozMonitorMessage = getActualResourceIsOKMessage(messageParameter);
			lastAlertTimeStamp = 0;
		} else {
			ozMonitorMessage = getActualResourceIsNotOKMessage(messageParameter);
		}
		OzMonitorResponse ozMonitorResponse = new OzMonitorResponse(resourceIsAvailable, ozMonitorMessage,
				longMeassageParameter);
		long currentTimeStamp = DateTimeUtils.getLocalTimeMillis();
		ozMonitorResponse.setPerformSendAlerts(false);
		if (!ozMonitorResponse.isResourceStatus() && (currentTimeStamp - lastAlertTimeStamp) > delayBetweenAlertsLong) {
			ozMonitorResponse.setPerformSendAlerts(true);
			lastAlertTimeStamp = currentTimeStamp;
		}
		return ozMonitorResponse;
	}

	public final OzMonitorResponse generateOzMonitorResponse(final boolean resourceIsAvailable,
			final String messageParameter, final String longMeassageParameter, final String[] actionScriptParameters) {
		OzMonitorResponse ozMonitorResponse = generateOzMonitorResponse(resourceIsAvailable, messageParameter,
				longMeassageParameter);
		ozMonitorResponse.setActionScriptParameters(actionScriptParameters);
		return ozMonitorResponse;
	}

	protected final String getActualResourceIsNotOKMessage(final String actualMeassage) {
		if (actualMeassage == null && resourceIsNotOKMessage == null) {
			return getResource() + " is not available!";
		}
		if (resourceIsNotOKMessage != null && resourceIsNotOKMessage.trim().length() > 0) {
			return resourceIsNotOKMessage;
		} else {
			return actualMeassage;
		}
	}

	protected final String getActualResourceIsOKMessage(final String actualMeassage) {
		if (actualMeassage == null && resourceIsOKMessage == null) {
			return getResource() + " is available!";
		}
		if (resourceIsOKMessage != null && resourceIsOKMessage.trim().length() > 0) {
			return resourceIsOKMessage;
		} else {
			return actualMeassage;
		}
	}

	public final String getEncryptionMethod() {
		return encryptionMethod;
	}

	protected final String getPassword() {
		if (decryptedPassword == null) {
			if (encryptionMethod == null || encryptionMethod.length() == 0) {
				encryptionMethod = EncryptionMethodEnum.PLAIN.toString();
			}
			EncryptionMethodEnum encryptionMethodEnum = EncryptionMethodEnum.valueOf(encryptionMethod);
			decryptedPassword = CryptographyUtils.decryptString(password, encryptionMethodEnum);
		}
		return decryptedPassword;
	}

	public final String getResource() {
		return resource;
	}

	protected final String getUserName() {
		return userName;
	}

	public abstract OzMonitorResponse isResourceStatusOK(OzMonitorDispatcher ozMonitorDispatcher);

	public final void setDelayBetweenAlerts(final String delayBetweenAlerts) {
		this.delayBetweenAlerts = delayBetweenAlerts;
		delayBetweenAlertsLong = Integer.parseInt(this.delayBetweenAlerts);
	}

	public final void setEncryptionMethod(final String encryptionMethod) {
		this.encryptionMethod = encryptionMethod;
	}

	public final void setPassword(final String password) {
		this.password = password;
	}

	public final void setResource(final String resource) {
		this.resource = resource;
	}

	public final void setResourceIsNotOKMessage(final String resourceIsNotOKMessage) {
		this.resourceIsNotOKMessage = resourceIsNotOKMessage;
	}

	public final void setResourceIsOKMessage(final String resourceIsOKMessage) {
		this.resourceIsOKMessage = resourceIsOKMessage;
	}

	public final void setUserName(final String userName) {
		this.userName = userName;
	}
}
