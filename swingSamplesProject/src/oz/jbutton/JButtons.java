package oz.jbutton;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import jpanels.ExitListener;

public class JButtons extends JFrame {
	public static void main(String[] args) {
		new JButtons();
	}

	public JButtons() {
		super("Using JButton");
		WindowUtilities.setNativeLookAndFeel();
		addWindowListener(new ExitListener());
		Container content = getContentPane();
		content.setBackground(Color.white);
		content.setLayout(new FlowLayout());
		JButton button1 = new JButton("Java");
		content.add(button1);
		ImageIcon cup = new ImageIcon(
				"C:\\oj\\projects\\swingSamplesProject\\src\\oz\\jbutton\\images\\cup.gif");
		JButton button2 = new JButton(cup);
		content.add(button2);
		JButton button3 = new JButton("Java", cup);
		content.add(button3);
		JButton button4 = new JButton("Java 4", cup);
		button4.setHorizontalTextPosition(SwingConstants.LEFT);
		content.add(button4);
		pack();
		setVisible(true);
	}
}