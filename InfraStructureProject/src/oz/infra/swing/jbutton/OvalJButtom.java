package oz.infra.swing.jbutton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;

public class OvalJButtom extends JButton {
	private Shape shape;

	public OvalJButtom(final String label) {
		super(label);
		Dimension size = getPreferredSize();
		size.width = Math.max(size.width, size.height);
		size.height = Math.max(size.width, size.height);
		setPreferredSize(size);
		setContentAreaFilled(false);
	}

	protected void paintComponent(Graphics g) {
		if (getModel().isArmed()) {
			g.setColor(Color.lightGray);
		} else {
			g.setColor(getBackground());
		}
		g.fillOval(0, getHeight() / 8, getWidth(), getHeight() - (getHeight() / 4));
		super.paintComponent(g);
	}

	protected void paintBorder(Graphics g) {
		g.setColor(getForeground());
		g.drawOval(0, getHeight() / 8, getWidth(), getHeight() - (getHeight() / 4));
	}

	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, getHeight() / 8, getWidth(), getHeight() - getHeight()
					/ 4);
		}
		return shape.contains(x, y);
	}
}
