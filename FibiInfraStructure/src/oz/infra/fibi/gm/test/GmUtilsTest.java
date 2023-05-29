package oz.infra.fibi.gm.test;

import java.util.Properties;

import oz.infra.datetime.DateTimeUtils;
import oz.infra.fibi.gm.GMUtils;

public class GmUtilsTest {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// testGmSendMail();
		testGmSendHtmlMail();
	}

	private static void testGmSendMail() {
		Properties gmMailProperties = GMUtils.getGmEmailDefaultProperties();
		gmMailProperties.put(GMUtils.CONTENT, "content");
		gmMailProperties.put(GMUtils.TO, "s177571,s177571");
		GMUtils.sendEmail(gmMailProperties);
	}

	private static void testGmSendHtmlMail() {
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
		Properties gmMailProperties = GMUtils.getGmEmailDefaultProperties();
		gmMailProperties.put(GMUtils.HTML_BODY, sb.toString());
		// gmMailProperties.put(GMUtils.TO, "s177571,s137294, S178993");
		gmMailProperties.put(GMUtils.TO, "s177571");
		GMUtils.sendEmail(gmMailProperties);
	}
}
