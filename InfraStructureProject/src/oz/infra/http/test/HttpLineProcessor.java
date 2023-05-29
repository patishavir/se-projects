package oz.infra.http.test;

import java.util.logging.Logger;

import oz.infra.io.LineProcessor;
import oz.infra.logging.jul.JulUtils;

public class HttpLineProcessor implements LineProcessor {
	private static Logger logger = JulUtils.getLogger();
	private int recordCount = 0;

	public void processEOF(final Object object) {
		// TODO Auto-generated method stub
		logger.info(String.valueOf(recordCount));
	}

	public void processLine(final String line) {
		// TODO Auto-generated method stub
		logger.finest(line);
		recordCount++;
	}
}
