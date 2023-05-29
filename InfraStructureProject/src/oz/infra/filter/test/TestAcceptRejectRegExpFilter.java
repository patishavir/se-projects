package oz.infra.filter.test;

import java.util.logging.Logger;

import oz.infra.filter.AcceptRejectRegExpFilter;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;

public class TestAcceptRejectRegExpFilter {
	private static final String filterString11 = "REJECT_ALL:MATCH_ALL:ACCEPT,^/RCM.*#REJECT,^/MatafServer.*#REJECT,.+\\.gif$#REJECT,.+\\.css$#REJECT,.+\\.js$";
	private static final String filterString12 = "REJECT_ALL:MATCH_ANY:ACCEPT,^/RCM.*#REJECT,^/MatafServer.*#REJECT,.+\\.gif$#REJECT,.+\\.css$#REJECT,.+\\.js$";
	private static final String filterString21 = "ACCEPT_ALL:MATCH_ALL:ACCEPT,^/RCM.*#REJECT,^/MatafServer.*#ACCEPT,^/RAMCM.*#REJECT,.+\\.gif$#REJECT,.+\\.css$#REJECT,.+\\.js$";
	private static final String filterString22 = "ACCEPT_ALL:MATCH_ANY:ACCEPT,^/RCM.*#REJECT,^/MatafServer.*#ACCEPT,\\.*yyy\\.*#REJECT,.+\\.gif$#REJECT,.+\\.css$#REJECT,.+\\.js$";
	private static final String filterString33 = "REJECT_ALL:MATCH_ANY:ACCEPT,^/RCM.+\\.gif$#ACCEPT,^/RCM.+\\.css$#ACCEPT,^/RCM.+\\.js$#ACCEPT,^/RCM.+\\.jpg#ACCEPT,^/RCM.+\\.png";
	// private static final String filterString =
	// "REJECT_ALL:MATCH_ANY:ACCEPT,^/JJJRCM.*";
	// private static final String filterString =
	// "REJECT_ALL:MATCH_ALL:ACCEPT,^/RCM.*";
	// private static final String filterString1 =
	// "REJECT_ALL:MATCH_ALL:ACCEPT,^/RCM.*#REJECT,^/MatafServer.*#REJECT,.+\\.gif$#REJECT,.+\\.css$#REJECT,.+\\.js$";
	// private static final String filterString2 =
	// "ACCEPT_ALL:MATCH_ALL:REJECT,^/MatafServer.*#REJECT,.+\\.gif$#REJECT,.+\\.css$#REJECT,.+\\.js$";
	// private static final String filterString3 =
	// "ACCEPT_ALL:MATCH_ALL:REJECT,.+\\.gif$#REJECT,.+\\.png$#REJECT,.+\\.css$#REJECT,.+\\.js$";
	// ----------------------------------------------------------------------

	private static final String string2Check1 = "/RCM/plugin?plugin=S";
	// private static final String string2Check2 =
	// "/MatafServer/plugin?plugin=FibiCustomPlugin&module=FibiCustomPlugin.Extension&page=getRiskScor";
	private static final String filePath = "./files/accesslog.log";
	private static final Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testAcceptRejectRegExpFilter1(filterString11, "/RCMxxx.gif",
		// "FALSE");
		// testAcceptRejectRegExpFilter1(filterString11, "/RCMxxx.giff",
		// "TRUE");
		// testAcceptRejectRegExpFilter1(filterString11, "/RCMxxx.js", "FALSE");
		// testAcceptRejectRegExpFilter1(filterString11, "/RRCMxxx.js",
		// "FALSE");
		testAcceptRejectRegExpFilter1(filterString33, "/RRCMxxx.js", "TRUE");
		testAcceptRejectRegExpFilter1(filterString33, "/RRCMxxx.gif", "TRUE");
		testAcceptRejectRegExpFilter1(filterString33, "/RRCMxxx.css", "TRUE");
		testAcceptRejectRegExpFilter1(filterString33, "/RRCMxxx.png", "TRUE");
		testAcceptRejectRegExpFilter1(filterString33, "/RRCMxxx.jpg", "TRUE");
		// testAcceptRejectRegExpFilter1(filterString12, "/RCMxxx.gif",
		// "FALSE");
		// testAcceptRejectRegExpFilter1(filterString12, "/RCMxxx.giff",
		// "TRUE");
		// testAcceptRejectRegExpFilter1(filterString12, "/RCCCCMxxx.giff",
		// "FALSE");
		// testAcceptRejectRegExpFilter1(filterString21, "/RCMxxx.gif",
		// "FALSE");
		// testAcceptRejectRegExpFilter1(filterString21, "/RCMxxx.giff",
		// "FALSE");
		// testAcceptRejectRegExpFilter1(filterString21, "/RCCMxxx.giff",
		// "FALSE");
		// testAcceptRejectRegExpFilter1(filterString22, "/RCMxxx.giff",
		// "FALSE");
		// testAcceptRejectRegExpFilter1(filterString22, "/RCMxxx.gif",
		// "FALSE");
		// testAcceptRejectRegExpFilter1(filterString22, "/RCMxxx/yyy.gif",
		// "TRUE");
		// testAcceptRejectRegExpFilter1(filterString1, "RCMxxx.css");
		// testAcceptRejectRegExpFilter1(filterString1, "RCMxxx.js");
		// testAcceptRejectRegExpFilter1(filterString1, "RCCMxxx.baba");
		// testAcceptRejectRegExpFilter1(filterString1, "RCMxxx.buba");
		// testAcceptRejectRegExpFilter1(filterString1, string2Check1);
		// testAcceptRejectRegExpFilter1(filterString1, string2Check2);
		// testAcceptRejectRegExpFilter1(filterString2, string2Check1);
		// testAcceptRejectRegExpFilter1(filterString2, string2Check2);
		// testAcceptRejectRegExpFilter2(filePath, filterString);
	}

	private static void testAcceptRejectRegExpFilter1(final String filterString, final String string2Test,
			final String expectedResult) {
		AcceptRejectRegExpFilter acceptRejectRegExpFilter = new AcceptRejectRegExpFilter(filterString);
		boolean verdict = acceptRejectRegExpFilter.accept(string2Test);
		logger.finest("filterString: " + filterString + " string2Test: " + string2Test + " verdict: "
				+ String.valueOf(verdict));
		System.out.print("\nexpectedResult:\t" + expectedResult);
	}

	private static void testAcceptRejectRegExpFilter2(final String filePath, final String filterString) {
		AcceptRejectRegExpFilter acceptRejectRegExpFilter = new AcceptRejectRegExpFilter(filterString);
		String[] fileArray = FileUtils.readTextFile2Array(filePath);
		for (String line : fileArray) {
			boolean verdict = acceptRejectRegExpFilter.accept(line);
			logger.finest("filterString: " + filterString + " line: " + line + " verdict: " + String.valueOf(verdict));
		}
		logger.info(
				String.valueOf(acceptRejectRegExpFilter.getRejectedStringsCounter()) + " records have been rejected.");
	}
}