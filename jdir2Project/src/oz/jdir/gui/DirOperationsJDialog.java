package oz.jdir.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

import oz.jdir.fileoperations.FileOperationTypesEnum;
import oz.jdir.fileoperations.FileOperationsEnum;
import oz.jdir.gui.listeners.ProcButtonActionListener;

public class DirOperationsJDialog extends JDialog {
	private static ButtonGroup group = new ButtonGroup();
	private static JRadioButton[] radioButtons = new JRadioButton[FileOperationsEnum
			.getCount(FileOperationTypesEnum.selectedFilesOperation)];;
	private static final Font rbFont = new Font("Arial", Font.BOLD, 12);
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public DirOperationsJDialog(final DirPanel dirPanel) {
		int i;
		ActionListener procButtonActionListener = new ProcButtonActionListener(dirPanel);
		this.setModal(true);
		Font largeFont = new Font("Arial", Font.BOLD, 14);
		/*
		 * procButton
		 */
		JButton procButton = new JButton("Proceed!");
		procButton.setFont(largeFont);
		procButton.setForeground(Color.blue);
		procButton.setActionCommand("Proceed!");
		procButton.addActionListener(procButtonActionListener);
		KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
		procButton.registerKeyboardAction(procButtonActionListener, "Escape", escapeKeyStroke,
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		//
		Box box = new Box(BoxLayout.Y_AXIS);
		Box ccBox = new Box(BoxLayout.Y_AXIS);
		/*
		 * label
		 */
		JLabel label = new JLabel("   Make your selection:");
		label.setFont(largeFont);
		label.setForeground(Color.blue);
		label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		//
		box.add(label);
		/*
		 * add buttons
		 */
		i = 0;
		for (FileOperationsEnum fileOperation1 : FileOperationsEnum.values()) {
			logger.finest(fileOperation1.toString() + ": "
					+ String.valueOf(fileOperation1.isEnabled()));
			if (fileOperation1.getoperationType() == FileOperationTypesEnum.selectedFilesOperation
					&& fileOperation1.isEnabled()) {
				add1Button(i, fileOperation1, radioButtons, box, rbFont, null);
				i++;
			}
		}

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(box, BorderLayout.NORTH);
		ccBox.add(Box.createRigidArea(new Dimension(0, 5)));
		cp.add(ccBox, BorderLayout.CENTER);
		cp.add(procButton, BorderLayout.SOUTH);
		this.setTitle("Command selection dialog");
		this.pack();
		i = dirPanel.getLocation().x + (dirPanel.getSize().width - this.getSize().width) / 2;
		int j = dirPanel.getLocation().y + (dirPanel.getSize().height - this.getSize().height) / 2;
		this.setLocation(i, j);
	}

	private void add1Button(final int i, final FileOperationsEnum fileOperation1,
			final JRadioButton[] myRadioButtons, final Box box, final Font myFont,
			final Color myBgColor) {
		myRadioButtons[i] = new JRadioButton(fileOperation1.toString());
		myRadioButtons[i].setActionCommand(myRadioButtons[i].getText());
		myRadioButtons[i].setFont(myFont);
		myRadioButtons[i].setBackground(myBgColor);
		group.add(myRadioButtons[i]);
		box.add(myRadioButtons[i]);
		fileOperation1.setJRadioButton(myRadioButtons[i]);

	}

	public final ButtonGroup getGroup() {
		return group;
	}
}