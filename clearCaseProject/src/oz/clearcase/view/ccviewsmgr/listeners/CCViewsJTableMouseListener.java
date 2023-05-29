package oz.clearcase.view.ccviewsmgr.listeners;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import oz.clearcase.view.ccviewsmgr.gui.CCViewStatusAreaJPanel;
import oz.clearcase.view.ccviewsmgr.gui.CCViewsJDialog;

public class CCViewsJTableMouseListener extends MouseAdapter {
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public final void mouseClicked(final MouseEvent e) {
		logger.finest("Starting " + this.getClass().getSimpleName() + ": mouseClicked");
		int mouseEventModifiers = e.getModifiers();
		if ((mouseEventModifiers & InputEvent.BUTTON3_MASK) != 0) {
			int selectedRowCount = ((JTable) e.getSource()).getSelectedRowCount();
			logger.finest(String.valueOf(selectedRowCount) + " rows selected.");
			logger.finest(e.paramString());
			logger.finest(MouseEvent.getModifiersExText(InputEvent.BUTTON3_MASK));
			logger.finest(MouseEvent.getMouseModifiersText(InputEvent.BUTTON3_MASK));
			CCViewStatusAreaJPanel.setStatus1(String.valueOf(selectedRowCount) + " rows selected");
			if (selectedRowCount == 0) {
				JOptionPane.showMessageDialog(null, "No row has been selected ! \n");
				return;
			}
			if (CCViewsJDialog.getCCViewsJDialog() == null) {
				new CCViewsJDialog();
			}
			CCViewsJDialog.getCCViewsJDialog().setVisible(true);
		}
		logger.finest("Leaving " + this.getClass().getSimpleName() + ": mouseClicked");
	}
}
