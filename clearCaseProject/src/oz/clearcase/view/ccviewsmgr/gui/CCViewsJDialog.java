package oz.clearcase.view.ccviewsmgr.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import oz.clearcase.view.ccviewsmgr.CCViewsParameters;
import oz.clearcase.view.ccviewsmgr.listeners.CCViewsJDialogActionListener;
import oz.clearcase.view.ccviewsmgr.viewoperations.EViewOperation;

public class CCViewsJDialog extends JDialog {
	private static CCViewsJDialog cCViewsJDialog = null;
	private ButtonGroup group = new ButtonGroup();

	public CCViewsJDialog() {
		cCViewsJDialog = this;
		int myNumButtons = 0;
		EViewOperation[] viewOperations = EViewOperation.values();

		if (CCViewsParameters.isEnableAdminOperations()) {
			myNumButtons = viewOperations.length;
		} else {
			for (EViewOperation viewOperation1 : viewOperations) {
				if (!viewOperation1.isAdmininstratorOeration()) {
					myNumButtons++;
				}
			}
		}
		final JRadioButton[] radioButtons = new JRadioButton[myNumButtons];
		final JButton procButton;
		this.setModal(true);
		final Font lbFont = new Font("Arial", Font.BOLD, 18);
		final Font rbFont = new Font("Arial", Font.BOLD, 14);
		for (int i = 0; i < myNumButtons; i++) {
			if (CCViewsParameters.isEnableAdminOperations()
					|| !viewOperations[i].isAdmininstratorOeration()) {
				radioButtons[i] = new JRadioButton(viewOperations[i].getDescription());
				radioButtons[i].setActionCommand(viewOperations[i].toString());
				radioButtons[i].setFont(rbFont);
				group.add(radioButtons[i]);
			}
		}
		procButton = new JButton("Proceed!");
		procButton.setFont(lbFont);
		procButton.setForeground(Color.blue);
		procButton.addActionListener(new CCViewsJDialogActionListener(this));
		/*
		 * Action listner
		 */
		Box box = new Box(BoxLayout.Y_AXIS);
		JLabel label = new JLabel("   Make your selection:");
		label.setFont(lbFont);
		label.setForeground(Color.blue);
		Box.createRigidArea(new Dimension(20, 0));
		box.add(label);
		for (int i = 0; i < myNumButtons; i++) {
			box.add(radioButtons[i]);
		}
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(box, BorderLayout.NORTH);
		cp.add(procButton, BorderLayout.CENTER);
		this.pack();
		CCViewsJTableJPanel viewJTableJPanel = CCViewsJTableJPanel.getViewJTableJPanel();
		int i = viewJTableJPanel.getBounds().x
				+ (viewJTableJPanel.getBounds().width - this.getSize().width) / 2;
		int j = viewJTableJPanel.getBounds().y
				+ (viewJTableJPanel.getBounds().height - this.getSize().height) / 2;
		this.setLocation(i, j);
	}

	public ButtonGroup getGroup() {
		return group;
	}

	public static final void setCCViewsJDialog(CCViewsJDialog viewsJDialog) {
		cCViewsJDialog = viewsJDialog;
	}

	public static final CCViewsJDialog getCCViewsJDialog() {
		return cCViewsJDialog;
	}
}