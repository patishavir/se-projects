package oz.clearcase.find;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.logging.Logger;

import javax.swing.JApplet;
import javax.swing.JOptionPane;

import oz.clearcase.infra.ClearCaseClass;

public class CtFindJApplet extends JApplet {
	private Logger logger = Logger.getLogger(this.getClass().toString());
	private CtFindParametersJPanel ctFindParametersJPanel;

	public CtFindJApplet() {
		logger.finest("ctFindJApplet constructor");
		ClearCaseClass ccc = new ClearCaseClass();
		if (!ClearCaseClass.doesClearToolExeExists()) {
			JOptionPane.showMessageDialog(this, "Cleartool.exe not found! \nctFind is terminated.");
			System.exit(-1);
		}
		if (!ccc.getClearCaseHostInfo()) {
			JOptionPane.showMessageDialog(this,
					"ClearCase is not running on this machine! \nctFind is terminated.");
			System.exit(-1);
		}
		ctFindParametersJPanel = new CtFindParametersJPanel();
		//
		GridBagLayout gridbag = new GridBagLayout();
		getContentPane().setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.01;
		c.weighty = 0.01;
		c.ipadx = 0;
		c.ipady = 0;
		c.insets = new Insets(1, 1, 1, 1);
		getContentPane().add(ctFindParametersJPanel, c);
		//
	}
}