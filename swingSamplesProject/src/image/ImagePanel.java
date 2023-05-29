package image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private BufferedImage image;

	private int width = -1;
	private int height = -1;

	public ImagePanel(final String imagePath) {
		try {
			image = ImageIO.read(new File(imagePath));
			width = image.getWidth();
			height = image.getHeight();
		} catch (IOException ex) {
			// handle exception...
			ex.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, null); // see javadoc for more info on the
										// parameters
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
