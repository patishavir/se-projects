package oz.jdir.gui.listeners;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RowSMListSelectionListener implements ListSelectionListener {
	public void valueChanged(final ListSelectionEvent e) {
		// Ignore extra messages.
		if (e.getValueIsAdjusting()) {
			return;
		}
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (!lsm.isSelectionEmpty()) {
			int selectedRow = lsm.getMinSelectionIndex();
		}
	}
}