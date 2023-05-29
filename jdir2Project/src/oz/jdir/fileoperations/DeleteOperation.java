package oz.jdir.fileoperations;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.io.FolderUtils;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;
import oz.jdir.fileoperations.userfeedback.UserFeedbackEnum;

public class DeleteOperation extends AbstractFileOperation {
	private boolean deleteRC = false;
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog,
			final FileOperationsEnum fileOperations1) {
		// Temporary Code: Sleep

		// End Of temporary code
		initialize(sd, dd, jdirIndex);
		final String dialogTitle = "File deletion confirmation dialog";
		final String dialogMsg = " Are you sure you want to delete ";
		String failureMessage = "Failed to delete " + sourceFilePath;
		if (fileOperationJDialog.getUserFeedBack() != UserFeedbackEnum.YESTOALL) {
			String myDialogMsg = dialogMsg;
			if (sourceFile.isHidden()) {
				myDialogMsg = dialogMsg + "the hidden file ";
			} else if (!sourceFile.canWrite()) {
				myDialogMsg = dialogMsg + "the read only file ";
			}
			fileOperationJDialog.showYesNoCancelDialog(dialogTitle, myDialogMsg + sourceFilePath);
		}
		if (fileOperationJDialog.getUserFeedBack() == UserFeedbackEnum.YES
				|| fileOperationJDialog.getUserFeedBack() == UserFeedbackEnum.YESTOALL) {
			if (!sourceFile.exists()) {
				// TODO issue error message and allow to proceed
				logger.warning(sourceFile.getAbsolutePath()
						+ " no longer exists!.\nDelete operation has not been performed.");
				System.exit(-1);
			}

			deleteRC = sourceFile.delete();
			if (!deleteRC && sourceFile.isDirectory() && sourceFile.listFiles().length > 0) {
				deleteDirectory();
			}

			if (deleteRC) {
				logger.info(sourceFilePath + " has been successfully deleted !");
				sd.removeFromFileList(jdirIndex);
				notifyObservers(sd, fileOperations1);
			}
		} else {
			logger.info(failureMessage);
		}
		return deleteRC;
	} /*
	 * recursively delete directory
	 */

	private void deleteDirectory() {
		File[] files2Delete = new FolderUtils().getAllFilesAndFoldersInFolder(sourceFile);
		for (int i = files2Delete.length; i > 0; i--) {
			File myFile = files2Delete[i - 1];
			String myAbsolutePath = myFile.getAbsolutePath();
			if (myFile.delete()) {
				logger.finest(myAbsolutePath + " deleted successfully");
			} else {
				logger.info("failed to delete " + myAbsolutePath);
			}
		}
		deleteRC = sourceFile.delete();
	}
}
