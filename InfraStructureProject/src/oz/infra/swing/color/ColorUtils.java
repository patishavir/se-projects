/*
 * Created on 22/07/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package oz.infra.swing.color;

import java.awt.Color;
import java.util.HashMap;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

/**
 * @author Oded
 */
public final class ColorUtils {
	// public static final Color LIGHT_BLUE = new Color(180, 240, 255);
	// public static final Color LIGHT_YELLOW = new Color(255, 255, 150);

	private static Logger logger = JulUtils.getLogger();
	static HashMap<String, Color> StringColorHashMap = new HashMap<String, Color>();
	static {
		logger = JulUtils.getLogger();
		StringColorHashMap.put("BLUE", Color.BLUE);
		StringColorHashMap.put("GRAY", Color.GRAY);
		StringColorHashMap.put("BLACK", Color.BLACK);
		StringColorHashMap.put("DARK_GRAY", Color.DARK_GRAY);
		StringColorHashMap.put("LIGHT_GRAY", Color.LIGHT_GRAY);
		StringColorHashMap.put("GREEN", Color.GREEN);
		StringColorHashMap.put("MAGENTA", Color.MAGENTA);
		StringColorHashMap.put("PINK", Color.PINK);
		StringColorHashMap.put("RED", Color.RED);
		StringColorHashMap.put("WHITE", Color.WHITE);
		StringColorHashMap.put("YELLOW", Color.YELLOW);
		StringColorHashMap.put("CYAN", Color.CYAN);
		StringColorHashMap.put("ORANGE", Color.ORANGE);
	}

	private ColorUtils() {
		super();
	}

	public static Color getColor(final String colorString) {
		Color outputColor = null;
		String[] rgbStringArray = colorString.split(",");
		if (rgbStringArray.length == 3) {
			// process rgb color
			outputColor = new Color(Integer.parseInt(rgbStringArray[0]),
					Integer.parseInt(rgbStringArray[1]), Integer.parseInt(rgbStringArray[2]));
		} else {
			outputColor = StringColorHashMap.get(colorString.toUpperCase());
		}
		if (outputColor == null) {
			logger.warning(colorString + ": Invalid color String parameter!");
		} else {
			logger.finer(colorString + ": " + outputColor.toString());
		}
		return outputColor;
	}
}