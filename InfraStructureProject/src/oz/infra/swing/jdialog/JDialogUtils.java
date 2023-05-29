package oz.infra.swing.jdialog;

import java.awt.Window;
import java.util.logging.Logger;

import javax.swing.JDialog;

import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public class JDialogUtils {
	private static Logger logger = JulUtils.getLogger();

	public static <W extends Window> JDialog getJDialog(final W component) {
		logger.finest("starting " + SystemUtils.getCurrentMethodName());
		return new JDialog(component);
	}
}
