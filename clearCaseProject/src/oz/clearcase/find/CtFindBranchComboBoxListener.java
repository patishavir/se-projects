package oz.clearcase.find;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JComboBox;

public class CtFindBranchComboBoxListener implements ActionListener {
	private CtFindParametersJPanel ctFindParametersJPanel;
	private Logger logger = Logger.getLogger(this.getClass().toString());

	/** Listens to the combo box. */
	CtFindBranchComboBoxListener(final CtFindParametersJPanel ctFindParametersJPanelP) {
		ctFindParametersJPanel = ctFindParametersJPanelP;
	}

	public final void actionPerformed(final ActionEvent e) {
		// ctFindParametersJPanel.populateBranchComboBox();
		JComboBox cb = (JComboBox) e.getSource();
		CtFindParametersJPanel.setBranchNameParameter((String) cb.getSelectedItem());
	}
}