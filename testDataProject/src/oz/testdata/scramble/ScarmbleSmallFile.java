package oz.testdata.scramble;

import java.io.File;
import java.text.DecimalFormat;
import java.util.logging.Logger;

import oz.infra.io.FileUtils;

public class ScarmbleSmallFile {
	private static Logger logger = Logger.getLogger(ScarmbleSmallFile.class.getName());

	static final void scrambleFile(final File file2ScrambleFile) {
		DecimalFormat formatter = new DecimalFormat("000000000000000000000000000000000000000000000");
		String file2ScramblePath = file2ScrambleFile.getAbsolutePath();

		String[] file2ScrambleArray = FileUtils.readTextFile2Array(file2ScramblePath);
		logger.info("In Array size: " + String.valueOf(file2ScrambleArray.length));
		StringBuffer scrambledStringBuffer = new StringBuffer();
		for (int i = 0; i < file2ScrambleArray.length; i++) {
			String ipNumberString = formatter.format(i);
			StringBuffer lineStringBuffer = new StringBuffer(file2ScrambleArray[i]);
			if (file2ScrambleArray[i].length() > 72) {
				lineStringBuffer.replace(72, 74, "IP ");
				int endReplace = 75 + ipNumberString.length() + 1;
				lineStringBuffer.replace(75, endReplace, ipNumberString);
				lineStringBuffer.append("\r\n");
			} else {
				System.out.println(file2ScrambleArray[i].length());
			}
			scrambledStringBuffer.append(lineStringBuffer);
		}
		String scrambledString = scrambledStringBuffer.toString();
		logger.info(String.valueOf(scrambledString.length()));
		File outFile = new File(file2ScramblePath + ".out");
		FileUtils.writeFile(outFile, scrambledString);

	}

}
