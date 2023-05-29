package oz.utils.io;

import java.io.File;

import oz.infra.io.FileUtils;
import oz.infra.system.SystemUtils;

public class CopyHTTPFileMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 2) {
			String message = "2 patrameters should be Supplied.\nProcessing has been aborted!";
			SystemUtils.printMessageAndExit(message, -1);
		} else {
			String url = args[0];
			String outFilePath = args[1];
			FileUtils.copyHTTPFile(url, new File(outFilePath));
		}
	}
}
