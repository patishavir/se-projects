package oz.infra.io.filesystem.watch;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import name.pachler.nio.file.Path;
import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import oz.infra.logging.jul.JulUtils;
import oz.infra.process.FileProcessor;

public class FileSystemWatcherObserver implements Observer {
	private FileProcessor fileProcessor = null;
	private static Logger logger = JulUtils.getLogger();

	FileSystemWatcherObserver(final FileProcessor fileProcessor) {
		this.fileProcessor = fileProcessor;
	}

	public void update(final Observable observable, final Object object) {
		logger.info("starting ...");
		List<WatchEvent<?>> eventsList = (List<WatchEvent<?>>) object;
		for (WatchEvent event : eventsList) {
			if (event.kind() == StandardWatchEventKind.ENTRY_CREATE) {
				Path context = (Path) event.context();
				fileProcessor.processFile(context.toString());
			} else if (event.kind() == StandardWatchEventKind.OVERFLOW) {
				String overFlowMessage = "OVERFLOW: more changes happened than we could retreive";
				logger.warning(overFlowMessage);
			}
		}
	}

}
