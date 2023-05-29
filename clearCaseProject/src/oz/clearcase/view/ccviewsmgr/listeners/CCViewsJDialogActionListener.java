package oz.clearcase.view.ccviewsmgr.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import oz.clearcase.view.ccviewsmgr.gui.CCViewsJDialog;
import oz.clearcase.view.ccviewsmgr.viewoperations.MultipleViewsOperation;
import oz.infra.logging.jul.JulUtils;

public class CCViewsJDialogActionListener implements ActionListener {
	private CCViewsJDialog viewJDialog;
	private static final Logger logger = JulUtils.getLogger();

	public CCViewsJDialogActionListener(final CCViewsJDialog viewJDialogP) {
		this.viewJDialog = viewJDialogP;
	}

	public final void actionPerformed(final ActionEvent e) {
		viewJDialog.setVisible(false);
		String actionCommand = viewJDialog.getGroup().getSelection().getActionCommand();
		logger.finest("ViewJDialogActionListener: actionPerformed command=" + actionCommand);
		// ok dialog
		MultipleViewsOperation multipleViewsOperation = new MultipleViewsOperation();
		multipleViewsOperation.runMultipleViewsOperation(actionCommand);
	}
}
