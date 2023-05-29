package oz.infra.http.test;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.http.URLBreakDown;
import oz.infra.logging.jul.JulUtils;

public class UrlBreakDownTest {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// logger.info(new
		// OldURLBreakDown("http://xxx.yyy:9080/:+7").toString());
		// logger.info(new
		// OldURLBreakDown("http://xxx.yyy:9080/:+7//").toString());
		// logger.info(new OldURLBreakDown("http://xxx.yyy/:+7//").toString());
		// logger.info(new OldURLBreakDown("ftp://x/:+7//").toString());
		// logger.info(OzConstants.LINE_FEED + new
		// OldURLBreakDown("http://xxx.yyy/aaa/bbb?ccc=ddd").toString());
		// logger.info(OzConstants.LINE_FEED + new/
		// URLBreakDown("http://xxx.yyy/aaa/bbb?ccc=ddd").toString());
		// logger.info(OzConstants.LINE_FEED + new
		// OldURLBreakDown("https://xxx.yyy/aaa/bbb?ccc=ddd").toString());
		// logger.info(OzConstants.LINE_FEED + new
		// URLBreakDown("https://xxx.yyy/aaa/bbb?ccc=ddd").toString());
		// logger.info(OzConstants.LINE_FEED + new
		// OldURLBreakDown("HTTPS://xxx.yyy/aaa/bbb?ccc=ddd").toString());
		// logger.info(OzConstants.LINE_FEED + new
		// URLBreakDown("HTTPS://xxx.yyy/aaa/bbb?ccc=ddd").toString());
		// logger.info(OzConstants.LINE_FEED + new URLBreakDown("ldap:///OU=FIBI,DC=fibi,DC=corp").toString());
		logger.info(OzConstants.LINE_FEED + new URLBreakDown("ldap:///OU=FIBI,DC=fibi,DC=corp").toString());
	}

}
