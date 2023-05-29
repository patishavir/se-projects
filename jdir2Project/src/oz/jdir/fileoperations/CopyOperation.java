package oz.jdir.fileoperations;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import oz.infra.io.FileUtils;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;
import oz.jdir.fileoperations.userfeedback.UserFeedbackEnum;

// TODO Folder copy should copy contents 
public class CopyOperation extends AbstractFileOperation {
	private static final String overWriteDialogTitle = "File overwrite confirmation dialog";
	private static final String roOoverWriteDialogTitle = "Read only file overwrite confirmation dialog";
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		initialize(sd, dd, jdirIndex);
		final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy  HH:mm.ss");
		;
		String fileFromDate;
		String fileToDate;
		try {
			if (!sourceFile.exists()) {
				logger.warning("File " + sourceFilePath + " does not exists. Copy aborted !");
				return false;
			}
			String outputFileDir = destinationFilePath.substring(0,
					destinationFilePath.lastIndexOf(File.separator));
			File outputFileDirFile = new File(outputFileDir);
			String existingoutputPart = destinationFilePath;
			File existingoutputPartFile = new File(destinationFilePath);
			while (!existingoutputPart.equals(destinationDir)) {
				existingoutputPartFile = new File(existingoutputPart);
				if (existingoutputPartFile.exists()) {
					break;
				}
				existingoutputPart = existingoutputPartFile.getParent();
			}
			logger.finest("existing output Folder Part=" + existingoutputPart);
			if (!outputFileDirFile.isDirectory()) {
				outputFileDirFile.mkdirs();
			}
			fileFromDate = formatter.format(new Date(destinationFile.lastModified()));
			fileToDate = formatter.format(new Date(sourceFile.lastModified()));
			String dialogMessage = setDialogMsg1(destinationFilePath, fileFromDate, sourceFilePath,
					fileToDate);
			if (destinationFile.exists()
					&& fileOperationJDialog.getUserFeedBack() != UserFeedbackEnum.YESTOALL) {
				String myTitle = overWriteDialogTitle;
				if (!destinationFile.canWrite()) {
					myTitle = roOoverWriteDialogTitle;
				}
				if (sourceFile.lastModified() < destinationFile.lastModified()
						|| !destinationFile.canWrite()) {
					fileOperationJDialog.showYesNoCancelDialog(myTitle, dialogMessage);
					if (fileOperationJDialog.getUserFeedBack() == UserFeedbackEnum.CANCEL
							|| fileOperationJDialog.getUserFeedBack() == UserFeedbackEnum.NO) {
						return false;
					}
				}
			}
			boolean copyRC = false;
			//
			if (destinationFile.exists() && !destinationFile.canWrite()) {
				fileOperationJDialog.showMessageDialog(destinationFile.getAbsoluteFile()
						+ " is read only and cannot be overwritten.\n" + "Copy operation aborted!");
				return false;
			}
			if (sourceFile.isFile()) {
				copyRC = FileUtils.copyFile(sourceFile, destinationFile);
				if (!copyRC) {
					return false;
				}
			} else if (sourceFile.isDirectory() && (!destinationFile.exists())) {
				destinationFile.mkdir();
			}
			CopyModifyDateOperation copyModifyDateOperation = new CopyModifyDateOperation();
			copyModifyDateOperation.exec(sd, dd, jdirIndex, fileOperationJDialog, null);
			if (destinationFileInfo != null) {
				destinationFileInfo.setLength(sourceFileInfo.length());
			} else {
				int fileListPtr = dd.add2FileList(sourceFileInfo);
				dd.setFileInfoByRow(jdirIndex, fileListPtr);

			}
			logger.info(sourceFilePath + " successfully copied to " + destinationFilePath + " !");
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	//
	private String setDialogMsg1(final String destinationFilePath, final String fileFromDate,
			final String sourceFilePath, final String fileToDate) {
		final String dialogMsg1 = "<html><body><p><br>Are you sure you want to overwrite <br>";
		final String dialogMsg2 = " from ";
		final String dialogMsg3 = " &nbsp;&nbsp;&nbsp;<br> By <br> ";
		final String dialogMsg4 = " from ";
		final String dialogMsg5 = " ? &nbsp;&nbsp;&nbsp; <br></p></body></html>";
		return dialogMsg1 + destinationFilePath + dialogMsg2 + fileFromDate + dialogMsg3
				+ sourceFilePath + dialogMsg4 + fileToDate + dialogMsg5;
	}
}