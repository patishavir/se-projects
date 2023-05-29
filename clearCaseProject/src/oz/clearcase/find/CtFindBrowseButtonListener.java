package oz.clearcase.find;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

public class CtFindBrowseButtonListener implements ActionListener {
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private CtFindParametersJPanel ctFindParametersJPanel;

	/** Listens to the combo box. */
	public CtFindBrowseButtonListener(final CtFindParametersJPanel ctFindParametersJPanelP) {
		this.ctFindParametersJPanel = ctFindParametersJPanelP;
	}

	public final void actionPerformed(final ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			ctFindParametersJPanel.getvobDirectoryJTextField().setText(
					fc.getSelectedFile().getAbsolutePath());
			ctFindParametersJPanel.populateBranchComboBox();
		}
	}
}