package oz.clearcase.stream.bl2stream;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

/**
 * 
 * @author Oded Zimerman
 * 
 */
public class BL2StreamMain {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BL2StreamParameters.getParameters();
		loggingSetup();
		BL2StreamProcessor bL2StreamProcessor = new BL2StreamProcessor();
		bL2StreamProcessor.validateParameters();
		bL2StreamProcessor.process();
		logger.info("BL2Stream processing completed successfully!");
	}

	private static void loggingSetup() {
		String logFileFolderPath = BL2StreamParameters.getLogFileFolderPath();
		File logFileFolderFile = new File(logFileFolderPath);
		if (!logFileFolderFile.exists()) {
			logFileFolderFile.mkdirs();
		}
		JulUtils.addFileHandler( 
				logFileFolderPath + BL2StreamParameters.getStream() + ".log");
	}
}
