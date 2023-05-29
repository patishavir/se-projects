package oz.jdir.fileoperations;

import java.io.File;
import java.util.logging.Logger;

import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class SyncOperation extends AbstractFileOperation {
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		String sourceDir = sd.getDirName();
		FileInfo sourceFileInfo = sd.getFileInfoByRow(jdirIndex);
		String destinationDir = dd.getDirName();
		FileInfo destinationFileInfo = dd.getFileInfoByRow(jdirIndex);
		String[] sourceFileParams = { sourceDir, null };
		if (sourceFileInfo != null) {
			sourceFileParams[1] = sourceFileInfo.getFilePartialPath();
		}
		String[] destinationFileParams = { destinationDir, null };
		if (destinationFileInfo != null) {
			destinationFileParams[1] = destinationFileInfo.getFilePartialPath();
		}
		if (sourceFileParams[1] == null && destinationFileParams[1] == null) {
			logger.severe("Source File and Destination file are both null !");
			return false;
		} else if (sourceFileParams[1] == null && destinationFileParams[1] != null) {
			boolean operationRC = new CopyOperation().exec(dd, sd, jdirIndex, fileOperationJDialog,
					FileOperationsEnum.Copy);
			return operationRC;
		} else if (sourceFileParams[1] != null && destinationFileParams[1] == null) {
			boolean operationRC = new CopyOperation().exec(sd, dd, jdirIndex, fileOperationJDialog,
					FileOperationsEnum.Copy);
			return operationRC;
		} else if (sourceFileParams[1] != null && destinationFileParams[1] == null) {
			boolean operationRC = new CopyOperation().exec(sd, dd, jdirIndex, fileOperationJDialog,
					FileOperationsEnum.Copy);
			return operationRC;
		} else if (sourceFileParams[1] != null && destinationFileParams[1] != null) {
			File sourceFile = new File(sourceFileParams[0] + File.separator + sourceFileParams[1]);
			File destinationFile = new File(destinationFileParams[0] + File.separator
					+ destinationFileParams[1]);
			if (sourceFileParams[1].equals(destinationFileParams[1])
					&& sourceFile.length() == destinationFile.length()
					&& sourceFile.lastModified() == destinationFile.lastModified())
				return true;
			if (sourceFile.lastModified() > destinationFile.lastModified()) {
				boolean operationRC = new CopyOperation().exec(sd, dd, jdirIndex,
						fileOperationJDialog, FileOperationsEnum.Copy);
				return operationRC;
			} else if (sourceFile.lastModified() < destinationFile.lastModified()) {
				boolean operationRC = new CopyOperation().exec(dd, sd, jdirIndex,
						fileOperationJDialog, FileOperationsEnum.Copy);
				return operationRC;
			}
		}
		return true;
	}
}