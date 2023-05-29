package oz.infra.nio.watcher;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.NioUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.print.PrintUtils;
import oz.infra.process.FileProcessor;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.system.env.EnvironmentUtils;

/**
 * Example to watch a directory (or tree) for changes to files.
 */

public class WatchDirService {
	private String dir = null;
	private WatchService watchService = null;
	private WatchEvent.Kind<?>[] eventKinds = null;
	private Map<WatchKey, Path> keys = null;
	private boolean recursive = false;
	private boolean trace = true;
	private int dirCount = 0;

	private static final String DISABLE_DYNAMIC_FOLDER_REGISTRATION = "disableDynamicFolderRegistration";
	private static boolean disableDynamicFolderRegistration = SystemUtils.getBooleanVarFromSysPropsOrEnv(DISABLE_DYNAMIC_FOLDER_REGISTRATION);
	private String watcherQuitFileName = "quit.watcher";
	private static final String WATCHER_QUIT_ENV_VAR_NAME = "quitWatcherFileName";
	private static final long SLEEP_BEFORE_DYNAMIC_REGISTER = 200L;
	private static final Logger logger = JulUtils.getLogger();

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(final WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	/**
	 * Creates a WatchService and registers the given directory
	 */
	public WatchDirService(final String dir, final boolean recursive, final WatchEvent.Kind<?>[] eventsKinds, final FileProcessor fileProcessor) {
		construct(dir, recursive, eventsKinds, fileProcessor);
	}

	public WatchDirService(final WatchDirServiceParameters watchDirServiceParameters, final FileProcessor fileProcessor) {
		construct(watchDirServiceParameters.getFolder2WatchPath(), watchDirServiceParameters.isRecursive(), watchDirServiceParameters.getEvents(),
				fileProcessor);
	}

	public WatchDirService(final Properties properties, final FileProcessor fileProcessor) {
		WatchDirServiceParameters watchDirServiceParameters = new WatchDirServiceParameters(properties);
		ParametersUtils.processPatameters(properties, watchDirServiceParameters);
		new WatchDirService(watchDirServiceParameters, fileProcessor);
	}

	public WatchDirService(final String propertiesFilePath, final FileProcessor fileProcessor) {
		WatchDirServiceParameters watchDirServiceParameters = new WatchDirServiceParameters(propertiesFilePath);
		Properties properties = PropertiesUtils.loadPropertiesFile(propertiesFilePath);
		ParametersUtils.processPatameters(properties, watchDirServiceParameters);
		new WatchDirService(watchDirServiceParameters, fileProcessor);
	}

	private void construct(final String dir, final boolean recursive, final WatchEvent.Kind<?>[] eventsKinds, final FileProcessor fileProcessor) {
		try {
			this.dir = dir;
			Path dirPath = Paths.get(dir);
			this.watchService = FileSystems.getDefault().newWatchService();
			this.keys = new HashMap<WatchKey, Path>();
			this.eventKinds = eventsKinds;
			this.recursive = recursive;
			if (recursive) {
				logger.info(String.format("Scanning %s ...", dirPath));
				registerAll(dirPath);
				logger.info(StringUtils.concat(dirPath.toString(), " registration has completed. ", String.valueOf(dirCount),
						" folders have been registered."));
			} else {
				register(dirPath);
			}
			String watcherQuitFileNameEnvVar = EnvironmentUtils.getEnvironmentVariable(WATCHER_QUIT_ENV_VAR_NAME);
			if (watcherQuitFileNameEnvVar != null) {
				watcherQuitFileName = watcherQuitFileNameEnvVar;
			}
			// enable trace after initial registration
			this.trace = true;
		} catch (Exception ex) {
			logger.warning(ex.toString());
		}
		processEvents(fileProcessor);
	}

	/**
	 * Process all events for keys queued to the watcher
	 */
	public final void processEvents(final FileProcessor fileProcessor) {
		WatchKey key = null;
		while (true) {
			writeWatchMessage();
			// wait for key to be signalled
			try {
				key = watchService.take();
				Path dirPath = keys.get(key);
				for (WatchEvent<?> event1 : key.pollEvents()) {
					WatchEvent.Kind kind = event1.kind();
					if (kind == OVERFLOW) {
						logger.warning("Overflow event has occurred");
					} else {
						WatchEvent<Path> event = (WatchEvent<Path>) (event1);
						Path name = event.context();
						Path child = dirPath.resolve(name);
						logger.info(String.format("%s: %s\n", event.kind().name(), child));
						// if directory is created, and watching recursively,
						// then register it and its sub-directories
						if (!disableDynamicFolderRegistration && recursive && (kind == ENTRY_CREATE) && Files.isDirectory(child, NOFOLLOW_LINKS)) {
							Thread.sleep(SLEEP_BEFORE_DYNAMIC_REGISTER);
							registerAll(child);
						}
						processExitRequest(child, kind);
						fileProcessor.processFile(child.toString());
						// reset key and remove from set if directory no longer accessible
						boolean valid = key.reset();
						if (!valid) {
							keys.remove(key);
							logger.info(key.toString().concat(" has been removed."));
							// all directories are inaccessible
							if (keys.isEmpty()) {
								logger.warning("keys is Empty.class break out");
								break;
							}
						}
					}
				}
			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
			}
		}
	}

	private void processExitRequest(final Path path, final WatchEvent.Kind kind) {
		if ((kind == ENTRY_CREATE) && Files.isRegularFile(path)) {
			String name = path.getFileName().toString();
			if (name.equalsIgnoreCase(watcherQuitFileName)) {
				logger.info("processing file: " + name + "\nperforming quit. bye bye ...");
				try {
					NioUtils.delete(path);
				} catch (Exception ex) {
					ExceptionUtils.printMessageAndStackTrace(ex);
				}
				System.exit(0);
			}
		}
	}

	/**
	 * Register the given directory with the WatchService
	 */
	private void register(final Path dir) throws IOException {
		WatchKey key = dir.register(watchService, eventKinds);
		if (trace) {
			Path prev = keys.get(key);
			if (prev == null) {
				logger.info(String.format("register: %s", dir));
			} else {
				if (!dir.equals(prev)) {
					logger.info(String.format("update: %s -> %s", prev, dir));
				}
			}
		}
		keys.put(key, dir);
		dirCount += 1;
	}

	/**
	 * Register the given directory, and all its sub-directories, with the WatchService.
	 */
	public final void registerAll(final Path start) throws IOException {
		// register directory and sub-directories
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
				register(dir);
				logger.finest("regsitering ".concat(dir.toString()));
				return FileVisitResult.CONTINUE;
			}
		});
	}

	private void writeWatchMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("wait for event ");
		for (WatchEvent.Kind<?> eventKind : eventKinds) {
			sb.append(eventKind.name());
			sb.append(OzConstants.BLANK);
		}
		sb.append("on ");
		sb.append(dir);
		sb.append(" host: ");
		sb.append(SystemUtils.getHostname());
		sb.append(" pid: ");
		sb.append(SystemUtils.getProcessId());
		sb.append(" recursive: ");
		sb.append(String.valueOf(recursive));
		logger.info(PrintUtils.getSeparatorLine(sb.toString()));
	}
}