package oz.utils.sms;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.fibi.gm.GMUtils;
import oz.infra.fibi.sms.FibiSmsUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.regexp.RegexpUtils;
import oz.infra.sms.SmsMessage;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.system.env.EnvironmentUtils;

public class SmsUtils {
	private static final String SEND_SMS_MECHAMISM_GM = "GM";
	private static final String GM_ENVIRONMENT_PREFIX = "gm";
	private static final String SEND_SMS_MECHAMISM = "sendSmsMechanism";
	private static final String SEND_SMS_TRANSPORT = "sendSmsTransport";
	private static final String SEND_SMS_ENVIRONMENT = "sendSmsEnvironment";
	private static Logger logger = JulUtils.getLogger();

	public static void sendSms(final SmsMessage smsMessage) {
		String messageText = smsMessage.getSmsMessageText();
		String to = smsMessage.getTo();
		Outcome outcome = Outcome.SUCCESS;
		String smsSendMechanism = EnvironmentUtils.getActualEnvVarValue(SEND_SMS_MECHAMISM_GM, SEND_SMS_MECHAMISM);
		if ((!StringUtils.isJustDigits(to)) && (!RegexpUtils.matches(to, RegexpUtils.REGEXP_CELL_PHONE_NUMBER))) {
			smsSendMechanism = SEND_SMS_MECHAMISM_GM;
		}
		if (smsSendMechanism.equalsIgnoreCase(SEND_SMS_MECHAMISM_GM)) {
			String gmEnvironment = GM_ENVIRONMENT_PREFIX
					+ EnvironmentUtils.getActualEnvVarValue("prod", SEND_SMS_ENVIRONMENT);
			int retCode = GMUtils.sendSms(smsMessage, gmEnvironment);
			if (retCode != OzConstants.INT_0) {
				outcome = Outcome.FAILURE;
			}
		} else {
			if (RegexpUtils.matches(to, RegexpUtils.REGEXP_CELL_PHONE_NUMBER)) {
				String[] numberArray = to.split(OzConstants.MINUS_SIGN);
				String numberPrefix = numberArray[0];
				String number = numberArray[1];
				outcome = FibiSmsUtils.sendSms(messageText, numberPrefix, number);
			} else {
				logger.warning(to + " is not a valid  cell phone number.  Sms has not been sent !");
				outcome = Outcome.FAILURE;
			}
		}
		logger.info("sent to: " + to + " sent from: " + smsMessage.getFrom() + " outcome: " + outcome.toString()
				+ "  message: " + messageText + SystemUtils.LINE_SEPARATOR
				+ StringUtils.repeatChar('=', OzConstants.INT_150));
		JulUtils.closeHandlers();
	}
}
