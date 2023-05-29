package oz.jdir.gui;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import oz.jdir.JdirParameters;
import oz.jdir.fileoperations.FileOperationTypesEnum;
import oz.jdir.fileoperations.FileOperationsEnum;

public class JdirMenuBar extends JMenuBar {
	private static JMenuItem jmiRebasePrep;

	private static JMenuItem jmiRefresh;

	private ActionListener jdirActionlistener;

	JdirMenuBar(final ActionListener jdirActionlistenerP) {
		this.jdirActionlistener = jdirActionlistenerP;
		JMenuItem jmiShowAll;
		JMenuItem jmiUsage;
		JMenuItem jmiAbout;
		JMenuItem jmiExit;
		JMenu jmFile = new JMenu("File"), jmView = new JMenu("View"), jmHelp = new JMenu("Help");
		jmiUsage = new JMenuItem(GuiComponentsEnum.Usage.toString());
		jmiAbout = new JMenuItem(GuiComponentsEnum.About.toString());
		jmiExit = new JMenuItem(GuiComponentsEnum.Exit.toString());
		jmiShowAll = new JMenuItem("Show all files");
		JMenuItem jmiOptions = new JMenuItem(GuiComponentsEnum.Options.toString());
		jmiRefresh = new JMenuItem(GuiComponentsEnum.Refresh.toString());
		//
		JMenuItem jmiOpenSourceDir = new JMenuItem("Open Source directory");
		setActionCommandaddActionListener(jmiOpenSourceDir,
				GuiComponentsEnum.OpenSourceDir.toString());
		jmFile.add(jmiOpenSourceDir);
		//
		if (JdirParameters.getNumberOfPanes() > 1) {
			JMenuItem jmiOpenDestDir = new JMenuItem("Open destination directory");
			jmFile.add(jmiOpenDestDir);
			setActionCommandaddActionListener(jmiOpenDestDir,
					GuiComponentsEnum.OpenDestinationDir.toString());
		}
		jmFile.addSeparator();
		boolean menuItemsAdded = false;
		for (FileOperationsEnum fileOperation1 : FileOperationsEnum.values()) {
			if (fileOperation1.isEnabled()
					&& JdirParameters.getNumberOfPanes() >= fileOperation1.getRequiredSides()
					&& fileOperation1.getoperationType() == FileOperationTypesEnum.twoSidedFileOperation) {
				JMenuItem jmi = new JMenuItem(fileOperation1.toString());
				jmi.setToolTipText(fileOperation1.getDescription());
				jmFile.add(jmi);
				jmi.addActionListener(jdirActionlistener);
				jmi.setActionCommand(fileOperation1.toString());
				menuItemsAdded = true;
				fileOperation1.setJMenuItem(jmi);
			}
		}
		if (menuItemsAdded) {
			jmFile.addSeparator();
		}
		menuItemsAdded = false;
		for (FileOperationsEnum fileOperation1 : FileOperationsEnum.values()) {
			if (fileOperation1.isEnabled()
					&& JdirParameters.getNumberOfPanes() >= fileOperation1.getRequiredSides()
					&& fileOperation1.getoperationType() == FileOperationTypesEnum.allFilesOperation) {
				JMenuItem jmi = new JMenuItem(fileOperation1.toString());
				jmi.setToolTipText(fileOperation1.getDescription());
				jmFile.add(jmi);
				jmi.addActionListener(jdirActionlistener);
				jmi.setActionCommand(fileOperation1.toString());
				fileOperation1.setJMenuItem(jmi);
				menuItemsAdded = true;
			}
		}

		if (menuItemsAdded) {
			jmFile.addSeparator();
		}

		jmFile.add(jmiExit);
		//
		jmView.add(jmiShowAll);
		if (JdirParameters.getNumberOfPanes() > 1) {
			JMenuItem jmiSwap = new JMenuItem("Swap panes");
			JMenuItem jmiShowEqual = new JMenuItem("Show equal files");
			JMenuItem jmiShowDiff = new JMenuItem("Show different files");
			jmView.add(jmiShowEqual);
			jmView.add(jmiShowDiff);
			jmView.add(jmiSwap);
			setActionCommandaddActionListener(jmiShowEqual, GuiComponentsEnum.ShowEqual.toString());
			setActionCommandaddActionListener(jmiShowDiff, GuiComponentsEnum.ShowDiff.toString());
			setActionCommandaddActionListener(jmiSwap, GuiComponentsEnum.Swap.toString());
		}
		jmView.addSeparator();
		jmView.add(jmiRefresh);
		jmView.addSeparator();
		jmView.add(jmiOptions);
		jmHelp.add(jmiUsage);
		jmHelp.add(jmiAbout);
		this.add(jmFile);
		this.add(jmView);
		this.add(jmHelp);
		setActionCommandaddActionListener(jmiShowAll, GuiComponentsEnum.ShowAll.toString());
		setActionCommandaddActionListener(jmiRefresh, GuiComponentsEnum.Refresh.toString());
		setActionCommandaddActionListener(jmiOptions, GuiComponentsEnum.Options.toString());
		setActionCommandaddActionListener(jmiExit, GuiComponentsEnum.Exit.toString());
		setActionCommandaddActionListener(jmiUsage, GuiComponentsEnum.Usage.toString());
		setActionCommandaddActionListener(jmiAbout, GuiComponentsEnum.About.toString());
	}

	private final void setActionCommandaddActionListener(final JMenuItem jMenuItem,
			final String actionCommandString) {
		jMenuItem.addActionListener(jdirActionlistener);
		jMenuItem.setActionCommand(actionCommandString);
	}

	public static JMenuItem getJmiRefresh() {
		return jmiRefresh;
	}

	public static final JMenuItem getJmiRebasePrep() {
		return jmiRebasePrep;
	}
}