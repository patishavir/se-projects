package oz.infra.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.dimension.DimensionUtils;

class ImageRenderComponent extends JPanel {
	private BufferedImage bufferedImage;
	private Dimension size;

	public ImageRenderComponent(final BufferedImage image) {
		this.bufferedImage = image;
		size = new Dimension(image.getWidth(), image.getHeight());
	}

	public Dimension getPreferredSize() {
		return size;
	}

	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		int x = (getWidth() - size.width) / 2;
		int y = (getHeight() - size.height) / 2;
		g.drawImage(bufferedImage, x, y, this);
	}
}

public class SwingUtils {
	private static Logger logger = JulUtils.getLogger();

	/** Returns an ImageIcon, or null if the path was invalid. */
	public static ImageIcon createImageIcon(final String path, final String description) {
		java.net.URL imgURL = Class.class.getResource(path);
		if (imgURL != null) {
			logger.info("imgURL:" + imgURL.getPath());
			ImageIcon imageIcon = new ImageIcon(imgURL, description);
			logger.info("Icon height: " + String.valueOf(imageIcon.getIconHeight()));
			return imageIcon;
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	private static void drawBufferedImage(final BufferedImage image) {
		ImageRenderComponent irc = new ImageRenderComponent(image);
		irc.setOpaque(true); // for use as a content pane
		// JFrame jframe = new JFrame();
		JDialog jframe = new JDialog();
		// jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// add as a component
		// f.getContentPane().add(irc);
		// set as content pane
		jframe.setContentPane(irc);
		jframe.setSize(image.getWidth() + OzConstants.INT_50, image.getHeight()
				+ OzConstants.INT_50);
		int screenHeight = DimensionUtils.getScreenHeight();
		int screenWidth = DimensionUtils.getScreenWidth();
		jframe.setLocation((screenWidth - image.getWidth()) / 2,
				(screenHeight - image.getHeight()) / 2);
		jframe.setVisible(true);
	}

	public static void drawImage(final BufferedImage image) {
		drawBufferedImage(image);
	}

	public static void drawImage(final String imageFilePath) {
		try {
			BufferedImage image = ImageIO.read(new File(imageFilePath));
			drawBufferedImage(image);
		} catch (IOException ioex) {
			ioex.printStackTrace();
			logger.warning(ioex.getMessage());
		}
	}
}
