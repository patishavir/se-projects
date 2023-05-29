package layeredpane;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class JLayeredPaneDemo extends JFrame {
	public JLayeredPaneDemo() {
		super("JLayeredPane Demo");
		setSize(256, 256);

		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setOpaque(false);

		JLabel label1 = new JLabel("Username:");
		label1.setForeground(Color.white);
		content.add(label1);

		JTextField field = new JTextField(15);
		content.add(field);

		JLabel label2 = new JLabel("Password:");
		label2.setForeground(Color.white);
		content.add(label2);

		JPasswordField fieldPass = new JPasswordField(15);
		content.add(fieldPass);

		setLayout(new FlowLayout());
		add(content);
		((JPanel) getContentPane()).setOpaque(false);

		ImageIcon earth = new ImageIcon("images/dukeWaveRed.gif");
		JLabel backlabel = new JLabel(earth);
		getLayeredPane().add(backlabel, new Integer(Integer.MIN_VALUE));
		backlabel.setBounds(0, 0, earth.getIconWidth(), earth.getIconHeight());
		backlabel.setBorder(BorderFactory.createLineBorder(Color.RED));
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		new JLayeredPaneDemo();
	}
}
