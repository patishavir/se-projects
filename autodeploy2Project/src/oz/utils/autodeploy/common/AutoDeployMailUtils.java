package oz.utils.autodeploy.common;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.email.EmailUtils;
import oz.infra.fibi.FibiUtils;
import oz.infra.fibi.gm.GMUtils;
import oz.infra.io.FileUtils;
import oz.infra.ldap.LdapHelperUtils;
import oz.infra.ldap.LdapUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.properties.PropertiesUtils;
import oz.utils.autodeploy.ear.EarFileParameters;

public class AutoDeployMailUtils {
	private static final String START_MESSAGE_SUBJECT = "startMessageSubject";
	private static final String START_MESSAGE_TEXT = "startMessageText";
	private static final String COMPLETION_MESSAGE_SUBJECT = "completionMessageSubject";
	private static final String COMPLETION_MESSAGE_TEXT = "completionMessageText";
	private static final String USER_NAME = "s177571";

	private static final String PASSWORD = "ppui78";

	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());

	private static void generateDistributionList(final String distributionListFilePath, final String userName, final String password) {
		String[] namesArray = FileUtils.readTextFileWithEncoding2Array(distributionListFilePath, OzConstants.WIN1255_ENCODING);
		ArrayUtils.printArray(namesArray);
		String[] attributeNames = { LdapUtils.MAIL, LdapUtils.NAME };
		String distributionList = LdapHelperUtils.getDistributionList(attributeNames, LdapUtils.DISPLAY_NAME + "=", namesArray, userName, password,
				OzConstants.LINE_FEED);
		logger.info(distributionList);
		FileUtils.writeTextFileWithEncoding("c:\\temp\\z.txt", OzConstants.WIN1255_ENCODING, distributionList);
	}

	private static Properties getEmailProperties(final Properties emailProperties, final Properties emailMessagesProperties,
			final AutoDeployMailMessageEnum autoDeployMailMessageEnum, final EarFileParameters earFileParameters) {
		String subject = null;
		String htmlMsg = null;
		switch (autoDeployMailMessageEnum) {
		case START_MESSAGE:
			subject = emailMessagesProperties.getProperty(START_MESSAGE_SUBJECT);
			htmlMsg = emailMessagesProperties.getProperty(START_MESSAGE_TEXT);
			break;
		case COMPLETION_MESSAGE:
			subject = emailMessagesProperties.getProperty(COMPLETION_MESSAGE_SUBJECT);
			int returnCode = earFileParameters.getReturnCode();
			String outcome = "הצלחה";
			String outcomeEnglish = "success";
			if (returnCode != 0) {
				outcome = "כשלון";
				outcomeEnglish = "failure";
			}
			subject = subject.replaceAll("%outcome%", outcome);
			subject = subject.replaceAll("%outcomeEnglish%", outcomeEnglish);
			htmlMsg = emailMessagesProperties.getProperty(COMPLETION_MESSAGE_TEXT);
			break;
		default:
			logger.warning("Invalid option !");
		}
		String applicationName = earFileParameters.getApplicationName();
		String system = earFileParameters.getSystem();
		subject = subject.replaceAll("%applicationName%", applicationName);
		subject = subject.replaceAll("%system%", system);
		String snifitVersion = earFileParameters.getSnifitVersion();
		if (snifitVersion != null && snifitVersion.length() > 0) {
			subject = subject.concat(" גרסה " + snifitVersion);
		}
		emailProperties.put("subject", subject);
		htmlMsg = htmlMsg.replaceAll("%subject%", subject);
		emailProperties.put("htmlBody", htmlMsg);
		emailProperties.put("htmlMsg", htmlMsg);
		return emailProperties;
	}

	public static void main(final String[] args) {
		generateDistributionList(args[0], USER_NAME, PASSWORD);
	}

	private static void sendGmMail(final Properties emailMessagesProperties, final AutoDeployMailMessageEnum autoDeployMailMessageEnum,
			final EarFileParameters earFileParameters) {
		Properties gmMailProperties = GMUtils.getGmEmailDefaultProperties(AutoDeployGlobalParameters.getGmEnvironment());
		logger.finest(PropertiesUtils.getAsDelimitedString(gmMailProperties));
		gmMailProperties.put(GMUtils.TO, earFileParameters.getGmMailTo());
		gmMailProperties.put(GMUtils.FROM, earFileParameters.getGmMailFrom());
		Properties emailProperties = getEmailProperties(gmMailProperties, emailMessagesProperties, autoDeployMailMessageEnum, earFileParameters);
		GMUtils.sendEmail(emailProperties);
	}

	public static void sendMail(final Properties emailMessagesProperties, final AutoDeployMailMessageEnum autoDeployMailMessageEnum,
			final EarFileParameters earFileParameters) {
		if (AutoDeployGlobalParameters.isSendMail()) {
			String gmMailTo = earFileParameters.getGmMailTo();
			String smtpMailTo = earFileParameters.getSmtpMailTo();
			if (gmMailTo != null && gmMailTo.length() > 0) {
				sendGmMail(emailMessagesProperties, autoDeployMailMessageEnum, earFileParameters);
			}
			if (smtpMailTo != null && smtpMailTo.length() > 0) {
				sendSmtpMail(emailMessagesProperties, autoDeployMailMessageEnum, earFileParameters);
			}
		}
	}

	private static void sendSmtpMail(final Properties emailMessagesProperties, final AutoDeployMailMessageEnum autoDeployMailMessageEnum,
			final EarFileParameters earFileParameters) {
		Properties smtpEmailProperties = FibiUtils.getFbiMailProperties();
		logger.info(PropertiesUtils.getAsDelimitedString(smtpEmailProperties));
		String to = earFileParameters.getSmtpMailTo();
		smtpEmailProperties.put("to", to);
		smtpEmailProperties.put("from", earFileParameters.getSmtpMailFrom());
		Properties emailProperties = getEmailProperties(smtpEmailProperties, emailMessagesProperties, autoDeployMailMessageEnum, earFileParameters);
		EmailUtils.sendHtmlEmail(emailProperties);
	}
}
