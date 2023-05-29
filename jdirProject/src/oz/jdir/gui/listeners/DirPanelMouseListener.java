package oz.jdir.gui.listeners;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import oz.jdir.gui.DirOperationsJDialog;
import oz.jdir.gui.DirPanel;

public class DirPanelMouseListener extends MouseAdapter {
	private DirPanel dirPanel;

	public DirPanelMouseListener(final DirPanel dirPanelP) {
		this.dirPanel = dirPanelP;
	}

	public void mouseClicked(final MouseEvent e) {
		int m = e.getModifiers();
		if ((m & InputEvent.BUTTON3_MASK) != 0) {
			if (((JTable) e.getSource()).getSelectedRowCount() == 0) {
				JOptionPane.showMessageDialog(null, "No row has been selected ! \n");
				return;
			} else if (dirPanel.getDirJDialog() == null) {
				dirPanel.setDirJDialog(new DirOperationsJDialog(dirPanel));
			}
			dirPanel.getDirJDialog().setVisible(true);
		}
	}
}