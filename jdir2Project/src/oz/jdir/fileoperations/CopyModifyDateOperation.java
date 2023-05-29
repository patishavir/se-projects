package oz.jdir.fileoperations;

import java.io.File;
import java.util.logging.Logger;

import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class CopyModifyDateOperation extends AbstractFileOperation {
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		String sourceDir = sd.getDirName();
		FileInfo sourceFileInfo = sd.getFileInfoByRow(jdirIndex);
		String destinationDir = dd.getDirName();
		FileInfo destinationFileInfo = dd.getFileInfoByRow(jdirIndex);
		String[] sourceFileParams = { sourceDir, sourceFileInfo.getFilePartialPath() };
		logger.finest(this.getClass().toString());
		String fileFrom = sourceFileParams[0] + File.separator + sourceFileParams[1];
		String fileTo = destinationDir + File.separator + sourceFileParams[1];
		File inputFile = new File(fileFrom);
		File outputFile = new File(fileTo);
		if (!inputFile.exists()) {
			logger.warning("File " + fileFrom + " does not exists. Operation aborted !");
			return false;
		}
		long timeInMillis = inputFile.lastModified();
		if (outputFile.setLastModified(timeInMillis) && destinationFileInfo != null) {
			destinationFileInfo.setLastModified(timeInMillis);
			logger.finest("CopyModDate " + fileFrom + " to " + fileTo);
			return true;
		} else {
			logger.finest("CopyModDate " + fileFrom + " to " + fileTo + "failed");
			return false;
		}
	}
}