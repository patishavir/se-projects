package oz.infra.fibi.gm;

import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;

public class GMEmailParameters {
	private String addressType = "smtp";
	private String from = null;
	private String to = null;
	private String cc = null;
	private String bcc = null;
	private String replyTo = null;
	private String subject = null;
	private String content = null;
	private String htmlBody = null;

	private static Logger logger = JulUtils.getLogger();

	public final String asString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=\"1.0\"?>");
		stringBuilder.append("<email>");
		stringBuilder.append("<addressType>");
		if (getAddressType() != null) {
			stringBuilder.append(getAddressType());
		}
		stringBuilder.append("</addressType>");
		stringBuilder.append("<from>");
		if (getFrom() != null) {
			stringBuilder.append(getFrom());
		}
		stringBuilder.append("</from>");
		stringBuilder.append("<to>");
		if (getTo() != null) {
			stringBuilder.append(getTo());
		}
		stringBuilder.append("</to>");
		stringBuilder.append("<cc>");
		if (getCc() != null) {
			stringBuilder.append(getCc());
		}
		stringBuilder.append("");
		stringBuilder.append("</cc>");
		stringBuilder.append("<bcc>");
		if (getBcc() != null) {
			stringBuilder.append(getBcc());
		}
		stringBuilder.append("</bcc>");
		stringBuilder.append("<replyTo>");
		if (getReplyTo() != null) {
			stringBuilder.append(getReplyTo());
		}
		stringBuilder.append("</replyTo>");
		stringBuilder.append("<subject>");
		if (getSubject() != null) {
			stringBuilder.append(getSubject());
		}
		stringBuilder.append("</subject>");
		stringBuilder.append("<content>");
		if (getContent() != null) {
			stringBuilder.append(getContent());
		}
		stringBuilder.append("</content>");
		stringBuilder.append("<htmlBody>");
		if (getHtmlBody() != null) {
			stringBuilder.append("<![CDATA[");
			stringBuilder.append(getHtmlBody().replaceAll(OzConstants.LINE_FEED,
					OzConstants.HTML_BR));
			stringBuilder.append("]]>");
		}
		stringBuilder.append("</htmlBody>");
		stringBuilder.append("</email>");
		return stringBuilder.toString();
	}

	public final String getAddressType() {
		return addressType;
	}

	public final String getBcc() {
		return bcc;
	}

	public final String getCc() {
		return cc;
	}

	public final String getContent() {
		return content;
	}

	public final String getFrom() {
		return from;
	}

	public final String getHtmlBody() {
		return htmlBody;
	}

	public final String getReplyTo() {
		return replyTo;
	}

	public final String getSubject() {
		return subject;
	}

	public final String getTo() {
		return to;
	}

	public final void setAddressType(final String addressType) {
		String[] addressTypesArray = { "ad", "smtp", "racf" };
		if (ArrayUtils.isObjectInArray(addressTypesArray, addressType)) {
			this.addressType = addressType;
		} else {
			logger.warning("Invalid address type. Mail wil no be sent !");
		}

	}

	public final void setBcc(final String bcc) {
		this.bcc = bcc;
	}

	public final void setCc(final String cc) {
		this.cc = cc;
	}

	public final void setContent(final String content) {
		this.content = content;
	}

	public final void setFrom(final String from) {
		this.from = from;
	}

	public final void setHtmlBody(final String htmlBody) {
		this.htmlBody = htmlBody;
	}

	public final void setReplyTo(final String replyTo) {
		this.replyTo = replyTo;
	}

	public final void setSubject(final String subject) {
		this.subject = subject;
	}

	public final void setTo(final String to) {
		this.to = to;
	}
}
