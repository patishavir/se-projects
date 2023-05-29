package oz.infra.swing.layout.gridbaglayout;

import java.awt.GridBagConstraints;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class GridBagLayoutUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void printGridBagConstraints(final GridBagConstraints gridBagConstraints) {
		StringBuilder sb = new StringBuilder();
		sb.append("x=" + String.valueOf(gridBagConstraints.gridx));
		sb.append("y" + String.valueOf(gridBagConstraints.gridy));
		logger.info(sb.toString());
	}
}
