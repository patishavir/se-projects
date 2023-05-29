package oz.utils.stress;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.datetime.StopWatch;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.NioUtils;
import oz.infra.system.SystemUtils;

public class StressUtils {
	private static int dataGenerationLoopControl = Integer.MIN_VALUE;
	private static int writeLoopControl = Integer.MIN_VALUE;
	private static final Logger logger = JulUtils.getLogger();

	private static void dasdStressUtils(final String rootFolderPath) {
		StopWatch stopWatch = new StopWatch();
		File rootFolder = new File(rootFolderPath);
		rootFolder.mkdirs();
		String dataString = getDataString();
		for (int i = 0; i < writeLoopControl; i++) {
			long nanoTime = System.nanoTime();
			String nanoTimeString = String.valueOf(nanoTime);
			String filePath = PathUtils.getFullPath(rootFolderPath, nanoTimeString);
			NioUtils.writeString2File(filePath, dataString, Level.FINEST);
			NioUtils.delete(filePath, Level.FINEST);
		}
		String summaryMessage = "*************** " + String.valueOf(writeLoopControl) + " files (length: "
				+ String.valueOf(dataString.length()) + ") have been written to " + rootFolderPath + " in";
		logger.info(stopWatch.appendElapsedTimeToMessage(summaryMessage));
	}

	private static String getDataString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < dataGenerationLoopControl; i++) {
			long nanoTime = System.nanoTime();
			String nanoTimeString = String.valueOf(nanoTime);
			// logger.finest(nanoTimeString + " length: " +
			// String.valueOf(nanoTimeString.length()));
			sb.append(nanoTimeString);
		}
		return sb.toString();
	}

	public static void main(final String[] args) {
		logger.info(SystemUtils.getRunInfo());
		String dataGenerationLoopControlString = args[0];
		String writeLoopControlString = args[1];
		dataGenerationLoopControl = Integer.parseInt(dataGenerationLoopControlString);
		writeLoopControl = Integer.parseInt(writeLoopControlString);
		for (int i = 2; i < args.length; i++) {
			dasdStressUtils(args[i]);
		}
	}
}