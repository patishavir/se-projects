package oz.jdir.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import oz.jdir.JdirInfo;
import oz.jdir.JdirParameters;
import oz.jdir.gui.listeners.DirFrameActionListener;
import oz.jdir.gui.listeners.ScrollBarSynchronizer;

public class DirFrame extends JFrame {
	private static DirFrame dirFrame;
	private Dimension screenSize;
	private JSplitPane jdirSplitPane;
	static final double SPLITPANE_SIZE_FACTOR = 0.5;
	private Logger logger = Logger.getLogger(this.getClass().toString());

	public DirFrame() {

		final String srcDirLabelText = "Source Directory";
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dirFrame = this;
		ActionListener dirFrameActionListener = new DirFrameActionListener();
		logger.finer("Screen width=" + String.valueOf(screenSize.width) + " Screen height="
				+ String.valueOf(screenSize.height));
		JdirParameters.getSd().setDirPanel(
				new DirPanel(srcDirLabelText, JdirParameters.getSd(), screenSize));
		JdirInfo dd = JdirParameters.getDd();
		// Destination dir processing
		final String destDirLabelText = "Destination Directory";
		dd.setDirPanel(new DirPanel(destDirLabelText, dd, screenSize));
		dd.getDirPanel()
				.getDirScrollPane()
				.getVerticalScrollBar()
				.getModel()
				.addChangeListener(
						new ScrollBarSynchronizer(JdirParameters.getSd().getDirPanel()
								.getDirScrollPane().getVerticalScrollBar()));
		JdirParameters
				.getSd()
				.getDirPanel()
				.getDirScrollPane()
				.getVerticalScrollBar()
				.getModel()
				.addChangeListener(
						new ScrollBarSynchronizer(dd.getDirPanel().getDirScrollPane()
								.getVerticalScrollBar()));
		//
		Container cp = this.getContentPane();
		this.setTitle("JDir: Files and directories management");
		this.setJMenuBar(new JdirMenuBar(dirFrameActionListener));
		if (JdirParameters.isShowToolBar()) {
			JdirToolBar toolBar = new JdirToolBar(dirFrameActionListener);
			cp.add(toolBar, BorderLayout.NORTH);
		}
		//
		if (JdirParameters.getDd().isEnabled()) {
			jdirSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			jdirSplitPane.setLeftComponent(JdirParameters.getSd().getDirPanel());
			jdirSplitPane.setRightComponent(dd.getDirPanel());
			jdirSplitPane.setOneTouchExpandable(true);
			jdirSplitPane
					.setDividerLocation(((int) (screenSize.width * SPLITPANE_SIZE_FACTOR) - 2));
			//
			cp.add(jdirSplitPane, BorderLayout.CENTER);
		} else {
			cp.add(JdirParameters.getSd().getDirPanel(), BorderLayout.CENTER);
		}
		// this.setResizable(false);
		this.setSize(screenSize.width, screenSize.height);
		this.pack();
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		JdirParameters.setDirFrame(this);
	}

	public static DirFrame getDirFrame() {
		return dirFrame;
	}

	public final JSplitPane getJdirSplitPane() {
		return jdirSplitPane;
	}
}