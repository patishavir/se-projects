package oz.clearcase.view.ccviewsmgr.listeners;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;

import oz.clearcase.view.ccviewsmgr.CCViewsInfo;
import oz.clearcase.view.ccviewsmgr.CCViewsParameters;
import oz.clearcase.view.ccviewsmgr.gui.CCViewsJFrame;
import oz.clearcase.view.ccviewsmgr.gui.CCViewsJTableJPanel;
import oz.guigenerator.GuiGeneratorParamsFileProcessor;
import oz.infra.logging.jul.JulUtils;

public class CCViewsMenuBarActionListener implements ActionListener, Observer {
	private GuiGeneratorParamsFileProcessor guiGeneratorParamsFileProcessor = null;
	private static final Logger logger = JulUtils.getLogger();

	public final void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals("About")) {
			JOptionPane
					.showMessageDialog(
							null,
							"CCViewsManager - ClearCase View Management Utility."
									+ "\nBy Oded Zimerman \nodedzimer@yahoo.com\n\nNovember 2004 ",
							"About CCViewsManager", JOptionPane.DEFAULT_OPTION);
		} else if (e.getActionCommand().equals("Exit")) {
			JulUtils.closeHandlers();
			System.exit(0);
		} else if (e.getActionCommand().equals("Details")
				&& ((JCheckBoxMenuItem) e.getSource()).isSelected()) {
			CCViewsParameters.setShowDetails(true);
			CCViewsJTableJPanel.updateCCViewJTable();
		} else if (e.getActionCommand().equals("Details")
				&& !((JCheckBoxMenuItem) e.getSource()).isSelected()) {
			CCViewsParameters.setShowDetails(false);
			CCViewsJTableJPanel.updateCCViewJTable();
		} else if (e.getActionCommand().equals("Refresh")) {
			refresh();
		} else if (e.getActionCommand().equals("Options")) {

			if (guiGeneratorParamsFileProcessor != null) {
				guiGeneratorParamsFileProcessor.getGuiGeneratorWindow()
						.setVisible(true);
			} else {

				InputStream optionsXmlInputStream = this.getClass()
						.getResourceAsStream(
								CCViewsParameters.getOptionsGuiXmlFile());
				if (optionsXmlInputStream == null) {
					logger.warning("load of resource "
							+ CCViewsParameters.getOptionsGuiXmlFile()
							+ " failed.");
				} else {
					guiGeneratorParamsFileProcessor = new GuiGeneratorParamsFileProcessor();
					guiGeneratorParamsFileProcessor.getParamsXmlDocument(
							optionsXmlInputStream, this, null,
							(Frame) CCViewsJFrame.getCcViewsJFrame());
				}
			}
		}
	}

	public static void refresh() {
		CCViewsInfo.setRefresh(true);
		CCViewsJTableJPanel.updateCCViewJTable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(final Observable Observable,
			final Object parametersHashTableObj) {
		Object[] setParamArray = { null };
		logger.finest("start update processing");
		CCViewsParameters ccViewParameters = new CCViewsParameters();
		Hashtable<String, String> parametersHashTable = (Hashtable<String, String>) parametersHashTableObj;
		for (Enumeration<String> enumeration = parametersHashTable.keys(); enumeration
				.hasMoreElements();) {
			String key = enumeration.nextElement();
			String value = parametersHashTable.get(key);
			Class[] klass = { String.class };
			String methodName = "set" + key;
			logger.finest(methodName);
			try {
				Method myMethod = CCViewsParameters.class.getDeclaredMethod(
						methodName, klass);
				setParamArray[0] = value;
				logger.finer(key + "=" + value);
				myMethod.invoke(ccViewParameters, setParamArray);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}