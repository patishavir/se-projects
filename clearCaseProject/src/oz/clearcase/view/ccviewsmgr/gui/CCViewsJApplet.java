package oz.clearcase.view.ccviewsmgr.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.logging.Logger;

import javax.swing.JApplet;
import javax.swing.JPanel;

import oz.clearcase.view.ccviewsmgr.listeners.CCViewsMenuBarActionListener;

public class CCViewsJApplet extends JApplet {
	private JPanel statusAreaJPanel;
	private CCViewsJTableJPanel viewJTableJPanel;
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public CCViewsJApplet() {
		logger.finest("CCViewJApplet constructor");
		CCViewsMenuBarActionListener ccViewManagerMenuBarActionListener = new CCViewsMenuBarActionListener();
		setJMenuBar(new CCViewsMenuBar(ccViewManagerMenuBarActionListener));
		statusAreaJPanel = CCViewStatusAreaJPanel.getStatusArea();
		viewJTableJPanel = new CCViewsJTableJPanel();
		GridBagLayout gridbag = new GridBagLayout();
		getContentPane().setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 0.99;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.insets = new Insets(0, 0, 0, 0);
		gridbag.setConstraints(viewJTableJPanel, c);
		getContentPane().add(viewJTableJPanel);
		c.weightx = 1.0;
		c.weighty = 0.01;
		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(statusAreaJPanel, c);
		getContentPane().add(statusAreaJPanel);
	}

	public final void init() {
		logger.finest("Applet init method invoked! ");
		new CCViewsJApplet();
	}
}