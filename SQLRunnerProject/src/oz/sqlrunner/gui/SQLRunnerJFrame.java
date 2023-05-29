package oz.sqlrunner.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import oz.infra.string.StringUtils;
import oz.infra.swing.container.ContainerUtils;
import oz.sqlrunner.SQLRunnerConnectionParameters;
import oz.sqlrunner.SQLRunnerStaticParameters;
import oz.sqlrunner.gui.listeners.SQLRunnerWindowListener;

public class SQLRunnerJFrame extends JFrame implements Observer {
	private static SQLRunnerJFrame sqlRunnerJFrame;
	private static final int MINIMUM_SIZE_FACTOR = 3;
	private static final String TITLE = "SQL runner";

	public static SQLRunnerJFrame getSqlRunnerJFrame() {
		return sqlRunnerJFrame;
	}

	public SQLRunnerJFrame() {
		sqlRunnerJFrame = this;
		SQLRunnerConnectionParameters sqlRunnerParameters = SQLRunnerStaticParameters
				.getConnectionSqlRunnerParameters();
		setTitle(TITLE);
		Container cp = getContentPane();
		BorderLayout borderLayout = new BorderLayout();
		cp.setLayout(borderLayout);
		SQLRunnerMenuBar sqlRunnerMenuBar = new SQLRunnerMenuBar();
		StatusBar sqlRunnerStatusBar = new StatusBar();
		SQLRunnerToolBar sqlRunnerToolBar = new SQLRunnerToolBar();
		this.setJMenuBar(sqlRunnerMenuBar);
		cp.add(sqlRunnerToolBar, BorderLayout.PAGE_START);
		cp.add(sqlRunnerStatusBar, BorderLayout.PAGE_END);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension minimumSize = new Dimension((int) screenSize.getWidth() / MINIMUM_SIZE_FACTOR,
				(int) screenSize.getHeight() / MINIMUM_SIZE_FACTOR);
		setMinimumSize(minimumSize);
		Dimension jframSize = getSize();
		setLocation((screenSize.width - jframSize.width) / 2, (screenSize.height - jframSize.height) / 2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sqlRunnerParameters.setSqlRunnerJFrame(this);
		add(new SQLRunnerJTabbedPane(), BorderLayout.CENTER);
		addWindowListener(new SQLRunnerWindowListener());
		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		SQLRunnerStaticParameters.getConnectionSqlRunnerParameters().addObserver(this);
	}

	public void update(final Observable observable, final Object object) {
		setTitle(StringUtils.concat(TITLE, " ", object.toString()));
		ContainerUtils.refreshDisplay(this);
	}
}
