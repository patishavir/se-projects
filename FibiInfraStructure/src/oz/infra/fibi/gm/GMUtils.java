package oz.infra.fibi.gm;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.fibi.gm.sms.GmSmsParameters;
import oz.infra.logging.jul.JulUtils;
import oz.infra.mq.MQUtils;
import oz.infra.operaion.Outcome;
import oz.infra.properties.PropertiesUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.sms.SmsMessage;
import oz.infra.varargs.VarArgsUtils;

public class GMUtils {
	public static final String GM_ENVIRONMENT = "gmEnvironment";
	public static final String ADDRESS_TYPE = "addressType";
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String CC = "cc";
	public static final String BCC = "bcc";
	public static final String REPLY_TO = "replyTo";
	public static final String SUBJECT = "subject";
	public static final String CONTENT = "content";
	public static final String HTML_BODY = "htmlBody";

	public static final String GM_TEST_ENVIRONMENT = "gmtest";
	public static final String GM_Q_ENVIRONMENT = "gmq";
	public static final String GM_PROD_ENVIRONMENT = "gmprod";

	public static final String GM_INTERNET_ENVIRONMENT = "gminternet";

	private static final Logger logger = JulUtils.getLogger();

	private static final int SMTP_MAX_SUBJECT_LENGTH = 300;

	public static Properties getGmEmailDefaultProperties(final String... gmEnvironments) {
		String gmEnvironment = VarArgsUtils.getMyArg(GM_TEST_ENVIRONMENT, gmEnvironments);
		Properties gmMailProperties = new Properties();
		gmMailProperties.put(GM_ENVIRONMENT, GM_TEST_ENVIRONMENT);
		if (gmEnvironment != null && gmEnvironment.length() > 0) {
			gmMailProperties.put(GM_ENVIRONMENT, gmEnvironment);
		}
		gmMailProperties.put(ADDRESS_TYPE, "ad");
		gmMailProperties.put(FROM, "Oded Z");
		gmMailProperties.put(TO, "s177571");
		gmMailProperties.put(CC, "");
		gmMailProperties.put(BCC, "");
		gmMailProperties.put(REPLY_TO, "");
		gmMailProperties.put(SUBJECT, "subject ...");
		gmMailProperties.put(CONTENT, "");
		gmMailProperties.put(HTML_BODY, "");
		return gmMailProperties;
	}

	private static Properties getGmMqProperties(final String gmEnvironment) {
		String packageName = GMUtils.class.getPackage().getName().replaceAll("\\.", "/");
		String propertiesFilePath = packageName + "/properties/" + gmEnvironment + ".properties";
		logger.finest(propertiesFilePath);
		Properties gmMqProperties = PropertiesUtils.loadPropertiesFromClassPath(propertiesFilePath);
		return gmMqProperties;
	}

	public static Outcome sendEmail(final GMEmailParameters gmEmailParameters, final String gmEnvironment) {
		Outcome outcome = Outcome.FAILURE;
		if (gmEmailParameters.getTo() == null || gmEmailParameters.getTo().length() < 1
				|| "ad,smtp,racf".indexOf(gmEmailParameters.getAddressType()) < 0
				|| ((gmEmailParameters.getSubject() == null || gmEmailParameters.getSubject().length() == 0)
						&& (gmEmailParameters.getContent() == null || gmEmailParameters.getContent().length() == 0))) {
			logger.warning("Invalid email parameters. Send email failed !");
		} else {
			String emailMessage = gmEmailParameters.asString();
			Properties gmMqProperties = getGmMqProperties(gmEnvironment);
			gmMqProperties.put("message", emailMessage);
			int retcode = MQUtils.putMessage(gmMqProperties);
			if (retcode == 0) {
				outcome = Outcome.SUCCESS;
			}
			logger.info("return code: ".concat(String.valueOf(retcode)));
		}
		return outcome;
	}

	public static Outcome sendEmail(final Properties gmEmailProperties) {
		Outcome outcome = Outcome.FAILURE;
		String to = gmEmailProperties.getProperty(TO);
		if (!RegexpUtils.matches(to, RegexpUtils.REGEXP_CELL_PHONE_NUMBER)) {
			GMEmailParameters gmEmailParameters = new GMEmailParameters();
			gmEmailParameters.setAddressType(gmEmailProperties.getProperty(ADDRESS_TYPE));
			gmEmailParameters.setFrom(gmEmailProperties.getProperty(FROM));
			gmEmailParameters.setTo(gmEmailProperties.getProperty(TO));
			gmEmailParameters.setCc(gmEmailProperties.getProperty(CC));
			gmEmailParameters.setBcc(gmEmailProperties.getProperty(BCC));
			gmEmailParameters.setReplyTo(gmEmailProperties.getProperty(REPLY_TO));
			String subject = gmEmailProperties.getProperty(SUBJECT);
			int subjectLength = subject.length();
			if (subjectLength > SMTP_MAX_SUBJECT_LENGTH) {
				subjectLength = SMTP_MAX_SUBJECT_LENGTH;
			}
			if (subject != null) {
				gmEmailParameters.setSubject(subject.substring(0, subjectLength));
			}
			gmEmailParameters.setContent(gmEmailProperties.getProperty(CONTENT));
			gmEmailParameters.setHtmlBody(gmEmailProperties.getProperty(HTML_BODY));
			outcome = sendEmail(gmEmailParameters, gmEmailProperties.getProperty(GM_ENVIRONMENT));
		}
		return outcome;
	}

	public static void sendEmail(final Properties gmEmailProperties, final String subject, final String body) {
		gmEmailProperties.put(GMUtils.HTML_BODY, body);
		gmEmailProperties.put(GMUtils.SUBJECT, subject);
		sendEmail(gmEmailProperties);
	}

	public static Outcome sendEmail(final String subject, final String body, final String receipients) {
		Properties gmEmailProperties = GMUtils.getGmEmailDefaultProperties();
		gmEmailProperties.put(GMUtils.HTML_BODY, body);
		gmEmailProperties.put(GMUtils.SUBJECT, subject);
		gmEmailProperties.put(GMUtils.TO, receipients);
		return sendEmail(gmEmailProperties);
	}

	public static int sendSms(final GmSmsParameters gmSmsParameters) {
		logger.finest("smsMessageText: " + gmSmsParameters.getSmsMessageText());
		Properties gmMqProperties = getGmMqProperties(gmSmsParameters.getGmEnvironment());
		gmMqProperties.put("message", gmSmsParameters.toString());
		return MQUtils.putMessage(gmMqProperties);
	}

	public static int sendSms(final SmsMessage smsMessage, final String gmEnvironment) {
		logger.finest("smsMessageText: " + smsMessage.getSmsMessageText());
		GmSmsParameters gmSmsParameters = new GmSmsParameters();
		gmSmsParameters.setFrom(smsMessage.getFrom());
		gmSmsParameters.setTo(smsMessage.getTo());
		gmSmsParameters.setSmsMessageText(smsMessage.getSmsMessageText());
		gmSmsParameters.setGmEnvironment(gmEnvironment);
		return sendSms(gmSmsParameters);
	}
}
