package oz.infra.sms;

public class SmsMessage {
	private String to = null;
	private String from = "Oded Zimerman";
	private String smsMessageText = null;

	public SmsMessage() {

	}

	public SmsMessage(final String to, final String from, final String smsMessageText) {
		this.to = to;
		this.from = from;
		this.smsMessageText = smsMessageText;
	}

	public String getFrom() {
		return from;
	}

	public String getSmsMessageText() {
		return smsMessageText;
	}

	public String getTo() {
		return to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setSmsMessageText(String smsMessageText) {
		this.smsMessageText = smsMessageText;
	}

	public void setTo(String to) {
		this.to = to;
	}
}