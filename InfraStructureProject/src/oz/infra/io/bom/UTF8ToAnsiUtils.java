package oz.infra.io.bom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class UTF8ToAnsiUtils {

	// FEFF because this is the Unicode char represented by the UTF-8 byte order
	// mark (EF BB BF).
	public static final String UTF8_BOM = "\uFEFF";

	public static void main(final String args[]) {
		try {
			if (args.length != 2) {
				System.out.println("Usage : java UTF8ToAnsiUtils utf8file ansifile");
				System.exit(1);
			}

			boolean firstLine = true;
			FileInputStream fis = new FileInputStream(args[0]);
			BufferedReader r = new BufferedReader(new InputStreamReader(fis, "UTF8"));
			FileOutputStream fos = new FileOutputStream(args[1]);
			Writer w = new BufferedWriter(new OutputStreamWriter(fos, "Cp1252"));
			for (String s = ""; (s = r.readLine()) != null;) {
				if (firstLine) {
					s = UTF8ToAnsiUtils.removeUTF8BOM(s);
					firstLine = false;
				}
				w.write(s + System.getProperty("line.separator"));
				w.flush();
			}

			w.close();
			r.close();
			System.exit(0);
		}

		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static String removeUTF8BOM(String s) {
		if (s.startsWith(UTF8_BOM)) {
			s = s.substring(1);
		}
		return s;
	}
}