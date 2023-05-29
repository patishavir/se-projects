package oz.infra.nio.watcher.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class WatcherTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// define a folder root
		Path dir2Watch = Paths.get(args[0]);
		try {
			WatchService watcher = dir2Watch.getFileSystem().newWatchService();
			dir2Watch.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);
			logger.info("dir2Watch.register completed for ".concat(dir2Watch.toString()));
			WatchKey watckKey = watcher.take();

			List<WatchEvent<?>> events = watckKey.pollEvents();
			for (WatchEvent<?> event : events) {
				logger.info("event: " + event.toString());
				if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
					logger.info("Created: " + event.context().toString());
				}
				if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
					logger.info("Delete: " + event.context().toString());
				}
				if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
					logger.info("Modify: " + event.context().toString());
				}
				logger.info(event.toString() + " has been processed !");
			}

		} catch (Exception ex) {
			logger.warning("Error: " + ex.toString());
		}

	}
}

// The FileSystem object and the WatchService can also be created like this:
//
// FileSystem fileSystem = FileSystems.getDefault();
// WatchService watcher = fileSystem.newWatchService();
//
// And the Path (watchable), what we watch, and register it with the
// WatchService object like this:
//
// Path myDir = fileSystem.getPath("D:/data");
// myDir.register(watcher, StandardWatchEventKind.ENTRY_CREATE,
// StandardWatchEventKind.ENTRY_DELETE, StandardWatchEventKind.ENTRY_MODIFY);
