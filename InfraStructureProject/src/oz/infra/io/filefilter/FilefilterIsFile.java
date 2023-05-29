package oz.infra.io.filefilter;

import java.io.File;
import java.io.FileFilter;

public class FilefilterIsFile implements FileFilter {
	public final boolean accept(final File file) {
		return file.isFile();
	}
}
