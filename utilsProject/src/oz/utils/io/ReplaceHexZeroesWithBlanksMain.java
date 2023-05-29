package oz.utils.io;

import oz.infra.io.FileUtils;
import oz.infra.system.SystemUtils;

public class ReplaceHexZeroesWithBlanksMain {
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String inFilePath = args[0];
		String outFilePath = args[1];
		if (args.length != 2) {
			SystemUtils.printMessageAndExit(
					"2 parameters should be specified.\nPeocessing has been aborted!", -1);
		}
		byte blankByte = (byte) (32);
		byte zeroesByte = (byte) (0);
		int returnCode = FileUtils.replaceBytesInFile(inFilePath, outFilePath, zeroesByte,
				blankByte);
		System.exit(returnCode);
	}
}
