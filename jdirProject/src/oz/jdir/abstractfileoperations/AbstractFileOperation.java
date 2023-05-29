package oz.jdir.abstractfileoperations;

import java.io.File;
import java.util.Observable;
import java.util.logging.Logger;

import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.fileoperations.FileOperationsEnum;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public abstract class AbstractFileOperation extends Observable {
	protected String sourceDir;
	protected FileInfo sourceFileInfo;
	protected String sourceFilePath;
	protected File sourceFile;
	protected String destinationDir;
	protected FileInfo destinationFileInfo;
	protected String destinationFilePath;
	protected File destinationFile;
	protected Logger logger = Logger.getLogger(this.getClass().toString());

	public abstract boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1);

	protected void initialize(final JdirInfo sd, final JdirInfo dd, final int jdirIndex) {
		sourceDir = sd.getDirName();
		sourceFileInfo = sd.getFileInfoByRow(jdirIndex);
		String[] sourceFileParams = { sourceDir, null };
		if (sourceFileInfo != null) {
			sourceFileParams[1] = sourceFileInfo.getFilePartialPath();
		}
		sourceFilePath = sourceFileParams[0] + File.separator + sourceFileParams[1];
		sourceFile = new File(sourceFilePath);
		destinationDir = dd.getDirName();
		destinationFileInfo = dd.getFileInfoByRow(jdirIndex);
		destinationFilePath = destinationDir + File.separator + sourceFileParams[1];
		destinationFile = new File(destinationFilePath);
	}

	protected void notifyObservers(final JdirInfo sd, FileOperationsEnum fileOperations1) {
		if (sd.getDirPanel() != null) {
			addObserver(sd.getDirPanel());
			setChanged();
			notifyObservers(fileOperations1.toString() + " " + sourceFilePath
					+ " has completed successfully");
			deleteObserver(sd.getDirPanel());
		}
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public String getDestinationFilePath() {
		return destinationFilePath;
	}
}