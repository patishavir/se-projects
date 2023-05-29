package oz.infra.fibi.gm.sms;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;

public class SmsParameters {
	private String xmlHeader = "<?xml version=\"1.0\"?>";
	private String smsStartTag = "<sms>";
	private String addressTypeStartTag = "<addressType>";
	private String addressType = "SMS";
	private String addressTypeEndTag = "</addressType>";
	private String fromStartTag = "<from>";
	private String from = "Oded Zimerman";
	private String fromEndTag = "</from>";
	private String toSatrtTag = "<to>";
	private String to = null;
	private String toEndTag = "</to>";
	private String smsMessageStartTag = "<smsMsg>";
	private String smsMessageText = null;
	private String smsMessageEndTag = "</smsMsg>";
	private String smsEndTag = "</sms>";

	private String[] toArray = null;
	private String gmEnvironment = "gmtest";
	private static final String ADDRESS_TYPE_AD = "ad";
	private static final int SMS_MAXIMAL_MESSAGE_LENGTH = 160;

	private static Logger logger = JulUtils.getLogger();

	public String getGmEnvironment() {
		return gmEnvironment;
	}

	public final String getSmsMessageText() {
		return smsMessageText;
	}

	public final String getTo() {
		return to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setGmEnvironment(final String gmEnvironment) {
		this.gmEnvironment = gmEnvironment;
	}

	public final void setSmsMessageText(final String smsMessageText) {
		String message = smsMessageText;
		if (message.length() > SMS_MAXIMAL_MESSAGE_LENGTH) {
			message = message.substring(0, SMS_MAXIMAL_MESSAGE_LENGTH);
		}
		this.smsMessageText = message;
	}

	public final void setTo(final String to) {
		this.to = to;
		toArray = to.trim().split(OzConstants.COMMA);
		if (toArray.length > 0
				&& !(RegexpUtils.matches(toArray[0], RegexpUtils.REGEXP_CELL_PHONE_NUMBER))) {
			addressType = ADDRESS_TYPE_AD;
		}
		logger.finest("To: " + to + " addressType: " + addressType);
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(xmlHeader);
		stringBuilder.append(smsStartTag);
		stringBuilder.append(addressTypeStartTag);
		stringBuilder.append(addressType);
		stringBuilder.append(addressTypeEndTag);
		stringBuilder.append(fromStartTag);
		stringBuilder.append(from);
		stringBuilder.append(fromEndTag);

		for (int i = 0; i < toArray.length; i++) {
			stringBuilder.append(toSatrtTag);
			stringBuilder.append(toArray[i].trim());
			stringBuilder.append(toEndTag);
		}
		stringBuilder.append(smsMessageStartTag);
		stringBuilder.append(smsMessageText);
		stringBuilder.append(smsMessageEndTag);
		stringBuilder.append(smsEndTag);
		return stringBuilder.toString();
	}
}
