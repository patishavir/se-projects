package oz.jdir;

import java.util.Observable;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import oz.infra.system.SystemUtils;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.DeleteOperation;
import oz.jdir.fileoperations.FileOperationFactory;
import oz.jdir.fileoperations.FileOperationTypesEnum;
import oz.jdir.fileoperations.FileOperationsEnum;
import oz.jdir.fileoperations.Print2FileDirectoryInfoOperation;
import oz.jdir.fileoperations.PrintFileDirectoryInfoOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;
import oz.jdir.fileoperations.userfeedback.UserFeedbackEnum;
import oz.jdir.gui.DirFrame;

public class MultipleFileOperation extends Observable implements Runnable {
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private FileOperationJDialog fileOperationJDialog = new FileOperationJDialog();
	private int fileOperationCounter;
	private String operationString;
	private static MultipleFileOperation multipleFileOperation = new MultipleFileOperation();
	private JdirInfo sd;
	private JdirInfo dd;
	private int[] selectedRows;
	private String actionCommand;

	/*
	 * multipleFileOperation
	 */

	public final Thread processMultipleFileOperation(final JdirInfo sd, final JdirInfo dd,
			final int[] selectedRows, final String actionCommand) {
		MultipleFileOperation multipleFileOperation = new MultipleFileOperation();
		multipleFileOperation.sd = sd;
		multipleFileOperation.dd = dd;
		multipleFileOperation.selectedRows = selectedRows;
		multipleFileOperation.actionCommand = actionCommand;
		logger.info(" Main thead: " + Thread.currentThread().getName() + " id:"
				+ Thread.currentThread().getId());
		logger.finest(" Thread started ...");
		Thread multipleFileOperationThread = new Thread(multipleFileOperation);
		multipleFileOperationThread.start();
		return multipleFileOperationThread;
	}

	public void run() {
		logger.finest("Thead: " + Thread.currentThread().getName() + " id:"
				+ Thread.currentThread().getId());

		FileOperationsEnum fileOperationsEnum = FileOperationsEnum.valueOf(actionCommand);
		AbstractFileOperation fileOperation = FileOperationFactory.getFileOperationFactory()
				.getFileOperation(actionCommand);
		if (fileOperation == null) {
			SystemUtils.printMessageAndExit("Invalid operation " + actionCommand
					+ ". Processing aborted!", -1);
		}
		if (!fileOperationsEnum.isEnabled()) {
			SystemUtils.printMessageAndExit("Operation " + actionCommand
					+ " is not enabled. Processing aborted!", -1);
		}
		if (fileOperationsEnum.isTemporarilyDisabled()) {
			logger.warning("Operation " + operationString
					+ " is temporarily disabled. Processing aborted!");
		} else {
			operationString = fileOperationsEnum.toString();
			logger.finest(operationString + " started");
			fileOperationJDialog.setUserFeedBack(UserFeedbackEnum.UNDEF);
			if (!(JdirParameters.getSd().getMatchTableEntries() > 0)) {
				JOptionPane.showMessageDialog(null, "No files to process. Request ignored");
				return;
			}
			if (fileOperationsEnum.getoperationType() == FileOperationTypesEnum.clearCaseOperation
					|| fileOperationsEnum.getoperationType() == FileOperationTypesEnum.clearCaseTwoSidedOperation) {
				if (!JdirParameters.isClearCaseIntegrationEnabled()
						|| !sd.isDirIsClearCaseManaged()) {
					return;
				}
			}
			/*
			 * Disable GUI
			 */
			for (FileOperationsEnum fileoperation1 : FileOperationsEnum.values()) {
				fileoperation1.disableFileOperationGUI(true);
			}
			fileOperationCounter = 0;
			executeFileOperation(sd, dd, selectedRows, fileOperation, fileOperationsEnum);
			switch (fileOperationsEnum) {
			case Move:
			case MoveAll:
				executeFileOperation(sd, dd, selectedRows, new DeleteOperation(),
						FileOperationsEnum.Delete);
				break;
			case Print2DirInfo:
				System.out.println(((Print2FileDirectoryInfoOperation) fileOperation)
						.getStringBuffer().toString() + "\n");
				break;
			case PrintDirInfo:
			case PrintAllDirInfo:
				System.out.println(((PrintFileDirectoryInfoOperation) fileOperation)
						.getStringBuffer().toString() + "\n");
			case ChangeFileType:
				JdirParameters.setFileType2Change(null);
				JdirParameters.setNewFileType(null);
				break;
			case FindAndReplace:
				JdirParameters.setString2Find(null);
				JdirParameters.setReplaceWithString(null);
				break;
			default:
			}
			String completionMessage = operationString + " completed on " + fileOperationCounter
					+ " files/folders.";
			logger.info(completionMessage);
			if (JdirParameters.isGui()) {
				DirFrame.getDirFrame().repaint();
				if (sd.getDirPanel() != null) {
					addObserver(sd.getDirPanel());
					setChanged();
					notifyObservers(completionMessage);
					deleteObserver(sd.getDirPanel());
				}
			}
			for (FileOperationsEnum fileoperation1 : FileOperationsEnum.values()) {
				fileoperation1.disableFileOperationGUI(false);
			}
			return;
		}
	}

	/*
	 * executeFileOperation
	 */
	private void executeFileOperation(final JdirInfo sd, final JdirInfo dd,
			final int[] selectedRows, final AbstractFileOperation fileOperation,
			final FileOperationsEnum fileOperations1) {
		int upperLimit, currentRow;
		if (selectedRows == null) {
			upperLimit = sd.getMatchTableEntries();
		} else {
			upperLimit = selectedRows.length;
		}
		int progressIndicatorInterval = JdirParameters.getProgressIndicatorInterval();
		/*
		 * main loop
		 */
		for (int rowIndex = 0; rowIndex < upperLimit; rowIndex++) {
			if ((rowIndex > 0) && (rowIndex % progressIndicatorInterval) == 0) {
				logger.info("Processing starting for file " + String.valueOf(rowIndex) + " out of "
						+ String.valueOf(upperLimit));
			}
			if (selectedRows == null) {
				currentRow = rowIndex;
			} else {
				currentRow = selectedRows[rowIndex];
			}
			boolean twoSidedOperation = fileOperations1.getoperationType() == FileOperationTypesEnum.twoSidedFileOperation
					|| fileOperations1.getoperationType() == FileOperationTypesEnum.clearCaseTwoSidedOperation;
			if (!twoSidedOperation && sd.getFileInfoByRow(currentRow) == null) {
				continue;
			}
			if (twoSidedOperation && sd.getFileInfoByRow(currentRow) == null
					&& dd.getFileInfoByRow(currentRow) == null) {
				continue;
			}
			if (fileOperationJDialog.getUserFeedBack() == UserFeedbackEnum.CANCEL) {
				break;
			}

			FileInfo sourceFileInfo = sd.getFileInfoByRow(currentRow);
			String destinationDir = dd.getDirName();
			FileInfo destinationFileInfo = dd.getFileInfoByRow(currentRow);
			logger.info("current row: " + String.valueOf(currentRow));
			boolean operationRC = fileOperation.exec(sd, dd, currentRow, fileOperationJDialog,
					fileOperations1);
			if (fileOperationJDialog.getUserFeedBack() == UserFeedbackEnum.CANCEL) {
				break;
			}
			String myFilePath = "";
			if (!twoSidedOperation || sourceFileInfo != null) {

				myFilePath = fileOperation.getSourceFilePath();
			} else if (twoSidedOperation && sourceFileInfo == null) {
				myFilePath = fileOperation.getDestinationFilePath();
			}
			if (!operationRC) {
				fileOperationJDialog.showMessageDialog(operationString + " failed for "
						+ myFilePath + " !");
			} else {
				String successMessage = operationString + " succeeded for " + myFilePath + " !";
				logger.info(successMessage);
				multipleFileOperation.setChanged();
				multipleFileOperation.notifyObservers(successMessage);
				fileOperationCounter++;
			}
		}
		/*
		 * end of main loop
		 */
	}

	public static final MultipleFileOperation getMultipleFileOperation() {
		return multipleFileOperation;
	}
}