package oz.guigenerator.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class GuiGButtonListener implements ActionListener {
	private String jOptionPaneText;

	public GuiGButtonListener(final String jOptionPaneTextP) {
		jOptionPaneText = jOptionPaneTextP;
	}

	public final void actionPerformed(final ActionEvent e) {
		JOptionPane.showMessageDialog(null, jOptionPaneText);
	}
}
