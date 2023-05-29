package oz.jdir.fileoperations;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class Print2FileDirectoryInfoOperation extends AbstractFileOperation {
	private File file1, file2;
	private StringBuffer stringBuffer = new StringBuffer();
	private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy  HH:mm.ss");

	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		String sourceDir = sd.getDirName();
		FileInfo sourceFileInfo = sd.getFileInfoByRow(jdirIndex);
		String destinationDir = dd.getDirName();
		FileInfo destinationFileInfo = dd.getFileInfoByRow(jdirIndex);
		String[] sourceFileParams = { sourceDir, null };
		if (sourceFileInfo != null) {
			sourceFileParams[1] = sourceFileInfo.getFilePartialPath();
		}
		String[] destinationFileParams = { destinationDir, null };
		if (destinationFileInfo != null) {
			destinationFileParams[1] = destinationFileInfo.getFilePartialPath();
		}
		file1 = null;
		file2 = null;
		if ((sourceFileParams[0] != null) && (sourceFileParams[1] != null)) {
			file1 = new File(sourceFileParams[0] + File.separator + sourceFileParams[1]);
		}
		if ((destinationFileParams[0] != null) && (destinationFileParams[1] != null)) {
			file2 = new File(destinationFileParams[0] + File.separator + destinationFileParams[1]);
		}
		stringBuffer.append("\n");
		if (file1 != null) {
			stringBuffer.append(file1.getAbsolutePath() + ",\t " + file1.length() + ",\t "
					+ formatter.format(new Date(file1.lastModified())) + "\t,");
		} else {
			stringBuffer.append("\t\t\t,\t,\t,\t");
		}
		if (file2 != null) {
			stringBuffer.append("\t" + file2.getAbsolutePath() + ",\t " + file2.length() + ",\t "
					+ formatter.format(new Date(file2.lastModified())));
		}
		return true;
	}

	public final StringBuffer getStringBuffer() {
		return stringBuffer;
	}
}