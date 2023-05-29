package oz.utils.http;

import oz.infra.http.HTTPUtils;

public class DownloadGzipFromUrl {
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String url = args[0];
		String targetFolder = args[1];
		int returnCode = HTTPUtils.downloadGzipFromUrl(url, targetFolder);
		System.exit(returnCode);
	}
}
