package oz.jdir.fileoperations.userfeedback;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import oz.jdir.gui.DirFrame;

public class FileOperationJDialog extends JDialog implements ActionListener {
	// DirFrame dirFrame;
	private JLabel msgLabel;
	private UserFeedbackEnum userFeedBack;
	private Dimension screenSize;
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public FileOperationJDialog() {
		super(DirFrame.getDirFrame());
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setModal(true);
		Font fmFont = new Font("Arial", Font.BOLD, 14);
		msgLabel = new JLabel(" ", SwingConstants.LEFT);
		msgLabel.setHorizontalAlignment(SwingConstants.LEFT);
		msgLabel.setFont(fmFont);
		msgLabel.setForeground(Color.red);
		Container cp = getContentPane();
		cp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 10, 0, 10);
		c.gridx = 0;
		c.gridy = 0;
		cp.add(msgLabel, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(15, 0, 5, 0);
		c.gridy = 1;
		cp.add(buildButtonsJPanel(), c);
		pack();
	}

	/*
	 * buildButtonsJPanel
	 */
	private JPanel buildButtonsJPanel() {
		String[] textArray = { "Yes", "No", "Yes to all", "Cancel" };
		String[] actionCommandArray = { UserFeedbackEnum.YES.toString(),
				UserFeedbackEnum.NO.toString(), UserFeedbackEnum.YESTOALL.toString(),
				UserFeedbackEnum.CANCEL.toString() };
		JButton[] jButtonArray = new JButton[textArray.length];

		JPanel jpanel = new JPanel();
		jpanel.setLayout(new GridBagLayout());
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 1.0;
		c.insets = new Insets(1, 1, 1, 1);
		for (int i = 0; i < textArray.length; i++) {
			jButtonArray[i] = new JButton(textArray[i]);
			c.gridx = i;
			jpanel.add(jButtonArray[i], c);
			jButtonArray[i].setActionCommand(actionCommandArray[i]);
			jButtonArray[i].addActionListener(this);
		}
		jpanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		return jpanel;
	}

	/*
	 * showDialog
	 */
	public final void showYesNoCancelDialog(final String dialogTitle, final String dialogMsg) {
		userFeedBack = UserFeedbackEnum.CANCEL;
		this.setTitle(dialogTitle);
		msgLabel.setText(dialogMsg + " ? ");
		this.pack();
		Dimension dimDiag = this.getSize();
		this.setLocation((screenSize.width - dimDiag.width) / 2,
				(screenSize.height - dimDiag.height) / 2);
		logger.finest("dirFrame height " + String.valueOf(screenSize.height) + " fmDialog  height "
				+ String.valueOf(dimDiag.height));
		logger.finest("dirFrame width " + String.valueOf(screenSize.width) + " fmDialog  width "
				+ String.valueOf(dimDiag.width));
		this.setVisible(true);
		return;
	}

	public final void showMessageDialog(final String message) {
		JOptionPane.showMessageDialog(null, message, "", JOptionPane.DEFAULT_OPTION);
		return;
	}

	public final String showInputDialog(final String dialogMsg, final String dialogTitle) {
		return JOptionPane.showInputDialog(null, dialogMsg, dialogTitle,
				JOptionPane.QUESTION_MESSAGE);
	}

	public final String showInputDialog(final String dialogMsg, final String dialogTitle,
			final String inittialValue) {
		return (String) JOptionPane.showInputDialog(null, dialogMsg, dialogTitle,
				JOptionPane.QUESTION_MESSAGE, null, null, inittialValue);
	}

	public void actionPerformed(final ActionEvent e) {
		setUserFeedBack(UserFeedbackEnum.valueOf(((JButton) e.getSource()).getActionCommand()));
		setVisible(false);
	}

	public final UserFeedbackEnum getUserFeedBack() {
		return userFeedBack;
	}

	public final void setUserFeedBack(final UserFeedbackEnum userFeedBackP) {
		this.userFeedBack = userFeedBackP;
	}
}