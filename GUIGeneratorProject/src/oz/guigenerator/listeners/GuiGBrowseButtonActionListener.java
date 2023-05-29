package oz.guigenerator.listeners;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class GuiGBrowseButtonActionListener implements ActionListener {
	private Component parent;
	private JTextField paramValueJTextField;

	public GuiGBrowseButtonActionListener(final Component parentP,
			final JTextField paramValueJTextFieldP) {
		parent = parentP;
		paramValueJTextField = paramValueJTextFieldP;
	}

	public final void actionPerformed(final ActionEvent e) {
		File myDir = new File(paramValueJTextField.getText());
		JFileChooser fc;
		if (myDir.isDirectory()) {
			fc = new JFileChooser(myDir);
		} else {
			fc = new JFileChooser();
		}
		fc.setFileSelectionMode(Integer.parseInt(e.getActionCommand()));
		int returnVal = fc.showOpenDialog(parent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			paramValueJTextField.setText(fc.getSelectedFile().getAbsolutePath());
		}
	}
}