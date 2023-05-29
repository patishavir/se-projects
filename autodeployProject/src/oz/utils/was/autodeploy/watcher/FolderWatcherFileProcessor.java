package oz.utils.was.autodeploy.watcher;

import oz.infra.operaion.Outcome;
import oz.infra.process.FileProcessor;

public class FolderWatcherFileProcessor implements FileProcessor {
	private EarFileProcessr earFileProcessr = null;

	public FolderWatcherFileProcessor(final EarFileProcessr earFileProcessr) {
		this.earFileProcessr = earFileProcessr;
	}

	public Outcome processFile(final String filePathSuffix) {
		earFileProcessr.processFile(filePathSuffix);
		return null;
	}
}
