package joptionpane;

import java.awt.Font;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import oz.infra.logging.jul.JulUtils;

public class JOptionPaneDemonstrationLocalized {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(String[] argv) {

		Font unicodeFont = new Font("LucidaSans", Font.PLAIN, 12);

		ResourceBundle bundle = ResourceBundle.getBundle("JOptionPaneResources",
				Locale.getDefault());
		logger.info(Locale.getDefault().toString());
		if (bundle == null)
			System.exit(1);

		String[] textMessages = new String[3];
		textMessages[0] = bundle.getString("Yes");
		textMessages[1] = bundle.getString("No");
		textMessages[2] = bundle.getString("Cancel");

		JOptionPane jop = new JOptionPane(bundle.getString("MessageText"),
				JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION, null, textMessages);
		JDialog jopDialog = jop.createDialog(null, bundle.getString("TitleText"));
		jop.setFont(unicodeFont);
		jopDialog.setVisible(true);
		Object userSelection = jop.getValue();
		// NOTE: The return value returned by the above statement is an int
		System.exit(0);
	}
}
