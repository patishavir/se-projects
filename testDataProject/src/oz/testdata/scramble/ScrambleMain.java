package oz.testdata.scramble;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class ScrambleMain {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String file2ScramblePath = args[0];
		File file2ScrambleFile = new File(file2ScramblePath);
		if (!file2ScrambleFile.exists()) {
			logger.severe("Input file " + file2ScramblePath
					+ " does not exist! Processing terminated.");
			System.exit(-1);
		}
		// ScarmbleSmallFile.scrambleFile(file2ScrambleFile);
		ScarmbleFile.scrambleFile(file2ScrambleFile);
	}
}
