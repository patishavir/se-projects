package oz.jdir.fileoperations;

import java.util.logging.Logger;

import oz.clearcase.infra.ClearToolCommand;
import oz.clearcase.infra.ClearToolResults;
import oz.infra.array.ArrayUtils;
import oz.jdir.JdirInfo;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.fileoperations.userfeedback.FileOperationJDialog;

public class ClearCaseOperation extends AbstractFileOperation {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public boolean exec(final JdirInfo sd, final JdirInfo dd, final int jdirIndex,
			final FileOperationJDialog fileOperationJDialog, FileOperationsEnum fileOperations1) {
		initialize(sd, dd, jdirIndex);

		String[] clearToolparams = null;
		switch (fileOperations1) {
		case Checkin:
			String[] checkInparams = { null, "checkin", "-nc", "-identical", sourceFilePath };
			clearToolparams = checkInparams;
			break;
		case Checkout:
			String[] checkOutParams = { null, "checkout", "-nwarn", "-nc", sourceFilePath };
			clearToolparams = checkOutParams;
			break;
		case Undocheckout:
			String[] uncheckOutParams = { null, "uncheckout", "-keep", sourceFilePath };
			clearToolparams = uncheckOutParams;
			break;
		case Update:
			String[] updateParams = { null, "update", "-rename", "-ptime", sourceFilePath };
			clearToolparams = updateParams;
			break;
		case Versiontree:
			String[] versionTreeParams = { null, "lsvtree", "-g", "-a", sourceFilePath };
			clearToolparams = versionTreeParams;
			break;
		}
		ArrayUtils.printArray(clearToolparams);
		ClearToolCommand ctc = new ClearToolCommand();
		ClearToolResults ctr = ctc.runClearToolCommand(clearToolparams, true);
		boolean returnCode = ctr.getReturnCode() == 0;
		if (returnCode) {
			switch (fileOperations1) {
			case Checkin:
			case Undocheckout:
				sourceFileInfo.getClearCaseAttributes().setCheckedOut(false);
				break;
			case Checkout:
				sourceFileInfo.getClearCaseAttributes().setCheckedOut(true);
				break;
			default:
			}
		} else {
			logger.warning(ctr.getStdErr());
		}
		return returnCode;
	}
}
