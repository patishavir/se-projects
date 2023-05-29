package oz.jdir.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.ButtonModel;

import oz.jdir.JdirInfo;
import oz.jdir.JdirParameters;
import oz.jdir.MultipleFileOperation;
import oz.jdir.fileoperations.FileOperationsEnum;
import oz.jdir.gui.DateJDialog;
import oz.jdir.gui.DirPanel;

public class ProcButtonActionListener implements ActionListener {
	private Logger logger = Logger.getLogger(this.getClass().toString());

	private DirPanel dirPanel;

	private DateJDialog dateJDialog;

	private MultipleFileOperation multipleFileOperation;

	public ProcButtonActionListener(final DirPanel dirPanelP) {
		this.dirPanel = dirPanelP;
	}

	public final void actionPerformed(final ActionEvent e) {
		dirPanel.getDirJDialog().setVisible(false);
		if (e.getActionCommand().equalsIgnoreCase("escape")) {
			return;
		}
		multipleFileOperation = MultipleFileOperation.getMultipleFileOperation();
		JdirInfo mySource = null;
		JdirInfo myDestination = null;
		dirPanel.getDirJDialog().setVisible(false);
		ButtonModel mySelection = dirPanel.getDirJDialog().getGroup().getSelection();
		if (mySelection == null) {
			return;
		}
		String actionCommand = mySelection.getActionCommand();
		if (dirPanel == JdirParameters.getSd().getDirPanel()) {
			mySource = JdirParameters.getSd();
			myDestination = JdirParameters.getDd();
		} else if (dirPanel == JdirParameters.getDd().getDirPanel()) {
			mySource = JdirParameters.getDd();
			myDestination = JdirParameters.getSd();
		}
		logger.finest("Action command:" + actionCommand);

		if (FileOperationsEnum.isSetFileModifyDateOperation(actionCommand)) {
			if (dateJDialog == null) {
				dateJDialog = new DateJDialog(dirPanel);
			}
			dateJDialog.setVisible(true);
		}
		int[] selectedRows = dirPanel.getDirJTable().getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			selectedRows[i] = dirPanel.getDirJTable().convertRowIndexToModel(selectedRows[i]);
		}
		multipleFileOperation.processMultipleFileOperation(mySource, myDestination, selectedRows,
				actionCommand);
	}
}
