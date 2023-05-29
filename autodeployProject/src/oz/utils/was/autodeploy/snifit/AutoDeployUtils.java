package oz.utils.was.autodeploy.snifit;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.email.EmailUtils;
import oz.infra.fibi.FibiUtils;
import oz.infra.io.FileUtils;
import oz.infra.ldap.LdapHelperUtils;
import oz.infra.ldap.LdapUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.properties.PropertiesUtils;

public class AutoDeployUtils {
	private final static String START_MESSAGE_SUBJECT = "startMessageSubject";
	private final static String START_MESSAGE_TEXT = "startMessageText";
	private final static String COMPLETION_MESSAGE_SUBJECT = "completionMessageSubject";
	private final static String COMPLETION_MESSAGE_TEXT = "completionMessageText";
	private final static String USER_NAME = "s177571";
	private final static String PASSWORD = "123k8uhxaoi";
	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());

	private static void generateDistributionList(final String distributionListFilePath, final String userName,
			final String password) {
		String[] namesArray = FileUtils.readTextFileWithEncoding2Array(distributionListFilePath,
				OzConstants.WIN1255_ENCODING);
		ArrayUtils.printArray(namesArray);
		String[] attributeNames = { LdapUtils.MAIL, LdapUtils.NAME };
		String distributionList = LdapHelperUtils.getDistributionList(attributeNames, LdapUtils.DISPLAY_NAME + "=",
				namesArray, userName, password, OzConstants.LINE_FEED);
		logger.info(distributionList);
		FileUtils.writeTextFileWithEncoding("c:\\temp\\z.txt", OzConstants.WIN1255_ENCODING, distributionList);
	}

	public static void main(final String[] args) {
		generateDistributionList(args[0], USER_NAME, PASSWORD);
	}

	public static void sendMail(final AutoDeployMailMessageEnum autoDeployMailMessageEnum,
			final Properties currentEarProperties) {
		Properties emailProperties = FibiUtils.getFbiMailProperties();
		logger.info(PropertiesUtils.getAsDelimitedString(currentEarProperties));
		logger.info(PropertiesUtils.getAsDelimitedString(emailProperties));
		String to = currentEarProperties.get("mailTo").toString();
		emailProperties.put("to", to);
		emailProperties.put("from", currentEarProperties.get("mailFrom"));
		String subject = null;
		String htmlMsg = null;
		Properties emailMessagesProperties = AutoDeployParameters.getEmailMessagesProperties();
		switch (autoDeployMailMessageEnum) {
		case START_MESSAGE:
			subject = emailMessagesProperties.getProperty(START_MESSAGE_SUBJECT);
			htmlMsg = emailMessagesProperties.getProperty(START_MESSAGE_TEXT);
			break;
		case COMPLETION_MESSAGE:
			subject = emailMessagesProperties.getProperty(COMPLETION_MESSAGE_SUBJECT);
			int returnCode = Integer.parseInt(currentEarProperties.getProperty("returnCode"));
			String outcome = "בהצלחה";
			if (returnCode != 0) {
				outcome = "בכשלון";
			}
			subject = subject.replaceAll("%outcome%", outcome);
			htmlMsg = emailMessagesProperties.getProperty(COMPLETION_MESSAGE_TEXT);
			break;
		default:
			logger.warning("Invalid option !");
		}
		String applicationNameSuffix = currentEarProperties.getProperty("applicationNameSuffix");
		subject = subject.replaceAll("%applicationNameSuffix%", applicationNameSuffix);
		emailProperties.put("subject", subject);
		htmlMsg = htmlMsg.replaceAll("%subject%", subject);
		emailProperties.put("htmlMsg", htmlMsg);
		logger.info(PropertiesUtils.getAsDelimitedString(emailProperties));
		EmailUtils.sendHtmlEmail(emailProperties);
	}
}