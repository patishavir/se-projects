package oz.jdir.fileoperations;

import java.io.File;
import java.util.logging.Logger;

import oz.infra.run.RunExecInaThread;
import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.JdirParameters;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class CompareOperation extends AbstractFileOperation {
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
		if (sourceFileInfo != null && destinationFileInfo != null) {
			/*
			 * source and destination are not null (both exist)
			 */
			File sourceFile = new File(sourceFileParams[0] + File.separator + sourceFileParams[1]);
			File destinationFile = new File(destinationFileParams[0] + File.separator
					+ destinationFileParams[1]);
			if (sourceFile.isFile() && destinationFile.isFile()) {
				String[] params = { JdirParameters.getCompareUtilityPath(),
						sourceFile.getAbsolutePath(), destinationFile.getAbsolutePath() };
				logger.info("CompareUtilityPath: " + params[0]);
				logger.info("sourceFile: " + params[1]);
				logger.info("destinationFile: " + params[2]);
				RunExecInaThread runExecInaThread = new RunExecInaThread(params);
				runExecInaThread.start();
				// RunExec re = new RunExec();
				// re.runCommand(params);
				return true;
			}
		}
		logger.warning("Source File and Destination file should both exist !");
		return true;
	}
}