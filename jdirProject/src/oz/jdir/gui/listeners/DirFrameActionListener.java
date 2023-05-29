package oz.jdir.gui.listeners;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import oz.clearcase.infra.ClearCaseClass;
import oz.guigenerator.GuiGeneratorParamsFileProcessor;
import oz.infra.io.FileUtils;
import oz.jdir.ClearCaseIntegration;
import oz.jdir.DirMatchTable;
import oz.jdir.JdirInfo;
import oz.jdir.JdirParameters;
import oz.jdir.MultipleFileOperation;
import oz.jdir.ShowOptionsEnum;
import oz.jdir.gui.DateJDialog;
import oz.jdir.gui.DirFrame;
import oz.jdir.gui.DirPanel;
import oz.jdir.gui.GuiComponentsEnum;
import oz.jdir.gui.JdirMenuBar;
import oz.jdir.gui.JdirToolBar;

public class DirFrameActionListener implements ActionListener, Observer {

	private Logger logger = Logger.getLogger(this.getClass().toString());
	private GuiGeneratorParamsFileProcessor guiGeneratorParamsFileProcessor = null;

	public void actionPerformed(final ActionEvent e) {
		ClearCaseClass ccc;
		String actionCommandString = e.getActionCommand();
		logger.finest("actionCommandString: " + actionCommandString);
		try {
			GuiComponentsEnum guiComponent = GuiComponentsEnum.valueOf(actionCommandString);
			switch (guiComponent) {
			case Usage:
				JOptionPane.showMessageDialog(null, "JDir: Files and directories management."
						+ "\nSupports ClearCase integration.\n"
						+ "\nWritten by Oded Zimerman \nApril 2006" + "\n\nUsage Instructions:",
						"JDir Usage Instructions", JOptionPane.DEFAULT_OPTION);
				break;
			case About:
				InputStream versionInfoInputStream = this.getClass().getResourceAsStream(
						JdirParameters.getVersionInfoFilePath());
				String versionInfoString = FileUtils.readInputStream(versionInfoInputStream);
				logger.finest(versionInfoString);
				JOptionPane.showMessageDialog(null, "JDir: Files and directories management."
						+ "\nSupports ClearCase integration.\n" + "\nWritten by Oded Zimerman \n"
						+ versionInfoString, "About JDir", JOptionPane.DEFAULT_OPTION);
				break;
			case Exit: //
				System.exit(0);
				break;
			case DateAll:
				if (!(JdirParameters.getSd().getMatchTableEntries() > 0)) {
					JOptionPane.showMessageDialog(null, "No files to process. Request ignored");
					return;
				}
				if (JdirParameters.getSd().getDirPanel().getDateJDialog() == null) {
					JdirParameters.getSd().getDirPanel()
							.setDateJDialog(new DateJDialog(JdirParameters.getSd().getDirPanel()));
				}
				JdirParameters.getSd().getDirPanel().getDateJDialog().setVisible(true);
				MultipleFileOperation.getMultipleFileOperation().processMultipleFileOperation(
						JdirParameters.getSd(), JdirParameters.getDd(), null, actionCommandString);
				break;
			case Swap: //
				String srcDirLabelText = JdirParameters.getSd().getDirPanel().getDirLabel()
						.getText();
				String destDirLabelText = JdirParameters.getDd().getDirPanel().getDirLabel()
						.getText();
				JdirParameters.getSd().getDirPanel().getDirLabel().setText(srcDirLabelText);
				JdirParameters.getDd().getDirPanel().getDirLabel().setText(destDirLabelText);
				//
				JdirInfo temp = JdirParameters.getSd();
				JdirParameters.setSd(JdirParameters.getDd());
				JdirParameters.setDd(temp);
				//
				DirFrame dirFrame = DirFrame.getDirFrame();
				dirFrame.getJdirSplitPane().setLeftComponent(JdirParameters.getSd().getDirPanel());
				dirFrame.getJdirSplitPane().setRightComponent(JdirParameters.getDd().getDirPanel());
				break;
			case Refresh:
				logger.fine("Starting RefreshJmi actionlistener processing");
				DirMatchTable.getDirMatchTable().buidMatchTable(true);
				logger.fine("End of RefreshJmi actionlistener processing");
				JdirParameters.getSd().getDirPanel().getDirScrollPane().revalidate();
				JdirParameters.getDd().getDirPanel().getDirScrollPane().revalidate();
				JdirParameters.getSd().getDirPanel().getDirScrollPane().repaint();
				JdirParameters.getDd().getDirPanel().getDirScrollPane().repaint();
				break;
			case EnableCC:
				ccc = new ClearCaseClass();
				JdirParameters.getSd().getDirPanel().setDirJDialog(null);
				JdirParameters.getDd().getDirPanel().setDirJDialog(null);
				ClearCaseIntegration clearCaseIntegration = new ClearCaseIntegration();
				if (JdirParameters.isClearCaseIntegrationEnabled()) {
					/*
					 * Disable ClearCase integration
					 */
					clearCaseIntegration.disableClearCaseIntegration(ccc);
					if (JdirToolBar.getCCButton() != null) {
						JdirToolBar.getCCButton().setText(ClearCaseIntegration.getENABLECC_TEXT());
					}
					if (JdirToolBar.getRebasePrepButton() != null) {
						JdirToolBar.getRebasePrepButton().setVisible(false);
					}
					JdirMenuBar.getJmiEnableCC().setText(ClearCaseIntegration.getENABLECC_TEXT());
					JOptionPane.showMessageDialog(null,
							"ClearCase integration has been disabled !\n");
				}
				/*
				 * enable ClearCase integration processing
				 */
				else {
					String errorMessage = clearCaseIntegration.enableClearCaseIntegration(ccc);
					if (errorMessage != null) {
						JdirToolBar.getCCButton().setText(ClearCaseIntegration.getENABLECC_TEXT());
						JdirMenuBar.getJmiEnableCC().setText(
								ClearCaseIntegration.getENABLECC_TEXT());
						JOptionPane.showMessageDialog(null, errorMessage);
						JdirToolBar.getCCButton().setText(ClearCaseIntegration.getENABLECC_TEXT());
						JdirMenuBar.getJmiEnableCC().setText(
								ClearCaseIntegration.getENABLECC_TEXT());
						JdirParameters.disableClearCaseIntegration();
						return;
					} else {
						JdirToolBar.getCCButton().setText(ClearCaseIntegration.getDISABLECC_TEXT());
						JdirToolBar.getRebasePrepButton().setVisible(true);
						JdirToolBar.getRebasePrepButton().setVisible(true);
						JdirMenuBar.getJmiEnableCC().setText(
								ClearCaseIntegration.getDISABLECC_TEXT());
					}
				}
				break;
			case ShowAll: //
				JdirParameters.setShowOption(ShowOptionsEnum.All);
				showFiles();
				break;
			case ShowDiff:
				JdirParameters.setShowOption(ShowOptionsEnum.Diff);
				showFiles();
				break;
			case ShowEqual:
				JdirParameters.setShowOption(ShowOptionsEnum.Equal);
				showFiles();
				break;
			case browse:
				DirPanel dirPannel = (DirPanel) ((JButton) e.getSource()).getParent();
				getDirectory(dirPannel);
				break;
			case OpenSourceDir:
				getDirectory(JdirParameters.getSd().getDirPanel());
				break;
			case OpenDestinationDir: // Open source Dir
				getDirectory(JdirParameters.getDd().getDirPanel());
				break;
			case Options:
				if (guiGeneratorParamsFileProcessor != null) {
					guiGeneratorParamsFileProcessor.getGuiGeneratorWindow().setVisible(true);
				} else {
					InputStream optionsXmlInputStream = this.getClass().getResourceAsStream(
							JdirParameters.getOptionsGuiXmlFile());
					if (optionsXmlInputStream == null) {
						logger.warning("load of resource " + JdirParameters.getOptionsGuiXmlFile()
								+ " failed.");
					}
					guiGeneratorParamsFileProcessor = new GuiGeneratorParamsFileProcessor();
					JdirParameters.getDirFrame();
					guiGeneratorParamsFileProcessor.getParamsXmlDocument(optionsXmlInputStream,
							this, null, (Frame) JdirParameters.getDirFrame());
				}
				break;
			default:
				logger.severe("Oops!, " + e.getActionCommand() + " is an invalid entry.");
			}
		} catch (IllegalArgumentException ex) {
			logger.info("actionCommandString for fileOperationFactory: " + actionCommandString);

			MultipleFileOperation.getMultipleFileOperation().processMultipleFileOperation(
					JdirParameters.getSd(), JdirParameters.getDd(), null, actionCommandString);
			return;

		}
	}

	void getDirectory(final DirPanel dirPanelP) {
		String currentDirectoryPathPath = dirPanelP.getDirectoryPathJtextField().getText();
		JFileChooser fc = null;
		if (new File(currentDirectoryPathPath).isDirectory()) {
			fc = new JFileChooser(currentDirectoryPathPath);
		} else {
			fc = new JFileChooser();
		}
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(dirPanelP);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String dirAbsolutePath = fc.getSelectedFile().getAbsolutePath();
			dirPanelP.getDirectoryPathJtextField().setText(dirAbsolutePath);
			dirPanelP.getJdirInfo().setDirName(dirAbsolutePath);
		}
	}

	final void showFiles() {
		if (JdirParameters.getShowOption() != null) {
			int numberOfPanes = JdirParameters.getNumberOfPanes();
			JdirInfo sd = JdirParameters.getSd();
			JdirInfo dd = JdirParameters.getDd();
			String currentSdDirName = sd.getDirName();
			String currentDdDirName = dd.getDirName();
			logger.finest("currentSdDirName: " + currentSdDirName + " currentDdDirName: "
					+ currentDdDirName);
			if ((currentSdDirName == null || currentSdDirName.trim().length() == 0)
					&& (numberOfPanes == 2 && (currentDdDirName == null || currentDdDirName.trim()
							.length() == 0))) {
				JOptionPane.showMessageDialog(null, "No directory specified!",
						"No input specified", JOptionPane.ERROR_MESSAGE);
				sd.getDirPanel().getDirectoryPathJtextField().requestFocus();
				return;
			}
			if (numberOfPanes == 2 && sd.getDirName().equals(dd.getDirName())) {
				JOptionPane.showMessageDialog(null,
						"Soource and destination directories should be different !",
						"Bad input parameters", JOptionPane.ERROR_MESSAGE);
				dd.getDirPanel().getDirectoryPathJtextField().requestFocus();
				return;
			}
			boolean rebuildFileList = (currentSdDirName == null)
					|| (!currentSdDirName.equals(sd.getBuiltDirName()))
					|| (!currentDdDirName.equals(dd.getBuiltDirName()));
			DirMatchTable.getDirMatchTable().buidMatchTable(rebuildFileList);
			JdirMenuBar.getJmiRefresh().addActionListener(this);

		}
		return;
	}

	public void update(final Observable Observable, final Object parametersHashTableObj) {
		JdirParameters.update(Observable, parametersHashTableObj);
	}
}