package oz.monitor.alert;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.email.EmailUtils;
import oz.infra.fibi.gm.GMUtils;
import oz.infra.fibi.gm.sms.GmSmsParameters;
import oz.infra.io.FileUtils;
import oz.infra.mq.MQUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.monitor.OzMonitorParameters;

public enum AlertTypeEnum {
	SEND_EMAIL, SEND_GMEMAIL, SEND_SMS, WRITE_MQ_MESSAGE, WRITE_ALERT_LOG_RECORD, WRITE_ALERT_LOG_FILE;
	private static final String FROM = "from";
	private Logger logger = Logger.getLogger(AlertTypeEnum.class.toString());

	public final int sendAlert(final Properties alertPropertiesParam, final String alertMessage,
			final String alertLongMessage, final String alertsRecipients) {
		int returnCode = 0;
		Properties alertProperties = PropertiesUtils.duplicate(alertPropertiesParam);
		if (alertProperties.get(GMUtils.TO) != null && alertsRecipients != null) {
			alertProperties.put(GMUtils.TO, alertsRecipients);
		}
		logger.finest("Processing alert " + this.name() + ". Message: " + alertMessage);
		switch (this) {
		case SEND_EMAIL:
			sendEmail(alertProperties, alertMessage, alertLongMessage);
			break;
		case SEND_GMEMAIL:
			alertProperties.put(FROM, OzMonitorParameters.getEnvironment());
			sendGmEmail(alertProperties, alertMessage, alertLongMessage);
			break;
		case SEND_SMS:
			sendSms(alertProperties, alertMessage);
			break;
		case WRITE_MQ_MESSAGE:
			writeMQMessage(alertProperties, alertMessage);
			break;
		case WRITE_ALERT_LOG_RECORD:
			String prefix = alertProperties.getProperty("prefix");
			if (alertLongMessage != null) {
				logger.info(prefix + ": " + alertMessage + SystemUtils.LINE_SEPARATOR + alertLongMessage);
			} else {
				logger.info(prefix + ": " + alertMessage);
			}
			break;
		case WRITE_ALERT_LOG_FILE:
			String path = alertProperties.getProperty("path");
			FileUtils.writeFile(path, alertMessage);
			logger.info("Message " + alertMessage + " has been written to " + path);
			break;
		default:
			logger.warning("Unknown alert type !");
			break;
		}
		return returnCode;
	}

	private void sendEmail(final Properties alertProperties, final String alertMessage, final String alertLongMessage) {
		Properties sendEmailProperties = (Properties) alertProperties.clone();
		sendEmailProperties.setProperty("subject", alertMessage);
		if (alertLongMessage == null || alertMessage.length() == 0) {
			sendEmailProperties.setProperty("message", alertMessage);
		} else {
			sendEmailProperties.setProperty("message", alertLongMessage);
		}
		PropertiesUtils.printProperties(sendEmailProperties, Level.FINEST);
		EmailUtils.sendSimpleEmail(sendEmailProperties);

	}

	private void sendGmEmail(final Properties alertProperties, final String alertMessage,
			final String alertLongMessage) {
		alertProperties.put("subject", alertMessage);
		if (alertLongMessage == null || alertMessage.length() == 0) {
			alertProperties.put("htmlBody", alertMessage);
		} else {
			alertProperties.put("htmlBody", alertLongMessage);
			logger.info(alertProperties.getProperty("htmlBody"));
		}
		GMUtils.sendEmail(alertProperties);
	}

	private void sendSms(final Properties alertProperties, final String alertMessage) {
		GmSmsParameters smsParameters = new GmSmsParameters();
		smsParameters.setSmsMessageText(alertMessage);
		smsParameters.setGmEnvironment(alertProperties.get(GMUtils.GM_ENVIRONMENT).toString());
		String[] phoneNumbersArray = alertProperties.get("to").toString().split(OzConstants.COMMA);
		for (String phoneNumber : phoneNumbersArray) {
			smsParameters.setTo(phoneNumber.trim());
			logger.finest("to: " + smsParameters.getTo() + "   messageText: " + smsParameters.getSmsMessageText());
			GMUtils.sendSms(smsParameters);
		}
	}

	private void writeMQMessage(final Properties alertProperties, final String alertMessage) {
		Properties writeMqMessageProperties = (Properties) alertProperties.clone();
		writeMqMessageProperties.put("message", alertMessage);
		MQUtils.putMessage(writeMqMessageProperties);
	}
}
