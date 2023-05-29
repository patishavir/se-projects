package oz.infra.swing.dimension;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class DimensionUtils {
	private static Logger logger = JulUtils.getLogger();

	public static Dimension adjustDimension(final Dimension dimension, final int xMargin,
			final int yMargin) {
		final double heightRatio = 0.80;
		final double widthRatio = 0.95;
		double dimensionHeight = dimension.getHeight();
		double dimensionWidth = dimension.getWidth();
		Dimension screenDimension = getScreenDimension();
		double screenHeight = screenDimension.getHeight();
		double screenWidth = screenDimension.getWidth();
		if (dimensionWidth > screenWidth) {
			dimensionWidth = screenWidth * widthRatio;
		}
		if (dimensionHeight > screenHeight) {
			dimensionHeight = screenHeight * heightRatio;
		}
		Dimension adjustedDimension = new Dimension((int) (dimensionWidth + xMargin),
				(int) (dimensionHeight + yMargin));
		return adjustedDimension;
	}

	public static String getAsString(final Dimension dimension, final Level level) {
		return getAsString(dimension, null, level);
	}

	public static String getAsString(final Dimension dimension, final String title,
			final Level level) {
		Level myLevel = level;
		if (myLevel == null) {
			myLevel = Level.INFO;
		}
		StringBuilder sb = new StringBuilder(SystemUtils.getCallerClassAndMethodName());
		sb.append(" ");
		if (title != null) {
			sb.append(title);
		}
		sb.append(StringUtils.concat(" Width: ", String.valueOf(dimension.getWidth()), " Height: ",
				String.valueOf(dimension.getHeight())));
		if (logger.isLoggable(myLevel)) {
			logger.info(sb.toString());
		}
		return sb.toString();
	}

	public static Dimension getScreenDimension() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	public static int getScreenHeight() {
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		return (int) screenDimension.getHeight();
	}

	public static Dimension getScreenSizePercentage(final int percents) {
		int newWidth = (getScreenWidth() * percents) / OzConstants.INT_100;
		int newHeight = (getScreenHeight() * percents) / OzConstants.INT_100;
		return new Dimension(newWidth, newHeight);
	}

	public static int getScreenWidth() {
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		return (int) screenDimension.getWidth();
	}
}