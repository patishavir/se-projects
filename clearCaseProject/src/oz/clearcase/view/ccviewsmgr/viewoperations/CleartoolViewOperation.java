package oz.clearcase.view.ccviewsmgr.viewoperations;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

import oz.clearcase.infra.ClearToolCommand;
import oz.clearcase.infra.ClearToolResults;
import oz.clearcase.view.ccviewsmgr.CCViewsParameters;
import oz.clearcase.view.ccviewsmgr.PrintCommandHandler;
import oz.infra.logging.jul.JulUtils;

public class CleartoolViewOperation implements IViewOperation {
	private static final Logger logger = JulUtils.getLogger();

	private ClearToolCommand ctc = new ClearToolCommand();

	public boolean exec(final EViewOperation viewOperation, final String[] params) {
		String[] clearToolparamsArray = viewOperation.getOperationCleartoolParametersArray();
		int numberOfParameters = 1;
		if (viewOperation.getParamsIndex() < 0) {
			numberOfParameters = 2;
		}
		String[] clearToolparams = new String[1 + clearToolparamsArray.length + numberOfParameters];
		clearToolparams[0] = "";
		for (int i = 0; i < clearToolparamsArray.length; i++) {
			clearToolparams[i + 1] = clearToolparamsArray[i];
		}
		if (numberOfParameters == 1) {
			clearToolparams[1 + clearToolparamsArray.length] = params[viewOperation
					.getParamsIndex()];
		} else if (numberOfParameters == 2) {
			clearToolparams[1 + clearToolparamsArray.length] = params[0];
			clearToolparams[1 + clearToolparamsArray.length + 1] = params[1];
		}
		if (viewOperation == EViewOperation.RemoveActivityFromViewOperation) {
			String[] removeActivityArray = new String[clearToolparams.length + 1];
			System.arraycopy(clearToolparams, 0, removeActivityArray, 0, clearToolparams.length);
			removeActivityArray[removeActivityArray.length - 1] = "-none";
			clearToolparams = removeActivityArray;
		}

		if (CCViewsParameters.isGenerateScripts()) {
			MultipleViewsOperation.add2Script(clearToolparams);
			return true;
		}
		PrintCommandHandler.printCommand(clearToolparams);
		ClearToolResults ctr = ctc.runClearToolCommand(clearToolparams, true);
		if ((ctr.getStdErr() != null) && ctr.getStdErr().length() > 0) {
			JOptionPane.showMessageDialog(null, ctr.getStdErr());
			logger.warning(ctr.getStdErr());
		}
		if ((ctr.getStdOut() != null) && ctr.getStdOut().length() > 0) {
			JOptionPane.showMessageDialog(null, ctr.getStdOut());

			logger.info(ctr.getStdOut());
		}
		if (ctr.getReturnCode() == 0) {

			return true;
		} else {
			return false;
		}

	}
}
