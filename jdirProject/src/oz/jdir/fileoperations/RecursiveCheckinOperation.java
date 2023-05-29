package oz.jdir.fileoperations;

import java.io.File;

import oz.clearcase.infra.ClearCaseClass;
import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class RecursiveCheckinOperation extends AbstractFileOperation {
	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		String sourceDir = sd.getDirName();
		FileInfo sourceFileInfo = sd.getFileInfoByRow(jdirIndex);
		ClearCaseClass ccc = new ClearCaseClass();
		return ccc.recursiveCheckin(sourceDir + File.separator
				+ sourceFileInfo.getFilePartialPath());
	}
}