/*
 * Created on 26/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package oz.clearcase.find;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import oz.clearcase.infra.ClearCaseClass;

/**
 * @author Oded TODO To change the template for this generated type comment go
 *         to Window - Preferences - Java - Code Style - Code Templates
 */
public class JmiLsvtreeActionListener implements ActionListener {
	private JTable resultsJTable;
	private final String lsvtreeString = "Version tree";

	JmiLsvtreeActionListener(final JTable resultsJTableP) {
		resultsJTable = resultsJTableP;
	}

	public final void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals(lsvtreeString)) {
			int numrows = resultsJTable.getSelectedRowCount();
			int[] rowsselected = resultsJTable.getSelectedRows();
			ClearCaseClass ccc = new ClearCaseClass();
			String[] lsvtreeParams = { ccc.getClearToolExePath(), "lsvtree", "-g", "" };
			for (int i = 0; i < numrows; i++) {
				lsvtreeParams[3] = (String) resultsJTable.getValueAt(rowsselected[i], 0);
				if (!ccc.clearToolCommand(lsvtreeParams)) {
					JOptionPane.showMessageDialog(null, "lsvtree failed for " + lsvtreeParams[3],
							"lsvtree failure", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}
	}
}