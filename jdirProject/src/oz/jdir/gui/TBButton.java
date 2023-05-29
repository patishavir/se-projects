package oz.jdir.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

/*
 * class tbButton - toolbar button
 */
class TBButton extends JButton {
	private static Font buttonFont = new Font("Arial", Font.BOLD, 12);

	public TBButton(final String tbButtonText, final Color tbButtonColor,
			final String tbButtonToolTipText, final Dimension tbButtonDimension,
			final JToolBar tbButtonToolBar, final ActionListener l) {
		super(tbButtonText);
		this.setFont(buttonFont);
		this.setForeground(tbButtonColor);
		this.setPreferredSize(tbButtonDimension);
		this.setMinimumSize(tbButtonDimension);
		this.setToolTipText(tbButtonToolTipText);
		this.addActionListener(l);
		tbButtonToolBar.add(this);
	}
}