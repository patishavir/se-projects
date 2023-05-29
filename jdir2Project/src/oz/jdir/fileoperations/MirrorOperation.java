package oz.jdir.fileoperations;

import java.io.File;
import java.util.logging.Logger;

import oz.jdir.FileComparison;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class MirrorOperation extends AbstractFileOperation {
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		initialize(sd, dd, jdirIndex);

		String[] sourceFileParams = { sourceDir, null };
		if (sourceFileInfo != null) {
			sourceFileParams[1] = sourceFileInfo.getFilePartialPath();
		}
		String[] destinationFileParams = { destinationDir, null };
		if (destinationFileInfo != null) {
			destinationFileParams[1] = destinationFileInfo.getFilePartialPath();
		}
		if (sourceFileParams[1] == null && destinationFileParams[1] != null) {
			/*
			 * source does not exist and destination exists
			 */
			boolean operationRC = new DeleteOperation().exec(dd, null, jdirIndex,
					fileOperationJDialog, null);
			return operationRC;
		} else if (sourceFileParams[1] != null && destinationFileParams[1] == null) {
			/*
			 * destination does not exists
			 */
			boolean operationRC = new CopyOperation().exec(sd, dd, jdirIndex, fileOperationJDialog,
					null);
			return operationRC;
		} else if (sourceFileParams[1] != null && destinationFileParams[1] != null) {
			/*
			 * source and destination are not null (both exist)
			 */
			String sourceFileFullPath = sourceFileParams[0] + File.separator + sourceFileParams[1];
			String destinationFileFullPath = destinationFileParams[0] + File.separator
					+ destinationFileParams[1];
			if (FileComparison.compareFiles(sourceFileInfo, sourceFileParams[1],
					sourceFileFullPath, destinationFileInfo, destinationFileParams[1],
					destinationFileFullPath)) {
				return true;
			}
			boolean operationRC = new CopyOperation().exec(sd, dd, jdirIndex, fileOperationJDialog,
					null);
			return operationRC;
		} else if (sourceFileParams[1] == null && destinationFileParams[1] == null) {
			logger.fine("Source File and Destination file are both null !");
		}
		return true;
	}
}