package oz.infra.nio.watcher.test;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

import oz.infra.io.filesystem.watch.FileSystemWatcherProcessor;
import oz.infra.nio.watcher.WatchDirService;

public final class Watcher7TestMain {

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		doTest();
	}

	private static void doTest() {
		String dir = "c:/temp";
		boolean recursive = true;
		WatchEvent.Kind<?>[] events = { StandardWatchEventKinds.ENTRY_CREATE };
		FileSystemWatcherProcessorImpl fileSystemWatcherProcessorImpl = new FileSystemWatcherProcessorImpl();
		WatchDirService watchDirService = new WatchDirService(dir, recursive, events, fileSystemWatcherProcessorImpl);
	}
}
