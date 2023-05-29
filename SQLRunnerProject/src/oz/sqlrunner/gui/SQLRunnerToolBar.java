package oz.sqlrunner.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.gui.enums.SQLRunnerGuiComponentsEnum;
import oz.sqlrunner.gui.listeners.JTabbedPaneChangeListener;
import oz.sqlrunner.gui.listeners.SQLRunnerActionListener;

public class SQLRunnerToolBar extends JToolBar implements Observer {
	private static final double BUTTON_SIZE_FACTOR = 0.025;
	private static final double TOOLBAR_HEIGHT_FACTOR = 0.017;
	private static Border blackline = BorderFactory.createLineBorder(Color.black);
	private JButton connectButton = null;
	private JButton disConnectButton = null;
	private JButton filterButton = null;
	private JButton runButton = null;
	private JButton newSQLStatementButton = null;
	private Logger logger = Logger.getLogger(this.getClass().toString());

	SQLRunnerToolBar() {
		setBorder(blackline);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		logger.fine("Screen width: " + screenWidth + " height: " + screenHeight);
		Dimension toolBarDimension = new Dimension(screenWidth,
				(int) (screenHeight * BUTTON_SIZE_FACTOR));
		setPreferredSize(toolBarDimension);
		setMaximumSize(toolBarDimension);
		logger.fine("Toolbar width: " + getPreferredSize().width + " height: "
				+ getPreferredSize().height);
		Dimension buttonDimension = new Dimension((int) (screenHeight * BUTTON_SIZE_FACTOR),
				(int) (screenWidth * BUTTON_SIZE_FACTOR));
		Dimension rigidAreaDimension = new Dimension((int) (screenWidth * TOOLBAR_HEIGHT_FACTOR), 0);
		BoxLayout boxLayout = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(boxLayout);
		SQLRunnerActionListener sqlRunnerActionListener = new SQLRunnerActionListener();

		connectButton = new ToolBarButton(SQLRunnerGuiComponentsEnum.Connect.getDisplayName(),
				SQLRunnerGuiComponentsEnum.Connect.toString(), Color.blue,
				SQLRunnerGuiComponentsEnum.Connect.getDisplayName(), buttonDimension, this,
				sqlRunnerActionListener, rigidAreaDimension);

		disConnectButton = new ToolBarButton(
				SQLRunnerGuiComponentsEnum.DisConnect.getDisplayName(),
				SQLRunnerGuiComponentsEnum.DisConnect.toString(), Color.blue,
				SQLRunnerGuiComponentsEnum.DisConnect.getDisplayName(), buttonDimension, this,
				sqlRunnerActionListener, rigidAreaDimension);
		disConnectButton.setEnabled(false);

		filterButton = new ToolBarButton(SQLRunnerGuiComponentsEnum.Filter.getDisplayName(),
				SQLRunnerGuiComponentsEnum.Filter.toString(), Color.blue,
				SQLRunnerGuiComponentsEnum.Filter.getDisplayName(), buttonDimension, this,
				sqlRunnerActionListener, rigidAreaDimension);
		filterButton.setEnabled(false);

		newSQLStatementButton = new ToolBarButton(
				SQLRunnerGuiComponentsEnum.NewSqlStatement.getDisplayName(),
				SQLRunnerGuiComponentsEnum.NewSqlStatement.toString(), Color.blue,
				SQLRunnerGuiComponentsEnum.NewSqlStatement.getDisplayName(), buttonDimension, this,
				sqlRunnerActionListener, rigidAreaDimension);
		newSQLStatementButton.setEnabled(false);

		runButton = new ToolBarButton(SQLRunnerGuiComponentsEnum.Run.getDisplayName(),
				SQLRunnerGuiComponentsEnum.Run.toString(), Color.blue,
				SQLRunnerGuiComponentsEnum.Run.getDisplayName(), buttonDimension, this,
				sqlRunnerActionListener, rigidAreaDimension);
		runButton.setEnabled(false);

		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		sqlRunnerParameters.setSqlRunnerToolBar(this);
		sqlRunnerParameters.addObserver(this);
	}

	public void update(final Observable observable, final Object object) {
		logger.fine("Update processing has started ... ");
		if (observable instanceof SQLRunnerConnectionParameters && object instanceof String) {
			String message = object.toString();
			if (message.equals(SQLRunnerConnectionParameters.NOT_CONNECTED)) {
				connectButton.setEnabled(true);
				disConnectButton.setEnabled(false);
				filterButton.setEnabled(false);
				newSQLStatementButton.setEnabled(false);
			} else {
				connectButton.setEnabled(false);
				disConnectButton.setEnabled(true);
				filterButton.setEnabled(true);
				newSQLStatementButton.setEnabled(true);
			}
		} else if (observable instanceof JTabbedPaneChangeListener && object instanceof JTabbedPane) {
			JTabbedPane jTabbedPane = (JTabbedPane) object;
			int selectedIndex = jTabbedPane.getSelectedIndex();
			runButton.setEnabled(selectedIndex > 0);
		}
	}
}
