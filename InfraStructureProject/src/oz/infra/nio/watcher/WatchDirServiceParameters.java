package oz.infra.nio.watcher;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.system.SystemUtils;

public class WatchDirServiceParameters {

	public static final String FOLDER2WATCH_PATH = "folder2WatchPath";
	public static final String RECURSIVE = "recursive";
	public static final String WATCHEVENTKINDS = "watchEventKinds";

	private String folder2WatchPath = null;
	private boolean recursive = true;
	private String watchEventKinds = null;
	private WatchEvent.Kind<?>[] events = { StandardWatchEventKinds.ENTRY_CREATE };
	private final Logger logger = JulUtils.getLogger();

	public WatchDirServiceParameters() {
	}

	public WatchDirServiceParameters(final Properties properties) {
		ParametersUtils.processPatameters(properties, this);
	}

	public WatchDirServiceParameters(final String folder2WatchPath) {
		setFolder2WatchPath(folder2WatchPath);
	}

	public WatchDirServiceParameters(final String folder2WatchPath, final boolean recursive) {
		setFolder2WatchPath(folder2WatchPath);
		setRecursive(recursive);
	}

	public WatchDirServiceParameters(final String folder2WatchPath, final boolean recursive, final String watchEventKinds) {
		setFolder2WatchPath(folder2WatchPath);
		setRecursive(recursive);
		setWatchEventKinds(watchEventKinds);
	}

	public final String getAsString() {
		StringBuilder sb = new StringBuilder();
		sb.append(FOLDER2WATCH_PATH + OzConstants.BLANK);
		sb.append(getFolder2WatchPath());
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append(RECURSIVE + OzConstants.BLANK);
		sb.append(String.valueOf(isRecursive()));
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append(WATCHEVENTKINDS + OzConstants.BLANK);
		sb.append(watchEventKinds);
		sb.append(SystemUtils.LINE_SEPARATOR);
		sb.append("folder2WatchPath: ");
		sb.append(getFolder2WatchPath());
		sb.append(SystemUtils.LINE_SEPARATOR);
		String asString = sb.toString();
		logger.info(asString);
		return asString;
	}

	public final WatchEvent.Kind<?>[] getEvents() {
		return events;
	}

	public final String getFolder2WatchPath() {
		return folder2WatchPath;
	}

	public final boolean isRecursive() {
		return recursive;
	}

	public final void setFolder2WatchPath(final String folder2WatchPath) {
		this.folder2WatchPath = folder2WatchPath;
	}

	public final void setRecursive(final boolean recursive) {
		this.recursive = recursive;
	}

	public final void setWatchEventKinds(final String watchEventKinds) {
		this.watchEventKinds = watchEventKinds;
		String[] watchEventKindsStringArray = this.watchEventKinds.split(OzConstants.COMMA);
		events = new WatchEvent.Kind<?>[watchEventKindsStringArray.length];
		for (int i = 0; i < watchEventKindsStringArray.length; i++) {
			switch (watchEventKindsStringArray[i]) {
			case "ENTRY_CREATE":
				events[i] = StandardWatchEventKinds.ENTRY_CREATE;
				break;
			case "ENTRY_DELETE":
				events[i] = StandardWatchEventKinds.ENTRY_DELETE;
				break;
			case "ENTRY_MODIFY":
				events[i] = StandardWatchEventKinds.ENTRY_MODIFY;
				break;
			}
		}
	}

}