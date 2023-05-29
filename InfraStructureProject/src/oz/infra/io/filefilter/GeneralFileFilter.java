package oz.infra.io.filefilter;

import java.io.File;
import java.io.FileFilter;

public class GeneralFileFilter implements FileFilter {
	private GeneralFileFilerEnum fileFilerEnum;

	public boolean accept(final File file) {
		boolean returnCode = false;
		switch (fileFilerEnum) {
		case IS_DIRECTORY:
			returnCode = file.isDirectory();
			break;
		case IS_FILE:
			returnCode = file.isFile();
			break;
		}
		return returnCode;
	}

	public GeneralFileFilter(final GeneralFileFilerEnum fileFilerEnum) {
		this.fileFilerEnum = fileFilerEnum;
	}
}
