/*
 * Created on 10/02/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package oz.clearcase.view.ccviewsmgr.viewoperations;

import java.io.File;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import oz.clearcase.infra.ClearCaseUtils;
import oz.clearcase.view.ccviewsmgr.CCViewsParameters;
import oz.clearcase.view.ccviewsmgr.gui.CCViewsJTable;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;

/**
 * @author Oded
 */
public class MultipleViewsOperation {
	private static final String lineSeparator = System
			.getProperty("line.separator");
	private static StringBuffer scriptStringBuffer = new StringBuffer();
	private static final Logger logger = JulUtils.getLogger();

	public final void runMultipleViewsOperation(final String actionCommand) {
		EViewOperation viewOperation = EViewOperation.valueOf(actionCommand);
		IViewOperation viewOperationObject = viewOperation
				.getOperationClassObject();
		TableModel viewTableModel = CCViewsJTable.getViewJTable().getModel();
		int[] selectedRows = CCViewsJTable.getViewJTable().getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			selectedRows[i] = CCViewsJTable.getViewJTable()
					.convertRowIndexToModel(selectedRows[i]);
		}
		for (int i = 0; i < selectedRows.length; i++) {
			int j = selectedRows[i];
			String[] params = { (String) (viewTableModel.getValueAt(j, 1)),
					(String) (viewTableModel.getValueAt(j, 2)), null };
			logger.finer("params=" + params[0] + "," + params[1]);
			if (params[0] == null) {
				continue;
			}
			if (viewOperationObject instanceof RenameViewTagOperation) {
				params[2] = JOptionPane.showInputDialog("Enter new view tag:",
						params[0]);
			}
			boolean returnCode = viewOperationObject
					.exec(viewOperation, params);

			if (returnCode && !CCViewsParameters.isGenerateScripts()) {
				if (viewOperation == EViewOperation.RemoveViewOperation
						|| viewOperation == EViewOperation.RemoveViewTagOperation
						|| viewOperation == EViewOperation.RenameViewTagOperation) {
					viewTableModel.setValueAt("*** Deleted ***", j, 1);
					if (viewOperation == EViewOperation.RenameViewTagOperation) {
						viewTableModel.setValueAt(params[2], j, 1);
					}
					CCViewsJTable.getViewJTable().repaint();
				}
			}

		}
		JOptionPane.showMessageDialog(
				null,
				"Operation completed for "
						+ String.valueOf(selectedRows.length + " views !"));
		// Write script file
		if (scriptStringBuffer != null && scriptStringBuffer.length() > 0) {
			File scriptFile = new File(CCViewsParameters.getScriptFilePath());
			String ccCommandsString = scriptStringBuffer.toString()
					+ lineSeparator + "pause";
			FileUtils.writeFile(scriptFile, ccCommandsString);
		}
	}

	public static final StringBuffer getScriptStringBuffer() {
		return scriptStringBuffer;
	}

	public static final void add2Script(final String[] commandStringArray) {
		if (commandStringArray[0] == null
				|| commandStringArray[0].length() == 0) {
			commandStringArray[0] = ClearCaseUtils.getClearToolPath();
		}
		for (int i = 0; i < commandStringArray.length; i++) {
			scriptStringBuffer.append(commandStringArray[i] + " ");
		}
		scriptStringBuffer.append(lineSeparator + "echo %ERRORLEVEL%"
				+ lineSeparator);
	}
}
