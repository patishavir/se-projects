package oz.utils.email;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.email.EmailUtils;
import oz.infra.email.EmailUtils.EmailTypeEnum;
import oz.infra.logging.jul.JulUtils;
import oz.infra.properties.PropertiesUtils;

public class EmailUtilsMain {

	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		if (args.length < 1) {
			logger.severe("properties file parameter has not been be specified.\nProcessing aborted!");
			System.exit(-1);
		}
		// TODO accept folder as parameter and process all files in folder
		Properties properties = PropertiesUtils.loadPropertiesFile(args[0]);
		String emailType = properties.getProperty("emailClass");
		EmailTypeEnum emailTypeEnum = EmailTypeEnum.valueOf(emailType);
		String messageID = null;
		switch (emailTypeEnum) {
		case SimpleEmail:
			messageID = EmailUtils.sendSimpleEmail(properties);
			break;
		case MultiPartEmail:
			messageID = EmailUtils.sendMultiPartEmail(properties);
			break;
		case HtmlEmail:
			messageID = EmailUtils.sendHtmlEmail(properties);
			break;
		default:
			logger.info(emailType + " is an invalid email type !");
		}
		int returnCode = -1;
		if (messageID != null) {
			returnCode = 0;
		}
		logger.finest("Return Code: " + String.valueOf(returnCode));
		System.exit(returnCode);
	}

}
