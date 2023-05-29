package oz.jdir.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import oz.jdir.JdirParameters;
import oz.jdir.fileoperations.FileOperationTypesEnum;
import oz.jdir.fileoperations.FileOperationsEnum;

public class JdirToolBar extends JToolBar {
	private static final double BUTTON_SIZE_FACTOR = 0.025;

	private static final double TOOLBAR_SIZE_FACTOR = 0.015;

	private static final Color ORANGE_COLOR = new Color(250, 150, 50);

	private static final Color GRREN_COLOR = new Color(0, 100, 0);

	private static JButton CCButton;

	private static JButton rebasePrepButton;
	private Logger logger = Logger.getLogger(this.getClass().toString());

	JdirToolBar(final ActionListener dirFrameActionListener) {

		Dimension screenSize;
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension buttonDimension = new Dimension((int) (screenSize.width * BUTTON_SIZE_FACTOR),
				(int) (screenSize.height * BUTTON_SIZE_FACTOR));
		Dimension rigidAreaDimension = new Dimension(
				(int) (screenSize.width * TOOLBAR_SIZE_FACTOR), 0);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JButton allButton = new TBButton("All", Color.blue, "Show all files", buttonDimension,
				this, dirFrameActionListener);
		allButton.setActionCommand(GuiComponentsEnum.ShowAll.toString());
		if (JdirParameters.getNumberOfPanes() > 1) {
			JButton equalButton = new TBButton(" = ", GRREN_COLOR, "Show equal files",
					buttonDimension, this, dirFrameActionListener);
			equalButton.setActionCommand(GuiComponentsEnum.ShowEqual.toString());
			JButton diffButton = new TBButton(" != ", Color.darkGray, "Show different files",
					buttonDimension, this, dirFrameActionListener);
			diffButton.setActionCommand(GuiComponentsEnum.ShowDiff.toString());
		}
		this.add(Box.createRigidArea(rigidAreaDimension));
		JButton opButton;
		for (FileOperationsEnum fileOperation1 : FileOperationsEnum.values()) {
			logger.finest(fileOperation1.toString() + ": "
					+ String.valueOf(fileOperation1.isEnabled()));
			if (fileOperation1.isEnabled()
					&& fileOperation1.getoperationType() == FileOperationTypesEnum.twoSidedFileOperation
					&& JdirParameters.getNumberOfPanes() >= fileOperation1.getRequiredSides()) {
				opButton = new TBButton(fileOperation1.toString(), Color.blue,
						fileOperation1.getDescription(), buttonDimension, this,
						dirFrameActionListener);
				opButton.setActionCommand(fileOperation1.toString());
				fileOperation1.setJButton(opButton);
			}
		}
		this.add(Box.createRigidArea(rigidAreaDimension));
		for (FileOperationsEnum fileOperation1 : FileOperationsEnum.values()) {
			if (fileOperation1.isEnabled()
					&& fileOperation1.getoperationType() == FileOperationTypesEnum.allFilesOperation
					&& JdirParameters.getNumberOfPanes() >= fileOperation1.getRequiredSides()) {
				opButton = new TBButton(fileOperation1.toString(), Color.DARK_GRAY,
						fileOperation1.getDescription(), buttonDimension, this,
						dirFrameActionListener);
				opButton.setActionCommand(fileOperation1.toString());
				fileOperation1.setJButton(opButton);
			}
		}

		this.add(Box.createRigidArea(rigidAreaDimension));
		JButton refreshButton = new TBButton("Refresh", Color.green,
				"Reread all directory information", buttonDimension, this, dirFrameActionListener);
		refreshButton.setActionCommand(GuiComponentsEnum.Refresh.toString());
		KeyStroke refreshKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0, true);
		refreshButton.registerKeyboardAction(dirFrameActionListener, "Refresh", refreshKeyStroke,
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		this.add(Box.createRigidArea(rigidAreaDimension));
		if (JdirParameters.getNumberOfPanes() > 1) {
			JButton swapButton = new TBButton("Swap", Color.orange,
					"Swap source and destination panes", buttonDimension, this,
					dirFrameActionListener);
			swapButton.setActionCommand(GuiComponentsEnum.Swap.toString());
			this.add(Box.createRigidArea(rigidAreaDimension));
		}
		//
		this.add(Box.createRigidArea(rigidAreaDimension));
		JButton helpButton = new TBButton("Help!", Color.magenta, "Usage instructions",
				buttonDimension, this, dirFrameActionListener);
		helpButton.setActionCommand(GuiComponentsEnum.Usage.toString());
	}

	/**
	 * @return CCButton
	 */
	public static JButton getCCButton() {
		return CCButton;
	}

	public static final JButton getRebasePrepButton() {
		return rebasePrepButton;
	}
}