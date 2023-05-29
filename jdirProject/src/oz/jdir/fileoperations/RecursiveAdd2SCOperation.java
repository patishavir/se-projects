package oz.jdir.fileoperations;

import oz.clearcase.infra.ClearCaseClass;
import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class RecursiveAdd2SCOperation extends AbstractFileOperation {
	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		String sourceDir = sd.getDirName();
		FileInfo sourceFileInfo = sd.getFileInfoByRow(jdirIndex);
		String[] sourceFileParams = { sourceDir, sourceFileInfo.getFilePartialPath() };
		ClearCaseClass ccc = new ClearCaseClass();
		return ccc.recursiveAdd2(sourceFileParams[0], sourceFileParams[1]);
	}
}