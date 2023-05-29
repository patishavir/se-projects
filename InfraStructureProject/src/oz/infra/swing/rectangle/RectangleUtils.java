package oz.infra.swing.rectangle;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class RectangleUtils {
	private static Logger logger = JulUtils.getLogger();

	public static String asString(final Rectangle rectangle, final String... title) {
		StringBuilder sb = new StringBuilder();
		if (title.length > 0) {
			sb.append(title[0]);
			sb.append(" ");
		}
		sb.append(" X: " + String.valueOf(rectangle.getX()) + " Y: "
				+ String.valueOf(rectangle.getY()));
		sb.append(" Width: " + String.valueOf(rectangle.getWidth()) + " tHeight: "
				+ String.valueOf(rectangle.getHeight()));
		String rectangleString = sb.toString();
		logger.finest(rectangleString);
		return rectangleString;
	}

	public static Rectangle adjustRectangle(final Rectangle rectangle, final int offsetX,
			final int offsetY) {
		return new Rectangle(rectangle.x + offsetX, rectangle.y + offsetY, rectangle.width,
				rectangle.height);
	}

	public static boolean isPointWithinRectangle(final Rectangle rectangle, final Point point) {
		boolean response = (point.x > rectangle.x) && (point.y > rectangle.y)
				&& (point.x < (rectangle.x + rectangle.width))
				&& (point.y < (rectangle.y + rectangle.height));
		return response;
	}
}