import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class FreezeEDT extends JFrame implements ActionListener {
	public FreezeEDT() {
		super("Freeze");
		JButton freezer = new JButton("Freeze");
		freezer.addActionListener(this);
		add(freezer);
		pack();
	}

	public void actionPerformed(ActionEvent e) {
		// Simulates a long running operation.
		// For instance: reading a large file,
		// performing network operations, etc.
		try {
			Thread.sleep(4000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String... args) {
		FreezeEDT edt = new FreezeEDT();
		edt.setVisible(true);
	}
}
