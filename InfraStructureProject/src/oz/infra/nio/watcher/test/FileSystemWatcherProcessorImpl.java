package oz.infra.nio.watcher.test;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.process.FileProcessor;

public class FileSystemWatcherProcessorImpl implements FileProcessor {
	private Logger logger = JulUtils.getLogger();

	public final Outcome processFile(final String filePath) {
		logger.info(String.format("file path: %s .", filePath));
		return null;
	}
}
