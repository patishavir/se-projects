package oz.sqlrunner.gui.jlabels;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import oz.infra.font.FontUtils;

abstract class StatusBarJLabel extends JLabel implements Observer {
	StatusBarJLabel() {
		setFont(FontUtils.ARIAL_BOLD_14);
		setForeground(Color.BLUE);
		setBackground(Color.LIGHT_GRAY);
		setOpaque(true);
		// setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	}

	abstract public void update(final Observable observable, final Object object);
}
