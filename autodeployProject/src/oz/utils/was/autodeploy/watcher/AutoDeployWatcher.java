package oz.utils.was.autodeploy.watcher;

import java.util.logging.Logger;

import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.ext.ExtendedWatchEventModifier;
import oz.infra.io.filesystem.watch.FileSystemWatcher;
import oz.infra.logging.jul.JulUtils;
import oz.infra.logging.jul.formatters.OneLineFormatterWithDatePrefix;
import oz.infra.print.PrintUtils;
import oz.utils.was.autodeploy.snifit.AutoDeployParameters;

public class AutoDeployWatcher {

	private static Logger logger = JulUtils.getLogger(new OneLineFormatterWithDatePrefix());

	private static void startWatcher(final FolderWatcherFileProcessor automaticApplicationUpdateFileProcessor) {
		WatchEvent.Kind<?>[] eventKinds = { StandardWatchEventKind.ENTRY_CREATE };
		new FileSystemWatcher().watch(AutoDeployParameters.getInputEarFolderPath(),
				automaticApplicationUpdateFileProcessor, eventKinds, ExtendedWatchEventModifier.FILE_TREE);
	}

	AutoDeployWatcher(final String applicationUpdatePropertiesFilePath) {
		logger.info(PrintUtils.getSeparatorLine(" starting up ... "));
		AutoDeployParameters.processParameters(applicationUpdatePropertiesFilePath);
		FolderWatcherFileProcessor automaticApplicationUpdateFileProcessor = new FolderWatcherFileProcessor(
				new EarFileProcessr());
		startWatcher(automaticApplicationUpdateFileProcessor);
	}

}