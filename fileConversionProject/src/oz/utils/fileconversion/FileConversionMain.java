package oz.utils.fileconversion;

import java.io.File;

import oz.infra.io.FileUtils;

public class FileConversionMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 8) {
			File inFile = new File(args[0]);
			File outFile = new File(args[1]);
			File exceptionsFile = new File(args[2]);
			String inFileCharsetString = args[3];
			String outFileCharsetString = args[4];
			int lrecl = Integer.parseInt(args[5]);
			boolean trim = args[6].equalsIgnoreCase("trim");
			boolean convertNulls2Blanks = args[7].equalsIgnoreCase("convertNulls2Blanks");

			FileUtils.convertFile(inFile, outFile, inFileCharsetString, outFileCharsetString,
					lrecl, trim, convertNulls2Blanks, exceptionsFile);
		} else {
			System.out.println("8 parameters should be specified !\nProcessin aborted!");
		}
	}
}
