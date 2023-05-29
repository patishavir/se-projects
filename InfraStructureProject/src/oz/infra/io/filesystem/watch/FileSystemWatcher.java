package oz.infra.io.filesystem.watch;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;

import name.pachler.nio.file.ClosedWatchServiceException;
import name.pachler.nio.file.FileSystems;
import name.pachler.nio.file.Path;
import name.pachler.nio.file.Paths;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.WatchKey;
import name.pachler.nio.file.WatchService;
import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.print.PrintUtils;
import oz.infra.process.FileProcessor;

public class FileSystemWatcher extends Observable {
	private static Logger logger = JulUtils.getLogger();

	public void watch(final String folder2Watch, final FileProcessor fileProcessor,
			final WatchEvent.Kind<?>[] eventKinds, final WatchEvent.Modifier<Path>... watchEventsModifiers) {
		// TODO Auto-generated method stub
		FileSystemWatcherObserver fileSystemWatcherObserver = new FileSystemWatcherObserver(fileProcessor);
		addObserver(fileSystemWatcherObserver);
		WatchService watchService = FileSystems.getDefault().newWatchService();
		Path watchedPath = Paths.get(folder2Watch);
		try {
			watchedPath.register(watchService, eventKinds, watchEventsModifiers);
		} catch (UnsupportedOperationException uox) {
			System.err.println("file watching not supported!");
			// handle this error here
		} catch (IOException iox) {
			System.err.println("I/O errors");
			// handle this error here
		}
		while (true) {
			// take() will block until a file has been created/deleted
			WatchKey signalledKey;
			try {
				writeWatchMessage(folder2Watch, eventKinds);
				signalledKey = watchService.take();
			} catch (InterruptedException ix) {
				// we'll ignore being interrupted
				continue;
			} catch (ClosedWatchServiceException cwse) {
				// other thread closed watch service
				System.out.println("watch service closed, terminating.");
				break;
			}
			logger.info("process event");
			// get list of events from key
			List<WatchEvent<?>> eventsList = signalledKey.pollEvents();
			// VERY IMPORTANT! call reset() AFTER pollEvents() to allow the
			// key to be reported again by the watch service
			signalledKey.reset();
			setChanged();
			notifyObservers(eventsList);
			//
		}
	}

	private void writeWatchMessage(final String folder2Watch, final WatchEvent.Kind<?>[] eventKinds) {
		StringBuilder sb = new StringBuilder();
		sb.append("wait for event ");
		for (WatchEvent.Kind<?> eventKind : eventKinds) {
			sb.append(eventKind.name());
			sb.append(OzConstants.BLANK);
		}
		sb.append("on ");
		sb.append(folder2Watch);
		logger.info(PrintUtils.getSeparatorLine(sb.toString()));
	}

}
