package oz.infra.io.filesystem.watch.test;

import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;

import name.pachler.nio.file.Path;
import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.ext.ExtendedWatchEventModifier;
import oz.infra.io.filesystem.watch.FileSystemWatcher;
import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.process.FileProcessor;

public class TestFileSystemWatcher implements FileProcessor {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		testFileSystemWatcher("c:\\temp");

	}

	private static void testFileSystemWatcher(final String folder2Watch) {
		// WatchEvent.Kind<?>[] events = { StandardWatchEventKind.ENTRY_CREATE,
		// StandardWatchEventKind.ENTRY_DELETE,
		// StandardWatchEventKind.ENTRY_MODIFY };
		WatchEvent.Kind<?>[] events = { StandardWatchEventKind.ENTRY_CREATE };
		new FileSystemWatcher().watch(folder2Watch, new TestFileSystemWatcher(), events,
				ExtendedWatchEventModifier.FILE_TREE);
	}

	public Outcome processFile(final String filePath) {
		logger.info("File path: " + filePath);
		return Outcome.SUCCESS;
	}

	public final void update(final Observable observable, final Object object) {
		List<WatchEvent<?>> eventsList = (List<WatchEvent<?>>) object;
		// TODO Auto-generated method stub
		logger.info("starting ...");
		for (WatchEvent event : eventsList) {
			String message = "";
			if (event.kind() == StandardWatchEventKind.ENTRY_CREATE) {
				Path context = (Path) event.context();
				message = context.toString() + " created";
			} else if (event.kind() == StandardWatchEventKind.ENTRY_DELETE) {
				Path context = (Path) event.context();
				message = context.toString() + " deleted";
			} else if (event.kind() == StandardWatchEventKind.ENTRY_MODIFY) {
				Path context = (Path) event.context();
				message = context.toString() + " modified";
			} else if (event.kind() == StandardWatchEventKind.OVERFLOW) {
				message = "OVERFLOW: more changes happened than we could retreive";
			}
			System.out.println(message);
		}
		try {
			int sleepMilliSeconds = 1000;
			logger.info("Sleep for " + String.valueOf(sleepMilliSeconds));
			Thread.sleep(sleepMilliSeconds);

		} catch (Exception ex) {
			logger.warning(ex.getMessage());
		}
	}
}
