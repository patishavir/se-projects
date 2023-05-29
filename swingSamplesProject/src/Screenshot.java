import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class Screenshot {
	public static void main(String[] args) throws Exception {

		// Make sure we have the correct number of arguments
		if (args.length != 1) {
			System.err.println("Usage: java Screenshot " + "<time_interval_in_seconds>");
			System.exit(1);
		}

		// Get interval
		long interval = Long.parseLong(args[0]) * 1000L;

		// Create date formatter
		DateFormat dateFormat = new SimpleDateFormat("MMddyy HHmm");

		// Robot
		Robot robot = new Robot();

		// Determine current screen size
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		Rectangle screenRect = new Rectangle(screenSize);

		double scale = 0.5;
		BufferedImageOp op = new AffineTransformOp(AffineTransform.getScaleInstance(scale, scale),
				new RenderingHints(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BICUBIC));

		while (true) {
			// Create screen shot
			BufferedImage src = robot.createScreenCapture(screenRect);

			// Resize image (fails...)
			// image = image.getScaledInstance(image.getWidth()/2,
			// image.getHeight()/2, image.SCALE_SMOOTH);

			BufferedImage dst = op.filter(src, null);

			// Filename
			Date rightNow = new Date();
			String outFileName = dateFormat.format(rightNow) + ".png";

			// Save to file
			ImageIO.write(dst, "png", new File(outFileName));

			// Give feedback
			System.out.println("Saved to \"" + outFileName + "\"");

			// Wait the interval
			System.out.println("Waiting " + (interval / 1000L) + " seconds...");
			Thread.sleep(interval);
		}
	}
}
