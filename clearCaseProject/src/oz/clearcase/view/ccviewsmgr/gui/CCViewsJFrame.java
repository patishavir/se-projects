package oz.clearcase.view.ccviewsmgr.gui;

import java.io.File;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import oz.clearcase.infra.ClearCaseClass;
import oz.clearcase.infra.ClearCaseUtils;
import oz.infra.logging.jul.JulUtils;

public class CCViewsJFrame extends JFrame {
	private static CCViewsJFrame ccViewsJFrame = null;
	private static final Logger logger = JulUtils.getLogger();

	public CCViewsJFrame() {
		ClearCaseClass ccc = new ClearCaseClass();
		String ClearToolFilePath = ClearCaseUtils.getClearToolPath();
		File ClearToolFile = new File(ClearToolFilePath);
		if (!ClearToolFile.isFile()) {
			JOptionPane.showMessageDialog(this, "Cleartool.exe not found! CCViewManager is terminated.");
			System.exit(-1);
		}
		if (!ccc.getClearCaseHostInfo()) {
			JOptionPane.showMessageDialog(this,
					"ClearCase is not running on this machine! CCViewManager is terminated.");
			System.exit(-1);
		}
		getContentPane().add(new CCViewsJApplet());
		setTitle("Rational ClearCase view manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		ccViewsJFrame = this;
	}

	public static CCViewsJFrame getCcViewsJFrame() {
		return ccViewsJFrame;
	}
}