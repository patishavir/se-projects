package oz.infra.http;

import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DownloadGzipFromUrl {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String url = "http://snifp-http01/waslogs/beta01/logs4Scp.tar.gz";
		System.out.println(url);

		try {
			URL site = new URL(url);
			URLConnection uc = site.openConnection();

			java.io.BufferedInputStream in = new java.io.BufferedInputStream(uc.getInputStream());
			GZIPInputStream gzin = new GZIPInputStream(in);

			java.io.FileOutputStream fos = new java.io.FileOutputStream("C:\\temp\\1.gzip");
			GZIPOutputStream gz = new GZIPOutputStream(fos);

			byte data[] = new byte[1024];

			int gsize = 0;
			while ((gsize = gzin.read(data)) != -1) {
				gz.write(data, 0, gsize);
			}

			gz.flush();
			gz.close();
			in.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
