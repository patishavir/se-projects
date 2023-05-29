package oz.infra.swing;

import java.awt.Toolkit;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import oz.infra.logging.jul.JulUtils;

public class DirectoryPathVerifier extends InputVerifier {
	private Logger logger = JulUtils.getLogger();

	public final boolean verify(final JComponent comp) {
		JTextField textField = (JTextField) comp;
		boolean returnValue = true;
		String directoryPath = textField.getText();
		if (directoryPath.trim().length() > 0) {
			returnValue = new File(directoryPath).isDirectory();
			if (!returnValue) {
				Toolkit.getDefaultToolkit().beep();
				String errorMessage = "Invalid input.\n" + textField.getText()
						+ "is not a directory !";
				JOptionPane.showMessageDialog(null, errorMessage);
				logger.warning(errorMessage);
			}
		}
		return returnValue;
	}
}