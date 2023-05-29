package oz.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import oz.infra.swing.dimension.DimensionUtils;

public class ImagePanel extends JPanel {

	private BufferedImage image;
	private static final int space = 4;
	private static int pileHeight = 4;

	public ImagePanel() {
		try {
			image = ImageIO.read(new File(
					"C:\\oj\\projects\\swingSamplesProject\\src\\images\\image1.jpg"));
			Dimension preferredSize = new Dimension(image.getWidth() + space * (pileHeight + 1),
					image.getHeight() + space * (pileHeight + 1));
			setPreferredSize(preferredSize);
			DimensionUtils.getAsString(preferredSize, Level.FINEST);

			setOpaque(true);
		} catch (IOException ex) {
			ex.printStackTrace();
			// handle exception...
		}
	}

	// @Override
	// public void paintComponent(Graphics g) {
	// // super.paintComponent(g);
	// int space = 3;
	// g.setColor(Color.RED);
	// for (int i = 0; i < 1; i = i++) {
	// g.drawImage(image, 0, 0, null);
	// int currentSpace = (i + 1) * space;
	// g.drawLine(currentSpace, image.getHeight() - currentSpace, image
	// .getWidth()
	// - currentSpace, image.getHeight() - currentSpace);
	// g.drawLine(currentSpace, currentSpace, currentSpace, image
	// .getHeight()
	// - currentSpace);
	//
	// }
	//
	// }

	@Override
	public void paintComponent(Graphics g) {
		// super.paintComponent(g);
		// int mySpace = space;
		// g.drawLine(mySpace, mySpace, mySpace, image.getHeight() + mySpace);
		// g.drawLine(mySpace, mySpace, image.getWidth() + mySpace, mySpace);
		// mySpace = space * 2;
		// g.drawLine(mySpace, mySpace, mySpace, image.getHeight() + mySpace);
		// g.drawLine(mySpace, mySpace, image.getWidth() + mySpace, mySpace);
		// mySpace = space * 3;
		// g.drawLine(mySpace, mySpace, mySpace, image.getHeight() + mySpace);
		// g.drawLine(mySpace, mySpace, image.getWidth() + mySpace, mySpace);
		// *** GOOD ! ***
		// for (int i=1;i<pileHeight;i++) {
		// int mySpace = space * i;
		// g.drawLine(mySpace, mySpace, mySpace, image.getHeight() + mySpace);
		// g.drawLine(mySpace, mySpace, image.getWidth() + mySpace, mySpace);
		//
		// }
		g.setColor(Color.GRAY);
		setOpaque(true);
		// g.draw3DRect(space, space, image.getWidth(), image.getHeight(),
		// true);
		for (int i = 1; i < pileHeight; i++) {
			int mySpace = space * i;
			g.draw3DRect(mySpace, mySpace, image.getWidth(), image.getHeight(), true);
			g.fill3DRect(mySpace, mySpace, image.getWidth(), image.getHeight(), true);
		}
		g.drawImage(image, space * pileHeight, space * pileHeight, null);
	}

}
