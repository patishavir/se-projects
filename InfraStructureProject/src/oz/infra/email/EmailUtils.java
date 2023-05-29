package oz.infra.email;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

import oz.infra.constants.OzConstants;
import oz.infra.io.FolderUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;

public final class EmailUtils {
	public enum EmailTypeEnum {
		SimpleEmail, MultiPartEmail, HtmlEmail
	};

	public static final String ATTACHMENT_PATH_DELIMITER = OzConstants.COMMA;
	private static Logger logger = JulUtils.getLogger();

	private static String[] fixArray(final String[] array) {
		String[] fixedArray = array;
		if (fixedArray.length == 1) {
			fixedArray = new String[2];
			fixedArray[0] = array[0];
			fixedArray[1] = array[0];
		}
		return fixedArray;
	}

	private static void processAttachment(final Properties properties, final MultiPartEmail email)
			throws EmailException {
		String attachmentPath = (properties.getProperty("attachmentPath"));
		if (attachmentPath != null && attachmentPath.length() > 0) {
			String[] attachmentPathArray = attachmentPath.split(ATTACHMENT_PATH_DELIMITER);
			for (int i = 0; i < attachmentPathArray.length; i++) {
				String attachmentPath1 = StringUtils.substituteEnvironmentVariables(attachmentPathArray[i]);
				File attachmentFile = new File(attachmentPath1);
				if (attachmentFile.exists()) {
					if (attachmentFile.isDirectory()) {
						attachmentFile = FolderUtils.getLatestFile(attachmentFile);
						attachmentPath1 = attachmentFile.getAbsolutePath();
					}
					if (attachmentFile != null) {
						String attachmentName = attachmentFile.getName();
						EmailAttachment attachment = new EmailAttachment();
						attachment.setDescription(attachmentPath1);
						attachment.setDisposition(EmailAttachment.ATTACHMENT);
						attachment.setPath(attachmentPath1);
						attachment.setDescription(attachmentName);
						attachment.setName(attachmentName);
						email.attach(attachment);
					}
				}
			}
		}
	}

	private static void processCommonParameters(final Properties properties, final Email email) {
		logger.info(PropertiesUtils.getAsDelimitedString(properties));
		try {
			String hostname = properties.getProperty("hostname");
			email.setHostName(hostname);
			String to = properties.getProperty("to");
			String[] toArray = to.split(";");
			for (int i = 0; i < toArray.length; i++) {
				toArray[i] = toArray[i].trim();
				if (toArray[i].length() > 0) {
					String[] recipient1Array = toArray[i].split(",");
					recipient1Array = fixArray(recipient1Array);
					email.addTo(recipient1Array[0], recipient1Array[1]);
					logger.finest("receipient: " + recipient1Array[0] + OzConstants.BLANK + recipient1Array[1]);
				}
			}
			String authentication = properties.getProperty("authentication");
			if (authentication != null && authentication.length() > 0) {
				String[] authenticationArray = authentication.split(",");
				email.setAuthentication(authenticationArray[0], authenticationArray[1]);
			}
			String[] fromArray = properties.getProperty("from").split(OzConstants.COMMA);
			fromArray = fixArray(fromArray);
			email.setFrom(fromArray[0], fromArray[1]);
			email.setSubject(properties.getProperty("subject"));
			String message = properties.getProperty("message");
			if (message != null) {
				email.setMsg(properties.getProperty("message"));
			}
			String debugString = properties.getProperty("debug");
			boolean debug = false;
			if (debugString != null && debugString.equalsIgnoreCase("yes")) {
				debug = true;
			}
			email.setDebug(debug);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
	}

	public static String sendHtmlEmail(final Properties properties) {
		String messageID = null;
		HtmlEmail email = new HtmlEmail();
		email.setCharset("ISO-8859-8");
		processCommonParameters(properties, email);
		try {
			processAttachment(properties, email);
			email.setHtmlMsg(properties.getProperty("htmlMsg"));
			String textMsg = properties.getProperty("textMsg");
			if (textMsg != null) {
				email.setTextMsg(textMsg);
			}
			messageID = email.send();
			logger.info("Message has been successfully sent. id=" + messageID);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
		return messageID;
	}

	public static String sendMultiPartEmail(final Properties properties) {
		String messageID = null;
		try {
			// Create the email message
			MultiPartEmail email = new MultiPartEmail();
			processCommonParameters(properties, email);
			processAttachment(properties, email);
			// send the email
			messageID = email.send();
			logger.info("Message has been successfully sent. id=" + messageID);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
		return messageID;
	}

	public static String sendSimpleEmail(final Properties properties) {
		String messageID = null;
		SimpleEmail email = new SimpleEmail();
		processCommonParameters(properties, email);
		try {
			messageID = email.send();
			logger.info("Message has been successfully sent. id=" + messageID);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
		return messageID;
	}

	private EmailUtils() {
		super();
	}

}
