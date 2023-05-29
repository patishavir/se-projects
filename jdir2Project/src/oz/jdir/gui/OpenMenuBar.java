package oz.jdir.gui;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.dimension.DimensionUtils;

public class OpenMenuBar extends JMenuBar {
	private static Logger logger = JulUtils.getLogger();

	private JMenu jmFile = new JMenu("File");

	OpenMenuBar(final ActionListener actionListener) {
		addJMenuItem(OpenOperationsEnum.Update, actionListener);
		jmFile.addSeparator();
		addJMenuItem(OpenOperationsEnum.Exit, actionListener);
		add(jmFile);
		Dimension preferedSize = new Dimension(DimensionUtils.getScreenWidth(), DimensionUtils.getScreenHeight() / 50);
		setPreferredSize(preferedSize);
	}

	private final JMenuItem addJMenuItem(final OpenOperationsEnum openOperationsEnum,
			final ActionListener actionListener) {
		JMenuItem jMenuItem = new JMenuItem(openOperationsEnum.name());
		jMenuItem.addActionListener(actionListener);
		jMenuItem.setActionCommand(openOperationsEnum.name());
		logger.finest("jMenuItem.getText(): " + jMenuItem.getText());
		jmFile.add(jMenuItem);
		return jMenuItem;
	}
}