package undoredo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The lunch applet presents the Lunch button, which, when invoked lunches the
 * main frame
 * 
 * @author Tomer Meshorer
 */
public class UndoRedoJFrame extends JFrame {

	// Initialize the applet
	public UndoRedoJFrame() {
		// set look & feel
		String laf = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException exc) {
			System.err.println("Warning: UnsupportedLookAndFeel: " + laf);
		} catch (Exception exc) {
			System.err.println("Error loading " + laf + ": " + exc);
		}

		JButton lunchBtn = new JButton("Lunch");
		lunchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				UndoPanel app = new UndoPanel();
				JFrame frame = new JFrame();
				frame.getContentPane().add(app);
				frame.setSize(600, 300);
				frame.setVisible(true);

			}
		});
		getContentPane().add(lunchBtn);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		new UndoRedoJFrame();
	}

}
