package oz.sqlrunner.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import oz.infra.font.FontUtils;

final class ToolBarButton extends JButton {
	private static Font buttonFont = FontUtils.ARIAL_BOLD_12;
	private static Border blackline = BorderFactory.createLineBorder(Color.black);

	public ToolBarButton(final String tbButtonText, final String actionCommand,
			final Color tbButtonColor, final String tbButtonToolTipText,
			final Dimension tbButtonDimension, final JToolBar toolBar,
			final ActionListener actionListener, final Dimension rigidAreaDimension) {
		super(" " + tbButtonText + " ");
		setFont(buttonFont);
		setForeground(tbButtonColor);
		setPreferredSize(tbButtonDimension);
		setMinimumSize(tbButtonDimension);
		setToolTipText(tbButtonToolTipText);
		addActionListener(actionListener);
		setActionCommand(actionCommand);
		setBorder(blackline);
		toolBar.add(this);
		toolBar.add(Box.createRigidArea(rigidAreaDimension));
	}

}
