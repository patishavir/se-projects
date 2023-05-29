package oz.jdir.fileoperations;

import java.io.File;
import java.util.logging.Logger;

import oz.clearcase.infra.ClearCaseClass;
import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class RenameOperation extends AbstractFileOperation {
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex, final FileOperationJDialog fileOperationJDialog,
			FileOperationsEnum fileOperations1) {
		String sourceDir = sd.getDirName();
		FileInfo sourceFileInfo = sd.getFileInfoByRow(jdirIndex);
		String[] sourceFileParams = { sourceDir, sourceFileInfo.getFilePartialPath() };
		final String dialogTitle = "File rename dialog";
		final String dialogMsg1 = "<html><boby><b> Enter new name for ";
		final String dialogMsg2 = "  <br> from ";
		final String dialogMsg3 = " </b></body></html>";
		File inputFile = new File(sourceFileParams[0] + File.separator + sourceFileParams[1]);
		String renameOldName = inputFile.getName();
		String renameDirName = inputFile.getParent();
		String renamePath = null;
		String dialogMsg = dialogMsg1 + renameOldName + dialogMsg2 + renameDirName + dialogMsg3;
		String renameNewName = fileOperationJDialog.showInputDialog(dialogMsg, dialogTitle, renameOldName);
		if (renameNewName == null) {
			return false;
		}
		if (renameNewName.length() > 0)
			renamePath = renameDirName + File.separator + renameOldName;
		if (!inputFile.exists()) {
			logger.warning("File " + renamePath + " does not exists. Delete operation aborted !");
			return false;
		}
		File outputFile = new File(renameDirName + File.separator + renameNewName);
		boolean fmRename1RC;
		ClearCaseClass ccc = new ClearCaseClass();
		if (ccc.isElementManaged(inputFile.getAbsolutePath())) {
			fmRename1RC = ccc.mv(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
		} else {
			fmRename1RC = inputFile.renameTo(outputFile);
		}
		if (fmRename1RC) {
			logger.info(inputFile.getAbsolutePath() + " renamed to " + renameNewName);
			sourceFileInfo.setFilePartialPath(outputFile.getAbsolutePath().substring(sourceDir.length()));
			return true;
		} else {
			logger.info("Failed to rename " + inputFile.getAbsolutePath() + " to " + renameNewName);
			return false;
		}
	}
}