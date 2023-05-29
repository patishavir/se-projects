package oz.utils.snifit.dbload.watchers;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.io.filesystem.watch.FileSystemWatcher;
import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.process.FileProcessor;
import oz.infra.ssh.scp.ScpUtils;
import oz.infra.string.StringUtils;
import oz.infra.thread.ThreadUtils;
import oz.utils.snifit.dbload.DbLoadParameters;

public class DbLoadProcessor implements FileProcessor {
	private static String folder2Watch = DbLoadParameters.getFolder2Watch();
	private static Logger logger = JulUtils.getLogger();

	public DbLoadProcessor() {
		logger.info(StringUtils.concat("\n", OzConstants.ASTERISKS_20, " starting up ... "));
		WatchEvent.Kind<?>[] eventKinds = { StandardWatchEventKind.ENTRY_CREATE };
		new FileSystemWatcher().watch(folder2Watch, this, eventKinds);
	}

	public Outcome processFile(final String partialPath) {
		if (partialPath != null && partialPath.length() > 0) {
			logger.info("fileName: " + partialPath);
			ThreadUtils.sleep(OzConstants.INT_1000, Level.INFO);
			if (partialPath.toLowerCase().startsWith("quit")) {
				logger.info("exiting ...");
				String fullPath = PathUtils.getFullPath(folder2Watch, partialPath);
				FileUtils.deleteAndLogResult(fullPath);
				System.exit(OzConstants.EXIT_STATUS_OK);
			}
			String fileFullPath = PathUtils.getFullPath(folder2Watch, partialPath);
			logger.info("Processing " + fileFullPath + " ...");
			Properties remoteProperties = DbLoadParameters.getRemoteProperties();
			ScpUtils.scp(fileFullPath, remoteProperties);
			FileUtils.backupFile(fileFullPath, DbLoadParameters.getBackupFolderPath(), true);
			logger.info("Processing of " + fileFullPath + " has completed ...");
		}
		return Outcome.SUCCESS;
	}
}
