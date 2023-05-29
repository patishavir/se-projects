package oz.infra.inputstream;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Scanner;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;

public class InputStreamUtils {

	public static String convertStreamToString(final InputStream is) {
		Scanner s = null;
		String result = OzConstants.EMPTY_STRING;
		try {
			s = new Scanner(is);
			s.useDelimiter("\\A");
			if (s.hasNext()) {
				result = s.next();
			}
		} finally {
			s.close();
		}
		return result;
	}

	public static String convertStreamToStr(final InputStream is) {
		String result = null;
		try {
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int ris = bis.read();
			while (ris != -1) {
				buf.write((byte) ris);
				ris = bis.read();
			}
			result = buf.toString();
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return result;
	}
}
