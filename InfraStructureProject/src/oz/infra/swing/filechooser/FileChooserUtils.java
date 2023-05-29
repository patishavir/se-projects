package oz.infra.swing.filechooser;

import java.util.logging.Logger;

import javax.swing.JFileChooser;

import oz.infra.logging.jul.JulUtils;

public class FileChooserUtils {
	private static Logger logger = JulUtils.getLogger();

	public static enum FileSelectionModeEnum {
		FILES_ONLY(JFileChooser.FILES_ONLY), DIRECTORIES_ONLY(JFileChooser.DIRECTORIES_ONLY), FILES_AND_DIRECTORIES(
				JFileChooser.FILES_AND_DIRECTORIES);

		private final int fileSelectionMode; // in kilograms

		FileSelectionModeEnum(final int fileSelectionMode) {
			this.fileSelectionMode = fileSelectionMode;
			logger.finer(this.toString() + ": " + String.valueOf(fileSelectionMode));
		}

		public int getFileSelectionMode() {
			return this.fileSelectionMode;
		}
	}

	public static int getFileSelectionMode(final String fileSelectionModeString) {
		int fileSelectionMode = FileSelectionModeEnum.valueOf(fileSelectionModeString).getFileSelectionMode();
		logger.finer(fileSelectionModeString + ": " + String.valueOf(fileSelectionMode));
		return fileSelectionMode;
	}
}
