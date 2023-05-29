package oz.infra.swing.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import oz.infra.logging.jul.JulUtils;

public class ImageUtils {
	private static Logger logger = JulUtils.getLogger();

	public static ImageIcon getResizedImageIcon(final String imageFilePath, final int imageWidth,
			final int imageHeight) {
		ImageIcon imageIcon = new ImageIcon(imageFilePath);
		Image image = imageIcon.getImage();
		Image scaledImage = image.getScaledInstance(imageWidth, imageHeight,
				java.awt.Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(scaledImage);
		return imageIcon;
	}

	public static BufferedImage loadImage(final String imageFilePath) {
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File(imageFilePath));
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			ex.printStackTrace();
		}
		return bufferedImage;
	}

	public static BufferedImage getResizedBufferedImage(BufferedImage bufferedImage, int newWidth,
			int newHeight) {
		int imageWidth = bufferedImage.getWidth();
		int imageHeight = bufferedImage.getHeight();
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, bufferedImage.getType());
		Graphics2D g = resizedImage.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(bufferedImage, 0, 0, newWidth, newHeight, 0, 0, imageWidth, imageHeight, null);
		g.dispose();
		return resizedImage;
	}

	public static BufferedImage getResizedBufferedImage(final String imageFilePath, int newWidth,
			int newHeight) {
		BufferedImage resizedImage = null;
		File imageFile = new File(imageFilePath);
		try {
			BufferedImage bufferedImage = javax.imageio.ImageIO.read(imageFile);
			logger.finest("bufferedImage.getType()" + bufferedImage.getType());
			int imageWidth = bufferedImage.getWidth();
			int imageHeight = bufferedImage.getHeight();
			resizedImage = new BufferedImage(newWidth, newHeight, bufferedImage.getType());
			Graphics2D g = resizedImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(bufferedImage, 0, 0, newWidth, newHeight, 0, 0, imageWidth, imageHeight,
					null);
			g.dispose();
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, "That was not an image "
					+ "format I recognized!  Please try again.");
		} catch (java.io.IOException e) {
			JOptionPane.showMessageDialog(null, "I was not able to read "
					+ "that image!  Please try again.");
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			ex.printStackTrace();
		}
		return resizedImage;
	}
}
