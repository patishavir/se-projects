package oz.jdir.fileoperations;

import java.io.File;
import java.util.logging.Logger;

import oz.clearcase.infra.ClearCaseClass;
import oz.jdir.FileInfo;
import oz.jdir.JdirInfo;
import oz.jdir.JdirParameters;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class RebasePrepOperation extends AbstractFileOperation {
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
		logger.finer("Starting " + this.getClass().getSimpleName());
		if (!JdirParameters.isClearCaseIntegrationEnabled()
				|| !JdirParameters.getSd().isDirIsClearCaseManaged()
				|| !JdirParameters.getDd().isDirIsClearCaseManaged()) {
			logger.severe("Clear is not enabled during RebasePrep operation!\nProcessing stopped.");
			System.exit(-1);
		}
		logger.finer("File1=" + sourceFileParams[0] + File.separator + sourceFileParams[1]);
		logger.finer("File2=" + destinationFileParams[0] + File.separator
				+ destinationFileParams[1]);
		ClearCaseClass ccc = new ClearCaseClass();
		boolean noNeed2Mirror1 = destinationFileParams[1] == null;
		boolean noNeed2Mirror2 = destinationFileParams[1] != null
				&& (destinationFileInfo.getClearCaseAttributes().isFromFoundationBaseline());
		boolean noNeed2Mirror3 = (sourceFileParams[1] != null)
				&& (destinationFileParams[1] != null && (!destinationFileInfo
						.getClearCaseAttributes().isCCmaganaged()));
		if (noNeed2Mirror1 || noNeed2Mirror2 || noNeed2Mirror3) {
			return true;
		} else {
			if (sourceFileInfo != null
					&& (!sourceFileInfo.getClearCaseAttributes().isCCmaganaged())) {
				DeleteOperation deleteOperation = new DeleteOperation();
				boolean operationRC = deleteOperation.exec(sd, null, jdirIndex,
						fileOperationJDialog, null);
				return operationRC;
			} else if (destinationFileInfo != null
					&& (!destinationFileInfo.getClearCaseAttributes().isCCmaganaged())) {
				DeleteOperation deleteOperation = new DeleteOperation();
				boolean operationRC = deleteOperation.exec(dd, null, jdirIndex,
						fileOperationJDialog, null);
				return operationRC;
			} else {
				MirrorOperation mirrorOperation = new MirrorOperation();
				boolean operationRC = mirrorOperation.exec(sd, dd, jdirIndex, fileOperationJDialog,
						null);
				return operationRC;
			}
		}
	}
}