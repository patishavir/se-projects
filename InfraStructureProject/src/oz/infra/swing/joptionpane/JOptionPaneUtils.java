package oz.infra.swing.joptionpane;

import java.awt.Component;
import java.awt.Font;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import oz.infra.logging.jul.JulUtils;
import oz.infra.varargs.VarArgsUtils;

public class JOptionPaneUtils {
	private static Logger logger = JulUtils.getLogger();

	public static int getMultipleInputs(final JPanel myPanel, final String title) {
		int result = JOptionPane.showConfirmDialog(null, myPanel, title, JOptionPane.OK_CANCEL_OPTION);
		return result;
	}

	public static void setJOptionPaneMessageFont(final Font font) {
		UIManager.put("OptionPane.messageFont", font);
	}

	public static String showInputDialog(final Object message) {
		String userInput = JOptionPane.showInputDialog(null, message);
		return userInput;
	}

	public static void showListMessageDialog(final String[] items, final String title,
			final Integer... visibleRowCount) {
		final int defaultVisibleRows = 20;
		int visibleRows = VarArgsUtils.getMyArg(defaultVisibleRows, visibleRowCount);
		visibleRows = Math.min(items.length, visibleRows);
		JList list = new JList(items); // data has type Object[]
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(visibleRows);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		JOptionPane.showMessageDialog(null, listScroller, title, JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showMessageDialog(final Component parentComponent, final Object message, final String title,
			final int messageType) {
		JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
	}

	public static String showPasswordDialog(final String message, final String title) {
		JPanel panel = new JPanel();
		JLabel enterPasswordLabel = new JLabel(message);
		JPasswordField passwordField = new JPasswordField(20);
		panel.add(enterPasswordLabel);
		panel.add(passwordField);
		String[] options = new String[] { "OK", "Cancel" };
		passwordField.requestFocusInWindow();
		int option = JOptionPane.showOptionDialog(null, panel, title, JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, options[1]);
		String password = null;
		if (option == 0) // pressing OK button
		{
			char[] passwordCharArray = passwordField.getPassword();
			password = new String(passwordCharArray);
			logger.finest("Your password is: " + password);
		}
		return password;
	}
}
