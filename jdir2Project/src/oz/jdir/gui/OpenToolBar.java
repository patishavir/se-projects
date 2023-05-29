package oz.jdir.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JToolBar;

import oz.infra.swing.dimension.DimensionUtils;

public class OpenToolBar extends JToolBar {
	private static final double BUTTON_SIZE_FACTOR = 0.030;
	private static final double TOOLBAR_SIZE_FACTOR = 0.015;

	private Logger logger = Logger.getLogger(this.getClass().toString());

	OpenToolBar(final ActionListener actionListener) {

		Dimension buttonDimension = new Dimension(
				(int) (DimensionUtils.getScreenWidth() * BUTTON_SIZE_FACTOR),
				(int) (DimensionUtils.getScreenHeight() * BUTTON_SIZE_FACTOR));
		Dimension rigidAreaDimension = new Dimension(
				(int) (DimensionUtils.getScreenWidth() * TOOLBAR_SIZE_FACTOR), 0);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		this.add(Box.createRigidArea(rigidAreaDimension));
		JButton updateButton = new TBButton(OpenOperationsEnum.Update.toString(), Color.blue,
				OpenOperationsEnum.Update.toString(), buttonDimension, this, actionListener);
		updateButton.setActionCommand(OpenOperationsEnum.Update.toString());
		// updateButton.setIcon(SwingUtils.createImageIcon(
		// "/oz/infra/icons/Save.jpg", OpenOperationsEnum.Update
		// .toString()));
		this.add(Box.createRigidArea(rigidAreaDimension));

		JButton exitButton = new TBButton(OpenOperationsEnum.Exit.toString(), Color.blue,
				OpenOperationsEnum.Exit.toString(), buttonDimension, this, actionListener);
		exitButton.setActionCommand(OpenOperationsEnum.Exit.toString());
		// exitButton.setIcon(SwingUtils
		// .createImageIcon("/oz/infra/icons/Close.gif",
		// OpenOperationsEnum.Exit.toString()));
		this.add(Box.createRigidArea(rigidAreaDimension));

	}
}