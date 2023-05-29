package oz.jdir.fileoperations;

import java.io.File;
import java.util.logging.Logger;

import oz.clearcase.infra.ClearCaseClass;
import oz.infra.io.PathUtils;
import oz.jdir.JdirInfo;
import oz.jdir.JdirParameters;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;
import oz.jdir.fileoperations.userfeedback.UserFeedbackEnum;

public class ChangeFileTypeOperation extends AbstractFileOperation {
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private static final String dialogTitle = "Chane file type confirmation dialog";
	private static final String dialogMsg = " Are you sure you want to change file type of ";

	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {

		initialize(sd, dd, jdirIndex);
		if ((JdirParameters.isGui())
				&& (JdirParameters.getFileType2Change() == null || JdirParameters.getNewFileType() == null)) {
			getFileTypes(fileOperationJDialog);
		}

		File newFile = null;
		String newFileName = null;
		String sourceFileName = sourceFile.getName();
		String sourceFileType = PathUtils.getFileExtension(sourceFile);

		if (!sourceFileType.equals("")
				&& sourceFileType.equals(JdirParameters.getFileType2Change())) {
			if (fileOperationJDialog.getUserFeedBack() != UserFeedbackEnum.YESTOALL) {
				fileOperationJDialog.showYesNoCancelDialog(dialogTitle, dialogMsg + sourceFilePath);
			}
			if (fileOperationJDialog.getUserFeedBack() == UserFeedbackEnum.YES
					|| fileOperationJDialog.getUserFeedBack() == UserFeedbackEnum.YESTOALL) {
				newFileName = sourceFileName.substring(0, sourceFileName.lastIndexOf(".") + 1)
						+ JdirParameters.getNewFileType();
				logger.finest("newFileName: " + newFileName);
				newFile = new File(sourceFile.getParent() + File.separator + newFileName);
				boolean fmchangeType1RC;
				ClearCaseClass ccc = new ClearCaseClass();
				if (ccc.isElementManaged(sourceFile.getAbsolutePath())) {
					fmchangeType1RC = ccc.mv(sourceFile.getAbsolutePath(),
							newFile.getAbsolutePath());
				} else {
					fmchangeType1RC = sourceFile.renameTo(newFile);
				}
				if (fmchangeType1RC) {
					logger.info(sourceFile.getAbsolutePath() + " changed type to "
							+ newFile.getAbsolutePath());
					sourceFileInfo.setFilePartialPath(newFile.getAbsolutePath().substring(
							sourceDir.length()));
					return true;
				} else {
					logger.info("Failed to change file type " + sourceFile.getAbsolutePath()
							+ " to " + JdirParameters.getNewFileType());
					return false;
				}
			}
		}
		return true;
	}

	private void getFileTypes(FileOperationJDialog fileOperationJDialog) {
		final String dialogTitle = "Change file type dialog";
		final String FileType2changeMsg = "<html><boby><b> Enter file type to change: ";
		final String newFileTypeMsg = "<html><boby><b> Enter new file type: ";
		final String dialogMsg2 = " </b></body></html>";
		String dialogMsg = FileType2changeMsg + dialogMsg2;
		JdirParameters.setFileType2Change(fileOperationJDialog.showInputDialog(dialogMsg,
				dialogTitle));
		dialogMsg = newFileTypeMsg + dialogMsg2;
		JdirParameters.setNewFileType(fileOperationJDialog.showInputDialog(dialogMsg, dialogTitle));
		logger.finest("JdirParameters.getFileType2Change: " + JdirParameters.getFileType2Change());
		logger.finest("JdirParameters.getNewFileType(): " + JdirParameters.getNewFileType());
	}
}