package oz.testdata.scramble;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Logger;

import oz.infra.checkdigit.CheckDigit;
import oz.infra.io.FileUtils;
import oz.infra.math.MathUtils;
import oz.infra.string.StringUtils;

public class ScarmbleFile {
	private static DecimalFormat decimalFormatter45 = new DecimalFormat(StringUtils.repeatChar('0',
			45));

	private static MathUtils mathUtils = new MathUtils();

	private static Logger logger = Logger.getLogger("ScarmbleLargeFile.class.getName()");

	private static int fromRecord = 0;

	private static int toRecord = Integer.MAX_VALUE;

	static final void scrambleFile(final File file2ScrambleFile) {
		String file2ScramblePath = file2ScrambleFile.getAbsolutePath();
		String scrambledFilePath = file2ScramblePath + ".scrambledBig";
		File scrambledFile = new File(scrambledFilePath);
		logger.info("Scrambling file: " + file2ScramblePath);
		logger.info("Scrambled file: " + scrambledFilePath);

		int recordProcessed = 0;
		//
		// logger.info(ScarmbleLargeFile.class.getName() +
		// "???????????????????????");
		// logger.info(ScarmbleLargeFile.class.getCanonicalName() +
		// "++++++++++???????????????????????");
		// logger.info(ScarmbleLargeFile.class.getSimpleName() +
		// "aaaaaaaaaaaaaaaaa???????????????????????");
		// StringUtils.getSameCharString('1', 17);
		// logger.info(new MathUtils().getRandomNumberAsString(7));
		// FileUtils.copyTextFile(file2ScrambleFile, scrambledFile);
		//
		try {
			BufferedReader in = new BufferedReader(new FileReader(file2ScramblePath));
			BufferedWriter out = new BufferedWriter(new FileWriter(scrambledFile));
			String line;
			for (int recordCounter = 0; ((line = in.readLine()) != null && recordCounter <= toRecord); recordCounter++) {
				logger.info("record length: " + line.length());
				if (recordCounter >= fromRecord) {
					recordProcessed++;
					out.write(processLine(line, recordCounter));
					out.newLine();
				}

			}
			in.close();
			out.close();

		} catch (IOException ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
		logger.info("File scrambled. " + String.valueOf(recordProcessed) + " records processed.");
		// build check files
		FileUtils.copyTextFile(file2ScrambleFile, new File(file2ScramblePath + ".check"), 1000001,
				1000050);
		FileUtils.copyTextFile(scrambledFile, new File(scrambledFilePath + ".check"), 1000001,
				1000050);

	}

	private static String processLine(final String line, final int recordCounter) {
		// process name column 72 to the end

		String ipNumberString = decimalFormatter45.format(recordCounter);
		StringBuffer lineStringBuffer = new StringBuffer(line);
		if (line.length() > 0) {
			if (line.length() > 72) {
				lineStringBuffer.replace(72, 74, "IP ");
				int endReplace = 75 + ipNumberString.length() + 1;
				lineStringBuffer.replace(75, endReplace, ipNumberString);

			} else {
				logger.warning("Line length: " + String.valueOf(line.length()));
			}
			// end of name processing
			// process Id
			String externalId = mathUtils.getRandomNumberAsString(8);
			String checkDigitString = CheckDigit.generateCheckDigit(externalId);
			lineStringBuffer.replace(51, 60, externalId + checkDigitString);
		}
		return (lineStringBuffer.toString());
	}

}
