package oz.utils.sms;

import java.util.logging.Handler;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.sms.SmsMessage;
import oz.infra.string.StringUtils;
import oz.infra.system.env.EnvironmentUtils;
import oz.utils.sms.gui.SendSmsGui;

public class SendSmsParameters {
	private static final String LOG_FILE_PATH = "logFilePath";
	private static final String SHOW_SEND_SMS_GUI = "showSendSmsGui";
	private static final String RECIPIENT_LIST = "recipientList";
	private static final String FROM = "from";
	private static final String MESSAGE_TEXT = "messageText";
	private static Logger logger = JulUtils.getLogger();

	public static void processParameters(final String[] args) {
		logger.info("parameters: from \"message\" to (comma seperated)");
		SmsMessage smsParameters = new SmsMessage();
		String logFilePath = EnvironmentUtils.getEnvironmentVariable(LOG_FILE_PATH);
		if (logFilePath != null) {
			Handler handler = JulUtils.getFileHandler(logFilePath, true);
			JulUtils.addHandler(handler);
			logger.info(logFilePath + " file handler has been added");
		}
		String from = EnvironmentUtils.getEnvironmentVariable(FROM);
		smsParameters.setFrom(from);
		String showSendSmsGui = EnvironmentUtils.getActualEnvVarValue("yes", SHOW_SEND_SMS_GUI);
		if (showSendSmsGui.equalsIgnoreCase(OzConstants.YES)) {
			String recipientList = EnvironmentUtils.getEnvironmentVariable(RECIPIENT_LIST);
			new SendSmsGui().showGui(smsParameters, recipientList);
		} else {
			smsParameters.setFrom(args[0]);
			smsParameters.setSmsMessageText(args[1]);
			String to = args[2].trim();
			if (StringUtils.isJustDigits(to) && to.length() == OzConstants.INT_10) {
				to = to.substring(0, OzConstants.INT_3) + OzConstants.MINUS_SIGN + to.substring(OzConstants.INT_3);
			}
			smsParameters.setTo(to);
			SmsUtils.sendSms(smsParameters);
		}
	}
}
