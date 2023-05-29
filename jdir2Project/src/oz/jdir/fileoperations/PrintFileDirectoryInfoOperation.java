package oz.jdir.fileoperations;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class PrintFileDirectoryInfoOperation extends AbstractFileOperation {
	private File sourceFile;
	private StringBuffer stringBuffer = new StringBuffer();
	private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy  HH:mm.ss");

	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		String sourceDir = sd.getDirName();
		FileInfo sourceFileInfo = sd.getFileInfoByRow(jdirIndex);
		String[] sourceFileParams = { sourceDir, null };
		if (sourceFileInfo != null) {
			sourceFileParams[1] = sourceFileInfo.getFilePartialPath();
			sourceFile = new File(sourceFileParams[0] + File.separator + sourceFileParams[1]);
			stringBuffer.append("\n" + sourceFile.getAbsolutePath() + ",\t " + sourceFile.length()
					+ ",\t " + formatter.format(new Date(sourceFile.lastModified())));
		}
		return true;
	}

	public final StringBuffer getStringBuffer() {
		return stringBuffer;
	}
}