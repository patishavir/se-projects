package oz.utils.p8.log;

import java.util.logging.Logger;

import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;

public class P8LogAnalyzerMain {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		analyze(args);
	}

	private static void analyze(final String[] args) {
		String logString = FileUtils.readTextFile(P8LogAnalyzerParameters.getFilePath());
		logger.info(String.valueOf(logString.length()));
		String[] logEntries = logString.split(P8LogAnalyzerParameters.getEventDelimiterRegexp());
		logger.info(String.valueOf(logEntries.length));

	}
}
