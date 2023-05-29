import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import oz.infra.swing.image.ImageUtils;

public class ImageApp {

	public void loadAndDisplayImage(JFrame frame) {
		// Load the img
		BufferedImage loadImg = ImageUtils
				.loadImage("C:\\JFST\\args\\images\\static\\01_lili_scarf_left.jpg");
		frame.setBounds(0, 0, loadImg.getWidth(), loadImg.getHeight());
		// Set the panel visible and add it to the frame
		frame.setVisible(true);
		// Get the surfaces Graphics object
		Graphics2D g = (Graphics2D) frame.getRootPane().getGraphics();
		// Now draw the image
		g.drawImage(loadImg, null, 0, 0);

	}

	public static void main(String[] args) {
		ImageApp ia = new ImageApp();
		JFrame frame = new JFrame("Tutorials");
		ia.loadAndDisplayImage(frame);
	}

}
