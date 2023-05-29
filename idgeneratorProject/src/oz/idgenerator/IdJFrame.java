package oz.idgenerator;

import javax.swing.JFrame;

public class IdJFrame extends JFrame {
	IdJPanel idJPanel = new IdJPanel();

	IdJFrame() {
		setDefaultLookAndFeelDecorated(true);
		this.setResizable(true);
		// 2. Set the title.
		this.setTitle("Ids generator");
		// 3. Optional: What happens when the frame closes?
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//
		idJPanel.setOpaque(true);
		this.setContentPane(idJPanel);
		// 5. Size the frame.
		this.pack();
		// 6. Show it.
		this.setVisible(true);
	}
}
