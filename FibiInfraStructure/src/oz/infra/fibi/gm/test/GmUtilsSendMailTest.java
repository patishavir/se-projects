package oz.infra.fibi.gm.test;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.datetime.DateTimeUtils;
import oz.infra.fibi.gm.GMUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.print.PrintUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.varargs.VarArgsUtils;

public class GmUtilsSendMailTest implements Runnable {
	private String[] gmEnvironments = null;
	private static final Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// testGmSendMail();
		SystemUtils.validateClassPath();
		GmUtilsSendMailTest gmUtilsSendMailTest = new GmUtilsSendMailTest(args);
		gmUtilsSendMailTest.run();

	}

	public GmUtilsSendMailTest(final String[] args) {
		String[] defaultGmEnvironments = { GMUtils.GM_TEST_ENVIRONMENT, GMUtils.GM_Q_ENVIRONMENT, GMUtils.GM_PROD_ENVIRONMENT };
		if (args.length > 0) {
			gmEnvironments = args;
		} else {
			gmEnvironments = defaultGmEnvironments;
		}
	}

	public void run() {
		for (String gmEnvironment : gmEnvironments) {
			Properties prodProperties = GMUtils.getGmEmailDefaultProperties(gmEnvironment);
			Outcome outcome = testGmSendHtmlMail(prodProperties);
			logger.info(PrintUtils.getSeparatorLine("sent mail via " + gmEnvironment + " environment. Outcome: " + outcome.toString()));
		}
	}

	private static Outcome testGmSendHtmlMail(final Properties... properties) {
		Properties gmMailProperties = VarArgsUtils.getMyArg(GMUtils.getGmEmailDefaultProperties(), properties);
		gmMailProperties.put(GMUtils.HTML_BODY, getHtmlBody());
		// gmMailProperties.put(GMUtils.TO, "s177571,s137294, S178993");
		gmMailProperties.put(GMUtils.TO, "s177571");
		gmMailProperties.put(GMUtils.SUBJECT, StringUtils.concat(SystemUtils.getHostname(), ": send email test from ",
				gmMailProperties.getProperty(GMUtils.GM_ENVIRONMENT), " environment", " thread: " + Thread.currentThread().getName()));
		return GMUtils.sendEmail(gmMailProperties);
	}

	private static String getHtmlBody() {
		StringBuilder sb = new StringBuilder();
		sb.append("<HTML>");
		sb.append("<BODY>");
		sb.append("logfile:");
		sb.append("<BR>");
		sb.append("hello new gmail !");
		sb.append("<BR>");
		sb.append("<a href=\"\\\\matafcc\\temp$\" >Sql runner logs!</a>");
		sb.append("<BR>time sent:");
		sb.append(DateTimeUtils.getTimeStamp());
		sb.append("<BR>");
		sb.append("</BODY>");
		sb.append("</HTML>");
		return sb.toString();
	}

	private static void testGmSendMail() {
		Properties gmMailProperties = GMUtils.getGmEmailDefaultProperties();
		gmMailProperties.put(GMUtils.CONTENT, "content");
		gmMailProperties.put(GMUtils.TO, "s177571,s177571");
		GMUtils.sendEmail(gmMailProperties);
	}
}
