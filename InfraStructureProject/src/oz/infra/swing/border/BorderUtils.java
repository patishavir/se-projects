package oz.infra.swing.border;

import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import oz.infra.logging.jul.JulUtils;

public class BorderUtils {
	private static Logger logger = JulUtils.getLogger();

	public static Border getCompoundBorder() {
		Border raisedBevel = BorderFactory.createRaisedBevelBorder();
		Border loweredBevel = BorderFactory.createLoweredBevelBorder();
		Border compound = BorderFactory.createCompoundBorder(raisedBevel, loweredBevel);
		return compound;
	}
}
