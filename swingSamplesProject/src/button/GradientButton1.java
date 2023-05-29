package button;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GradientButton1 extends JButton {

	public static void main(String[] args) {
		GradientButton1 button = new GradientButton1("Clickkkkk ...");
		JFrame frame = new JFrame("GradientButton1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(button);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public GradientButton1(final String text) {
		super();
		setText(text);
		setOpaque(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Composite oldComposite = g2.getComposite();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		// g2.setColor(Color.RED);
		// g2.fillOval(0, 0, 80, 40);

		g2.setComposite(oldComposite);
	}
}