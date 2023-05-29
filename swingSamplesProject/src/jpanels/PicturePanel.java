package jpanels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PicturePanel extends JPanel {

	BufferedImage img;

	public PicturePanel() {
		// Load the image
		img = getImage("http://antwrp.gsfc.nasa.gov/apod/image/1003/centaurusA_carruthers.jpg");

		// Make the panel as big as the image is.
		this.setPreferredSize(new Dimension(800, 600));
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, getWidth(), getHeight(), 0, 0, img.getWidth(), img.getHeight(), null);
	}

	private BufferedImage getImage(String filename) {
		// This time, you can use an InputStream to load
		try {
			// Grab the URL for the image
			URL url = new URL(filename);

			// Then read it in.
			return ImageIO.read(url);
		} catch (IOException e) {
			System.out.println("The image was not loaded.");
			System.exit(1);
		}
		return null;
	}
}