package oz.infra.temp;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class CheckUrl {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			logger.info(assembleWorkplaceUrl2("www.fibi-maya.co.il"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static String assembleWorkplaceUrl(String localname) throws Exception {
		// String url = "http://www.fibi-online.co.il/WorkplaceXT";
		String url = null;
		if (url == null || url.length() == 0) {
			StringBuffer sbUrl = new StringBuffer();
			String protocol = "HTTP";
			String host = localname;
			String subdmn = "wpxt";
			String port = "444";
			String cntxtroot = "workplacwXT";
			if (protocol != null && protocol.length() > 0 && host != null && host.length() > 0
					&& cntxtroot != null && cntxtroot.length() > 0) {
				sbUrl.append(protocol + "://");
				if (subdmn != null && subdmn.length() > 0) {
					int ind = host.indexOf("www.");
					if (ind > -1) {
						host = host.substring(ind + 4);
					}
					sbUrl.append(subdmn + "." + host);
				} else
					sbUrl.append(host);
				if (port != null && port.length() > 0)
					sbUrl.append(":" + port + "/");
				else
					sbUrl.append("/");
				sbUrl.append(cntxtroot);
			}
			url = sbUrl.toString();
		}
		return url;
	}

	private static String assembleWorkplaceUrl2(String localname) throws Exception {
		// String url = "http://www.fibi-online.co.il/WorkplaceXT";
		String url = null;
		if (url == null || url.length() == 0) {
			StringBuffer sbUrl = new StringBuffer();
			String protocol = "HTTP";
			String host = localname;
			// String subdmn = "wpxt";
			// String port = "444";
			String port = null;
			String cntxtroot = "workplacwXT";
			if (protocol != null && protocol.length() > 0 && host != null && host.length() > 0
					&& cntxtroot != null && cntxtroot.length() > 0) {
				sbUrl.append(protocol + "://");
				// if (subdmn != null && subdmn.length() > 0) {
				// int ind = host.indexOf("www.");
				// if (ind > -1) {
				// host = host.substring(ind + 4);
				// }
				// sbUrl.append(subdmn + "." + host);
				// } else
				sbUrl.append(host);
				if (port != null && port.length() > 0)
					sbUrl.append(":" + port + "/");
				else
					sbUrl.append("/");
				sbUrl.append(cntxtroot);
			}
			url = sbUrl.toString();
		}
		return url;
	}

}
