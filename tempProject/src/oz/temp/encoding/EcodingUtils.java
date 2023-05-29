package oz.temp.encoding;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.mail.internet.MimeUtility;

import oz.infra.io.FileUtils;

public class EcodingUtils {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File myInputFile = new File("c:\\temp\\gz\\test5000.gz");
		String contents = FileUtils.readTextFile(myInputFile);

		try {
			FileInputStream fis = new FileInputStream(myInputFile);
			InputStream in = MimeUtility.decode(fis, "UTF8");
			// String decoded = MimeUtility.decodeText(contents);
			// System.out.println(decoded);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println(" all done");
	}

}
