package oz.jdir.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import oz.infra.swing.DirectoryPathVerifier;
import oz.jdir.JdirInfo;
import oz.jdir.JdirParameters;
import oz.jdir.MultipleFileOperation;
import oz.jdir.abstractfileoperations.AbstractFileOperation;
import oz.jdir.gui.listeners.DirDirectoryFocusListener;
import oz.jdir.gui.listeners.DirFrameActionListener;

/*
 * class DirPanel -set up a directory panel
 */
public class DirPanel extends JPanel implements Observer {
	private JdirInfo jdirInfo;
	private JLabel dirLabel;
	private JTextField directoryPathJtextField;
	private JButton dirBrowse;
	private JScrollPane dirScrollPane;
	private DirJTable dirJTable;
	private JPanel statusBar;
	private JLabel statusLabel;
	private DirOperationsJDialog dirJDialog;
	private JDialog dateJDialog;
	private Logger logger = Logger.getLogger(this.getClass().toString());
	final static double PANEL_HEIGHT_FACTOR = 0.87;
	final static double STATUSBAR_HEIGHT_FACTOR = 0.020;
	final static double SPLITPANE_SIZE_FACTOR = 0.5;

	public DirPanel(String dirPanelTitle, JdirInfo jdirInfoP, Dimension screenSize) {
		logger.finest("Entering DirPanel constructor");
		this.jdirInfo = jdirInfoP;
		String dirName = jdirInfo.getDirName();
		// this.dirFrame = dirFrame;
		int panelHeight = (int) (screenSize.height * PANEL_HEIGHT_FACTOR);
		this.setPreferredSize(new Dimension((screenSize.width / 2 - 2), panelHeight));
		this.setMinimumSize(new Dimension(0, panelHeight));
		this.setMinimumSize(new Dimension((screenSize.width / 2 - 2), panelHeight));
		this.setBorder(new MatteBorder(2, 1, 2, 2, Color.orange));
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridbag);
		c.fill = GridBagConstraints.BOTH;
		dirLabel = new JLabel(dirPanelTitle, SwingConstants.LEFT);
		dirLabel.setHorizontalAlignment(SwingConstants.LEFT);
		int y = 0;
		c.gridx = 0;
		c.gridy = y;
		c.weightx = 0.1;
		c.weighty = 0.025;
		gridbag.setConstraints(dirLabel, c);
		add(dirLabel);
		//
		directoryPathJtextField = new JTextField(20);
		directoryPathJtextField.setBorder(new MatteBorder(2, 2, 2, 1, Color.orange));
		directoryPathJtextField.addFocusListener(new DirDirectoryFocusListener(jdirInfo));
		directoryPathJtextField.setInputVerifier(new DirectoryPathVerifier());
		c.gridx = 0;
		c.gridy = ++y;
		c.weightx = 0.9;
		c.weighty = 0.02;
		gridbag.setConstraints(directoryPathJtextField, c);
		directoryPathJtextField.setText(dirName);
		directoryPathJtextField.setFont(new Font("Arial", Font.BOLD, 12));
		add(directoryPathJtextField);
		dirBrowse = new JButton(GuiComponentsEnum.browse.toString());
		dirBrowse.setActionCommand(GuiComponentsEnum.browse.toString());
		dirBrowse.setPreferredSize(new Dimension(40, 8));
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		dirBrowse.setBorder(raisedbevel);
		c.gridx = 1;
		c.gridy = y;
		c.weightx = 0.1;
		c.weighty = 0.02;
		gridbag.setConstraints(dirBrowse, c);
		add(dirBrowse, c);
		ActionListener dirFrameActionListener = new DirFrameActionListener();
		dirBrowse.addActionListener(dirFrameActionListener);
		dirScrollPane = new JScrollPane();
		c.gridx = 0;
		c.gridy = ++y;
		c.weightx = 1.0;
		c.weighty = 0.95;
		c.ipady = 400;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.SOUTH;
		gridbag.setConstraints(dirScrollPane, c);
		add(dirScrollPane);
		statusBar = new JPanel();
		statusBar.setPreferredSize(new Dimension(
				(int) (screenSize.width * SPLITPANE_SIZE_FACTOR) - 4,
				(int) (screenSize.height * STATUSBAR_HEIGHT_FACTOR)));
		statusBar.setMinimumSize(new Dimension(
				(int) (screenSize.width * SPLITPANE_SIZE_FACTOR) - 4,
				(int) (screenSize.height * STATUSBAR_HEIGHT_FACTOR)));
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusBar.setLayout(new BorderLayout());
		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(JLabel.LEFT);
		statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
		statusBar.add(statusLabel, BorderLayout.WEST);
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 1.0;
		c.weighty = 0.02;
		c.ipady = 0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.SOUTH;
		gridbag.setConstraints(statusBar, c);
		add(statusBar);
		jdirInfo.addObserver(this);

	}

	public final void update(final Observable observable, final Object parameterObject) {
		if ((parameterObject != null) && (parameterObject instanceof String)
				&& ((String) parameterObject).equals(JdirInfo.getDIRMATCHTABLE_PROCESSING_DONE())) {
			JdirInfo jdirInfo = (JdirInfo) observable;
			logger.finest("Starting update ...");
			if (dirJTable == null) {
				dirJTable = new DirJTable(jdirInfo, this);
				dirScrollPane.setViewportView(dirJTable);
			} else {
				dirJTable.clearSelection();
				dirJTable.revalidate();
				dirJTable.repaint();
			}
			String displayType = null;
			switch (JdirParameters.getShowOption()) {
			case All:
				displayType = "all files";
				break;
			case Diff:
				displayType = "diffrent files";
				break;
			case Equal:
				displayType = "equal files";
				break;
			default:
				logger.warning("buidMatchTable: invalid allDiffEqual value.");
				return;
			}
			int counters[] = jdirInfo.getDirsFilesCounters();
			statusLabel.setText("Current display - " + displayType + ": "
					+ String.valueOf(counters[0]) + " directories, " + String.valueOf(counters[1])
					+ " files");
			logger.finest(statusLabel.getText());
		} else if (parameterObject != null
				&& parameterObject instanceof String
				&& (observable instanceof MultipleFileOperation || observable instanceof AbstractFileOperation)) {
			statusLabel.setText((String) parameterObject);
		}
	}

	public final DirOperationsJDialog getDirJDialog() {
		return dirJDialog;
	}

	public final void setDirJDialog(final DirOperationsJDialog dialog) {
		dirJDialog = dialog;
	}

	public final JLabel getDirLabel() {
		return dirLabel;
	}

	public final JTextField getDirectoryPathJtextField() {
		return directoryPathJtextField;
	}

	public final DirJTable getDirJTable() {
		return dirJTable;
	}

	public final JDialog getDateJDialog() {
		return dateJDialog;
	}

	public final void setDateJDialog(final JDialog dateJDialogP) {
		this.dateJDialog = dateJDialogP;
	}

	public final JdirInfo getJdirInfo() {
		return jdirInfo;
	}

	public final JScrollPane getDirScrollPane() {
		return dirScrollPane;
	}
}