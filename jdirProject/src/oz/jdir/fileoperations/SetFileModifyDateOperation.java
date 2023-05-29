package oz.jdir.fileoperations;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.JdirParameters;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class SetFileModifyDateOperation extends AbstractFileOperation {
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private static final String ERROR_MESSAGE1 = "Could not determine whether source or destination table is being processed !";

	public final boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		String sourceDir = sd.getDirName();
		FileInfo sourceFileInfo = sd.getFileInfoByRow(jdirIndex);
		logger.info("Starting SetFileModifyDateOperation operation");
		String[] sourceFileParams = { sourceDir, null };
		if (sourceFileInfo != null) {
			sourceFileParams[1] = sourceFileInfo.getFilePartialPath();
		}
		String file2ChangePath = sourceFileParams[0] + File.separator + sourceFileParams[1];
		JdirInfo myJd = null;
		if (JdirParameters.getSd().getDirName().equalsIgnoreCase(sourceDir)) {
			myJd = JdirParameters.getSd();
		} else if (JdirParameters.getDd().getDirName().equalsIgnoreCase(sourceDir)) {
			myJd = JdirParameters.getDd();
		} else {
			logger.severe(ERROR_MESSAGE1);
			System.exit(-1);
		}
		long timeInMillis = myJd.getLastModifiedCalendar().getTime().getTime();
		File file2ChangeFile = new File(file2ChangePath);
		if (!file2ChangeFile.exists()) {
			logger.warning("File " + file2ChangePath + " does not exists. Operation aborted !");
			return false;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm.ss");
		if (file2ChangeFile.setLastModified(timeInMillis)) {
			sourceFileInfo.setLastModified(timeInMillis);
			logger.finest("Last modified date for " + file2ChangeFile.getAbsolutePath()
					+ " successfully changed to " + formatter.format(new Date(timeInMillis)));
			return true;
		} else {
			logger.info("Failed to change Last modified date for "
					+ file2ChangeFile.getAbsolutePath());
			return false;
		}
	}
}