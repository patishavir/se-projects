package oz.infra.exception.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Logger;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;


public class TestExceptionUtils {
	private static final Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		genException();
	}

	private static void genException() {
		StringBuffer contentsSb = new StringBuffer();
		// declared here only to make visible to finally clause
		BufferedReader bufferedReader = null;
		try {
			// use buffering
			// this implementation reads one line at a time
			File myInputFile = new File("c:\\temp\\bababa");
			bufferedReader = new BufferedReader(new FileReader(myInputFile));
			String line = null; // not declared within while loop
			while ((line = bufferedReader.readLine()) != null) {
				contentsSb.append(line);
				contentsSb.append(System.getProperty("line.separator"));
			}
			bufferedReader.close();
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
			// ex.printStackTrace();
			// logger.warning(ex.getMessage());
		}
	}
}
