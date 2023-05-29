package oz.jdir.fileoperations;

import java.util.logging.Logger;

import oz.clearcase.infra.ClearCaseClass;
import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class MkelemOperation extends AbstractFileOperation {
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		String sourceDir = sd.getDirName();
		FileInfo sourceFileInfo = sd.getFileInfoByRow(jdirIndex);
		if ((sourceFileInfo.getClearCaseAttributes() != null)
				&& (sourceFileInfo.getClearCaseAttributes().isCCmaganaged())) {
			return true;
		} else {
			ClearCaseClass ccc = new ClearCaseClass();
			boolean returnCode = ccc.mkelem(sourceDir, sourceFileInfo.getFilePartialPath());
			if (returnCode && sourceFileInfo.getClearCaseAttributes() != null) {
				sourceFileInfo.getClearCaseAttributes().setCCmaganaged(true);
				sourceFileInfo.getClearCaseAttributes().setCheckedOut(true);
			}
			return returnCode;
		}
	}
}