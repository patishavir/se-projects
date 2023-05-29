package oz.infra.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import oz.infra.io.FileUtils;

public class MailUtils {
	private static final String NEWLINE = "\r\n";
	private Logger logger = Logger.getLogger(this.getClass().toString());

	/*
	 * buildMailMessage method
	 */
	public final String buildMailMessage(final String fromString, final String toString,
			final String ccString, final String subjectString, final String bodyString) {
		ArrayList<String> mailMessage = new ArrayList<String>();
		mailMessage.add("From:" + fromString + NEWLINE);
		mailMessage.add("To: " + toString + NEWLINE);
		mailMessage.add("Cc: " + ccString + NEWLINE);
		mailMessage.add("Subject: " + subjectString + NEWLINE);
		mailMessage.add("MIME-Version:1.0" + NEWLINE);
		mailMessage
				.add("Content-type: multipart/alternative; boundary=\"XXXX-SIMPLE-BOUNDRY-XXXX\""
						+ NEWLINE + NEWLINE + NEWLINE);
		mailMessage.add("--XXXX-SIMPLE-BOUNDRY-XXXX" + NEWLINE);
		mailMessage.add("Content-type: text/html;" + NEWLINE);
		mailMessage.add("	charset=\"windows-1255\"" + NEWLINE + NEWLINE + NEWLINE);
		mailMessage.add("<HTML><HEAD>" + NEWLINE);
		mailMessage
				.add("<META http-equiv=Content-Type content=\"text/html; charset=windows-1255\">"
						+ NEWLINE);
		mailMessage.add("</HEAD><BODY>" + NEWLINE);
		mailMessage.add(bodyString);
		mailMessage.add(NEWLINE + "</BODY></HTML>" + NEWLINE + NEWLINE);
		mailMessage.add("--XXXX-SIMPLE-BOUNDRY-XXXX--");
		mailMessage.trimToSize();
		StringBuffer mailMessageStringBuffer = new StringBuffer();
		for (int i = 0; i < mailMessage.size(); i++) {
			mailMessageStringBuffer.append((String) mailMessage.get(i));
		}
		String mailMessageString = mailMessageStringBuffer.toString();
		logger.finest(NEWLINE + mailMessageString);
		return mailMessageString;
	}

	public void putMessageinPickupDir(final String mailMessageString, final String mailFilePath) {
		File mailFile = new File(mailFilePath);
		FileUtils.writeFile(mailFile, mailMessageString);
	}

	public void sendSMTPMailMessage(final String fromString, final String toString,
			final String ccString, final String subjectString, final String bodyString,
			final String mailFilePath) {
		String mailMessageString = buildMailMessage(fromString, toString, ccString, subjectString,
				bodyString);
		putMessageinPickupDir(mailMessageString, mailFilePath);
	}
}
