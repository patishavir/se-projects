package oz.infra.nio.watcher.test;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

import oz.infra.nio.watcher.WatchDirService;
import oz.infra.process.FileProcessor;

public final class WatchDirServiceTest {

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		doTest();
	}

	private static void doTest() {
		String dir = "c:/temp";
		boolean recursive = false;
		WatchEvent.Kind<?>[] events = { StandardWatchEventKinds.ENTRY_CREATE };
		FileProcessor fileProcessor = new FileSystemWatcherProcessorImpl();
		// WatchDirService watchDirService = new WatchDirService(dir, recursive,
		// events, fileSystemWatcherProcessor);
		WatchDirService watchDirService = new WatchDirService(dir, recursive, events, fileProcessor);

	}

}
