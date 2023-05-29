package oz.infra.fibi.sms;

public class SmsMessage {
	private String part1 = "<?xml version=\"1.0\" encoding=\"iso-8859-8\"?><T50CSMSM><T50CSMSM-ISURGENT>1</T50CSMSM-ISURGENT><T50CSMSM-DATA>";
	private String messageTest = null;
	private String part2 = "</T50CSMSM-DATA><T50CSMSM-CEL-PREF>";
	private String numberPrefix = null;
	private String part3 = "</T50CSMSM-CEL-PREF><T50CSMSM-CEL-NUM>";
	private String number = null;
	private String part4 = "</T50CSMSM-CEL-NUM></T50CSMSM>";

	public SmsMessage(final String messageTest, final String numberPrefix, final String number) {
		this.messageTest = messageTest;
		this.numberPrefix = numberPrefix;
		this.number = number;
	}

	public String getAsString() {
		return part1 + messageTest + part2 + numberPrefix + part3 + number + part4;
	}
}
