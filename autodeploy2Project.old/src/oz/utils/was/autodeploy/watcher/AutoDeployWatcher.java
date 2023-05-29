package oz.utils.was.autodeploy.watcher;

import java.util.logging.Logger;

import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.ext.ExtendedWatchEventModifier;
import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.io.filesystem.watch.FileSystemWatcher;
import oz.infra.io.filesystem.watch.FileSystemWatcherProcessor;
import oz.infra.jmx.exit.ExitHandler;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.print.PrintUtils;
import oz.infra.process.FileProcessor;

public class AutoDeployWatcher implements FileSystemWatcherProcessor {
	private String folder2WatchFilePath = null;
	private FileProcessor fileProcessor = null;
	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());

	AutoDeployWatcher(final String folder2WatchFilePath, final FileProcessor fileProcessor) {
		this.folder2WatchFilePath = PathUtils.getAbsolutePath(folder2WatchFilePath);
		this.fileProcessor = fileProcessor;
		this.folder2WatchFilePath = PathUtils.getAbsolutePath(folder2WatchFilePath);
		FolderUtils.mkdirIfNoExists(this.folder2WatchFilePath);
		new ExitHandler();
		logger.info(PrintUtils.getSeparatorLine(" starting up ... "));
		startWatcher(this);
	}

	public void processFile(final String filePathSuffix) {
		String earFileFullPath = PathUtils.getFullPath(folder2WatchFilePath, filePathSuffix);
		fileProcessor.processFile(earFileFullPath);
	}

	private void startWatcher(final FileSystemWatcherProcessor fileSystemWatcherProcessor) {
		WatchEvent.Kind<?>[] eventKinds = { StandardWatchEventKind.ENTRY_CREATE };
		new FileSystemWatcher().watch(folder2WatchFilePath, fileSystemWatcherProcessor, eventKinds,
				ExtendedWatchEventModifier.FILE_TREE);
	}
}