package oz.clearcase.view.ccviewsmgr.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import oz.infra.logging.jul.JulUtils;

public class CCViewsJTableJPanel extends JPanel {
	private static JTable viewJTable;
	private static JScrollPane jscrollPaneViewJTable;
	private static CCViewsJTableJPanel viewJTableJPanel;
	private static GridBagLayout gridbag = new GridBagLayout();
	private static GridBagConstraints c = new GridBagConstraints();
	private static final Logger logger = JulUtils.getLogger();

	CCViewsJTableJPanel() {
		super();
		viewJTableJPanel = this;
		setLayout(gridbag);
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.insets = new Insets(0, 0, 0, 0);
		setBorder(null);
		updateCCViewJTable();
	}

	public static void updateCCViewJTable() {
		logger.finest("starting updateCCViewJTable");
		viewJTable = CCViewsJTable.updateViewsJTable();
		if (jscrollPaneViewJTable == null) {
			jscrollPaneViewJTable = new JScrollPane(viewJTable);
			jscrollPaneViewJTable.setWheelScrollingEnabled(true);
			jscrollPaneViewJTable.setBorder(null);
			gridbag.setConstraints(jscrollPaneViewJTable, c);
			viewJTableJPanel.add(jscrollPaneViewJTable);
		} else {
			jscrollPaneViewJTable.setViewportView(viewJTable);
		}
		viewJTableJPanel.revalidate();
		viewJTableJPanel.repaint();
	}

	/**
	 * @return
	 */
	public static CCViewsJTableJPanel getViewJTableJPanel() {
		return viewJTableJPanel;
	}
}