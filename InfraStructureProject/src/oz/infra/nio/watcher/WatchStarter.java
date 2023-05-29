package oz.infra.nio.watcher;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.logging.Logger;

import oz.infra.io.FolderUtils;
import oz.infra.io.PathUtils;
import oz.infra.jmx.exit.ExitHandler;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.operaion.Outcome;
import oz.infra.print.PrintUtils;
import oz.infra.process.FileProcessor;
import oz.infra.thread.ThreadUtils;
import oz.infra.varargs.VarArgsUtils;

public class WatchStarter implements FileProcessor {
	private FileProcessor fileProcessor = null;
	private String folder2WatchFilePath = null;
	private Long wait4CopyCompletion = null;
	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());

	public WatchStarter(final String folder2WatchFilePath, final FileProcessor fileProcessor,
			final Long... wait4CopyCompletions) {
		this.folder2WatchFilePath = PathUtils.getAbsolutePath(folder2WatchFilePath);
		this.fileProcessor = fileProcessor;
		wait4CopyCompletion = VarArgsUtils.getMyArg(0L, wait4CopyCompletions);
		FolderUtils.mkdirIfNoExists(this.folder2WatchFilePath);
		new ExitHandler();
		logger.info(PrintUtils.getSeparatorLine(" starting up ... "));
		boolean recursive = true;
		WatchEvent.Kind<?>[] events = { StandardWatchEventKinds.ENTRY_CREATE };
		WatchDirService watchDirService = new WatchDirService(folder2WatchFilePath, recursive, events, this);
	}

	public Outcome processFile(final String filePathSuffix) {
		String earFileFullPath = PathUtils.getFullPath(folder2WatchFilePath, filePathSuffix);
		if (wait4CopyCompletion > 0) {
			ThreadUtils.sleep(wait4CopyCompletion);
		}
		fileProcessor.processFile(earFileFullPath);
		return Outcome.SUCCESS;
	}
}