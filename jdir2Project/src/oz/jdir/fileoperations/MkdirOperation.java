package oz.jdir.fileoperations;

import java.io.File;
import java.util.logging.Logger;

import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class MkdirOperation extends AbstractFileOperation {
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		String sourceDir = sd.getDirName();
		FileInfo sourceFileInfo = sd.getFileInfoByRow(jdirIndex);
		String[] sourceFileParams = { sourceDir, sourceFileInfo.getFilePartialPath() };
		final String dialogTitle = "mkdir dialog";
		final String dialogMsg1 = "<html><boby><b> Enter name for new directory in ";
		final String dialogMsg2 = " </b></body></html>";
		String newDirFullPath;
		String newDirName;
		File mkdirFile;
		boolean mkdirRc = false;
		File currentDir = new File(sourceFileParams[0] + File.separator + sourceFileParams[1]);
		if (!currentDir.isDirectory()) {
			logger.warning(currentDir.getAbsolutePath()
					+ " is not a directory. mkdir operation aborted !");
			return false;
		}
		String dialogMsg = dialogMsg1 + currentDir.getAbsolutePath() + dialogMsg2;
		newDirName = fileOperationJDialog.showInputDialog(dialogMsg, dialogTitle);
		if (newDirName == null || newDirName.length() <= 0)
			return false;
		newDirFullPath = currentDir.getAbsolutePath() + File.separator + newDirName;
		mkdirFile = new File(newDirFullPath);
		if (mkdirFile.exists()) {
			logger.warning(newDirFullPath + " already exists. Mkdir operation aborted !");
			return false;
		}
		mkdirRc = mkdirFile.mkdirs();

		if (mkdirRc) {
			logger.info(newDirFullPath + " has been successfully created.");
			return true;
		} else
			logger.info("Failed to create directory " + newDirFullPath);
		return false;
	}
}