/*
 * Created on 31/08/2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package oz.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

/**
 * @author utxeddd
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FileGrabber {

	private static String FILE_SOURCE_PATH;
	private static String FILE_DESTINATION_PATH;
	private static String trustStorePath;
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		try {
			long start = System.currentTimeMillis();
			if (args.length != 3) {
				throw new Exception(
						"Error in FileGrabber->init: Incorrect number of input parameters.");
			} else {
				FILE_SOURCE_PATH = args[0];
				FILE_DESTINATION_PATH = args[1];
				trustStorePath = args[2];
			}
			InputStream fileIn = getInputStream();
			FileOutputStream fileOut = getFileOutputStream();
			copy(fileIn, fileOut);
			long end = System.currentTimeMillis();
			logger.info("FileGrabber: Copy from file " + FILE_SOURCE_PATH
					+ " to file " + FILE_DESTINATION_PATH + " completed.");
			logger.info("FileGrabber: Copy time is " + (end - start)
					+ " millisecond.");
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
		}
	}

	private static void copy(InputStream fileIn, FileOutputStream fileOut)
			throws IOException {
		int size = 1024 * 4;
		byte[] buf = new byte[size];
		int len;
		while ((len = fileIn.read(buf)) > 0) {
			fileOut.write(buf, 0, len);
		}
		fileIn.close();
		fileOut.close();
	}

	private static InputStream getInputStream() {
		try {
			if (FILE_SOURCE_PATH.toLowerCase().startsWith("http")) {
				System.setProperty("javax.net.ssl.trustStore", trustStorePath);
				System.setProperty("javax.net.ssl.trustStorePassword", "123456");
				URL url = new URL(FILE_SOURCE_PATH);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				String usrPwd = "Basic aHJzcnYwMTpoZXJtdGVzdA==";
				connection.setRequestProperty("Authorization", usrPwd);
				connection.connect();
				int rc = connection.getResponseCode();
				if (rc == HttpURLConnection.HTTP_NOT_AUTHORITATIVE
						|| rc == HttpURLConnection.HTTP_UNAUTHORIZED)
					throw new Exception();
				return connection.getInputStream();
			} else {
				return new FileInputStream(new File(FILE_SOURCE_PATH));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			return null;

		}
	}

	private static FileOutputStream getFileOutputStream()
			throws FileNotFoundException {
		try {
			return new FileOutputStream(new File(FILE_DESTINATION_PATH));
		} catch (Exception e) {
			throw new FileNotFoundException(
					"Error in FileGrabber->getFileOutputStream: problem during opening output stream.");
		}
	}

}
