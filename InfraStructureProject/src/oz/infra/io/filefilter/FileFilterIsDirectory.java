package oz.infra.io.filefilter;

import java.io.File;
import java.io.FileFilter;

public class FileFilterIsDirectory implements FileFilter {
	public boolean accept(final File file) {
		return file.isDirectory();
	}
}
