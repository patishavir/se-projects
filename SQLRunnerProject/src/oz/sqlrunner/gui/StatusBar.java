package oz.sqlrunner.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.container.ContainerUtils;
import oz.sqlrunner.gui.jlabels.ConnectionStatusJLabel;
import oz.sqlrunner.gui.jlabels.DatabaseObjectsCountJLabel;
import oz.sqlrunner.gui.jlabels.OperationFeedBackJLabel;

public class StatusBar extends JPanel {
	private Logger logger = JulUtils.getLogger();
	private static final double STATUS_BAR_HEIGHT_FACTOR = 0.025;
	private ConnectionStatusJLabel connectionStatusJLabel = new ConnectionStatusJLabel();
	private DatabaseObjectsCountJLabel databaseObjectsCountJLabel = new DatabaseObjectsCountJLabel();
	private OperationFeedBackJLabel operationFeedBackJLabel = new OperationFeedBackJLabel();
	private Border loweredbevel = BorderFactory.createLoweredBevelBorder();
	

	StatusBar() {
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		Dimension statusbarDimesion = new Dimension(width,
				(int) (height * STATUS_BAR_HEIGHT_FACTOR));
		setPreferredSize(statusbarDimesion);
		setMinimumSize(statusbarDimesion);
		setBorder(loweredbevel);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		logger.fine("Status width: " + getPreferredSize().width + " height: "
				+ getPreferredSize().height);
		logger.fine("Status location x:" + getLocation().x + " y: " + getLocation().y);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(connectionStatusJLabel);
		add(Box.createRigidArea(new Dimension(20, 0)));
		add(databaseObjectsCountJLabel);
		add(Box.createRigidArea(new Dimension(20, 0)));
		add(operationFeedBackJLabel);

	}

	private void addUpdateLabel(final JLabel jLabel, final String text) {
		logger.finest(text);
		jLabel.setText(text);
		if (!ContainerUtils.isComponentInContainer(this, jLabel)) {
			add(Box.createRigidArea(new Dimension(20, 0)));
			add(jLabel);
		}
	}
}