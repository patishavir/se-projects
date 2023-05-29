package oz.guigenerator.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import oz.infra.run.RunExecInaThread;

public class GuiGRunButtonActionListener implements ActionListener {
	private static String runCommandDelimiter = " ";
	private JTextField paramValueJTextField;

	public GuiGRunButtonActionListener(final JTextField paramValueJTextFieldP) {
		paramValueJTextField = paramValueJTextFieldP;
	}

	public final void actionPerformed(final ActionEvent e) {
		String[] params = paramValueJTextField.getText().split(runCommandDelimiter);
		RunExecInaThread runExecInaThread = new RunExecInaThread(params);
		runExecInaThread.start();
		// System.out.println("Return code = " + String.valueOf(returnCode));
	}

	public static void setRunCommandDelimiter(final String runCommandDelimiter) {
		GuiGRunButtonActionListener.runCommandDelimiter = runCommandDelimiter;
	}
}