package oz.jdir;

import java.io.File;

import oz.clearcase.infra.ClearCaseAttributes;

/*
 * FileInfo class
 */
public class FileInfo implements Comparable {
	private String filePartialPath;
	private long length;
	private long fileLastModified;
	private boolean isDirectory;
	private boolean isFile;
	private ClearCaseAttributes clearCaseAttributes;

	FileInfo(final File file, final String baseDirName) {
		this.filePartialPath = file.getAbsolutePath().substring(baseDirName.length() + 1);
		this.length = file.length();
		this.fileLastModified = file.lastModified();
		this.isDirectory = file.isDirectory();
		this.isFile = file.isFile();
	}

	public FileInfo(final String filePartialPathP, final long fileLength,
			final long fileLastModifiedP, final boolean isFileP, final boolean isDirectoryP) {
		this.filePartialPath = filePartialPathP;
		this.length = fileLength;
		this.fileLastModified = fileLastModifiedP;
		this.isFile = isFileP;
		this.isDirectory = isDirectoryP;
	}

	//
	final boolean isFile() {
		return this.isFile;
	}

	public final boolean isDirectory() {
		return this.isDirectory;
	}

	public final String getFilePartialPath() {
		return this.filePartialPath;
	}

	public final long lastModified() {
		return this.fileLastModified;
	}

	public final void setLastModified(final long timeInMillis) {
		this.fileLastModified = timeInMillis;
	}

	public final long length() {
		return this.length;
	}

	public final int compareTo(final Object obj) {
		return this.filePartialPath.compareTo(((FileInfo) obj).filePartialPath);
	}

	public final ClearCaseAttributes getClearCaseAttributes() {
		return clearCaseAttributes;
	}

	public final void setClearCaseAttributes(final ClearCaseAttributes clearCaseAttributesP) {
		this.clearCaseAttributes = clearCaseAttributesP;
	}

	public final void setLength(final long lengthP) {
		this.length = lengthP;
	}

	public final void setFilePartialPath(final String filePartialPathP) {
		this.filePartialPath = filePartialPathP;
	}
}