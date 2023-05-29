package oz.infra.swing.point;

import java.awt.Point;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class PointUtils {
	private static Logger logger = JulUtils.getLogger();

	public static String asString(final Point point, final String... title) {
		StringBuilder sb = new StringBuilder();
		if (title.length > 0) {
			sb.append(title[0]);
		}
		sb.append("X: " + String.valueOf(point.getX()) + " Y: " + String.valueOf(point.getY()));
		String pointString = sb.toString();
		logger.finest(pointString);
		return pointString;
	}
}
