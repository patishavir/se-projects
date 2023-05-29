package oz.clearcase.find;

import java.util.logging.Logger;

import javax.swing.JFrame;

class CtFindJFrame extends JFrame {
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public CtFindJFrame() {
		getContentPane().add(new CtFindJApplet());
		setTitle("Graphic interface for cleartool find command");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(0, 0);
		pack();
		// setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}
}