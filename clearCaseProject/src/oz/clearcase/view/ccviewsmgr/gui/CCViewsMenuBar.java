package oz.clearcase.view.ccviewsmgr.gui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import oz.clearcase.view.ccviewsmgr.CCViewsParameters;
import oz.clearcase.view.ccviewsmgr.listeners.CCViewsMenuBarKeyListener;

public class CCViewsMenuBar extends JMenuBar {
	private JMenuItem jmiAbout = new JMenuItem("About");
	private JCheckBoxMenuItem jmiDetails = new JCheckBoxMenuItem("Details",
			CCViewsParameters.isShowDetails());
	private JMenuItem jmiRefresh = new JMenuItem("Refresh   F5");
	private JMenuItem jmiOptions = new JMenuItem("Options");
	private JMenuItem jmiExit = new JMenuItem("Exit");
	private JMenu jmFile = new JMenu("File");
	private JMenu jmView = new JMenu("View");
	private JMenu jmHelp = new JMenu("Help");

	CCViewsMenuBar(final ActionListener actionListener) {
		jmFile.add(jmiExit);
		jmHelp.add(jmiAbout);
		jmView.addSeparator();
		jmView.add(jmiDetails);
		jmView.addSeparator();
		jmView.add(jmiRefresh);
		jmView.addSeparator();
		jmView.add(jmiOptions);
		this.add(jmFile);
		this.add(jmView);
		this.add(jmHelp);
		//
		jmiExit.addActionListener(actionListener);
		jmiExit.setActionCommand("Exit");
		jmiDetails.addActionListener(actionListener);
		jmiDetails.setActionCommand("Details");
		jmiOptions.addActionListener(actionListener);
		jmiOptions.setActionCommand("Options");
		jmiRefresh.addActionListener(actionListener);
		jmiRefresh.setActionCommand("Refresh");
		jmiAbout.addActionListener(actionListener);
		jmiAbout.setActionCommand("About");
		// add key Listener
		CCViewsMenuBarKeyListener ccViewsMenuBarKeyListener = new CCViewsMenuBarKeyListener();
		addKeyListener(ccViewsMenuBarKeyListener);
		KeyStroke refreshKeyStoke = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0, true);
		registerKeyboardAction(actionListener, "Refresh", refreshKeyStoke,
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		//
	}
}